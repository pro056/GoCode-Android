package com.example.i317409.gocode;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Dialog extends DialogFragment implements View.OnClickListener {


    Button b1, b2, b3, b4, b5, b6, ok,  cancel;
    ArrayList<String> sites;
    String[] site = {"Codechef", "Codeforces", "TopCoder", "HackerEarth", "HackerRank", "Kaggle"};
    ArrayList<String> mSelectedItems;
    Boolean[] siteinfo = {false, false, false, false, false, false};
    public Dialog() {


    }

    public void setArgs (ArrayList<String>sites) {
            this.sites = sites;
    }

   
   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectedItems = new ArrayList<String>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Select Sites");
        getDialog().onDetachedFromWindow();
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        double width = this.getResources().getDisplayMetrics().widthPixels;
        width = width * 0.9;

        int height = this.getResources().getDisplayMetrics().heightPixels;
        view.setMinimumWidth((int)width);
    /*    ImageView img = (ImageView) view.findViewById(R.id.invisible);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(width / 3, height / 2, width / 3, height / 4);
        img.setLayoutParams(params);*/
        b1 = (Button) view.findViewById(R.id.chef);
        b2 = (Button) view.findViewById(R.id.tc);
        b3 = (Button) view.findViewById(R.id.forces);
        b4 = (Button) view.findViewById(R.id.he);
        b5 = (Button) view.findViewById(R.id.hr);
        b6 = (Button) view.findViewById(R.id.kgl);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        for (int i = 0; i < sites.size(); i++) {
            if (sites.get(i).toString().toLowerCase().equals("codechef")) {
                b1.setBackgroundColor(Color.parseColor("#CCFF90"));
                siteinfo[i] = true;
            }
            if (sites.get(i).toString().toLowerCase().equals("topcoder")) {
                b2.setBackgroundColor(Color.parseColor("#CCFF90"));
                siteinfo[i] = true;
            }
            if (sites.get(i).toString().toLowerCase().equals("codeforces")) {
                b3.setBackgroundColor(Color.parseColor("#CCFF90"));
                siteinfo[i] = true;
            }
            if (sites.get(i).toString().toLowerCase().equals("hackerearth")) {
                b4.setBackgroundColor(Color.parseColor("#CCFF90"));
                siteinfo[i] = true;
            }
            if (sites.get(i).toString().toLowerCase().equals("hackerrank")) {
                b5.setBackgroundColor(Color.parseColor("#CCFF90"));
                siteinfo[i] = true;
            }
            if (sites.get(i).toString().toLowerCase().equals("kaggle")) {
                b6.setBackgroundColor(Color.parseColor("#CCFF90"));
                siteinfo[i] = true;
            }
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chef:
                if (siteinfo[0] == true) {
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    siteinfo[0] = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#CCFF90"));
                    siteinfo[0] = true;
                }
                break;
            case R.id.forces:
                if (siteinfo[1] == true) {
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    siteinfo[1] = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#CCFF90"));
                    siteinfo[1] = true;
                }
                break;
            case R.id.tc:
                if (siteinfo[2] == true) {
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    siteinfo[2] = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#CCFF90"));
                    siteinfo[2] = true;
                }
                break;
            case R.id.he:
                if (siteinfo[3] == true) {
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    siteinfo[3] = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#CCFF90"));
                    siteinfo[3] = true;
                }
                break;
            case R.id.hr:
                if (siteinfo[4] == true) {
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    siteinfo[4] = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#CCFF90"));
                    siteinfo[4] = true;
                }
                break;
            case R.id.kgl:
                if (siteinfo[5] == true) {
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    siteinfo[5] = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#CCFF90"));
                    siteinfo[5] = true;
                }
                break;
            case R.id.ok:
                for (int i = 0; i < siteinfo.length; i++) {
                    if (siteinfo[i] == true) {
                        sites.add(site[i]);
                    }
                }


        }

    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
