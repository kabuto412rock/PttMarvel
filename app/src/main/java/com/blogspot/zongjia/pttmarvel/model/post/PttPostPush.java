package com.blogspot.zongjia.pttmarvel.model.post;

public class PttPostPush {
    public String symbol; // 推、噓
    public String author;   // 推文者的名稱
    public String ip;     // ip位址
    public String time;   // 推文時間
    public String content; // 推文內容
    public int floor = 1; // 樓層

    public PttPostPush(String symbol, String author, String ip, String time, String content, int floor) {
        this.symbol = symbol;
        this.author = author;
        this.ip = ip;
        this.time = time;
        this.content = content;
        this.floor = floor;
    }


}
