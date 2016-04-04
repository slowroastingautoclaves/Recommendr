package edu.gatech.slowroastingautoclaves.recommendr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.R;
import edu.gatech.slowroastingautoclaves.recommendr.model.Condition;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * Represents a screen that shows admin activities, only opens for admin users.
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
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(email);

        populateListView();

        Button mPlaceholderDone = (Button) findViewById(R.id.admin_logout);
        mPlaceholderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(userIntent);
                finish();
            }
        });

    }

    /**
     * Adds User items to the list of Users.
     */
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

    /**
     * Adapter for list of users.
     * @param <E> is User object type
     */
    public class ArrayAdapterItem<E> extends ArrayAdapter<E> {

        Context mContext;

        public ArrayAdapterItem(Context mContext, int layoutResourceId) {

            super(mContext, layoutResourceId);
            this.mContext = mContext;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

                // create radio group listener to record any changes
                final int pos = position;
                RadioGroup radioGroup = (RadioGroup) convertView.findViewById(R.id.radio_group);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        final User u = users.get(pos);
                        if (checkedId == R.id.radio) {
                            u.setCondition(Condition.UNLOCKED);
                        } else if (checkedId == R.id.radio2) {
                            u.setCondition(Condition.LOCKED);
                        } else if (checkedId == R.id.radio3) {
                            u.setCondition(Condition.BANNED);
                        }
                    }
                });
            }
//
            return convertView;
        }

        @Override
        public int getCount() {
            return users.size();
        }
    }
}
