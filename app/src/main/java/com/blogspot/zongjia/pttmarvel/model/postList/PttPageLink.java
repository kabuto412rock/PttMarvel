package com.blogspot.zongjia.pttmarvel.model.postList;

import java.util.Map;

import pl.droidsonroids.jspoon.annotation.Selector;

public class PttPageLink {
    @Selector("a")
    String label;

    @Selector(value = "a", attr = "href", defValue = "")
    String link;

    public String getLabel() {
        return label;
    }

    public String getLink() {
        return link;
    }
}
