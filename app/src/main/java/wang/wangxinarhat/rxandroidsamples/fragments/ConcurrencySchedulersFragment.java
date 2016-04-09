package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wang.wangxinarhat.rxandroidsamples.R;

/**
 * Created by wang on 2016/4/9.
 */
public class ConcurrencySchedulersFragment extends BaseFragment {

    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.recycler)
    RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_concurrency_schedulers, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn)
    public void onClick() {

    }
}
