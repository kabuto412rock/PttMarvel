package com.blogspot.zongjia.pttmarvel.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.zongjia.pttmarvel.R;
import com.blogspot.zongjia.pttmarvel.databinding.ArticleContentBlockItemBinding;
import com.bumptech.glide.Glide;

public class PostContentBlockListAdapter extends ListAdapter<String, PostContentBlockListAdapter.BlockViewHolder> {
    private boolean isShowImage = false;
    private int fontSize = 25;
    public PostContentBlockListAdapter() {
        super(new StringDiffCallback());
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ArticleContentBlockItemBinding binding = ArticleContentBlockItemBinding.inflate(inflater, parent, false);
        binding.contentBlock.setTextSize(fontSize);
        return new BlockViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        String contentBlock = getItem(position);

        holder.bind(contentBlock);
    }

    public void setShowImage(boolean isShowImage) {
        this.isShowImage = isShowImage;
        notifyDataSetChanged();
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public class BlockViewHolder extends RecyclerView.ViewHolder {
        private ArticleContentBlockItemBinding binding;

        public BlockViewHolder(ArticleContentBlockItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String contentBlock) {
            binding.setContentBlock(contentBlock);

            if (isShowImage) {
                // 如果是圖片的網址，
                String contentTrim = contentBlock.trim();
                String urlpattern = "http(s)?://(.)+[jpg|png|gif]";
                String urlPattern2 = "http(s)?://tinyurl\\.com/(.)+";

                // 將imgur.com的相片網址改成i.imgur.com並添加後標為.jpg，
                // 這樣才可以正確取得imgur.com真正的相片網址．
                if (contentTrim.contains("/imgur.com") && !contentTrim.contains(".jpg")) {
                    contentTrim = contentTrim.replace("imgur", "i.imgur") + ".jpg";
                }
                // 如果內容文字符合相片網址，則傳遞
                if (contentTrim.matches(urlpattern) || contentTrim.matches(urlPattern2)) {
                    binding.contentImage.setVisibility(View.VISIBLE);
                    bindImage(binding.contentImage, contentTrim);
                    Log.d("PostContenBlock", contentBlock + "解析成功");
                } else {
                    binding.contentImage.setVisibility(View.GONE);
                    Log.d("PostContenBlock", contentBlock + "解析失敗");

                }
            } else if (binding.contentImage.getVisibility() != View.GONE) {
                binding.contentImage.setVisibility(View.GONE);
            }
            binding.executePendingBindings();
        }

        void bindImage(ImageView imageView, String url) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_airplanemode_active_black_24dp)
                    .into(imageView);
        }
    }

}