package com.elfilibustero.uidesigner.lib.utils;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final int SELECTOR_TYPE_FLAG = 0;
	public static final int SELECTOR_TYPE_ENUM = 1;

	public static final int SELECTOR_TYPE_RELATIVE = 0;
	public static final int SELECTOR_TYPE_CONSTRAINT = 1;

	public static final int INPUT_TYPE_TEXT = 0;
	public static final int INPUT_TYPE_ID = 1;
	public static final int INPUT_TYPE_NUMBER = 2;
	public static final int INPUT_TYPE_FLOAT = 3;
	public static final int INPUT_TYPE_STRING = 4;

	public static final List<Class<?>> EXCLUDED_CLASS = Arrays.asList(
			AdapterView.class,
			CalendarView.class,
			SearchView.class,
			Toolbar.class,
			RecyclerView.class,
			NavigationBarView.class,
			TabLayout.class);

	public static final String[] ANDROID_CLASS_PREFIX = {
			"android.widget.", "android.view.", "android.webkit."
	};

	public static final String[] VIEW = {
			"android:id",
			"android:layout_width",
			"android:layout_height",
			"android:background",
			"android:foreground",
			"android:elevation",
			"android:gravity",
			"android:padding",
			"android:alpha",
			"android:contentDescription",
			"android:paddingStart",
			"android:paddingLeft",
			"android:paddingTop",
			"android:paddingEnd",
			"android:paddingRight",
			"android:paddingBottom",
			"android:rotation",
			"android:rotationX",
			"android:rotationY",
			"android:translationX",
			"android:translationY",
			"android:translationZ",
			"android:scaleX",
			"android:scaleY",
			"android:enabled",
			"android:visibility",
			"android:fitsSystemWindows"
	};

	public static final String[] LINEARLAYOUT = { "android:orientation", "android:weightSum" };

	public static final String[] SCROLLVIEW = { "android:fillViewport" };

	public static final String[] TEXTVIEW = {
			"android:text", "android:textSize", "android:textColor", "android:textStyle"
	};

	public static final String[] EDITTEXT = {
			"android:hint", "android:inputType", "android:textColorHint", "android:singleLine"
	};

	public static final String[] IMAGEVIEW = {
			"android:src", "android:scaleType", "android:tint", "app:srcCompat"
	};

	public static final String[] BUTTON = { "android:backgroundTint" };

	public static final String[] COMPOUNDBUTTON = { "android:checked" };

	public static final String[] PROGRESSBAR = {
			"android:progress", "android:max", "android:min", "android:indeterminate"
	};

	public static final String[] RECYCLERVIEW = { "app:spanCount", "app:layoutManager" };

	public static final String[] CARDVIEW = {
			"app:cardElevation",
			"app:cardCornerRadius",
			"app:cardBackgroundColor",
			"app:cardUseCompatPadding"
	};

	public static final String[] MATERIAL_CARDVIEW = {
			"app:strokeColor",
			"app:strokeWidth",
			"app:cardForegroundColor",
			"app:checkedIcon",
			"app:checkedIconTint",
			"app:checkedIconSize"
	};

	public static final String[] FAB = {
			"android:backgroundTint",
			"app:backgroundTint",
			"app:borderWidth",
			"app:elevation",
			"app:fabSize",
			"app:useCompatPadding"
	};

	public static final String[] PARENT_LINEARLAYOUT = {
			"android:layout_weight",
			"android:layout_gravity",
			"android:layout_margin",
			"android:layout_marginLeft",
			"android:layout_marginTop",
			"android:layout_marginRight",
			"android:layout_marginBottom"
	};

	public static final String[] PARENT_FRAMELAYOUT = {
			"android:layout_gravity",
			"android:layout_margin",
			"android:layout_marginLeft",
			"android:layout_marginTop",
			"android:layout_marginRight",
			"android:layout_marginBottom"
	};

	public static final String[] PARENT_RELATIVELAYOUT = {
			"android:layout_margin",
			"android:layout_marginLeft",
			"android:layout_marginTop",
			"android:layout_marginRight",
			"android:layout_marginBottom",
			"android:layout_centerInParent",
			"android:layout_centerVertical",
			"android:layout_centerHorizontal",
			"android:layout_toLeftOf",
			"android:layout_toRightOf",
			"android:layout_above",
			"android:layout_below",
			"android:layout_alignLeft",
			"android:layout_alignTop",
			"android:layout_alignRight",
			"android:layout_alignBottom",
			"android:layout_alignParentLeft",
			"android:layout_alignParentTop",
			"android:layout_alignParentRight",
			"android:layout_alignParentBottom",
			"android:layout_alignBaseline"
	};

	public static final String[] PARENT_COORDINATORLAYOUT = {
			"android:layout_gravity",
			"android:layout_margin",
			"android:layout_marginLeft",
			"android:layout_marginTop",
			"android:layout_marginRight",
			"android:layout_marginBottom",
			"app:layout_anchor",
			"app:layout_anchorGravity",
			"app:layout_behavior",
			"app:layout_dodgeInsetEdges",
			"app:layoutInsetTop",
			"app:layoutInsetBottom",
			"app:layoutInsetLeft",
			"app:layoutInsetRight",
			"app:layout_insetEdge",
			"app:layout_keyline"
	};

	public static final String[] PARENT_CONSTRAINTLAYOUT = {
			"android:layout_margin",
			"android:layout_marginLeft",
			"android:layout_marginTop",
			"android:layout_marginRight",
			"android:layout_marginBottom",
			"app:layout_constraintLeft_toLeftOf",
			"app:layout_constraintLeft_toRightOf",
			"app:layout_constraintRight_toLeftOf",
			"app:layout_constraintRight_toRightOf",
			"app:layout_constraintTop_toTopOf",
			"app:layout_constraintTop_toBottomOf",
			"app:layout_constraintBottom_toTopOf",
			"app:layout_constraintBottom_toBottomOf",
			"app:layout_constraintBaseline_toBaselineOf",
			"app:layout_constraintStart_toStartOf",
			"app:layout_constraintStart_toEndOf",
			"app:layout_constraintEnd_toStartOf",
			"app:layout_constraintEnd_toEndOf",
			"app:layout_marginBaseline",
			"app:layout_goneMarginLeft",
			"app:layout_goneMarginRight",
			"app:layout_goneMarginTop",
			"app:layout_goneMarginBottom",
			"app:layout_goneMarginStart",
			"app:layout_goneMarginEnd",
			"app:layout_goneMarginBaseline",
			"app:layout_constraintHorizontal_bias",
			"app:layout_constraintVertical_bias"
	};

	public static final List<String> COLORS = Arrays.asList(
			"TRANSPARENT",
			"WHITE",
			"BLACK",
			"BLUE GREY",
			"GREY",
			"BROWN",
			"DEEP ORANGE",
			"ORANGE",
			"AMBER",
			"YELLOW",
			"LIME",
			"LIGHT GREEN",
			"GREEN",
			"TEAL",
			"CYAN",
			"LIGHT BLUE",
			"BLUE",
			"INDIGO",
			"DEEP PURPLE",
			"PURPLE",
			"PINK",
			"RED");

	public static final List<String> COLOR_RED = Arrays.asList(
			"#F44336", "#FFEBEE", "#FFCDD2", "#EF9A9A", "#E57373", "#EF5350", "#F44336",
			"#E53935", "#D32F2F", "#C62828", "#B71C1C", "#FF8A80", "#FF5252", "#FF1744",
			"#D50000");

	public static final List<String> COLOR_PINK = Arrays.asList(
			"#E91E63", "#FCE4EC", "#F8BBD0", "#F48FB1", "#F06292", "#EC407A", "#E91E63",
			"#D81B60", "#C2185B", "#AD1457", "#880E4F", "#FF80AB", "#FF4081", "#F50057",
			"#C51162");

	public static final List<String> COLOR_PURPLE = Arrays.asList(
			"#9C27B0", "#F3E5F5", "#E1BEE7", "#CE93D8", "#BA68C8", "#AB47BC", "#9C27B0",
			"#8E24AA", "#7B1FA2", "#6A1B9A", "#4A148C", "#EA80FC", "#E040FB", "#D500F9",
			"#AA00FF");

	public static final List<String> COLOR_DEEP_PURPLE = Arrays.asList(
			"#673AB7", "#EDE7F6", "#D1C4E9", "#B39DDB", "#9575CD", "#7E57C2", "#673AB7",
			"#5E35B1", "#512DA8", "#4527A0", "#311B92", "#B388FF", "#7C4DFF", "#651FFF",
			"#6200EA");

	public static final List<String> COLOR_INDIGO = Arrays.asList(
			"#3F51B5", "#E8EAF6", "#C5CAE9", "#9FA8DA", "#7986CB", "#5C6BC0", "#3F51B5",
			"#3949AB", "#303F9F", "#283593", "#1A237E", "#8C9EFF", "#536DFE", "#3D5AFE",
			"#304FFE");

	public static final List<String> COLOR_BLUE = Arrays.asList(
			"#2196F3", "#E3F2FD", "#BBDEFB", "#90CAF9", "#64B5F6", "#42A5F5", "#2196F3",
			"#1E88E5", "#1976D2", "#1565C0", "#0D47A1", "#82B1FF", "#448AFF", "#2979FF",
			"#2962FF");

	public static final List<String> COLOR_LIGHT_BLUE = Arrays.asList(
			"#03A9F4", "#E1F5FE", "#B3E5FC", "#81D4FA", "#4FC3F7", "#29B6F6", "#03A9F4",
			"#039BE5", "#0288D1", "#0277BD", "#01579B", "#80D8FF", "#40C4FF", "#00B0FF",
			"#0091EA");

	public static final List<String> COLOR_CYAN = Arrays.asList(
			"#00BCD4", "#E0F7FA", "#B2EBF2", "#80DEEA", "#4DD0E1", "#26C6DA", "#00BCD4",
			"#00ACC1", "#0097A7", "#00838F", "#006064", "#84FFFF", "#18FFFF", "#00E5FF",
			"#00B8D4");

	public static final List<String> COLOR_TEAL = Arrays.asList(
			"#009688", "#E0F2F1", "#B2DFDB", "#80CBC4", "#4DB6AC", "#26A69A", "#009688",
			"#00897B", "#00796B", "#00695C", "#004D40", "#A7FFEB", "#64FFDA", "#1DE9B6",
			"#00BFA5");

	public static final List<String> COLOR_GREEN = Arrays.asList(
			"#4CAF50", "#E8F5E9", "#C8E6C9", "#A5D6A7", "#81C784", "#66BB6A", "#4CAF50",
			"#43A047", "#388E3C", "#2E7D32", "#1B5E20", "#B9F6CA", "#69F0AE", "#00E676",
			"#00C853");

	public static final List<String> COLOR_LIGHT_GREEN = Arrays.asList(
			"#8BC34A", "#F1F8E9", "#DCEDC8", "#C5E1A5", "#AED581", "#9CCC65", "#8BC34A",
			"#7CB342", "#689F38", "#558B2F", "#33691E", "#CCFF90", "#B2FF59", "#76FF03",
			"#64DD17");

	public static final List<String> COLOR_LIME = Arrays.asList(
			"#CDDC39", "#F9FBE7", "#F0F4C3", "#E6EE9C", "#DCE775", "#D4E157", "#CDDC39",
			"#C0CA33", "#AFB42B", "#9E9D24", "#827717", "#F4FF81", "#EEFF41", "#C6FF00",
			"#AEEA00");

	public static final List<String> COLOR_YELLOW = Arrays.asList(
			"#FFEB3B", "#FFFDE7", "#FFF9C4", "#FFF59D", "#FFF176", "#FFEE58", "#FFEB3B",
			"#FDD835", "#FBC02D", "#F9A825", "#F57F17", "#FFFF8D", "#FFFF00", "#FFEA00",
			"#FFD600");

	public static final List<String> COLOR_AMBER = Arrays.asList(
			"#FFC107", "#FFF8E1", "#FFECB3", "#FFE082", "#FFD54F", "#FFCA28", "#FFC107",
			"#FFB300", "#FFA000", "#FF8F00", "#FF6F00", "#FFE57F", "#FFD740", "#FFC400",
			"#FFAB00");

	public static final List<String> COLOR_ORANGE = Arrays.asList(
			"#FF9800", "#FFF3E0", "#FFE0B2", "#FFCC80", "#FFB74D", "#FFA726", "#FF9800",
			"#FB8C00", "#F57C00", "#EF6C00", "#E65100", "#FFD180", "#FFAB40", "#FF9100",
			"#FF6D00");

	public static final List<String> COLOR_DEEP_ORANGE = Arrays.asList(
			"#FF5722", "#FBE9E7", "#FFCCBC", "#FFAB91", "#FF8A65", "#FF7043", "#FF5722",
			"#F4511E", "#E64A19", "#D84315", "#BF360C", "#FF9E80", "#FF6E40", "#FF3D00",
			"#DD2C00");

	public static final List<String> COLOR_BROWN = Arrays.asList(
			"#795548", "#EFEBE9", "#D7CCC8", "#BCAAA4", "#A1887F", "#8D6E63", "#795548",
			"#6D4C41", "#5D4037", "#4E342E", "#3E2723");

	public static final List<String> COLOR_GREY = Arrays.asList(
			"#9E9E9E", "#FAFAFA", "#F5F5F5", "#EEEEEE", "#E0E0E0", "#BDBDBD", "#9E9E9E",
			"#757575", "#616161", "#424242", "#212121");

	public static final List<String> COLOR_BLUE_GREY = Arrays.asList(
			"#607D8B", "#ECEFF1", "#CFD8DC", "#B0BEC5", "#90A4AE", "#78909C", "#607D8B",
			"#546E7A", "#455A64", "#37474F", "#263238");

	public static boolean isExcludedViewGroup(ViewGroup viewGroup) {
		return EXCLUDED_CLASS.stream().anyMatch(clazz -> clazz.isInstance(viewGroup));
	}
}
