package hooapps.mac.hoober1;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rides extends AppCompatActivity {

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout ll;
    private TextView tv1, tv2;


    //firebase stuff
    private DatabaseReference mPostReference;


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

        mPostReference = FirebaseDatabase.getInstance().getReference().child("rides");
        ll = (LinearLayout)findViewById(R.id.linlay);

        settings = (Button) findViewById(R.id.settingsButton);
        settings.setBackgroundColor(0xFFFFA500);
        settings.setTextColor(Color.BLACK);
        filter = (Button) findViewById(R.id.filterButton);
        filter.setBackgroundColor(0xFFFFA500);
        filter.setTextColor(Color.BLACK);
        newRide = (Button) findViewById(R.id.newButton);
        newRide.setBackgroundColor(0xFFFFA500);
        newRide.setTextColor(Color.BLACK);
        newRide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rides.this, RideCreateForm.class);
                Rides.this.startActivity(intent);
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
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride post = postSnapshot.getValue(Ride.class);

                    RelativeLayout rl = new RelativeLayout(Rides.this);
                    TextView newtv = new TextView(Rides.this);
                    newtv.setText("Date " + post.date + "    " + "Time: " + post.time + "     " +
                            "Seats:" + String.valueOf(post.seats) + "\n");
                    rl.addView(newtv);
                    ll.addView(rl);


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






}
