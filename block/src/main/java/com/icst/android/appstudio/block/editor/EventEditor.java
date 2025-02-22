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

package com.icst.android.appstudio.block.editor;

import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.adapter.BlocksHolderAdapter;
import com.icst.android.appstudio.block.databinding.EventEditorLayoutBinding;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockHolderModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;
import com.icst.android.appstudio.block.utils.TargetUtils;
import com.icst.android.appstudio.block.view.BlockDragView;
import com.icst.android.appstudio.block.view.BlockPreview;
import com.icst.android.appstudio.block.view.BlockView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

public class EventEditor extends RelativeLayout {

	public EventEditorLayoutBinding binding;
	public BlockDragView blockFloatingView;
	public BlockView draggingBlock;
	public BlockPreview blockPreview;
	public HashMap<String, Object> variables;
	private boolean isDarkMode;

	public boolean isDragging = false;
	private boolean isBlockPallateVisible = false;

	// Contants for showing the section easily
	public static final int LOADING_SECTION = 0;
	public static final int INFO_SECTION = 1;
	public static final int EDITOR_SECTION = 2;
	public static final int VALUE_EDITOR_SECTION = 3;

	public EventEditor(final Context context, final AttributeSet set) {
		super(context, set);

		binding = EventEditorLayoutBinding.inflate(LayoutInflater.from(context));
		blockFloatingView = new BlockDragView(context, null);
		blockPreview = new BlockPreview(this);
		binding.getRoot().setClipChildren(true);
		addView(binding.getRoot());
		setMatchParent(binding.getRoot());
		switchSection(EDITOR_SECTION);
		binding.fab.setOnClickListener(
				v -> {
					isBlockPallateVisible = !isBlockPallateVisible;
					showBlocksPallete(isBlockPallateVisible);
				});
		invalidate();
	}

	private void setMatchParent(View view) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(layoutParams);
	}

	/*
	 * Method for switching the section quickly.
	 */
	public void switchSection(int section) {
		binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
		binding.info.setVisibility(section == INFO_SECTION ? View.VISIBLE : View.GONE);
		binding.editorSection.setVisibility(section == EDITOR_SECTION ? View.VISIBLE : View.GONE);
		binding.valueEditorSection.setVisibility(
				section == VALUE_EDITOR_SECTION ? View.VISIBLE : View.GONE);
		if (section == VALUE_EDITOR_SECTION) {
			showBlocksPallete(false);
			binding.fab.setVisibility(View.GONE);
		} else {
			showBlocksPallete(isBlockPallateVisible);
			binding.fab.setVisibility(View.VISIBLE);
		}
	}

	public void showBlocksPallete(boolean show) {
		binding.blockArea.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public void initEditor(Event event, boolean darkMode, HashMap<String, Object> variables) {
		setVariables(variables);
		setDarkMode(darkMode);
		binding.canva.initEditor(event, this);
	}

	public void setHolder(ArrayList<BlockHolderModel> holderList) {
		binding.blocksHolderList.setAdapter(new BlocksHolderAdapter(holderList, this));
		binding.blocksHolderList.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	public void startBlockDrag(BlockView draggingBlock, BlockModel block, float x, float y) {
		if (draggingBlock.isInsideEditor()) {
			draggingBlock.setVisibility(View.GONE);
		}
		int notAllowedIconWidth = 0;
		if (blockFloatingView.notAllowed != null) {
			if (blockFloatingView.notAllowed.getParent() != null) {
				notAllowedIconWidth = blockFloatingView.notAllowed.getWidth();
			}
		}

		binding.canva.setDisableScrollForcefully(true);
		binding.blockList.requestDisallowInterceptTouchEvent(true);
		isDragging = true;
		this.draggingBlock = draggingBlock;
		blockFloatingView.setBlock(block);
		addView(blockFloatingView);
		RelativeLayout.LayoutParams blockFloatingViewParam = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		blockFloatingView.setLayoutParams(blockFloatingViewParam);
		blockFloatingView.setX(x - notAllowedIconWidth);
		blockFloatingView.setY(y);
		blockFloatingView.setAllowed(isBlockFloatingViewInsideCanva(x, y));
	}

	public void stopDrag(float x, float y) {
		isDragging = false;
		binding.canva.setDisableScrollForcefully(false);
		binding.blockList.requestDisallowInterceptTouchEvent(false);
		blockPreview.removePreview();
		removeView(blockFloatingView);
		drop(x, y);
		draggingBlock = null;
	}

	public void dropInCanva(float x, float y) {
		if (draggingBlock.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
			LinearLayout droppable = new LinearLayout(getContext());
			droppable.setId(R.id.notAttachedBlockLayout);
			droppable.setOrientation(LinearLayout.VERTICAL);

			FrameLayout.LayoutParams droppableParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

			BlockView block = new BlockView(this, getContext(), draggingBlock.getBlockModel().clone(), isDarkMode());

			droppableParams.setMargins(
					(int) x + binding.canva.getScrollX() - binding.codeEditor.getPaddingStart(),
					(int) y + binding.canva.getScrollY() - binding.codeEditor.getPaddingTop(),
					0,
					0);

			block.setEnableDragDrop(true);
			block.setEnableEditing(true);
			block.setInsideEditor(true);

			droppable.addView(block);
			binding.canva.addView(droppable);
			droppable.setLayoutParams(droppableParams);

			if (((ViewGroup) draggingBlock.getParent()).getTag() != null) {
				if (((ViewGroup) draggingBlock.getParent()).getTag() instanceof BlockDroppableTag) {
					BlockDroppableTag tag = ((BlockDroppableTag) ((ViewGroup) draggingBlock.getParent()).getTag());

					if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER
							&& block.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
						tag.getDropProperty(BlockHolderLayer.class)
								.getBlocks()
								.remove(((ViewGroup) draggingBlock.getParent()).indexOfChild(draggingBlock));
					}
				}
			}
		} else {
			FrameLayout.LayoutParams blockParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

			BlockView block = new BlockView(this, getContext(), draggingBlock.getBlockModel().clone(), isDarkMode());

			blockParams.setMargins(
					(int) x + binding.canva.getScrollX() - binding.codeEditor.getPaddingStart(),
					(int) y + binding.canva.getScrollY() - binding.codeEditor.getPaddingTop(),
					0,
					0);

			block.setEnableDragDrop(true);
			block.setEnableEditing(true);
			block.setInsideEditor(true);
			binding.canva.addView(block);
			block.setLayoutParams(blockParams);
		}
	}

	public void drop(float x, float y) {
		if (isBlockFloatingViewInsideCanva(x, y)) {

			/*
			 * Drop is made inside the canva view.
			 * Make sure attachedBlockLayout is initialized before operations on it.
			 * If attachedBlockLayout is null then skip the drop.
			 * If editing of event is disabled then skip drop.
			 */

			if (binding.canva.attachedBlockLayout == null) {
				return;
			}

			if (!binding.canva.getEvent().getEnableEdit()) {
				return;
			}

			/*
			 * Drop will not be cancelled any how since all the requirement is met
			 */

			// Check every children of canva for droppables
			boolean droppableAvailable = false;
			for (int i = binding.canva.getChildCount() - 1; i >= 0; i--) {
				if (binding.canva.getChildAt(i) instanceof BlockView dropPointingBlock) {

					if (dropPointingBlock.drop(x, y, draggingBlock.getBlockModel())) {
						droppableAvailable = true;
					}

				} else if ((binding.canva.getChildAt(i).getId() == R.id.notAttachedBlockLayout
						|| binding.canva.getChildAt(i).getId() == R.id.attachedBlockLayout)
						&& binding.canva.getChildAt(i) instanceof LinearLayout dropView) {

					if (TargetUtils.isDragInsideTargetView(dropView, binding.getRoot(), (int) x, (int) y)) {

						int index = 0;
						for (int i2 = 0; i2 < dropView.getChildCount(); i2++) {
							View child = dropView.getChildAt(i2);
							if (y + binding.canva.getScrollY() > dropView.getY() + child.getY()
									+ (child.getHeight() / 2)) {
								index = i2 + 1;
							} else {
								break;
							}
						}

						dropBlockView(index, dropView, x, y);

						droppableAvailable = true;
					}
				}
			}

			if (!droppableAvailable) {
				dropInCanva(x, y);
			}
			/*
			 * Removing block from original position if it was dragged from inside the
			 * editor
			 * as drop is successfully made.
			 */
			if (draggingBlock.isInsideEditor()) {
				if (draggingBlock.getParent() != null) {
					((ViewGroup) draggingBlock.getParent()).removeView(draggingBlock);
				}
			}
		} else {

			/*
			 * Show the dragging view on its original position since drag is made
			 * outside the canva where drop is not allowed.
			 * But make sure that the blocks was dragged from canva not from pallete list.
			 */

			if (draggingBlock.isInsideEditor()) {
				draggingBlock.setVisibility(View.VISIBLE);
			}
		}
	}

	public void dropBlockView(int index, LinearLayout fallBackTarget, float x, float y) {

		if (index == 0 && fallBackTarget.getId() == R.id.attachedBlockLayout) {
			index = 1;
		}

		boolean isDropConsumed = false;
		if (fallBackTarget.getChildCount() > index) {
			if (fallBackTarget.getChildAt(index) instanceof BlockView) {
				BlockView blockView = (BlockView) fallBackTarget.getChildAt(index);
				if (blockView.drop(x, y, draggingBlock.getBlockModel())) {
					isDropConsumed = true;
				}
			}
		}

		if (!isDropConsumed) {
			if (fallBackTarget.getChildCount() > (index - 1)) {
				if (fallBackTarget.getChildAt(index - 1) instanceof BlockView) {
					BlockView blockView = (BlockView) fallBackTarget.getChildAt(index - 1);
					if (blockView.drop(x, y, draggingBlock.getBlockModel())) {
						isDropConsumed = true;
					}
				}
			}
		}

		if (!isDropConsumed) {
			if (fallBackTarget.getChildCount() > (index + 1)) {
				if (fallBackTarget.getChildAt(index + 1) instanceof BlockView) {
					BlockView blockView = (BlockView) fallBackTarget.getChildAt(index + 1);
					if (blockView.drop(x, y, draggingBlock.getBlockModel())) {
						isDropConsumed = true;
					}
				}
			}
		}

		if (!isDropConsumed
				&& draggingBlock.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
			BlockView block = new BlockView(this, getContext(), draggingBlock.getBlockModel().clone(), isDarkMode());

			block.setEnableDragDrop(true);
			block.setEnableEditing(true);
			block.setInsideEditor(true);
			if (((ViewGroup) draggingBlock.getParent()).getTag() != null) {
				if (((ViewGroup) draggingBlock.getParent()).getTag() instanceof BlockDroppableTag) {
					BlockDroppableTag tag = ((BlockDroppableTag) ((ViewGroup) draggingBlock.getParent()).getTag());

					if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER
							&& block.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
						tag.getDropProperty(BlockHolderLayer.class)
								.getBlocks()
								.remove(((ViewGroup) draggingBlock.getParent()).indexOfChild(draggingBlock));
					}
				}
			}

			fallBackTarget.addView(block, index);
			if (draggingBlock.isInsideEditor()) {
				((ViewGroup) draggingBlock.getParent()).removeView(draggingBlock);
			}
		}
	}

	public void moveFloatingBlockView(float x, float y) {
		int notAllowedIconWidth = 0;
		if (blockFloatingView.notAllowed != null) {
			if (blockFloatingView.notAllowed.getParent() != null) {
				notAllowedIconWidth = blockFloatingView.notAllowed.getWidth();
			}
		}
		blockFloatingView.setX(x - notAllowedIconWidth);
		blockFloatingView.setY(y);
		blockFloatingView.setAllowed(isBlockFloatingViewInsideCanva(x, y));

		blockPreview.removePreview();
		blockPreview.setBlock(draggingBlock.getBlockModel());

		if (isBlockFloatingViewInsideCanva(x, y)) {

			/*
			 * Drag is made inside the canva view.
			 * Make sure attachedBlockLayout is initialized before operations on it.
			 * If attachedBlockLayout is null then skip the drop.
			 * If editing of event is disabled then skip drop.
			 */

			if (binding.canva.attachedBlockLayout == null) {
				return;
			}

			if (!binding.canva.getEvent().getEnableEdit()) {
				return;
			}

			/*
			 * Preview may be cancelled if not droppable found.
			 */

			// Check every children of canva for droppables
			boolean droppableAvailable = false;
			for (int i = binding.canva.getChildCount() - 1; i >= 0; i--) {
				if (binding.canva.getChildAt(i) instanceof BlockView dropPointingBlock) {

					if (dropPointingBlock.preview(x, y, draggingBlock.getBlockModel())) {
						droppableAvailable = true;
					}

				} else if ((binding.canva.getChildAt(i).getId() == R.id.notAttachedBlockLayout
						|| binding.canva.getChildAt(i).getId() == R.id.attachedBlockLayout)
						&& binding.canva.getChildAt(i) instanceof LinearLayout dropView) {

					if (TargetUtils.isDragInsideTargetView(dropView, binding.getRoot(), (int) x, (int) y)) {

						int index = 0;
						for (int i2 = 0; i2 < dropView.getChildCount(); i2++) {
							View child = dropView.getChildAt(i2);
							if (y + binding.canva.getScrollY() > child.getY() + dropView.getY()
									+ (child.getHeight() / 2)) {
								index = i2 + 1;
							} else {
								break;
							}
						}

						setBlockPreview(index, dropView, x, y);

						droppableAvailable = true;
					}
				}
			}

			if (!droppableAvailable) {
				blockPreview.removePreview();
			}
		} else {

			/*
			 * Remove preview since it is outside the drop region.
			 */

			blockPreview.removePreview();
		}
	}

	public void setBlockPreview(int index, LinearLayout fallBackTarget, float x, float y) {

		if (index == 0 && fallBackTarget.getId() == R.id.attachedBlockLayout) {
			index = 1;
		}

		boolean isPreviewConsumed = false;
		if (fallBackTarget.getChildCount() > index) {
			if (fallBackTarget.getChildAt(index) instanceof BlockView) {
				BlockView blockView = (BlockView) fallBackTarget.getChildAt(index);
				if (blockView.preview(x, y, draggingBlock.getBlockModel())) {
					isPreviewConsumed = true;
				}
			}
		}

		if (!isPreviewConsumed) {
			if (fallBackTarget.getChildCount() > (index - 1)) {
				if (fallBackTarget.getChildAt(index - 1) instanceof BlockView) {
					BlockView blockView = (BlockView) fallBackTarget.getChildAt(index - 1);
					if (blockView.preview(x, y, draggingBlock.getBlockModel())) {
						isPreviewConsumed = true;
					}
				}
			}
		}

		if (!isPreviewConsumed) {
			if (fallBackTarget.getChildCount() > (index + 1)) {
				if (fallBackTarget.getChildAt(index + 1) instanceof BlockView) {
					BlockView blockView = (BlockView) fallBackTarget.getChildAt(index + 1);
					if (blockView.preview(x, y, draggingBlock.getBlockModel())) {
						isPreviewConsumed = true;
					}
				}
			}
		}

		if (!isPreviewConsumed
				&& draggingBlock.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
			fallBackTarget.addView(blockPreview, index);
		}
	}

	public boolean isBlockFloatingViewInsideCanva(float x, float y) {
		int notAllowedIconWidth = 0;
		if (blockFloatingView.notAllowed != null) {
			if (blockFloatingView.notAllowed.getParent() != null) {
				notAllowedIconWidth = blockFloatingView.notAllowed.getWidth();
			}
		}

		return TargetUtils.isDragInsideTargetView(
				binding.editorSection, this, (int) x + notAllowedIconWidth, (int) y);
	}

	public void loadBlocksInEvent() {
		if (binding.canva.attachedBlockLayout == null)
			return;
		if (binding.canva.getEvent() == null)
			return;

		ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
		for (int i = 0; i < binding.canva.attachedBlockLayout.getChildCount(); ++i) {
			if (i == 0)
				continue;
			if (binding.canva.attachedBlockLayout.getChildAt(i) instanceof BlockView) {
				blocks.add(((BlockView) binding.canva.attachedBlockLayout.getChildAt(i)).getBlockModel());
			}
		}
		binding.canva.getEvent().setBlockModels(blocks);
	}

	public Event getEvent() {
		return binding.canva.getEvent();
	}

	public HashMap<String, Object> getVariables() {
		return this.variables;
	}

	public void setVariables(HashMap<String, Object> variables) {
		this.variables = variables;
	}

	public boolean isDarkMode() {
		return this.isDarkMode;
	}

	public void setDarkMode(boolean isDarkMode) {
		this.isDarkMode = isDarkMode;
	}
}
