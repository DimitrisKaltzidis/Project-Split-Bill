package jk.com.splitbill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Random;


public class PersonNumber extends ActionBarActivity implements View.OnLongClickListener, View.OnClickListener {

    private final ArrayList<User> users = new ArrayList<User>();
    public LinearLayout llAddPeople;
    private AddFloatingActionButton addPerson;
    private FloatingActionButton fabNext;
    private String currency;
    private Activity thisActivity = this;
    public ShowcaseView sv;
    private static int createRandomColor() {

        Random rand = new Random();

        // generate the random integers for r, g and b value
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        int randomColor = Color.rgb(r, g, b);

        return randomColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_number);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //// Hide the actionbar
        getSupportActionBar().hide();


        //// Set Currency
        currency = "$";
        currency = Methods.loadPrefsString("CURRENCY", "$", getApplicationContext());

        if (Methods.loadPrefsString("FIRST_TIME_ADD_PEOPLE", "YES", getApplicationContext()).equals("YES")) {
            //  Methods.savePrefsString("FIRST_TIME_ADD_PEOPLE", "NO", getApplicationContext());

            Target viewTarget = new ViewTarget(R.id.semi_transparent, thisActivity);

           /* ShowcaseView sv;*/
            sv = new ShowcaseView.Builder(thisActivity, true)
                    .setTarget(viewTarget)
                    .setStyle(R.style.CustomShowcaseTheme4)
                    .setContentTitle(R.string.add_people_header_scv)
                    .setContentText(R.string.add_people_body_scv)
                    .singleShot(1)
                    .hideOnTouchOutside().build();


            sv.hideButton();
         //   sv.setBlocksTouches(true);



        }
        //// Getting Reference to LinearLayout and Add Button
        addPerson = (AddFloatingActionButton) findViewById(R.id.semi_transparent);
        llAddPeople = (LinearLayout) findViewById(R.id.llAddPeople);
        fabNext = (FloatingActionButton) findViewById(R.id.fabNext);

        fabNext.setVisibility(View.GONE);

        //// Add on clickListener
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //User newUser = new User();
                new ScaleOutAnimation(v).setDuration(Methods.animationDuration).setListener(new AnimationListener() {


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        displayAlertDialog();
                        addPerson.setVisibility(View.VISIBLE);
                    }

                }).animate();
                addPerson.setVisibility(View.VISIBLE);
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.hide();
                new ScaleOutAnimation(v).setDuration(Methods.animationDuration).setListener(new AnimationListener() {


                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {

                        startActivity(new Intent(PersonNumber.this, Charges.class));
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }

                }).animate();
                addPerson.setVisibility(View.VISIBLE);
            }
        });

    }

    private void displayAlertDialog() {
         sv.hide();
        AlertDialogWrapper.Builder alert = new AlertDialogWrapper.Builder(this);


        alert.setTitle(getString(R.string.alert_dialog_title));
        alert.setMessage(" ");

        final LinearLayout llEdit = new LinearLayout(this);
        llEdit.setOrientation(LinearLayout.VERTICAL);


        final TextView tvName = new TextView(this);
        tvName.setText(getString(R.string.name_title));
        tvName.setTextColor(getResources().getColor(R.color.red_soft));
        tvName.setGravity(Gravity.CENTER);
        llEdit.addView(tvName);


        final EditText etName = new EditText(this);
        etName.setGravity(Gravity.CENTER);
        llEdit.addView(etName);


        final TextView tvAmount = new TextView(this);
        tvAmount.setText(getString(R.string.starting_debt));
        tvAmount.setTextColor(getResources().getColor(R.color.red_soft));
        tvAmount.setGravity(Gravity.CENTER);
        llEdit.addView(tvAmount);

        final EditText etAmount = new EditText(this);
        etAmount.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmount.setGravity(Gravity.CENTER);
        etAmount.setText("0");
        llEdit.addView(etAmount);

        alert.setView(llEdit);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addPerson.setVisibility(View.VISIBLE);
                double amount = 0;

                try {
                    amount = Double.parseDouble(etAmount.getText().toString());
                } catch (Exception e) {

                }
                String username = etName.getText().toString();
                if (username.isEmpty()) {

                    username = getString(R.string.default_user_name) + " " + (users.size() + 1);
                }
                users.add(new User(username, null, createRandomColor(), amount));

                Methods.setUsers(users);

                //// Clear LinearLayout
                llAddPeople.removeAllViews();

                //// Add new Users
                addUserCardView(llAddPeople, users);

                //// Allow user to add charges
                fabNext.setVisibility(View.VISIBLE);


                Target viewTarget = new ViewTarget(R.id.fabNext, thisActivity);
               sv =  new ShowcaseView.Builder(thisActivity, true)
                        .setTarget(viewTarget)
                        .setStyle(R.style.CustomShowcaseTheme4)
                        .setContentTitle(R.string.go_to_add_charge_activity_header_scv)
                        .setContentText(R.string.go_to_add_charge_activity_body_scv)
                        .singleShot(2)
                        .hideOnTouchOutside()
                        .build();
                sv.hideButton();


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //// Canceled.

                addPerson.setVisibility(View.VISIBLE);
            }
        });

        alert.show();

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
            tvUser.setText("  " + users.get(i).getUserName() + "\t\t\t\t\t\t " + currency + ": " + users.get(i).getAmount());
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

            //// Adding LongClickListener
            cvUser.setOnLongClickListener(this);
            cvUser.setOnClickListener(this);

            //// Setting Parameters
            cvUser.setLayoutParams(cardLayoutParams);

            //// Adding CardView to LinearLayout
            llAddPeople.addView(cvUser);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onLongClick(final View v) {
        Log.e("LONG CLICK", "LONG CLICK");
        v.setClickable(false);
        new ScaleOutAnimation(v).setDuration(250).setListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {

                try {
                    users.remove(v.getId());
                } catch (Exception e) {
                    Log.e("ERROR", "CANT REMOVE USER FROM LIST");
                }

                try {
                    llAddPeople.removeAllViews();
                } catch (Exception e) {
                    Log.e("ERROR", "CANT REMOVE VIEWS FROM LLADDPEOPLE");
                }

                try {
                    addUserCardView(llAddPeople, users);
                    Methods.setUsers(users);
                } catch (Exception e) {
                    Log.e("ERROR", "CANT ADD VIEWS TO LLADDPEOPLE");
                }

                //// Remove NEXT Button
                if (users.isEmpty()) {
                    fabNext.setVisibility(View.GONE);
                }


            }
        }).animate();


        return false;
    }

    @Override
    public void onClick(View v) {
        Log.e(" CLICK", " CLICK");
    }


}
