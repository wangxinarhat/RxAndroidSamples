package wang.wangxinarhat.rxandroidsamples.domain;

/**
 * Created by wangxinarhat on 16-4-5.
 */
public class Token {
    public String token;
    public boolean isInvalid;

    public Token(boolean isInvalid) {
        this.isInvalid = isInvalid;
    }

    public Token() {
    }
}
