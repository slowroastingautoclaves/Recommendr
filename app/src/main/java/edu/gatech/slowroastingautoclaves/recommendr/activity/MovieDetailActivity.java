package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

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

        final Button rate = (Button) findViewById(R.id.submit);

        final RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);

        // handle listener
        rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final LayerDrawable layerDrawable = (LayerDrawable) rating.getProgressDrawable();
                DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), Color.YELLOW);
                return false;
            }

        });

        // record data and do not allow user to make another rating
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating.setEnabled(false);
                rate.setEnabled(false);

                double rateValue = rating.getRating() * 25;
                Rating currentRate = new Rating(MovieDetailActivity.this.identifier,
                        MovieDetailActivity.this.user, rateValue);

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


                // tell user that rating has been recorded
                CharSequence text = "Your rating has been recorded";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(rate.getContext(), text, duration);
                toast.show();
            }
        });


        /*
        //Make button to show ratings.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Enter an integer rating from 0 to 100 for the movie.\n";
                message += RatingList.getInstance().getRating(MovieDetailActivity.this.identifier);
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieDetailActivity.this);

                final EditText input = new EditText(MovieDetailActivity.this);

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
                               Rating currentRate = new Rating(MovieDetailActivity.this.identifier,
                                       MovieDetailActivity.this.user, rateValue);
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
                           }
                       })
                       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
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
        */
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
