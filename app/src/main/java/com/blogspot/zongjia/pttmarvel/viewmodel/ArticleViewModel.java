package com.blogspot.zongjia.pttmarvel.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.blogspot.zongjia.pttmarvel.model.post.PttPostPush;
import com.blogspot.zongjia.pttmarvel.repo.PttRepo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleViewModel extends ViewModel {

    private CompositeDisposable disposables = new CompositeDisposable();
    private PttRepo repository = new PttRepo();

    // 文章內容(長字串)
    public MutableLiveData<String> postContent = new MutableLiveData<>();

    // 推文列表Push板
    public MutableLiveData<List<PttPostPush>> postPushes = new MutableLiveData<>();

    // 文章內容轉成顯示用的字串列表
    public LiveData<ArrayList<String>> postContentBlocks = Transformations.map(postContent, (content -> {
        /**
         * 如何將原本文章切割成一行行文字
         * 一行一個block
         */
        String[] lines = content.split("\n");
        ArrayList<String> combinedList = new ArrayList<>();

        for (String str : lines) {
            combinedList.add(str);
        }
        return combinedList;
    }));

    //文章標題
    public MutableLiveData<String> postTitle = new MutableLiveData<>();
    //文章圖片是否顯示
    public MutableLiveData<Boolean> isShowImage = new MutableLiveData<>();
    private String TAG = "ArticleViewModel";

    public ArticleViewModel(boolean showImage) {
        postPushes.setValue(null);
        isShowImage.setValue(showImage);
    }

    public void refreshSinglePost(String url) {
        Disposable disposable = repository.getSinglePostRx(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pttSinglePost -> {
                    String content = pttSinglePost.getContent();

                    postContent.setValue(content);
                    postTitle.setValue(pttSinglePost.getTitle());
                    postPushes.setValue(pttSinglePost.getPushs());

                    if (pttSinglePost.getPushs() == null) {
                        return;
                    }
                    for (PttPostPush push : pttSinglePost.getPushs()) {
                        Log.d(TAG, "作者: " + push.author);
                        Log.d(TAG, "內容: " + push.content);
                        Log.d(TAG, "IP時間: " + push.time);
                    }

                }, (throwable) -> {
                    Log.d("ArticleViewModel", "錯誤丟出 : " + throwable.getMessage());
                });

        disposables.add(disposable);
    }

    public boolean ChangeShowImage() {
        Boolean isShow = isShowImage.getValue();
        isShowImage.setValue(!isShow);
        return !isShow;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
