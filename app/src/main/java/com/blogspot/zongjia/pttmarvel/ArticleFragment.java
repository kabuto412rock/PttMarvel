package com.blogspot.zongjia.pttmarvel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blogspot.zongjia.pttmarvel.adapter.PostContentBlockListAdapter;
import com.blogspot.zongjia.pttmarvel.databinding.FragmentArticleBinding;
import com.blogspot.zongjia.pttmarvel.model.post.PttPostPush;
import com.blogspot.zongjia.pttmarvel.viewmodel.ArticleViewModel;
import com.blogspot.zongjia.pttmarvel.viewmodel.ArticleViewModelFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ArticleFragment extends Fragment {
    private AppCompatActivity activity;
    private TextView appbarTitle;
    private ArticleViewModel viewModel;
    PostContentBlockListAdapter adapter;
    SharedPreferences pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String postLink = ArticleFragmentArgs.fromBundle(getArguments()).getPostLink();

        // ViewModel & Binding initialized
        FragmentArticleBinding binding = FragmentArticleBinding.inflate(inflater);
        ArticleViewModelFactory viewModelFactory = new ArticleViewModelFactory(isShowImageFromPreference());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticleViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        adapter = new PostContentBlockListAdapter();
        adapter.setFontSize(getFontSizeFromPreference());
        binding.contentBlcckRecyclerView.setAdapter(adapter);
        binding.contentBlcckRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activity = (AppCompatActivity) getActivity();
        // 當點擊appBar的標題文字時，跳出AlertDialog顯示完整標題
        if (activity != null && activity.getSupportActionBar() != null) {
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.action_bar);
            appbarTitle = (TextView) toolbar.getChildAt(0);

            appbarTitle.setOnClickListener((view) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(viewModel.postTitle.getValue());
                builder.show();

            });
        }

        // 「文章標題」變動時
        viewModel.postTitle.observe(this, title -> {
            if (activity != null && activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(title);
            }
        });

        // 「文章內容區塊」變動時，對adapter
        viewModel.postContentBlocks.observe(this, combinedList -> {
            adapter.submitList(combinedList);
        });

        // 刷新等等要移去ViewModel自身內部
        viewModel.refreshSinglePost(postLink);

        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        Log.d("onDestroyView", "clean the appBarTitle's clickListener ");
        // 將appbar標題文字的ClickListener替換空值
        if (appbarTitle != null) {
            appbarTitle.setOnClickListener(null);
        }
        super.onDestroyView();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_option_menu, menu);

        viewModel.isShowImage.observe(this, isShowImage -> {
            adapter.setShowImage(isShowImage);
            MenuItem item = menu.findItem(R.id.showImageItem);
            if (isShowImage) {
                // 因為正在顯示圖片，所以提示為可"關閉圖片"
                item.setTitle("關閉圖片");
            } else {
                // 因為沒有顯示圖片，所以提示為可"開啟圖片"
                item.setTitle("開啟圖片");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.showCommentItem:
                // 顯示dialog
                List<PttPostPush> comments = viewModel.postPushes.getValue();
                if (comments == null) {
                    comments = new ArrayList<>();
                }
                CommentsDialogFragment dialog = new CommentsDialogFragment(true);

                Bundle args = new Bundle();
                args.putSerializable("comments", (Serializable)comments);
                dialog.setArguments(args);

                if (getFragmentManager() != null)
                    dialog.show(getFragmentManager(), "CommentsDialog");
                return true;
            case R.id.showImageItem:
                // true代表顯示圖片中
                viewModel.ChangeShowImage();
                return true;
        }
        Log.d("hell", "o world");
        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences getMyPreference(Context context) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return pref;
    }

    private boolean isShowImageFromPreference() {
        return getMyPreference(getContext()).getBoolean("isShowImage", false);
    }

    private int getFontSizeFromPreference() {
        return getMyPreference(getContext()).getInt("contentFontSize", 25);
    }


}
