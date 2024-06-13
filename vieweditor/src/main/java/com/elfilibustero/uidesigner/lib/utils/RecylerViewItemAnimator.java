package com.elfilibustero.uidesigner.lib.utils;

import android.animation.ObjectAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class RecylerViewItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @Nullable ItemHolderInfo preInfo, @Nullable ItemHolderInfo postInfo) {
        if (preInfo != null && postInfo != null) {
            var view = newHolder.itemView;
            view.setAlpha(0);

            var alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            alphaAnimator.setDuration(150);
            alphaAnimator.start();

            return true;
        }
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }
}
