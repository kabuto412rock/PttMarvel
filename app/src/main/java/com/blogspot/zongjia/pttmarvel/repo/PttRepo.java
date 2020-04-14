package com.blogspot.zongjia.pttmarvel.repo;

import com.blogspot.zongjia.pttmarvel.api.PttService;
import com.blogspot.zongjia.pttmarvel.api.RetrofitManager;
import com.blogspot.zongjia.pttmarvel.model.post.PttSinglePost;
import com.blogspot.zongjia.pttmarvel.model.postList.PttPage;

import io.reactivex.Single;

public class PttRepo {
    private PttService pttService = RetrofitManager.getPttService();

    // 看板名稱
    private String kanBanName = "Beauty";
    public void setKanBan(String name) {
        kanBanName = name;
    }
    public String getKanBan() {
        return kanBanName;
    }

    public Single<PttPage> getIndexPageRx() {
        return pttService.getIndexPageRx(kanBanName);
    }

    public Single<PttPage> getQueryPageRx(int pageNum, String query) {
        return pttService.getQueryPageRx(kanBanName, pageNum, query);
    }

    public Single<PttPage> getUrlPageRx(String url) {
        return pttService.getPageRx(url);
    }

    public Single<PttSinglePost> getSinglePostRx(String url) {
        return pttService.getSinglePostRx(url);
    }

}
