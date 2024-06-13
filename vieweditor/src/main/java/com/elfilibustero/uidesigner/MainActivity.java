package com.elfilibustero.uidesigner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.ResourceUtils;
import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tscodeeditor.android.appstudio.vieweditor.databinding.ActivityMainBinding;
import com.tscodeeditor.android.appstudio.vieweditor.R;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MaterialToolbar toolbar;
    private LayoutDesigner editor;

    private final OnBackPressedCallback onBackPressedCallback =
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    showExitDialog();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
        setContentView(binding.getRoot());
        toolbar = binding.toolbar;
        editor = binding.editor;
        editor.setLayoutFromXml(ResourceUtils.readAssets2String("sample.xml"));
        editor.attachOnBackPressedToActivity(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> showExitDialog());
        addMenuProvider(
                new MenuProvider() {
                    @Override
                    public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
                        menuInflater.inflate(R.menu.project, menu);
                    }

                    @Override
                    public boolean onMenuItemSelected(MenuItem menuItem) {
                        var id = menuItem.getItemId();
                        if (id == R.id.device_size) {
                            selectDeviceSize(findViewById(id));
                        } else if (id == R.id.source_code) {
                            editor.showSourceCode();
                            return true;
                        }
                        return false;
                    }
                },
                this,
                Lifecycle.State.RESUMED);
    }

    private void selectDeviceSize(View view) {
        final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_size);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    var id = item.getItemId();
                    if (id == R.id.device_size_small) {
                        editor.setSize(LayoutDesigner.Size.SMALL);
                    } else if (id == R.id.device_size_default) {
                        editor.setSize(LayoutDesigner.Size.DEFAULT);
                    } else if (id == R.id.device_size_large) {
                        editor.setSize(LayoutDesigner.Size.LARGE);
                    }
                    return true;
                });

        popupMenu.show();
    }

    private void showExitDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.common_title_close_project)
                .setMessage(R.string.common_message_close_project)
                .setNegativeButton(R.string.common_text_no, null)
                .setPositiveButton(R.string.common_text_yes, (d, w) -> finishAffinity())
                .show();
    }
}
