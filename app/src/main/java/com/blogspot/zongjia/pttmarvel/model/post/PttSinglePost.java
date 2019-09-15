package com.blogspot.zongjia.pttmarvel.model.post;

import android.text.TextUtils;

import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

public class PttSinglePost {
    @Selector(value = "div #main-content", converter = PostContentJsoupConverter.class)
    String content;

    String formattedContent = null;

    // 分別存放 作者名稱、標題、時間
    @Selector(value = "div.article-metaline > .article-meta-value", defValue = "")
    List<String> articleMetaValue;

    @Selector(value = "div.push")
    List<PttPostPush> pushs;

    public List<PttPostPush> getPushs() {
        return pushs;
    }


    public String getContent() {
        // 內容為 "Po文時間" 和 "-- ※ 發信站: 批踢踢實業坊"兩者之間的字串！
        if (formattedContent != null)
            return formattedContent;

        String dateString = getDateString();

        if (TextUtils.isEmpty(dateString)) {
            return content;
        } else if (!content.contains(dateString)) {
            dateString = dateString.replaceFirst("[a-zA-Z]{3} [a-zA-Z]{3} ",
                    "$0 ");
        }
        String[] splitStrs = content.split(dateString);

        String[] finalSplitStrs = splitStrs[1].split("※ 發信站: 批踢踢實業坊");

        formattedContent = finalSplitStrs[0];
        return formattedContent;
    }

    // 0->作者 1->標題 2->時間
    public String getAuthor() {
        if (articleMetaValue == null || articleMetaValue.size() != 3)
            return "";
        return articleMetaValue.get(0);
    }

    public String getTitle() {
        if (articleMetaValue == null || articleMetaValue.size() != 3)
            return "";
        return articleMetaValue.get(1);
    }

    public String getDateString() {
        if (articleMetaValue == null || articleMetaValue.size() != 3)
            return "";
        return articleMetaValue.get(2);
    }
}
