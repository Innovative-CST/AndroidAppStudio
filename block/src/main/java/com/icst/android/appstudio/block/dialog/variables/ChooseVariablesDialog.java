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

package com.icst.android.appstudio.block.dialog.variables;

import java.util.ArrayList;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.block.adapter.AddVariableListAdapter;
import com.icst.android.appstudio.block.databinding.DialogVariableChooserBinding;
import com.icst.android.appstudio.block.model.VariableModel;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class ChooseVariablesDialog extends MaterialAlertDialogBuilder {
	private ArrayList<VariableModel> variables;
	private Context context;
	private DialogVariableChooserBinding binding;
	private AddVariableListAdapter adapter;
	private AlertDialog dialog;

	public ChooseVariablesDialog(Context context, ArrayList<VariableModel> variables) {
		super(context);
		this.variables = variables;
		this.context = context;

		binding = DialogVariableChooserBinding.inflate(LayoutInflater.from(context));
		adapter = new AddVariableListAdapter(context, variables) {
			@Override
			public void setSelectedVariable(VariableModel selectedVariable) {
				super.setSelectedVariable(selectedVariable);
				onSelectedVariable(selectedVariable);
				dialog.dismiss();
			}
		};
		binding.gridView.setAdapter(adapter);
		setTitle("Add variables");
		setView(binding.getRoot());
		dialog = show();
	}

	public void onSelectedVariable(VariableModel selectedVariable) {
	}

	public VariableModel getSelectedVariableModel() {
		return adapter.getSelectedVariable();
	}
}
