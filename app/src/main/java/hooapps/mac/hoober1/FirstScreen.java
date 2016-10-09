package hooapps.mac.hoober1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FirstScreen extends AppCompatActivity {
    Button signup, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //gets rid of title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_first_screen);
        //initialize buttons
        signup = (Button)findViewById(R.id.signUpButton);
        signup.setBackgroundColor(0xFFFFA500);
        signup.setTextColor(Color.BLACK);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, HamburgerTest.class);
                FirstScreen.this.startActivity(intent);
            }

        });
        login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, Rides.class);
                FirstScreen.this.startActivity(intent);
            }

        });
        login.setBackgroundColor(0xFF0099CC);
    }
}
