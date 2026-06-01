package infiapp.envento.photorecoverynew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import infiapp.envento.photorecoverynew.model.AlbumAudio;
import com.recovery.photodeleted.data.R;

public class AlbumsAudioAdapter extends RecyclerView.Adapter<AlbumsAudioAdapter.MyViewHolder> {

    Context context;
    ArrayList<AlbumAudio> albumAudios = new ArrayList<>();
    OnClickItemListener mOnClickItemListener;
    Activity aaa;

    public AlbumsAudioAdapter(Context context, ArrayList<AlbumAudio> mList, OnClickItemListener onClickItemListener) {
        this.context = context;
        this.albumAudios = mList;
        mOnClickItemListener = onClickItemListener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mAudioFileSize;
        TextView filePath;
        TextView tvFolderName;
        android.widget.LinearLayout mFolderIcon;
        android.widget.ImageView ivFolderIcon;

        OnClickItemListener onClickItemListener;

        public MyViewHolder(View view, OnClickItemListener onClickItemListener) {
            super(view);

            mAudioFileSize = (TextView) view.findViewById(R.id.tv_folder2);
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_album_audio, parent, false);
        return new MyViewHolder(itemView, mOnClickItemListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int count = albumAudios.get(position).getListAudio().size();
        holder.mAudioFileSize.setText(count + "\nAudios");
        
        // Extract folder name from directory path for clean presentation
        String folderPath = albumAudios.get(position).getStrAudioFolder();
        holder.filePath.setText(folderPath);
        
        if (folderPath != null) {
            java.io.File file = new java.io.File(folderPath);
            holder.tvFolderName.setText(file.getName());
        } else {
            holder.tvFolderName.setText("Unknown Folder");
        }

        // Set warm orange background and dynamic audio icon
        if (holder.mFolderIcon != null) {
            holder.mFolderIcon.setBackgroundResource(R.drawable.bg_card_audios);
        }
        if (holder.ivFolderIcon != null) {
            holder.ivFolderIcon.setImageResource(R.drawable.ic_cat_audio);
        }

    }

    @Override
    public int getItemCount() {
        return albumAudios.size();
    }

    public interface OnClickItemListener {
        void onClickItem(int position);
    }
}
