package edu.gatech.slowroastingautoclaves.recommendr;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;

import edu.gatech.slowroastingautoclaves.recommendr.databasedrivers.DatabaseComs;

/**
 * A profile screen that allows users to edit their profile, e.g. change their major.
 */
public class ProfileActivity extends AppCompatActivity {
    private String username;
    private DatabaseComs db;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.db = new DatabaseComs();
        db.connectToServer();
        //Log.d("ProfileActivity", db.getProfile(this.username).equals("") + "");

        Intent intent = getIntent();
        this.username = intent.getStringExtra("Username");

        u = new User(this.username, "", "");
        try {

            if (db.getProfile(this.username).equals("")) {
                Log.i("PLS: ", u.getMajor());
            } else {
                u.setMajor(db.getProfile(this.username));
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("ProfileActivity", "Error in on create: " + e.getMessage() + "\n" + Log.getStackTraceString(new Exception()));
        }

        TextView usernameView = (TextView) findViewById(R.id.User);
        TextView majorView = (TextView) findViewById(R.id.major);

        if (u != null) {
            usernameView.setText(u.getUsername());
            if (u.getMajor() != null) {
                majorView.setText(u.getMajor());
            }
        }

        Button mSaveButton = (Button) findViewById(R.id.save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        Button mExitButton = (Button) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(ProfileActivity.this, UserActivity.class);
                userIntent.putExtra("Username", ProfileActivity.this.username);
                startActivity(userIntent);
                finish();
            }
        });
    }

    /**
     * Attempts to update profile with information entered by user.
     */
    private void editProfile() {
        TextView majorView = (TextView) findViewById(R.id.major);
        u.setMajor(majorView.getText().toString());
        boolean profileExists = false;
        try {
            profileExists = this.db.updateProfile(u.getUsername(), u.getMajor());
        } catch(Exception e) {
            Log.e("ProfileActivity", "Error in edit profile: " + e.getMessage() + "\n" + Log.getStackTraceString(new Exception()));
        }
        //check if user already has profile, if not then make one in database
        if (!profileExists) {
            this.db.createProfile(u.getUsername(), u.getMajor());
        }

        boolean success = this.db.updateProfile(u.getUsername(), u.getMajor());
        //if profile is updated then display update message, else display error message
        if (success) {
            Context context = getApplicationContext();
            CharSequence text = "Profile updated.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Something went wrong, profile not updated.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
