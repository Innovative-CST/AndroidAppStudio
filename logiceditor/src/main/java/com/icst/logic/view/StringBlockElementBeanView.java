/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.view;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.android.appstudio.beans.StringBlockElementBean;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.sheet.InputFieldBottomSheet;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StringBlockElementBeanView extends LinearLayout {
	private TextView label;
	private StringBlockBeanView strBlockView;
	private BlockBeanView blockView;
	private StringBlockElementBean mStringBlockElementBean;
	private LogicEditorConfiguration logicEditorConfiguration;
	private LogicEditorView logicEditor;
	private InputFieldBottomSheet currentSheet;

	public StringBlockElementBeanView(
			Context context,
			BlockBeanView blockView,
			StringBlockElementBean mStringBlockElementBean,
			LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		super(context);
		this.mStringBlockElementBean = mStringBlockElementBean;
		this.blockView = blockView;
		this.logicEditorConfiguration = configuration;
		this.logicEditor = logicEditor;
		setMinimumWidth(UnitUtils.dpToPx(getContext(), 20));
		setBackgroundColor(Color.WHITE);
		setGravity(Gravity.CENTER_VERTICAL);
		setPadding(2, 2, 2, 2);
		label = new TextView(context);
		label.setTextSize(configuration.getTextSize().getTextSize());
		setOnClickListener(
				v -> {
					if (blockView.isInsideCanva()) {
						currentSheet = new InputFieldBottomSheet(
								getContext(),
								mStringBlockElementBean,
								new InputFieldBottomSheet.ValueListener() {
									@Override
									public void onChange(String value) {
										setValue(value);
									}
								});
						currentSheet.show();
					}
				});
		init();
	}

	private void init() {
		removeAllViews();
		if (mStringBlockElementBean.getStringBlock() != null) {
			createStringBlockView();
			configureStringBlockView();
			addStringBlockViewToLayout();
			showStringBlock();
		} else {
			addLabelViewToLayout();
			showLabel();
			updateLabelText();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (currentSheet != null && currentSheet.isShowing()) {
			currentSheet.dismiss();
		}
		currentSheet = null;
	}

	private void createStringBlockView() {
		strBlockView = new StringBlockBeanView(
				getContext(),
				mStringBlockElementBean.getStringBlock(),
				logicEditorConfiguration,
				logicEditor) {
			@Override
			public void setVisibility(int arg0) {
				super.setVisibility(arg0);
				handleStringBlockVisibility();
			}
		};
	}

	private void configureStringBlockView() {
		LayoutParams lp = generateDefaultLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		strBlockView.setLayoutParams(lp);
		strBlockView.setInsideCanva(true);
	}

	private void addStringBlockViewToLayout() {
		if (strBlockView.getParent() == null) {
			addView(strBlockView);
		}
		label.setText("");
		removeView(label);
	}

	private void showStringBlock() {
		strBlockView.setVisibility(VISIBLE);
	}

	private void invisibleStringBlockIfVisible() {
		if (strBlockView == null) {
			return;
		}
		int visibility = strBlockView.getVisibility();
		if (visibility == VISIBLE) {
			strBlockView.setVisibility(INVISIBLE);
		}
	}

	private void showStringBlockIfInvisible() {
		int visibility = strBlockView.getVisibility();
		if (visibility == INVISIBLE) {
			strBlockView.setVisibility(VISIBLE);
		}
	}

	private void handleStringBlockVisibility() {
		int visibility = strBlockView.getVisibility();
		if (visibility == GONE) {
			if (label.getParent() == null) {
				addView(label);
			}
			label.setText("");
		} else if (visibility == VISIBLE) {
			removeView(label);
		}
	}

	private void addLabelViewToLayout() {
		if (label.getParent() == null) {
			addView(label);
		}
		removeView(strBlockView);
	}

	private void showLabel() {
		label.setVisibility(VISIBLE);
	}

	private void invisibeLabel() {
		label.setVisibility(INVISIBLE);
	}

	private void updateLabelText() {
		if (mStringBlockElementBean.getString() != null) {
			label.setText(mStringBlockElementBean.getString());
		} else {
			label.setText("");
		}
	}

	public void setValue(String str) {
		if (str == null)
			return;
		mStringBlockElementBean.setValue(str);
		strBlockView = null;
		init();
	}

	public void setValue(StringBlockBean strBlock) {
		if (strBlock == null)
			return;
		if (strBlockView != null) {
			strBlockView.setOnTouchListener(null);
		}
		mStringBlockElementBean.setStringBlock(strBlock);
		init();
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(UnitUtils.dpToPx(getContext(), 4), 0, UnitUtils.dpToPx(getContext(), 4), 0);
		return lp;
	}

	public StringBlockElementBean getStringBlockElementBean() {
		return mStringBlockElementBean;
	}

	public boolean canDrop(BlockBean block, float x, float y) {
		if (block == null)
			return false;
		if (block != mStringBlockElementBean.getStringBlock()) {
			if (strBlockView != null) {
				if (strBlockView.canDrop(block, x, y))
					return true;
			}
		}
		if (block instanceof ExpressionBlockBean expressionBlock) {
			return expressionBlock.getReturnDatatype()
					.isSuperTypeOrDatatype(mStringBlockElementBean.getAcceptedReturnType());
		}
		return false;
	}

	public void highlight(BlockBean block, float x, float y) {
		if (block != mStringBlockElementBean.getStringBlock()) {
			if (mStringBlockElementBean.getStringBlock() != null) {
				if (strBlockView.canDrop(block, x, y)) {
					strBlockView.highlightNearestTarget(block, x, y);
					return;
				}
			}
		}
		if (block instanceof StringBlockBean stringBlockBean) {
			invisibeLabel();
			invisibleStringBlockIfVisible();
			setBackgroundColor(Color.BLACK);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), block);
			logicEditor.setDummyHighlighter(highlighter);
			addView(highlighter);
		}
	}

	public void dropBlockIfAllowed(BlockBean block, float x, float y) {
		if (block != mStringBlockElementBean.getStringBlock()) {
			if (mStringBlockElementBean.getStringBlock() != null) {
				if (strBlockView.canDrop(block, x, y)) {
					strBlockView.drop(block, x, y);
					return;
				}
			}
		}
		if (block instanceof StringBlockBean stringBlockBean) {
			setValue(stringBlockBean);
		}
	}

	private void handleHighlighterRemoval() {
		if (mStringBlockElementBean.getString() != null) {
			showLabel();
			updateLabelText();
		}

		if (mStringBlockElementBean.getStringBlock() != null) {
			showStringBlockIfInvisible();
		}
		setBackgroundColor(Color.WHITE);
	}

	@Override
	public void removeView(View view) {
		super.removeView(view);
		if (view instanceof NearestTargetHighlighterView) {
			handleHighlighterRemoval();
		}
	}
}
