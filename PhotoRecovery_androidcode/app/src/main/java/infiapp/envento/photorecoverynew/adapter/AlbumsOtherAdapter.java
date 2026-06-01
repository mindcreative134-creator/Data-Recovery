package infiapp.envento.photorecoverynew.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import infiapp.envento.photorecoverynew.model.AlbumOthers;
import com.recovery.photodeleted.data.R;

public class AlbumsOtherAdapter extends RecyclerView.Adapter<AlbumsOtherAdapter.MyViewHolder> {

    Context context;
    ArrayList<AlbumOthers> albumOthers = new ArrayList<>();
    OnClickItemListener mOnClickItemListener;
    Activity aaa;

    public AlbumsOtherAdapter(Context context, ArrayList<AlbumOthers> mList, OnClickItemListener onClickItemListener) {
        this.context = context;
        this.albumOthers = mList;
        mOnClickItemListener = onClickItemListener;
        Log.e("01122", "intData_2: " + albumOthers);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_album_audio, parent, false);
        return new MyViewHolder(itemView, mOnClickItemListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int count = albumOthers.get(position).getListOther().size();
        holder.mOtherFileSize.setText(count + "\nOthers");
        
        // Extract folder name from directory path for clean presentation
        String folderPath = albumOthers.get(position).getStrOtherFolder();
        holder.filePath.setText(folderPath);
        
        if (folderPath != null) {
            java.io.File file = new java.io.File(folderPath);
            holder.tvFolderName.setText(file.getName());
        } else {
            holder.tvFolderName.setText("Unknown Folder");
        }

        // Set sleek cyan background and dynamic other files icon
        if (holder.mFolderIcon != null) {
            holder.mFolderIcon.setBackgroundResource(R.drawable.bg_card_others);
        }
        if (holder.ivFolderIcon != null) {
            holder.ivFolderIcon.setImageResource(R.drawable.ic_cat_others);
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mOtherFileSize;
        TextView filePath;
        TextView tvFolderName;
        android.widget.LinearLayout mFolderIcon;
        android.widget.ImageView ivFolderIcon;

        OnClickItemListener onClickItemListener;

        public MyViewHolder(View view, OnClickItemListener onClickItemListener) {
            super(view);

            mOtherFileSize = (TextView) view.findViewById(R.id.tv_folder2);
            filePath = (TextView) view.findViewById(R.id.filePath);
            tvFolderName = (TextView) view.findViewById(R.id.tvFolderName);
            mFolderIcon = (android.widget.LinearLayout) view.findViewById(R.id.imageLayout);
            ivFolderIcon = (android.widget.ImageView) view.findViewById(R.id.iv_folder_icon);
            this.onClickItemListener = onClickItemListener;
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickItemListener.onClickItem(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return albumOthers.size();
    }

    public interface OnClickItemListener {
        void onClickItem(int position);
    }

}
