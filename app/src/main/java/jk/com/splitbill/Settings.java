package jk.com.splitbill;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.getbase.floatingactionbutton.FloatingActionButton;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Settings extends ActionBarActivity {

    private WaveAnimation waveDrawable, waveDrawable1, waveDrawable2;
    private FloatingActionButton Down;

    //// Display alert dialog for the first time
    private void ShowAlertDialogFirstTime() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.play_new)
                .setTitleText(getString(R.string.hi))
                .setContentText(getString(R.string.message_to_user))
                .setConfirmText(" " + getString(R.string.store) + " ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        sweetAlertDialog.dismiss();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    }
                })
                .setCancelText(getString(R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //// Do Nothing
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();

        waveDrawable = new WaveAnimation(Color.parseColor("#c75c5c"), 650, 500);
        waveDrawable1 = new WaveAnimation(Color.parseColor("#c75c5c"), 650, 500);
        waveDrawable2 = new WaveAnimation(Color.parseColor("#c75c5c"), 650, 500);

        TableRow trCurrency = (TableRow) findViewById(R.id.trSettingsCurrency);
        TableRow trPlaystore = (TableRow) findViewById(R.id.trSettingsPlayStore);
        TableRow trRandomColors = (TableRow) findViewById(R.id.trSettingsAutoCall);

        LinearLayout llRandomColors = (LinearLayout) findViewById(R.id.llFirstCardView_Colors);

        trPlaystore.setBackgroundDrawable(waveDrawable1);

        //trRandomColors.setBackgroundDrawable(waveDrawable2);
        llRandomColors.setBackgroundDrawable(waveDrawable2);

        trPlaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // waveDrawable1.startAnimation();
                ShowAlertDialogFirstTime();
            }
        });

        final ToggleButton tbCurrency = (ToggleButton) findViewById(R.id.tbCurrency);
        final Switch sRandomColors = (android.widget.Switch) findViewById(R.id.switchColors);


        Down = (FloatingActionButton) findViewById(R.id.fabDown);

        if (Methods.loadPrefsString("RANDOM_COLORS", "YES", getApplicationContext()).equals("YES")) {
            sRandomColors.setChecked(true);
        }

        trCurrency.setBackgroundDrawable(waveDrawable);
        if (Methods.loadPrefsString("CURRENCY", "$", getApplicationContext()).equals("€")) {
            tbCurrency.setChecked(true);
        }

       /* trRandomColors*/
        llRandomColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sRandomColors.performClick();
            }
        });


        sRandomColors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                waveDrawable2.startAnimation();
                if (isChecked) {
                    Methods.savePrefsString("RANDOM_COLORS", "YES", getApplicationContext());
                } else {
                    Methods.savePrefsString("RANDOM_COLORS", "NO", getApplicationContext());
                }
            }
        });


        trCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbCurrency.performClick();
            }
        });

        tbCurrency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                waveDrawable.startAnimation();
                if (isChecked) {
                    Methods.savePrefsString("CURRENCY", "€", getApplicationContext());
                } else {
                    Methods.savePrefsString("CURRENCY", "$", getApplicationContext());
                }
            }
        });

        Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScaleOutAnimation(v).setDuration(Methods.animationDuration).setListener(new AnimationListener() {


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        Intent openOverView = new Intent(Settings.this, Overview.class);
                        openOverView.putExtra("PREVIOUS", "SETTINGS");
                        startActivity(openOverView);
                        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);//// to swsto
                    }

                }).animate();
            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Settings.this, Overview.class));
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);//// to swsto
    }
}
