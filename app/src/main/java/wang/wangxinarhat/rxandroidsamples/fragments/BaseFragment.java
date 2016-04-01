package wang.wangxinarhat.rxandroidsamples.fragments;

import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import butterknife.OnClick;
import rx.Subscription;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.global.BaseApplication;

public class BaseFragment
        extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BaseApplication.getRefWatcher();
        refWatcher.watch(this);
    }


    protected Subscription subscription;

    @Nullable
    @OnClick(R.id.tipBt)
    void tip() {

        if (-1 != getDialogRes() && -1 != getTitleRes()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getTitleRes())
                    .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    protected int getDialogRes() {
        return -1;
    }

    protected int getTitleRes() {
        return -1;
    }
}
