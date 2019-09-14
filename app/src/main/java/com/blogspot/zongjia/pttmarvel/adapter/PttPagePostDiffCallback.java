package com.blogspot.zongjia.pttmarvel.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.blogspot.zongjia.pttmarvel.model.postList.PttPagePost;

public class PttPagePostDiffCallback extends DiffUtil.ItemCallback<PttPagePost> {

    @Override
    public boolean areItemsTheSame(@NonNull PttPagePost oldItem, @NonNull PttPagePost newItem) {
        return oldItem.getLink() == newItem.getLink();
    }

    @Override
    public boolean areContentsTheSame(@NonNull PttPagePost oldItem, @NonNull PttPagePost newItem) {
        return oldItem.getLink().equals(newItem.getLink());
    }
}
