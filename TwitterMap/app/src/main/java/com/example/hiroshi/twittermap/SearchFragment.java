package com.example.hiroshi.twittermap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

public class SearchFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // search word
    EditText searchText;
    // list for result of searching
    ListView lv;
    // search button
    ImageButton searchBtn;

    public SearchFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SearchFragment newInstance(int sectionNumber) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchText = (EditText)rootView.findViewById(R.id.searchText);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        searchBtn = (ImageButton)rootView.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // search words
                String words = searchText.getText().toString();

                // search
                final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                        .query(words)
                        .build();
                final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getContext())
                        .setTimeline(searchTimeline)
                        .build();

                // show the result in the list view
                lv = (ListView) getView().findViewById(android.R.id.list);
                lv.setAdapter(adapter);
            }
        });
        return rootView;
    }
}
