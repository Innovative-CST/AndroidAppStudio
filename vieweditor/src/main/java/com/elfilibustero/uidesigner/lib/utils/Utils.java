package com.elfilibustero.uidesigner.lib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.elfilibustero.uidesigner.AppLoader;
import com.icst.android.appstudio.vieweditor.R;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    public static final String STATUS_BAR_HEIGHT = "status_bar_height";
    public static final String NAVIGATION_BAR_GESTURE_HEIGHT = "navigation_bar_height";

    private static Context getContext() {
        return AppLoader.getContext();
    }

    public static float getDip(Context context, float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static int getStatusBarHeight(Context context) {
        return getResourceDimen(context, STATUS_BAR_HEIGHT);
    }

    public static int getNavigationHeight() {
        return getResourceDimen(NAVIGATION_BAR_GESTURE_HEIGHT);
    }

    public static int getNavigationBarHeight(Context context) {
        return getResourceDimen(context, NAVIGATION_BAR_GESTURE_HEIGHT);
    }

    public static int getResourceDimen(String identifier) {
        return getResourceDimen(getContext(), identifier);
    }

    public static int getResourceDimen(Context context, String identifier) {
        var resources = context.getResources();
        int resourceId = resources.getIdentifier(identifier, "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getActionBarSize(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(
                    typedValue.data, context.getResources().getDisplayMetrics());
        }
        return (int) getDip(context, 32.0f);
    }

    public static Drawable getDefaultImage() {
        return ContextCompat.getDrawable(getContext(), R.drawable.default_image);
    }

    public static boolean isDarkMode(Context context) {
        int uiMode =
                context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return uiMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static Paint getDefaultStrokePaint(Context context) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getDip(context, 1.0f));
        paint.setColor(Color.parseColor("#5e5e5e"));
        return paint;
    }

    public static List<String> generateItems(String prefix, int itemCount) {
        return IntStream.rangeClosed(1, itemCount)
                .mapToObj(i -> prefix + " " + i)
                .collect(Collectors.toList());
    }
}
