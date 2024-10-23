package com.elfilibustero.uidesigner.lib.utils;

import android.util.Pair;
import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.icst.android.appstudio.vieweditor.R;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtil {

	public static String getMethodFor(String attr) {
		String attributeName = ResourceFactory.parseReferName(attr, ":");
		if (attr.startsWith("android:")) {
			return getAndroidPropertyMethodFor(attributeName);
		} else if (attr.startsWith("app:")) {
			return getAppPropertyMethodFor(attributeName);
		} else {
			return switch (attr) {
				case "tools:listitem" -> "setListItem";
				case "tools:itemCount" -> "setItemCount";
				default -> null;
			};
		}
	}

	public static String getAndroidPropertyMethodFor(String attr) {
		return switch (attr) {
			case "id" -> "setId";
			case "layout_width" -> "setLayoutWidth";
			case "layout_height" -> "setLayoutHeight";
			case "layout_weight" -> "setWeight";
			case "fitsSystemWindows" -> "setFitsSystemWindows";
			case "layout_centerInParent" -> "setCenterInParent";
			case "layout_centerVertical" -> "setCenterVertical";
			case "layout_centerHorizontal" -> "setCenterHorizontal";
			case "layout_alignStart" -> "setAlignStart";
			case "layout_alignLeft" -> "setAlignLeft";
			case "layout_alignTop" -> "setAlignTop";
			case "layout_alignEnd" -> "setAlignEnd";
			case "layout_alignBottom" -> "setAlignButtom";
			case "layout_alignBaseline" -> "setAlignBaseline";
			case "layout_toStartOf" -> "setToStartOf";
			case "layout_toLeftOf" -> "setToLeftOf";
			case "layout_toEndOf" -> "setToEndOf";
			case "layout_toRightOf" -> "setToRightOf";
			case "layout_above" -> "setAbove";
			case "layout_below" -> "setBelow";
			case "layout_alignParentStart" -> "setAlignParentStart";
			case "layout_alignParentLeft" -> "setAlignParentLeft";
			case "layout_alignParentTop" -> "setAlignParentTop";
			case "layout_alignParentEnd" -> "setAlignParentEnd";
			case "layout_alignParentRight" -> "setAlignParentRight";
			case "layout_alignParentBottom" -> "setAlignParentBottom";
			case "layout_margin" -> "setMargins";
			case "layout_marginStart" -> "setMarginStart";
			case "layout_marginLeft" -> "setMarginLeft";
			case "layout_marginTop" -> "setMarginTop";
			case "layout_marginEnd" -> "setMarginEnd";
			case "layout_marginRight" -> "setMarginRight";
			case "layout_marginBottom" -> "setMarginBottom";
			case "orientation" -> "setOrientation";
			case "gravity" -> "setGravity";
			case "layout_gravity" -> "setLayoutGravity";
			case "elevation" -> "setElevation";
			case "visibility" -> "setVisibility";
			case "enabled" -> "setEnabled";
			case "alpha" -> "setAlpha";
			case "weightSum" -> "setWeightSum";
			case "clipToPadding" -> "setClipToPadding";
			case "contentDescription" -> "setContentDescription";
			case "padding" -> "setPaddings";
			case "paddingStart" -> "setPaddingStart";
			case "paddingLeft" -> "setPaddingLeft";
			case "paddingTop" -> "setPaddingTop";
			case "paddingEnd" -> "setPaddingEnd";
			case "paddingRight" -> "setPaddingRight";
			case "rotation" -> "setRotation";
			case "rotationX" -> "setRotationX";
			case "rotationY" -> "setRotationY";
			case "translationX" -> "setTranslationX";
			case "translationY" -> "setTranslationY";
			case "translationZ" -> "setTranslationZ";
			case "scaleX" -> "setScaleX";
			case "scaleY" -> "setScaleY";
			case "background" -> "setBackground";
			case "backgroundTint" -> "setBackgroundTint";
			case "foreground" -> "setForeground";
			case "fillViewport" -> "setFillViewport";
			case "checked" -> "setChecked";
			case "text" -> "setText";
			case "textColor" -> "setTextColor";
			case "textSize" -> "setTextSize";
			case "textStyle" -> "setTextStyle";
			case "textIsSelectable" -> "setTextSelectable";
			case "hint" -> "setHint";
			case "lines" -> "setLines";
			case "maxLines" -> "setMaxLines";
			case "minLines" -> "setMinLines";
			case "singleLine" -> "setSingleLine";
			case "src" -> "setImage";
			case "scaleType" -> "setScaleType";
			case "tint" -> "setTint";
			case "progress" -> "setProgress";
			case "min" -> "setMin";
			case "max" -> "setMax";
			case "indeterminate" -> "setIndeterminate";
			default -> null;
		};
	}

	public static String getAppPropertyMethodFor(String attr) {
		return switch (attr) {
			case "elevation" -> "setElevation";
			case "srcCompat" -> "setImage";
			case "strokeColor" -> "setCardStrokeColor";
			case "strokeWidth" -> "setCardStrokeWidth";
			case "checkedIcon" -> "setCheckedIcon";
			case "checkedIconTint" -> "setCheckedIconTint";
			case "cardElevation" -> "setCardElevation";
			case "cardCornerRadius" -> "setCardCornerRadius";
			case "cardBackgroundColor" -> "setCardBackgroundColor";
			case "cardForegroundColor" -> "setCardForegroundColor";
			case "cardUseCompatPadding" -> "setCardUseCompatPadding";
			case "layoutManager" -> "setLayoutManager";
			case "layout_behavior" -> "setBehavior";
			case "layout_anchor" -> "setAnchorId";
			case "layout_anchorGravity" -> "setAnchorGravity";
			case "layout_dodgeInsetEdges" -> "setDodgeInsetEdges";
			case "layout_insetEdge" -> "setInsetEdge";
			case "layout_keyline" -> "setKeyline";
			case "layoutInsetTop" -> "setInsetTop";
			case "layoutInsetLeft" -> "setInsetLeft";
			case "layoutInsetRight" -> "setInsetRight";
			case "layoutInsetBottom" -> "setInsetBottom";
			case "layout_constraintStart_toStartOf" -> "setStartToStartOf";
			case "layout_constraintStart_toEndOf" -> "setStartToEndOf";
			case "layout_constraintLeft_toLeftOf" -> "setLeftToLeftOf";
			case "layout_constraintLeft_toRightOf" -> "setLeftToRightOf";
			case "layout_constraintRight_toRightOf" -> "setRightToRightOf";
			case "layout_constraintRight_toLeftOf" -> "setRightToLeftOf";
			case "layout_constraintEnd_toStartOf" -> "setEndToStartOf";
			case "layout_constraintEnd_toEndOf" -> "setEndToEndOf";
			case "layout_constraintTop_toTopOf" -> "setTopToTopOf";
			case "layout_constraintTop_toBottomOf" -> "setTopToBottomOf";
			case "layout_constraintBottom_toTopOf" -> "setBottomToTopOf";
			case "layout_constraintBottom_toBottomOf" -> "setBottomToBottomOf";
			case "layout_constraintBaseline_toBaselineOf" -> "setBaselineToBaselineOf";
			case "layout_constraintVertical_bias" -> "setVerticalBias";
			case "layout_constraintHorizontal_bias" -> "setHorizontalBias";
			case "layout_constraintDimensionRatio" -> "setDimentionRatio";
			case "layout_constraintHorizontal_chainStyle" -> "setHorizontalChainStyle";
			case "layout_constraintVertical_chainStyle" -> "setVerticalChainStyle";
			case "layout_constraintHorizontal_weight" -> "setHorizontalWeight";
			case "layout_constraintVertical_weight" -> "setVerticalWeight";
			case "layout_goneMarginLeft" -> "setGoneMarginLeft";
			case "layout_goneMarginTop" -> "setGoneMarginTop";
			case "layout_goneMarginRight" -> "setGoneMarginRight";
			case "layout_goneMarginBottom" -> "setGoneMarginBottom";
			case "title" -> "setTitle";
			default -> null;
		};
	}

	public static int getIconFor(String attr) {
		return switch (attr) {
			case "id" -> R.drawable.ic_property_id;
			case "layout_width", "strokeWidth" -> R.drawable.ic_property_width;
			case "layout_height" -> R.drawable.ic_property_height;
			case "layout_weight", "weightSum" -> R.drawable.ic_property_weight;
			// case "alpha" -> R.drawable.ic_property_alpha;
			case "rotate" -> R.drawable.ic_property_rotate;
			case "orientation" -> R.drawable.ic_property_orientation;
			case "background" -> R.drawable.ic_property_background;
			case "layout_gravity",
					"foregroundGravity",
					"gravity",
					"layout_anchorGravity",
					"layout_dodgeInsetEdges",
					"layout_insetEdge" ->
				R.drawable.ic_property_gravity;
			case "layout_marginBottom",
					"layout_marginLeft",
					"layout_marginStart",
					"layout_marginEnd",
					"layout_marginTop",
					"layout_marginRight",
					"layout_marging" ->
				R.drawable.ic_property_margin;
			case "padding",
					"paddingLeft",
					"paddingStart",
					"paddingRight",
					"paddingEnd",
					"paddingTop",
					"paddingBottom" ->
				R.drawable.ic_property_padding;
			case "text" -> R.drawable.ic_property_text;
			case "textSize" -> R.drawable.ic_property_text_size;
			case "hint" -> R.drawable.ic_property_hint;
			case "progress" -> R.drawable.ic_property_progress;
			case "indeterminate" -> R.drawable.ic_property_indeterminate;
			case "scaleType" -> R.drawable.ic_property_image_scale;
			case "src", "srcCompat" -> R.drawable.ic_property_src;
			case "translationX", "translationY", "translationZ" -> R.drawable.ic_property_translate;
			case "elevation", "cardElevation" -> R.drawable.ic_property_elevation;
			default -> R.drawable.ic_code;
		};
	}

	public static String getPropertyValue(String attr, String value) {
		String name = ResourceFactory.parseReferName(attr, ":");
		return switch (name) {
			case "scaleType", "typeface", "orientation", "visibility" -> parseEnum(name, value);
			case "layout_gravity",
					"foregroundGravity",
					"gravity",
					"layout_anchorGravity",
					"layout_dodgeInsetEdges",
					"layout_insetEdge",
					"inputType",
					"textStyle" ->
				parseFlag(name, value);
			case "translationX", "translationY", "translationZ", "textSize" -> resolveSize(value);
			case "layout_margin",
					"padding",
					"paddingLeft",
					"paddingStart",
					"paddingRight",
					"paddingEnd",
					"paddingTop",
					"paddingBottom",
					"layout_marginBottom",
					"layout_marginLeft",
					"layout_marginStart",
					"layout_marginEnd",
					"layout_marginTop",
					"layout_marginRight",
					"elevation",
					"cardElevation",
					"strokeWidth",
					"cardCornerRadius",
					"layout_height",
					"layout_width" ->
				resolveDimenSize(name, value);
			default -> value;
		};
	}

	public static String parseEnum(String name, String value) {
		return parseEnum(name, value, value);
	}

	public static String parseEnum(String name, String value, String def) {
		Map<String, String> map = ConstantsProperties.MAP_ENUM.get(name);
		assert map != null;
		if (map.containsKey(value))
			return map.get(value);
		return def;
	}

	public static String parseFlag(String name, String value) {
		Map<String, Integer> map = ConstantsProperties.MAP_FLAG.get(name);
		assert map != null;

		if (!value.contains("|")) {
			return String.valueOf(map.getOrDefault(value, -1));
		}

		int flag = -1;
		String[] flags = value.split("\\|");
		for (String f : flags) {
			if (!f.trim().isEmpty()) {
				Integer v = map.get(f);
				if (v != null) {
					if (flag == -1) {
						flag = v;
					} else {
						flag |= v;
					}
				}
			}
		}
		return String.valueOf(flag);
	}

	private static String resolveSize(String value) {
		var reference = getUnitOrPrefix(value);
		if (reference != null) {
			if (hasDimensionSuffix(value)) {
				var intValue = Integer.parseInt(reference.second);
				return String.valueOf(intValue);
			} else {
				return String.valueOf(value);
			}
		} else {
			return value;
		}
	}

	private static String resolveDimenSize(String name, String value) {
		var reference = getUnitOrPrefix(value);
		if (reference != null) {
			if (hasDimensionSuffix(value)) {
				var intValue = Integer.parseInt(reference.second);
				if (intValue > 0)
					intValue = ResourceFactory.getInstance().getDip(intValue);
				return String.valueOf(intValue);
			} else {
				return String.valueOf(ResourceFactory.getInstance().getRes(value, true));
			}
		} else {
			if (name.equals("layout_width") || name.equals("layout_height")) {
				var va = parseEnum(name, value, null);
				if (va != null) {
					return va;
				}
			}
			return value;
		}
	}

	public static String removeDimensionSuffix(String str) {
		if (!hasDimensionSuffix(str))
			return str;
		String pattern = "^(.+)(dp|sp|px|pt|in|mm)$";
		return str.replaceAll(pattern, "$1");
	}

	public static boolean hasDimensionSuffix(String str) {
		String pattern = "^(.+)dp$|^(.+)sp$|^(.+)px$|^(.+)pt$|^(.+)in$|^(.+)mm$";
		return str.matches(pattern);
	}

	public static Pair<String, String> getUnitOrPrefix(String value) {
		Pattern prefixPattern = Pattern.compile("([@?][^/]+/)(.*)");

		Pattern unitPattern = Pattern.compile("(-?\\d+)(dp|sp|px|mm|pt|in)$");

		Pattern hexColorPattern = Pattern.compile("(#)([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

		Matcher prefixMatcher = prefixPattern.matcher(value);
		if (prefixMatcher.find()) {
			return Pair.create(prefixMatcher.group(1), prefixMatcher.group(2));
		}

		Matcher unitMatcher = unitPattern.matcher(value);
		if (unitMatcher.find()) {
			return Pair.create(unitMatcher.group(2), unitMatcher.group(1));
		}

		Matcher hexColorMatcher = hexColorPattern.matcher(value);
		if (hexColorMatcher.find()) {
			return Pair.create(hexColorMatcher.group(1), hexColorMatcher.group(2));
		}

		return null;
	}

	private static String toIntString(String str) {
		if (str.contains("."))
			return str.substring(0, str.lastIndexOf('.'));
		return str;
	}
}
