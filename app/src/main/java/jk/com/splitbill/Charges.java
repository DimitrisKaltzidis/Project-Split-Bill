package jk.com.splitbill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Arrays;


public class Charges extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener {

    public static boolean isALL = false;
    private LinearLayout llCharges;
    private AddFloatingActionButton fabAddCharges;
    private FloatingActionButton fabOverView;
    private double amount = 0;
    private ArrayList<Charge> charges = new ArrayList<Charge>();
    private String chargeName = "";
    private TextView tvHeader;
    private int exitCounter = 0;
    private String currency;
    public ShowcaseView sv;
    public Activity thisActivity = this;

    @SuppressLint("LongLogTag")
    public static void updateUserCharges(ArrayList<User> affectedUsers, Charge chargeToBeAdded) {
        ArrayList<User> allUsers = Methods.getUsers();

        for (int i = 0; i < affectedUsers.size(); i++) {
            if (allUsers.contains(affectedUsers.get(i))) {
                affectedUsers.get(i).addCharge(chargeToBeAdded);
            }


        }
        Methods.setUsers(allUsers);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        exitCounter = 0;
        getSupportActionBar().hide();


        currency = "$";
        currency = Methods.loadPrefsString("CURRENCY", "$", getApplicationContext());
        tvHeader = (TextView) findViewById(R.id.tvCharges);
        llCharges = (LinearLayout) findViewById(R.id.llAddCharges);
        fabAddCharges = (AddFloatingActionButton) findViewById(R.id.fab_semi_transparent);
        fabOverView = (FloatingActionButton) findViewById(R.id.fabOverView);

        fabOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.hide();
                new ScaleOutAnimation(v).setDuration(Methods.animationDuration).setListener(new AnimationListener() {


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        Intent openOverView = new Intent(Charges.this, Overview.class);
                        // openOverView.putExtra()
                        startActivity(new Intent(Charges.this, Overview.class));
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    }

                }).animate();
                fabAddCharges.setVisibility(View.VISIBLE);
                sv.hide();

            }
        });

        try {
            charges = Methods.getCharges();
            llCharges.removeAllViews();
            addUserCardView(llCharges, charges);
        } catch (Exception e) {

        }

        if (Methods.times > 0)
            try {
                Methods.times++;
                double totalCharges = 0;
                for (int i = 0; i < charges.size(); i++) {
                    totalCharges += charges.get(i).getAmount();
                }
                tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
            } catch (Exception e) {

            }


        fabAddCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScaleOutAnimation(v).setDuration(120).setListener(new AnimationListener() {////custom set


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        displayAlertDialog();
                        fabAddCharges.setVisibility(View.VISIBLE);

                    }

                }).animate();
            }
        });

        Target viewTarget = new ViewTarget(R.id.fab_semi_transparent, thisActivity);
        sv = new ShowcaseView.Builder(thisActivity, true)
                .setTarget(viewTarget)
                .setStyle(R.style.CustomShowcaseTheme4)
                .setContentTitle(R.string.add_charge_header_scv)
                .setContentText(R.string.add_charge_body_scv)
                .singleShot(3)
                .hideOnTouchOutside().build();


        sv.hideButton();
        sv.setBlocksTouches(true);


    }

    private void addUserCardView(LinearLayout llAddPeople, ArrayList<Charge> users) {


        for (int i = 0; i < users.size(); i++) {
            //// Initialize CardView and textView
            CardView cvUser = new CardView(this);
            TextView tvUser = new TextView(this);

            //// Setting elevation and radius for the corners
            cvUser.setCardElevation((float) 15.0);
            cvUser.setRadius((float) 15.0);
            cvUser.setId(i);


            //// Setting Text to textView and defining parameters
            //  tvUser.setText(users.get(i).getAmount() + "\t  $ \t\t\t\t\t" + users.get(i).getDescription());
            tvUser.setText("  " + users.get(i).getDescription() + " \t\t\t\t\t\t " + currency + ": " + String.format("%.2f",users.get(i).getAmount()));
            tvUser.setGravity(Gravity.CENTER);
            tvUser.setTextSize(16);


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

            cvUser.setOnClickListener(this);
            cvUser.setOnLongClickListener(this);

            //// Setting Parameters
            cvUser.setLayoutParams(cardLayoutParams);

            //// Adding CardView to LinearLayout
            llAddPeople.addView(cvUser);
        }
    }

    private void displayAlertDialog() {
        sv.hide();
        AlertDialogWrapper.Builder alert = new AlertDialogWrapper.Builder(this);


        alert.setTitle(getString(R.string.alert_dialog_charge_title));
        alert.setMessage(" ");

        final LinearLayout llEdit = new LinearLayout(this);
        llEdit.setOrientation(LinearLayout.VERTICAL);


        final TextView tvName = new TextView(this);
        tvName.setText(getString(R.string.charge_description));
        tvName.setTextColor(getResources().getColor(R.color.red_soft));
        tvName.setGravity(Gravity.CENTER);
        llEdit.addView(tvName);


        final EditText etName = new EditText(this);
        etName.setGravity(Gravity.CENTER);
        llEdit.addView(etName);


        final TextView tvAmount = new TextView(this);
        tvAmount.setText(getString(R.string.charge_amount));
        tvAmount.setTextColor(getResources().getColor(R.color.red_soft));
        tvAmount.setGravity(Gravity.CENTER);
        llEdit.addView(tvAmount);

        final EditText etAmount = new EditText(this);
        etAmount.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmount.setGravity(Gravity.CENTER);
        etAmount.setText("0");
        llEdit.addView(etAmount);

        alert.setView(llEdit);


        alert.setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                amount = 0;

                try {
                    amount = Double.parseDouble(etAmount.getText().toString());
                } catch (Exception e) {

                }

                try {
                    chargeName = etName.getText().toString();
                } catch (Exception e) {

                }


                if (chargeName.isEmpty()) {

                    chargeName = getString(R.string.default_charge_name) + " " + (charges.size() + 1);
                }
               // launchAddItems(amount, chargeName);
                launchAddItemsBeta(amount,chargeName);


            }
        });

        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //// Canceled.
                fabAddCharges.setVisibility(View.VISIBLE);
            }
        });

        alert.show();
    }

    public void launchAddItems(final double amount, final String chargEName) {

        final ArrayList<Integer> mSelectedUSERS = new ArrayList<Integer>();//// Users selected to slpit amount


        final ArrayList<User> users = Methods.getUsers();//// all users

        final String[] items = new String[users.size()];//// position of users selected to split bill between GOOD


        for (int i = 0; i < users.size(); i++) {//// GOOD

            items[i] = users.get(i).getUserName();
        }


        AlertDialogWrapper.Builder ab = new AlertDialogWrapper.Builder(this);
        ab.setTitle(getString(R.string.split_between))
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            mSelectedUSERS.add(which);
                        } else if (mSelectedUSERS.contains(which)) {
                            mSelectedUSERS.remove(Integer.valueOf(which));
                        }

                    }

                }).setNeutralButton(getString(R.string.neutral_all_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedUSERS.clear();
                for (int i = 0; i < users.size(); i++) {
                    mSelectedUSERS.add(i);
                }
                Charge runningCharge = new Charge(chargEName, mSelectedUSERS, amount);
                charges.add(runningCharge);

                if (!mSelectedUSERS.isEmpty()) {
                    updateUserCharges(runningCharge.getSplitBetweenUsersAsObjects(), runningCharge);
                }

                runningCharge.getSplitBetweenUsersAsObjects();

                Methods.setCharges(charges);
                double totalCharges = 0;
                for (int i = 0; i < charges.size(); i++) {
                    totalCharges += charges.get(i).getAmount();
                }
                tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
                charges.get(0).getSplitBetweenUsersAsObjects();

                try {
                    llCharges.removeAllViews();
                } catch (Exception e) {

                }
                addUserCardView(llCharges, charges);
                fabAddCharges.setVisibility(View.VISIBLE);

            }
        })

                .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                                if (!mSelectedUSERS.isEmpty()) {
                                    Charge runningCharge = new Charge(chargEName, mSelectedUSERS, amount);
                                    charges.add(runningCharge);


                                    if (!mSelectedUSERS.isEmpty()) {
                                        updateUserCharges(runningCharge.getSplitBetweenUsersAsObjects(), runningCharge);
                                    }
                                    runningCharge.getSplitBetweenUsersAsObjects();

                                    Methods.setCharges(charges);
                                    double totalCharges = 0;
                                    for (int i = 0; i < charges.size(); i++) {
                                        totalCharges += charges.get(i).getAmount();
                                    }
                                    tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
                                    charges.get(0).getSplitBetweenUsersAsObjects();

                                    try {
                                        llCharges.removeAllViews();
                                    } catch (Exception e) {

                                    }
                                    addUserCardView(llCharges, charges);
                                } else {
                                    CardView cvToast = new CardView(getApplicationContext());

                                    cvToast.setCardElevation((float) 15.0);
                                    cvToast.setRadius((float) 15.0);
                                    cvToast.setContentPadding(15,15,15,15);
                                    cvToast.setCardBackgroundColor(getResources().getColor(R.color.grey_secondary));


                                    TextView tvToast = new TextView(getApplicationContext());
                                    tvToast.setText(getString(R.string.no_charge_added));
                                    tvToast.setGravity(Gravity.CENTER);
                                    tvToast.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvToast.setTextColor(getResources().getColor(R.color.white));

                                    cvToast.addView(tvToast);
                                    Toast t = new Toast(getApplicationContext());
                                    t.setView(cvToast);
                                    t.setDuration(Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,150);
                                    t.show();

                                }
                                fabAddCharges.setVisibility(View.VISIBLE);

                                Target viewTarget = new ViewTarget(R.id.fabOverView, thisActivity);
                                sv = new ShowcaseView.Builder(thisActivity, true)
                                        .setTarget(viewTarget)
                                        .setStyle(R.style.CustomShowcaseTheme4)
                                        .setContentTitle(R.string.go_to_overview_activity_header_scv)
                                        .setContentText(R.string.go_to_overview_activity_body_scv)
                                        .singleShot(4)
                                        .hideOnTouchOutside().build();


                                sv.hideButton();
                                sv.setBlocksTouches(true);

                            }
                        }

                ).

                setNegativeButton(getString(R.string.cancel),

                        new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                fabAddCharges.setVisibility(View.VISIBLE);
                            }
                        }

                );

        ab.create();
        ;


        ab.show();
    }


    public void launchAddItemsBeta(final double amount, final String chargEName) {




        final ArrayList<User> users = Methods.getUsers();//// all users

        final String[] items = new String[users.size()];//// position of users selected to split bill between GOOD


        for (int i = 0; i < users.size(); i++) {//// GOOD

            items[i] = users.get(i).getUserName();
        }

        new MaterialDialog.Builder(this)
                .title(getString(R.string.split_between))
                .items(items)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMulti() {
                    @Override
                    public void onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        System.out.println("You called me onSelection");
                        for(int i = 0 ; i <which.length;i++){
                            System.out.println(" >"+which[i]);
                        }

                        ArrayList<Integer> tempArrayList =  new ArrayList<Integer>(Arrays.asList(which));

                        Methods.setmSelectedUserz(tempArrayList);



                        System.out.println("You called me Positive");
                        System.out.println("Total number of users is "+Methods.getmSelectedUserz().size());

                        ArrayList<Integer> mSelectedUSERS = Methods.getmSelectedUserz();
                        for(int i = 0 ; i <mSelectedUSERS.size();i++){
                            System.out.println(" <>"+mSelectedUSERS.get(i));
                        }
                        if (!mSelectedUSERS.isEmpty()) {
                            Charge runningCharge = new Charge(chargEName, mSelectedUSERS, amount);
                            charges.add(runningCharge);


                            if (!mSelectedUSERS.isEmpty()) {
                                updateUserCharges(runningCharge.getSplitBetweenUsersAsObjects(), runningCharge);
                            }
                            runningCharge.getSplitBetweenUsersAsObjects();

                            Methods.setCharges(charges);
                            double totalCharges = 0;
                            for (int i = 0; i < charges.size(); i++) {
                                totalCharges += charges.get(i).getAmount();
                            }
                            tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
                            charges.get(0).getSplitBetweenUsersAsObjects();

                            try {
                                llCharges.removeAllViews();
                            } catch (Exception e) {

                            }
                            addUserCardView(llCharges, charges);
                        } else {
                            CardView cvToast = new CardView(getApplicationContext());

                            cvToast.setCardElevation((float) 15.0);
                            cvToast.setRadius((float) 15.0);
                            cvToast.setContentPadding(15,15,15,15);
                            cvToast.setCardBackgroundColor(getResources().getColor(R.color.grey_secondary));


                            TextView tvToast = new TextView(getApplicationContext());
                            tvToast.setText(getString(R.string.no_charge_added));
                            tvToast.setGravity(Gravity.CENTER);
                            tvToast.setTypeface(Typeface.DEFAULT_BOLD);
                            tvToast.setTextColor(getResources().getColor(R.color.white));

                            cvToast.addView(tvToast);
                            Toast t = new Toast(getApplicationContext());
                            t.setView(cvToast);
                            t.setDuration(Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,150);
                            t.show();

                        }
                        fabAddCharges.setVisibility(View.VISIBLE);

                        Target viewTarget = new ViewTarget(R.id.fabOverView, thisActivity);
                        sv = new ShowcaseView.Builder(thisActivity, true)
                                .setTarget(viewTarget)
                                .setStyle(R.style.CustomShowcaseTheme4)
                                .setContentTitle(R.string.go_to_overview_activity_header_scv)
                                .setContentText(R.string.go_to_overview_activity_body_scv)
                                .singleShot(4)
                                .hideOnTouchOutside().build();


                        sv.hideButton();
                        sv.setBlocksTouches(true);
                    }
                })
                .negativeText(getString(R.string.cancel))
                .positiveText(getString(R.string.done))
                .neutralText(getString(R.string.neutral_all_button))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        fabAddCharges.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        ArrayList<Integer> mSelectedUSERS = Methods.getmSelectedUserz();
                        mSelectedUSERS.clear();
                        for (int i = 0; i < users.size(); i++) {
                            mSelectedUSERS.add(i);
                        }
                        Charge runningCharge = new Charge(chargEName, mSelectedUSERS, amount);
                        charges.add(runningCharge);

                        if (!mSelectedUSERS.isEmpty()) {
                            updateUserCharges(runningCharge.getSplitBetweenUsersAsObjects(), runningCharge);
                        }

                        runningCharge.getSplitBetweenUsersAsObjects();

                        Methods.setCharges(charges);
                        double totalCharges = 0;
                        for (int i = 0; i < charges.size(); i++) {
                            totalCharges += charges.get(i).getAmount();
                        }
                        tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
                        charges.get(0).getSplitBetweenUsersAsObjects();

                        try {
                            llCharges.removeAllViews();
                        } catch (Exception e) {

                        }
                        addUserCardView(llCharges, charges);
                        fabAddCharges.setVisibility(View.VISIBLE);
                    }
                })
                .show();

        /*AlertDialogWrapper.Builder ab = new AlertDialogWrapper.Builder(this);
        ab.setTitle(getString(R.string.split_between))
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            mSelectedUSERS.add(which);
                        } else if (mSelectedUSERS.contains(which)) {
                            mSelectedUSERS.remove(Integer.valueOf(which));
                        }

                    }

                }).setNeutralButton(getString(R.string.neutral_all_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedUSERS.clear();
                for (int i = 0; i < users.size(); i++) {
                    mSelectedUSERS.add(i);
                }
                Charge runningCharge = new Charge(chargEName, mSelectedUSERS, amount);
                charges.add(runningCharge);

                if (!mSelectedUSERS.isEmpty()) {
                    updateUserCharges(runningCharge.getSplitBetweenUsersAsObjects(), runningCharge);
                }

                runningCharge.getSplitBetweenUsersAsObjects();

                Methods.setCharges(charges);
                double totalCharges = 0;
                for (int i = 0; i < charges.size(); i++) {
                    totalCharges += charges.get(i).getAmount();
                }
                tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
                charges.get(0).getSplitBetweenUsersAsObjects();

                try {
                    llCharges.removeAllViews();
                } catch (Exception e) {

                }
                addUserCardView(llCharges, charges);
                fabAddCharges.setVisibility(View.VISIBLE);

            }
        })

                .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                                if (!mSelectedUSERS.isEmpty()) {
                                    Charge runningCharge = new Charge(chargEName, mSelectedUSERS, amount);
                                    charges.add(runningCharge);


                                    if (!mSelectedUSERS.isEmpty()) {
                                        updateUserCharges(runningCharge.getSplitBetweenUsersAsObjects(), runningCharge);
                                    }
                                    runningCharge.getSplitBetweenUsersAsObjects();

                                    Methods.setCharges(charges);
                                    double totalCharges = 0;
                                    for (int i = 0; i < charges.size(); i++) {
                                        totalCharges += charges.get(i).getAmount();
                                    }
                                    tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
                                    charges.get(0).getSplitBetweenUsersAsObjects();

                                    try {
                                        llCharges.removeAllViews();
                                    } catch (Exception e) {

                                    }
                                    addUserCardView(llCharges, charges);
                                } else {
                                    CardView cvToast = new CardView(getApplicationContext());

                                    cvToast.setCardElevation((float) 15.0);
                                    cvToast.setRadius((float) 15.0);
                                    cvToast.setContentPadding(15,15,15,15);
                                    cvToast.setCardBackgroundColor(getResources().getColor(R.color.grey_secondary));


                                    TextView tvToast = new TextView(getApplicationContext());
                                    tvToast.setText(getString(R.string.no_charge_added));
                                    tvToast.setGravity(Gravity.CENTER);
                                    tvToast.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvToast.setTextColor(getResources().getColor(R.color.white));

                                    cvToast.addView(tvToast);
                                    Toast t = new Toast(getApplicationContext());
                                    t.setView(cvToast);
                                    t.setDuration(Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,150);
                                    t.show();

                                }
                                fabAddCharges.setVisibility(View.VISIBLE);

                                Target viewTarget = new ViewTarget(R.id.fabOverView, thisActivity);
                                sv = new ShowcaseView.Builder(thisActivity, true)
                                        .setTarget(viewTarget)
                                        .setStyle(R.style.CustomShowcaseTheme4)
                                        .setContentTitle(R.string.go_to_overview_activity_header_scv)
                                        .setContentText(R.string.go_to_overview_activity_body_scv)
                                        .singleShot(4)
                                        .hideOnTouchOutside().build();


                                sv.hideButton();
                                sv.setBlocksTouches(true);

                            }
                        }

                ).

                setNegativeButton(getString(R.string.cancel),

                        new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                fabAddCharges.setVisibility(View.VISIBLE);
                            }
                        }

                );

        ab.create();
        ;


        ab.show();*/
    }
    @Override
    public void onClick(View v) {

        ArrayList<User> users = charges.get(v.getId()).getSplitBetweenUsersAsObjects();
        String[] items = new String[users.size()];

        for (int i = 0; i < users.size(); i++) {
            items[i] = users.get(i).getUserName() + "\t\t Debt : " + String.format("%.2f",users.get(i).getAmount());
        }

        if(items.length==0){
            items = new String[1];
            items[0] = "No users selected";
        }


        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(this);
        builder.setTitle(getString(R.string.divided_between));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onLongClick(final View v) {


        new ScaleOutAnimation(/*findViewById(R.id.llStores)*/v).setDuration(250).setListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {

                try {
                    llCharges.removeAllViews();
                } catch (Exception e) {

                }
                //// Set clickState
                v.setClickable(false);
                ArrayList<User> temUsers = Methods.getUsers();

                for (int i = 0; i < temUsers.size(); i++) {
                    temUsers.get(i).removeCharge(charges.get(v.getId()));
                }

                Methods.setUsers(temUsers);

                charges.remove(v.getId());
                Methods.setCharges(charges);
                charges = Methods.getCharges();

                try {
                    addUserCardView(llCharges, charges);
                } catch (Exception e) {

                }

                double totalCharges = 0;
                for (int i = 0; i < charges.size(); i++) {
                    totalCharges += charges.get(i).getAmount();
                }
                tvHeader.setText(getString(R.string.total_charges) + " " + String.format("%.2f",totalCharges));
            }
        }).animate();

        return false;
    }

    @Override
    public void onBackPressed() {


    }
}

