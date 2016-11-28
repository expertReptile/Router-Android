package edu.csumb.cst438.router;

import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
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
    private ArrayList<User> allFriends;
    private ArrayList<User> displayedFriends;
    private Connector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        mConnector = new Connector();
        allFriends = new ArrayList(mConnector.getFriends());
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
            public void onSearchAction(final String query) {
                setDisplayedFriends(SearchEngine.findFriends(query, allFriends));
                if(displayedFriends.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(findViewById(R.id.friends_drawer_layout).getContext());
                    builder.setTitle("Would you like to search for new friends with this name?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((FriendsSwipeRecyclerViewAdapter)mRecyclerAdapter).setIsAdd(true);
                            setDisplayedFriends(mConnector.searchFriends(query));
                            mRecyclerAdapter.notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }

                setDisplayedFriends(allFriends);
                mRecyclerAdapter.notifyDataSetChanged();
                ((FriendsSwipeRecyclerViewAdapter)mRecyclerAdapter).setIsAdd(false);
            }
        });

    }

    public void setDisplayedFriends(ArrayList<User> newFriends) {
        this.displayedFriends.clear();
        this.displayedFriends.addAll(newFriends);
    }
}
