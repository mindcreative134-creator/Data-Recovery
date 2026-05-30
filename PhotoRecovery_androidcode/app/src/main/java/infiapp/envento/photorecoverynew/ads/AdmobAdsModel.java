package infiapp.envento.photorecoverynew.ads;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.recovery.photodeleted.data.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdmobAdsModel {

    Context context;
    static InterstitialAd mInterstitialAd;
    String mTag = "AdmobAdsModel";

    // Frequency capping: show interstitial every N navigation actions
    private static final int INTERSTITIAL_FREQUENCY = 3;
    static int navClickCount = 0;

    // Flag used by AppOpenManager to avoid stacking App Open Ad on top of Interstitial
    public static boolean isInterstitialShowing = false;

    public AdmobAdsModel(Context context) {
        this.context = context;
    }

    public void bannerAds(Context context, ViewGroup frameLayout) {

        AdView adView = new AdView(context);
        adView.setAdUnitId(context.getString(R.string.admob_banner_ads));
        adView.setAdSize(getAdSize((Activity) context));
        frameLayout.addView(adView);
        AdRequest build = new AdRequest.Builder().build();
        adView.loadAd(build);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e(mTag, "onAdClosed: ");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(mTag, "onAdFailedToLoad: Banner Ads " + loadAdError.getMessage());
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e(mTag, "onAdOpened: ");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e(mTag, "onAdLoaded: ");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.e(mTag, "onAdClicked: ");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e(mTag, "onAdImpression: ");
            }
        });

    }

    private static AdSize getAdSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity,
                (int) (displayMetrics.widthPixels / displayMetrics.density));
    }

    public void interstitialAdLoad(Context context) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, context.getString(R.string.admob_interstitial_ads), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;
                        Log.d(mTag, "onAdLoaded: ");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d(mTag, "onAdFailedToLoad: " + loadAdError.getMessage());
                    }
                });
    }

    public void interstitialAdShow(Activity context, final GetBackPointer getBackPointer) {

        // Frequency capping: only show interstitial every INTERSTITIAL_FREQUENCY taps
        navClickCount++;
        if (navClickCount % INTERSTITIAL_FREQUENCY != 0) {
            Log.d(mTag, "interstitialAdShow: skipped (count=" + navClickCount + ")");
            if (getBackPointer != null) {
                getBackPointer.returnAction();
            }
            return;
        }

        if (mInterstitialAd != null) {
            isInterstitialShowing = true;
            mInterstitialAd.show(context);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    isInterstitialShowing = false;
                    Log.d(mTag, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Log.d(mTag, "onAdShowedFullScreenContent: ");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    isInterstitialShowing = false;
                    Log.d(mTag, "onAdDismissedFullScreenContent: ");
                    interstitialAdLoad(context);
                    if (getBackPointer != null) {
                        getBackPointer.returnAction();
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Log.d(mTag, "onAdImpression: ");
                }
            });
        } else {
            if (getBackPointer != null) {
                getBackPointer.returnAction();
            }
        }

    }

    public interface GetBackPointer {
        public void returnAction();
    }

    public void nativeAds(Context context, FrameLayout frameLayout) {
        AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.admob_native_advanced_ads))
                .forNativeAd(nativeAd -> {
                    NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(context)
                            .inflate(R.layout.native_ad_layout, null);
                    populateNativeAdView(nativeAd, nativeAdView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(nativeAdView);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Log.e(mTag, "onAdFailedToLoad: Native Ad " + adError.getMessage());
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView nativeAdView) {
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        nativeAdView.setMediaView((MediaView) nativeAdView.findViewById(R.id.ad_media));

        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            nativeAdView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            nativeAdView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        nativeAdView.setNativeAd(nativeAd);
    }

}
