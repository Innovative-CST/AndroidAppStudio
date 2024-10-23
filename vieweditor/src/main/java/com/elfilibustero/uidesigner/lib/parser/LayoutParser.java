package com.elfilibustero.uidesigner.lib.parser;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import com.elfilibustero.uidesigner.lib.handler.PropertiesHandler;
import com.elfilibustero.uidesigner.lib.progress.ProgressIndicator;
import com.elfilibustero.uidesigner.lib.progress.ProgressManager;
import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.elfilibustero.uidesigner.lib.tool.ViewIdentifierFactory;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class LayoutParser {

	private Map<View, Map<String, Object>> dump = new HashMap<>();

	private Context context;
	private XmlPullParser parser;

	private DynamicViewFactory viewFactory = DynamicViewFactory.getInstance();
	private ResourceFactory resourceFactory = ResourceFactory.getInstance();
	private ViewIdentifierFactory viewIdentifier;

	private ViewGroup container;

	private Callback callback;

	public interface Callback {
		void onLayoutParsed(Map<View, Map<String, Object>> map);

		void onParsingComplete();
	}

	private LayoutParser(Context context, ViewGroup root) {
		this.context = context;
		container = root;
		ViewIdentifierFactory.init(context);
		viewIdentifier = ViewIdentifierFactory.getInstance();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		} catch (Exception e) {
		}
	}

	public static LayoutParser with(ViewGroup container) {
		return new LayoutParser(container.getContext(), container);
	}

	public void addCallback(Callback callback) {
		this.callback = callback;
	}

	public ViewIdentifierFactory getViewIds() {
		return viewIdentifier;
	}

	public LayoutParser parse(Reader reader) {
		ProgressManager.getInstance()
				.runAsync(
						() -> new Handler(Looper.getMainLooper())
								.post(
										() -> {
											try {
												parser.setInput(reader);
												if (parse()) {
													if (callback != null)
														callback.onLayoutParsed(dump);
												}
												reader.close();
											} catch (XmlPullParserException
													| IOException e) {
											} finally {
												if (callback != null)
													callback.onParsingComplete();
											}
										}),
						indicator -> {
						},
						new ProgressIndicator());

		return this;
	}

	public LayoutParser parse(String xml) {
		parse(new StringReader(xml));
		return this;
	}

	public LayoutParser parse(File path) {
		try {
			parse(new FileReader(path));
		} catch (Exception e) {
		}
		return this;
	}

	private boolean parse() throws XmlPullParserException, IOException {
		List<View> listViews = new ArrayList<>();
		listViews.add(container);
		while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
			switch (parser.getEventType()) {
				case XmlPullParser.START_TAG -> {
					View view;
					String attr = parser.getAttributeValue(null, "style");

					if (attr != null) {
						int attrInt = resourceFactory.getRes(attr, false);
						if (attrInt != -1) {
							view = (View) viewFactory.createView(parser.getName(), attrInt);
						} else {
							view = (View) viewFactory.createView(parser.getName());
						}
					} else {
						view = (View) viewFactory.createView(parser.getName());
					}
					listViews.add(view);

					Map<String, Object> map = new HashMap<>();

					IntStream.range(0, parser.getAttributeCount())
							.filter(i -> !parser.getAttributeName(i).startsWith("xmlns"))
							.forEach(
									i -> map.put(
											parser.getAttributeName(i),
											parser.getAttributeValue(i)));

					dump.put(view, map);
					break;
				}

				case XmlPullParser.END_TAG -> {
					View currentView = null;
					int index = parser.getDepth();
					var view = listViews.get(index);
					if (view != null) {
						var preView = listViews.get(index - 1);
						if (preView instanceof ViewGroup parent) {
							parent.addView(view);
							currentView = view;
							listViews.remove(index);
						}
					}
					break;
				}
			}

			parser.next();
		}
		dump.forEach(
				(view, attributeMap) -> attributeMap.entrySet().stream()
						.filter(entry -> "android:id".equals(entry.getKey()))
						.map(entry -> entry.getValue().toString())
						.forEach(newId -> viewIdentifier.register(view, newId)));

		dump.keySet().forEach(view -> applyAttributes(view, dump.get(view)));
		return true;
	}

	private void applyAttributes(View view, Map<String, Object> attributeMap) {
		attributeMap.entrySet().stream()
				.filter(attr -> !attr.getKey().equals("android:id"))
				.forEach(attr -> applyProperty(view, attr.getKey(), attr.getValue().toString()));
	}

	private void applyProperty(View view, String attr, String value) {
		var methodName = PropertiesUtil.getMethodFor(attr);
		if (methodName == null) {
			return;
		}
		var params = PropertiesUtil.getPropertyValue(attr, value);
		var handler = new PropertiesHandler(context, view, dump.get(view));
		handler.setPropertyFor(methodName, params);
	}
}
