package com.blogspot.zongjia.pttmarvel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.zongjia.pttmarvel.adapter.PagePostClickListener;
import com.blogspot.zongjia.pttmarvel.adapter.PagePostListAdapter;
import com.blogspot.zongjia.pttmarvel.databinding.ArticleListFragmentBinding;
import com.blogspot.zongjia.pttmarvel.viewmodel.ArticleListViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ArticleListFragment extends Fragment
        implements ChoosePageIndexDialogFragment.ChangePageListener {


    private ArticleListViewModel viewModel;
    private ArticleListFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ArticleListFragmentBinding.inflate(inflater);

        viewModel = ViewModelProviders.of(this).get(ArticleListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            String kanbanName = pref.getString("kanban", "marvel");
            viewModel.setKanban(kanbanName);
        }
        // 文章列表的Adapter
        PagePostListAdapter adapter = new PagePostListAdapter(
                new PagePostClickListener(link -> {
                    hideKeyboad(getActivity());

                    if (TextUtils.isEmpty(link)) {
                        Snackbar.make(binding.getRoot(), "空連結，文章已被刪除！" + link, Snackbar.LENGTH_LONG)
                                .show();
                        return;
                    }

                    // 導覽到文章展示的頁面
                    Navigation.findNavController(binding.getRoot())
                            .navigate(ArticleListFragmentDirections.actionArticleListFragmentToArticleFragment(link));
                }));

        binding.pagePostRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.pagePostRecyclerView.setAdapter(adapter);

        /**
         * View點擊事件的處理
         */
        // "搜尋"的按鈕被按下
        binding.queryButton.setOnClickListener(v -> {
            // 以當前的關鍵字當作參考在Ptt頁面上搜尋
            String queryText = binding.editQueryText.getText().toString();
            viewModel.refreshPagePostsByQuery(1, queryText);
            hideKeyboad(v.getContext());
        });

        // "下一頁"的按鈕被按下
        binding.nextPageButton.setOnClickListener(v -> {
            // 點擊載入下一頁的資料
            viewModel.loadPagePostsFromNextPage();
            hideKeyboad(v.getContext());
        });

        // "上一頁"的按鈕被按下
        binding.previousPageButton.setOnClickListener(v -> {
            // 點擊載入上一頁的資料
            viewModel.loadPagePostsFromPreviousPage();
            hideKeyboad(v.getContext());
        });

        // "索引Dialog"的按鈕被按下
        binding.choosePageIndexDialogButton.setOnClickListener(v -> {
            ChoosePageIndexDialogFragment dialog = new ChoosePageIndexDialogFragment();

            dialog.setChangePageListener(ArticleListFragment.this);
            if (getFragmentManager() != null) {
                Bundle arguments = new Bundle();
                // TODO:: 修改成當前頁面的MaxValue
                arguments.putInt("mMaxPageIndexNumber", viewModel.getMaxPageIndexNumber());
                arguments.putInt("mCurrentPageIndexNumber", viewModel.getCurrentPageIndexNumber());
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(), "ChoosePageIndexDialogFragment");
            }
        });

        /**
         * 觀察LiveData的位置
         */
        // TODO::無法覆蓋掉 Navigation的title
        viewModel.mKanbanName.observe(this, newKanbanName -> {
            Log.d("newKanbanName", newKanbanName);
            if (activity != null && activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(String.format("Ptt麻佛[%s]", newKanbanName));
            }
        });

        // 當viewModel的文章列表(mPagePosts)被更新時，向adapter發送最新的文章列表(mPagePosts)
        viewModel.mPagePosts.observe(this,
                pttPagePosts -> adapter.submitList(pttPagePosts));

        // 當載入錯誤事件的錯誤訊息(errorMessage)變動時
        viewModel.getRefreshErrorEvent().observe(this, errorMessage -> {
            if (TextUtils.isEmpty(errorMessage)) {
                //沒有出現錯誤時errorMessage的字串為空，不動作．
                return;
            }
            // 出現錯誤，使用SnackBar顯示錯誤訊息
            Snackbar.make(this.getView(), errorMessage, Snackbar.LENGTH_LONG).show();
            viewModel.refreshErrorEventCompleted();
        });

        // 更新UI 當前的頁數
        viewModel.currentPageNumber.observe(this, (pageNumber) -> {
            binding.choosePageIndexDialogButton.setText(pageNumber.toString());
        });

        // 當文章列表正在載入時，顯示飛機旋轉圖，沒在載入則隱藏。
        viewModel.getRefreshLoading().observe(this, (isLoading) -> {
            if (isLoading) {
                Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
                rotation.setRepeatCount(Animation.INFINITE);
                binding.imgLoadingPlane.startAnimation(rotation);
                binding.imgLoadingPlane.setVisibility(View.VISIBLE);
                binding.editQueryText.clearFocus();
            } else {
                binding.imgLoadingPlane.clearAnimation();
                binding.imgLoadingPlane.setVisibility(View.GONE);
            }
        });

        // 決定選頁按鈕是否被致能。
        viewModel.mPageIndexNumberCanChoose.observe(this, (enable) -> {
            binding.choosePageIndexDialogButton.setEnabled(enable);
        });
        // 當前優化TODO
        // OK:: 當沒有下一頁or上一頁時應該暫時把按鈕disable
        // OK:: 當按下搜尋或上一頁or下一頁按鈕時應該把鍵盤影藏
        // OK:: SoftKeyboad當手機橫擺時會導致破圖(下一頁按鈕不知為何出現在鍵盤旁邊)
        // OK:: (過度功能點擊item會跳出SnackBar，顯示對應的網址
        // Fix:: Bug 初始頁面案上一頁，會崩潰...已修復，是字串空值導致
        // Fix:: 簡易版規 沒有時間的文章內容會出錯
        // Fix:: Show Long Text(字體大小，但不適合使用autoTextSizing)
        // TODO:: 在ListFilter裡添加一些常用的標籤當搜尋(日本、nosleep)
        // OK:: 在文章頁面右上角點擊可顯示推文
        // OK:: 感覺一開始EditQuery可以不用取得焦點，因為預設會先顯示一個Index頁面(不喜再換)
        // OK:: 顯示當前的頁數(當重新按搜尋時會重置 當前頁數)
        // TODO:: Ｘ 分享文章的功能(Messager或gmail又或者Instant run) 非優先功能
        // TODO: 文章Block會顯示圖片(進階的希望能隨時關閉，可設計在menuIcon)
        // Maybe OK:: 載入資料時希望有載入的動畫，旋轉的圈圈(X) 旋轉的飛機(O)


        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_list_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_page_Item:
                viewModel.refreshPagePostByIndexHtml();
                if (getContext() != null)
                    hideKeyboad(getContext());
                return true;
        }
        return false;
    }

    private static void hideKeyboad(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 檢查如果沒有View有焦點則直接return
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null) return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void chnageQyeryPageByIndex(int index) {
        Log.d("chnageQyeryPageByIndex", "被呼叫了");
        String queryText = binding.editQueryText.getText().toString();
        viewModel.refreshPagePostsByQuery(index, queryText);
    }

}