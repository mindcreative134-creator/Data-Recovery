package infiapp.envento.photorecoverynew.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import infiapp.envento.photorecoverynew.model.AlbumPhoto;
import infiapp.envento.photorecoverynew.model.PhotoModel;
import com.recovery.photodeleted.data.R;

public class AlbumsImageAdapter extends RecyclerView.Adapter<AlbumsImageAdapter.MyViewHolder> {

    Context context;
    ArrayList<AlbumPhoto> albumPhotos = new ArrayList<>();
    OnClickItemListener mOnClickItemListener;
    Activity aaa;

    // Cycling folder colors matching reference screenshot
    private static final int[] FOLDER_COLORS = {
        Color.parseColor("#FF4F81"), // Pink  - All Photos
        Color.parseColor("#FF9800"), // Orange - Screenshots
        Color.parseColor("#7B52E0"), // Purple - Documents
        Color.parseColor("#00BCD4"), // Cyan   - People
        Color.parseColor("#4CAF50"), // Green  - Maps
        Color.parseColor("#FF6B6B"), // Coral  - ID Photos
        Color.parseColor("#E91E8C"), // Hot pink - Favourites
        Color.parseColor("#2196F3"), // Blue
        Color.parseColor("#9C27B0"), // Deep Purple
        Color.parseColor("#009688"), // Teal
    };

    public AlbumsImageAdapter(Context context, ArrayList<AlbumPhoto> mList,
                              OnClickItemListener onClickItemListener) {
        this.context = context;
        this.albumPhotos = mList;
        mOnClickItemListener = onClickItemListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mPhotoFileSize;
        RecyclerView mImageListView;
        LinearLayout mFolderIcon;

        public MyViewHolder(View view, OnClickItemListener onClickItemListener) {
            super(view);
            this.mImageListView = view.findViewById(R.id.recycler_view_list);
            mPhotoFileSize     = view.findViewById(R.id.tv_folder2);
            mFolderIcon        = view.findViewById(R.id.imageLayout);

            // ✅ Full card click — anywhere tap = open album
            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_ID && onClickItemListener != null) {
                    onClickItemListener.onClickItem(pos);
                }
            });

            // Prevent inner RecyclerView from swallowing parent click
            mImageListView.setFocusable(false);
            mImageListView.setClickable(false);
            mImageListView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_album_new, parent, false);
        return new MyViewHolder(itemView, mOnClickItemListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AlbumPhoto album = albumPhotos.get(position);

        // Set count text
        int count = album.getListPhoto().size();
        holder.mPhotoFileSize.setText(count + "\nPictures");

        // Set cycling background color on folder square
        int color = FOLDER_COLORS[position % FOLDER_COLORS.length];
        holder.mFolderIcon.setBackgroundColor(color);

        // Load thumbnails (max 4 shown)
        ArrayList<PhotoModel> singleSectionItems = album.getListPhoto();
        SectionListDataAdapterForImages itemListDataAdapter =
                new SectionListDataAdapterForImages(context, singleSectionItems, position, aaa);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false);
        holder.mImageListView.setLayoutManager(layoutManager);
        holder.mImageListView.setHasFixedSize(true);
        holder.mImageListView.setAdapter(itemListDataAdapter);
    }

    @Override
    public int getItemCount() {
        return albumPhotos.size();
    }

    public interface OnClickItemListener {
        void onClickItem(int position);
    }
}
