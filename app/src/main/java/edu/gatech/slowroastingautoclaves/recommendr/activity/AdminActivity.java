package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.Condition;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * Created by Christian Girala on 3/14/2016.
 */
public class AdminActivity extends Activity{
    private String email;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile);
        this.users = new ArrayList<>();

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");

        populateListView();
        //registerClickCallback();


    }

    private void populateListView() {
        // create list of items

        for (User u : UserList.getInstance().getUsers()) {
            Log.i("PLS", u.getEmail());
            if (!u.getEmail().equals(email)) {
                users.add(u);
            }
        }
        //build adapter
        ArrayAdapter<User> adapter = new ArrayAdapterItem<User>(this, R.layout.user_detail);

        //configure listview
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    public class ArrayAdapterItem<E> extends ArrayAdapter<E> {

        Context mContext;

        public ArrayAdapterItem(Context mContext, int layoutResourceId) {

            super(mContext, layoutResourceId);
            this.mContext = mContext;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("GETVIEW","CALLED");
            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.user_detail, parent, false);
                TextView username = (TextView) convertView.findViewById(R.id.textView15);
                User u = users.get(position);
                username.setText(u.getUsername());
                RadioButton rb;
                if (u.getCondition() == Condition.UNLOCKED) {
                    rb = (RadioButton) convertView.findViewById(R.id.radio);
                } else if (u.getCondition() == Condition.LOCKED) {
                    rb = (RadioButton) convertView.findViewById(R.id.radio2);
                } else {
                    rb = (RadioButton) convertView.findViewById(R.id.radio3);
                }
                rb.setChecked(true);
            }



            return convertView;
        }

        @Override
        public int getCount() {
            return users.size();
        }
    }
/*
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
                                    long id) {

            }
        });
    }*/
}
