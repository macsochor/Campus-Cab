package hooapps.mac.hoober1;

import android.app.DatePickerDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import java.text.SimpleDateFormat;
import java.util.Map;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RideCreateForm extends AppCompatActivity {
    //views
    EditText dateLeaving,timeLeaving, destination, numSeats, origin;
    Button submit;

    ArrayList<String> s;
    CharSequence[] cs;
    //firebase stuff
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< thissuper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_create_form);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dateLeaving = (EditText) findViewById(R.id.dateLeavingInput);
        timeLeaving = (EditText) findViewById(R.id.timeLeavingInput);
        numSeats = (EditText) findViewById(R.id.seatNumInput);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setBackgroundColor(0xFFFFA500);
        //set dateleaving listener
        destination = (EditText)findViewById(R.id.destInput);
        origin = (EditText)findViewById(R.id.originInput);


        dateLeaving.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(RideCreateForm.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //set timeleaving listener
        timeLeaving.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                 Calendar mcurrentTime = Calendar.getInstance();
                 int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                 int minute = mcurrentTime.get(Calendar.MINUTE);
                 TimePickerDialog mTimePicker;

                 mTimePicker = new TimePickerDialog(RideCreateForm.this, new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                         String ampm = "PM";
                         if(selectedHour < 12) ampm = "AM";
                         timeLeaving.setText( selectedHour + ":" + selectedMinute + " " + ampm);
                     }
                 }, hour, minute, false);//Yes 24 hour time
                 mTimePicker.setTitle("Select Time");

                 mTimePicker.show();

             }

        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final Intent intent = getIntent();


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                writeNewRide(mFirebaseUser.getDisplayName(), origin.getText().toString(), destination.getText().toString(),
                        dateLeaving.getText().toString(),timeLeaving.getText().toString(),
                        Integer.parseInt(numSeats.getText().toString()), intent.getBooleanExtra("isPassenger", true));

                finish();
            }

        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

//        destination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    getGeo();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        s = new ArrayList<String>();
        cs = s.toArray(new CharSequence[s.size()]);

    }

    final int PLACE_PICKER_REQUEST = 1;

    public void getGeo() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                destination.setText(place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    Calendar myCalendar = Calendar.getInstance();



    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateLeaving.setText(sdf.format(myCalendar.getTime()));
    }

    private void writeNewRide(String userId, String origin, String destination, String date, String time, int seats, boolean isPassenger) {
        // Create new ride at /user-rides/$userid/$rideid and at
        // /rides/$rideid simultaneously
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String key = mDatabase.child("posts").push().getKey();
        Ride ride = new Ride(origin, destination, date, time, seats, isPassenger,
                String.valueOf(c.getTimeInMillis()));

        Map<String, Object> rideValues = ride.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/rides/" + key, rideValues);
        //childUpdates.put("/user-rides/" + userId + "/" + key, rideValues);

        mDatabase.updateChildren(childUpdates);
    }
}