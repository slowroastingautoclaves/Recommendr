package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
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

import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;

public class SearchMovieActivity extends AppCompatActivity {

    /*Volley queue for REST calls*/
    private RequestQueue queue;
    /*Holds result from REST call*/
    private String response;

    private SearchView mMovieSearchView;
    private Button mRecentReleaseButton;
    private Button mRecentDVDsButton;
    private Button mTopMoviesButton;
    private Button mTopMoviesMajorButton;
    private ImageButton mMovieSearchButton;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");

        setContentView(R.layout.activity_search_movie);
        queue = Volley.newRequestQueue(this);

        this.mMovieSearchView = (SearchView) findViewById(R.id.movieSearchView);
        this.mMovieSearchButton = (ImageButton) findViewById(R.id.movieSearchButton);
        mMovieSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchRequested();
            }
        });
        this.mRecentReleaseButton = (Button) findViewById(R.id.releasesButton);
        mRecentReleaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecentReleases();
            }
        });
        this.mRecentDVDsButton = (Button) findViewById(R.id.dvdsButton);
        mRecentDVDsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecentDVDs();
            }
        });
        this.mTopMoviesButton = (Button) findViewById(R.id.topMoviesButton);
        mTopMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTopMovies("null");
            }
        });
        this.mTopMoviesMajorButton = (Button) findViewById(R.id.topMoviesMajorButton);
        mTopMoviesMajorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTopMovies("major");
            }
        });
    }

    /**
     * Gets movies matching search query from RottenTomatoes REST API and displays them in a
     * Master/Detail list.
     *
     * @return true if search completed (does not return false, throws Exception)
     */
    @Override
    public boolean onSearchRequested() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
        String query = mMovieSearchView.getQuery().toString().replace(" ", "+");

        url += ("&q=" + query + "&page_limit=50");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        //handle a valid response coming back.  Getting this string mainly for debug
                        response = resp.toString();
                        //printing first 500 chars of the response.  Only want to do this for debug
                        //TextView view = (TextView) findViewById(R.id.textView2);
                        //view.setText(response.substring(0, 500));

                        //Now we parse the information.  Looking at the format, everything encapsulated in RestResponse object
                        JSONArray array = null;
                        try {
                            array = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assert array != null;
                        //From that object, we extract the array of actual data labeled result
                        // (NOT IN ROTTEN TOMATOES)
                        //JSONArray array = obj1.optJSONArray("result");
                        ArrayList<Movie> movies = new ArrayList<>();
                        for(int i=0; i < array.length(); i++) {

                            try {
                                //for each array element, we have to create an object
                                JSONObject jsonObject = array.getJSONObject(i);
                                Movie m = new Movie();
                                assert jsonObject != null;
                                m.setTitle(jsonObject.optString("title"));
                                m.setYear(jsonObject.optString("year"));
                                m.setDescription(jsonObject.optString("synopsis"));
                                //save the object for later
                                movies.add(m);


                            } catch (JSONException e) {
                                Log.d("VolleyApp", "Failed to get JSON object");
                                e.printStackTrace();
                            }
                        }
                        //once we have all data, then go to list screen
                        changeView(movies);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        response = "JSon Request Failed!!";
                        //show error on phone
                        //TextView view = (TextView) findViewById(R.id.textView2);
                        //view.setText(response);
                    }
                });
        //this actually queues up the async response with Volley
        queue.add(jsObjRequest);
        return true;
    }

    /**
     * Changes view to Master/Detail list with given list of movies.
     * @param movies
     */
    public void changeView(ArrayList<Movie> movies) {
        Intent viewResultsIntent = new Intent(this, MovieListActivity.class);
        viewResultsIntent.putExtra("movies", movies);
        viewResultsIntent.putExtra("Email", SearchMovieActivity.this.email);
        startActivity(viewResultsIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //Go back to UserActivity instead of closing app.
        Intent userIntent = new Intent(this, UserActivity.class);
        userIntent.putExtra("Email", SearchMovieActivity.this.email);
        startActivity(userIntent);
        finish();
    }

    /**
     * Gets recent releases from RottenTomatoes REST API and displays them in a Master/Detail list.
     */
    public void getRecentReleases() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/opening.json?apikey=yedukp76ffytfuy24zsqk7f5";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        //handle a valid response coming back.  Getting this string mainly for debug
                        response = resp.toString();
                        //printing first 500 chars of the response.  Only want to do this for debug
                        //TextView view = (TextView) findViewById(R.id.textView2);
                        //view.setText(response.substring(0, 500));

                        //Now we parse the information.  Looking at the format, everything encapsulated in RestResponse object
                        JSONArray array = null;
                        try {
                            array = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assert array != null;
                        //From that object, we extract the array of actual data labeled result
                        // (NOT IN ROTTEN TOMATOES)
                        //JSONArray array = obj1.optJSONArray("result");
                        ArrayList<Movie> movies = new ArrayList<>();
                        for(int i=0; i < array.length(); i++) {

                            try {
                                //for each array element, we have to create an object
                                JSONObject jsonObject = array.getJSONObject(i);
                                JSONObject ratingsJSON = jsonObject.getJSONObject("ratings");
                                Movie m = new Movie();
                                assert jsonObject != null;
                                m.setTitle(jsonObject.optString("title"));
                                m.setYear(jsonObject.optString("year"));
                                m.setDescription(jsonObject.optString("synopsis"));
                                m.setRating(ratingsJSON.optString("critics_rating"));
                                //save the object for later
                                movies.add(m);


                            } catch (JSONException e) {
                                Log.d("VolleyApp", "Failed to get JSON object");
                                e.printStackTrace();
                            }
                        }
                        //once we have all data, then go to list screen
                        changeView(movies);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        response = "JSon Request Failed!!";
                        //show error on phone
                        //TextView view = (TextView) findViewById(R.id.textView2);
                        //view.setText(response);
                    }
                });
        //this actually queues up the async response with Volley
        queue.add(jsObjRequest);
    }

    /**
     * Gets recent DVDs from RottenTomatoes REST API and displays them in a Master/Detail list.
     */
    public void getRecentDVDs() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        //handle a valid response coming back.  Getting this string mainly for debug
                        response = resp.toString();
                        //printing first 500 chars of the response.  Only want to do this for debug
                        //TextView view = (TextView) findViewById(R.id.textView2);
                        //view.setText(response.substring(0, 500));

                        //Now we parse the information.  Looking at the format, everything encapsulated in RestResponse object
                        JSONArray array = null;
                        try {
                            array = resp.getJSONArray("movies");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assert array != null;
                        //From that object, we extract the array of actual data labeled result
                        // (NOT IN ROTTEN TOMATOES)
                        //JSONArray array = obj1.optJSONArray("result");
                        ArrayList<Movie> movies = new ArrayList<>();
                        for(int i=0; i < array.length(); i++) {

                            try {
                                //for each array element, we have to create an object
                                JSONObject jsonObject = array.getJSONObject(i);
                                Movie m = new Movie();
                                assert jsonObject != null;
                                m.setTitle(jsonObject.optString("title"));
                                m.setYear(jsonObject.optString("year"));
                                m.setDescription(jsonObject.optString("synopsis"));
                                //save the object for later
                                movies.add(m);


                            } catch (JSONException e) {
                                Log.d("VolleyApp", "Failed to get JSON object");
                                e.printStackTrace();
                            }
                        }
                        //once we have all data, then go to list screen
                        changeView(movies);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        response = "JSon Request Failed!!";
                        //show error on phone
                        //TextView view = (TextView) findViewById(R.id.textView2);
                        //view.setText(response);
                    }
                });
        //this actually queues up the async response with Volley
        queue.add(jsObjRequest);
    }

    public void getTopMovies(String filter) {
        if (filter.equals("null")) {
            ArrayList<Movie> out = RatingList.getInstance().getTopMovies("null", "null");
            if (out == null) {
                Context context = getApplicationContext();
                CharSequence text = "No rated movies found matching criteria.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                changeView(out);
            }
        } else if (filter.equals("major")) {
            String message = "Enter a major to filter movie recommendations by.\n";
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchMovieActivity.this);

            final EditText input = new EditText(SearchMovieActivity.this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint("Type in major to restrict search.");

            builder.setMessage(message)
                    .setTitle("Filter Major")
                    .setView(input)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String userIn = input.getText().toString();
                            if (userIn.length() < 1) {
                                return;
                            }
                            ArrayList<Movie> out = RatingList.getInstance().getTopMovies("major", userIn);
                            if (out == null) {
                                Context context = getApplicationContext();
                                CharSequence text = "No rated movies found matching criteria.";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                return;
                            }
                            changeView(out);
                            return;
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
    }
}
