package com.example.i317409.gocode;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class UserProfile extends ActionBarActivity  {

    String name, email;
    TextView user, id;
    ImageView im;
    Button button;
    int width;
    Context context;
    int height;
    int i = 1;
    View v;
    Display disp;
    MySimpleArrayAdapter adapter;
    ArrayList<String> sites;
    OkHttpClient client;
    JSONArray array;
    ListView lview;
    SharedPreferences site;

    ImageView imageView;
    LinearLayout l1;
    FrameLayout l2;
    JSONObject obj;
    private static int RESULT_LOAD_IMAGE = 1;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        user = (TextView) findViewById(R.id.name);
        id = (TextView) findViewById(R.id.email);
        site = getPreferences(MODE_PRIVATE);
        Intent i = getIntent();
        v = findViewById(R.id.profile);
        user.setText(i.getStringExtra("name"));
        id.setText(i.getStringExtra("email"));
        email = i.getStringExtra("email");
        width = v.getWidth();
        ActionBar acbar = getSupportActionBar();
        imageView = (ImageView) findViewById(R.id.dp);
        array = new JSONArray();

        context = this;
        height = v.getHeight();
        sites = new ArrayList<String>();
        sites.clear();


        im = (ImageView) findViewById(R.id.dp);
        client = new OkHttpClient();
        l1 = (LinearLayout) findViewById(R.id.mid);
        l2 = (FrameLayout) findViewById(R.id.waiting);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.id.dp);
        button = (Button) findViewById(R.id.change);
        lview = (ListView) findViewById(R.id.lview);
        sites.clear();

        adapter = new MySimpleArrayAdapter(this, sites);
        adapter.clear();
        lview.setAdapter(adapter);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.defimage);
        Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
        Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
        disp = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        im.setImageBitmap(conv_bm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        CheckInternet hasInternet = new CheckInternet(this);
        if (hasInternet.haveNetworkConnection())
            getSites();
        else {
            Toast toast = Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);

        return true;
    }


    public Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(v.getWidth()/2, 100, v.getWidth(), paint);
       //     paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ContestsPage.class);

        intent.putExtra("email", email);
        startActivity(intent);


        return super.onOptionsItemSelected(item);
    }


    public void getSites() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                l2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                String json = "{\"email\" : \"" + id.getText().toString() + "\"}";
                String url = "https://gocode-programmingsport.rhcloud.com/getsites/";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String res = "";
                try {
                    res = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    obj = new JSONObject(res);
                    array = obj.getJSONArray("sites");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                l2.setVisibility(View.GONE);
                l1.setVisibility(View.VISIBLE);
                sites.clear();
                adapter.clear();


                for (i = 0; i < array.length(); i++) {
                    try {
                        sites.add(i, array.get(i).toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                sites.add(i, "Add");
        //        adapter.addAll(sites);
                adapter.notifyDataSetChanged();


            }
        }.execute(null, null, null);
    }
    class MySimpleArrayAdapter extends ArrayAdapter<String>  {
        private final Context context;
        private final ArrayList<String> values;
        int pos;


        public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
            super (context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.profilelview, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.site);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            textView.setText(values.get(position));

            pos = position;
            OptimizeBitmap optimizeBitmap = new OptimizeBitmap();
            switch (values.get(position).toLowerCase()) {
                case "codechef":
                    imageView.setImageBitmap(
                            optimizeBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.chef, 80, 80));
                    break;
                case "topcoder":
                    imageView.setImageBitmap(
                            optimizeBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.tc, 80, 80));
                    break;
                case "hackerearth":
                    imageView.setImageBitmap(
                            optimizeBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.he, 80, 80));
                    break;
                case "hackerrank":
                    imageView.setImageBitmap(
                            optimizeBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.hr, 80, 80));
                    break;
                case "kaggle":
                    imageView.setImageBitmap(
                            optimizeBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.kaggle, 80, 80));
                    break;
                case "add":
                    imageView.setImageBitmap(
                            optimizeBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.add, 80, 80));
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager fm = getSupportFragmentManager();
                            Dialog dialog = new Dialog();
                            dialog.setArgs(values);
                            dialog.show(fm, "Select Sites");
                        }
                    });
                    break;

            }


            return rowView;
        }

        @Override
        public int getCount() {
            return values.size();
        }

    }
}
