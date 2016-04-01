// (c)2016 Flipboard Inc, All Rights Reserved.

package wang.wangxinarhat.rxandroidsamples.http.api;

import android.support.annotation.NonNull;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;
import wang.wangxinarhat.rxandroidsamples.domain.FakeThing;
import wang.wangxinarhat.rxandroidsamples.domain.FakeToken;

public class FakeApi {
    Random random = new Random();

    public Observable<FakeToken> getFakeToken(@NonNull String fakeAuth) {
        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() {
                    @Override
                    public FakeToken call(String fakeAuth) {
                        // Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        FakeToken fakeToken = new FakeToken();
                        fakeToken.token = createToken();
                        return fakeToken;
                    }
                });
    }

    private static String createToken() {
        return "fake_token_" + System.currentTimeMillis() % 10000;
    }

    public Observable<FakeThing> getFakeData(FakeToken fakeToken) {
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing call(FakeToken fakeToken) {
                        // Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (fakeToken.expired) {
                            throw new IllegalArgumentException("Token expired!");
                        }

                        FakeThing fakeData = new FakeThing();
                        fakeData.id = (int) (System.currentTimeMillis() % 1000);
                        fakeData.name = "FAKE_USER_" + fakeData.id;
                        return fakeData;
                    }
                });
    }
}
