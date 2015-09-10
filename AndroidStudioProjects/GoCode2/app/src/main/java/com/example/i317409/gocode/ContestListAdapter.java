package com.example.i317409.gocode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I317409 on 8/26/2015.
 */
public class ContestListAdapter extends ArrayAdapter <Contest> implements View.OnClickListener {

    Context context;
    ArrayList<Contest> contests;

    public ContestListAdapter(Context context, ArrayList<Contest> objects) {
        super(context, -1, objects);
        this.context = context;
        this.contests = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.contestlist, parent, false);
        TextView cid = (TextView) row.findViewById(R.id.contestid);
        TextView site = (TextView)row.findViewById(R.id.site);
        ImageView imageView = (ImageView) row.findViewById(R.id.icon);
        String conid = contests.get(position).getName();
        String consite = contests.get(position).getSite();
        OptimizeBitmap optimizeBitmap = new OptimizeBitmap();
        cid.setText(conid);
        site.setText(consite);
        switch (consite) {
            case "codechef":
                imageView.setImageBitmap(
                        optimizeBitmap.decodeSampledBitmapFromResource(context.getResources(), R.drawable.chef, 80, 80));
                break;
            case "topcoder":
                imageView.setImageBitmap(
                        optimizeBitmap.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tc, 80, 80));
                break;
            case "hackerearth":
                imageView.setImageBitmap(
                        optimizeBitmap.decodeSampledBitmapFromResource(context.getResources(), R.drawable.he, 80, 80));
                break;
            case "hackerrank":
                imageView.setImageBitmap(
                        optimizeBitmap.decodeSampledBitmapFromResource(context.getResources(), R.drawable.hr, 80, 80));
                break;
            case "kaggle":
                imageView.setImageBitmap(
                        optimizeBitmap.decodeSampledBitmapFromResource(context.getResources(), R.drawable.kaggle, 80, 80));
                break;
            case "add":
                imageView.setImageBitmap(
                        optimizeBitmap.decodeSampledBitmapFromResource(context.getResources(), R.drawable.add, 80, 80));
                break;
        }
        return row;
    }

    @Override
    public void onClick(View v) {

    }
}
