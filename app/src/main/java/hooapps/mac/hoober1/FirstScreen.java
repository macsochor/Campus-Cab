package hooapps.mac.hoober1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirstScreen extends AppCompatActivity {
    Button signup, login, googsignin;
    GoogleApiClient mGoogleApiClient;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private DatabaseReference mDatabase;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //gets rid of time bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_first_screen);


        //initialize buttons
//        signup = (Button) findViewById(R.id.signUpButton);
//        signup.setBackgroundColor(0xFF00FF00);
//        signup.setTextColor(Color.BLACK);
//        signup.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FirstScreen.this, HamburgerTest.class);
//                FirstScreen.this.startActivity(intent);
//            }
//
//        });
//        login = (Button) findViewById(R.id.loginButton);
//        login.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FirstScreen.this, Rides.class);
//                FirstScreen.this.startActivity(intent);
//            }
//
//        });
//        login.setBackgroundColor(0xFF0099CC);
//
//        googsignin = (Button) findViewById(R.id.gSignIn);
//        googsignin.setBackgroundColor(0xFF0CC0FF);
//        googsignin.setTextColor(Color.BLACK);
//        googsignin.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FirstScreen.this, SignInActivity.class);
//                FirstScreen.this.startActivity(intent);
//            }
//
//        });
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
