package jk.com.splitbill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;


public class Overview extends ActionBarActivity implements View.OnClickListener {

    private FloatingActionButton fabBack, fabInfo;
    private ArrayList<User> users;
    private LinearLayout llPeopleOverView;
    private String currency;
    private ShowcaseView sv;
    private Activity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getSupportActionBar().hide();

        currency = "$";
        currency = Methods.loadPrefsString("CURRENCY", "$", getApplicationContext());
        fabBack = (FloatingActionButton) findViewById(R.id.fabBack);
        fabInfo = (FloatingActionButton) findViewById(R.id.fabInfo);
        llPeopleOverView = (LinearLayout) findViewById(R.id.llPeopleOverView);

        llPeopleOverView.removeAllViews();

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sv.hide();
                } catch (Exception e) {

                }
                new ScaleOutAnimation(v).setDuration(Methods.animationDuration).setListener(new AnimationListener() {


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        startActivity(new Intent(Overview.this, Charges.class));

                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    }

                }).animate();
            }
        });

        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sv.hide();
                } catch (Exception e) {

                }
                new ScaleOutAnimation(v).setDuration(Methods.animationDuration).setListener(new AnimationListener() {


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        //  ShowAlertDialogFirstTime();
                        startActivity(new Intent(Overview.this, Settings.class));
                        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                    }

                }).animate();

            }
        });
        users = Methods.getUsers();

        addUserCardView(llPeopleOverView, users);


        Target viewTarget = new ViewTarget(R.id.tvOverView, thisActivity);
        sv = new ShowcaseView.Builder(thisActivity, true)
                .setTarget(viewTarget)
                .setStyle(R.style.CustomShowcaseTheme4)
                .setContentTitle(R.string.see_details_header_scv)
                .setContentText(R.string.see_details_body_scv)
                .singleShot(5)
                .hideOnTouchOutside()
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        scv.setVisibility(View.GONE);
                        Target viewTarget = new ViewTarget(R.id.fabInfo, thisActivity);
                        sv = new ShowcaseView.Builder(thisActivity, true)
                                .setTarget(viewTarget)
                                .setStyle(R.style.CustomShowcaseTheme4)
                                .setContentTitle(R.string.go_to_add_charge_activity_from_overview_header_scv)
                                .setContentText(R.string.go_to_add_charge_activity_from_overview_body_scv)
                                .singleShot(7)
                                .hideOnTouchOutside()
                                .setShowcaseEventListener(new OnShowcaseEventListener() {

                                    @Override
                                    public void onShowcaseViewShow(final ShowcaseView scv) {
                                    }

                                    @Override
                                    public void onShowcaseViewHide(final ShowcaseView scv) {
                                        scv.setVisibility(View.GONE);
                                        Target viewTarget = new ViewTarget(R.id.fabBack, thisActivity);
                                        sv = new ShowcaseView.Builder(thisActivity, true)
                                                .setTarget(viewTarget)
                                                .setStyle(R.style.CustomShowcaseTheme4)
                                                .setContentTitle(R.string.go_to_add_charge_activity_from_overview_header_scv)
                                                .setContentText(R.string.go_to_add_charge_activity_from_overview_body_scv)
                                                .singleShot(8)
                                                .hideOnTouchOutside().build();


                                        sv.hideButton();
                                        sv.setBlocksTouches(true);

                                    }

                                    @Override
                                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                                    }

                                }).build();


                        sv.hideButton();
                        sv.setBlocksTouches(true);

                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                })

                .build();


        sv.hideButton();
        sv.setBlocksTouches(true);
    }

    private void addUserCardView(LinearLayout llAddPeople, ArrayList<User> users) {

        for (int i = 0; i < users.size(); i++) {
            //// Initialize CardView and textView
            CardView cvUser = new CardView(this);
            TextView tvUser = new TextView(this);

            //// Setting elevation and radius for the corners
            cvUser.setCardElevation((float) 15.0);
            cvUser.setRadius((float) 15.0);
            cvUser.setId(i);


            //// Setting Text to textView and defining parameters
            // tvUser.setText(users.get(i).getAmount() + "\t  $ \t\t\t\t\t" + users.get(i).getUserName());
            tvUser.setText("  " + users.get(i).getUserName() + "\t\t\t\t\t\t " + currency + ": " + String.format("%.2f", users.get(i).getAmount()));
            tvUser.setGravity(Gravity.CENTER);
            tvUser.setTextSize(16);
            if (Methods.loadPrefsString("RANDOM_COLORS", "YES", getApplicationContext()).equals("YES")) {
                tvUser.setTextColor(users.get(i).getColor());
            } else {
                tvUser.setTextColor(getResources().getColor(R.color.grey_from_buttons));
            }

            //// Setting textView height
            tvUser.setHeight(Methods.getButtonHeight(getResources()));

            //// Adding textView to CardView
            cvUser.addView(tvUser);


            //// Initializing Parameters
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //// Defining Parameters
            cardLayoutParams.setMargins(12, 18, 12, 18);
            //cvUser.setCardBackgroundColor(users.get(i).getColor());

            //// Adding OnClickListener
            cvUser.setOnClickListener(this);

            //// Setting Parameters
            cvUser.setLayoutParams(cardLayoutParams);

            //// Adding CardView to LinearLayout
            llAddPeople.addView(cvUser);
        }
    }


    @Override
    public void onClick(View v) {

        User runningUser = Methods.getUsers().get(v.getId());


        String[] items = new String[runningUser.getUserCharges().size()];

        ArrayList<Charge> chargesToDisplay = runningUser.getUserCharges();

        for (int i = 0; i < chargesToDisplay.size(); i++) {
            items[i] = runningUser.getUserCharges().get(i).getDescription() + "      " + " $: " + String.format("%.2f", runningUser.getUserCharges().get(i).getAmountPerUser()) /*runningUser.getUserCharges().get(i).getAmountPerUser()*/;
            System.out.println(chargesToDisplay.get(i).getDescription());
        }
        if (items.length == 0) {
            items = new String[1];
            items[0] = getString(R.string.no_charges);
        }

        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(this);
        builder.setTitle(getString(R.string.alert_dialog_user_charges) + " " + runningUser.getUserName());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Overview.this, Charges.class));

        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
