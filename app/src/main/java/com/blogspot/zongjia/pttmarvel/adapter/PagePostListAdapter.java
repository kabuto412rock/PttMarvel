package com.blogspot.zongjia.pttmarvel.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.zongjia.pttmarvel.databinding.ArticleListItemBinding;
import com.blogspot.zongjia.pttmarvel.model.postList.PttPagePost;

public class PagePostListAdapter extends ListAdapter<PttPagePost, PagePostListAdapter.PagePostViewHolder> {
    private PagePostClickListener clickListener;
    public PagePostListAdapter(PagePostClickListener clickListener) {
        super(new PttPagePostDiffCallback());
        this.clickListener = clickListener;
    }

    public class PagePostViewHolder extends RecyclerView.ViewHolder {
        private ArticleListItemBinding binding;

        public PagePostViewHolder(ArticleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PttPagePost pttPagePost, PagePostClickListener clickListener) {
            binding.setPttPagePost(pttPagePost);
            binding.executePendingBindings();
            binding.setClickListener(clickListener);
        }
    }

    @NonNull
    @Override
    public PagePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ArticleListItemBinding binding = ArticleListItemBinding.inflate(inflater, parent, false);
        return new PagePostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PagePostViewHolder holder, int position) {
        PttPagePost pagePost = getItem(position);
        holder.bind(pagePost, clickListener);
    }
}
