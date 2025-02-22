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

package com.icst.android.appstudio.block.view;

import java.util.ArrayList;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import com.icst.android.appstudio.block.utils.BlockMarginConstants;
import com.icst.android.appstudio.block.utils.ColorPalleteUtils;
import com.icst.android.appstudio.block.utils.LayerBuilder;
import com.icst.android.appstudio.block.utils.TargetUtils;
import com.icst.android.appstudio.block.utils.UnitUtils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

public class BlockView extends LinearLayout {
	private EventEditor editor;
	private Context context;
	private BlockModel blockModel;
	private ArrayList<LinearLayout> droppables;
	private LinearLayout blockTop;
	private boolean enableDragDrop;
	private boolean enableEditing;
	private boolean isInsideEditor;
	private boolean isRootBlock;
	private boolean darkMode;
	private float x, y;

	public BlockView(EventEditor editor, Context context, BlockModel blockModel, boolean darkMode) {
		super(context);
		this.editor = editor;
		this.context = context;
		this.blockModel = blockModel;
		this.darkMode = darkMode;
		updateBlock();
	}

	public BlockView(EventEditor editor, Context context, boolean darkMode) {
		super(context);
		this.editor = editor;
		this.context = context;
		this.darkMode = darkMode;
	}

	public void updateBlock() {
		removeAllViews();
		setOrientation(LinearLayout.VERTICAL);
		droppables = new ArrayList<LinearLayout>();

		if (getBlockModel() == null)
			return;

		if (getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {

			addBlockTopView();

			for (int layerCount = 0; layerCount < getBlockModel().getBlockLayerModel().size(); ++layerCount) {
				LinearLayout layerLayout = new LinearLayout(getContext());
				layerLayout.setOrientation(LinearLayout.VERTICAL);
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layerLayout.setLayoutParams(layoutParams);

				if (getBlockModel().getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {

					LayerBuilder.buildBlockFieldLayer(
							this,
							((BlockFieldLayerModel) getBlockModel().getBlockLayerModel().get(layerCount)),
							getBlockModel(),
							layerLayout,
							droppables,
							layerCount,
							darkMode);

					if (blockTop != null) {
						layerLayout
								.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										() -> {
											ViewGroup.LayoutParams lp = blockTop.getLayoutParams();
											lp.width = layerLayout.getWidth();
											blockTop.setLayoutParams(lp);
										});
					}

				} else if (getBlockModel().getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
					LayerBuilder.buildBlockHolderLayer(
							this,
							((BlockHolderLayer) getBlockModel().getBlockLayerModel().get(layerCount)),
							getBlockModel(),
							layerLayout,
							droppables,
							layerCount,
							darkMode);
				}
				addView(layerLayout);
			}

			addBlockBottomView();
		} else if (getBlockModel().getBlockType() == BlockModel.Type.defaultBoolean
				|| getBlockModel().getBlockType() == BlockModel.Type.number
				|| getBlockModel().getBlockType() == BlockModel.Type.variable) {

			if (getBlockModel().getBlockType() == BlockModel.Type.defaultBoolean) {
				Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_boolean);
				drawable.setTint(
						ColorPalleteUtils.transformColor(getBlockModel().getColor(), editor.isDarkMode()));
				drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				setBackground(drawable);
			} else if (getBlockModel().getBlockType() == BlockModel.Type.number) {
				Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_number);
				drawable.setTint(
						ColorPalleteUtils.transformColor(getBlockModel().getColor(), editor.isDarkMode()));
				drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				setBackground(drawable);
			} else if (getBlockModel().getBlockType() == BlockModel.Type.variable) {
				Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_variable);
				drawable.setTint(
						ColorPalleteUtils.transformColor(getBlockModel().getColor(), editor.isDarkMode()));
				drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				setBackground(drawable);
			}

			for (int layerCount = 0; layerCount < getBlockModel().getBlockLayerModel().size(); ++layerCount) {
				LinearLayout layerLayout = new LinearLayout(getContext());
				layerLayout.setOrientation(LinearLayout.VERTICAL);
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layerLayout.setLayoutParams(layoutParams);

				if (getBlockModel().getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {

					LayerBuilder.buildBlockFieldLayer(
							this,
							((BlockFieldLayerModel) getBlockModel().getBlockLayerModel().get(layerCount)),
							getBlockModel(),
							layerLayout,
							droppables,
							layerCount,
							darkMode);

				} else if (getBlockModel().getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
					LayerBuilder.buildBlockHolderLayer(
							this,
							((BlockHolderLayer) getBlockModel().getBlockLayerModel().get(layerCount)),
							getBlockModel(),
							layerLayout,
							droppables,
							layerCount,
							darkMode);
				}
				addView(layerLayout);
			}
		}
		setDragListener();
	}

	public void addBlockTopView() {
		if (!(getBlockModel().getBlockType() == BlockModel.Type.defaultBlock))
			return;
		if (getBlockModel().isFirstBlock()) {
			LinearLayout firstBlockTop = new LinearLayout(getContext());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			firstBlockTop.setLayoutParams(layoutParams);
			Drawable firstBlockTopDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_first_top);
			firstBlockTopDrawable.setTint(
					ColorPalleteUtils.transformColor(getBlockModel().getColor(), darkMode));
			firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			firstBlockTop.setBackground(firstBlockTopDrawable);
			addView(firstBlockTop);
		} else {
			blockTop = new LinearLayout(getContext());
			ViewGroup.LayoutParams _lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			blockTop.setLayoutParams(_lp);
			Drawable blockTopDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_default_top);
			blockTopDrawable.setTint(
					ColorPalleteUtils.transformColor(getBlockModel().getColor(), darkMode));
			blockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			blockTop.setBackground(blockTopDrawable);
			addView(blockTop);
		}
	}

	public void addBlockBottomView() {
		if (!(getBlockModel().getBlockType() == BlockModel.Type.defaultBlock))
			return;
		if (!getBlockModel().isLastBlock()) {
			LinearLayout blockBottomJoint = new LinearLayout(getContext());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			blockBottomJoint.setLayoutParams(layoutParams);
			Drawable blockBottomJointDrawable = ContextCompat.getDrawable(getContext(),
					R.drawable.block_default_bottom_joint);
			blockBottomJointDrawable.setTint(
					ColorPalleteUtils.transformColor(getBlockModel().getColor(), darkMode));
			blockBottomJointDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			blockBottomJoint.setBackground(blockBottomJointDrawable);
			addView(blockBottomJoint);
		}
	}

	public void setDragListener() {
		Runnable dragStartRunnable = new Runnable() {
			@Override
			public void run() {
				editor.startBlockDrag(
						BlockView.this,
						BlockView.this.getBlockModel(),
						x
								+ TargetUtils.getRelativePosition(BlockView.this, editor)[0]
								- UnitUtils.dpToPx(getContext(), 60),
						y
								+ TargetUtils.getRelativePosition(BlockView.this, editor)[1]
								- UnitUtils.dpToPx(getContext(), 80));
			}
		};
		Handler dragHandler = new Handler();

		setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						x = event.getX();
						y = event.getY();
						if (getEnableDragDrop()
								&& getBlockModel().isDragAllowed()
								&& editor.getEvent() != null
								&& editor.getEvent().getEnableEdit()) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								if (!editor.isDragging) {
									dragHandler.postDelayed(
											dragStartRunnable, ViewConfiguration.getLongPressTimeout());
								}
							}
							if (event.getAction() == MotionEvent.ACTION_UP) {
								dragHandler.removeCallbacks(dragStartRunnable);
								if (editor.isDragging) {
									editor.stopDrag(
											x
													+ TargetUtils.getRelativePosition(BlockView.this, editor)[0]
													- UnitUtils.dpToPx(getContext(), 60),
											y
													+ TargetUtils.getRelativePosition(BlockView.this, editor)[1]
													- UnitUtils.dpToPx(getContext(), 80));
								}
							}
							if (event.getAction() == MotionEvent.ACTION_CANCEL) {
								dragHandler.removeCallbacks(dragStartRunnable);
								if (editor.isDragging) {
									editor.stopDrag(
											x
													+ TargetUtils.getRelativePosition(BlockView.this, editor)[0]
													- UnitUtils.dpToPx(getContext(), 60),
											y
													+ TargetUtils.getRelativePosition(BlockView.this, editor)[1]
													- UnitUtils.dpToPx(getContext(), 80));
								}
							}

							if (event.getAction() == MotionEvent.ACTION_MOVE) {
								if (editor.isDragging) {
									editor.moveFloatingBlockView(
											x
													+ TargetUtils.getRelativePosition(BlockView.this, editor)[0]
													- UnitUtils.dpToPx(getContext(), 60),
											y
													+ TargetUtils.getRelativePosition(BlockView.this, editor)[1]
													- UnitUtils.dpToPx(getContext(), 80));
								}
							}
						}
						return true;
					}
				});
	}

	public static void setDrawable(View view, int res, int color) {
		Drawable drawable = ContextCompat.getDrawable(view.getContext(), res);
		drawable.setTint(color);
		drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
		view.setBackground(drawable);
	}

	public boolean drop(float x, float y, BlockModel toDrop) {

		for (int i = 0; i < droppables.size(); ++i) {

			if (droppables.get(i).getTag() instanceof BlockDroppableTag) {

				BlockDroppableTag tag = (BlockDroppableTag) droppables.get(i).getTag();

				if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
					if (toDrop.getBlockType() == BlockModel.Type.defaultBlock) {
						if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {

							int index = 0;
							for (int ind = 0; ind < droppables.get(i).getChildCount(); ind++) {
								View child = droppables.get(i).getChildAt(ind);
								int[] relativePos = TargetUtils.getRelativePosition(droppables.get(i).getChildAt(ind),
										editor);
								if (y > relativePos[1] + (child.getHeight() / 2)) {
									index = ind + 1;
								} else {
									break;
								}
							}
							dropBlockView(index, x, y, droppables.get(i), tag, toDrop);
							return true;
						}
					} else if (toDrop.getBlockType() == BlockModel.Type.defaultBoolean
							|| toDrop.getBlockType() == BlockModel.Type.number
							|| toDrop.getBlockType() == BlockModel.Type.variable) {
						if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
							for (int ind = 0; ind < droppables.get(i).getChildCount(); ind++) {
								View child = droppables.get(i).getChildAt(ind);
								if (TargetUtils.isDragInsideTargetView(child, editor, x, y)) {
									if (child instanceof BlockView) {
										BlockView blockView = (BlockView) child;
										return blockView.drop(x, y, toDrop);
									}
									break;
								}
							}
						}
					}
				} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_BOOLEAN_DROPPER) {
					if (toDrop.getBlockType() == BlockModel.Type.defaultBoolean) {
						if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
							if (tag.getDropProperty(BlockValueFieldModel.class).getBlockModel() == null) {

								if (editor.draggingBlock.isInsideEditor()) {
									((ViewGroup) editor.draggingBlock.getParent()).removeView(editor.draggingBlock);
								}

								BlockView draggingBlockView = new BlockView(
										editor,
										getContext(),
										editor.draggingBlock.getBlockModel().clone(),
										darkMode);

								draggingBlockView.setEnableDragDrop(true);
								draggingBlockView.setEnableEditing(true);
								draggingBlockView.setInsideEditor(true);

								droppables.get(i).addView(draggingBlockView);
								return true;

							} else {
								if (!((BooleanView) droppables.get(i)).getBooleanBlock().drop(x, y, toDrop)) {
									if (editor.draggingBlock.isInsideEditor()) {
										((ViewGroup) editor.draggingBlock.getParent()).removeView(editor.draggingBlock);
									}
									BlockView draggingBlockView = new BlockView(
											editor,
											getContext(),
											editor.draggingBlock.getBlockModel().clone(),
											darkMode);

									draggingBlockView.setEnableDragDrop(true);
									draggingBlockView.setEnableEditing(true);
									draggingBlockView.setInsideEditor(true);

									droppables.get(i).addView(draggingBlockView);
									return true;
								}
							}
						}
					}
				} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_NUMBER_DROPPER) {
					if (toDrop.getBlockType() == BlockModel.Type.number) {

						if (ArrayUtils.ifContainAnyElement(
								tag.getDropProperty(BlockValueFieldModel.class).getAcceptors(),
								toDrop.getReturns())) {

							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								if (tag.getDropProperty(BlockValueFieldModel.class).getBlockModel() == null) {

									if (editor.draggingBlock.isInsideEditor()) {
										((ViewGroup) editor.draggingBlock.getParent()).removeView(editor.draggingBlock);
									}

									BlockView draggingBlockView = new BlockView(
											editor,
											getContext(),
											editor.draggingBlock.getBlockModel().clone(),
											darkMode);

									draggingBlockView.setEnableDragDrop(true);
									draggingBlockView.setEnableEditing(true);
									draggingBlockView.setInsideEditor(true);

									droppables.get(i).addView(draggingBlockView);
									return true;

								} else {
									if (!((NumberView) droppables.get(i)).getNumberBlock().drop(x, y, toDrop)) {
										if (editor.draggingBlock.isInsideEditor()) {
											((ViewGroup) editor.draggingBlock.getParent())
													.removeView(editor.draggingBlock);
										}
										BlockView draggingBlockView = new BlockView(
												editor,
												getContext(),
												editor.draggingBlock.getBlockModel().clone(),
												darkMode);

										draggingBlockView.setEnableDragDrop(true);
										draggingBlockView.setEnableEditing(true);
										draggingBlockView.setInsideEditor(true);

										droppables.get(i).addView(draggingBlockView);
										return true;
									}
								}
							}
						}
					}
				} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_VARIABLE_DROPPER) {
					if (toDrop.getBlockType() == BlockModel.Type.variable) {

						if (ArrayUtils.ifContainAnyElement(
								tag.getDropProperty(BlockValueFieldModel.class).getAcceptors(),
								toDrop.getReturns())) {

							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								if (tag.getDropProperty(BlockValueFieldModel.class).getBlockModel() == null) {

									if (editor.draggingBlock.isInsideEditor()) {
										((ViewGroup) editor.draggingBlock.getParent()).removeView(editor.draggingBlock);
									}

									BlockView draggingBlockView = new BlockView(
											editor,
											getContext(),
											editor.draggingBlock.getBlockModel().clone(),
											darkMode);

									draggingBlockView.setEnableDragDrop(true);
									draggingBlockView.setEnableEditing(true);
									draggingBlockView.setInsideEditor(true);

									droppables.get(i).addView(draggingBlockView);
									return true;

								} else {
									if (!((BlockVariableFieldView) droppables.get(i)).getBlock().drop(x, y, toDrop)) {
										if (editor.draggingBlock.isInsideEditor()) {
											((ViewGroup) editor.draggingBlock.getParent())
													.removeView(editor.draggingBlock);
										}
										BlockView draggingBlockView = new BlockView(
												editor,
												getContext(),
												editor.draggingBlock.getBlockModel().clone(),
												darkMode);

										draggingBlockView.setEnableDragDrop(true);
										draggingBlockView.setEnableEditing(true);
										draggingBlockView.setInsideEditor(true);

										droppables.get(i).addView(draggingBlockView);
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public void dropBlockView(
			int index,
			float x,
			float y,
			ViewGroup target,
			BlockDroppableTag targetTag,
			BlockModel blockModel) {
		boolean isDropConsumed = false;
		if (target.getChildCount() > index) {
			if (target.getChildAt(index) instanceof BlockView) {
				BlockView blockView = (BlockView) target.getChildAt(index);
				if (blockView.drop(x, y, blockModel)) {
					isDropConsumed = true;
				}
			}
		}

		if (!isDropConsumed) {
			if (target.getChildCount() > (index - 1)) {
				if (target.getChildAt((index - 1)) instanceof BlockView) {
					BlockView blockView = (BlockView) target.getChildAt((index - 1));
					if (blockView.drop(x, y, blockModel)) {
						isDropConsumed = true;
					}
				}
			}
		}

		if (!isDropConsumed) {
			if (target.getChildCount() > (index + 1)) {
				if (target.getChildAt((index + 1)) instanceof BlockView) {
					BlockView blockView = (BlockView) target.getChildAt((index + 1));
					if (blockView.drop(x, y, blockModel)) {
						isDropConsumed = true;
					}
				}
			}
		}

		if (!isDropConsumed) {
			BlockView block = new BlockView(editor, getContext(), blockModel.clone(), darkMode);

			block.setEnableDragDrop(true);
			block.setEnableEditing(true);
			block.setInsideEditor(true);
			if (targetTag.getDropProperty(BlockHolderLayer.class).getBlocks() == null) {
				ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
				targetTag.getDropProperty(BlockHolderLayer.class).setBlocks(blocks);
			}
			target.addView(block, index);
			targetTag
					.getDropProperty(BlockHolderLayer.class)
					.getBlocks()
					.add(index, block.getBlockModel());
			if (editor.draggingBlock.isInsideEditor()) {
				if (((ViewGroup) editor.draggingBlock.getParent()).getTag() != null) {
					if (((ViewGroup) editor.draggingBlock.getParent()).getTag() instanceof BlockDroppableTag) {
						if (((BlockDroppableTag) ((ViewGroup) editor.draggingBlock.getParent()).getTag())
								.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
							((BlockDroppableTag) ((ViewGroup) editor.draggingBlock.getParent()).getTag())
									.getDropProperty(BlockHolderLayer.class)
									.getBlocks()
									.remove(
											((ViewGroup) editor.draggingBlock.getParent())
													.indexOfChild(editor.draggingBlock));
						}
					}
				}
			}
			if (editor.draggingBlock.isInsideEditor()) {
				((ViewGroup) editor.draggingBlock.getParent()).removeView(editor.draggingBlock);
			}
		}
	}

	public boolean preview(float x, float y, BlockModel toDrop) {
		for (int i = 0; i < droppables.size(); ++i) {

			if (droppables.get(i).getTag() != null) {
				if (droppables.get(i).getTag() instanceof BlockDroppableTag) {

					BlockDroppableTag tag = (BlockDroppableTag) droppables.get(i).getTag();

					if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
						if (toDrop.getBlockType() == BlockModel.Type.defaultBlock) {
							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								editor.blockPreview.removePreview();
								editor.blockPreview.setBlock(toDrop);

								int index = 0;
								for (int ind = 0; ind < droppables.get(i).getChildCount(); ind++) {
									View child = droppables.get(i).getChildAt(ind);
									int[] relativePos = TargetUtils
											.getRelativePosition(droppables.get(i).getChildAt(ind), editor);
									if (y > relativePos[1] + (child.getHeight() / 2)) {
										index = ind + 1;
									} else {
										break;
									}
								}

								setBlockPreview(index, x, y, droppables.get(i), toDrop);
								return true;
							}
						} else if (toDrop.getBlockType() == BlockModel.Type.defaultBoolean
								|| toDrop.getBlockType() == BlockModel.Type.number
								|| toDrop.getBlockType() == BlockModel.Type.variable) {
							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								for (int ind = 0; ind < droppables.get(i).getChildCount(); ind++) {
									View child = droppables.get(i).getChildAt(ind);
									if (TargetUtils.isDragInsideTargetView(child, editor, x, y)) {
										if (child instanceof BlockView) {
											BlockView blockView = (BlockView) child;
											return blockView.preview(x, y, toDrop);
										}
										break;
									}
								}
							}
						}
					} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_BOOLEAN_DROPPER) {
						if (toDrop.getBlockType() == BlockModel.Type.defaultBoolean) {
							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								if (tag.getDropProperty(BlockValueFieldModel.class).getBlockModel() == null) {
									editor.blockPreview.removePreview();
									editor.blockPreview.setBlock(toDrop);
									droppables.get(i).addView(editor.blockPreview);
									return true;
								} else {
									if (!((BooleanView) droppables.get(i)).getBooleanBlock().preview(x, y, toDrop)) {
										editor.blockPreview.removePreview();
										editor.blockPreview.setBlock(toDrop);
										droppables.get(i).addView(editor.blockPreview);
										return true;
									}
								}
							}
						}
					} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_NUMBER_DROPPER) {
						if (toDrop.getBlockType() == BlockModel.Type.number) {
							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								if (ArrayUtils.ifContainAnyElement(
										tag.getDropProperty(BlockValueFieldModel.class).getAcceptors(),
										toDrop.getReturns())) {

									if (tag.getDropProperty(BlockValueFieldModel.class).getBlockModel() == null) {
										editor.blockPreview.removePreview();
										editor.blockPreview.setBlock(toDrop);
										droppables.get(i).addView(editor.blockPreview);
										return true;
									} else {
										if (!((NumberView) droppables.get(i)).getNumberBlock().preview(x, y, toDrop)) {
											editor.blockPreview.removePreview();
											editor.blockPreview.setBlock(toDrop);
											droppables.get(i).addView(editor.blockPreview);
											return true;
										}
									}
								}
							}
						}
					} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_VARIABLE_DROPPER) {
						if (toDrop.getBlockType() == BlockModel.Type.variable) {
							if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
								if (ArrayUtils.ifContainAnyElement(
										tag.getDropProperty(BlockValueFieldModel.class).getAcceptors(),
										toDrop.getReturns())) {

									if (tag.getDropProperty(BlockValueFieldModel.class).getBlockModel() == null) {
										editor.blockPreview.removePreview();
										editor.blockPreview.setBlock(toDrop);
										droppables.get(i).addView(editor.blockPreview);
										return true;
									} else {
										if (!((BlockVariableFieldView) droppables.get(i))
												.getBlock()
												.preview(x, y, toDrop)) {
											editor.blockPreview.removePreview();
											editor.blockPreview.setBlock(toDrop);
											droppables.get(i).addView(editor.blockPreview);
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public void setBlockPreview(
			int index, float x, float y, ViewGroup target, BlockModel blockModel) {
		boolean isPreviewConsumed = false;
		if (target.getChildCount() > index) {
			if (target.getChildAt(index) instanceof BlockView) {
				BlockView blockView = (BlockView) target.getChildAt(index);
				if (blockView.preview(x, y, blockModel)) {
					isPreviewConsumed = true;
				}
			}
		}

		if (!isPreviewConsumed) {
			if (target.getChildCount() > (index - 1)) {
				if (target.getChildAt((index - 1)) instanceof BlockView) {
					BlockView blockView = (BlockView) target.getChildAt((index - 1));
					if (blockView.preview(x, y, blockModel)) {
						isPreviewConsumed = true;
					}
				}
			}
		}

		if (!isPreviewConsumed) {
			if (target.getChildCount() > (index + 1)) {
				if (target.getChildAt((index + 1)) instanceof BlockView) {
					BlockView blockView = (BlockView) target.getChildAt((index + 1));
					if (blockView.preview(x, y, blockModel)) {
						isPreviewConsumed = true;
					}
				}
			}
		}

		if (!isPreviewConsumed) {
			target.addView(editor.blockPreview, index);
		}
	}

	@Override
	public void setVisibility(int arg) {
		super.setVisibility(arg);
		if (getParent() == null)
			return;
		if (!(getParent() instanceof LinearLayout))
			return;
		LinearLayout parent = ((LinearLayout) getParent());
		if (parent.indexOfChild(this) != 0)
			return;
		if (parent.getTag() == null)
			return;
		if (!(parent.getTag() instanceof BlockDroppableTag))
			return;
		BlockDroppableTag tag = ((BlockDroppableTag) parent.getTag());
		if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
			if (parent.getChildAt(1) == null)
				return;
			if (arg == View.GONE) {
				parent
						.getChildAt(1)
						.setLayoutParams(
								new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.WRAP_CONTENT));
			} else if (arg == View.VISIBLE) {
				LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				blockParams.setMargins(
						0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);
				parent.getChildAt(1).setLayoutParams(blockParams);
			}
		} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_BOOLEAN_DROPPER) {
			if (parent instanceof BooleanView) {
				((BooleanView) parent).ensureFieldBackground();
			}
		} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_NUMBER_DROPPER) {
			if (parent instanceof NumberView) {
				((NumberView) parent).ensureFieldBackground();
			}
		} else if (tag.getBlockDroppableType() == BlockDroppableTag.BLOCK_VARIABLE_DROPPER) {
			if (parent instanceof BlockVariableFieldView) {
				((BlockVariableFieldView) parent).ensureFieldBackground();
			}
		}
	}

	public EventEditor getEditor() {
		return this.editor;
	}

	public void setEditor(EventEditor editor) {
		this.editor = editor;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public BlockModel getBlockModel() {
		return this.blockModel;
	}

	public void setBlockModel(BlockModel blockModel) {
		this.blockModel = blockModel.clone();
	}

	public boolean getEnableDragDrop() {
		return this.enableDragDrop;
	}

	public void setEnableDragDrop(boolean enableDragDrop) {
		this.enableDragDrop = enableDragDrop;
	}

	public boolean getEnableEditing() {
		return this.enableEditing;
	}

	public void setEnableEditing(boolean enableEditing) {
		this.enableEditing = enableEditing;
	}

	public boolean isInsideEditor() {
		return this.isInsideEditor;
	}

	public void setInsideEditor(boolean isInsideEditor) {
		this.isInsideEditor = isInsideEditor;
	}

	public boolean isRootBlock() {
		return this.isRootBlock;
	}

	public void setRootBlock(boolean isRootBlock) {
		this.isRootBlock = isRootBlock;
	}
}
