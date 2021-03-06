package app.smartify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.rm.rmswitch.RMSwitch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RuntimePermission {
    private final List<Adapter> List = new ArrayList<>();
    private ListAdapter mAdapter;
    private SharedPreferences.Editor editor;
    private static final int REQUEST_PERMISSION = 10;
    InterstitialAd mInterstitialAd;
    // private final static int PERM_REQUEST_CODE_DRAW_OVERLAYS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!getPackageName().equals("app.smartify")) {
            Toast.makeText(this, "Please respect developer's effort and use original app", Toast.LENGTH_SHORT).show();
            finish();
        }

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7383233719473844~6183091215");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.

            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Log.e("ads", "Closed");
            }
        });

        if (!getPackageName().equals("app.smartify")) {
            Toast.makeText(this, "Please respect developer's effort and use original app", Toast.LENGTH_SHORT).show();
            finish();
        }

        mInterstitialAd.show();
        final SharedPreferences settings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        boolean isAppInstalled = sp.getBoolean("isAppInstalled", false);

        if (!isAppInstalled) {
            editor.putBoolean("isAppInstalled", true);
            editor.apply();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AutoStart(MainActivity.this);
                }
            }, 35000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInputDialog(MainActivity.this);
                }
            }, 25000);


            new Handler().postDelayed(new Runnable() {
                @SuppressLint("BatteryLife")
                @Override
                public void run() {
                    Intent intent = new Intent();
                    String packageName = MainActivity.this.getPackageName();
                    PowerManager pm = (PowerManager) MainActivity.this.getSystemService(Context.POWER_SERVICE);
                    if (Build.VERSION.SDK_INT >= 23) {
                        assert pm != null;
                        if (pm.isIgnoringBatteryOptimizations(packageName))
                            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        else {
                            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                            intent.setData(Uri.parse("package:" + packageName));
                        }
                        MainActivity.this.startActivity(intent);
                    }
                }
            }, 15000);
        }


        if (Build.VERSION.SDK_INT >= 23) {
           /* if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERM_REQUEST_CODE_DRAW_OVERLAYS);
            }*/

            //Ask for permission
            int ReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            int ReadPhoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (ReadContactPermission != PackageManager.PERMISSION_GRANTED && ReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                requestAppPermissions(new String[]{
                                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE},
                        R.string.msg, REQUEST_PERMISSION);
            } else if (ReadContactPermission != PackageManager.PERMISSION_GRANTED) {
                requestAppPermissions(new String[]{
                                Manifest.permission.READ_CONTACTS},
                        R.string.msg, REQUEST_PERMISSION);
            } else if (ReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                requestAppPermissions(new String[]{
                                Manifest.permission.READ_PHONE_STATE},
                        R.string.msg, REQUEST_PERMISSION);
            }
        }


        boolean wifi = settings.getBoolean("wifi", false);
        boolean ring = settings.getBoolean("ring", false);

        RMSwitch S_wifi, S_ring;

        S_wifi = findViewById(R.id.s_wifi);
        S_wifi.setChecked(wifi);

        S_wifi.setChecked(false);
        S_wifi.setEnabled(true);
        S_wifi.setForceAspectRatio(false);
        S_wifi.setSwitchBkgCheckedColor(Color.YELLOW);
        S_wifi.setSwitchBkgNotCheckedColor(Color.LTGRAY);
        S_wifi.setSwitchToggleCheckedColor(Color.YELLOW);
        S_wifi.setSwitchToggleNotCheckedColor(Color.LTGRAY);
        S_wifi.setSwitchToggleCheckedDrawableRes(R.drawable.wifi);
        S_wifi.setSwitchToggleNotCheckedDrawableRes(R.drawable.wifioff);

        S_wifi.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                editor.putBoolean("wifi", isChecked);
                editor.apply();
            }
        });

        S_ring = findViewById(R.id.s_ring);
        S_ring.setChecked(ring);
        S_ring.setChecked(false);
        S_ring.setEnabled(true);
        S_ring.setForceAspectRatio(false);
        S_ring.setSwitchBkgCheckedColor(Color.YELLOW);
        S_ring.setSwitchBkgNotCheckedColor(Color.LTGRAY);
        S_ring.setSwitchToggleCheckedColor(Color.YELLOW);
        S_ring.setSwitchToggleNotCheckedColor(Color.LTGRAY);
        S_ring.setSwitchToggleCheckedDrawableRes(R.drawable.startring);
        S_ring.setSwitchToggleNotCheckedDrawableRes(R.drawable.stopring);
        S_ring.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                editor.putBoolean("ring", isChecked);
                editor.apply();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ListAdapter(List);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Dialog webViewDialog;
                webViewDialog = new Dialog(view.getContext());
                webViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                webViewDialog.setCancelable(true);

                switch (position) {
                    case 0:
                        Intent i = new Intent(getBaseContext(), Help.class);
                        mInterstitialAd.show();
                        startActivity(i);
                        break;
                    case 1:
                        webViewDialog.setContentView(R.layout.upgrade);
                        webViewDialog.show();
                        Button btnUpg = webViewDialog.findViewById(R.id.btn_upg);
                        btnUpg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse("market://details?id=app.smartifyPro");
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=app.smartifyPro")));
                                }
                                finish();
                            }
                        });
                        break;
                    case 2:
                        new Extras().share_app(view.getContext());
                        break;
                    case 3:
                        new Extras().rate(view.getContext());
                        break;
                    case 4:
                        mInterstitialAd.show();
                        Intent about = new Intent(getBaseContext(), AboutDev.class);
                        startActivity(about);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }

        }));
        prepareRecyclerView();
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERM_REQUEST_CODE_DRAW_OVERLAYS) {
            if (android.os.Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
                if (!Settings.canDrawOverlays(this)) {
                } else {
                    finish();
                    startActivity(getIntent());
                }
            }
        }
    }*/


    @Override
    public void onPermissionsGranted(int requestCode) {
        Snackbar SB = Snackbar.make(findViewById(android.R.id.content), "Permission Granted", Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) SB.getView();
        SB.setActionTextColor(Color.BLACK);
        group.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.skyblue));
        SB.show();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        mInterstitialAd.show();
        finish();

    }

    @Override
    protected void onDestroy() {
        mInterstitialAd.show();
        super.onDestroy();

    }

    private void showHelp() {
        final Dialog webViewDialog;
        webViewDialog = new Dialog(MainActivity.this);
        webViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        webViewDialog.setCancelable(true);
        webViewDialog.setContentView(R.layout.activity_splash);
        webViewDialog.show();
        TextView btnClose = webViewDialog.findViewById(R.id.ok);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cng:
                showInputDialog(this);
                break;
            case R.id.help:
                AutoStart(this);
                break;
            case R.id.privacy_policy:
                Intent privacyPolicy = new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(privacyPolicy);
                break;
        }
        return true;
    }


    void AutoStart(Context context) {
        String manufacturer = "xiaomi";
        Intent intent = new Intent();
        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            Toast.makeText(this, "In order for app to work properly, Add Smartify to auto Start manager.", Toast.LENGTH_LONG).show();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            context.startActivity(intent);
        } else
            showHelp();
    }

    private void prepareRecyclerView() {
        Adapter list = new Adapter("Why Smartify", "How to use this app", 0);
        List.add(list);

        list = new Adapter("Upgrade", "Upgrade to pro version", R.drawable.pro);
        List.add(list);

        list = new Adapter("Share", "Share this app", 0);
        List.add(list);

        list = new Adapter("Rate this app", "Help us improve", 0);
        List.add(list);

        list = new Adapter("About", "About the developer", 0);
        List.add(list);

        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    private void showInputDialog(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        final EditText editText = promptView.findViewById(R.id.edittext);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editor.putString("number", editText.getText().toString());
                        editor.apply();
                        Snackbar SB = Snackbar.make(findViewById(android.R.id.content), "Number Changed Successfully", Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) SB.getView();
                        group.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.skyblue));
                        SB.setActionTextColor(Color.BLACK);
                        SB.show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialogBuilder.setView(promptView);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
