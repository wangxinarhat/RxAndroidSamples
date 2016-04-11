package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.adapter.LogAdapter;

/**
 * Created by wang on 2016/4/11.
 */
public class BufferFragment extends BaseFragment {

    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.btn)
    Button btn;


    private List<String> mLogs;
    private LogAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buffer, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        setLogger();


        subscription = RxView.clicks(btn)
                .map(new Func1<Void, Integer>() {
                    @Override
                    public Integer call(Void aVoid) {
                        log("点击一次");
                        return 1;
                    }
                })
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private Observer<List<Integer>> getObserver() {
        return new Observer<List<Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Integer> integers) {

                log(String.format("3s内 %d 次点击", integers.size()));
            }
        };
    }


    private void log(String logMsg) {

        if (isCurrentlyOnMainThread()) {
            mLogs.add(logMsg + " (main thread) ");
            mAdapter.notifyDataSetChanged();

        } else {

            mLogs.add(logMsg + " (NOT main thread) ");

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    private boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    private void setLogger() {
        mLogs = new ArrayList<>();
        mAdapter = new LogAdapter(mLogs);
        recycler.setAdapter(mAdapter);
    }

}
