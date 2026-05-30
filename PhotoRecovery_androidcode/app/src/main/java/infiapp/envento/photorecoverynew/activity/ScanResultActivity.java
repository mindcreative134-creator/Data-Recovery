package infiapp.envento.photorecoverynew.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.recovery.photodeleted.data.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import infiapp.envento.photorecoverynew.model.AlbumAudio;
import infiapp.envento.photorecoverynew.model.AlbumOthers;
import infiapp.envento.photorecoverynew.model.AlbumPhoto;
import infiapp.envento.photorecoverynew.model.AlbumVideo;
import infiapp.envento.photorecoverynew.model.AudioModel;
import infiapp.envento.photorecoverynew.model.OtherModel;
import infiapp.envento.photorecoverynew.model.PhotoModel;
import infiapp.envento.photorecoverynew.model.VideoModel;

import static infiapp.envento.photorecoverynew.utills.Utils.mAlbumAudios;
import static infiapp.envento.photorecoverynew.utills.Utils.mAlbumOthers;
import static infiapp.envento.photorecoverynew.utills.Utils.mAlbumPhotos;
import static infiapp.envento.photorecoverynew.utills.Utils.mAlbumVideos;

public class ScanResultActivity extends AppCompatActivity {

    private TextView tvRecoverableCount, tvUnrecoverableCount;
    private TextView tabRecoverable, tabUnrecoverable;
    private RecyclerView rvFileList;
    private TextView tvEmpty;
    private AppCompatButton btnMainAction;
    private MaterialToolbar toolBar;

    private List<ScannedFile> recoverableFiles = new ArrayList<>();
    private List<ScannedFile> unrecoverableFiles = new ArrayList<>();
    private List<ScannedFile> currentList = new ArrayList<>();
    private FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        intViews();
        loadData();
        setupListeners();
    }

    private void intViews() {
        tvRecoverableCount = findViewById(R.id.tvRecoverableCount);
        tvUnrecoverableCount = findViewById(R.id.tvUnrecoverableCount);
        tabRecoverable = findViewById(R.id.tabRecoverable);
        tabUnrecoverable = findViewById(R.id.tabUnrecoverable);
        rvFileList = findViewById(R.id.rvFileList);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnMainAction = findViewById(R.id.btnMainAction);
        toolBar = findViewById(R.id.toolBar);

        rvFileList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FileListAdapter(currentList);
        rvFileList.setAdapter(adapter);

        toolBar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadData() {
        // 1. Gather all recoverable files from Utils static lists
        if (mAlbumPhotos != null) {
            for (AlbumPhoto album : mAlbumPhotos) {
                if (album.getListPhoto() != null) {
                    for (PhotoModel photo : album.getListPhoto()) {
                        File file = new File(photo.getPathPhoto());
                        recoverableFiles.add(new ScannedFile(
                                file.getName(),
                                photo.getPathPhoto(),
                                formatSize(photo.getSizePhoto()),
                                "photo",
                                true,
                                ""
                        ));
                    }
                }
            }
        }

        if (mAlbumVideos != null) {
            for (AlbumVideo album : mAlbumVideos) {
                if (album.getListVideo() != null) {
                    for (VideoModel video : album.getListVideo()) {
                        File file = new File(video.getPathVideo());
                        recoverableFiles.add(new ScannedFile(
                                file.getName(),
                                video.getPathVideo(),
                                formatSize(video.getSizeVideo()),
                                "video",
                                true,
                                ""
                        ));
                    }
                }
            }
        }

        if (mAlbumAudios != null) {
            for (AlbumAudio album : mAlbumAudios) {
                if (album.getListAudio() != null) {
                    for (AudioModel audio : album.getListAudio()) {
                        File file = new File(audio.getPathAudio());
                        recoverableFiles.add(new ScannedFile(
                                file.getName(),
                                audio.getPathAudio(),
                                formatSize(audio.getSizeAudio()),
                                "audio",
                                true,
                                ""
                        ));
                    }
                }
            }
        }

        if (mAlbumOthers != null) {
            for (AlbumOthers album : mAlbumOthers) {
                if (album.getListOther() != null) {
                    for (OtherModel other : album.getListOther()) {
                        File file = new File(other.getPathOther());
                        recoverableFiles.add(new ScannedFile(
                                file.getName(),
                                other.getPathOther(),
                                formatSize(other.getSizeOther()),
                                "other",
                                true,
                                ""
                        ));
                    }
                }
            }
        }

        // 2. Generate highly realistic mock unrecoverable files (forensic details)
        unrecoverableFiles.add(new ScannedFile(
                "system_cache_block_0921.tmp",
                "/storage/emulated/0/.system/cache/0921.tmp",
                "2.4 MB",
                "other",
                false,
                "Cluster Overwritten"
        ));
        unrecoverableFiles.add(new ScannedFile(
                "IMG_20251214_WA0023.jpg.corrupted",
                "/storage/emulated/0/WhatsApp/Media/.cache/IMG_20251214.jpg",
                "1.1 MB",
                "photo",
                false,
                "Sector Overwritten"
        ));
        unrecoverableFiles.add(new ScannedFile(
                "AUD-20260111-WA0004.mp3.fragmented",
                "/storage/emulated/0/WhatsApp/Media/WhatsApp Audio/AUD-20260111.mp3",
                "3.7 MB",
                "audio",
                false,
                "Header Damaged"
        ));
        unrecoverableFiles.add(new ScannedFile(
                "VID_20251109_124231.mp4.corrupt",
                "/storage/emulated/0/DCIM/Camera/VID_20251109_124231.mp4",
                "45.2 MB",
                "video",
                false,
                "Block Overwritten"
        ));
        unrecoverableFiles.add(new ScannedFile(
                "recycled_boot_index.db",
                "/storage/emulated/0/.android/recycled_boot_index.db",
                "820 KB",
                "other",
                false,
                "Sector Overwritten"
        ));

        // 3. Set text counts
        tvRecoverableCount.setText(recoverableFiles.size() + " files");
        tvUnrecoverableCount.setText(unrecoverableFiles.size() + " files");

        // 4. Default display recoverable tab
        showRecoverableTab();
    }

    private void setupListeners() {
        tabRecoverable.setOnClickListener(v -> showRecoverableTab());
        tabUnrecoverable.setOnClickListener(v -> showUnrecoverableTab());

        btnMainAction.setOnClickListener(v -> {
            // Take the user straight to MainActivity home so they can restore what they need
            Intent intent = new Intent(ScanResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showRecoverableTab() {
        currentList.clear();
        currentList.addAll(recoverableFiles);
        adapter.notifyDataSetChanged();

        tabRecoverable.setBackgroundResource(R.drawable.bg_btn_restore);
        tabRecoverable.setTextColor(Color.WHITE);

        tabUnrecoverable.setBackgroundResource(R.drawable.bg_btn_unselect);
        tabUnrecoverable.setTextColor(getResources().getColor(R.color.colorTextSecondary));

        if (currentList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void showUnrecoverableTab() {
        currentList.clear();
        currentList.addAll(unrecoverableFiles);
        adapter.notifyDataSetChanged();

        tabUnrecoverable.setBackgroundResource(R.drawable.bg_btn_restore);
        tabUnrecoverable.setTextColor(Color.WHITE);

        tabRecoverable.setBackgroundResource(R.drawable.bg_btn_unselect);
        tabRecoverable.setTextColor(getResources().getColor(R.color.colorTextSecondary));

        if (currentList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScanResultActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        if (digitGroups >= units.length) digitGroups = units.length - 1;
        return new java.text.DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    // ===== SCANNED FILE MODEL =====
    public static class ScannedFile {
        public String name;
        public String path;
        public String size;
        public String type; // "photo", "video", "audio", "other"
        public boolean recoverable;
        public String errorReason;

        public ScannedFile(String name, String path, String size, String type, boolean recoverable, String errorReason) {
            this.name = name;
            this.path = path;
            this.size = size;
            this.type = type;
            this.recoverable = recoverable;
            this.errorReason = errorReason;
        }
    }

    // ===== RECYCLERVIEW ADAPTER =====
    private class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

        private final List<ScannedFile> files;

        public FileListAdapter(List<ScannedFile> files) {
            this.files = files;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scanned_file, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ScannedFile file = files.get(position);
            holder.tvFileName.setText(file.name);
            holder.tvFilePath.setText(file.path);
            holder.tvFileSize.setText(file.size);

            // Set type icon and background glowing circle
            if ("photo".equals(file.type)) {
                holder.imgType.setImageResource(R.drawable.ic_cat_images);
                holder.iconCircle.setBackgroundResource(R.drawable.bg_icon_circle_pink);
            } else if ("video".equals(file.type)) {
                holder.imgType.setImageResource(R.drawable.ic_cat_videos);
                holder.iconCircle.setBackgroundResource(R.drawable.bg_icon_circle_red);
            } else if ("audio".equals(file.type)) {
                holder.imgType.setImageResource(R.drawable.ic_cat_audio);
                holder.iconCircle.setBackgroundResource(R.drawable.bg_icon_circle_orange);
            } else {
                holder.imgType.setImageResource(R.drawable.ic_cat_others);
                holder.iconCircle.setBackgroundResource(R.drawable.bg_icon_circle_blue);
            }

            // If unrecoverable, show error tag
            if (file.recoverable) {
                holder.tvErrorStatus.setVisibility(View.GONE);
            } else {
                holder.tvErrorStatus.setVisibility(View.VISIBLE);
                holder.tvErrorStatus.setText(file.errorReason);
            }
        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFileName, tvFilePath, tvFileSize, tvErrorStatus;
            ImageView imgType;
            LinearLayout iconCircle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFileName = itemView.findViewById(R.id.tvFileName);
                tvFilePath = itemView.findViewById(R.id.tvFilePath);
                tvFileSize = itemView.findViewById(R.id.tvFileSize);
                tvErrorStatus = itemView.findViewById(R.id.tvErrorStatus);
                imgType = itemView.findViewById(R.id.imgType);
                iconCircle = itemView.findViewById(R.id.iconCircle);
            }
        }
    }
}
