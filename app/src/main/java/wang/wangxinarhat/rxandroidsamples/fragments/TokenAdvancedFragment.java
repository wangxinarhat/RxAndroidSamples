package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import wang.wangxinarhat.rxandroidsamples.http.api.TokenApi;

/**
 * Created by wangxinarhat on 16-4-5.
 */
public class TokenAdvancedFragment extends BaseFragment {


    @Bind(R.id.tokenTv)
    TextView tokenTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    final Token cachedFakeToken = new Token(true);
    boolean tokenUpdated;

    @OnClick(R.id.invalidateTokenBt)
    void incalidate() {
        cachedFakeToken.isInvalid = true;
        Toast.makeText(getActivity(), R.string.token_expired, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.requestBt)
    void request() {
        tokenUpdated = false;
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final TokenApi tokenApi = new TokenApi();
        subscription = Observable.just(null).flatMap(new Func1<Object, rx.Observable<DataInfo>>() {
            @Override
            public Observable<DataInfo> call(Object o) {


                return null == cachedFakeToken.token ?
                        Observable.<DataInfo>error(new NullPointerException("token id null")) :
                        tokenApi.getData(cachedFakeToken);
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
                            return tokenApi.getToken("flat_map")
                                    .doOnNext(new Action1<Token>() {
                                        @Override
                                        public void call(Token token) {
                                            tokenUpdated = true;

                                            cachedFakeToken.token = token.token;
                                            cachedFakeToken.isInvalid = token.isInvalid;
                                        }
                                    });
                        }
                        return Observable.just(throwable);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataInfo>() {
                    @Override
                    public void call(DataInfo dataInfo) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cachedFakeToken.token;
                        if (tokenUpdated) {
                            token += "(" + getString(R.string.updated) + ")";
                        }
                        tokenTv.setText(String.format(getString(R.string.got_token_and_data), token,dataInfo.id, dataInfo.name));
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

        View view = inflater.inflate(R.layout.fragment_token_advanced, container, false);

        ButterKnife.bind(this, view);
        return view;


    }


    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unsubscribe();
    }
}
