// (c)2016 Flipboard Inc, All Rights Reserved.

package wang.wangxinarhat.rxandroidsamples.http.api;


import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ImageInfoBean>> search(@Query("q") String query);
}
