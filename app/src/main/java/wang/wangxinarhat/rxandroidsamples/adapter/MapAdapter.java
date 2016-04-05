package wang.wangxinarhat.rxandroidsamples.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import wang.wangxinarhat.rxandroidsamples.R;
import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;
import wang.wangxinarhat.rxandroidsamples.holder.ImageInfoHolder;

/**
 * Created by wang on 2016/4/5.
 */
public class MapAdapter extends RecyclerView.Adapter<ImageInfoHolder> {
    @Override
    public ImageInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new ImageInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageInfoHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(images.get(position).image_url).into(holder.getImage());
        holder.getDescription().setText(images.get(position).description);
    }


    @Override
    public int getItemCount() {
        return null == images ? 0 : images.size();
    }

    private List<ImageInfoBean> images;

    public void setImages(List<ImageInfoBean> images) {
        this.images = images;
        notifyDataSetChanged();
    }
}
