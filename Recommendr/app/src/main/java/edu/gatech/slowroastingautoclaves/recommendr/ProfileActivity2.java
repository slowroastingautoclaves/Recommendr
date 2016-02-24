package edu.gatech.slowroastingautoclaves.recommendr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A profile screen that allows users to edit their profile, e.g. change their major.
 */
public class ProfileActivity2 extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");

        User u = UserList.getInstance().findUserByEmail(this.email);
        TextView usernameView = (TextView) findViewById(R.id.User);
        TextView textView11 = (TextView) findViewById(R.id.textView11);
        TextView textView12 = (TextView) findViewById(R.id.textView12);

        if (u != null) {
            usernameView.setText(u.getUsername());
            usernameView.clearComposingText();
            usernameView.append(u.getUsername());
            if (u.getMajor() != null) {
                textView11.setText(u.getMajor());
                textView11.clearComposingText();
                textView11.append(u.getUsername());
            }
            if (u.getDescription() != null) {
                textView12.setText(u.getDescription());
                textView12.clearComposingText();
                textView12.append(u.getDescription());
            }
        }



        Button mExitButton = (Button) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(ProfileActivity2.this, UserActivity.class);
                userIntent.putExtra("Email", ProfileActivity2.this.email);
                startActivity(userIntent);
                finish();
            }
        });
    }
}
