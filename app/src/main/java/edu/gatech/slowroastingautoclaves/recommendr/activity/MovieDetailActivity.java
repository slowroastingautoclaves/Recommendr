package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.Movies;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;
import edu.gatech.slowroastingautoclaves.recommendr.presenter.MovieDetailFragment;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private String identifier;
    private Movie movie;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");
        user = UserList.getInstance().findUserByEmail(email);


        // Do NOT show the Up button in the action bar.
        // Will be using back button to navigate instead.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(MovieDetailFragment.ARG_ITEM_ID));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
        this.movie = Movies.ITEM_MAP.get(intent.getStringExtra("Movie"));
        this.identifier = this.movie.toString();

//        Log.i("USER EXISTS", this.user.getEmail());
//
//        Log.i("MOVIE EXISTS", movie.toString());
//        Log.i("IDENTIFIER EXISTS", identifier);

        //Make button to show ratings.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Enter an integer rating from 0 to 100 for the movie.\n";
                message += RatingList.getInstance().getRating(MovieDetailActivity.this.identifier);
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieDetailActivity.this);

                final EditText input = new EditText(MovieDetailActivity.this);
//                final RatingBar input = new RatingBar(MovieDetailActivity.this);
//                input.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
//                input.setStepSize(0.5f);
//                input.setMax(5);
//                input.setNumStars(5);
//                input.setRating(2.0f);
//                input.setId(0);

                input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                input.setHint("Type rating from 0.0 - 100.0");

                builder.setMessage(message)
                       .setTitle("Rate Movie")
                       .setView(input)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               String userIn = input.getText().toString();
                               if (userIn.length() < 1) {
                                   dialog.dismiss();
                                   return;
                               }
                               double rateValue = 0;
                               if (Double.parseDouble(input.getText().toString()) > 0) {
                                   rateValue = Double.parseDouble(input.getText().toString());
                               }
                               Rating currentRate = new Rating(MovieDetailActivity.this.identifier, MovieDetailActivity.this.user, rateValue);
                               if (!RatingList.getInstance().getRatings().contains(currentRate)) {
                                   RatingList.getInstance().addRating(currentRate);
                                   RatingList.getInstance().addMovie(MovieDetailActivity.this.movie);
                                   MovieDetailActivity.this.user.addRating(currentRate);
                               } else {
                                   RatingList.getInstance().removeRating(currentRate);
                                   MovieDetailActivity.this.user.removeRating(currentRate);
                                   RatingList.getInstance().addRating(currentRate);
                                   RatingList.getInstance().addMovie(MovieDetailActivity.this.movie);
                                   MovieDetailActivity.this.user.addRating(currentRate);
                               }
                               dialog.dismiss();
                               return;
                           }
                       })
                       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                               return;
                           }
                       });
                AlertDialog dialog = builder.create();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is NOT shown. Back button is used to navigate up one level.
            //
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
