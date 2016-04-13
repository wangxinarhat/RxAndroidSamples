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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.adapter.LogAdapter;

/**
 * Created by wang on 2016/4/12.
 */
public class IntervalFragment extends BaseFragment {

    @Bind(R.id.recycler)
    RecyclerView recycler;


    private List<String> mLogs;
    private LogAdapter mAdapter;

    private CompositeSubscription compositeSubscription;
    private int counter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_interval, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLogger();

        compositeSubscription = new CompositeSubscription();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.simple_polling, R.id.increasingly_delayed_polling})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.simple_polling:
                simplePolling();
                break;
            case R.id.increasingly_delayed_polling:
                increasePolling();
                break;
        }
    }

    private void increasePolling() {
        mLogs.clear();
        mAdapter.notifyDataSetChanged();

        log(String.format("Start increasingly delayed polling now time: [xx:%02d]", + getSecondHand()));

        compositeSubscription.add(

                Observable.just(1)
                .repeatWhen(new RepeatWithDelay(188,1000))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log(String.format("Executing polled task now time : [xx:%02d]", getSecondHand()));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        log("error message : " + throwable.getMessage());
                    }
                })

        );

    }

    private void simplePolling() {

        mLogs.clear();
        mAdapter.notifyDataSetChanged();

        compositeSubscription.add(
                Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                        .map(new Func1<Long, String>() {
                            @Override
                            public String call(Long aLong) {
                                return doNetworkCallAndGetStringResult(aLong);
                            }
                        }).take(6)
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {

                                log(String.format("start simple polling count： %d", counter));
                            }
                        })
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                log(String.format("Executing polled task [%s] now time : [xx:%d]", s, getSecondHand()));
                            }
                        })

        );

    }


    private String doNetworkCallAndGetStringResult(long attempt) {
        try {
            if (attempt == 4) {
                // randomly make one event super long so we test that the repeat logic waits
                // and accounts for this.
                Thread.sleep(9000);
            } else {
                Thread.sleep(3000);
            }

        } catch (InterruptedException e) {
            Timber.d("Operation was interrupted");
        }
        counter++;

        return String.valueOf(counter);
    }

    private int getSecondHand() {
        long millis = System.currentTimeMillis();
        return (int) (TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }


    private void log(String logMsg) {

        if (isCurrentlyOnMainThread()) {
            mLogs.add(0, logMsg + " (main thread) ");
            mAdapter.notifyDataSetChanged();

        } else {

            mLogs.add(0, logMsg + " (NOT main thread) ");

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


    public class RepeatWithDelay
            implements Func1<Observable<? extends Void>, Observable<?>> {

        private final int repeatLimit;
        private final int pollingInterval;
        private int repeatCount = 1;

        RepeatWithDelay(int repeatLimit, int pollingInterval) {
            this.pollingInterval =pollingInterval;
            this.repeatLimit =repeatLimit;
        }

        // this is a notificationhandler, all we care about is
        // the emission "type" not emission "content"
        // only onNext triggers a re-subscription

        @Override
        public Observable<?> call(Observable<? extends Void> inputObservable) {

            // it is critical to use inputObservable in the chain for the result
            // ignoring it and doing your own thing will break the sequence

            return inputObservable.flatMap(new Func1<Void, Observable<?>>() {
                @Override
                public Observable<?> call(Void blah) {


                    if (repeatCount >= repeatLimit) {
                        // terminate the sequence cause we reached the limit
                        log("Completing sequence");
                        return Observable.empty();
                    }

                    // since we don't get an input
                    // we store state in this handler to tell us the point of time we're firing
                    repeatCount++;

                    return Observable.timer(repeatCount * pollingInterval,
                            TimeUnit.MILLISECONDS);
                }
            });
        }
    }



}
