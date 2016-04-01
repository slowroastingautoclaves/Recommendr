package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.File;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * Screen that allows user to access user functions, e.g. editing profile.
 */
public class UserActivity extends AppCompatActivity {
    private String email;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get buttons and text fields.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");

        for (User u: UserList.getInstance().getUsers()) {
            if (u.getEmail().equals(email)) {
                user = u;
            }
        }

        RatingList.getInstance().loadRatings(new File(this.getFilesDir(), RatingList.RATINGS), new File(this.getFilesDir(), RatingList.MOVIES));

        populateListView();

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user.getUsername());

        TextView major = (TextView) findViewById(R.id.tvMajor);
        major.setText(user.getMajor());

        Button mPlaceholderDone = (Button) findViewById(R.id.placeholder_done);
        mPlaceholderDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(userIntent);
                finish();
            }
        });

        Button viewProfile = (Button) findViewById(R.id.VProfile);
        viewProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(UserActivity.this, ViewProfileActivity.class);
                userIntent.putExtra("Email", UserActivity.this.email);
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

        Button mStartSearchButton = (Button) findViewById(R.id.startSearchButton);
        mStartSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(UserActivity.this, SearchMovieActivity.class);
                searchIntent.putExtra("Email", UserActivity.this.email);
                startActivity(searchIntent);
                finish();
            }
        });
    }

    private void populateListView() {
    }

    @Override
    public void onBackPressed() {
        RatingList.getInstance().saveRatings(new File(this.getFilesDir(), RatingList.RATINGS), new File(this.getFilesDir(), RatingList.MOVIES));
        super.onBackPressed();
    }
}
