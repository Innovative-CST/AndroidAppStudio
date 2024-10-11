/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.logic.block.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.EventBlockBean;
import com.icst.logic.lib.builder.LayerBuilder;
import com.icst.logic.lib.config.LogicEditorConfiguration;
import com.icst.logic.utils.BlockImageUtills;
import com.icst.logic.utils.ImageViewUtils;
import java.util.ArrayList;

public class EventBlockBeanView extends LinearLayout {
  private Context context;
  private EventBlockBean eventBlockBean;
  private LogicEditorConfiguration configuration = new LogicEditorConfiguration();

  public EventBlockBeanView(Context context, EventBlockBean eventBlockBean) {
    super(context);
    this.context = context;
    this.eventBlockBean = eventBlockBean;
    init();
  }

  private void init() {
    setOrientation(VERTICAL);
    LinearLayout header = new LinearLayout(context);
    LinearLayout.LayoutParams headerLayoutParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, // Width
            LinearLayout.LayoutParams.WRAP_CONTENT // Height
            );
    header.setBackgroundDrawable(
        ImageViewUtils.getImageView(
            context,
            eventBlockBean.getColor(),
            BlockImageUtills.getImage(BlockImageUtills.Image.EVENT_BLOCK_ROUND_EDGE_TOP)));
    header.setLayoutParams(headerLayoutParams);
    addView(header);

    ArrayList<BlockElementLayerBean> layers = eventBlockBean.getElementsLayers();

    for (int i = 0; i < layers.size(); ++i) {
      BlockElementLayerBean elementLayer = layers.get(i);
      View layerView = LayerBuilder.buildBlockLayerView(context, elementLayer, configuration);

      LinearLayout.LayoutParams layerLayoutParams =
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, // Width
              LinearLayout.LayoutParams.WRAP_CONTENT // Height
              );
      header.setLayoutParams(layerLayoutParams);
      addView(layerView);
    }
  }

  private void setEventBlockBean(EventBlockBean eventBlockBean) {
    this.eventBlockBean = eventBlockBean;
    removeAllViews();
    init();
  }
}
