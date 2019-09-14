package com.blogspot.zongjia.pttmarvel.api;

import com.blogspot.zongjia.pttmarvel.model.post.PttSinglePost;
import com.blogspot.zongjia.pttmarvel.model.postList.PttPage;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface PttService {
    String BASE_URL = "https://www.ptt.cc";

    // 例: https://www.ptt.cc/bbs/marvel/index.html
    @Headers("Cookie: over18=1")
    @GET("bbs/{kanBan}/index.html")
    Single<PttPage> getIndexPageRx(@Path("kanBan") String kanBan);

    @Headers("Cookie: over18=1")
    // 例:https://www.ptt.cc/bbs/marvel/search?page=1&q=%E6%97%A5%E6%9C%AC
    @GET("bbs/{kanBan}/search")
    Single<PttPage> getQueryPageRx(@Path("kanBan") String kanBan, @Query("page") int page, @Query("q") String query);

    @Headers("Cookie: over18=1")
    @GET
    Single<PttPage> getPageRx(@Url String url);

    @Headers("Cookie: over18=1")
    @GET
    Single<PttSinglePost> getSinglePostRx(@Url String url);
}
