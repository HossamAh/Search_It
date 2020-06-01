package com.example.searchitapp;

import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.example.searchitapp.models.QueryResultItem;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.Socket;
import java.util.ArrayList;

import java.util.List;


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
    private int nextLimit;
    private ArrayList<String> QueryKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ResultsNumber = (TextView)findViewById(R.id.result_number_textView);
        CurrentPage = (TextView)findViewById(R.id.CurrentPAge_textView);
        NextPage =(ImageButton)findViewById(R.id.Next_Button);
        PreviousPage = (ImageButton)findViewById(R.id.Previous_Button);

        ResultsList =new ArrayList<>();
        ArrayList<String> ResultsString = getIntent().getStringArrayListExtra("Results");
        for (int k=0;k<=ResultsString.size()-2;k+=3)
        {
            QueryResultItem tempItem1 =new QueryResultItem(0,ResultsString.get(k),
                    ResultsString.get(k+1),ResultsString.get(k+2),null,null);
            ResultsList.add(tempItem1);
        }
//
//        QueryResultItem ResultItem1 =new QueryResultItem(0,"Facebook|Login or sign up",
//                "https://www.facebook.com/","facebook about About FACEBOOK",null,null);
//        QueryResultItem ResultItem2 =new QueryResultItem(0,"Twitter|Login or sign up",
//                "https://www.twitter.com/","twitter about twitter again",null,null);
//        QueryResultItem ResultItem3 =new QueryResultItem(0,"Instagram|Login or sign up",
//                "https://www.Instagram.com/","instagram about instagram again",null,null);
//        for (int k=0;k<10;k++)
//        {
//            ResultsList.add(ResultItem2);
//        }
//        for (int k=0;k<2;k++)
//        {
//            ResultsList.add(ResultItem1);
//        }
        QueryKeys =new ArrayList<>();
        QueryKeys = getIntent().getStringArrayListExtra("QueryKeys");
        TotalPagesNumber= (int) Math.ceil((ResultsList.size()/10.0));

        ResultsNumber.setText("results contain "+ResultsList.size()+" pages");
        CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
        CurrentResultPage = new ArrayList<>();
        if(ResultsList.size()<10)
        {
            CurrentResultPage.addAll(ResultsList);

        }
        else
            CurrentResultPage.addAll(ResultsList.subList(0,CurrentPageNumber*10));

        pagesFragment = ResultsPageFragment.newInstance(CurrentResultPage,QueryKeys);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.ResultPage_FrameLayout, pagesFragment);
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
                    if(previousLimit*10 +10 <= ResultsList.size())
                    {
                        nextLimit = previousLimit*10 +10;
                    }
                    else
                        {
                            nextLimit = (ResultsList.size()-previousLimit*10)+previousLimit*10;
                        }
                    CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
                    CurrentResultPage = new ArrayList<>();
                    Log.e("TAG", "onClick: results size"+ResultsList.size());
                    Log.e("TAG", "onClick: previous limit"+previousLimit*10);
                    Log.e("TAG", "onClick: next limit"+nextLimit);
                    CurrentResultPage.addAll(ResultsList.subList(previousLimit*10, nextLimit));
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
                    if(previousLimit*10 +10 <= ResultsList.size())
                    {
                        nextLimit = previousLimit*10 +10;
                    }
                    else
                    {
                        nextLimit = (ResultsList.size()-previousLimit*10)+previousLimit*10;
                    }
                    CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
                    CurrentResultPage = new ArrayList<>();
                    Log.e("TAG", "onClick: results size"+ResultsList.size());
                    Log.e("TAG", "onClick: previous limit"+previousLimit*10);
                    Log.e("TAG", "onClick: next limit"+nextLimit);

                    CurrentResultPage.addAll(ResultsList.subList(previousLimit*10,nextLimit));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ResultsList.clear();
        startActivity(new Intent(this,MainActivity.class));
    }
}

