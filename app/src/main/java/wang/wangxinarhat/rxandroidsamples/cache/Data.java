package wang.wangxinarhat.rxandroidsamples.cache;

import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.annotation.DataSource;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;
import wang.wangxinarhat.rxandroidsamples.global.BaseApplication;
import wang.wangxinarhat.rxandroidsamples.http.Network;
import wang.wangxinarhat.rxandroidsamples.operators.BeautyResult2Beautise;

/**
 * Created by wang on 2016/4/6.
 */
public class Data {

    private static Data instance;
    private static final int DATA_SOURCE_MEMORY = 1;
    private static final int DATA_SOURCE_DISK = 2;
    private static final int DATA_SOURCE_NETWORK = 3;

//    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK})

    BehaviorSubject<List<ImageInfoBean>> cache;

    private int dataSource;

    private Data() {
    }

    public static Data newInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    private void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return BaseApplication.getApplication().getString(dataSourceTextRes);
    }

    public void loadData() {

        Network.getGankApi()
                .getBeauties(80, 1)
                .map(BeautyResult2Beautise.newInstance())
                .doOnNext(new Action1<List<ImageInfoBean>>() {
                    @Override
                    public void call(List<ImageInfoBean> list) {
                        DataCache.newInstance().writeData(list);
                    }
                })
                .subscribe(new Action1<List<ImageInfoBean>>() {
                    @Override
                    public void call(List<ImageInfoBean> list) {
                        cache.onNext(list);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }


    public Subscription subscribeData(@Nullable Observer<List<ImageInfoBean>> observer) {


        if (null == cache) {
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<List<ImageInfoBean>>() {
                @Override
                public void call(Subscriber<? super List<ImageInfoBean>> subscriber) {

                    List<ImageInfoBean> list = DataCache.newInstance().readData();

                    if (null == list) {
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadData();
                    } else {
                        setDataSource(DATA_SOURCE_DISK);
                        subscriber.onNext(list);
                    }


                }
            })
                    .subscribeOn(Schedulers.io()).subscribe(cache);

        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }

        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

    }


    public void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        DataCache.newInstance().deleteCache();
    }


}
