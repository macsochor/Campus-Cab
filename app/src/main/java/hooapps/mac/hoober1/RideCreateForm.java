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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RideCreateForm extends AppCompatActivity {
    //views
    EditText dateLeaving, destination, numSeats, origin;
    Button submit;
    boolean isPass;
    String min, hr, ampm, datestr, originStr, destStr = "";
    int numberOfSeats = -1;
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
        //timeLeaving = (EditText) findViewById(R.id.timeLeavingInput);
        numSeats = (EditText) findViewById(R.id.seatNumInput);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setBackgroundColor(0xFFFFA500);
        //set dateleaving listener
        destination = (EditText)findViewById(R.id.destInput);
        origin = (EditText)findViewById(R.id.originInput);

        isPass = getIntent().getBooleanExtra("isPassenger", true);
        dateLeaving.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(RideCreateForm.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });

        numSeats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RideCreateForm.this);
                final String reqoff = isPass ? "Requesting" : "Offering";
                String msg = isPass ? "How many seats are you requesting?" : "How many seats are you offering?";
                builder.setTitle(msg);
                builder.setCancelable(true);
                builder.setItems(new CharSequence[]
                                {"1", "2", "3", "4", "5", "6"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                numSeats.setText(reqoff + " " + String.valueOf(which+1) + " seat");
                                numberOfSeats=which+1;
                                if(which!=0)numSeats.setText(numSeats.getText()+"s");
                                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                            }
                        });

                builder.create().show();
            }

        });





        //set timeleaving listener
//        //timeLeaving.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                 Calendar mcurrentTime = Calendar.getInstance();
//                 int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                 int minute = mcurrentTime.get(Calendar.MINUTE);
//                 TimePickerDialog mTimePicker;
//
//                 mTimePicker = new TimePickerDialog(RideCreateForm.this, new TimePickerDialog.OnTimeSetListener() {
//                     @Override
//                     public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                         String ampm = "PM";
//                         if(selectedHour < 12) ampm = "AM";
//                         if(selectedHour == 0) selectedHour=12;
//                         if(selectedHour > 12) selectedHour-=12;
//                         timeLeaving.setText("Leaving at "+ selectedHour + ":" + selectedMinute + " " + ampm);
//                     }
//                 }, hour, minute, false);//Yes 24 hour time
//                 mTimePicker.setTitle("Select Departure Time");
//                 mTimePicker.show();
//
//             }
//
//        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final Intent intent = getIntent();


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(origin.getText().toString().length() < 2){
                    Toast.makeText(RideCreateForm.this, "You must have a departure location",
                            Toast.LENGTH_LONG).show();
                } else if(destination.getText().toString().length() < 2) {
                    Toast.makeText(RideCreateForm.this, "You must have a destination",
                            Toast.LENGTH_LONG).show();
                } else if(min == "") {
                    Toast.makeText(RideCreateForm.this, "You must set a time and date",
                            Toast.LENGTH_LONG).show();
                } else if(numberOfSeats == -1) {
                    Toast.makeText(RideCreateForm.this, "You must select a number of seats",
                            Toast.LENGTH_LONG).show();
                } else {
                    writeNewRide(mFirebaseUser.getDisplayName(),
                            origin.getText().toString(), destination.getText().toString(),

                            datestr, min + ":" + hr + " " + ampm, numberOfSeats, isPass);
                    finish();
                }
            }

        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

//        s = new ArrayList<String>();
//        cs = s.toArray(new CharSequence[s.size()]);

    }
//
//    final int PLACE_PICKER_REQUEST = 1;
//
//    public void getGeo() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 || requestCode == 2) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format("Place: %s", place.getName());
//                destination.setText(place.getName());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }


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
        datestr = sdf.format(myCalendar.getTime());
        dateLeaving.setText("Leaving on " + datestr);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(RideCreateForm.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ampm = "PM";
                if(selectedHour < 12) ampm = "AM";
                if(selectedHour == 0) selectedHour=12;
                if(selectedHour > 12) selectedHour-=12;
                min = (selectedMinute > 9) ? String.valueOf(selectedMinute): "0"+String.valueOf(selectedMinute);
                hr = String.valueOf(selectedHour);
                dateLeaving.setText(dateLeaving.getText() + " at "+ selectedHour + ":" + min + " " + ampm);
            }
        }, hour, minute, false);//Np 24 hour time
        mTimePicker.setTitle("Select Departure Time");
        mTimePicker.show();

    }

    private void writeNewRide(String userId, String origin, String destination, String date, String time, int seats, boolean isPassenger) {
        // Create new ride at /user-rides/$userid/$rideid and at
        // /rides/$rideid simultaneously
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = df.format(c.getTime());
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