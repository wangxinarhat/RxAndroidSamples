package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.adapter.PrimaryAdapter;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;
import wang.wangxinarhat.rxandroidsamples.http.Network;
import wang.wangxinarhat.rxandroidsamples.operators.BeautyResult2Beautise;

/**
 * Created by wang on 2016/4/5.
 */
public class MapFragment extends BaseFragment {

    private int page;
    @Bind(R.id.pageTv)
    TextView pageTv;
    @Bind(R.id.previousPageBt)
    AppCompatButton previousPageBt;
    @Bind(R.id.nextPageBt)
    AppCompatButton nextPageBt;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

    PrimaryAdapter adapter;

    Observer<List<ImageInfoBean>> observer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        if (null == adapter) {
            adapter = new PrimaryAdapter();
        }
        gridRv.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.previousPageBt, R.id.nextPageBt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previousPageBt:
                if (page > 1) {
                    loadPage(--page);

                    previousPageBt.setEnabled(true);
                } else {
                    previousPageBt.setEnabled(false);
                }
                break;
            case R.id.nextPageBt:
                loadPage(page++);
                if (page > 1) {
                    previousPageBt.setEnabled(true);
                }
                break;
        }
    }


    private void loadPage(int page) {
//        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Network.getGankApi()
                .getBeauties(6, page)
                .map(BeautyResult2Beautise.newInstance())
                .subscribeOn(Schedulers.io())
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
                    Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(List<ImageInfoBean> images) {
                    adapter.setImages(images);
                }
            };
        }


        return observer;
    }


    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }
}
