package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.activity.UserActivity;
import edu.gatech.slowroastingautoclaves.recommendr.model.RInfo;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.DatabaseComs;

/**
 * A profile screen that allows users to edit their profile, e.g. change their major.
 */
public class ViewProfileActivity extends AppCompatActivity {
    private String username;
    private RInfo db;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        db = new DatabaseComs();
        db.start();

        Intent intent = getIntent();
        this.username = intent.getStringExtra("Username");


        TextView usernameView = (TextView) findViewById(R.id.User);
        TextView textView11 = (TextView) findViewById(R.id.textView11);

        usernameView.setText(this.username);

        u = new User(this.username, "", "", null);


            if (db.getProfile(this.username).equals("")) {
                Log.i("PLS: ", u.getMajor() + "mess " + db.getProfile(this.username).equals(""));
            } else {
                u.setMajor(db.getProfile(this.username));
            }


        if (u != null) {
            usernameView.setText(u.getUsername());
            if (u.getMajor() != null) {
                textView11.setText(u.getMajor());
            }
        }


        //Exit button.
        Button mExitButton = (Button) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(ViewProfileActivity.this, UserActivity.class);
                userIntent.putExtra("Username", ViewProfileActivity.this.username);
                startActivity(userIntent);
                finish();
            }
        });
    }
}
