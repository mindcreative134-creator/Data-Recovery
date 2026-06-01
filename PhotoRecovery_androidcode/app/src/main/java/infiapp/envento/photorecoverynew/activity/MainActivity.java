package infiapp.envento.photorecoverynew.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import com.recovery.photodeleted.data.BuildConfig;
import com.recovery.photodeleted.data.R;
import infiapp.envento.photorecoverynew.ads.AdmobAdsModel;
import infiapp.envento.photorecoverynew.utills.Utils;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    TextView startBtn, totalImage, totalVideo, totalAudio, totalOther;
    LinearLayout allImages, allVideos, allAudios, allOthers;
    LinearLayout navHome, navRecovered, navTrash, navSettings;
    androidx.cardview.widget.CardView bannerCard;

    String[] permissions = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    MaterialToolbar toolBar;

    @Override
    protected void onResume() {
        super.onResume();
        loadCachedCounts();
    }

    private void loadCachedCounts() {
        android.content.SharedPreferences prefs = getSharedPreferences("data_recovery_prefs", MODE_PRIVATE);
        Utils.noOfImage = prefs.getString("noOfImage", "0");
        Utils.noOfVideo = prefs.getString("noOfVideo", "0");
        Utils.noOfAudio = prefs.getString("noOfAudio", "0");
        Utils.noOfOther = prefs.getString("noOfOther", "0");

        if (totalImage != null) totalImage.setText(Utils.noOfImage + " Files");
        if (totalVideo != null) totalVideo.setText(Utils.noOfVideo + " Files");
        if (totalAudio != null) totalAudio.setText(Utils.noOfAudio + " Files");
        if (totalOther != null) totalOther.setText(Utils.noOfOther + " Files");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load ads with a safety delay to prevent blocking the main thread during SDK initialization
        new android.os.Handler().postDelayed(() -> {
            try {
                if (!isFinishing() && !isDestroyed()) {
                    new AdmobAdsModel(MainActivity.this).interstitialAdLoad(MainActivity.this);
                    new AdmobAdsModel(MainActivity.this).bannerAds(MainActivity.this, findViewById(R.id.adsView));
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error loading startup ads: " + e.getMessage());
            }
        }, 3000);

        startBtn = findViewById(R.id.startBtn);
        totalImage = findViewById(R.id.totalImage);
        totalVideo = findViewById(R.id.totalVideo);
        totalAudio = findViewById(R.id.totalAudio);
        totalOther = findViewById(R.id.totalOther);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        toolBar.inflateMenu(R.menu.toolbar_menu);
        
        // Make the left menu button functional - opens the settings activity
        toolBar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        bannerCard = findViewById(R.id.recycling);
        allImages = findViewById(R.id.allImages);
        allVideos = findViewById(R.id.allVideos);
        allAudios = findViewById(R.id.allAudios);
        allOthers = findViewById(R.id.allOthers);

        // Bottom Nav
        navHome = findViewById(R.id.navHome);
        navRecovered = findViewById(R.id.navRecovered);
        navTrash = findViewById(R.id.navTrash);
        navSettings = findViewById(R.id.navSettings);

        // Make entire banner trigger scan
        bannerCard.setOnClickListener(v -> {
            permission();
        });

        // "Start Scan" button also triggers it
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> {
            permission();
        });

        loadCachedCounts();

        allImages.setOnClickListener(v -> {
            new AdmobAdsModel(this).interstitialAdShow(this, () -> {
                if (Utils.mAlbumPhotos.size() != 0) {
                    Intent intent = new Intent(getApplicationContext(), AlbumActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please Start Scanning", Toast.LENGTH_SHORT).show();
                }
            });
        });

        allVideos.setOnClickListener(v -> {
            new AdmobAdsModel(this).interstitialAdShow(this, () -> {
                if (Utils.mAlbumVideos.size() != 0) {
                    Intent intent = new Intent(getApplicationContext(), VideoAlbumActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please Start Scanning", Toast.LENGTH_SHORT).show();
                }
            });

        });

        allAudios.setOnClickListener(v -> {
            new AdmobAdsModel(this).interstitialAdShow(this, () -> {
                if (Utils.mAlbumAudios.size() != 0) {
                    Intent intent = new Intent(getApplicationContext(), AudioAlbumActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please Start Scanning", Toast.LENGTH_SHORT).show();
                }
            });

        });

        allOthers.setOnClickListener(v -> {
            new AdmobAdsModel(this).interstitialAdShow(this, () -> {
                if (Utils.mAlbumOthers.size() != 0) {
                    Intent intent = new Intent(getApplicationContext(), OtherAlbumActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please Start Scanning", Toast.LENGTH_SHORT).show();
                }
            });

        });

        // Bottom Nav Actions
        navHome.setOnClickListener(v -> {
            // Already on home
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        });

        navRecovered.setOnClickListener(v -> {
            // Usually shows a list of recovered files, for now toast
            Toast.makeText(this, "Recovered Files", Toast.LENGTH_SHORT).show();
        });

        navTrash.setOnClickListener(v -> {
            Toast.makeText(this, "Trash", Toast.LENGTH_SHORT).show();
        });

        navSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        Log.d("debug", "activity : onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:

                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            default:
                // Do Something
                break;
        }
        return true;
    }

    void permission() {

        if (SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), getString(R.string.app_name)
                        + " required permissions to access all files.Please go to Setting and enable the 'all files access' then go back ",
                        Snackbar.LENGTH_INDEFINITE);

                Snackbar.SnackbarLayout params = (Snackbar.SnackbarLayout) snack.getView();
                params.setMinimumHeight(150);
                snack.setAction("Settings", v -> {
                    try {
                        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                        startActivity(intent);
                    } catch (Exception ex) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    }
                });
                snack.show();
            } else {
                // Permission granted — go straight to scan (no ad before scan, AdMob policy)
                startActivity(new Intent(MainActivity.this, ScanningActivity.class));
            }
        } else {

            // And finally ask for the permission
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                // Go straight to scan without ad — ad before scan violates AdMob policy
                startActivity(new Intent(MainActivity.this, ScanningActivity.class));
            } else {
                ActivityCompat.requestPermissions(this, permissions, 101);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, ScanningActivity.class));
                } else {

                    // Do Something
                }
                break;
        }

    }

}