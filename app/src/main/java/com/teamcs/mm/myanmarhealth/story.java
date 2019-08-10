package com.teamcs.mm.myanmarhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.material.snackbar.Snackbar;

public class story extends AppCompatActivity  {

    // ads section
    private PublisherAdView mPublisherAdView;
    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.reward_ads_id),
                new AdRequest.Builder().build());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Use an activity context to get the rewarded video instance.
        //mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        //mRewardedVideoAd.setRewardedVideoAdListener(this);

        //loadRewardedVideoAd();


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter2_ads_id_real));
        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))
        /*
        AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.ads_test_device_id))  // An example device ID
                .build();
                */
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdLoaded",Toast.LENGTH_SHORT).show();
                //mInterstitialAd.show();
                if (mInterstitialAd.isLoaded()) {
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "အခ်ိန္ ႏွစ္စကၠန္႕ ေလာက္ေပးျပီ ေၾကာ္ညာ ၾကည့္ႏိုင္မလား", 10000);
                    snackBar.setAction("ၾကည့္မယ္", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            mInterstitialAd.show();
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (mInterstitialAd.isLoaded()) { mInterstitialAd.show();}
                        }
                    }, 300000);
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdFailedToLoad "+errorCode,Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))
                    }
                }, 5000);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdOpened",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"ေၾကာ္ညာဝင္ၾကည့္ေပးတဲ့အတြက္ ေက်းဇူးပါဗ်ိဳ႕",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                ///Toast.makeText(getApplicationContext(),"mInterstitialAd onAdClicked",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdLeftApplication",Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdClosed",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdClosed",Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // .addTestDevice(getResources().getString(R.string.ads_test_device_id)).
                    }
                }, 300000);
            }
        });



        // ads initialize
        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                // 56BA60CBC498754A2AA31298980C444C
                //.addTestDevice(getResources().getString(R.string.ads_test_device_id))  // An example device ID
                .build();
        mPublisherAdView.loadAd(adRequest);
        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(getApplicationContext(),"onAdLoaded",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //Toast.makeText(getApplicationContext(),"onAdFailedToLoad",Toast.LENGTH_SHORT).show();

                PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                mPublisherAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                //Toast.makeText(getApplicationContext(),"onAdOpened",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                //Toast.makeText(getApplicationContext(),"onAdClicked",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                //Toast.makeText(getApplicationContext(),"onAdLeftApplication",Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                //Toast.makeText(getApplicationContext(),"onAdClosed",Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
            }
        });

        Intent intent = getIntent();
        String story = intent.getExtras().getString("story");
        WebView wv = (WebView) findViewById(R.id.webView);
        String style = "<style>@font-face {font-family: Zawgyi;src: url('file:///android_asset/fonts/zawgyi.ttf')}body {font-family: Zawgyi;font-size: medium;text-align: justify;}body {line-height: 1.5;padding-bottom:100px;}</style>"+story+"<br><br><br><br>";
        wv.loadDataWithBaseURL("",style,"text/html","UTF-8","");

    }

/*
    private void showRewardedVideo() {
        //showVideoButton.setVisibility(View.INVISIBLE);
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }
*/

}
