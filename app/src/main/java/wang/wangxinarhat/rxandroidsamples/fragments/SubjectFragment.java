package wang.wangxinarhat.rxandroidsamples.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.adapter.PrimaryAdapter;
import wang.wangxinarhat.rxandroidsamples.cache.Data;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;

/**
 * Created by wang on 2016/4/6.
 */
public class SubjectFragment extends BaseFragment {


    @Bind(R.id.data_situation)
    TextView dataSituation;
    @Bind(R.id.load)
    Button load;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private PrimaryAdapter adapter;

    private Observer<List<ImageInfoBean>> observer;
    private long startTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Toolbar toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);


        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (null == adapter) {
            adapter = new PrimaryAdapter();
        }
        recycler.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

//        inflater.inflate(R.menu.menu_search, menu);

//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        unsubscribe();
    }

    @OnClick(R.id.load)
    public void onClick() {

        startTime = System.currentTimeMillis();
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Data.newInstance()
                .subscribeData(getObserver());


    }

    private Observer<List<ImageInfoBean>> getObserver() {

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
                public void onNext(List<ImageInfoBean> list) {
                    swipeRefreshLayout.setRefreshing(false);
                    int loadingTime = (int) (System.currentTimeMillis() - startTime);
                    dataSituation.setText(getString(R.string.loading_time_and_source, loadingTime, Data.newInstance().getDataSourceText()));
                    adapter.setImages(list);
                }
            };
        }
        return observer;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_cache;
    }
}
