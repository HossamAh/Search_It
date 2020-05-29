package com.example.searchitapp;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.searchitapp.R;
import com.example.searchitapp.models.ImageResultItem;

import java.util.ArrayList;

public class imageResult extends AppCompatActivity {
    ArrayList<ImageResultItem> imageResultItems;
    imageResultAdapter imageResultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_result);
        imageResultItems=new ArrayList<>();

        //imageResultItems = getIntent().getParcelableArrayListExtra("imagesResult");
        imageResultAdapter = new imageResultAdapter(this,R.layout.activity_image_result_item,imageResultItems);
        ImageResultItem imageResultItem = new ImageResultItem("https://i.stack.imgur.com/mc6rV.png","How to get all css properties and events of a html element with jquery or other libraries?","https://stackoverflow.com");
        for(int i=0;i<10;i++)
        {
            imageResultAdapter.add(imageResultItem);
        }

        imageResultItems =new ArrayList<>();
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setVisibility(View.VISIBLE);
        gridView.post(new Runnable() {
            public void run() {
                System.out.println(imageResultAdapter.getCount());
                gridView.setAdapter(imageResultAdapter);
            }
        });
    }
}
