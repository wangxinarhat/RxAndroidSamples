package wang.wangxinarhat.rxandroidsamples.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wang.wangxinarhat.rxandroidsamples.R;

/**
 * Created by wang on 2016/4/11.
 */
public class SimpleTextHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.log)
    TextView log;

    public TextView getLog() {
        return log;
    }

    public SimpleTextHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
