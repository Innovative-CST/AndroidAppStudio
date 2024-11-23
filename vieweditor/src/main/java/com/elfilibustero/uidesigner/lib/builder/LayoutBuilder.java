package com.elfilibustero.uidesigner.lib.builder;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import com.elfilibustero.uidesigner.lib.utils.Constants;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class LayoutBuilder {

	private final Context context;
	private ViewGroup container;
	private Document doc;
	private Element rootElement;

	private Map<View, Map<String, Object>> viewMap;

	private LayoutBuilder(Context context, ViewGroup container) {
		this.context = context;
		this.container = container;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {

		}
	}

	public static LayoutBuilder with(ViewGroup container) {
		return new LayoutBuilder(container.getContext(), container);
	}

	public void build(Map<View, Map<String, Object>> viewMap) {
		this.viewMap = viewMap;
	}

	public String generate() {
		if (container.getChildCount() == 0 || viewMap.isEmpty()) {
			return "";
		}

		try {
			var root = container.getChildAt(0);
			var className = getClassName(root);
			rootElement = doc.createElement(className);
			rootElement.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android");
			if (hasNamespace(viewMap, "app:"))
				rootElement.setAttribute("xmlns:app", "http://schemas.android.com/apk/res-auto");
			if (hasNamespace(viewMap, "tools:"))
				rootElement.setAttribute("xmlns:tools", "http://schemas.android.com/tools");
			setAttributes(rootElement, viewMap.get(root));
			doc.appendChild(rootElement);
			if (root instanceof ViewGroup viewGroup) {
				generate(viewGroup, rootElement);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.getBuffer().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void generate(ViewGroup group, Element parentElement) {
		IntStream.range(0, group.getChildCount())
				.mapToObj(group::getChildAt)
				.forEach(
						childView -> {
							boolean isNotDefaultChild = viewMap.containsKey(childView);
							Element childElement = doc.createElement(getClassName(childView));
							setAttributes(childElement, viewMap.get(childView));
							if (isNotDefaultChild)
								parentElement.appendChild(childElement);
							if (childView instanceof ViewGroup viewGroup
									&& !Constants.isExcludedViewGroup(viewGroup)) {
								generate(
										viewGroup,
										isNotDefaultChild ? childElement : parentElement);
							}
						});
	}

	private void setAttributes(Element element, Map<String, Object> attributeMap) {
		if (attributeMap != null) {
			attributeMap.forEach((key, value) -> element.setAttribute(key, value.toString()));
		}
	}

	public static String getClassName(View view) {
		return removeClassPrefix(DynamicViewFactory.getName(view));
	}

	private static String removeClassPrefix(String str) {
		return Arrays.stream(Constants.ANDROID_CLASS_PREFIX)
				.filter(str::startsWith)
				.findFirst()
				.map(prefix -> str.substring(prefix.length()))
				.orElse(str);
	}

	public static boolean hasNamespace(Map<View, Map<String, Object>> viewMap, String namespace) {
		return viewMap.values().stream().anyMatch(innerMap -> hasNamespace(namespace, innerMap));
	}

	public static boolean hasNamespace(String namespace, Map<String, Object> innerMap) {
		return innerMap.keySet().stream().anyMatch(key -> key.startsWith(namespace));
	}
}
