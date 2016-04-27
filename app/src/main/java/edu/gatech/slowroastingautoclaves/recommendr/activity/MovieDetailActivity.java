package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
    private String response;
    private RequestQueue queue;
    private String posterURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");
        user = UserList.getInstance().findUserByEmail(email);

        queue = Volley.newRequestQueue(this);

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

        ImageView imageView = (ImageView) findViewById(R.id.movie_poster);

        getPoster();
        try {
            URL url = new URL(posterURL);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPosterURL(String url) {
        posterURL = url;
    }

    private void getPoster() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
        String query = this.movie.getTitle().replace(" ", "+");
        url += ("&q=" + query + "&page_limit=1");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        //handle a valid response coming back.  Getting this string mainly for debug
                        response = resp.toString();
                        Log.i("resp", response.toString());

                        //Now we parse the information.  Looking at the format, everything encapsulated in RestResponse object
                        JSONArray array = null;
                        try {
                            array = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            Log.e("SearchMovieActivity", e.getMessage());
                        }
                        assert array != null;
                        //From that object, we extract the array of actual data labeled result
                        // (NOT IN ROTTEN TOMATOES)
                        //JSONArray array = obj1.optJSONArray("result");

                            try {
                                //for each array element, we have to create an object
                                JSONObject jsonObject = array.getJSONObject(0);
                                JSONObject posterJSON = jsonObject.getJSONObject("posters");
                                setPosterURL(posterJSON.get("original").toString());

//                                Movie m = new Movie();
//                                m.setDescription(jsonObject.optString("original"));
//                                m.setRating(ratingsJSON.optString("critics_rating"));
                                //save the object for later


                            } catch (JSONException e) {
                                Log.d("VolleyApp", "Failed to get JSON object");
                                Log.e("MovieDetailActivity", e.getMessage());
                            }
                        }
                        //once we have all data, then go to list screen
                        //changeView(movies);
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                response = "JSon Request Failed!!";
            }
        });
        //this actually queues up the async response with Volley
        queue.add(jsObjRequest);
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
