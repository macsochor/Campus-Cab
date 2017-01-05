package hooapps.mac.hoober1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Rides extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout ll;
    private EditText leavingfromtv, goingtotv;
    private TextView tv1, tv2;
    private int height, width;
    static int id = 1;


    //firebase stuff
    private DatabaseReference mPostReference;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    //Recycler stuff
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button settings, filter, newRide;


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        //authentication
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Ride> a = new ArrayList<Ride>();
        Ride r = new Ride("origin", "dest", "today", "tomorrow", "mac", 3, false, "123456");
        a.add(r);

        mAdapter = new MyAdapter(a);
        mRecyclerView.setAdapter(mAdapter);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //ad money
        MobileAds.initialize(this, "ca-app-pub-8060765523787614/1076515284");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("FF1783FC6F9F734348C61A6F1F5B3C8B")  // An example device ID
                .build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics metrics = Rides.this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        //24a4021a6a848a3c


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
                //signOut();
                LayoutInflater inflater = LayoutInflater.from(Rides.this);
                final View filterLL = inflater.inflate(R.layout.filter, null);

                TextView textview = (TextView) filterLL.findViewById(R.id.textmsg);
                final EditText dateStart = (EditText) filterLL.findViewById(R.id.filterDateStartTV);


                dateStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();

                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "MM/dd/yy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                final String datestr = sdf.format(cal.getTime());
                                dateStart.setText(datestr);
                                TimePickerDialog mTimePicker = new TimePickerDialog(Rides.this, new TimePickerDialog.OnTimeSetListener() {
                                 @Override
                                 public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                     String ampm = "PM";
                                     if(selectedHour < 12) ampm = "AM";
                                     if(selectedHour == 0) selectedHour=12;
                                     if(selectedHour > 12) selectedHour-=12;
                                     dateStart.setText(dateStart.getText() + " @ "+ selectedHour + ":" + selectedMinute + " " + ampm);
                                 }
                             }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);//Yes 24 hour time
                             mTimePicker.setTitle("Select Departure Time");
                             mTimePicker.show();
                            }
                        };

                        new DatePickerDialog(Rides.this, date, cal
                                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });

                leavingfromtv = (EditText)findViewById(R.id.leavingFromTV);
                goingtotv = (EditText)findViewById(R.id.goingToTV);

                final EditText dateEnd = (EditText)filterLL.findViewById(R.id.filterDateEndTV);
                dateEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();

                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "MM/dd/yy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                final String datestr = sdf.format(cal.getTime());
                                dateEnd.setText(datestr);
                                TimePickerDialog mTimePicker = new TimePickerDialog(Rides.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        String ampm = "PM";
                                        if(selectedHour < 12) ampm = "AM";
                                        if(selectedHour == 0) selectedHour=12;
                                        if(selectedHour > 12) selectedHour-=12;
                                        dateEnd.setText(dateEnd.getText() + " @ "+ selectedHour + ":" + selectedMinute + " " + ampm);
                                    }
                                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);//Yes 24 hour time
                                mTimePicker.setTitle("Select Departure Time");
                                mTimePicker.show();
                            }
                        };

                        new DatePickerDialog(Rides.this, date, cal
                                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                //textview.setText(R.string.large_text);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Rides.this);
                alertDialog.setPositiveButton(
                        "Apply",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alertDialog.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                alertDialog.setTitle("Filter Rides");
                alertDialog.setView(filterLL);
                AlertDialog alert = alertDialog.create();

                alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alert.show();
            }
        });
        newRide = (Button) findViewById(R.id.newButton);
        newRide.setBackgroundColor(0xFFFFA500);
        newRide.setTextColor(Color.BLACK);
        newRide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Rides.this);
                builder1.setTitle("Ride Request Form");
                builder1.setMessage("I am...");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "A passenger",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(Rides.this, RideCreateForm.class);
                                intent.putExtra("isPassenger", true);
                                Rides.this.startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "A driver",
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

        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ll.removeAllViews();
                for (final DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride ride = postSnapshot.getValue(Ride.class);
                    View v = getLayoutInflater().inflate(R.layout.ride_obj_layout, null);
                    ImageView iv = (ImageView) v.findViewById(R.id.iconIV);
                    Drawable d;
                    if(ride.isPassenger) {
                        d = getResources().getDrawable(R.drawable.manclip);
                    } else {
                        d = getResources().getDrawable(R.drawable.carclip);
                    }
                    iv.setImageDrawable(d);
                    LinearLayout linlay = (LinearLayout)findViewById(R.id.linlay);
                    linlay.addView(v);
                    TextView originTV = (TextView)v.findViewById(R.id.originTV);
                    originTV.setText(ride.origin);
                    TextView destinationTV = (TextView)v.findViewById(R.id.destinationTV);
                    destinationTV.setText(ride.destination);
                    if(ride.destination.length() + ride.origin.length() >= 35){
                        originTV.setTextSize(13);
                        destinationTV.setTextSize(13);
                        TextView toTV = (TextView)v.findViewById(R.id.toTV);
                        toTV.setTextSize(13);

                    }
                    TextView timeTV = (TextView)v.findViewById(R.id.timeTV);
                    timeTV.setText(ride.time);
                    TextView dateTV = (TextView)v.findViewById(R.id.timeTV);

                    TextView ageTV = (TextView)v.findViewById(R.id.ageTV);
                                        Calendar c = Calendar.getInstance();

                    long diff = c.getTimeInMillis() - Long.valueOf(ride.timeCreated);
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String age = "";
                    if(days > 0) age += days + "d";
                    else if(hours > 0) age += hours + "h";
                    else if(minutes > 0) age += minutes + "m";
                    else age = "just now";
                    ageTV.setText(age);
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
                     } else {
                         Log.w("TAG", f + " " + postSnapshot.getRef().toString());
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

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Rides.this, "Signed out", Toast.LENGTH_SHORT).show();

        if(mGoogleApiClient.isConnected())Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(Rides.this, SignInActivity.class));

    }
}