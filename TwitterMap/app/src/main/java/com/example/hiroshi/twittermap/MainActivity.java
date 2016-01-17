package com.example.hiroshi.twittermap;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit.http.GET;
import retrofit.http.Query;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xJsMFyTrRKvw9zbMMrHqGLgry";
    private static final String TWITTER_SECRET = "AMXAhKvuQxiPJkLNzKoFiEv2t6uc1KvKTM3fqMgG82LQzt3X2v";

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    static private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    static private ViewPager mViewPager;

    // authorized button for logging in twitter
    static TwitterLoginButton loginButton;
    // tab (main, map, search tab)
    static TabLayout tabLayout;

    // mapfragment for google map
    static com.google.android.gms.maps.MapFragment map;

    // save data from twitter
    // lat : latitude
    // lon : longtitude
    // text : twiiter text
    // date : tweet date
    static ArrayList<Double> lat = new ArrayList<>();
    static ArrayList<Double> lon = new ArrayList<>();
    static ArrayList<String> text = new ArrayList<>();
    static ArrayList<String> date = new ArrayList<>();

    // To judge that user opens this app once or not
    static boolean flg = false;

    // Twitter's account name
    static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // set up the tab
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // google map
        map = (com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onresume");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onresume");
    }

    protected void onDestroy() {
        System.exit(RESULT_OK);
        Log.d("lifecycle", "ondestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        TextView mTextView;
        long myId;
        ListView lv;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            loginButton = (TwitterLoginButton) rootView.findViewById(R.id.login_button);
            mTextView = (TextView) rootView.findViewById(R.id.twitter_handle);

            // call back for logging in button (authorization)
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // Do something with result, which provides a TwitterSession for making API calls

                    userName = result.data.getUserName(); //the user name
                    myId = result.data.getId(); // the user id

                    // call back for getting twitter information
                    Callback<List<Tweet>> cb = new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {

                            int cnt = 0;

                            // time parse
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

                            // to get data from json
                            for (int i = 0; i < result.data.size(); i++) {
                                if (result.data.get(i).place != null) {
                                    lat.add(cnt, result.data.get(i).place.boundingBox.coordinates.get(0).get(0).get(1));
                                    lon.add(cnt, result.data.get(i).place.boundingBox.coordinates.get(0).get(0).get(0));
                                    text.add(cnt, result.data.get(i).text);
                                    date.add(cnt, dateFormat.format(new Date(Date.parse(result.data.get(i).createdAt))));
                                    cnt++;
                                }
                            }

                            // renew the fragment
                            mSectionsPagerAdapter.notifyDataSetChanged();
                            flg = true;
                            SectionsPagerAdapter sectionsPagerAdapter1 = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
                            mViewPager.setAdapter(sectionsPagerAdapter1);
                            tabLayout.setupWithViewPager(mViewPager);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            Log.d("twittercommunity", "exception is " + exception);
                        }
                    };

                    // execute for getting twitter information function
                    TwitterSession session = Twitter.getSessionManager().getActiveSession();
                    new MyTwitterApiClient(session).getGeoService().show(userName, cb);

                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                }
            });

            // hold the listview of fragment_main
            if(flg){
                // timeline
                UserTimeline userTimeline = new UserTimeline.Builder()
                        .screenName(userName)
                        .build();
                TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(loginButton.getContext())
                        .setTimeline(userTimeline)
                        .build();
                mTextView.setText("Logged in with @" + userName);
                lv = (ListView) rootView.findViewById(android.R.id.list);
                lv.setAdapter(adapter);
            }

            return rootView;
        }

        // twitter login
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    // class of getting twitter information
    static class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        /**
         * Provide CustomService with defined endpoints
         */
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }

        public GeoService getGeoService() {
            return getService(GeoService.class);
        }
    }

    // to get user's information (query)
    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long userId,
                  @Query("screen_name") String screenName,
                  @Query("include_entities") Boolean includeEntities,
                  Callback<User> cb);
    }

    // to get geo information (query)
    interface GeoService {
        @GET("/1.1/statuses/user_timeline.json")
        void show(@Query("screen_name") String screenName,
                  Callback<List<Tweet>> cb);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    static public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // an order of tabs
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return MapFragment.newInstance(position + 1, lat, lon, text, date);
                default:
                    return SearchFragment.newInstance(position + 1);
            }
        }

        // an amount of tabs
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        // names of tabs
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MAIN";
                case 1:
                    return "MAP";
                case 2:
                    return "SEARCH";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    // able to tweet from tweet button
    public void tweet(View v) {
        Intent intent = new TweetComposer.Builder(this)
                .createIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
