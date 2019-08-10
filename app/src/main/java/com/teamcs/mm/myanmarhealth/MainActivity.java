package com.teamcs.mm.myanmarhealth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Integer.parseInt;



public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout pullToRefresh;
    ArrayAdapter<String> arrAdapter;
    ListView listView;

    static int last_post_id = 120;
    static int user_coin = 10; // default coin value
    static int article_read_cost = 1;
    static String[] titles;
    static String[] content_list;

    SharedPreferences.Editor editor;
    SharedPreferences pref;


    // MY_PREFS_NAME - a static String variable like:
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String FILE_NAME = "filename";
    public static final String TITLES_FILE = "titles";
    public static final String CONTENT_LIST_FILE = "content_list";


    // ads section
    private PublisherAdView mPublisherAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    private RewardedAdLoadCallback adLoadCallback;


    private String log_tag = "AKM";
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId);
        yourTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/zawgyi.ttf");
        yourTextView.setTypeface(tf);
        */
//haha();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        rewardedAd = new RewardedAd(this,getResources().getString(R.string.reward_ads_id_real));
        adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.i(log_tag,"onRewardedAdLoaded on main activity ");
                //Toast.makeText(getApplicationContext(),"onRewardedAdLoaded",Toast.LENGTH_SHORT).show();
                enableMenuTitles(true);
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Coin  (၁၀)ခုကို ဗီဒိယိုၾကည့္ျပီး ရယူလိုက္ပါ", 10000);
                snackBar.setAction("ရယူမယ္", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your action method here
                        openReward();
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                //Toast.makeText(getApplicationContext(),"onRewardedAdFailedToLoad "+errorCode,Toast.LENGTH_SHORT).show();
                Log.i(log_tag,"onRewardedAdFailedToLoad on main activity "+errorCode);

                rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter1_ads_id_real));
        mInterstitialAd.loadAd(new AdRequest.Builder().build()); //.addTestDevice(getResources().getString(R.string.ads_test_device_id)) .addTestDevice(getResources().getString(R.string.ads_test_device_id))
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
                Log.i(log_tag,"mInterstitialAd onAdLoaded on main activity ");
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
                Log.i(log_tag,"mInterstitialAd onAdFailedToLoad on main activity "+errorCode);
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

                Log.i(log_tag,"mInterstitialAd onAdOpened on main activity ");
                user_coin += 3;
                updateUserCoin(user_coin);
                Toast.makeText(getApplicationContext(),"3 coins are added! "+user_coin,Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdOpened",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"ေၾကာ္ညာဝင္ၾကည့္ေပးတဲ့အတြက္ ေက်းဇူးပါဗ်ိဳ႕",Toast.LENGTH_SHORT).show();
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
                Log.i(log_tag,"mInterstitialAd onAdClosed on main activity ");
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdClosed",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"mInterstitialAd onAdClosed",Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))
                    }
                }, 300000);
            }
        });


        // ads initialize
        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder() //.build();
        // 56BA60CBC498754A2AA31298980C444C
        //.addTestDevice(getResources().getString(R.string.ads_test_device_id))  // An example device ID
                .build();
        mPublisherAdView.loadAd(adRequest);
        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(log_tag,"mPublisherAdView onAdLoaded on main activity ");
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(getApplicationContext(),"onAdLoaded",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(log_tag,"mPublisherAdView onAdFailedToLoad on main activity "+errorCode);
                // Code to be executed when an ad request fails.
                //Toast.makeText(getApplicationContext(),"onAdFailedToLoad",Toast.LENGTH_SHORT).show();

                //PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                //mPublisherAdView.loadAd(adRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build(); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))
                        mPublisherAdView.loadAd(adRequest);
                    }
                }, 5000);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                //Toast.makeText(getApplicationContext(),"onAdOpened",Toast.LENGTH_SHORT).show();
                /*
                user_coin += 2;
                Toast.makeText(getApplicationContext(),"2 coins are added!",Toast.LENGTH_SHORT).show();
                updateUserCoin(user_coin);
                */
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build(); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))
                        mPublisherAdView.loadAd(adRequest);
                    }
                }, 5000);

            }
        });


        /*
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.getting_new_post), Snackbar.LENGTH_LONG).show();
                        //.setAction("Action", null).show();

                fetchWeatherDetails(last_post_id);
                pullToRefresh.setRefreshing(true);
                //fab.setVisibility(false);
                view.setEnabled(false);
            }
        });
        */



        listView = findViewById(R.id.listView);


        titles = new String[] { "Welcome to Myanmar Health" ,"Pull to Refresh"};
        content_list = new String[] { "Welcome to Myanmar Health" ,"Pull to Refresh"};
        /*
        arrAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, titles);
        listView.setAdapter(arrAdapter);
        */

        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, titles);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // we have to check coin in here
                if(user_coin == 0 ){
                    /*
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.insufficient_coin), Toast.LENGTH_SHORT)
                            .show();
                            */

                    haha();
                    // dialog and choose to watch video
                    // specific activity for viewing ads (inter and banner )

                    return;
                }
                // ListView Clicked item index
                int itemPosition     = position;
                user_coin -= article_read_cost;

                editor.putInt("user_coin",user_coin);
                editor.apply();
                updateMenuTitles(user_coin);
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.remaining_coin)+user_coin,Toast.LENGTH_SHORT).show();

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                /*
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_SHORT)
                        .show();
                        */
                String easyPuzzle  = content_list[position];
                Intent i = new Intent(getApplicationContext(), story.class);
                i.putExtra("story", easyPuzzle);
                startActivity(i);

            }

        });

        pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        last_post_id = pref.getInt("last_post_id",last_post_id);
        user_coin = pref.getInt("user_coin",user_coin);



        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshData(); // your code
                fetchWeatherDetails(last_post_id);
                pullToRefresh.setRefreshing(true);
            }
        });
        //updateMenuTitles(last_post_id);
        //enableMenuTitles(false);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        /*
        editor.putString("name", "Elena");
        editor.putInt("idName", 12);
        editor.apply();
        */




        File file = new File(getApplicationContext().getFilesDir(),TITLES_FILE);
        if(file.exists()) {
            //Toast.makeText(getApplicationContext(),"file already exists",Toast.LENGTH_SHORT).show();
            new readJsonFile(TITLES_FILE).execute(); // this file read two files
        }
        else{
            //Toast.makeText(getApplicationContext(),"file does not exists",Toast.LENGTH_SHORT).show();
            // this write two file
            new writeJsonFile(TITLES_FILE,"['ျမန္မာ က်န္းမာေရး App မွ ၾကိဳဆိုပါတယ္။','က်န္းမာေရး သတင္း ေဆာင္းပါး နဲ႕ အ ေမး အ ေျဖ ေတြကို ဖတ္ရႈဖို႕ screen ကို ေအာက္ကို ဆြဲခ် လိုက္ပါ']").execute("['ျမန္မာ က်န္းမာေရး App မွ ၾကိဳဆိုပါတယ္။','က်န္းမာေရး သတင္း ေဆာင္းပါး နဲ႕ အ ေမး အ ေျဖ ေတြကို ဖတ္ရႈဖို႕ screen ကို ေအာက္ကို ဆြဲခ် လိုက္ပါ']");
        }

    }


    private void updateUserCoin(int coin_count){
        user_coin = coin_count;
        editor.putInt("user_coin",coin_count);
        editor.apply();
        updateMenuTitles(user_coin);
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.remaining_coin)+user_coin,Toast.LENGTH_SHORT).show();
    }
    private void updateMenuTitles(int coin_count) {
        MenuItem your_coin_menu_item = this.menu.findItem(R.id.action_your_coins);
        String main_str = "Your coin ("+coin_count+")";
        your_coin_menu_item.setTitle(main_str);
        /*
        if (inBed) {
            bedMenuItem.setTitle(outOfBedMenuTitle);
        } else {
            bedMenuItem.setTitle(inBedMenuTitle);
        }
        */
    }

    private void enableMenuTitles(boolean bl ) {
        MenuItem coin_item = menu.findItem(R.id.action_settings);
        if(bl) {
            coin_item.setEnabled(true);
            coin_item.setVisible(true);
        }
        else {
            coin_item.setEnabled(false);
            coin_item.setVisible(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        updateMenuTitles(user_coin);
        enableMenuTitles(false);


        user_coin += 3;
        Toast.makeText(getApplicationContext(),"3 coins are added! "+user_coin,Toast.LENGTH_SHORT).show();
        updateUserCoin(user_coin);


        return true;
    }

/*
    public void showDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.dialog_fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dia =  builder.create();
        dia.show();

    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();




        //noinspection SimplifiableIfStatement
        if (id == R.id.action_auto_renew) {
            // update content
            // disable this

            fetchWeatherDetails(last_post_id);

            pullToRefresh.setRefreshing(true);
            //findViewById(R.id.fab).setEnabled(false);
            menu.findItem(R.id.action_auto_renew).setEnabled(false).setVisible(false);

            Snackbar.make(getWindow().getDecorView(), getResources().getString(R.string.getting_new_post), Snackbar.LENGTH_LONG).show();
            //.setAction("Action", null).show();
            return true;
        }
        if (id == R.id.action_settings) {
            // it is reared video open
            openReward();
            return true;
        }
        if (id == R.id.action_get_new_coins) {
            haha();
            return true;
        }
        if (id == R.id.action_about_app) {
            Intent i = new Intent(getApplicationContext(), AboutApp.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,getResources().getString(R.string.inter1_ads_id_real));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                //Toast.makeText(getApplicationContext(),"onRewardedAdLoaded",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                //Toast.makeText(getApplicationContext(),"onRewardedAdFailedToLoad "+errorCode,Toast.LENGTH_SHORT).show();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))
        return rewardedAd;
    }

    public void openReward(){
        //Toast.makeText(getApplicationContext(),"openReward",Toast.LENGTH_SHORT).show();
        if (rewardedAd.isLoaded()) {
            //Toast.makeText(getApplicationContext(),"openReward isLoaded",Toast.LENGTH_SHORT).show();
            Activity activityContext = MainActivity.this;//(Activity) getApplicationContext();
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                    //Toast.makeText(getApplicationContext(),"onRewardedAdOpened",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    //Toast.makeText(getApplicationContext(),"onRewardedAdClosed",Toast.LENGTH_SHORT).show();
                    //rewardedAd = createAndLoadRewardedAd();

                    enableMenuTitles(false);
                    rewardedAd = new RewardedAd(getApplicationContext(),getResources().getString(R.string.reward_ads_id_real));
                    adLoadCallback = new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            // Ad successfully loaded.
                            //Toast.makeText(getApplicationContext(),"onRewardedAdLoaded",Toast.LENGTH_SHORT).show();
                            enableMenuTitles(true);
                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Coin  (၁၀)ခုကို ဗီဒိယိုၾကည့္ျပီး ရယူလိုက္ပါ", 10000);
                            snackBar.setAction("ရယူမယ္", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Call your action method here
                                    openReward();
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
                            Log.i(log_tag,"onRewardedAdFailedToLoad on main activity "+errorCode);

                            rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
                            // Ad failed to load.
                            //Toast.makeText(getApplicationContext(),"onRewardedAdFailedToLoad "+errorCode,Toast.LENGTH_SHORT).show();
                        }
                    };
                    rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback); // .addTestDevice(getResources().getString(R.string.ads_test_device_id))

                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    //Toast.makeText(getApplicationContext(),"onUserEarnedReward",Toast.LENGTH_SHORT).show();
                    user_coin += 10;
                    Toast.makeText(getApplicationContext(),"10 coins are added! \n"+getResources().getString(R.string.remaining_coin)+user_coin,Toast.LENGTH_SHORT).show();
                    updateUserCoin(user_coin);
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    Log.i(log_tag," onRewardedAdFailedToShow on main activity "+errorCode);
                    // Ad failed to display
                    //Toast.makeText(getApplicationContext(),"onRewardedAdFailedToShow",Toast.LENGTH_SHORT).show();
                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            Log.i(log_tag," The rewarded ad wasn't loaded yet. on main activity ");
            //Toast.makeText(getApplicationContext(),"The rewarded ad wasn't loaded yet.",Toast.LENGTH_SHORT).show();
        }
    }
    public void refreshData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(false);
            }
        }, 5000);
        return;
    }

/*
    private void read_json_file(){
        File file = new File(getApplicationContext().getFilesDir(),FILE_NAME);
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String response = null;

        StringBuffer output = new StringBuffer();
        if(file.exists()){
            try{
                fileReader = new FileReader(file.getAbsoluteFile());
                bufferedReader = new BufferedReader(fileReader);
                String line;
                line = bufferedReader.readLine();
                while (line != null){
                    output.append(line + "\n");
                    line = bufferedReader.readLine();
                }
                response = output.toString();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private void write_json_file(String json_str){
        File file = new File(getApplicationContext().getFilesDir(),FILE_NAME);
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String response = null;
        if(!file.exists()){
            try{
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(json_str);
                bufferedWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
*/

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }

    public class writeJsonFile extends AsyncTask<String, Integer, String> {

        //private TextView textViewToSet;
        private String file_name;
        private String second_str;
        //private OnTaskCompleted listener;
        public writeJsonFile(String file_name,String second_str){
            //this.textViewToSet = descriptionTextView;
            this.file_name = file_name;
            this.second_str = second_str;
        }

        @Override
        protected String doInBackground(String... params) {
            File file = new File(getApplicationContext().getFilesDir(),this.file_name);
            FileReader fileReader = null;
            FileWriter fileWriter = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;
            String response = null;
            String result = "";
            try {
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(params[0]);
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate() {
            //called when the background task makes any progress
        }

        protected void onPreExecute() {
            //called before doInBackground() is started
        }

        @Override
        protected void onPostExecute(String result) {
            //this.textViewToSet.setText(result);
            //Toast.makeText(getApplicationContext(),"Write Data JSOn OK "+this.file_name,Toast.LENGTH_SHORT).show();

            if(this.file_name.equals(TITLES_FILE)){
                //Toast.makeText(getApplicationContext(),"next file write ",Toast.LENGTH_SHORT).show();
                new writeJsonFile(CONTENT_LIST_FILE,null).execute(this.second_str);
            }

            //new writeJsonFile(CONTENT_LIST_FILE).execute();

        }
    }

    public class readJsonFile extends AsyncTask<String, Integer, String> {

        //private TextView textViewToSet;
        private String file_name;
        //private String second_str;
        public readJsonFile(String file_name){
            //this.textViewToSet = descriptionTextView;
            this.file_name = file_name;
            //this.second_str = second_str;
        }

        @Override
        protected String doInBackground(String... params) {

            File file = new File(getApplicationContext().getFilesDir(),this.file_name);
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            String result = null;

            StringBuffer output = new StringBuffer();
            if(file.exists()){
                try{
                    fileReader = new FileReader(file.getAbsoluteFile());
                    bufferedReader = new BufferedReader(fileReader);
                    String line;
                    line = bufferedReader.readLine();
                    while (line != null){
                        output.append(line + "\n");
                        line = bufferedReader.readLine();
                    }
                    result = output.toString();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onProgressUpdate() {
            //called when the background task makes any progress
        }

        protected void onPreExecute() {
            //called before doInBackground() is started
            //Toast.makeText(getApplicationContext(),"Write Data JSOn OK",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            //this.textViewToSet.setText(result);
            //Toast.makeText(getApplicationContext(),"Read Data  OK for "+this.file_name,Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            //if(this.file_name == FIle)
            if(this.file_name.equals(TITLES_FILE)){
                // first parse the title to static variable and
                // we have to read another file
                //

                Gson gson = new Gson();
                //String titles_str = gson.toJson(titles);
                titles = gson.fromJson(result,String[].class);
                //titles =
                MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), titles);
                //setListAdapter(adapter);
                listView.setAdapter(adapter);
                //Toast.makeText(getApplicationContext(),"next read to Content List File",Toast.LENGTH_SHORT).show();
                new readJsonFile(CONTENT_LIST_FILE).execute();
            }
            else if(this.file_name.equals(CONTENT_LIST_FILE)){

                Gson gson = new Gson();
                //String titles_str = gson.toJson(titles);
                content_list = gson.fromJson(result,String[].class);
                /*
                Toast.makeText(getApplicationContext(),"total content "+content_list.length,Toast.LENGTH_SHORT).show();
                for(int i = 0 ; i < content_list.length; i++){

                    Toast.makeText(getApplicationContext(),content_list[i],Toast.LENGTH_SHORT).show();
                }
                */
                // we have to parse to content list and just stop async operatoin
            }
        }
    }
    private void fetchWeatherDetails(int post_id) {
        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = networking.getRetrofitClient();
        /*
        The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        */
        //WeatherAPIs weatherAPIs = retrofit.create(WeatherAPIs.class);
        api_handler bloggerAPIs = retrofit.create(api_handler.class);
        /*
        Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        */
        //Call call = weatherAPIs.getWeatherByCity("Naypyidaw", "235bef5a99d6bc6193525182c409602c");
        Call call = bloggerAPIs.getStoryByLatestId("select", post_id);
        /*
        This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        */
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                if (response.body() != null) {
                    Edu_post edu_post = (Edu_post) response.body();
                    /*
                    String resp = "Temp: " + wResponse.getMain().getTemp() + "\n " +
                            "Humidity: " + wResponse.getMain().getHumidity() + "\n" +
                            "Pressure: " + wResponse.getMain().getPressure();
                            */
                    //String resp = edu_post.getStatus();
                    /* loop through */
                    final edu_detail[] data = edu_post.getData();
                    //update_ui(data, tVtest);


                    // we have to maintain titles
                    // we have to maintain content_list
                    // and last_post_id



                    String[] old_title = titles;
                    String[] old_content_list = content_list;
                    int new_lenght = titles.length + data.length;
                    titles = new String[new_lenght];
                    content_list = new String[new_lenght];

                    for(int i=0; i<data.length; i++){
                        edu_detail  edu = data[i];
                        String content = edu.getContent();
                        String title = edu.getTitle();
                        String db_id = edu.getDb_id();

                        // add to new array
                        content_list[i] = content;
                        titles[i] = title;
                        last_post_id = parseInt(db_id);
                    }
                    for(int i = data.length; i<titles.length; i++){
                        int old_index = i - data.length;
                        titles[i] = old_title[old_index];
                    }
                    for(int i = data.length; i<content_list.length; i++){
                        int old_index = i - data.length;
                        content_list[i] = old_content_list[old_index];
                    }


                    // so we have titles and content list
                    // we need to write these data to file
                    Gson gson = new Gson();
                    String titles_str = gson.toJson(titles);
                    String content_list_str = gson.toJson(content_list);
                    new writeJsonFile(TITLES_FILE,content_list_str).execute(titles_str);

                    editor.putInt("last_post_id",last_post_id);
                    editor.apply();


                    //updateMenuTitles(last_post_id);



                    /*
                    edu_detail  edu = data[0];
                    String content = edu.getContent();

                    int count = titles.length + 1;
                    String[] num = new String[count];
                    num[0] = edu.getTitle();
                    for (int i = 1; i < num.length; i++) {
                        int old_index = i - 1;
                        String neki = titles[old_index];
                        num[i] = neki;
                    }
                    titles = num;
                    //arrAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, titles);
                    //listView.setAdapter(arrAdapter);
                    */
                    MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), titles);
                    //setListAdapter(adapter);
                    listView.setAdapter(adapter);

                    //tVtest.append(resp + "data "+ data.length);
                    //wv.loadDataWithBaseURL("",content,"text/html","UTF-8","");
                    //Toast.makeText(getApplicationContext(),resp + "data "+ data.length,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"Return result from server,",Toast.LENGTH_SHORT).show();
                    //linear_layout_signup.setVisibility(View.VISIBLE);
                    //linear_layout_signup_uploading.setVisibility(View.INVISIBLE);

                    pullToRefresh.setRefreshing(false);
                    menu.findItem(R.id.action_auto_renew).setEnabled(true).setVisible(true);
                    //findViewById(R.id.fab).setEnabled(true);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                /*
                Error callback
                */
                //Toast.makeText(getApplicationContext(),"error in netwrok request",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"အင္တာနက္ ခ်ိတ္ဆက္မႈ မရိွပါ",Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);
                menu.findItem(R.id.action_auto_renew).setEnabled(true).setVisible(true);
                //findViewById(R.id.fab).setEnabled(true);

                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "အင္တာနက္ ခ်ိတ္ဆက္မႈ မရိွပါ", 10000);
                snackBar.setActionTextColor(getResources().getColor(R.color.myColor));
                snackBar.setAction("ထပ္ၾကိဳးစားမည္", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your action method here
                        //mInterstitialAd.show();
                        fetchWeatherDetails(last_post_id);
                        pullToRefresh.setRefreshing(true);
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            }
        });
    }

    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MySimpleArrayAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.simple_listview_layout_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.text1);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.listview_image);
            Typeface tf= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/zawgyi.ttf");
            textView.setTypeface(tf);
            textView.setText(values[position]);
            imageView.setImageResource(R.mipmap.ic_launcher_final);
            return rowView;
        }
    }
    public class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton(R.string.dialog_fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    private boolean _doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (_doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }
        this._doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, getResources().getString(R.string.exit_greeting), Toast.LENGTH_SHORT).show();
        Snackbar.make(getWindow().getDecorView(), getResources().getString(R.string.exit_greeting), Snackbar.LENGTH_LONG).show();

        //WV.loadUrl("javascript:android_hello()");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                _doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void haha(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_title_out_of_coin))
                .setMessage(getResources().getString(R.string.dialog_content_out_of_coin))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                        //Toast.makeText(getApplicationContext(),"ok clicked",Toast.LENGTH_SHORT).show();
                        // we have to load / show ads and add coin to user
                        // especially in js
                        openReward();
                        user_coin += 5;
                        Toast.makeText(getApplicationContext(),"Bouns 5 coins are added to your account!",Toast.LENGTH_SHORT).show();
                    }
                })
                /*
                .setNegativeButton(getResources().getString(R.string.dialog_cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Aborting mission...");
                        Toast.makeText(getApplicationContext(),"cancel clicked",Toast.LENGTH_SHORT).show();
                    }
                })
                */
                .show();
    }
}
