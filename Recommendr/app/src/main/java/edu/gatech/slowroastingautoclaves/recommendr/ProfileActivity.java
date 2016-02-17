package edu.gatech.slowroastingautoclaves.recommendr;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");

        User u = UserList.getInstance().findUserByEmail(this.email);

        TextView usernameView = (TextView) findViewById(R.id.usernamedisplay);
        TextView majorView = (TextView) findViewById(R.id.major);
        TextView descriptionView = (TextView) findViewById(R.id.description);

        if (u != null) {
            usernameView.setText(u.getUsername());
            if (u.getMajor() != null) {
                majorView.setText(u.getMajor());
            }
            if (u.getDescription() != null) {
                descriptionView.setText(u.getDescription());
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
                userIntent.putExtra("Email", ProfileActivity.this.email);
                startActivity(userIntent);
                finish();
            }
        });
    }

    private void editProfile() {
        TextView majorView = (TextView) findViewById(R.id.major);
        TextView descriptionView = (TextView) findViewById(R.id.description);
        User u = UserList.getInstance().findUserByEmail(this.email);
        u.setMajor(majorView.getText().toString());
        u.setDescription(descriptionView.getText().toString());

        Context context = getApplicationContext();
        CharSequence text = "Profile updated.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
