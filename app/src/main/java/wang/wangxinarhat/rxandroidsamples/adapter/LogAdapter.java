package wang.wangxinarhat.rxandroidsamples.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.holder.SimpleTextHolder;

/**
 * Created by wang on 2016/4/11.
 */
public class LogAdapter extends RecyclerView.Adapter<SimpleTextHolder> {

    private List<String> mLogs;

    public LogAdapter(List<String> logs) {
        this.mLogs = logs;
    }

    @Override
    public SimpleTextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log,parent,false);
        return new SimpleTextHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleTextHolder holder, int position) {

        holder.getLog().setText(mLogs.get(position));
    }

    @Override
    public int getItemCount() {
        return null == mLogs?0:mLogs.size();
    }


}
