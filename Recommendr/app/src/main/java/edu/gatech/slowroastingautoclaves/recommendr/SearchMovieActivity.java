package edu.gatech.slowroastingautoclaves.recommendr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SearchMovieActivity extends AppCompatActivity {

    /*Volley queue for REST calls*/
    private RequestQueue queue;
    /*Holds result from REST call*/
    private String response;

    private SearchView mMovieSearchView;
    private Button mRecentReleaseButton;
    private Button mRecentDVDsButton;
    private Button mMovieSearchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        queue = Volley.newRequestQueue(this);

        this.mMovieSearchView = (SearchView) findViewById(R.id.movieSearchView);
        this.mMovieSearchButton = (Button) findViewById(R.id.movieSearchButton);
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
    }

    @Override
    public boolean onSearchRequested() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
        String query = 
    }
}
