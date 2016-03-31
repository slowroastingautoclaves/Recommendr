package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.slowroastingautoclaves.recommendr.R;
//import edu.gatech.slowroastingautoclaves.recommendr.model.Conditions;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * Screen that allows user to register a new account.
 */
public class RegisterActivity extends AppCompatActivity {

    private TextView mUsernameView;
    private TextView mEmailView;
    private TextView mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get buttons and text fields.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameView = (TextView) findViewById(R.id.username);

        mEmailView = (TextView) findViewById(R.id.email);

        mPasswordView = (TextView) findViewById(R.id.password);

        Button mRegisterButton = (Button) findViewById(R.id.register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        Button mCancelButton = (Button) findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        Button mDoneButton = (Button) findViewById(R.id.done);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    /**
     * Checks validity of username.
     * @param username is username being checked.
     * @return true if username is valid, else false
     */
    private boolean isUsernameValid(String username) {
        return username.length() > 0;
    }

    /**
     * Checks validity of email.
     * @param email is email being checked.
     * @return true if email is valid, else false
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Checks validity of password.
     * @param password is password being checked.
     * @return true if password is valid, else false
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Tries to register by checking username, email, and password and whether user exists.
     */
    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        View focusView = null;

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        User currentUser = new User(username, email, password);

        boolean cancel = false;
        focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //Check for a valid username.
        if (TextUtils.isEmpty(email)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError("This username is invalid.");
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a Toast to alert user that a new user profile was created (or not).
            if (isEmailValid(email) && isPasswordValid(password) && isUsernameValid(username)) {
                if (!UserList.getInstance().getUsers().contains(currentUser)) {
                    UserList.getInstance().addUser(currentUser);
                    Context context = getApplicationContext();
                    CharSequence text = "New user created.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    focusView = mEmailView;
                    mEmailView.setError("User already exists.");
                    focusView.requestFocus();
                }

            } else {
                if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                }
                if (!isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                }
                if (!isUsernameValid(username)) {
                    mUsernameView.setError("This username is invalid.");
                    focusView = mUsernameView;
                }
                focusView.requestFocus();
            }
        }
    }
}
