package yzl.swu.yyreader.activity;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yzl.swu.yyreader.databinding.ActivitySelfCenterBinding;
import yzl.swu.yyreader.remote.RemoteRepository;

public class SelfCenterActivity extends BaseActivity<ActivitySelfCenterBinding>{
    @Override
    protected void processLogic() {
        super.processLogic();
        loadInfo();
    }

    private void loadInfo() {
        Disposable disposable = RemoteRepository.getInstance()
                .getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userInfo -> {
                            if (userInfo != null) {
                                viewBinding.setupUName.setText(userInfo.getNickName() == null ? "" : userInfo.getNickName());
                                viewBinding.setupPhoneNum.setText(userInfo.getUsername());
                            }
                        }
                        ,
                        e -> {
                            Log.e(this.getClass().getName(),e.toString());
                        }
                );
        addDisposable(disposable);
    }
}
