package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.DatabaseComs;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * A profile screen that allows users to edit their profile, e.g. change their major.
 */
public class ProfileActivity extends AppCompatActivity {
    private String username;
    private DatabaseComs presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new DatabaseComs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("Username");

        User u = presenter.getUser(this.username);

        TextView usernameView = (TextView) findViewById(R.id.User);
        TextView majorView = (TextView) findViewById(R.id.major);


        if (u != null) {
            usernameView.setText(u.getUsername());
            if (u.getMajor() != null) {
                majorView.setText(u.getMajor());
            }

        }

        //Get buttons.
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
        User u = presenter.getUser(this.username);
        u.setMajor(majorView.getText().toString());
        presenter.updateProfile(u.getUsername(), u.getMajor());

        //Display alert to user that profile has been updated successfully.
        Context context = getApplicationContext();
        CharSequence text = "Profile updated.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
