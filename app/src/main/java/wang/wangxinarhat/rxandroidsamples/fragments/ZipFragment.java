package wang.wangxinarhat.rxandroidsamples.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.adapter.PrimaryAdapter;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;
import wang.wangxinarhat.rxandroidsamples.http.Network;
import wang.wangxinarhat.rxandroidsamples.operators.BeautyResult2Beautise;
import wang.wangxinarhat.rxandroidsamples.utils.LogUtils;

/**
 * Created by wang on 2016/4/5.
 */
public class ZipFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private PrimaryAdapter adapter;
    private Observer<List<ImageInfoBean>> observer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (null == adapter) {
            adapter = new PrimaryAdapter();
        }
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(true);


        swipeRefreshLayout.setOnRefreshListener(this);


        load();
    }

    private void load() {

        swipeRefreshLayout.setRefreshing(true);

        subscription = Observable.zip(Network.getGankApi().getBeauties(100, 1).map(BeautyResult2Beautise.newInstance()),
                Network.getZhuangbiApi().search("装逼"),
                new Func2<List<ImageInfoBean>, List<ImageInfoBean>, List<ImageInfoBean>>() {
                    @Override
                    public List<ImageInfoBean> call(List<ImageInfoBean> imageInfoBeen, List<ImageInfoBean> imageInfoBeen2) {


                        int num = imageInfoBeen.size() < imageInfoBeen2.size() ? imageInfoBeen.size() : imageInfoBeen2.size();

                        List<ImageInfoBean> list = new ArrayList<>();
                        for (int i = 0; i < num; i++) {


                            list.add(imageInfoBeen.get(i));
                            list.add(imageInfoBeen2.get(i));


                        }


                        return list;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());


    }


    private Observer<? super List<ImageInfoBean>> getObserver() {

        if (null == observer) {
            observer = new Observer<List<ImageInfoBean>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();


                    LogUtils.LOGD(ZipFragment.class.getSimpleName(), e.toString(), e);
                }


                @Override
                public void onNext(List<ImageInfoBean> images) {

                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setImages(images);
                }
            };
        }

        return observer;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
        ButterKnife.unbind(this);
    }


    @Override
    protected int getDialogRes() {
        return R.layout.dialog_zip;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }


    @Override
    public void onRefresh() {
        load();
    }
}
