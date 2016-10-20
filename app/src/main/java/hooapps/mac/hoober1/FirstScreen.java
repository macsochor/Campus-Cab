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
    Button signup, login, db;
    EditText et;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //gets rid of time bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_first_screen);

        et = (EditText)findViewById(R.id.editText);

        //initialize buttons
        signup = (Button) findViewById(R.id.signUpButton);
        signup.setBackgroundColor(0xFF00FF00);
        signup.setTextColor(Color.BLACK);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, HamburgerTest.class);
                FirstScreen.this.startActivity(intent);
            }

        });
        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, Rides.class);
                FirstScreen.this.startActivity(intent);
            }

        });
        login.setBackgroundColor(0xFF0099CC);

        db = (Button) findViewById(R.id.db);
        db.setBackgroundColor(0xFFFF00FF);
        db.setTextColor(Color.BLACK);
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the databse

                myRef.setValue(et.getText().toString());
                writeNewPost(et.getText().toString(), "a", "b", "c");
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Read from the database
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    @IgnoreExtraProperties
    public class Post {

        public String uid;
        public String author;
        public String title;
        public String body;
        public int starCount = 0;
        public Map<String, Boolean> stars = new HashMap<>();

        public Post() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Post(String uid, String author, String title, String body) {
            this.uid = uid;
            this.author = author;
            this.title = title;
            this.body = body;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", uid);
            result.put("date", author);
            result.put("time", title);
            result.put("body", body);
            result.put("starCount", starCount);
            result.put("stars", stars);

            return result;
        }

    }

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


}
