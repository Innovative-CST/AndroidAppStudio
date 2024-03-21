package com.tscodeeditor.android.appstudio.activities;

import android.os.Bundle;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.databinding.ActivityEventEditorBinding;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;

public class EventEditorActivity extends BaseActivity {
  private ActivityEventEditorBinding binding;

  /*
   * Contains the location of project directory.
   * For example: /../../Project/100
   */
  private File projectRootDirectory;
  /*
   * Contains the location of currently selected file model.
   * For example: /../../Project/100/../abc/FileModel
   */
  private File fileModelDirectory;
  /*
   * Contains the location of event list path.
   * For example: /../../Project/100/../../Events/Config
   */
  private File eventListPath;
  /*
   * Contains the location of event file path.
   * For example: /../../Project/100/../../Events/Config/ActivityBasics
   */
  private File eventFile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityEventEditorBinding.inflate(getLayoutInflater());

    // Initialize the files paths

    projectRootDirectory = new File(getIntent().getStringExtra("projectRootDirectory"));
    fileModelDirectory = new File(getIntent().getStringExtra("fileModelDirectory"));
    eventListPath = new File(getIntent().getStringExtra("eventListPath"));
    eventFile = new File(getIntent().getStringExtra("eventFile"));

    setContentView(binding.getRoot());
    // SetUp the toolbar
    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
  }
}
