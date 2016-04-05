package wang.wangxinarhat.rxandroidsamples.http.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;

/**
 * Created by wang on 2016/4/5.
 */
public interface ImageApi {
    @GET("search")
    Observable<List<ImageInfoBean>> search(@Query("q") String query);
}
