package com.blogspot.zongjia.pttmarvel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ArticleViewModelFactory implements ViewModelProvider.Factory {
    private boolean showImage;

    public ArticleViewModelFactory(boolean showImage) {
        this.showImage = showImage;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ArticleViewModel.class)) {
            ArticleViewModel viewModel = new ArticleViewModel(showImage);
            return (T) viewModel;
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
