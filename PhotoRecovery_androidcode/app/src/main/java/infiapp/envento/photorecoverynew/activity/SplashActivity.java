package infiapp.envento.photorecoverynew.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.recovery.photodeleted.data.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivSplashLogo = findViewById(R.id.ivSplashLogo);
        if (ivSplashLogo != null) {
            // State-of-the-art bounce popup overshoot scale animation
            ivSplashLogo.setScaleX(0.2f);
            ivSplashLogo.setScaleY(0.2f);
            ivSplashLogo.setAlpha(0.0f);
            
            ivSplashLogo.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1.0f)
                    .setDuration(1300)
                    .setInterpolator(new android.view.animation.OvershootInterpolator(1.4f))
                    .start();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);

    }
}