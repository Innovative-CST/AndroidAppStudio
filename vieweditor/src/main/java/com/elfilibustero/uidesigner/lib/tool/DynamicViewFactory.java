package com.elfilibustero.uidesigner.lib.tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.elfilibustero.uidesigner.ui.designer.items.DefaultView;
import com.elfilibustero.uidesigner.ui.designer.items.UnknownView;
import com.elfilibustero.uidesigner.ui.designer.items.androidx.ItemDrawerLayout;
import com.elfilibustero.uidesigner.ui.designer.items.androidx.ItemRecyclerView;
import com.elfilibustero.uidesigner.ui.designer.items.androidx.ItemToolbar;
import com.elfilibustero.uidesigner.ui.designer.items.google.ItemBottomAppBar;
import com.elfilibustero.uidesigner.ui.designer.items.google.ItemBottomNavigationView;
import com.elfilibustero.uidesigner.ui.designer.items.google.ItemNavigationView;
import com.elfilibustero.uidesigner.ui.designer.items.google.ItemTabLayout;
import com.elfilibustero.uidesigner.ui.designer.items.layout.ItemHorizontalScrollView;
import com.elfilibustero.uidesigner.ui.designer.items.layout.ItemScrollView;
import com.elfilibustero.uidesigner.ui.designer.items.list.ItemGridView;
import com.elfilibustero.uidesigner.ui.designer.items.list.ItemListView;
import com.elfilibustero.uidesigner.ui.designer.items.list.ItemSpinner;
import com.elfilibustero.uidesigner.ui.designer.items.widget.ItemButton;
import com.elfilibustero.uidesigner.ui.designer.items.widget.ItemCalendarView;
import com.elfilibustero.uidesigner.ui.designer.items.widget.ItemEditText;
import com.elfilibustero.uidesigner.ui.designer.items.widget.ItemSearchView;
import com.tscodeeditor.android.appstudio.vieweditor.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DynamicViewFactory {
    private static DynamicViewFactory instance;
    private ContextThemeWrapper context;

    public DynamicViewFactory(Context context) {
        this.context = new ContextThemeWrapper(context, R.style.LayoutEditorTheme);
    }

    public static void init(Context context) {
        instance = new DynamicViewFactory(context);
    }

    public static DynamicViewFactory getInstance() {
        return instance;
    }

    public Object createView(@NonNull String name) {
        View v = null;
        if (isFullPackage(name)) {
            v = createSpecial(name);
            if (v == null) {
                v = create(name);
            }
        } else {
            v = createSpecial(name);
            if (v == null) {
                for (String prefix : Constants.ANDROID_CLASS_PREFIX) {
                    v = create(prefix + name);
                    if (v != null) return v;
                }
            }
        }

        if (v == null) return createUnknownView(name);
        return v;
    }

    public Object createView(@NonNull String name, int defStyle) {
        View v = null;
        if (isFullPackage(name)) {
            v = create(name, defStyle);
        } else {
            v = createSpecial(name);
            if (v == null) {
                for (String prefix : Constants.ANDROID_CLASS_PREFIX) {
                    v = create(prefix + name, defStyle);
                    if (v != null) return v;
                }
            }
        }
        if (v == null) return createView(name);
        return v;
    }

    private View create(@NonNull String name) {
        try {
            Class<?> clazz = Class.forName(name);
            Constructor<?> con = clazz.getDeclaredConstructor(Context.class);
            con.setAccessible(true);
            return (View) con.newInstance(context);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        } catch (SecurityException e) {
        }
        return null;
    }

    private View create(@NonNull String name, int defStyle) {
        try {
            Class<?> clazz = Class.forName(name);
            Constructor<?> con =
                    clazz.getDeclaredConstructor(Context.class, AttributeSet.class, int.class);
            con.setAccessible(true);
            return (View) con.newInstance(context, null, defStyle);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public View createDefault(@NonNull String text) {
        var v = new DefaultView(context);
        v.setClassName(text);
        return v;
    }

    public View createUnknownView(@NonNull String className) {
        var unknownView = new UnknownView(context);
        unknownView.setClassName(className);
        return unknownView;
    }

    private View createSpecial(@NonNull String tag) {
        var className = getNameFromTag(tag);
        View view =
                switch (className) {
                    case "include" -> createUnknownView(tag);
                        // create view
                    case "ScrollView" -> new ItemScrollView(context);
                    case "HorizontalScrollView" -> new ItemHorizontalScrollView(context);
                    case "EditText" -> new ItemEditText(context);
                    case "Button" -> new ItemButton(context);
                    case "Spinner" -> new ItemSpinner(context);
                    case "ListView" -> new ItemListView(context);
                    case "GridView" -> new ItemGridView(context);
                    case "CalendarView" -> new ItemCalendarView(context);
                    case "SearchView" -> new ItemSearchView(context);
                        // create view(for full package)
                    case "RecyclerView" -> new ItemRecyclerView(context);
                    case "Toolbar", "MaterialToolbar" -> new ItemToolbar(context);
                    case "TabLayout" -> new ItemTabLayout(context);
                    case "BottomAppBar" -> new ItemBottomAppBar(context);
                    case "BottomNavigationView" -> new ItemBottomNavigationView(context);
                    case "DrawerLayout" -> new ItemDrawerLayout(context);
                    case "NavigationView" -> new ItemNavigationView(context);
                        // create a DefaultView
                    case "View",
                            "WebView",
                            "ViewPager",
                            "ViewPager2",
                            "FragmentContainerView" -> new DefaultView(context);
                    default -> null;
                };
        if (view != null && view instanceof DesignerItem item) {
            item.setClassName(tag);
        }
        return view;
    }

    public static boolean isFullPackage(@NonNull String s) {
        return s.contains(".");
    }

    public static String getNameFromTag(@NonNull String s) {
        try {
            if (isFullPackage(s)) return s.substring(s.lastIndexOf(".") + 1);
        } catch (Exception e) {
        }
        return s;
    }

    public static Object getLayoutParamsFor(ViewGroup parent, int width, int height) {
        Class<?> parentClass = parent.getClass();
        while (parentClass != ViewGroup.class) {
            try {
                Class<?> layoutParamsClass = Class.forName(parentClass.getName() + "$LayoutParams");
                Constructor<?> constructor = layoutParamsClass.getConstructor(int.class, int.class);
                var layoutParams = constructor.newInstance(width, height);
                if (layoutParams != null) {
                    return layoutParams;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            parentClass = parentClass.getSuperclass();
        }
        return new ViewGroup.LayoutParams(width, height);
    }

    public static String getSimpleName(View view) {
        if (view instanceof UnknownView unknownView) {
            return unknownView.getClassName();
        } else if (view instanceof DesignerItem item) {
            return item.getClassType().getSimpleName();
        } else {
            return view.getClass().getSimpleName();
        }
    }

    public static String getName(View view) {
        if (view instanceof UnknownView unknownView) {
            return unknownView.getClassName();
        } else if (view instanceof DesignerItem item) {
            return item.getClassType().getName();
        } else {
            return view.getClass().getName();
        }
    }
}
