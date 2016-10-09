package hooapps.mac.hoober1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Rides extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button settings, filter, newRide;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = (Button)findViewById(R.id.settingsButton);
        settings.setBackgroundColor(0xFFFFA500);
        settings.setTextColor(Color.BLACK);
        filter = (Button)findViewById(R.id.filterButton);
        filter.setBackgroundColor(0xFFFFA500);
        filter.setTextColor(Color.BLACK);
        newRide = (Button)findViewById(R.id.newButton);
        newRide.setBackgroundColor(0xFFFFA500);
        newRide.setTextColor(Color.BLACK);
        newRide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rides.this, RideCreateForm.class);
                Rides.this.startActivity(intent);
            }

        });

    }

}
