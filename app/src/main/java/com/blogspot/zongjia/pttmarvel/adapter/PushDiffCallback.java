package com.blogspot.zongjia.pttmarvel.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.blogspot.zongjia.pttmarvel.model.post.PttPostPush;

public class PushDiffCallback extends DiffUtil.ItemCallback<PttPostPush> {


    @Override
    public boolean areItemsTheSame(@NonNull PttPostPush oldItem, @NonNull PttPostPush newItem) {
        return oldItem.content + oldItem.ip == newItem.content + oldItem.ip;
    }

    @Override
    public boolean areContentsTheSame(@NonNull PttPostPush oldItem, @NonNull PttPostPush newItem) {
        return oldItem.content.equals(newItem.content);
    }
}
