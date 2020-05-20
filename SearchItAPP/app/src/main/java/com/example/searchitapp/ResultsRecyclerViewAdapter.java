package com.example.searchitapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchitapp.models.QueryResultItem;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder>
{
        @NonNull
        private ArrayList<QueryResultItem> Results = new ArrayList<>();
        private Context mContext;
        private ArrayList<String>QueryKeys;
        private ResultsRecyclerViewAdapter.customItemCLickListener customItemCLickListener;

        public ResultsRecyclerViewAdapter(@NonNull ArrayList<QueryResultItem> results,ArrayList<String> QueryKeys, Context mContext, customItemCLickListener customItemCLickListener) {
            this.Results=results;
            this.QueryKeys=QueryKeys;
            this.mContext = mContext;
            this.customItemCLickListener = customItemCLickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_item, viewGroup, false);
            ViewHolder holder = new ViewHolder(view, customItemCLickListener);
            holder.PageTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = holder.PageLink.getText().toString();
                    if (!url.startsWith("https://") && !url.startsWith("http://")){
                        url = "http://" + url;
                    }
                    Log.d(TAG, "onClick: "+url);
                    Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    openUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mContext,openUrlIntent,null);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            String str = Results.get(i).getAboutPage();
            SpannableString spannableString = new SpannableString(str);
            for(String Key :QueryKeys)
            {
                int index = str.indexOf(Key);
                while (index >= 0) {
                    int end = index + Key.length();
                    spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), index, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    index = str.indexOf(Key, index + 1);
                }
            }
            viewHolder.PageAbout.setText(spannableString);
            viewHolder.PageTitle.setText(Results.get(i).getPageTitle());
            viewHolder.PageLink.setText(Results.get(i).getPageUrl());

        }


        @Override
        public int getItemCount() {

            return Results.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView PageTitle;
            customItemCLickListener customItemCLickListener;
            TextView PageAbout;
            TextView PageLink;


            public ViewHolder(@NonNull final View itemView, customItemCLickListener customItemCLickListener) {
                super(itemView);
                PageLink = (TextView) itemView.findViewById(R.id.resultLink_textView);
                PageTitle = (TextView) itemView.findViewById(R.id.ResultTitle_textView);
                PageAbout = (TextView) itemView.findViewById(R.id.AboutResult_textView);
                this.customItemCLickListener = customItemCLickListener;
                itemView.setOnClickListener(this);

            }


            @Override
            public void onClick(View view) {
                customItemCLickListener.onItemClick(view,getAdapterPosition());
            }

        }

        public interface customItemCLickListener
        {
            public void onItemClick(View v, int position);
        }
    }

