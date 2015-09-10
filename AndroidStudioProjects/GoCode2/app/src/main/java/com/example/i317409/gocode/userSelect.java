package com.example.i317409.gocode;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.i317409.gocode.R.color.greenish;


public class userSelect extends ActionBarActivity implements View.OnFocusChangeListener  {

    EditText name, email, password, phone;
    Button button;
    LinearLayout l1;
    SharedPreferences sp, sp1;
    JSONObject obj;
    OkHttpClient client;;
    TextView withname, withemail, withpass, withphone;
    private String json;
    Context mContext;
    ProgressDialog progressDialog;


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp1 = getPreferences(MODE_PRIVATE);
        sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Log.d("name", sp.getString("name", "not recieved"));
        Log.d("email", sp.getString("email", "not recieved"));
        Log.d("email", sp.getString("entry", "null"));
        String entry = sp.getString("entry", "null");
        Log.d ("entry", entry);

        if (sp1.contains("done") || entry.equals("old")) {
            Intent i = new Intent(this, UserProfile.class);
            i.putExtra("name", sp.getString("name", "nothing"));
            i.putExtra("email", sp.getString("email", "nothing"));
            startActivity(i);
        } else {
            Log.d("tagged", "BULLSHIT");
        }
        setContentView(R.layout.activity_user_select);
        l1 = (LinearLayout) findViewById(R.id.layout);
        l1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View v1 = name;
                if (name.isFocused()) {
                    v1 = name;
                } else if (email.isFocused()) {
                    v1 = email;
                } else if (password.isFocused()) {
                    v1 = password;
                } else if (phone.isFocused()) {
                    v1 = phone;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (v1.isFocused()) {
                        Rect outRect = new Rect();
                        v1.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            v1.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        withname = (TextView) findViewById(R.id.withname);
        withemail = (TextView) findViewById(R.id.withemail);
        withpass = (TextView) findViewById(R.id.withpassword);
        withphone = (TextView) findViewById(R.id.withphone);

        button = (Button) findViewById(R.id.submit);
        client = new OkHttpClient();
        mContext = this;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(AlertDialog.THEME_HOLO_DARK);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = name.getText().toString();
                String pass = password.getText().toString();
                String id = email.getText().toString();
                String phoneno = phone.getText().toString();
                Log.d("its here", String.valueOf(pass.length()));
                if (pass.length() < 8) {
                    withpass.setTextColor(Color.RED);
                    Toast toast = Toast.makeText(getApplicationContext(), "Password should be of minimum 8 characters", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else if (!isValidEmail(id)) {
                    withemail.setTextColor(Color.RED);
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else if (phoneno.length() != 10) {
                    withphone.setTextColor(Color.RED);
                    Toast toast = Toast.makeText(getApplicationContext(), "Phone number should be of 10 digits.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                } else {
                    Users newuser = new Users(pass, user, phoneno, id);
                    Gson gson = new Gson();
                    json = gson.toJson(newuser);

                    run();
                }
            }
        });
        name.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        phone.setOnFocusChangeListener(this);

        Intent i = getIntent();
        String uname = i.getStringExtra("name");
        email.setText(i.getStringExtra("email"));
        name.setText(uname);
    }

    private void run() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(mContext, "Checking..", "Loading.. Please Wait..!!", true);

            }

            @Override
            protected Void doInBackground(Void... params) {
                String url = "https://gocode-programmingsport.rhcloud.com/createuser/";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response == null) {
                    Log.d("response", "is null");
                }
                String res = null;
                try {
                    res = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("res", res);
                try {
                    obj = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (obj.has("email_id")) {
                    progressDialog.dismiss();

                    Toast toast = Toast.makeText(mContext, "This email ID has been already registered", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    withemail.setTextColor(Color.RED);
                } else {
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(mContext, "Saved Successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    SharedPreferences.Editor edit = sp1.edit();
                    edit.putString("done", "yes");
                    edit.commit();
                    SharedPreferences.Editor edit1 = sp.edit();
                    edit1.putString("name", name.getText().toString());
                    edit1.putString("email", email.getText().toString());
                    edit1.putString("entry", "old");
                    edit1.commit();
                    Intent intent = new Intent(userSelect.this, UserProfile.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                }
            }


        }.execute(null, null, null);
    }


    void colorAllBlack(){
        withname.setTextColor(Color.parseColor("#ff373737"));
        withpass.setTextColor(Color.parseColor("#ff373737"));
        withemail.setTextColor(Color.parseColor("#ff373737"));
        withphone.setTextColor(Color.parseColor("#ff373737"));
    }




    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        colorAllBlack();


        if (v == name ) {

            withname.setTextColor(getResources().getColor(R.color.greenish));
        } else if (v == password ) {

            withpass.setTextColor(getResources().getColor(R.color.greenish));

        } else if (v == email) {

            withemail.setTextColor(getResources().getColor(R.color.greenish));
        } else if (v == phone) {

            withphone.setTextColor(getResources().getColor(R.color.greenish));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.getString("entry", "old") == "old") {
            finish();
        }
    }
}



