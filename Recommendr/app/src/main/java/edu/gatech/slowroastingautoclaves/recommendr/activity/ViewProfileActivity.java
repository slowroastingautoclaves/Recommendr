package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * A profile screen that allows users to edit their profile, e.g. change their major.
 */
public class ViewProfileActivity extends AppCompatActivity {
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
//                textView11.append(u.getUsername());
            }
            if (u.getDescription() != null) {
                textView12.setText(u.getDescription());
                textView12.clearComposingText();
                textView12.append(u.getDescription());
            }
        }


        //Exit button.
        Button mExitButton = (Button) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(ViewProfileActivity.this, UserActivity.class);
                userIntent.putExtra("Email", ViewProfileActivity.this.email);
                startActivity(userIntent);
                finish();
            }
        });
    }
}
