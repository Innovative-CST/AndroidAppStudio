package com.tscodeeditor.android.appstudio.fragments.projectmodelconfig;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import com.tscodeeditor.android.appstudio.databinding.FragmentProjectModelAppConfigrationLayoutBinding;

public class ProjectModelAppConfigrationFragment extends ProjectModelConfigBaseFragment {
  private FragmentProjectModelAppConfigrationLayoutBinding binding;

  @Override
  @MainThread
  @Nullable
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
    binding = FragmentProjectModelAppConfigrationLayoutBinding.inflate(layoutInflater);
    return binding.getRoot();
  }
}
