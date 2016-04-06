package wang.wangxinarhat.rxandroidsamples.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import wang.wangxinarhat.rxandroidsamples.R;

public class MainFragment
        extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }


    private void clickedOn(@NonNull Class<? extends Fragment> fragmentClass) {

        final String tag = fragmentClass.getSimpleName();

        Fragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }


    @OnClick({R.id.btn_basic, R.id.btn_map, R.id.btn_zip, R.id.btn_flatmap_token, R.id.btn_flatmap_token_advance, R.id.btn_cache})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic:
                clickedOn(PrimaryFragment.class);
                break;
            case R.id.btn_map:
                clickedOn(MapFragment.class);
                break;
            case R.id.btn_zip:
                clickedOn(ZipFragment.class);
                break;
            case R.id.btn_flatmap_token:
                clickedOn(TokenFragment.class);
                break;
            case R.id.btn_flatmap_token_advance:
                clickedOn(TokenAdvancedFragment.class);
                break;
            case R.id.btn_cache:
                clickedOn(SubjectFragment.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
