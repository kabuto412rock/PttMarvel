package com.blogspot.zongjia.pttmarvel.model.post;

import java.io.Serializable;

import pl.droidsonroids.jspoon.annotation.Selector;

public class PttPostPush implements Serializable {
    @Selector(value = "span.push-tag")
    public String symbol; // 推、噓

    @Selector(value = "span.push-userid")
    public String author;   // 推文者的名稱

    @Selector(value = "span.push-ipdatetime")
    public String time;   // 推文時間

    @Selector(value = "span.push-content")
    public String content; // 推文內容


    public int floor = 1; // 樓層


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTime() {
        return time;
    }
}
