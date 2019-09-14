package com.blogspot.zongjia.pttmarvel.adapter;

import com.blogspot.zongjia.pttmarvel.model.postList.PttPagePost;

public class PagePostClickListener {
    private PagePostClickInterface clickListener;

    public PagePostClickListener(PagePostClickInterface clickListener) {
        this.clickListener = clickListener;
    }

    public void onClick(PttPagePost pagePost) {
        clickListener.onClick(pagePost.getLink());
    }

    public interface PagePostClickInterface {
        void onClick(String link);
    }
}
