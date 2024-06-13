package com.elfilibustero.uidesigner.lib.handler;

import static com.elfilibustero.uidesigner.lib.utils.Utils.getDip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.elfilibustero.uidesigner.lib.tool.ViewIdentifierFactory;
import com.elfilibustero.uidesigner.lib.utils.InvokeUtil;
import com.elfilibustero.uidesigner.ui.designer.DesignerListItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class PropertiesHandler {
    private final Context context;
    private final float dip;
    private final ResourceFactory resource;
    private final ViewIdentifierFactory viewIdentifier = ViewIdentifierFactory.getInstance();
    private final View view;
    private final Map<String, Object> attributes;

    public PropertiesHandler(Context context, View view, Map<String, Object> attributes) {
        this.context = context;
        this.view = view;
        this.attributes = attributes;
        dip = getDip(context, 1.0f);
        resource = ResourceFactory.getInstance();
    }

    private boolean hasAttribute(String attr) {
        return attributes.keySet().stream().anyMatch(key -> key.equals(attr));
    }

    public void setPropertyFor(String methodName, String value) {
        try {
            Method method = getClass().getMethod(methodName, String.class);
            if (method != null) method.invoke(this, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setId(String value) {
        viewIdentifier.register(view, value);
    }

    public void setLayoutWidth(String value) {
        InvokeUtil.setField(view.getLayoutParams(), "width", Integer.parseInt(value));
    }

    public void setLayoutHeight(String value) {
        InvokeUtil.setField(view.getLayoutParams(), "height", Integer.parseInt(value));
    }

    public void setWeight(String value) {
        InvokeUtil.setField(view.getLayoutParams(), "weight", Float.parseFloat(value));
    }

    public void setFitsSystemWindows(String value) {
        InvokeUtil.invoke(
                view,
                "setFitsSystemWindows",
                new Class[] {boolean.class},
                Boolean.parseBoolean(value));
    }

    public void setCenterInParent(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.CENTER_IN_PARENT);
    }

    public void setCenterVertical(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.CENTER_VERTICAL);
    }

    public void setCenterHorizontal(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.CENTER_HORIZONTAL);
    }

    public void setAlignStart(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_START,
                viewIdentifier.getId(value));
    }

    public void setAlignLeft(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_LEFT,
                viewIdentifier.getId(value));
    }

    public void setAlignTop(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_TOP,
                viewIdentifier.getId(value));
    }

    public void setAlignEnd(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_END,
                viewIdentifier.getId(value));
    }

    public void setAlignRight(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_RIGHT,
                viewIdentifier.getId(value));
    }

    public void setAlignBottom(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_BOTTOM,
                viewIdentifier.getId(value));
    }

    public void setAlignBaseline(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ALIGN_BASELINE,
                viewIdentifier.getId(value));
    }

    public void setToStartOf(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.START_OF,
                viewIdentifier.getId(value));
    }

    public void setToLeftOf(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.LEFT_OF,
                viewIdentifier.getId(value));
    }

    public void setToEndOf(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.END_OF,
                viewIdentifier.getId(value));
    }

    public void setToRightOf(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.RIGHT_OF,
                viewIdentifier.getId(value));
    }

    public void setAbove(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.ABOVE,
                viewIdentifier.getId(value));
    }

    public void setBelow(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "addRule",
                new Class[] {int.class, int.class},
                RelativeLayout.BELOW,
                viewIdentifier.getId(value));
    }

    public void setAlignParentStart(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.ALIGN_PARENT_START);
    }

    public void setAlignParentLeft(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.ALIGN_PARENT_LEFT);
    }

    public void setAlignParentTop(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.ALIGN_PARENT_TOP);
    }

    public void setAlignParentEnd(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.ALIGN_PARENT_END);
    }

    public void setAlignParentRight(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    public void setAlignParentBottom(String value) {
        if (Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view.getLayoutParams(),
                    "addRule",
                    new Class[] {int.class},
                    RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    public void setMargins(String value) {
        int layout_margin = Integer.parseInt(value);
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
            InvokeUtil.invoke(
                    layoutParams,
                    "setMargins",
                    new Class[] {int.class, int.class, int.class, int.class},
                    layout_margin,
                    layout_margin,
                    layout_margin,
                    layout_margin);
        }
    }

    public void setMarginLeft(String value) {
        if (!hasAttribute("android:layout_margin")) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
                InvokeUtil.setField(layoutParams, "leftMargin", Integer.parseInt(value));
            }
        }
    }

    public void setMarginTop(String value) {
        if (!hasAttribute("android:layout_margin")) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
                InvokeUtil.setField(layoutParams, "topMargin", Integer.parseInt(value));
            }
        }
    }

    public void setMarginRight(String value) {
        if (!hasAttribute("android:layout_margin")) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
                InvokeUtil.setField(layoutParams, "rightMargin", Integer.parseInt(value));
            }
        }
    }

    public void setMarginBottom(String value) {
        if (!hasAttribute("android:layout_margin")) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
                InvokeUtil.setField(layoutParams, "bottomMargin", Integer.parseInt(value));
            }
        }
    }

    public void setMarginStart(String value) {
        if (!hasAttribute("android:layout_margin")) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
                InvokeUtil.invoke(
                        layoutParams,
                        "setMarginStart",
                        new Class[] {int.class},
                        Integer.parseInt(value));
            }
        }
    }

    public void setMarginEnd(String value) {
        if (!hasAttribute("android:layout_margin")) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams layoutParams) {
                InvokeUtil.invoke(
                        layoutParams,
                        "setMarginEnd",
                        new Class[] {int.class},
                        Integer.parseInt(value));
            }
        }
    }

    public void setOrientation(String value) {
        InvokeUtil.invoke(view, "setOrientation", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setGravity(String value) {
        InvokeUtil.invoke(view, "setGravity", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setLayoutGravity(String value) {
        InvokeUtil.setField(view.getLayoutParams(), "gravity", Integer.parseInt(value));
    }

    public void setElevation(String value) {
        InvokeUtil.invoke(
                view,
                "setElevation",
                new Class[] {float.class},
                getDip(context, Float.parseFloat(value)));
    }

    public void setVisibility(String value) {
        InvokeUtil.invoke(view, "setVisibility", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setEnabled(String value) {
        if (!Boolean.parseBoolean(value))
            InvokeUtil.invoke(
                    view, "setEnabled", new Class[] {boolean.class}, Boolean.parseBoolean(value));
    }

    public void setAlpha(String value) {
        InvokeUtil.invoke(view, "setAlpha", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setWeightSum(String value) {
        InvokeUtil.invoke(view, "setWeightSum", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setClipToPadding(String value) {
        InvokeUtil.invoke(
                view, "setClipToPadding", new Class[] {boolean.class}, Boolean.parseBoolean(value));
    }

    public void setContentDescription(String value) {
        InvokeUtil.invoke(
                view,
                "setContentDescription",
                new Class[] {CharSequence.class},
                resource.getString(value));
    }

    public void setPaddings(String value) {
        int padding = Integer.parseInt(value);
        InvokeUtil.invoke(
                view,
                "setPadding",
                new Class[] {int.class, int.class, int.class, int.class},
                padding,
                padding,
                padding,
                padding);
    }

    public void setPaddingStart(String value) {
        if (!hasAttribute("android:padding")) {
            int top = (int) InvokeUtil.invoke(view, "getPaddingTop", null);
            int bottom = (int) InvokeUtil.invoke(view, "getPaddingBottom", null);
            int end = (int) InvokeUtil.invoke(view, "getPaddingEnd", null);
            InvokeUtil.invoke(
                    view,
                    "setPaddingRelative",
                    new Class[] {int.class, int.class, int.class, int.class},
                    Integer.parseInt(value),
                    top,
                    end,
                    bottom);
        }
    }

    public void setPaddingLeft(String value) {
        if (!hasAttribute("android:padding")) {
            int top = (int) InvokeUtil.invoke(view, "getPaddingTop", null);
            int right = (int) InvokeUtil.invoke(view, "getPaddingRight", null);
            int bottom = (int) InvokeUtil.invoke(view, "getPaddingBottom", null);
            InvokeUtil.invoke(
                    view,
                    "setPadding",
                    new Class[] {int.class, int.class, int.class, int.class},
                    Integer.parseInt(value),
                    top,
                    right,
                    bottom);
        }
    }

    public void setPaddingTop(String value) {
        if (!hasAttribute("android:padding")) {
            int left = (int) InvokeUtil.invoke(view, "getPaddingLeft", null);
            int right = (int) InvokeUtil.invoke(view, "getPaddingRight", null);
            int bottom = (int) InvokeUtil.invoke(view, "getPaddingBottom", null);
            InvokeUtil.invoke(
                    view,
                    "setPadding",
                    new Class[] {int.class, int.class, int.class, int.class},
                    left,
                    Integer.parseInt(value),
                    right,
                    bottom);
        }
    }

    public void setPaddingEnd(String value) {
        if (!hasAttribute("android:padding")) {
            int top = (int) InvokeUtil.invoke(view, "getPaddingTop", null);
            int bottom = (int) InvokeUtil.invoke(view, "getPaddingBottom", null);
            int start = (int) InvokeUtil.invoke(view, "getPaddingStart", null);
            InvokeUtil.invoke(
                    view,
                    "setPaddingRelative",
                    new Class[] {int.class, int.class, int.class, int.class},
                    start,
                    top,
                    Integer.parseInt(value),
                    bottom);
        }
    }

    public void setPaddingRight(String value) {
        if (!hasAttribute("android:padding")) {
            int left = (int) InvokeUtil.invoke(view, "getPaddingLeft", null);
            int top = (int) InvokeUtil.invoke(view, "getPaddingTop", null);
            int bottom = (int) InvokeUtil.invoke(view, "getPaddingBottom", null);
            InvokeUtil.invoke(
                    view,
                    "setPadding",
                    new Class[] {int.class, int.class, int.class, int.class},
                    left,
                    top,
                    Integer.parseInt(value),
                    bottom);
        }
    }

    public void setRotation(String value) {
        InvokeUtil.invoke(view, "setRotation", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setRotationX(String value) {
        InvokeUtil.invoke(view, "setRotationX", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setRotationY(String value) {
        InvokeUtil.invoke(view, "setRotationY", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setScaleX(String value) {
        InvokeUtil.invoke(view, "setScaleX", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setScaleY(String value) {
        InvokeUtil.invoke(view, "setScaleY", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setTranslationX(String value) {
        InvokeUtil.invoke(
                view, "setTranslationX", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setTranslationY(String value) {
        InvokeUtil.invoke(
                view, "setTranslationY", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setTranslationZ(String value) {
        InvokeUtil.invoke(
                view, "setTranslationZ", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setFillViewport(String value) {
        InvokeUtil.invoke(
                view, "setFillViewport", new Class[] {boolean.class}, Boolean.parseBoolean(value));
    }

    public void setBackground(String value) {
        InvokeUtil.invoke(
                view,
                "setBackgroundDrawable",
                new Class[] {Drawable.class},
                resource.getDrawable(value));
    }

    public void setBackgroundTint(String value) {
        InvokeUtil.invoke(
                view,
                "setBackgroundTintList",
                new Class[] {ColorStateList.class},
                ColorStateList.valueOf(resource.getColor(value)));
    }

    public void setForeground(String value) {
        InvokeUtil.invoke(
                view, "setForeground", new Class[] {Drawable.class}, resource.getDrawable(value));
    }

    public void setChecked(String value) {
        InvokeUtil.invoke(
                view, "setChecked", new Class[] {Boolean.class}, Boolean.parseBoolean(value));
    }

    public void setText(String value) {
        InvokeUtil.invoke(
                view, "setText", new Class[] {CharSequence.class}, resource.getString(value));
    }

    public void setTextColor(String value) {
        int color = resource.getColor(value);
        if (color == Color.TRANSPARENT) color = 0x80000000;
        InvokeUtil.invoke(view, "setTextColor", new Class[] {int.class}, color);
    }

    public void setTextSize(String value) {
        InvokeUtil.invoke(
                view,
                "setTextSize",
                new Class[] {int.class, float.class},
                TypedValue.COMPLEX_UNIT_SP,
                Float.parseFloat(value));
    }

    public void setTextStyle(String value) {
        int style = Integer.parseInt(value);
        var tf = (Typeface) InvokeUtil.invoke(view, "getTypeface", null);
        InvokeUtil.invoke(view, "setTypeface", new Class[] {Typeface.class, int.class}, tf, style);
    }

    public void setTextSelectable(String value) {
        InvokeUtil.invoke(
                view,
                "setTextIsSelectable",
                new Class[] {boolean.class},
                Boolean.parseBoolean(value));
    }

    public void setHint(String value) {
        InvokeUtil.invoke(
                view, "setHint", new Class[] {CharSequence.class}, resource.getString(value));
    }

    public void setInputType(String value) {
        InvokeUtil.invoke(view, "setInputType", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setLines(String value) {
        InvokeUtil.invoke(view, "setLines", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setMaxLines(String value) {
        InvokeUtil.invoke(view, "setMaxLines", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setMinLines(String value) {
        InvokeUtil.invoke(view, "setMinLines", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setSingleLine(String value) {
        InvokeUtil.invoke(
                view, "setSingleLine", new Class[] {boolean.class}, Boolean.parseBoolean(value));
    }

    public void setImage(String value) {
        InvokeUtil.invoke(
                view,
                "setImageDrawable",
                new Class[] {Drawable.class},
                resource.getDrawable(value));
    }

    public void setScaleType(String value) {
        InvokeUtil.invoke(
                view,
                "setScaleType",
                new Class[] {ImageView.ScaleType.class},
                ImageView.ScaleType.valueOf(value));
    }

    public void setTint(String value) {
        InvokeUtil.invoke(
                view, "setColorFilter", new Class[] {int.class}, resource.getColor(value));
    }

    public void setProgress(String value) {
        InvokeUtil.invoke(view, "setProgress", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setMin(String value) {
        InvokeUtil.invoke(view, "setMin", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setMax(String value) {
        InvokeUtil.invoke(view, "setMax", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setIndeterminate(String value) {
        InvokeUtil.invoke(
                view, "setIndeterminate", new Class[] {boolean.class}, Boolean.parseBoolean(value));
    }

    public void setListItem(String value) {
        if (view instanceof DesignerListItem listView) {
            int layout = resource.getLayout(value);
            if (layout <= 0) {
                layout = android.R.layout.simple_list_item_1;
            }
            listView.setListItem(layout);
        }
    }

    public void setItemCount(String value) {
        if (view instanceof DesignerListItem listView) {
            if (!hasAttribute("tools:listitem")) listView.setItemCount(Integer.parseInt(value));
        }
    }

    // Compat

    public void setTitle(String value) {
        InvokeUtil.invoke(
                view, "setTitle", new Class[] {CharSequence.class}, resource.getString(value));
    }

    public void setCardElevation(String value) {
        InvokeUtil.invoke(
                view, "setCardElevation", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setCardCornerRadius(String value) {
        InvokeUtil.invoke(view, "setRadius", new Class[] {float.class}, Float.parseFloat(value));
    }

    public void setCardStrokeColor(String value) {
        InvokeUtil.invoke(
                view, "setStrokeColor", new Class[] {int.class}, resource.getColor(value));
    }

    public void setCardStrokeWidth(String value) {
        InvokeUtil.invoke(view, "setStrokeWidth", new Class[] {int.class}, Integer.parseInt(value));
    }

    public void setCardUseCompatPadding(String value) {
        InvokeUtil.invoke(
                view,
                "setUseCompatPadding",
                new Class[] {boolean.class},
                Boolean.parseBoolean(value));
    }

    public void setCardBackgroundColor(String value) {
        InvokeUtil.invoke(
                view, "setCardBackgroundColor", new Class[] {int.class}, resource.getColor(value));
    }

    public void setCardForegroundColor(String value) {
        InvokeUtil.invoke(
                view,
                "setCardForegroundColor",
                new Class[] {ColorStateList.class},
                ColorStateList.valueOf(resource.getColor(value)));
    }

    public void setCheckedIcon(String value) {
        InvokeUtil.invoke(
                view, "setCheckedIcon", new Class[] {Drawable.class}, resource.getDrawable(value));
    }

    public void setCheckedIconTint(String value) {
        InvokeUtil.invoke(
                view,
                "setCheckedIconTint",
                new Class[] {ColorStateList.class},
                ColorStateList.valueOf(resource.getColor(value)));
    }

    // recyclerview

    public void setLayoutManager(String value) {
        var layoutManager =
                (LinearLayoutManager)
                        InvokeUtil.create(
                                resource.getString(value), new Class[] {Context.class}, context);
        if (layoutManager == null) {
            int spanCount = 2;
            var count = attributes.getOrDefault("app:spanCount", null);
            if (count != null) {
                spanCount = Integer.parseInt((String) count);
            }
            layoutManager =
                    (GridLayoutManager)
                            InvokeUtil.create(
                                    resource.getString(value),
                                    new Class[] {Context.class, int.class},
                                    context,
                                    spanCount);
        }
        var orientation = attributes.getOrDefault("android:orientation", null);
        if (orientation != null) {
            layoutManager.setOrientation(
                    orientation.equals("vertical")
                            ? RecyclerView.VERTICAL
                            : RecyclerView.HORIZONTAL);
        }
        InvokeUtil.invoke(
                view,
                "setLayoutManager",
                new Class[] {RecyclerView.LayoutManager.class},
                layoutManager);
    }

    private void generateId(View view) {
        if (view.getId() == View.NO_ID) view.setId(View.generateViewId());
    }

    // ConstraintLayout
    private int getConstraintId(String value) {
        if (value.equals("parent")) {
            return ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            return viewIdentifier.getId(value);
        }
    }

    private void generateViewId(View view) {
        if (view.getId() == View.NO_ID) view.setId(View.generateViewId());
    }

    private void setConstraint(String methodName, Class<?>[] paramTypes, Object... params) {
        if (view.getParent() instanceof ConstraintLayout constraintLayout) {
            generateViewId(view);
            generateViewId(constraintLayout);
            try {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                InvokeUtil.invoke(constraintSet, methodName, paramTypes, params);
                constraintSet.applyTo(constraintLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setConstraint(String value, int start, int end) {
        if (view.getParent() instanceof ConstraintLayout constraintLayout) {
            generateViewId(view);
            generateViewId(constraintLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(view.getId(), start, getConstraintId(value), end);
            constraintSet.applyTo(constraintLayout);
        }
    }

    private void setConstraintChainStyle(String orientation, String value) {
        setConstraint(
                orientation,
                new Class<?>[] {int.class, int.class},
                view.getId(),
                getChainStyle(value));
    }

    private void setConstraintBiasWeight(String method, String value) {
        setConstraint(
                method,
                new Class<?>[] {int.class, float.class},
                view.getId(),
                Float.parseFloat(value));
    }

    private static int getChainStyle(String value) {
        return switch (value) {
            case "spread" -> ConstraintLayout.LayoutParams.CHAIN_SPREAD;
            case "spread_inside" -> ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE;
            case "packed" -> ConstraintLayout.LayoutParams.CHAIN_PACKED;
            default -> ConstraintLayout.LayoutParams.CHAIN_SPREAD;
        };
    }

    public void setStartToStartOf(String value) {
        setConstraint(value, ConstraintSet.START, ConstraintSet.START);
    }

    public void setStartToEndOf(String value) {
        setConstraint(value, ConstraintSet.START, ConstraintSet.END);
    }

    public void setLeftToLeftOf(String value) {
        setConstraint(value, ConstraintSet.LEFT, ConstraintSet.LEFT);
    }

    public void setLeftToRightOf(String value) {
        setConstraint(value, ConstraintSet.LEFT, ConstraintSet.RIGHT);
    }

    public void setRightToRightOf(String value) {
        setConstraint(value, ConstraintSet.RIGHT, ConstraintSet.RIGHT);
    }

    public void setRightToLeftOf(String value) {
        setConstraint(value, ConstraintSet.RIGHT, ConstraintSet.LEFT);
    }

    public void setEndToStartOf(String value) {
        setConstraint(value, ConstraintSet.END, ConstraintSet.START);
    }

    public void setEndToEndOf(String value) {
        setConstraint(value, ConstraintSet.END, ConstraintSet.END);
    }

    public void setTopToTopOf(String value) {
        setConstraint(value, ConstraintSet.TOP, ConstraintSet.TOP);
    }

    public void setTopToBottomOf(String value) {
        setConstraint(value, ConstraintSet.TOP, ConstraintSet.BOTTOM);
    }

    public void setBottomToTopOf(String value) {
        setConstraint(value, ConstraintSet.BOTTOM, ConstraintSet.TOP);
    }

    public void setBottomToBottomOf(String value) {
        setConstraint(value, ConstraintSet.BOTTOM, ConstraintSet.BOTTOM);
    }

    public void setBaselineToBaselineOf(String value) {
        setConstraint(value, ConstraintSet.BASELINE, ConstraintSet.BASELINE);
    }

    public void setVerticalBias(String value) {
        setConstraintBiasWeight("setVerticalBias", value);
    }

    public void setHorizontalBias(String value) {
        setConstraintBiasWeight("setHorizontalBias", value);
    }

    public void setDimensionRatio(String value) {
        setConstraint(
                "setDimentionRatio", new Class<?>[] {int.class, String.class}, view.getId(), value);
    }

    public void setHorizontalChainStyle(String value) {
        setConstraintChainStyle("setHorizontalChainStyle", value);
    }

    public void setVerticalChainStyle(String value) {
        setConstraintChainStyle("setVerticalChainStyle", value);
    }

    public void setHorizontalWeight(String value) {
        setConstraintBiasWeight("setHorizontalWeight", value);
    }

    public void setVerticalWeight(String value) {
        setConstraintBiasWeight("setVerticalWeight", value);
    }

    public void setGoneMarginLeft(String value) {
        // InvokeUtil.setField(view.getLayoutParams(), "goneLeftMargin", Float.parseFloat(value));
    }

    public void setGoneMarginTop(String value) {
        // InvokeUtil.setField(view.getLayoutParams(), "goneTopMargin", Float.parseFloat(value));
    }

    public void setGoneMarginRight(String value) {
        // InvokeUtil.setField(view.getLayoutParams(), "goneRightMargin", Float.parseFloat(value));
    }

    public void setGoneMarginBottom(String value) {
        // InvokeUtil.setField(view.getLayoutParams(), "goneBottomMargin", Float.parseFloat(value));
    }

    // CoordinatorLayout
    public void setAnchorId(String value) {
        if (!value.startsWith("@id/")) return;
        int id = viewIdentifier.getId(value);
        if (id != View.NO_ID)
            InvokeUtil.invoke(view.getLayoutParams(), "setAnchorId", new Class[] {int.class}, id);
    }

    public void setAnchorGravity(String value) {
        InvokeUtil.setField(view.getLayoutParams(), "anchorGravity", Integer.parseInt(value));
    }

    public void setInsetLeft(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setInsetLeft",
                new Class[] {int.class},
                Integer.parseInt(value));
    }

    public void setInsetTop(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setInsetTop",
                new Class[] {int.class},
                Integer.parseInt(value));
    }

    public void setInsetRight(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setInsetRight",
                new Class[] {int.class},
                Integer.parseInt(value));
    }

    public void setInsetBottom(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setInsetBottom",
                new Class[] {int.class},
                Integer.parseInt(value));
    }

    public void setBehavior(String value) {
        var behavior =
                (CoordinatorLayout.Behavior) InvokeUtil.create(resource.getString(value), null);
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setBehavior",
                new Class[] {CoordinatorLayout.Behavior.class},
                behavior);
    }

    public void setDodgeInsetEdges(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setDodgeInsetEdges",
                new Class[] {int.class},
                Integer.parseInt(value));
    }

    public void setInsetEdge(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setInsetEdge",
                new Class[] {int.class},
                Integer.parseInt(value));
    }

    public void setKeyline(String value) {
        InvokeUtil.invoke(
                view.getLayoutParams(),
                "setKeyline",
                new Class[] {int.class},
                Integer.parseInt(value));
    }
}
