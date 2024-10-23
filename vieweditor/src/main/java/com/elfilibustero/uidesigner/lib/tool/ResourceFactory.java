package com.elfilibustero.uidesigner.lib.tool;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.elfilibustero.uidesigner.enums.ResourceType;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceFactory {

	private static ResourceFactory instance;
	private Context context;

	private Map<String, Object> drawableMap;

	private static final String HEX_COLOR_PATTERN = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

	private ResourceFactory(Context context) {
		this.context = context;
		drawableMap = new HashMap<>();
		parseAndroidResourceDrawable();
	}

	public static void init(Context context) {
		instance = new ResourceFactory(context);
	}

	public static ResourceFactory getInstance() {
		return instance;
	}

	public Object getResource(String value) {
		return getResource(value, false);
	}

	public Object getResource(String value, boolean size) {
		var reference = PropertiesUtil.getUnitOrPrefix(value);
		if (reference != null) {
			return switch (ResourceType.fromPrefix(reference.first)) {
				case ANDROID_ATTR, ATTR, ANDROID_DIMEN, DIMEN, ANDROID_STYLE, STYLE -> getRes(
						value, size);
				case ANDROID_DRAWABLE, DRAWABLE -> getDrawable(value);
				case ANDROID_COLOR, COLOR, HEX_COLOR -> getColor(value);
				case ANDROID_STRING, STRING -> getString(value);
				default -> null;
			};
		}
		return null;
	}

	public String getString(String value) {
		var reference = PropertiesUtil.getUnitOrPrefix(value);
		if (reference != null) {
			return switch (ResourceType.fromPrefix(reference.first)) {
				case ANDROID_STRING -> parseStringResource(value);
				case STRING -> {
					yield value;
				}
				default -> value;
			};
		}
		return value;
	}

	public Drawable getDrawable(String value) {
        var reference = PropertiesUtil.getUnitOrPrefix(value);
        if (reference != null) {
            return switch (ResourceType.fromPrefix(reference.first)) {
                case ANDROID_DRAWABLE -> parseDrawableResource(value);
                case DRAWABLE -> Utils.getDefaultImage();
                case ANDROID_COLOR, COLOR, HEX_COLOR -> new ColorDrawable(getColor(value));
                default -> {
                    yield null;
                }
            };
        }
        return null;
    }

	public int getColor(String value) {
        var reference = PropertiesUtil.getUnitOrPrefix(value);
        if (reference != null) {
            return switch (ResourceType.fromPrefix(reference.first)) {
                case ANDROID_COLOR -> parseColorResource(value);
                case COLOR -> Color.parseColor("#40FFFFFF");
                case HEX_COLOR -> parseColor(value);
                default -> {
                    yield Color.TRANSPARENT;
                }
            };
        }
        return Color.TRANSPARENT;
    }

	public int getLayout(String value) {
		String name;
		if (value.startsWith("@android:layout/")) {
			name = parseReferName(value);
			int resourceId = parseAndroidResource(android.R.layout.class, name);
			return resourceId;
		} else if (value.startsWith("@layout/")) {
			// TODO parse @layout/
			return -1;
		}
		return -1;
	}

	public int getRes(String value, boolean size) {
		int id = getAttr(value);
		if (id == -1) {
			id = getStyle(value);
		}
		if (!size) {
			return id;
		}
		if (id == -1) {
			id = getDimen(value);
		}
		var theme = context.getTheme();
		var typeValue = new TypedValue();
		if (theme.resolveAttribute(id, typeValue, true)) {
			return TypedValue.complexToDimensionPixelSize(
					typeValue.data, context.getResources().getDisplayMetrics());
		}
		return -1;
	}

	public int getStyle(String value) {
		var name = parseReferName(value);
		if (value.startsWith("@android:style/") || value.startsWith("@style/")) {
			return parseAndroidResource(android.R.style.class, name.replace(".", "_"));
		}
		return -1;
	}

	public int getAttr(String value) {
		var name = parseReferName(value);
		if (value.startsWith("?android:attr/") || value.startsWith("@android:attr/")) {
			return parseAndroidResource(android.R.attr.class, name);
		}
		if (value.startsWith("?attr/") || value.startsWith("@attr/")) {
			return parseAndroidResource(com.google.android.material.R.attr.class, name);
		}
		return -1;
	}

	public int getDimen(String value) {
		String name = parseReferName(value);
		if (value.startsWith("@android:dimen/")) {
			return parseAndroidResource(android.R.dimen.class, name);
		}
		return -1;
	}

	public String parseStringResource(String value) {
		String name = parseReferName(value);
		int resourceId = parseAndroidResource(android.R.string.class, name);
		if (resourceId != -1) {
			return context.getString(resourceId);
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	public Drawable parseDrawableResource(String value) {
		String name = parseReferName(value);
		int resourceId = parseAndroidResource(android.R.drawable.class, name);
		if (resourceId != -1) {
			return context.getResources().getDrawable(resourceId);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public int parseColorResource(String value) {
		String name = parseReferName(value);
		int resourceId = parseAndroidResource(android.R.color.class, name);
		if (resourceId != -1) {
			return context.getResources().getColor(resourceId);
		}
		return Color.TRANSPARENT;
	}

	@SuppressWarnings("deprecation")
	private void parseAndroidResourceDrawable() {
		Field[] drawablesFields = android.R.drawable.class.getFields();
		for (Field field : drawablesFields) {
			try {
				int resourceId = field.getInt(null);
				Drawable drawable = context.getResources().getDrawable(resourceId);
				drawableMap.put(field.getName(), drawable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Object> getDrawables() {
		return drawableMap;
	}

	public Object getDrawableByName(String name) {
		return drawableMap.get(name);
	}

	private int parseAndroidResource(Class<?> resourceClass, String name) {
		try {
			return resourceClass.getField(name).getInt(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String parseReferName(String reference) {
		if (reference == null) {
			return null;
		}
		return parseReferName(reference, "/");
	}

	public static String parseReferName(String reference, String sep) {
		if (reference == null) {
			return null;
		}
		int index = reference.indexOf(sep);
		if (index >= 0 && index < reference.length() - 1) {
			return reference.substring(index + 1);
		} else {
			return reference;
		}
	}

	public static boolean isHexColor(String color) {
		if (color == null || color.isEmpty()) {
			return false;
		}

		Pattern pattern = Pattern.compile(HEX_COLOR_PATTERN);
		Matcher matcher = pattern.matcher(color);

		return matcher.matches();
	}

	public static boolean isColor(String color) {
		return color.matches("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{8})$");
	}

	private static int parseColor(String color) {
		String hexColor = color.replaceFirst("#", "");
		String formattedColor = String.format("#%8s", hexColor).replaceAll(" ", "F");
		return Color.parseColor(formattedColor);
	}

	public int getDip(int value) {
		return (int) Utils.getDip(context, value);
	}
}
