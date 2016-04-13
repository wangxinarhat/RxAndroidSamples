package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.functions.Func3;
import wang.wangxinarhat.rxandroidsamples.R;

/**
 * Created by wang on 2016/4/13.
 */
public class CombineLatestFragment extends BaseFragment {

    @Bind(R.id.username)
    EditText mUsername;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.button)
    Button mButton;
    private Observable<CharSequence> usernameObservable, emailObservable, passwordObservable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_combine, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButton.setEnabled(false);

        combineLatestEvent();
    }


    private void combineLatestEvent() {

        usernameObservable = RxTextView.textChanges(mUsername).skip(1);
        emailObservable = RxTextView.textChanges(mEmail).skip(1);
        passwordObservable = RxTextView.textChanges(mPassword).skip(1);

        subscription = Observable.combineLatest(usernameObservable, emailObservable, passwordObservable,
                new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence userName, CharSequence email, CharSequence password) {

                        boolean isUserNameValid = !TextUtils.isEmpty(userName) && (userName.toString().length() > 2 && userName.toString().length() < 9);

                        if (!isUserNameValid) {
                            mUsername.setError("用户名无效");
                        }


                        boolean isEmailValid = !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();

                        if (!isEmailValid) {
                            mEmail.setError("邮箱无效");
                        }

                        boolean isPasswordValid = !TextUtils.isEmpty(password) && (password.toString().length() > 6 && password.toString().length() < 11);

                        if (!isPasswordValid) {
                            mPassword.setError("密码无效");
                        }


                        return isUserNameValid && isEmailValid && isPasswordValid;
                    }
                })
                .subscribe(getObserver());
    }

    private Observer<Boolean> getObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

                mButton.setEnabled(aBoolean);
            }
        };
    }

    @OnClick(R.id.button)
    public void onClick() {
        Toast.makeText(getActivity(), "注册", Toast.LENGTH_SHORT).show();
    }
}
