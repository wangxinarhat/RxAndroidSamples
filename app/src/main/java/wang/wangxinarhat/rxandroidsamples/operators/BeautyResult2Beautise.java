package wang.wangxinarhat.rxandroidsamples.operators;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;
import wang.wangxinarhat.rxandroidsamples.domain.BeautyResult;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;

/**
 * Created by wang on 2016/4/5.
 */
public class BeautyResult2Beautise implements Func1<BeautyResult, List<ImageInfoBean>> {


    public static BeautyResult2Beautise newInstance() {
        return new BeautyResult2Beautise();
    }


    @Override
    public List<ImageInfoBean> call(BeautyResult beautyResult) {

        List<ImageInfoBean> imageInfoBeanList = new ArrayList<>(beautyResult.results.size());

        for (ImageInfoBean bean : beautyResult.results) {
            ImageInfoBean imageInfoBean = new ImageInfoBean();

            imageInfoBean.description = bean.desc;

            imageInfoBean.image_url = bean.url;

            imageInfoBeanList.add(imageInfoBean);

        }

        return imageInfoBeanList;
    }
}
