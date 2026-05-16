package infiapp.envento.photorecoverynew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import infiapp.envento.photorecoverynew.model.PhotoModel;
import com.recovery.photodeleted.data.R;
import infiapp.envento.photorecoverynew.view.SquareImageView;

public class SectionListDataAdapterForImages
        extends RecyclerView.Adapter<SectionListDataAdapterForImages.SingleItemRowHolder> {

    private ArrayList<PhotoModel> itemsList;
    private Context mContext;
    int size;
    int postion;
    Activity aaa;

    public SectionListDataAdapterForImages(Context context, ArrayList<PhotoModel> itemsList,
            int mPostion, Activity aaa) {
        this.itemsList = itemsList;
        this.mContext = context;
        postion = mPostion;
        this.aaa = aaa;
        // Show max 4 thumbnails in album row (enough for preview)
        if (itemsList.size() >= 4) {
            size = 4;
        } else {
            size = itemsList.size();
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_image, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {
        PhotoModel singleItem = itemsList.get(i);
        try {
            Glide.with(mContext)
                    .load("file://" + singleItem.getPathPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .centerCrop()
                    .error(R.drawable.ic_error)
                    .into(holder.itemImage);
        } catch (Exception e) {
            // ignore
        }
        // ✅ Do NOT consume click — let parent CardView handle it
        holder.itemImage.setClickable(false);
        holder.itemImage.setFocusable(false);
        holder.itemView.setClickable(false);
        holder.itemView.setFocusable(false);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected SquareImageView itemImage;

        public SingleItemRowHolder(View view) {
            super(view);
            this.itemImage = view.findViewById(R.id.ivImage);
        }
    }
}