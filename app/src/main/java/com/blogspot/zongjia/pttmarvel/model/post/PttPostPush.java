package com.blogspot.zongjia.pttmarvel.model.post;

import com.blogspot.zongjia.pttmarvel.api.IpListService;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String ip = ""; // 推文Ip，如果沒有ip就是""

    public int floor = 1; // 樓層

    public boolean isTaiwan = false;
    private static Pattern ipPattern = Pattern.compile("\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+");
    private boolean isAlreadyParsed = false; //

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getSymbol() {
        return symbol;
    }
    public String getFloorText() {
        return String.format("%sF", floor);
    }
    public String getIp() {
        parseIpTime();
        return ip;
    }
    public String getTime() {
        parseIpTime();
        return time;
    }

    private void parseIpTime() {
        if (isAlreadyParsed) {
            return;
        }

        Matcher ipMatcher = ipPattern.matcher(time);

        if (ipMatcher.find()) {
            ip = ipMatcher.group();
            time = ipMatcher.replaceAll("");
        }
    }


}
