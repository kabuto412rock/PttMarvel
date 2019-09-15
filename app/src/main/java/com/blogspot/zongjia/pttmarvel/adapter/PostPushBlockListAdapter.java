package com.blogspot.zongjia.pttmarvel.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.zongjia.pttmarvel.databinding.ArticlePushBlockItemBinding;
import com.blogspot.zongjia.pttmarvel.model.post.PttPostPush;

public class PostPushBlockListAdapter extends ListAdapter<PttPostPush, PostPushBlockListAdapter.PushViewHolder> {
    public PostPushBlockListAdapter() {
        super(new PushDiffCallback());
    }

    public class PushViewHolder extends RecyclerView.ViewHolder {
        private ArticlePushBlockItemBinding binding;

        public PushViewHolder(ArticlePushBlockItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PttPostPush push) {
            binding.setPushBlock(push);
            switch (push.symbol) {
                case "→":
                    binding.pushSymbol.setTextColor(Color.BLACK);
                    break;
                case "推":
                    binding.pushSymbol.setTextColor(Color.YELLOW);
                    break;
                case "噓":
                    binding.pushSymbol.setTextColor(Color.RED);
                    break;
            }
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public PushViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ArticlePushBlockItemBinding binding = ArticlePushBlockItemBinding.inflate(inflater, parent, false);
        return new PushViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PushViewHolder holder, int position) {
        PttPostPush push = getItem(position);
        holder.bind(push);
    }
}
