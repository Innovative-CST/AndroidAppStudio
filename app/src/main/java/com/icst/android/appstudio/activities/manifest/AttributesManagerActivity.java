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

package com.icst.android.appstudio.activities.manifest;

import java.util.ArrayList;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.adapters.xml.XmlValuesAdapter;
import com.icst.android.appstudio.bottomsheet.XmlAttributeOperationBottomSheet;
import com.icst.android.appstudio.bottomsheet.XmlElementOperationBottomSheet;
import com.icst.android.appstudio.databinding.ActivityAttributeManagerBinding;
import com.icst.android.appstudio.xml.XmlAttributeModel;
import com.icst.android.appstudio.xml.XmlModel;

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

		changesCallback = registerForActivityResult(
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
			XmlAttributeOperationBottomSheet sheet = new XmlAttributeOperationBottomSheet(
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
			XmlElementOperationBottomSheet sheet = new XmlElementOperationBottomSheet(
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
