package com.example.searchitapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchitapp.models.QueryResultItem;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultsPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsPageFragment extends Fragment  implements ResultsRecyclerViewAdapter.customItemCLickListener   {

    private static final String ARG_PARAM1 = "PagesList";
    private static final String ARG_PARAM2 = "QueryKeys";

    private ResultsRecyclerViewAdapter ResultsAdapter;
    private RecyclerView ResultRecyclerView;
    private ArrayList<QueryResultItem> ResultsList;//list of 10 or less result to display.
    private ArrayList<String>QueryKeys;


    public ResultsPageFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagesList Parameter 1.
     * @return A new instance of fragment com.example.ResultsPageFragment.
     */
    public static ResultsPageFragment newInstance(ArrayList<QueryResultItem> pagesList,ArrayList<String>QueryKeys) {
        ResultsPageFragment fragment = new ResultsPageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1,  pagesList);
        args.putStringArrayList(ARG_PARAM2,  QueryKeys);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResultsList = getArguments().getParcelableArrayList(ARG_PARAM1);
            QueryKeys = getArguments().getStringArrayList(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_results_page, container, false);
        ResultRecyclerView = (RecyclerView) view.findViewById(R.id.Results_recyclerView);
        ResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        ResultsAdapter = new ResultsRecyclerViewAdapter(ResultsList,QueryKeys,getContext().getApplicationContext(),this);
        ResultRecyclerView.setAdapter(ResultsAdapter);
        ResultRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }


    @Override
    public void onItemClick(View v, int position) {

    }
}
