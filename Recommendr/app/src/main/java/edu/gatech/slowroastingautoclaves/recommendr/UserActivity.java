package edu.gatech.slowroastingautoclaves.recommendr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Screen that allows user to access user functions, e.g. editing profile.
 */
public class UserActivity extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get buttons and text fields.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");

        Button mPlaceholderDone = (Button) findViewById(R.id.placeholder_done);
        mPlaceholderDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(userIntent);
                finish();
            }
        });

        Button mEditProfile = (Button) findViewById(R.id.edit_profile);
        mEditProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send email to ProfileActivity and start ProfileActivity.
                Intent profileIntent = new Intent(UserActivity.this, ProfileActivity.class);
                profileIntent.putExtra("Email", UserActivity.this.email);
                startActivity(profileIntent);
                finish();
            }
        });
    }
}
