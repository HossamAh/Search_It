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
        QueryKeys =new ArrayList<>();
        QueryKeys = getIntent().getStringArrayListExtra("QueryKeys");
        //QueryKeys.add("Shopping");
        /*QueryKeys.add("facebook");
        QueryKeys.add("twitter");
        QueryKeys.add("instagram");*/
       /* Thread resultCollector =
                new Thread(new Runnable() {
                    @Override
                    public void run() {

        try {
           Socket s = new Socket("192.168.194.92",1700);
            Log.d("STATE", "socket established!");
        ObjectInputStream is=new ObjectInputStream(s.getInputStream());
        List<String> pages=null;
        pages=(List<String>) is.readObject();
            s.close();
        for (int k=0;k<=pages.size()-2;k+=3)
            {
                QueryResultItem tempItem1 =new QueryResultItem(0,pages.get(k),
                        pages.get(k+1),pages.get(k+2),null,null);
                ResultsList.add(tempItem1);
            }



        } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
   }});
       resultCollector.start();*/
       /* try {
            resultCollector.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        TotalPagesNumber= (int) Math.ceil((ResultsList.size()/10.0));

        ResultsNumber.setText("results contain "+ResultsList.size()+" pages");
        CurrentPage.setText(CurrentPageNumber+"/"+TotalPagesNumber);
        CurrentResultPage = new ArrayList<>();
        if(ResultsList.size()<10)
        {CurrentResultPage.addAll(ResultsList);
            NextPage.setClickable(false);}
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ResultsList.clear();
        startActivity(new Intent(this,MainActivity.class));
    }
}

