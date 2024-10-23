package com.elfilibustero.uidesigner.lib.handler;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.elfilibustero.uidesigner.beans.ViewBean;
import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.ImmutableMap;
import com.icst.android.appstudio.vieweditor.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ViewPaletteHandler {

	public static List<ViewBean> getAll() {
		List<ViewBean> palettes = new ArrayList<>();
		palettes.add(createIncludePalette());
		palettes.addAll(getLayouts());
		palettes.addAll(getAndroidx());
		palettes.addAll(getWidgets());
		palettes.addAll(getLists());
		palettes.addAll(getGoogle());
		return palettes;
	}

	private static ViewBean createIncludePalette() {
		return ViewBean.builder()
				.setClassName("include")
				.setName("include")
				.setIcon(R.drawable.ic_item_include)
				.addDefaultValue("layout", "@layout/your_layout")
				.build();
	}

	public static List<ViewBean> getLayouts() {
		return List.of(
				createPalette(
						LinearLayout.class.getName(),
						LinearLayout.class.getSimpleName() + "(V)",
						R.drawable.ic_item_linear,
						ImmutableMap.of(
								"android:layout_height",
								"match_parent",
								"android:padding",
								"8dp",
								"android:orientation",
								"vertical")),
				createPalette(
						LinearLayout.class.getName(),
						LinearLayout.class.getSimpleName() + "(H)",
						R.drawable.ic_item_linear,
						ImmutableMap.of(
								"android:layout_width",
								"match_parent",
								"android:padding",
								"8dp",
								"android:orientation",
								"horizontal")),
				createPalette(
						RelativeLayout.class.getName(),
						R.drawable.ic_item_relative,
						ImmutableMap.of("android:padding", "8dp")),
				createPalette(
						FrameLayout.class.getName(),
						R.drawable.ic_item_frame,
						ImmutableMap.of("android:padding", "8dp")),
				createPalette(
						RadioGroup.class.getName(),
						R.drawable.ic_item_radiogroup,
						ImmutableMap.of("android:padding", "8dp")),
				createPalette(
						ScrollView.class.getName(),
						R.drawable.ic_item_scroll,
						ImmutableMap.of("android:padding", "8dp")),
				createPalette(
						HorizontalScrollView.class.getName(),
						R.drawable.ic_item_hscroll,
						ImmutableMap.of("android:padding", "8dp")));
	}

	public static List<ViewBean> getWidgets() {
		return List.of(
				createPalette(
						Button.class.getName(),
						R.drawable.ic_item_button,
						ImmutableMap.of("android:text", "Button")),
				createPalette(
						TextView.class.getName(),
						R.drawable.ic_item_textview,
						ImmutableMap.of("android:text", "TextView")),
				createPalette(
						EditText.class.getName(),
						R.drawable.ic_item_edittext,
						ImmutableMap.of("android:hint", "EditText")),
				createPalette(
						RadioButton.class.getName(),
						R.drawable.ic_item_radiobutton,
						ImmutableMap.of("android:text", "RadioButton")),
				createPalette(
						Switch.class.getName(),
						R.drawable.ic_item_switch,
						ImmutableMap.of("android:text", "Switch")),
				createPalette(
						CheckBox.class.getName(),
						R.drawable.ic_item_checkbox,
						ImmutableMap.of("android:text", "CheckBox")),
				createPalette(ImageView.class.getName(), R.drawable.ic_item_imageview),
				createPalette(
						ProgressBar.class.getName(),
						R.drawable.ic_item_progressbar,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						ProgressBar.class.getName(),
						ProgressBar.class.getSimpleName() + "(H)",
						R.drawable.ic_item_progressbar,
						ImmutableMap.of(
								"android:layout_width",
								"match_parent",
								"style",
								"?android:attr/progressBarStyleHorizontal")),
				createPalette(
						WebView.class.getName(),
						R.drawable.ic_item_webview,
						ImmutableMap.of(
								"android:layout_width",
								"match_parent",
								"android:layout_height",
								"match_parent")),
				createPalette(
						VideoView.class.getName(),
						R.drawable.ic_item_videoview,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						SearchView.class.getName(),
						R.drawable.ic_item_searchview,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(RatingBar.class.getName(), R.drawable.ic_item_ratingbar),
				createPalette(CalendarView.class.getName(), R.drawable.ic_item_calendarview));
	}

	public static List<ViewBean> getAndroidx() {
		return List.of(
				createPalette(
						CoordinatorLayout.class.getName(),
						R.drawable.ic_item_coordinatorlayout,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						ConstraintLayout.class.getName(),
						R.drawable.ic_item_constraint,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						Toolbar.class.getName(),
						R.drawable.ic_item_toolbar,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						CardView.class.getName(),
						R.drawable.ic_item_cardview,
						ImmutableMap.of(
								"android:layout_width",
								"match_parent",
								"app:cardElevation",
								"4dp",
								"app:cardCornerRadius",
								"30dp")),
				createPalette(
						NestedScrollView.class.getName(),
						R.drawable.ic_item_nscroll,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						DrawerLayout.class.getName(),
						R.drawable.ic_item_drawerlayout,
						ImmutableMap.of(
								"android:layout_width",
								"match_parent",
								"android:layout_height",
								"match_parent")));
	}

	public static List<ViewBean> getLists() {
		return List.of(
				createPalette(
						ListView.class.getName(),
						R.drawable.ic_item_listview,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						GridView.class.getName(),
						R.drawable.ic_item_gridview,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						Spinner.class.getName(),
						R.drawable.ic_item_spinner,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						RecyclerView.class.getName(),
						R.drawable.ic_item_recyclerview,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						ViewPager.class.getName(),
						R.drawable.ic_item_viewpager,
						ImmutableMap.of(
								"android:layout_width",
								"match_parent",
								"android:layout_height",
								"match_parent")));
	}

	public static List<ViewBean> getGoogle() {
		return List.of(
				createPalette(
						AppBarLayout.class.getName(),
						R.drawable.ic_item_appbar,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						BottomAppBar.class.getName(),
						R.drawable.ic_item_bappbar,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						CollapsingToolbarLayout.class.getName(),
						R.drawable.ic_item_collapsingtoolbar,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						MaterialToolbar.class.getName(),
						R.drawable.ic_item_toolbar,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						TabLayout.class.getName(),
						R.drawable.ic_item_tablayout,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(
						BottomNavigationView.class.getName(),
						R.drawable.ic_item_bottomnavigation,
						ImmutableMap.of("android:layout_width", "match_parent")),
				createPalette(FloatingActionButton.class.getName(), R.drawable.ic_item_fab));
	}

	@DrawableRes
	public static int getIcon(String name) {
		for (ViewBean bean : getAll()) {
			String className = DynamicViewFactory.getNameFromTag(bean.getClassName());
			if (className.equals(name)) {
				return bean.getIcon();
			}
		}
		return getIconDefault(name);
	}

	@DrawableRes
	private static int getIconDefault(String name) {
		return switch (name) {
			case "include" -> R.drawable.ic_item_include;
			case "LinearProgressIndicator", "CircularProgressIndicator" -> R.drawable.ic_item_progressbar;
			default -> R.drawable.ic_item_unknownview;
		};
	}

	public static ViewBean createPalette(@NonNull String className) {
		return createPalette(className, getIcon(className), Collections.emptyMap());
	}

	public static ViewBean createPalette(@NonNull String className, @DrawableRes int icon) {
		return createPalette(className, icon, Collections.emptyMap());
	}

	public static ViewBean createPalette(
			@NonNull String className, @NonNull String name, @DrawableRes int icon) {
		return createPalette(className, name, icon, Collections.emptyMap());
	}

	public static ViewBean createPalette(
			@NonNull String className, @DrawableRes int icon, Map<String, Object> attributes) {
		String name = className.substring(className.lastIndexOf('.') + 1);
		return createPalette(className, name, icon, attributes);
	}

	public static ViewBean createPalette(
			@NonNull String className,
			@NonNull String name,
			@DrawableRes int icon,
			Map<String, Object> attributes) {
		ViewBean.Builder builder = ViewBean.builder().setClassName(className).setName(name).setIcon(icon);
		builder.addDefaultValue("android:layout_width", "wrap_content");
		builder.addDefaultValue("android:layout_height", "wrap_content");
		attributes.forEach(builder::addDefaultValue);
		return builder.build();
	}
}
