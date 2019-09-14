package com.blogspot.zongjia.pttmarvel;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.zongjia.pttmarvel.adapter.PostContentBlockListAdapter;

import java.util.ArrayList;

public class CommentsDialogFragment extends AppCompatDialogFragment {
    private boolean isShowImage;
    public CommentsDialogFragment(Boolean isShowImage) {
        isShowImage = false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("推文");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_comments, null);
        builder.setView(view);

        RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PostContentBlockListAdapter adapter = new PostContentBlockListAdapter();
        adapter.setShowImage(isShowImage);

        Bundle args = getArguments();
        ArrayList<String> comments = args.getStringArrayList("comments");
        adapter.submitList(comments);
        recyclerView.setAdapter(adapter);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
