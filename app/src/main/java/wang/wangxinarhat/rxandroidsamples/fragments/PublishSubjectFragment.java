package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import wang.wangxinarhat.rxandroidsamples.R;

/**
 * Created by wang on 2016/4/12.
 */
public class PublishSubjectFragment extends BaseFragment {

    @Bind(R.id.num1)
    EditText num1;
    @Bind(R.id.num2)
    EditText num2;
    @Bind(R.id.result)
    TextView result;
    private PublishSubject<Float> resultEmitterSubject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_publish_subject, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resultEmitterSubject = PublishSubject.create();
        subscription = resultEmitterSubject
                .asObservable()
                .subscribe(new Action1<Float>() {
                    @Override
                    public void call(Float aFloat) {
                        result.setText(String.valueOf(aFloat));
                    }
                });

        numChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnTextChanged({R.id.num1, R.id.num2})
    public void numChanged() {
        float num1 = 0f;
        float num2 = 0f;

        if (!TextUtils.isEmpty(this.num1.getText())) {
            num1 = Float.valueOf(this.num1.getText().toString());
        }
        if (!TextUtils.isEmpty(this.num2.getText())) {
            num2 = Float.valueOf(this.num2.getText().toString());
        }

        resultEmitterSubject.onNext(num1 + num2);
    }
}
