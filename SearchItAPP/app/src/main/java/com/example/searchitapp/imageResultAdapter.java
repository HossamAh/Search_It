package com.example.searchitapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.searchitapp.models.ImageResultItem;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

public class imageResultAdapter extends ArrayAdapter<ImageResultItem> {
    private Context mContext;
    private ArrayList<ImageResultItem>imageResultItems;
    URL url;
    public imageResultAdapter(Context context, int textViewResourceId, ArrayList<ImageResultItem> imageResultItems) {
        super(context, textViewResourceId, imageResultItems);
        mContext = context;
        this.imageResultItems =imageResultItems;
        Log.e(TAG, "imageResultAdapter: in constructor" );
    }

    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e(TAG, "getView: inside adapter"+imageResultItems.get(position).getImageURL()+" "+imageResultItems.get(position).getImagePageLink()+" "+imageResultItems.get(position).getImageCaption());

        View v;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_image_result_item, null);
        ImageView image = (ImageView)v.findViewById(R.id.imageView);
        TextView imageCaption = (TextView)v.findViewById(R.id.imageCaption_textView);
        TextView imagePageLink =(TextView)v.findViewById(R.id.PageLink_textView);

        Picasso.with(mContext).load(imageResultItems.get(position).getImageURL()).fit().centerCrop().into(image);
        imageCaption.setText(imageResultItems.get(position).getImageCaption());
        try {
            url = new URL(imageResultItems.get(position).getImagePageLink());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imagePageLink.setText(url.getHost());

        Log.e(TAG, "getView: Host"+url.getHost() );
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageResultItems.get(position).getImageURL()));
                openUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mContext,openUrlIntent,null);
            }
        });

        imageCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                openUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mContext,openUrlIntent,null);
            }
        });
        imagePageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+url.getHost()));
                openUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mContext,openUrlIntent,null);
            }
        });
        return v;
    }

}
