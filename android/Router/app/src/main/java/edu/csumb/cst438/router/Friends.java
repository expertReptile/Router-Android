package edu.csumb.cst438.router;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FloatingSearchView mSearchView;
    private TextView noData;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;

    private String friends[];
    private ArrayList<User> allFriends;
    private ArrayList<User> displayedFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        //      TODO: make proper call to DB to get the list of the User's friends

        //allFriends = new ArrayList(/*make call to get all of the users friends from the DB*/);
        User testUser1 = new User("Friend1", "test bio1", "test email1", "1");
        User testUser2 = new User("Friend2", "test bio2", "test email2", "2");
        User testUser3 = new User("Friend3", "test bio3", "test email3", "3");
        User testUser4 = new User("Friend4", "test bio4", "test email4", "4");
        allFriends = new ArrayList();
        allFriends.add(testUser1);
        allFriends.add(testUser2);
        allFriends.add(testUser3);
        allFriends.add(testUser4);

        displayedFriends = new ArrayList(allFriends);

        noData = (TextView) findViewById(R.id.no_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.friends_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter= new FriendsSwipeRecyclerViewAdapter(this, displayedFriends);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.friends_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.friends_left_drawer);
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menu_string_array)));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {}

            @Override
            public void onSearchAction(String query) {
                displayedFriends.clear();
                displayedFriends.addAll(SearchEngine.findFriends(query, allFriends));
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });

    }
}
