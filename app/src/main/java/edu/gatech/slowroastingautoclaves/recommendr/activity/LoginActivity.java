package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.slowroastingautoclaves.recommendr.R;
//import edu.gatech.slowroastingautoclaves.recommendr.model.Condition;
import edu.gatech.slowroastingautoclaves.recommendr.model.Condition;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // record login attempts
    private short attempts;
    private User prevAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get buttons and text fields.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

                mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        final Button mRegisterButton = (Button) findViewById(R.id.register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        UserList.getInstance().loadUsers(new File(this.getFilesDir(), UserList.USERS));
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("This password is too short or incorrect");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

            // lock account if tried too many times
            if (attempts == 3) {
                prevAttempt.setCondition(Condition.LOCKED);
            }
        }
    }



    @Override
    public void onBackPressed() {
        //Checks if authentication is running and cancels it, otherwise exits app as in super.
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
        } else {
            UserList.getInstance().saveUsers(new File(this.getFilesDir(), UserList.USERS));
            super.onBackPressed();
        }
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
     * Shows the progress UI and hides the login form.
     * @param show is whether to show or hide the progress spinner
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                    ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                    .CONTENT_ITEM_TYPE},

                    // Show primary email addresses first. Note that there won't be
                    // a primary email address if the user hasn't specified one.
                    ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        final List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Adds emails to autocomplete.
     * @param emailAddressCollection is list of emails
     */
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        final ArrayAdapter<String> adapter =
            new ArrayAdapter<>(LoginActivity.this,
                    android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        // --Commented out by Inspection (4/4/16, 2:50 AM):int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        /**
         * Constructor for a UserLoginTask object representing an asyncrhonous task.
         * @param email is email to attempt using.
         * @param password is password to attempt using.
         */
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(20);
            } catch (InterruptedException e) {
                return false;
            }

            for (User u : UserList.getInstance().getUsers()) {
                if (u.getEmail().equals(mEmail)) {
                    // see if tried logging into this user before
                    if (prevAttempt != null && prevAttempt.equals(u)) {
                        attempts++;
                    } else {
                        prevAttempt = u;
                        attempts = 0;
                    }

                    // Account exists, return true if the password matches.
                    if (u.getPassword().equals(mPassword)) {
                        // password matches, does the account have access to app?
                        return u.getCondition().equals(Condition.UNLOCKED);
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            Intent userIntent;

            if (success) {
                if (!prevAttempt.getAdminStatus()) {
                    //Send email to UserActivity and start UserActivity.
                    userIntent = new Intent(LoginActivity.this, UserActivity.class);
                } else {
                    userIntent = new Intent(LoginActivity.this, AdminActivity.class);
                }
                userIntent.putExtra("Email", this.mEmail);
                startActivity(userIntent);
                finish();
            } else {
                if (UserList.getInstance().getUsers() != null && UserList.getInstance().getUsers().size() > 0 && UserList.getInstance().findUserByEmail(this.mEmail) == null) {
                    mPasswordView.setError("This user does not exist.");
                    mPasswordView.requestFocus();
                } else //noinspection ConstantConditions,ConstantConditions
                    if (UserList.getInstance().getUsers() != null && UserList.getInstance().getUsers().size() > 0 && (UserList.getInstance().findUserByEmail(this.mEmail).getCondition().equals(Condition.BANNED) || UserList.getInstance().findUserByEmail(this.mEmail).getCondition().equals(Condition.LOCKED))) {
                    mPasswordView.setError("This user is banned/locked.");
                    mPasswordView.requestFocus();
                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

