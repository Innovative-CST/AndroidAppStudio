package com.elfilibustero.uidesigner.ui.designer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.elfilibustero.uidesigner.adapters.AttributesAdapter;
import com.elfilibustero.uidesigner.adapters.PaletteListAdapter;
import com.elfilibustero.uidesigner.beans.ViewBean;
import com.elfilibustero.uidesigner.lib.builder.LayoutBuilder;
import com.elfilibustero.uidesigner.lib.handler.AttributeSetHandler;
import com.elfilibustero.uidesigner.lib.handler.ViewPaletteHandler;
import com.elfilibustero.uidesigner.lib.parser.LayoutParser;
import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.elfilibustero.uidesigner.lib.tool.ViewIdentifierFactory;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.ConstantsProperties;
import com.elfilibustero.uidesigner.lib.utils.SimpleTextWatcher;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.lib.view.DeviceView;
import com.elfilibustero.uidesigner.lib.view.PaletteButton;
import com.elfilibustero.uidesigner.ui.designer.items.UnknownView;
import com.elfilibustero.uidesigner.ui.designer.properties.IdsSelectorDialog;
import com.elfilibustero.uidesigner.ui.designer.properties.PropertyDialog;
import com.elfilibustero.uidesigner.ui.designer.properties.PropertyInputDialog;
import com.elfilibustero.uidesigner.ui.designer.properties.PropertyResourceDialog;
import com.elfilibustero.uidesigner.ui.designer.properties.PropertySelectorDialog;
import com.elfilibustero.uidesigner.ui.designer.properties.PropertySizeDialog;
import com.elfilibustero.uidesigner.ui.designer.tree.TreeAdapter;
import com.elfilibustero.uidesigner.ui.designer.tree.TreeNode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tscodeeditor.android.appstudio.vieweditor.R;
import com.tscodeeditor.android.appstudio.vieweditor.databinding.LayoutEditorBinding;
import com.tscodeeditor.android.appstudio.vieweditor.databinding.PropertyInputItemBinding;
import com.tscodeeditor.android.appstudio.vieweditor.databinding.ShowAttributesDialogBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LayoutDesigner extends RelativeLayout
        implements View.OnTouchListener, TreeAdapter.TreeItemClickListener {

    private LayoutEditorBinding binding;

    private RecyclerView recyclerView;
    private PaletteListAdapter adapter;
    private TreeAdapter treeAdapter;

    private DeviceView deviceView;
    private LayoutContainer editor;

    private GestureDetector gestureDetector;

    private AttributeSetHandler handler;

    private BottomSheetBehavior<View> mBehavior;

    private DynamicViewFactory viewFactory;
    private ResourceFactory resource;
    private ViewIdentifierFactory viewIdentifier;

    private List<ViewBean> customViews = new ArrayList<>();

    private View draggedView = null;

    private final OnBackPressedCallback onBackPressedCallback =
            new OnBackPressedCallback(false) {
                @Override
                public void handleOnBackPressed() {
                    if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        return;
                    }
                }
            };

    private float dip = 0.0f;
    private int totalHeight;
    private int totalWidth;
    private int minDist = 0;

    private float posInitX = 0.0f;
    private float posInitY = 0.0f;

    private boolean isDragged = false;
    private boolean isTouching = false;

    private Size currentSize;

    public LayoutDesigner(Context context) {
        super(context);
        init(context);
    }

    public LayoutDesigner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = LayoutEditorBinding.inflate(LayoutInflater.from(context), this, true);
        ResourceFactory.init(context);
        DynamicViewFactory.init(context);
        viewFactory = DynamicViewFactory.getInstance();
        resource = ResourceFactory.getInstance();

        dip = Utils.getDip(context, 1.0f);

        totalWidth = getResources().getDisplayMetrics().widthPixels;
        totalHeight = getResources().getDisplayMetrics().heightPixels;

        currentSize = Size.DEFAULT;

        deviceView = new DeviceView(context);
        binding.editorContainer.addView(deviceView);

        editor = new LayoutContainer(context);
        editor.setOnTouchListener(this);
        editor.setLayoutParams(
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
        deviceView.addContainer(editor);

        minDist = ViewConfiguration.get(context).getScaledTouchSlop();

        gestureDetector =
                new GestureDetector(
                        context,
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onSingleTapUp(MotionEvent e) {
                                if (!isDragged) {
                                    if (draggedView != null
                                            && !(draggedView instanceof PaletteButton)) {
                                        showPropertiesFor(draggedView);
                                    }
                                    dragCancel();
                                }
                                return true;
                            }

                            @Override
                            public void onLongPress(MotionEvent e) {
                                startDrag(draggedView);
                            }
                        });

        handler = new AttributeSetHandler(context);

        recyclerView = binding.paletteList;
        adapter = new PaletteListAdapter(this);
        treeAdapter = new TreeAdapter();
        treeAdapter.setOnTreeItemClickListener(this);
        recyclerView.setAdapter(adapter);
        setupPaletteContainer();
    }

    private void setupPaletteContainer() {
        mBehavior = BottomSheetBehavior.from(binding.paletteDrawer);
        mBehavior.setGestureInsetBottomIgnored(false);
        mBehavior.addBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                bottomSheet.setElevation(0);
                                onBackPressedCallback.setEnabled(false);
                                break;
                            case BottomSheetBehavior.STATE_DRAGGING:
                            case BottomSheetBehavior.STATE_EXPANDED:
                                bottomSheet.setElevation(Utils.getDip(getContext(), 12.0f));
                                onBackPressedCallback.setEnabled(true);
                                break;
                        }
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED
                                || newState == BottomSheetBehavior.STATE_EXPANDED) {
                            treeAdapter.submitList(populateTreeNode());
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
                });
        setupPaletteNavigationRail();
    }

    @SuppressWarnings("unchecked")
    private void setPaletteList(List<?> list) {
        if (list == null || list.isEmpty()) return;

        boolean isTreeNode = list.get(0) instanceof TreeNode;
        RecyclerView.LayoutManager layoutManager =
                isTreeNode
                        ? new LinearLayoutManager(getContext())
                        : new GridLayoutManager(getContext(), 2);
        recyclerView.setAdapter(isTreeNode ? treeAdapter : adapter);
        recyclerView.setLayoutManager(layoutManager);
        if (isTreeNode) treeAdapter.submitList((List<TreeNode>) list);
        else adapter.submitList((List<ViewBean>) list);
    }

    @SuppressWarnings("unchecked")
    private void setupPaletteNavigationRail() {
        var navigationRail = binding.navigationView;
        var addPaletteButton =
                (FloatingActionButton) navigationRail.getHeaderView().findViewById(R.id.fab);

        navigationRail.setOnItemSelectedListener(
                item -> {
                    int id = item.getItemId();
                    List<?> palettes;

                    if (id == R.id.menu_all) {
                        palettes = ViewPaletteHandler.getAll();
                    } else if (id == R.id.menu_layout) {
                        palettes = ViewPaletteHandler.getLayouts();
                    } else if (id == R.id.menu_androidx) {
                        palettes = ViewPaletteHandler.getAndroidx();
                    } else if (id == R.id.menu_widget) {
                        palettes = ViewPaletteHandler.getWidgets();
                    } else if (id == R.id.menu_list) {
                        palettes = ViewPaletteHandler.getLists();
                    } else if (id == R.id.menu_google) {
                        palettes = ViewPaletteHandler.getGoogle();
                    } else if (id == R.id.menu_tree) {
                        palettes = populateTreeNode();
                    } else {
                        palettes = ViewPaletteHandler.getAll();
                    }

                    if (id == R.id.menu_all) {
                        addPaletteButton.show();
                        ((List<ViewBean>) palettes).addAll(customViews);
                    } else {
                        addPaletteButton.hide();
                    }

                    setPaletteList(palettes);
                    return true;
                });

        navigationRail.setSelectedItemId(R.id.menu_all);
        addPaletteButton.setOnClickListener(v -> addNewWidget());
    }

    public void collapsePaletteSheet() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void addNewWidget() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("New Widget")
                .setView(R.layout.property_input_item)
                .setNegativeButton(
                        "Cancel",
                        (dialog, which) -> {
                            dialog.dismiss();
                        })
                .setPositiveButton(
                        "Add",
                        (dialog, which) -> {
                            var alertDialog = (AlertDialog) dialog;
                            TextInputEditText inputText = alertDialog.findViewById(R.id.input_text);
                            TextInputLayout inputLayout =
                                    alertDialog.findViewById(R.id.text_input_layout);

                            String widgetName = inputText.getText().toString();
                            if (!widgetName.isEmpty()) {
                                customViews.add(ViewPaletteHandler.createPalette(widgetName));
                                binding.navigationView.setSelectedItemId(
                                        binding.navigationView.getSelectedItemId());
                                dialog.dismiss();
                            } else {
                                inputLayout.setError("Please enter widget name");
                            }
                        })
                .show();
    }

    public void setLayoutFromXml(String xml) {
        LayoutParser parser = LayoutParser.with(editor);
        parser.addCallback(
                new LayoutParser.Callback() {
                    @Override
                    public void onLayoutParsed(Map<View, Map<String, Object>> viewMap) {
                        handler.setViewAttributes(viewMap);
                        viewMap.keySet()
                                .forEach(
                                        view -> {
                                            view.setOnTouchListener(LayoutDesigner.this);
                                            editor.setDefaultWidthAndHeight(view);
                                            editor.setTransition(view);
                                        });
                    }

                    @Override
                    public void onParsingComplete() {
                        viewIdentifier = parser.getViewIds();
                    }
                });
        parser.parse(xml);
    }

    public String getEditorSourceCode() {
        var builder = LayoutBuilder.with(editor);
        builder.build(handler.getViewMap());
        return builder.generate();
    }

    public void showSourceCode() {
        var sourceCodeText = new TextView(getContext());
        sourceCodeText.setText(getEditorSourceCode());
        var padding = (int) Utils.getDip(getContext(), 8.0f);
        sourceCodeText.setPadding(padding, padding, padding, padding);
        sourceCodeText.setTextIsSelectable(true);

        var scrollView = new ScrollView(getContext());
        scrollView.addView(sourceCodeText);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Source Code")
                .setView(scrollView)
                .setPositiveButton("Okay", (dialog, which) -> {})
                .show();
    }

    public void attachOnBackPressedToFragment(Fragment fragment) {
        fragment.requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(fragment, onBackPressedCallback);
    }

    public void attachOnBackPressedToActivity(AppCompatActivity activity) {
        activity.getOnBackPressedDispatcher().addCallback(activity, onBackPressedCallback);
    }

    public void detachOnBackPressed() {
        onBackPressedCallback.remove();
    }

    private void startDrag(View view) {
        if (isDragged) dragCancel();

        if (view == null) {
            return;
        }

        if (isDraggedFromEditor(view)) {
            var attrs = handler.get(view);
            if (attrs != null && !attrs.isEmpty()) {
                var id = (String) attrs.getOrDefault("android:id", null);
                viewIdentifier.remove((String) attrs.getOrDefault("android:id", null));
                if (id != null) {
                    var updatedViews =
                            findRelativeViewsById(id).entrySet().stream()
                                    .map(entry -> createView(entry.getKey(), entry.getValue()))
                                    .collect(Collectors.toList());

                    updatedViews.forEach(
                            currentView ->
                                    handler.applyAttributesFor(
                                            currentView, handler.get(currentView)));
                }
            }
            editor.removeItem(view);
        }

        isDragged = true;
        deviceView.removeSelection();
        editor.findDroppable(view);
        if (isTouching) {
            dragMove(posInitX, posInitY);
        } else {
            dragCancel();
        }
    }

    private void dragMove(float x, float y) {
        if (!isDraggedInEditor(x, y)) {
            editor.resetDroppable(true);
            return;
        }
        editor.updateDroppableFor((int) x, (int) y);
    }

    private void dragDropped(float x, float y) {
        editor.resetDroppable(false);
        if (isDraggedInEditor(x, y)) {
            addWidget(draggedView);
        } else if (!isDraggedInEditor(x, y) && isDraggedFromEditor(draggedView)) {
            removeWidget(draggedView);
        }
        dragCancel();
    }

    private void dragCancel() {
        draggedView = null;
        editor.resetDragging();
        isDragged = false;
    }

    public List<TreeNode> populateTreeNode() {
        List<TreeNode> nodes = new ArrayList<>();
        TreeNode node = new TreeNode(editor);
        node.setViewGroup(true);
        populateNode((ViewGroup) editor, node);
        return node.getChildren();
    }

    public void populateNode(ViewGroup group, TreeNode node) {
        for (int i = 0; i < group.getChildCount(); i++) {
            var childView = group.getChildAt(i);
            boolean isNotDefaultChild = handler.getViewMap().containsKey(childView);
            TreeNode childNode = new TreeNode(childView);
            if (childView instanceof ViewGroup viewGroup
                    && !Constants.isExcludedViewGroup(viewGroup)) {
                populateNode(viewGroup, isNotDefaultChild ? childNode : node);
                if (viewGroup instanceof UnknownView unknownView)
                    childNode.setViewGroup(unknownView.isViewGroup());
                else childNode.setViewGroup(true);
            } else {
                childNode.setViewGroup(false);
            }
            if (isNotDefaultChild) {
                node.addChild(childNode);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        totalWidth = getResources().getDisplayMetrics().widthPixels;
        totalHeight = getResources().getDisplayMetrics().heightPixels;

        var dimensions = calculateDimensions(currentSize, dip, totalWidth, totalHeight);

        int offsetX = dimensions.offsetX;
        int offsetY = dimensions.offsetY;
        int editorWidth = dimensions.editorWidth;
        int editorHeight = dimensions.editorHeight;

        float scale =
                Math.min(
                        (float) (editorWidth - offsetX * 2) / (float) totalWidth,
                        (float) (editorHeight - offsetY * 2) / (float) totalHeight);
        int scaleX = offsetX - (int) (((float) totalWidth - (float) totalWidth * scale) / 2.0F);
        int scaleY = offsetY - (int) ((totalHeight - scale * totalHeight) / 2.0F);

        deviceView.setLayoutParams(new FrameLayout.LayoutParams(totalWidth, totalHeight));
        deviceView.setScaleX(scale);
        deviceView.setScaleY(scale);
        deviceView.setY((float) scaleY);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isDragged && (draggedView != null)) {
            var size = new Point();
            var touchPoint = new Point();

            int width = draggedView.getMeasuredWidth();
            int height = draggedView.getMeasuredHeight();

            int defaultValue = (int) Utils.getDip(getContext(), 32.0f);

            if (width <= 0) {
                width = defaultValue;
            }

            if (height <= 0) {
                height = defaultValue;
            }

            size.set(width, height);
            touchPoint.set(width / 2, height / 2);

            var paint = new Paint();
            paint.setAlpha(128);
            canvas.save();
            canvas.translate(posInitX - touchPoint.x, posInitY - touchPoint.y);
            canvas.drawBitmap(getBitmapFromView(draggedView, width, height), 0, 0, paint);
            canvas.drawRect(0, 0, width, height, Utils.getDefaultStrokePaint(getContext()));
            canvas.restore();
        }
    }

    private Bitmap getBitmapFromView(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                posInitX = event.getX();
                posInitY = event.getY();
                isTouching = true;
                break;
            }
            case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isTouching = false;
                if (isDragged) dragCancel();
                break;
            }
        }
        return isDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDragged) {
            collapsePaletteSheet();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    posInitX = event.getX();
                    posInitY = event.getY();
                    dragMove(event.getRawX(), event.getRawY());
                }
                case MotionEvent.ACTION_UP -> dragDropped(event.getRawX(), event.getRawY());
                case MotionEvent.ACTION_CANCEL -> dragCancel();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        if (view == editor) {
            if (action != MotionEvent.ACTION_DOWN) return true;
            draggedView = null;
            return true;
        }
        if (action == MotionEvent.ACTION_DOWN) {
            isDragged = false;
            posInitX = event.getX();
            posInitY = event.getY();
            draggedView = view;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onItemClick(TreeNode node) {
        collapsePaletteSheet();
        showPropertiesFor(node.getView());
    }

    @Override
    public void onItemLongClick(TreeNode node, MotionEvent event) {
        isDragged = false;
        posInitX = event.getX();
        posInitY = event.getY();
        draggedView = node.getView();
        if (isDraggedFromEditor(draggedView)) {
            editor.removeItem(draggedView);
        }
        startDrag(draggedView);
    }

    @Override
    public void onItemExpandClick(TreeNode node) {}

    private void addWidget(View view) {
        if (view instanceof PaletteButton palette) {
            try {
                View newView = null;
                var bean = palette.getBean();
                var attrs = bean.getAttributes();

                int attrInt =
                        attrs.containsKey("style")
                                ? resource.getRes((String) attrs.get("style"), false)
                                : -1;

                if (attrInt != -1)
                    newView = (View) viewFactory.createView(bean.getClassName(), attrInt);
                else newView = (View) viewFactory.createView(bean.getClassName());

                if ((newView instanceof ImageView imageView) && !attrs.containsKey("android:src")) {
                    imageView.setImageDrawable(Utils.getDefaultImage());
                }

                if (editor.addItem(newView)) {
                    handler.addView(newView, attrs);
                    handler.applyAttributesFor(newView);
                    newView.setOnTouchListener(this);
                }
            } catch (ClassCastException e) {
            }
        } else {
            editor.addItem(view);
            handler.applyAttributesFor(view);
            var attrs = handler.get(view);
            if (attrs != null && !attrs.isEmpty()) {
                var id = (String) attrs.getOrDefault("android:id", null);
                if (id != null) {
                    var updatedViews =
                            findRelativeViewsById(id).entrySet().stream()
                                    .map(entry -> createView(entry.getKey(), entry.getValue()))
                                    .collect(Collectors.toList());

                    updatedViews.forEach(
                            currentView ->
                                    handler.applyAttributesFor(
                                            currentView, handler.get(currentView)));
                }
            }
        }
    }

    private void updateWidget(View view) {
        view.setVisibility(View.VISIBLE);
        if (editor.updateItem(view)) {
            handler.applyAttributesFor(view);
            updateLayout(view);
        }
    }

    private void removeWidget(View view) {
        removeWidgetAttributes(view);
    }

    private void removeWidgetAttributes(View view) {
        var attrs = handler.get(view);
        if (attrs != null && !attrs.isEmpty())
            viewIdentifier.remove((String) attrs.getOrDefault("android:id", null));
        handler.removeView(view);
        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                removeWidgetAttributes(group.getChildAt(i));
            }
        }
    }

    private boolean isDraggedFromEditor(View view) {
        if (view != null) {
            var parent = view.getParent();
            while (parent != null) {
                if (parent instanceof LayoutContainer) {
                    return true;
                }
                parent = parent.getParent();
            }
        }
        return false;
    }

    private boolean isDraggedInEditor(float x, float y) {
        var rect = new Rect();
        editor.getGlobalVisibleRect(rect);

        float editorWidth = editor.getWidth() * deviceView.getScaleX();
        float editorHeight = editor.getHeight() * deviceView.getScaleY();

        return x > rect.left
                && x < rect.left + editorWidth
                && y > rect.top
                && y < rect.top + editorHeight;
    }

    public void showPropertiesFor(View selectedView) {
        var allAttrs = handler.getAttributesFor(selectedView);
        var selectedAttributes = handler.get(selectedView);
        if (selectedAttributes == null) return;

        deviceView.select(selectedView);

        List<Pair<String, String>> attrs = new ArrayList<>();

        var dialog = new BottomSheetDialog(getContext());
        var binding = ShowAttributesDialogBinding.inflate(LayoutInflater.from(getContext()));

        var attributesList = binding.attributesList;
        var add = binding.add;
        var more = binding.more;

        dialog.setContentView(binding.getRoot());
        TooltipCompat.setTooltipText(add, "Add attribute");
        TooltipCompat.setTooltipText(more, "More");

        selectedAttributes
                .entrySet()
                .forEach(attr -> attrs.add(new Pair<>(attr.getKey(), attr.getValue().toString())));

        var attributesAdapter = new AttributesAdapter(attrs);

        attributesAdapter.setOnItemClickListener(
                new AttributesAdapter.ItemClickListener() {
                    @Override
                    public void onClick(Pair<String, String> attribute) {
                        showPropertyDialogFor(selectedView, attribute);
                        dialog.dismiss();
                    }

                    @Override
                    public void onLongClick(Pair<String, String> attribute, int position) {
                        attributesAdapter.removeItem(position);
                        new MaterialAlertDialogBuilder(getContext())
                                .setTitle(R.string.common_title_delete)
                                .setMessage(R.string.common_msg_delete)
                                .setCancelable(false)
                                .setNegativeButton(
                                        R.string.common_text_no,
                                        (d, w) ->
                                                attributesAdapter.restoreItem(attribute, position))
                                .setPositiveButton(
                                        R.string.common_text_yes,
                                        (d, w) -> {
                                            dialog.dismiss();
                                            updateView(selectedView, attribute, true);
                                        })
                                .show();
                    }
                });

        attributesList.setAdapter(attributesAdapter);
        attributesList.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        var viewName = DynamicViewFactory.getSimpleName(selectedView);

        binding.icon.setImageResource(ViewPaletteHandler.getIcon(viewName));
        binding.viewName.setText(viewName);
        add.setOnClickListener(
                v -> {
                    addNewAttributesFor(selectedView);
                    dialog.dismiss();
                });

        more.setOnClickListener(
                v -> {
                    var popupMenu = new PopupMenu(getContext(), more);

                    popupMenu
                            .getMenuInflater()
                            .inflate(R.menu.menu_properties, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(
                            menuItem -> {
                                var id = menuItem.getItemId();
                                if (id == R.id.front) {
                                    editor.bringToFront(selectedView);
                                } else if (id == R.id.delete) {
                                }
                                return true;
                            });
                    popupMenu.show();
                });

        dialog.show();
        dialog.setOnDismissListener(
                d -> {
                    deviceView.removeSelection();
                });
    }

    private void updateView(
            View selectedView, Pair<String, String> updatedAttribute, boolean delete) {
        var attrs = handler.get(selectedView);

        var attributeName = updatedAttribute.first;
        var attributeValue = updatedAttribute.second;

        var preAttributeValue =
                attrs.containsKey(attributeName) ? attrs.get(attributeName).toString() : null;

        if (!delete) {
            attrs.put(attributeName, attributeValue);
        } else {
            attrs.remove(attributeName);
        }

        if ("android:id".equals(attributeName)) {
            viewIdentifier.remove(preAttributeValue);
            if (!delete) viewIdentifier.register(selectedView, attributeValue);

            var updatedViews =
                    findRelativeViewsById(delete ? preAttributeValue : attributeValue)
                            .entrySet()
                            .stream()
                            .map(entry -> createView(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList());

            updatedViews.forEach(
                    currentView ->
                            handler.applyAttributesFor(currentView, handler.get(currentView)));
        }

        var newView = createView(selectedView, attrs);
        handler.applyAttributesFor(newView, handler.get(newView));

        ThreadUtils.runOnUiThreadDelayed(() -> showPropertiesFor(newView), 100);
    }

    private View createView(View view, Map<String, Object> attrs) {
        View newView = null;
        int attrInt =
                attrs.containsKey("style")
                        ? resource.getRes((String) attrs.get("style"), false)
                        : -1;

        if (attrInt != -1)
            newView = (View) viewFactory.createView(DynamicViewFactory.getName(view), attrInt);
        else newView = (View) viewFactory.createView(DynamicViewFactory.getName(view));

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) {
            handler.removeView(view);
            return null;
        }
        int index = parent.indexOfChild(view);

        if (attrs.containsKey("android:id")) {
            String id = attrs.get("android:id").toString();
            if (view.getId() != View.NO_ID) {
                viewIdentifier.update(newView, id, view.getId());
            } else {
                viewIdentifier.register(newView, id);
            }
        }

        List<View> children = new ArrayList<>();

        if (view instanceof ViewGroup viewGroup && !Constants.isExcludedViewGroup(viewGroup)) {
            children =
                    IntStream.range(0, viewGroup.getChildCount())
                            .mapToObj(viewGroup::getChildAt)
                            .collect(Collectors.toList());
            viewGroup.removeAllViews();
        }

        parent.removeView(view);
        handler.removeView(view);

        newView.setLayoutParams(
                (ViewGroup.LayoutParams)
                        DynamicViewFactory.getLayoutParamsFor(
                                parent,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

        parent.addView(newView, index);
        handler.addView(newView, attrs);

        if (view instanceof ViewGroup viewGroup && !Constants.isExcludedViewGroup(viewGroup)) {
            ViewGroup newGroup = (ViewGroup) newView;
            newGroup.removeAllViews();
            children.stream()
                    .filter(child -> !Constants.isExcludedViewGroup(newGroup))
                    .forEach(newGroup::addView);
        }

        if ((newView instanceof ImageView imageView) && !attrs.containsKey("android:src")) {
            imageView.setImageDrawable(Utils.getDefaultImage());
        }

        newView.setOnTouchListener(this);
        editor.setDefaultWidthAndHeight(newView);
        editor.setTransition(newView);
        return newView;
    }

    private void updateLayout(View view) {
        var parent = (ViewGroup) view.getParent();
        parent.invalidate();
        parent.requestLayout();
    }

    private Map<View, Map<String, Object>> findRelativeViewsById(String id) {
        if (id == null) return Collections.emptyMap();

        return handler.getViewMap().entrySet().stream()
                .filter(
                        entry ->
                                entry.getValue().values().stream()
                                        .anyMatch(value -> isMatchingId(value.toString(), id)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isMatchingId(String value, String id) {
        return value.startsWith("@id/") && value.equals(id.replace("+", ""));
    }

    private void showPropertyDialogFor(View selectedView, Pair<String, String> attribute) {
        var attributeName = ResourceFactory.parseReferName(attribute.first, ":");
        var propertyDialog = getPropertyDialog(selectedView, attributeName);
        propertyDialog.setName(attribute.first);
        propertyDialog.setValue(attribute.second);
        propertyDialog.addSaveCallback(
                newValue -> {
                    if (!attribute.second.equals(newValue)) {
                        updateView(selectedView, new Pair<>(attribute.first, newValue), false);
                    } else {
                        showPropertiesFor(selectedView);
                    }
                    propertyDialog.dismiss();
                });
        propertyDialog.show();
        propertyDialog.get().setCancelable(false);
        propertyDialog
                .get()
                .getButton(DialogInterface.BUTTON_NEGATIVE)
                .setOnClickListener(
                        v -> {
                            showPropertiesFor(selectedView);
                            propertyDialog.dismiss();
                        });
    }

    private PropertyDialog getPropertyDialog(View selectedView, String attributeName) {
        if (ConstantsProperties.isPropertySize(attributeName)) {
            return new PropertySizeDialog(getContext());
        } else if (ConstantsProperties.isPropertyEnum(attributeName)) {
            return new PropertySelectorDialog(getContext(), Constants.SELECTOR_TYPE_ENUM);
        } else if (ConstantsProperties.isPropertyFlag(attributeName)) {
            return new PropertySelectorDialog(getContext(), Constants.SELECTOR_TYPE_FLAG);
        } else if (ConstantsProperties.isConstraintProperty(attributeName)) {
            return new IdsSelectorDialog(
                    getContext(),
                    Constants.SELECTOR_TYPE_CONSTRAINT,
                    findAvailableIdsInParentFor(selectedView));
        } else if (ConstantsProperties.isRelativeProperty(attributeName)) {
            return new IdsSelectorDialog(
                    getContext(),
                    Constants.SELECTOR_TYPE_RELATIVE,
                    findAvailableIdsInParentFor(selectedView));
        } else if (ConstantsProperties.isPropertyResource(attributeName)) {
            return new PropertyResourceDialog(getContext());
        } else if (ConstantsProperties.isPropertyNumber(attributeName)) {
            return new PropertyInputDialog(getContext(), Constants.INPUT_TYPE_NUMBER);
        } else if (ConstantsProperties.isPropertyFloat(attributeName)) {
            return new PropertyInputDialog(getContext(), Constants.INPUT_TYPE_FLOAT);
        } else if (attributeName.equals("id")) {
            return new PropertyInputDialog(getContext(), Constants.INPUT_TYPE_ID);
        } else {
            return new PropertyInputDialog(getContext(), Constants.INPUT_TYPE_TEXT);
        }
    }

    private List<String> findAvailableIdsInParentFor(View view) {
        List<String> ids = new ArrayList<>();
        var parent = (ViewGroup) view.getParent();
        var viewMap = handler.getViewMap();

        if (parent != null) {
            ids =
                    IntStream.range(0, parent.getChildCount())
                            .mapToObj(parent::getChildAt)
                            .filter(childView -> view != childView)
                            .flatMap(
                                    childView -> {
                                        var attributes =
                                                viewMap.getOrDefault(
                                                        childView, Collections.emptyMap());
                                        String id =
                                                (String)
                                                        attributes.getOrDefault("android:id", null);
                                        return id != null && !id.isEmpty()
                                                ? Stream.of(ResourceFactory.parseReferName(id))
                                                : Stream.empty();
                                    })
                            .collect(Collectors.toList());
        }

        return ids;
    }

    private void addNewAttributesFor(View selectedView) {
        List<String> allAttrs = new ArrayList<>(handler.getAvailableAttributesFor(selectedView));
        Collections.sort(allAttrs);

        var listView = new ListView(getContext());
        listView.setDividerHeight(0);
        listView.setAdapter(
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allAttrs));
        listView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        var propertyInput = PropertyInputItemBinding.inflate(LayoutInflater.from(getContext()));
        var textInput = propertyInput.textInputLayout;
        textInput.setHint("Enter attribute name");
        var input = textInput.getEditText();

        var root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, (int) Utils.getDip(getContext(), 16.0f), 0, 0);
        root.addView(listView);
        root.addView(propertyInput.getRoot());

        var builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Add new attribute");
        builder.setView(root);
        builder.setPositiveButton(
                "Add",
                (d, i) ->
                        showPropertyDialogFor(
                                selectedView, Pair.create(input.getText().toString(), "")));
        builder.setNegativeButton("Cancel", (d, i) -> showPropertiesFor(selectedView));
        var dialog = builder.create();
        dialog.setOnShowListener(
                d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false));
        dialog.setCancelable(false);
        dialog.show();

        input.addTextChangedListener(
                new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable p1) {
                        String text = input.getText().toString();

                        if (text.equals("")) {
                            textInput.setErrorEnabled(true);
                            textInput.setError("Field cannot be empty!");
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            return;
                        }

                        textInput.setErrorEnabled(false);
                        textInput.setError("");
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                });

        listView.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    input.setText(allAttrs.get(i));
                    input.setSelection(input.getText().length());
                });
    }

    public enum Size {
        SMALL,
        DEFAULT,
        LARGE
    }

    public void setSize(Size size) {
        if (currentSize != size) {
            currentSize = size;
            requestLayout();
        }
    }

    private class DesignerDimensions {
        int offsetX;
        int offsetY;
        int editorWidth;
        int editorHeight;

        public DesignerDimensions(int offsetX, int offsetY, int editorWidth, int editorHeight) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.editorWidth = editorWidth;
            this.editorHeight = editorHeight;
        }
    }

    private DesignerDimensions calculateDimensions(
            Size currentSize, float dip, int totalWidth, int totalHeight) {

        return switch (currentSize) {
            case SMALL -> new DesignerDimensions(
                    (int) (dip * 16.0f),
                    (int) (dip * 28.0f),
                    totalWidth - (int) (150.0f * dip),
                    totalHeight - (int) (dip * 60.0f) - (int) (dip * 60.0f));
            case DEFAULT -> new DesignerDimensions(
                    (int) (dip * 12.0f),
                    (int) (dip * 20.0f),
                    totalWidth - (int) (120.0f * dip),
                    totalHeight - (int) (dip * 48.0f) - (int) (dip * 48.0f));
            case LARGE -> new DesignerDimensions(
                    (int) (dip * 8.0f),
                    (int) (dip * 12.0f),
                    totalWidth - (int) (90.0f * dip),
                    totalHeight - (int) (dip * 36.0f) - (int) (dip * 36.0f));
        };
    }

    public AttributeSetHandler getAttributeSetHandler() {
        return this.handler;
    }

    public LayoutContainer getEditor() {
        return this.editor;
    }
}
