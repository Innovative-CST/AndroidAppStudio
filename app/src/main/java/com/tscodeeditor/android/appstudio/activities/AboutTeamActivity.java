/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.tscodeeditor.android.appstudio.activities;

import android.code.editor.utils.RequestNetwork;
import android.code.editor.utils.RequestNetworkController;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.R;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;
import org.json.JSONException;

public class AboutTeamActivity extends BaseActivity {
  private LinearLayout main;

  private LinearLayout loading;

  private RecyclerView list;

  public String contributorsData =
      "https://raw.githubusercontent.com/TS-Code-Editor/AndroidAppStudio/main/assets/contributors.json";

  public RequestNetwork reqNetwork;

  public RequestNetwork.RequestListener reqListener;

  public ArrayList<HashMap<String, Object>> contributorsList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    setContentView(R.layout.activity_about_team);
    initActivity();
  }

  public void initActivity() {
    // Initialze views in layout
    init();
    main.setVisibility(View.GONE);
    loading.setVisibility(View.VISIBLE);

    reqNetwork = new RequestNetwork(this);

    reqListener =
        new RequestNetwork.RequestListener() {
          @Override
          public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
            final String _tag = _param1;
            final String _response = _param2;
            final HashMap<String, Object> _responseHeaders = _param3;
            try {
              loading.setVisibility(View.GONE);
              main.setVisibility(View.VISIBLE);
              JSONObject object = new JSONObject(_response);
              JSONArray contributors = object.getJSONArray("Contributors");
              for (int count = 0; count < contributors.length(); count++) {
                {
                  HashMap<String, Object> _item = new HashMap<>();
                  _item.put("Name", contributors.getJSONObject(count).getString("Name"));
                  _item.put("Image", contributors.getJSONObject(count).getString("Image"));
                  _item.put(
                      "Description", contributors.getJSONObject(count).getString("Description"));
                  contributorsList.add(_item);
                }
              }
              list.setAdapter(
                  new AboutTeamMemberListAdapter(contributorsList, AboutTeamActivity.this));
              list.setLayoutManager(new LinearLayoutManager(AboutTeamActivity.this));
            } catch (JSONException e) {
              Toast.makeText(AboutTeamActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
          }

          @Override
          public void onErrorResponse(String _param1, String _param2) {
            final String _tag = _param1;
            final String _message = _param2;
          }
        };

    reqNetwork.startRequestNetwork(
        RequestNetworkController.GET, contributorsData, "Contributors", reqListener);
  }

  public void init() {
    // Setup Toolbat
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.about_team);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View arg0) {
            onBackPressed();
          }
        });
    // Define views
    list = findViewById(R.id.list);
    main = findViewById(R.id.main);
    loading = findViewById(R.id.loading);
  }
}
