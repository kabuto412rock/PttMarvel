package com.blogspot.zongjia.pttmarvel;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.zongjia.pttmarvel.adapter.PostContentBlockListAdapter;
import com.blogspot.zongjia.pttmarvel.adapter.PostPushBlockListAdapter;
import com.blogspot.zongjia.pttmarvel.model.post.PttPostPush;

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
        PostPushBlockListAdapter adapter = new PostPushBlockListAdapter();
//        adapter.setShowImage(isShowImage);

        Bundle args = getArguments();
        ArrayList<String> comments = args.getStringArrayList("comments");
        ArrayList<PttPostPush> pushes = new ArrayList<>();
        for (String comment : comments) {
            comment = comment.trim();
            Log.d("push皆悉", comment);
            String[] chips = comment.split(" ");

            String symbol = chips[0];
            String author = chips[1].replace(":", "");
            String time = chips[chips.length - 2] + " " + chips[chips.length - 1];
            String ipPattern = "\\d{2,3}\\.\\d{2,3}\\.\\d{2,3}\\.\\d{2,3}";

            String[] ipSplit = chips[chips.length - 3].split(ipPattern);
            String ip = "";
            if (ipSplit.length > 0) {
                ip = ipSplit[ipSplit.length - 1];
            }
            comment = comment.split(time)[0];// 取ip前面的字段
            String content = comment.substring(comment.indexOf(":")+1).trim();
//            String content = "@W@"+comment+"!W!";
            PttPostPush onePush = new PttPostPush(symbol, author, ip, time, content);
            pushes.add(onePush);
//            Log.d("comment推", onePush.symbol);
//            Log.d("comment名稱", onePush.author);
//            Log.d("comment位址", onePush.ip);
//            Log.d("comment時間", onePush.time);
//            Log.d("comment內容", onePush.content);
        }
        adapter.submitList(pushes);
        recyclerView.setAdapter(adapter);
        return builder.create();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
