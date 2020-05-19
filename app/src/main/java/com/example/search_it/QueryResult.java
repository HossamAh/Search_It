package com.example.search_it;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.search_it.models.QueryResultItem;

import java.util.ArrayList;

public class QueryResult extends AppCompatActivity{
    private ArrayList<QueryResultItem> ResultsList,CurrentResultPage;
    private TextView ResultsNumber;
    private TextView CurrentPage;
    private ImageButton NextPage;
    private ImageButton PreviousPage;
    private ResultsPageFragment pagesFragment;
    private FragmentTransaction ft;
    private int CurrentPageNumber =1;
    private int TotalPagesNumber;
    private int previousLimit;
    private ArrayList<String> QueryKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result2);
        ResultsNumber = (TextView)findViewById(R.id.result_number_textView);
        CurrentPage = (TextView)findViewById(R.id.CurrentPAge_textView);
        NextPage =(ImageButton)findViewById(R.id.Next_Button);
        PreviousPage = (ImageButton)findViewById(R.id.Previous_Button);

        ResultsList =new ArrayList<>();
        //ResultsList = getIntent().getParcelableArrayListExtra("Results");
        QueryKeys =new ArrayList<>();
        //QueryKeys = getIntent().getStringArrayListExtra("QueryKeys");
        QueryKeys.add("facebook");
        QueryKeys.add("twitter");
        QueryKeys.add("instagram");


        QueryResultItem ResultItem1 =new QueryResultItem(0,"Facebook|Login or sign up",
                "https://www.facebook.com/","facebook about",null,null);
        QueryResultItem ResultItem2 =new QueryResultItem(0,"Twitter|Login or sign up",
                "https://www.twitter.com/","twitter about",null,null);
        QueryResultItem ResultItem3 =new QueryResultItem(0,"Instagram|Login or sign up",
                "https://www.Instagram.com/","instagram about",null,null);
        for(int i=0;i<10;i++)
            ResultsList.add(ResultItem1);
        for(int i=0;i<10;i++)
            ResultsList.add(ResultItem2);
        for(int i=0;i<10;i++)
            ResultsList.add(ResultItem3);



        TotalPagesNumber= (int) Math.ceil((ResultsList.size()/10.0));

        ResultsNumber.setText("results contain "+ResultsList.size()+" pages");
        CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
        CurrentResultPage = new ArrayList<>();
        CurrentResultPage.addAll(ResultsList.subList(0,CurrentPageNumber*10));

        pagesFragment = ResultsPageFragment.newInstance(CurrentResultPage,QueryKeys);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.ResultPage_FrameLayout, pagesFragment).addToBackStack(null);
        ft.commit();
        if(CurrentPageNumber ==1 )
        {
            PreviousPage.setClickable(false);
        }

        PreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentPageNumber > 1) {
                    CurrentPageNumber--;
                    if(CurrentPageNumber<TotalPagesNumber)
                    {
                        NextPage.setClickable(true);
                    }
                    if(CurrentPageNumber>0)
                        previousLimit = CurrentPageNumber-1;
                    CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
                    CurrentResultPage = new ArrayList<>();
                    CurrentResultPage.addAll(ResultsList.subList(previousLimit*10, CurrentPageNumber * 10));
                    pagesFragment = ResultsPageFragment.newInstance(CurrentResultPage,QueryKeys);
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.ResultPage_FrameLayout, pagesFragment).addToBackStack(null);
                    ft.commit();

                    if (CurrentPageNumber == 1) {
                        PreviousPage.setClickable(false);
                    }
                }
            }
        });
        NextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentPageNumber<= TotalPagesNumber) {
                    CurrentPageNumber += 1;
                    if(CurrentPageNumber>1)
                    {
                        PreviousPage.setClickable(true);
                    }
                    if(CurrentPageNumber>0)
                        previousLimit = CurrentPageNumber-1;
                    CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
                    CurrentResultPage = new ArrayList<>();
                    CurrentResultPage.addAll(ResultsList.subList(previousLimit*10,CurrentPageNumber*10));
                    pagesFragment = ResultsPageFragment.newInstance(CurrentResultPage,QueryKeys);
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.ResultPage_FrameLayout, pagesFragment).addToBackStack(null);
                    ft.commit();

                    if(CurrentPageNumber==TotalPagesNumber)
                    {
                        NextPage.setClickable(false);
                    }
                }
            }
        });
    }

}
