package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import wang.wangxinarhat.rxandroidsamples.R;

/**
 * Created by wangxinarhat on 16-4-5.
 */
public class TokenAdvancedFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_token_advanced,container,false);

        ButterKnife.bind(this,view);
        return view;


    }
}
