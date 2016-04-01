// (c)2016 Flipboard Inc, All Rights Reserved.

package wang.wangxinarhat.rxandroidsamples.http.api;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wang.wangxinarhat.rxandroidsamples.domain.GankBeautyResult;

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
