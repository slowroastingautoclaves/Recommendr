package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Created by Christian Girala on 3/14/2016.
 */
public class AdminActivity extends AppCompatActivity{
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile);

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");


    }
}
