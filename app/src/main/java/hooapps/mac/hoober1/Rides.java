package hooapps.mac.hoober1;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Rides extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout ll;
    private TextView tv1, tv2;


    //firebase stuff
    private DatabaseReference mPostReference;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button settings, filter, newRide;


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        tv1 = (TextView)findViewById(R.id.textView1);
//        tv2 = (TextView)findViewById(R.id.textView2);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        mPostReference = FirebaseDatabase.getInstance().getReference().child("rides");
        ll = (LinearLayout)findViewById(R.id.linlay);


        settings = (Button) findViewById(R.id.settingsButton);
        settings.setBackgroundColor(0xFFFFA500);
        settings.setTextColor(Color.BLACK);
        settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });


        filter = (Button) findViewById(R.id.filterButton);
        filter.setBackgroundColor(0xFFFFA500);
        filter.setTextColor(Color.BLACK);
        filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Rides.this, "Signed out", Toast.LENGTH_SHORT).show();

                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                startActivity(new Intent(Rides.this, SignInActivity.class));


            }

        });
        newRide = (Button) findViewById(R.id.newButton);
        newRide.setBackgroundColor(0xFFFFA500);
        newRide.setTextColor(Color.BLACK);
        newRide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Rides.this);
                builder1.setMessage("What do you need?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "A ride",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(Rides.this, RideCreateForm.class);
                                intent.putExtra("isPassenger", true);
                                Rides.this.startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "Passengers",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(Rides.this, RideCreateForm.class);
                                intent.putExtra("isPassenger", false);
                                Rides.this.startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }

        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Ride ride = dataSnapshot.getValue(Ride.class);

                //tv1.setText(ride.place);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("BADBADBADBAD", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);


        // My top posts by number of stars
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ll.removeAllViews();
                for (final DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride post = postSnapshot.getValue(Ride.class);

                    TextView originTV = new TextView(Rides.this);
                    originTV.setText(post.origin + "\n" + post.date);

                    TextView destTV = new TextView(Rides.this);
                    destTV.setText(post.destination);

                    TextView arrowTV = new TextView(Rides.this);
                    arrowTV.setText("------->\n" + post.time);

                    TextView dateTV = new TextView(Rides.this);
                    dateTV.setText(post.date);

                    TextView timeTV = new TextView(Rides.this);
                    timeTV.setText(post.time);

                    TextView seatsTV = new TextView(Rides.this);
                    seatsTV.setText(String.valueOf(post.seats + "\n"));

                    ImageView isPassTV = new ImageView(Rides.this);
                    Drawable d;
                    if(post.isPassenger) {
                        d = getResources().getDrawable(R.drawable.manclip);
                    } else {
                        d = getResources().getDrawable(R.drawable.carclip);
                    }
                    isPassTV.setImageDrawable(d);
                    LinearLayout.LayoutParams layoutSizeParams = new LinearLayout.LayoutParams(200, 175);
                    isPassTV.setLayoutParams(layoutSizeParams);


                    final Button deleteButton = (Button) getLayoutInflater().inflate(R.layout.custombutton, null);
                    //deleteButton.setText("Hello world");
                    //final Button deleteButton = new Button(Rides.this);
                    deleteButton.setText("Delete");
                    deleteButton.setLayoutParams(new LinearLayout.LayoutParams(200, 120));


                    deleteButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //deleteButton.setText(postSnapshot.getRef().toString());
                            String s = postSnapshot.getRef().toString();
                            //s = s.substring(s.indexOf("/-"),s.length());
                            deletePost(s);
                        }

                    });

//                            + " Destination " + post.destination +
//                            " Date " + post.date + " Time: " + post.time +
//                            " Seats:" + String.valueOf(post.seats) + "\n");

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    param.gravity = Gravity.RIGHT;
                    originTV.setTextSize(15);
                    destTV.setTextSize(15);
                    arrowTV.setTextSize(15);
                    dateTV.setTextSize(15);
                    timeTV.setTextSize(15);
                    seatsTV.setTextSize(15);


                    destTV.setLayoutParams(param);
                    originTV.setLayoutParams(param);
                    arrowTV.setLayoutParams(param);
                    dateTV.setLayoutParams(param);
                    timeTV.setLayoutParams(param);
                    seatsTV.setLayoutParams(param);

                    LinearLayout linloOriginDest = new LinearLayout(Rides.this);
                    linloOriginDest.setWeightSum(5);
                    linloOriginDest.setOrientation(LinearLayout.HORIZONTAL);
                    linloOriginDest.addView(isPassTV);
                    linloOriginDest.addView(originTV);
                    linloOriginDest.addView(arrowTV);
                    linloOriginDest.addView(destTV);
                    linloOriginDest.addView(deleteButton);
                    ll.addView(linloOriginDest);



                    LinearLayout linloDateTime = new LinearLayout(Rides.this);
                    linloDateTime.setWeightSum(4);
                    linloDateTime.setOrientation(LinearLayout.HORIZONTAL);
                    linloDateTime.addView(dateTV);
                    linloDateTime.addView(timeTV);
//                    linloDateTime.addView(isPassTV);
//                    linloDateTime.addView(deleteButton);
//                    ll.addView(linloDateTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
     public void deletePost(final String f) {
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rides");
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getRef().toString().equals(f)) {
                        Log.w("TAG", "loadPost:onCancelled");
                        postSnapshot.getRef().removeValue();
                    } else{
                        Log.w("TAG", f +" " + postSnapshot.getRef().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Bad", "onConnectionFailed:" + connectionResult);
    }
}