package wang.wangxinarhat.rxandroidsamples.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wang.wangxinarhat.rxandroidsamples.R;

/**
 * Created by wang on 2016/4/1.
 */
public class ImageInfoHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.item_grid_image)
    ImageView image;
    @Bind(R.id.item_grid_description)
    TextView description;

    public ImageView getImage() {
        return image;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageInfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
