package com.example.searchitapp;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.searchitapp.R;
import com.example.searchitapp.models.ImageResultItem;
import com.example.searchitapp.models.QueryResultItem;

import java.util.ArrayList;

public class imageResult extends AppCompatActivity {
    ArrayList<ImageResultItem> imageResultItems;
    ArrayList<String> imageResultString;
    imageResultAdapter imageResultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_result);
        imageResultItems=new ArrayList<>();
        imageResultString = getIntent().getStringArrayListExtra("imagesResult");
        imageResultAdapter = new imageResultAdapter(this,R.layout.activity_image_result_item,imageResultItems);
        for (int k=0;k<=imageResultString.size()-2;k+=3)
        {
            ImageResultItem tempItem1 =new ImageResultItem(imageResultString.get(k),
                    imageResultString.get(k+1),imageResultString.get(k+2));
            imageResultAdapter.add(tempItem1);
        }
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setVisibility(View.VISIBLE);
        gridView.post(new Runnable() {
            public void run() {

                gridView.setAdapter(imageResultAdapter);
            }
        });
    }
}
