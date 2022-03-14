package yzl.swu.yyreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.LoginActivity;
import yzl.swu.yyreader.databinding.SelfFragmentBinding;

public class SelfFragment extends BaseFragment<SelfFragmentBinding> {
    @Override
    protected void initWidget() {
        initEvents();
    }

    private void initEvents() {
        viewBinding.catTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
