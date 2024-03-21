package com.tscodeeditor.android.appstudio.block.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.tscodeeditor.android.appstudio.block.R;
import com.tscodeeditor.android.appstudio.block.editor.EventEditor;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;

public class BlockView extends LinearLayout {
  private EventEditor editor;
  private Context context;
  private BlockModel blockModel;

  public BlockView(EventEditor editor, Context context, BlockModel blockModel) {
    super(context);
    this.editor = editor;
    this.context = context;
    this.blockModel = blockModel.clone();
    updateBlock();
  }

  public BlockView(EventEditor editor, Context context) {
    super(context);
    this.editor = editor;
    this.context = context;
  }

  public void updateBlock() {
    removeAllViews();
    setOrientation(LinearLayout.VERTICAL);

    if (getBlockModel() == null) return;

    if (getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
      if (getBlockModel().isFirstBlock()) {
        LinearLayout firstBlockTop = new LinearLayout(getContext());
        Drawable firstBlockTopDrawable =
            ContextCompat.getDrawable(getContext(), R.drawable.block_first_top);
        firstBlockTopDrawable.setTint(Color.parseColor(getBlockModel().getColor()));
        firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
        firstBlockTop.setBackground(firstBlockTopDrawable);
        addView(firstBlockTop);
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
}
