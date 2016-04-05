package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.domain.DataInfo;
import wang.wangxinarhat.rxandroidsamples.domain.Token;
import wang.wangxinarhat.rxandroidsamples.http.Network;
import wang.wangxinarhat.rxandroidsamples.http.api.TokenApi;

/**
 * Created by wangxinarhat on 16-4-5.
 */
public class TokenFragment extends BaseFragment {


    @Bind(R.id.tokenTv)
    TextView tokenTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.requestBt)
    void upload() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final TokenApi tokenApi = Network.getTokenApi();
        subscription = tokenApi.getToken("flat_map")
                .flatMap(new Func1<Token, Observable<DataInfo>>() {
                    @Override
                    public Observable<DataInfo> call(Token token) {
                        return tokenApi.getData(token);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<DataInfo>() {
                    @Override
                    public void call(DataInfo dataInfo) {
                        swipeRefreshLayout.setRefreshing(false);
                        tokenTv.setText(String.format(getString(R.string.got_data),dataInfo.id,dataInfo.name));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_token, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unsubscribe();
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token;
    }
}
