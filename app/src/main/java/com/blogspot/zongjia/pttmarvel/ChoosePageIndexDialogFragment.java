package com.blogspot.zongjia.pttmarvel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

public class ChoosePageIndexDialogFragment extends AppCompatDialogFragment {
    private ChangePageListener listener;
    private int chooseIndexNumber = 1;

    public interface ChangePageListener {
        void chnageQyeryPageByIndex(int index);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("onCreateDialog", "ok");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//        builder.setTitle("選擇");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choose_page_index, null);

        int maxIndexNumber = getArguments().getInt("mMaxPageIndexNumber", 1);
        chooseIndexNumber = getArguments().getInt("mCurrentPageIndexNumber", 1);
        NumberPicker indexNumberPicker = view.findViewById(R.id.page_index_number_picker);

        indexNumberPicker.setMinValue(1);
        indexNumberPicker.setMaxValue(maxIndexNumber);
        indexNumberPicker.setValue(chooseIndexNumber);

        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int chooseIndexNumber = indexNumberPicker.getValue();
                // TODO::: 傳遞選擇的索引值給原本的ArticleListFragment
                Log.d("chooseIndexNumber", "indexNumber = "+chooseIndexNumber);
                if (listener != null)
                    listener.chnageQyeryPageByIndex(chooseIndexNumber);
                dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public void setChangePageListener(ChangePageListener listener) {
        this.listener = listener;
    }
}
