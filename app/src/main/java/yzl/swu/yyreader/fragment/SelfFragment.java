package yzl.swu.yyreader.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.R;
import yzl.swu.yyreader.activity.CacheActivity;
import yzl.swu.yyreader.activity.CommentActivity;
import yzl.swu.yyreader.activity.FeedBackActivity;
import yzl.swu.yyreader.activity.LoginActivity;
import yzl.swu.yyreader.activity.RecommendActivity;
import yzl.swu.yyreader.activity.SelfCenterActivity;
import yzl.swu.yyreader.activity.SetupActivity;
import yzl.swu.yyreader.databinding.SelfFragmentBinding;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.models.User;
import yzl.swu.yyreader.remote.RemoteRepository;
import yzl.swu.yyreader.utils.BookManager;

public class SelfFragment extends BaseFragment<SelfFragmentBinding> {
    Activity activity;
    @Override
    protected void initWidget() {
        initEvents();
    }

    private void initEvents() {
        activity = this.getActivity();
        viewBinding.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent,123);
            }
        });

        viewBinding.cacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CacheActivity.class));
            }
        });

        viewBinding.helpAndFeedBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FeedBackActivity.class));
            }
        });

        viewBinding.selfCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelfCenterActivity.class));
            }
        });

        viewBinding.menuSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SetupActivity.class));
            }
        });

        viewBinding.recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RecommendActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 123){
            Disposable disposable = RemoteRepository.getInstance()
                    .getUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userInfo -> {
                                if (userInfo != null) {
                                    viewBinding.userName.setText(userInfo.getNickName() == null ? "" : userInfo.getNickName());
                                }
                            }
                            ,
                            e -> {
                                Log.e(this.getClass().getName(),e.toString());
                                viewBinding.userName.setText("泪无痕");
                            }
                    );
            addDisposable(disposable);
        }
    }
}
