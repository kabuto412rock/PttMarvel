package com.blogspot.zongjia.pttmarvel.model.postList;

import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

public class PttPage {
    // <div class="r-ent"> ...</div>
    @Selector("div.r-ent")
    List<PttPagePost> articles;

//  <a class="btn wide" href="/bbs/marvel/search?page=45&amp;q=%E4%BD%A0">最舊</a>
    @Selector(value = "div.btn-group-paging > a")
    List<PttPageLink> pagingButtons;


    public List<PttPagePost> getArticles() {
        return articles;
    }

    public List<PttPageLink> getPagingButtons() {
        return pagingButtons;
    }


}
