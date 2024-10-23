package com.elfilibustero.uidesigner.lib.handler;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AttributeSetHandler {
	private final Context ctx;
	private Map<View, Map<String, Object>> viewMap = new HashMap<>();
	private final List<Pair<Class<?>, List<String>>> attributes;
	private final List<Pair<Class<?>, List<String>>> parentAttributes;

	public AttributeSetHandler(Context ctx) {
		this.ctx = ctx;
		attributes = getAttributeSet();
		parentAttributes = getParentAttributeSet();
	}

	public void addView(View view, Map<String, Object> defaultAttributes) {
		viewMap.put(view, defaultAttributes);
	}

	public void removeView(View view) {
		viewMap.remove(view);
	}

	public void setViewAttributes(Map<View, Map<String, Object>> viewAttributesMap) {
		viewMap = viewAttributesMap;
	}

	public Map<View, Map<String, Object>> getViewMap() {
		return viewMap;
	}

	public Map<String, Object> get(View view) {
		return viewMap.get(view);
	}

	public void applyAttributesFor(View view) {
		if (view == null)
			return;
		var attributes = viewMap.get(view);
		getAttributesFor(view).stream()
				.filter(attributes::containsKey)
				.forEach(
						attribute -> applyAttributeFor(
								view,
								attribute,
								attributes.get(attribute).toString(),
								attributes));
	}

	public void applyAttributesFor(View view, Map<String, Object> attributeMap) {
		if (attributeMap == null)
			return;
		attributeMap.entrySet().stream()
				.filter(attr -> !attr.getKey().equals("android:id"))
				.forEach(
						attr -> applyAttributeFor(
								view,
								attr.getKey(),
								attr.getValue().toString(),
								attributeMap));
	}

	public void applyAttributeFor(
			View view, String attribute, String value, Map<String, Object> defaultAttributes) {
		var viewAttributes = viewMap.get(view);
		if (viewAttributes == null) {
			return;
		}

		String methodName = PropertiesUtil.getMethodFor(attribute);
		if (methodName == null) {
			return;
		}

		var params = PropertiesUtil.getPropertyValue(attribute, value);
		viewAttributes.put(attribute, value);
		new PropertiesHandler(ctx, view, defaultAttributes).setPropertyFor(methodName, params);
	}

	public boolean hasAttribute(View view, String attr) {
		return viewMap.containsKey(view) && viewMap.get(view).containsKey(attr);
	}

	public Set<String> getAvailableAttributesFor(View target) {
		Set<String> allAttrs = getAttributesFor(target);
		Map<String, Object> attributes = viewMap.get(target);
		allAttrs.removeIf(attributes::containsKey);
		return allAttrs;
	}

	public Set<String> getAttributesFor(View view) {
		Set<String> uniqueAttributes = new HashSet<>();

		Class<?> clazz = (view instanceof DesignerItem item) ? item.getClassType() : view.getClass();
		Class<?> viewParentClazz = View.class.getSuperclass();

		while (clazz != viewParentClazz) {
			var attrs = getAttributesFor(attributes, clazz);
			if (attrs != null) {
				uniqueAttributes.addAll(attrs.second);
			}
			clazz = clazz.getSuperclass();
		}

		var parent = view.getParent();
		if (parent != null) {
			clazz = parent.getClass();

			while (clazz != viewParentClazz) {
				var parentAttrs = getAttributesFor(parentAttributes, clazz);
				if (parentAttrs != null) {
					uniqueAttributes.addAll(parentAttrs.second);
				}
				clazz = clazz.getSuperclass();
			}
		}
		return uniqueAttributes;
	}

	public Pair<Class<?>, List<String>> getAttributesFor(
			List<Pair<Class<?>, List<String>>> attributes, Class clazz) {
		Optional<Pair<Class<?>, List<String>>> matchingClazz = attributes.stream()
				.filter(pair -> pair.first.equals(clazz)).findFirst();
		if (matchingClazz.isPresent()) {
			return matchingClazz.get();
		}
		return null;
	}

	public List<Pair<Class<?>, List<String>>> getAttributeSet() {
		return List.of(
				new Pair<>(View.class, Arrays.asList(Constants.VIEW)),
				new Pair<>(LinearLayout.class, Arrays.asList(Constants.LINEARLAYOUT)),
				new Pair<>(ScrollView.class, Arrays.asList(Constants.SCROLLVIEW)),
				new Pair<>(RecyclerView.class, Arrays.asList(Constants.RECYCLERVIEW)),
				new Pair<>(CardView.class, Arrays.asList(Constants.CARDVIEW)),
				new Pair<>(MaterialCardView.class, Arrays.asList(Constants.MATERIAL_CARDVIEW)),
				new Pair<>(TextView.class, Arrays.asList(Constants.TEXTVIEW)),
				new Pair<>(EditText.class, Arrays.asList(Constants.EDITTEXT)),
				new Pair<>(ImageView.class, Arrays.asList(Constants.IMAGEVIEW)),
				new Pair<>(Button.class, Arrays.asList(Constants.BUTTON)),
				new Pair<>(CompoundButton.class, Arrays.asList(Constants.COMPOUNDBUTTON)),
				new Pair<>(ProgressBar.class, Arrays.asList(Constants.PROGRESSBAR)),
				new Pair<>(FloatingActionButton.class, Arrays.asList(Constants.FAB)));
	}

	public List<Pair<Class<?>, List<String>>> getParentAttributeSet() {
		return List.of(
				new Pair<>(LinearLayout.class, Arrays.asList(Constants.PARENT_LINEARLAYOUT)),
				new Pair<>(FrameLayout.class, Arrays.asList(Constants.PARENT_FRAMELAYOUT)),
				new Pair<>(RelativeLayout.class, Arrays.asList(Constants.PARENT_RELATIVELAYOUT)),
				new Pair<>(
						CoordinatorLayout.class, Arrays.asList(Constants.PARENT_COORDINATORLAYOUT)),
				new Pair<>(
						ConstraintLayout.class, Arrays.asList(Constants.PARENT_CONSTRAINTLAYOUT)));
	}
}
