package com.elfilibustero.uidesigner.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

public class SimpleListAdapter extends BaseAdapter {

	List<String> _data;
	int layout;

	public SimpleListAdapter(@LayoutRes int layout) {
		_data = new ArrayList<>();
		this.layout = layout;
	}

	public SimpleListAdapter(List<String> _arr, @LayoutRes int layout) {
		_data = _arr;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return _data.size();
	}

	@Override
	public String getItem(int _index) {
		return _data.get(_index);
	}

	@Override
	public long getItemId(int _index) {
		return _index;
	}

	@Override
	public View getView(final int _position, View _v, ViewGroup _container) {
		LayoutInflater _inflater = LayoutInflater.from(_container.getContext());
		View _view = _v;
		if (_view == null) {
			_view = _inflater.inflate(layout, null);
		}

		final TextView textView = _view.findViewById(android.R.id.text1);
		if (textView != null)
			textView.setText(_data.get(_position));
		return _view;
	}

	public void setData(List<String> newData) {
		_data = newData;
		notifyDataSetChanged();
	}
}
