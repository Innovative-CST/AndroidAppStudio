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

package com.icst.android.appstudio.activities.manifest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.adapters.xml.XmlValuesAdapter;
import com.icst.android.appstudio.bottomsheet.XmlAttributeOperationBottomSheet;
import com.icst.android.appstudio.bottomsheet.XmlElementOperationBottomSheet;
import com.icst.android.appstudio.databinding.ActivityAttributeManagerBinding;
import com.icst.android.appstudio.xml.XmlAttributeModel;
import com.icst.android.appstudio.xml.XmlModel;
import java.util.ArrayList;

public class AttributesManagerActivity extends BaseActivity {

  private ActivityAttributeManagerBinding binding;
  private XmlValuesAdapter adapter;
  private XmlModel xmlModel;
  public ActivityResultLauncher<Intent> changesCallback;
  public int position;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityAttributeManagerBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    if (!getIntent().hasExtra("xmlModel")) {
      Toast.makeText(this, "Error", 0).show();
      finish();
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        xmlModel = getIntent().getSerializableExtra("xmlModel", XmlModel.class);
      } else {
        xmlModel = (XmlModel) getIntent().getSerializableExtra("xmlModel");
      }

      load();
    }

    changesCallback =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                XmlModel xml = (XmlModel) intent.getSerializableExtra("xmlModel");
                xmlModel.getChildren().set(position, xml);
                load();
              }
            });
  }

  public void load() {
    adapter = new XmlValuesAdapter(xmlModel, getIntent().getStringExtra("tag"), this);
    binding.listView.setAdapter(adapter);
    binding.listView.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  public void onBackPressed() {
    Intent result = new Intent();
    result.putExtra("xmlModel", adapter.getXml());
    setResult(RESULT_OK, result);
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.activity_menu_xml_attribute, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.new_attribute) {
      XmlAttributeOperationBottomSheet sheet =
          new XmlAttributeOperationBottomSheet(
              this,
              new XmlAttributeOperationBottomSheet.XmlAttributeOperation() {
                @Override
                public void onDeleteAttribute() {
                  load();
                }

                @Override
                public void onModifyAttribute(XmlAttributeModel xmlAttributeModel) {
                  if (xmlModel.getAttributes() != null) {
                    xmlModel.getAttributes().add(xmlAttributeModel);
                  } else {
                    ArrayList<XmlAttributeModel> attrs = new ArrayList<XmlAttributeModel>();
                    attrs.add(xmlAttributeModel);
                    xmlModel.setAttributes(attrs);
                  }

                  load();
                }
              },
              null);
      sheet.show();
    } else if (menuItem.getItemId() == R.id.new_element) {
      XmlElementOperationBottomSheet sheet =
          new XmlElementOperationBottomSheet(
              this,
              new XmlElementOperationBottomSheet.XmlElementOperation() {
                @Override
                public void onDelete() {
                  load();
                }

                @Override
                public void onModify(XmlModel xml) {
                  if (xmlModel.getChildren() != null) {
                    xmlModel.getChildren().add(xml);
                  } else {
                    ArrayList<XmlModel> childs = new ArrayList<XmlModel>();
                    childs.add(xml);
                    xmlModel.setChildren(childs);
                  }

                  load();
                }
              },
              null);
      sheet.show();
    }
    return super.onOptionsItemSelected(menuItem);
  }
}
