package app.smartify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;


public class AboutDev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_dev);
        loadAbout();
    }

    private void loadAbout() {
        Intent i = new Intent(this, Help.class);

        Uri uri = Uri.parse("market://details?id=app.smartifyPro");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        final FrameLayout flHolder = (FrameLayout) this.findViewById(R.id.aboutA);
        String url = "http://play.google.com/store/apps/details?id=" + this.getPackageName();
        flHolder.addView(
                AboutBuilder.with(this)
                        .setAppIcon(R.drawable.smartify)
                        .setAppName(R.string.app_name)
                        .setPhoto(R.drawable.profilepic)
                        .setCover(R.mipmap.profile_cover)
                        .setLinksAnimated(true)
                        .setDividerDashGap(13)
                        .setName("Abhishek U Bhat")
                        .setSubTitle("Android Developer")
                        .setLinksColumnsCount(4)
                        .setBrief(R.string.about)
                        .addGooglePlayStoreLink("6398180878838119598")
                        .addFacebookLink("abhishek.ubhat?fref=ts")
                        .addTwitterLink("Blackhat_96")
                        .addInstagramLink("abhishek.ubhat/")
                        .addGooglePlusLink("115614625101193369027")
                        .addLinkedinLink("abhishek-bhat-a49558126")
                        .addEmailLink("theloneintrovert@gmail.com")
                        .addWhatsappLink("Abhishek U Bhat", "+918197006829")
                        .addFiveStarsAction()
                        .addMoreFromMeAction("Abhishek U Bhat")
                        .setVersionAsAppTitle()
                        .addShareAction("Check out this new App ", url)
                        .setActionsColumnsCount(2)
                        .addFeedbackAction("theloneintrovert@gmail.com")
                        .addHelpAction(i)
                        .addRemoveAdsAction(goToMarket)
                        .setWrapScrollView(true)
                        .build());
    }
}