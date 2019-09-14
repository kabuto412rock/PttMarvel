package com.blogspot.zongjia.pttmarvel.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.blogspot.zongjia.pttmarvel.model.postList.PttPage;
import com.blogspot.zongjia.pttmarvel.model.postList.PttPageLink;
import com.blogspot.zongjia.pttmarvel.model.postList.PttPagePost;
import com.blogspot.zongjia.pttmarvel.repo.PttRepo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleListViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private PttRepo repository = new PttRepo();
    private CompositeDisposable disposables = new CompositeDisposable();
    // 看板名稱
    public MutableLiveData<String> mKanbanName = new MutableLiveData<>();

    // 文章頁面LiveData(包含 列表 & 上下一頁連結等資訊)
    private MutableLiveData<PttPage> mPttPage = new MutableLiveData<>();

    // 文章列表LiveData
    public LiveData<List<PttPagePost>> mPagePosts = Transformations.map(mPttPage, PttPage::getArticles);

    // 下一頁、上一頁的URL(LiveData)
    public MutableLiveData<String> mNextPageLink = new MutableLiveData<>();
    public MutableLiveData<String> mPreviousPageLink = new MutableLiveData<>();

    // 最舊頁、最新頁的URL(只是在dialog傳值用，所以不需要隨時觀察)
    public String mOldestPageLink = "";
    public String mNewestPageLink = "";

    // 當前頁面的索引值
    public MutableLiveData<Integer> currentPageNumber = new MutableLiveData<>();
    private int mCurrentPageIndexNumber = 1;

    // 是否致能頁面索引選擇按鈕
    public MutableLiveData<Boolean> mPageIndexNumberCanChoose = new MutableLiveData<>();

    // 更新錯誤事件
    private MutableLiveData<String> mRefreshErrorEvent = new MutableLiveData<>();

    // 載入狀態
    private MutableLiveData<Boolean> refreshLoading = new MutableLiveData<>();

    private String TAG = "ArticleListViewModel";

    interface AfterRequest {
        void onCorrect();
    }

    public ArticleListViewModel() {
        mRefreshErrorEvent.setValue("");
        
        mNextPageLink.setValue("");
        mPreviousPageLink.setValue("");

        refreshLoading.setValue(false);
        mPageIndexNumberCanChoose.setValue(false);

        mKanbanName.setValue(repository.getKanBan());
    }


    // 當按下一頁時，載入下一頁的內容
    public void loadPagePostsFromNextPage() {
        Log.d(TAG, "載入下一頁");
        if (TextUtils.isEmpty(mNextPageLink.getValue())) return;
        refreshPagePostsByUrl(mNextPageLink.getValue(), new AfterRequest() {
            @Override
            public void onCorrect() {
                mCurrentPageIndexNumber -= 1;
                currentPageNumber.setValue(mCurrentPageIndexNumber);
            }
        });

    }

    // 當按上一頁時，載入上一頁的內容
    public void loadPagePostsFromPreviousPage() {
        Log.d(TAG, "載入上一頁");
        if (TextUtils.isEmpty(mPreviousPageLink.getValue())) return;
        refreshPagePostsByUrl(mPreviousPageLink.getValue(), new AfterRequest() {
            @Override
            public void onCorrect() {
                mCurrentPageIndexNumber += 1;
                currentPageNumber.setValue(mCurrentPageIndexNumber);
            }
        });
    }

    // 依據Ptt子節點的url更新mPagePosts (url為上 or 下一頁按鈕的網址)
    private void refreshPagePostsByUrl(String url, AfterRequest afterRequest) {
        if (TextUtils.isEmpty(url)) return;
        refreshPagePosts(repository.getUrlPageRx(url), afterRequest);
    }

    // 依據pageNum、query更新mPagePosts
    public void refreshPagePostsByQuery(int pageNum, String query) {
        if (TextUtils.isEmpty(query)) {
            checkErrorAndReport(new IllegalArgumentException("關鍵字不得為空！"));
            return;
        }
        refreshPagePosts(repository.getQueryPageRx(pageNum, query), new AfterRequest() {
            @Override
            public void onCorrect() {
                mPageIndexNumberCanChoose.setValue(true);
                mCurrentPageIndexNumber = pageNum;
                currentPageNumber.setValue(mCurrentPageIndexNumber);
            }
        });
    }

    // 載入看板頁面更新(首頁)
    public void refreshPagePostByIndexHtml() {
        refreshPagePosts(repository.getIndexPageRx(), new AfterRequest() {
            @Override
            public void onCorrect() {
                mPageIndexNumberCanChoose.setValue(false);
                mCurrentPageIndexNumber = 1;
                currentPageNumber.setValue(mCurrentPageIndexNumber);

                Log.d("refreshIndexHtml", String.format("kanban name is %s", repository.getKanBan()));

            }
        });
    }

    // 載入看板頁面
    private void refreshPagePosts(Single<PttPage> singlePttPage, AfterRequest afterRequest) {
        refreshLoading.setValue(true);

        Log.d("refreshPagePosts", String.format("網頁請求文章... %s",  repository.getKanBan()));
        Disposable d = singlePttPage
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    refreshLoading.setValue(false);
                })
                .subscribe(
                        // OnSuccess
                        (pttPage -> {
                            mPttPage.setValue(pttPage);
                            if ( pttPage.getPagingButtons() == null) {
                                checkErrorAndReport(new Throwable("網頁解析失敗Sorry..."));
                                return;
                            }
                            for (PttPageLink pageLink : pttPage.getPagingButtons()) {

                                String label = pageLink.getLabel();
                                String link = pageLink.getLink();

                                if (label.contains("上")) {
                                    // 設定上一頁的按鈕URL
                                    mPreviousPageLink.setValue(link);
                                } else if (label.contains("下")) {
                                    // 設定下一頁的按鈕URL
                                    mNextPageLink.setValue(link);
                                } else if (label.contains("新")) {
                                    // 設定最新頁面的按鈕URL
                                    mNewestPageLink = link;
                                } else if (label.contains("舊")) {
                                    // 設定最舊頁面的按鈕URL
                                    mOldestPageLink = link;
                                }
                            }
                            afterRequest.onCorrect();
                        }),
                        // OnError
                        (throwable -> checkErrorAndReport(throwable)));
        disposables.add(d);

    }

    // 當載入網路資料出現問題時，會有錯誤訊息事件的LiveData
    public LiveData<String> getRefreshErrorEvent() {
        return mRefreshErrorEvent;
    }

    // 當View已經完成查看事件時，將錯誤訊息事件的值設為空字串！
    public void refreshErrorEventCompleted() {
        mRefreshErrorEvent.setValue("");
    }

    public LiveData<Boolean> getRefreshLoading() {
        return refreshLoading;
    }

    // 檢查RxJava丟出的Throwable，將錯誤訊息事件的值設為對應的錯誤訊息
    private void checkErrorAndReport(Throwable throwable) {

        if (throwable instanceof java.net.UnknownHostException) {
            mRefreshErrorEvent.setValue("網路出現錯誤，請重新連上Wifi");
            return;
        } else if (throwable instanceof IllegalArgumentException) {
            mRefreshErrorEvent.setValue(throwable.getMessage());
            return;
        }
        mRefreshErrorEvent.setValue("無法解析關鍵字or批踢踢暫時死亡");
    }

    // 取得當前頁面的索引值(index)
    public int getCurrentPageIndexNumber() {
        return mCurrentPageIndexNumber;
    }

    // 取得最舊頁面的URL中的索引值(index)
    public int getMaxPageIndexNumber() {
        if (!mOldestPageLink.contains("page="))
            return -1;
        Pattern pattern = Pattern.compile("page=[\\d]+");
        Matcher matcher = pattern.matcher(mOldestPageLink);

        if (matcher.find()) {
            try {
                String maxPageNumStr = matcher.group().split("page=")[1];
                return Integer.parseInt(maxPageNumStr);
            } catch (Exception e) {
                return 1;
            }
        }
        return 1;
    }

    // TODO::當出現請求錯誤時，重新設定與換頁相館的計數
    private void resetPageIndexFunction() {
        mCurrentPageIndexNumber = 1;
        currentPageNumber.setValue(mCurrentPageIndexNumber);
        mPageIndexNumberCanChoose.setValue(false);
    }

    // 當ViewModel結束時，回收disposables避免memory leak
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void setKanban(String kanbanName) {
        // 如果當前看板不等於 kanbanName，則更新repository的看板
        if (!kanbanName.equals(repository.getKanBan())) {
            repository.setKanBan(kanbanName);
            mKanbanName.setValue(kanbanName);
            refreshPagePostByIndexHtml();
        }
    }
}
