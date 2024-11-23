package com.elfilibustero.uidesigner.lib.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public class ConstantsProperties {
	public static Map<String, Map<String, String>> MAP_ENUM = new HashMap<>();
	public static Map<String, Map<String, Integer>> MAP_FLAG = new HashMap<>();

	public static String[] CONSTRAINT_IDS = new String[] {
			"layout_constraintStart_toStartOf",
			"layout_constraintStart_toEndOf",
			"layout_constraintLeft_toLeftOf",
			"layout_constraintLeft_toRightOf",
			"layout_constraintRight_toRightOf",
			"layout_constraintRight_toLeftOf",
			"layout_constraintEnd_toStartOf",
			"layout_constraintEnd_toEndOf",
			"layout_constraintTop_toTopOf",
			"layout_constraintTop_toBottomOf",
			"layout_constraintBottom_toTopOf",
			"layout_constraintBottom_toBottomOf",
			"layout_constraintBaseline_toBaselineOf"
	};

	public static String[] RELATIVE_IDS = new String[] {
			"layout_alignStart",
			"layout_alignLeft",
			"layout_alignTop",
			"layout_alignEnd",
			"layout_alignBottom",
			"layout_alignBaseline",
			"layout_toStartOf",
			"layout_toLeftOf",
			"layout_toEndOf",
			"layout_toRightOf",
			"layout_above",
			"layout_below"
	};

	public static String[] PROPERTY_RESOURCE = new String[] {
			"background",
			"backgroundTint",
			"tint",
			"textColor",
			"textHintColor",
			"cardBackgroundColor",
			"cardForegroundColor",
			"strokeColor",
			"src",
			"srcCompat",
			"checkedIcon",
			"checkedIconTint"
	};

	public static String[] PROPERTY_FLOAT = new String[] { "alpha", "scaleX", "scaleY", "rotationX", "rotationY" };

	public static String[] PROPERTY_NUMBER = new String[] {
			"layout_weight",
			"weightSum",
			"rotation",
			"lines",
			"min",
			"max",
			"maxLines",
			"minLines",
			"progress",
			"spanCount",
			"itemCount"
	};

	public static String[] PROPERTY_SIZE = new String[] {
			"layout_margin",
			"padding",
			"paddingLeft",
			"paddingStart",
			"paddingRight",
			"paddingEnd",
			"paddingTop",
			"paddingBottom",
			"translationX",
			"translationY",
			"translationZ",
			"layout_marginBottom",
			"layout_marginLeft",
			"layout_marginStart",
			"layout_marginEnd",
			"layout_marginTop",
			"layout_marginRight",
			"elevation",
			"strokeWidth",
			"cardElevation",
			"cardCornerRadius",
			"textSize",
			"layout_height",
			"layout_width"
	};

	public static String[] PROPERTY_STRING = new String[] { "text", "hint", "setTitle", "layoutManager",
			"layout_behavior" };

	static {
		initializeEnumMap("orientation", "vertical", "1", "horizontal", "0");
		String[] layout = new String[] {
				"fill_parent",
				String.valueOf(ViewGroup.LayoutParams.MATCH_PARENT),
				"match_parent",
				String.valueOf(ViewGroup.LayoutParams.MATCH_PARENT),
				"wrap_content",
				String.valueOf(ViewGroup.LayoutParams.WRAP_CONTENT)
		};
		initializeEnumMap("layout_width", layout);
		initializeEnumMap("layout_height", layout);
		initializeEnumMap(
				"typeface",
				"sans",
				"SANS_SERIF",
				"serif",
				"SERIF",
				"monospace",
				"MONOSPACE",
				"normal",
				"DEFAULT");
		initializeEnumMap(
				"scaleType",
				"matrix",
				"MATRIX",
				"fitXY",
				"FIT_XY",
				"fitStart",
				"FIT_START",
				"fitCenter",
				"FIT_CENTER",
				"fitEnd",
				"FIT_END",
				"center",
				"CENTER",
				"centerCrop",
				"CENTER_CROP",
				"centerInside",
				"CENTER_INSIDE");
		initializeEnumMap(
				"visibility",
				"visible",
				String.valueOf(View.VISIBLE),
				"invisible",
				String.valueOf(View.INVISIBLE),
				"gone",
				String.valueOf(View.GONE));

		initializeBooleanMap(
				"layout_centerInParent",
				"layout_centerVertical",
				"layout_centerHorizontal",
				"layout_alignParentStart",
				"layout_alignParentLeft",
				"layout_alignParentTop",
				"layout_alignParentEnd",
				"layout_alignParentRight",
				"layout_alignParentBottom",
				"enabled",
				"clipToPadding",
				"fillViewport",
				"textIsSelectable",
				"singleLine",
				"indeterminate",
				"cardUseCompatPadding",
				"fitsSystemWindows");

		initializeFlagMap(
				"textStyle",
				"normal",
				Typeface.NORMAL,
				"bold",
				Typeface.BOLD,
				"italic",
				Typeface.ITALIC,
				"bold|italic",
				Typeface.BOLD_ITALIC);
		initializeFlagMap(
				"inputType",
				"none",
				InputType.TYPE_NULL,
				"text",
				InputType.TYPE_CLASS_TEXT,
				"textCapCharacters",
				InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
				"textCapWords",
				InputType.TYPE_TEXT_FLAG_CAP_WORDS,
				"textCapSentences",
				InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
				"textAutoCorrect",
				InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
				"textAutoComplete",
				InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE,
				"textMultiLine",
				InputType.TYPE_TEXT_FLAG_MULTI_LINE,
				"textImeMultiLine",
				InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE,
				"textNoSuggestions",
				InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
				"textUri",
				InputType.TYPE_TEXT_VARIATION_URI,
				"textEmailAddress",
				InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
				"textEmailSubject",
				InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT,
				"textShortMessage",
				InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE,
				"textLongMessage",
				InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE,
				"textPersonName",
				InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
				"textPostalAddress",
				InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,
				"textPassword",
				InputType.TYPE_TEXT_VARIATION_PASSWORD,
				"textVisiblePassword",
				InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
				"textWebEditText",
				InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT,
				"textFilter",
				InputType.TYPE_TEXT_VARIATION_FILTER,
				"textPhonetic",
				InputType.TYPE_TEXT_VARIATION_PHONETIC,
				"textWebEmailAddress",
				InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS,
				"textWebPassword",
				InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD,
				"number",
				InputType.TYPE_CLASS_NUMBER,
				"numberSigned",
				InputType.TYPE_NUMBER_FLAG_SIGNED,
				"numberDecimal",
				InputType.TYPE_NUMBER_FLAG_DECIMAL,
				"numberPassword",
				InputType.TYPE_NUMBER_VARIATION_PASSWORD,
				"phone",
				InputType.TYPE_CLASS_PHONE,
				"datetime",
				InputType.TYPE_CLASS_DATETIME,
				"date",
				InputType.TYPE_DATETIME_VARIATION_DATE,
				"time",
				InputType.TYPE_DATETIME_VARIATION_TIME,
				"textEnableTextConversionSuggestions",
				InputType.TYPE_TEXT_FLAG_ENABLE_TEXT_CONVERSION_SUGGESTIONS);

		var gravity = new Object[] {
				"top",
				Gravity.TOP,
				"bottom",
				Gravity.BOTTOM,
				"left",
				Gravity.LEFT,
				"right",
				Gravity.RIGHT,
				"center_vertical",
				Gravity.CENTER_VERTICAL,
				"fill_vertical",
				Gravity.FILL_VERTICAL,
				"center_horizontal",
				Gravity.CENTER_HORIZONTAL,
				"fill_horizontal",
				Gravity.FILL_HORIZONTAL,
				"center",
				Gravity.CENTER,
				"fill",
				Gravity.FILL,
				"clip_vertical",
				Gravity.CLIP_VERTICAL,
				"clip_horizontal",
				Gravity.CLIP_HORIZONTAL,
				"start",
				Gravity.START,
				"end",
				Gravity.END
		};
		initializeFlagMap("gravity", gravity);
		initializeFlagMap("layout_gravity", gravity);
		initializeFlagMap("foregroundGravity", gravity);
		initializeFlagMap("layout_anchorGravity", gravity);
		initializeFlagMap("layout_dodgeInsetEdges", gravity);
		initializeFlagMap("layout_insetEdge", gravity);
	}

	private static void initializeEnumMap(String key, String... values) {
		Map<String, String> map = new HashMap<>();
		MAP_ENUM.put(key, map);
		for (int i = 0; i < values.length; i += 2) {
			map.put(values[i], values[i + 1]);
		}
	}

	private static void initializeBooleanMap(String... keys) {
		Map<String, String> booleanMap = new HashMap<>();
		for (String key : keys) {
			MAP_ENUM.put(key, booleanMap);
		}
		booleanMap.put("true", String.valueOf(true));
		booleanMap.put("false", String.valueOf(false));
	}

	private static void initializeFlagMap(String key, Object... values) {
		Map<String, Integer> map = new HashMap<>();
		MAP_FLAG.put(key, map);
		for (int i = 0; i < values.length; i += 2) {
			map.put((String) values[i], (Integer) values[i + 1]);
		}
	}

	public static boolean containsReference(String reference, Set<String> set) {
		return set.contains(reference);
	}

	public static boolean isPropertyEnum(String reference) {
		return containsReference(reference, MAP_ENUM.keySet());
	}

	public static boolean isPropertyFlag(String reference) {
		return containsReference(reference, MAP_FLAG.keySet());
	}

	public static boolean isPropertySize(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(PROPERTY_SIZE)));
	}

	public static boolean isConstraintProperty(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(CONSTRAINT_IDS)));
	}

	public static boolean isRelativeProperty(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(RELATIVE_IDS)));
	}

	public static boolean isPropertyFloat(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(PROPERTY_FLOAT)));
	}

	public static boolean isPropertyNumber(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(PROPERTY_NUMBER)));
	}

	public static boolean isPropertyString(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(PROPERTY_STRING)));
	}

	public static boolean isPropertyResource(String reference) {
		return containsReference(reference, new HashSet<>(Arrays.asList(PROPERTY_RESOURCE)));
	}
}
