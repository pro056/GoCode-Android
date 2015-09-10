package com.example.i317409.gocode;

/**
 * Created by I317409 on 8/26/2015.
 */
public class Contest {

    String name;
    String site;
    String duration;
    String start;
    String end;

    Contest (String s1, String s2, String s3, String s4, String s5) {
        name = s1;
        site = s2;
        start = s3;
        end = s4;
        duration = s5;
    }

    public String getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
