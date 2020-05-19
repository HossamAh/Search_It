package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.search_it.R;

public class resultsTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_test);
        Intent intent = getIntent();
        TextView testtext=findViewById(R.id.textView);
        testtext.setText(intent.getStringExtra("search_query"));
    }
}
