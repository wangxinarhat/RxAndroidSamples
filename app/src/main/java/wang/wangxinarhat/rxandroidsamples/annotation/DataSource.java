package wang.wangxinarhat.rxandroidsamples.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by wang on 2016/4/6.
 */
@Documented
@Target({METHOD, PARAMETER, FIELD})
public @interface DataSource {
}

