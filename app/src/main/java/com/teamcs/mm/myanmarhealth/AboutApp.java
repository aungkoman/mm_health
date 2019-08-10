package com.teamcs.mm.myanmarhealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        //TextView tv = (TextView) findViewById(R.id.textView);
        String str = "<center><h2>က်န္းမာေရး </h2></center>\n" +
                "<p> အင္တာနက္ေပၚ က်န္မာေရး website, blog မ်ားမွ ေဆာင္းပါးမ်ားကို အလြယ္တကူ ဖတ္ရႈႏိုင္ေစရန္ ေရးသားထားျခင္း ျဖစ္ပါသည္။ </p>\n" +
                "\n" +
                "<p> ေဆာင္းပါးေတြကို ေရးသားခဲ့တဲ့ စာေရးသူ တစ္ဦးခ်င္းစီတို္င္းကို ဒီေနရာကေန credit ေပးပါတယ္။ </p>\n" +
                "\n" +
                "<p> ေနာက္ျပီး ဒီေဆာင္းပါးေတြကို အင္တာနက္ ဘေလာ့ေတြမွာ ေရးတင္ခဲ့တဲ့ blogger တစ္ဦးခ်င္းစီကိုလည္း ေက်းဇူးတင္ပါတယ္ခင္ဗ် </p>";
        //tv.setText(Html.fromHtml(str));

        Element versionElement = new Element();
        versionElement.setTitle(getResources().getString(R.string.version));

        Element adsElement = new Element();
        adsElement.setTitle("ADS");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(Html.fromHtml(str))
                .setImage(R.mipmap.ic_launcher_final_round)
                .addItem(versionElement)
                /*
                .addGroup("Connect with us")
                .addEmail("elmehdi.sakout@gmail.com")
                .addWebsite("http://medyo.github.io/")
                .addFacebook("the.medy")
                .addTwitter("medyo80")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("medyo")
                .addInstagram("medyo80")
                */
                .create();

        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.aboutAppLayout);
        parentLayout.addView(aboutPage);

        //haha();//dialog();
    }


    public void haha(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_title_out_of_coin))
                .setMessage(getResources().getString(R.string.dialog_content_out_of_coin))
                .setPositiveButton(getResources().getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                        Toast.makeText(getApplicationContext(),"ok clicked",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Aborting mission...");
                        Toast.makeText(getApplicationContext(),"cancel clicked",Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
