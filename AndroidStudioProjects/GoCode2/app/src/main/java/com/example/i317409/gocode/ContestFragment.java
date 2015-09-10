package com.example.i317409.gocode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.i317409.gocode.dummy.DummyContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ContestFragment extends Fragment implements ListView.OnItemClickListener {


    String url;
    String email;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private ContestListAdapter mAdapter;
    OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    JSONArray array;
    ProgressDialog pd;
    FrameLayout waiting, nothing;
    ArrayList<Contest> contests;
    public ContestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
        Log.d("url", url);
        email = getArguments().getString("email");
        Log.d("url", url);
        client = new OkHttpClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        waiting = (FrameLayout) view.findViewById(R.id.waiting);
        nothing = (FrameLayout) view.findViewById(R.id.nothing);
        contests = new ArrayList<Contest>() ;
        mAdapter = new ContestListAdapter(getActivity(), contests);
        mListView = new ListView(getActivity());
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);
        Log.d("All good", "damn right");
        CheckInternet ci = new CheckInternet(getActivity());

        if (ci.haveNetworkConnection()) {
            Log.d("url", url);
            populateList();
        } else {

            Toast toast = Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        return view;
    }

    private void populateList() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                waiting.setVisibility(View.VISIBLE);
                nothing.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                Log.d("url", url);
                String json = "{\"email\" : \"" + email + "\"}";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    array = obj.getJSONArray("contests");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.length(); i++) {
                    Contest contest = null;
                    try {
                        JSONObject object = (JSONObject) array.get(i);
                        contest = new Contest (object.getString("name"), object.getString("site"),
                                object.getString("start"), object.getString("end"),
                                object.getString("duration"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    contests.add(contest);
                    Log.d ("name", contests.get(i).toString());
                }
            //    Log.d("co", contests.get(0).getName());
              //  Log.d("co", contests.get(0).getSite());
              //  Log.d("co", contests.get(0).getStart());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

        //        mAdapter.addAll(contests);
                waiting.setVisibility(View.GONE);
                if (contests.size() == 0) {
                    nothing.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();



            }
        }.execute(null, null, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
