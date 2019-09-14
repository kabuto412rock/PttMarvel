package com.blogspot.zongjia.pttmarvel.model.postList;

import pl.droidsonroids.jspoon.annotation.Selector;

public class PttPagePost {

    /**
     *
     *  標題 title
     *  <div class="title"> <a>[創作] 折價舖6-1</a>
     *
     *  推數 pushCount
     *  <div class="nrec"><span class="hl f3">26</span></div>
     *
     *  作者名稱 authorName
     *  <div class="author"> kabuto543 </div>
     *
     *  發布日期 dateString
     *  <div class="date">6/25</div>
     *
     *  文章連結 link
     *  <div class="title"><a href="/bbs/marvel/M.1558281179.A.F8D.html"></a></div>
     *
     */
    @Selector("div.title > a")
    String title;

    @Selector("div.nrec > span")
    String pushCount;

    @Selector("div.author")
    String authorName;

    @Selector("div.date")
    String dateString;

    @Selector(value = "div.title > a", attr = "href")
    String link;

    public String getLink() {
        if (link == null)
            return "";
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDateString() {
        return dateString;
    }

    public String getPushCount() {
        return pushCount;
    }

    public String getAuthorName() {
        return authorName;
    }
}
