package com.example.i317409.gocode;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.TestUserManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button fb, sign;
    private LoginButton loginButton;
    String id, name, email, gender;
    private CallbackManager callbackManager;
    LinearLayout loading;
    ProgressDialog progressDialog, pd;
    public SharedPreferences sp;
    private Activity activity;
    EditText emailid, pass;
    Context mContext;
    LinearLayout l1;
    JSONObject obj;
    ScrollView sview;
    OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
    /*    try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.i317409.gocode",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        if (sp.contains("email")) {
            Intent intent;
            Log.d("entry", sp.getString("entry", "old"));
            if (sp.getString("entry", "null") == "old") {

                intent = new Intent(MainActivity.this, UserProfile.class);
            } else {
                intent = new Intent(MainActivity.this, userSelect.class);
            }
            intent.putExtra("name", sp.getString("name", null));
            intent.putExtra("email", sp.getString("email", null));
            startActivity(intent);
        } else {
            Log.d ("message", "nothing happening");
        }

        client = new OkHttpClient();


        mContext = this;
        progressDialog = new ProgressDialog(mContext);

        progressDialog.setProgressStyle(AlertDialog.THEME_HOLO_DARK);
        ActionBar acbar = getSupportActionBar();
        acbar.setDisplayHomeAsUpEnabled(false);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        l1 = (LinearLayout)findViewById(R.id.layoutmain);
        emailid = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        sign = (Button) findViewById(R.id.signin);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailid.getText().toString();
                String passw = pass.getText().toString();

                checkCredentials(email, passw);
            }
        });

        l1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (emailid.isFocused()) {
                        Rect outRect = new Rect();
                        emailid.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            emailid.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    } else if (pass.isFocused()) {
                        Rect outRect = new Rect();
                        pass.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            pass.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        callbackManager = CallbackManager.Factory.create();

        name = null;
        activity = this;
        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile");
        loginButton.setReadPermissions(permissionNeeds);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        progressDialog = ProgressDialog.show(activity, "Logging in..", "Loading.. Please Wait..!!", true);


                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.d("accessToken", accessToken);
                        findViewById(R.id.layoutmain).setVisibility(View.GONE);

                        progressDialog.setMessage("Fetching Data..");



                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        Log.d("LoginActivity", response.toString());
                                        try {
                                            JSONObject objname = (JSONObject) response.getJSONObject();
                                            id = object.getString("id");

                                /*        try {
                                            URL profile_pic = new URL(
                                                    "http://graph.facebook.com/" + id + "/picture?type=large");
                                            Log.d("profile_pic", profile_pic + "");

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }*/
                                            Log.d("name", objname.getString("name"));
                                            Log.d("email", objname.getString("email"));
                                            Log.d("gender", objname.getString("gender"));
                                            name = objname.getString("name");
                                            email = objname.getString("email");
                                            gender = objname.getString("gender");
                                            SharedPreferences.Editor edit = sp.edit();
                                            edit.putString("name", name);
                                            edit.putString("email", email);
                                            edit.putString("entry", "new");
                                            edit.commit();
                                            runThread();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        request.setParameters(parameters);
                        request.executeAsync();


                        //    Log.d("profile_pic", name);

                    }

                    @Override
                    public void onCancel() {
                        Log.d("cancelled", "onCancel");
                        progressDialog.dismiss();
                        findViewById(R.id.FrameLayout1).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        progressDialog.dismiss();
                        findViewById(R.id.FrameLayout1).setVisibility(View.VISIBLE);

                    }
                }
        );
    }

    void checkCredentials(final String email, final String passw) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
          //      super.onPreExecute();
                pd = ProgressDialog.show(activity, "Please Wait", "Checking Credentials", true);

            }

            @Override
            protected Void doInBackground(Void... params) {
                String url = "https://gocode-programmingsport.rhcloud.com/getuser/";

                String json = "{\"email\" : \"" + email + "\", \"password\" : \"" + passw + "\"}";
                CheckInternet ci = new CheckInternet(MainActivity.this);
                if (ci.haveNetworkConnection() == true) {

                    RequestBody body = RequestBody.create(JSON, json);

                    Request request = new Request.Builder().url(url).post(body).build();

                    Response response = null;
                    String result = null;
                    try {
                        response = client.newCall(request).execute();

                        result = response.body().string();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        obj = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    pd.dismiss();

                    if (obj.get("email") == "error") {
                        Toast toast = Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                    } else {
                        Editor edit = sp.edit();
                        String emailid = obj.get("email").toString();
                        String username = obj.get("name").toString();
                        edit.putString("email", emailid);
                        edit.putString("name", username);
                        edit.putString("entry", "old");
                        edit.commit();
                        Intent intent = new Intent(MainActivity.this, UserProfile.class);
                        intent.putExtra("email", emailid);
                        intent.putExtra("name", username);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, item);
        return super.onCreateOptionsMenu(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signup) {
            Intent intent = new Intent(MainActivity.this, userSelect.class);
            intent.putExtra("name", "");
            intent.putExtra("email", "");
            startActivity(intent);
        }
        return true;
    }

    public void runThread() {
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (name != null) {
                            Intent intent = new Intent(MainActivity.this, userSelect.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        }
                    }
                });
            }
        }.start();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        callbackManager.onActivityResult(requestCode, responseCode, data);

    }

    @Override
    public void onClick(View v) {
        if (v == fb) {
            progressDialog = ProgressDialog.show(activity, "Logging in", "Loading.. Please Wait..!!", true);
            findViewById(R.id.FrameLayout1).setVisibility(View.INVISIBLE);
            loginButton.performClick();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (name != null) {
            finish();
        } else {
            findViewById(R.id.FrameLayout1).setVisibility(View.VISIBLE);
        }
    }

   


}








