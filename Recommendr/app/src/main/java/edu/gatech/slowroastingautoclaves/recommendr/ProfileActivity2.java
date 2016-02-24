package edu.gatech.slowroastingautoclaves.recommendr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.slowroastingautoclaves.recommendr.databasedrivers.DBdriver;
import edu.gatech.slowroastingautoclaves.recommendr.databasedrivers.DatabaseComs;

/**
 * A profile screen that allows users to edit their profile, e.g. change their major.
 */
public class ProfileActivity2 extends AppCompatActivity {
    private String username;
    private DatabaseComs db;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        db = new DatabaseComs();
        db.connectToServer();

        Intent intent = getIntent();
        this.username = intent.getStringExtra("Username");


        TextView usernameView = (TextView) findViewById(R.id.User);
        TextView textView11 = (TextView) findViewById(R.id.textView11);

        usernameView.setText(this.username);

        u = new User(this.username, "", "");
        try {

            if (db.getProfile(this.username).equals("")) {
                Log.i("PLS: ", u.getMajor());
            } else {
                u.setMajor(db.getProfile(this.username));
            }
        } catch(Exception e) {
            Log.e("ProfileActivity", "Error in on create: " + e.getMessage() + "\n" + Log.getStackTraceString(new Exception()));
        }

        if (u != null) {
            usernameView.setText(u.getUsername());
            if (u.getMajor() != null) {
                textView11.setText(u.getMajor());
            }
        }



        Button mExitButton = (Button) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(ProfileActivity2.this, UserActivity.class);
                userIntent.putExtra("Email", ProfileActivity2.this.username);
                startActivity(userIntent);
                finish();
            }
        });
    }
}
