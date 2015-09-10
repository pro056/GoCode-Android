package com.example.i317409.gocode;

import android.provider.ContactsContract;

/**
 * Created by I317409 on 8/23/2015.
 */
public class sites {

    String chef_handle;
    String tc_handle;
    String forces_handle;
    String hackere_handle;
    String hackerr_handle;
    String kaggle_handle;

    sites (String s1, String s2,String s3, String s4, String s5,String s6) {
        chef_handle = s1;
        tc_handle = s2;
        forces_handle = s3;
        hackere_handle = s4;
        hackerr_handle = s5;
        kaggle_handle = s6;
    }
    String getChef_handle (){ return chef_handle;}
    String getTc_handle () {return tc_handle;}
    String getForces_handle () {return forces_handle;}
    String getHackere_handle () {return hackere_handle;}
    String getHackerr_handle () {return hackerr_handle;}
    String getKaggle_handle () {return kaggle_handle;}


}
