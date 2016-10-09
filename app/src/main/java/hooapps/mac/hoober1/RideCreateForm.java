package hooapps.mac.hoober1;

import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;

import java.text.SimpleDateFormat;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.vision.text.Text;


public class RideCreateForm extends AppCompatActivity {
    EditText dateLeaving;
    EditText timeLeaving;
    EditText placeHeading;
    Button submit;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< thissuper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_create_form);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PLACES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.schools_list);
        textView.setAdapter(adapter);
        dateLeaving = (EditText) findViewById(R.id.dateLeavingInput);
        timeLeaving = (EditText) findViewById(R.id.timeLeavingInput);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setBackgroundColor(0xFFFFA500);
        //set dateleaving listener
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
                         timeLeaving.setText( selectedHour + ":" + selectedMinute);
                     }
                 }, hour, minute, true);//Yes 24 hour time
                 mTimePicker.setTitle("Select Time");
                 mTimePicker.show();

             }

        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        //TODO rename shcools_list
        placeHeading = (EditText)findViewById(R.id.schools_list);
        placeHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getGeo();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    int PLACE_PICKER_REQUEST = 1;

    public void getGeo() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                placeHeading.setText(place.getName());
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


    private static final String[] PLACES = new String[]{
            "Virginia Tech", "James Madison", "Charlottesville"
    };

}


