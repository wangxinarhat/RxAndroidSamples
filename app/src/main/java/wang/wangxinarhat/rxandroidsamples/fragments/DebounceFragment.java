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
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.adapter.LogAdapter;

/**
 * Created by wang on 2016/4/12.
 */
public class DebounceFragment extends BaseFragment {

    @Bind(R.id.input)
    EditText input;
    @Bind(R.id.recycler)
    RecyclerView recycler;



    private List<String> mLogs;
    private LogAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_debounce, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLogger();
        subscription = RxTextView.textChangeEvents(input)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observer<? super TextViewTextChangeEvent> getObserver() {

        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                log(String.format("搜索关键字 ： %s",textViewTextChangeEvent.text().toString()));
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.clear_input, R.id.clear_log})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_input:
                input.getText().clear();
                break;
            case R.id.clear_log:

                mLogs.clear();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }


    private void log(String logMsg) {

        if (isCurrentlyOnMainThread()) {
            mLogs.add(0,logMsg + " (main thread) ");
            mAdapter.notifyDataSetChanged();

        } else {

            mLogs.add(0,logMsg + " (NOT main thread) ");

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
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLogs = new ArrayList<>();
        mAdapter = new LogAdapter(mLogs);
        recycler.setAdapter(mAdapter);
    }


}
