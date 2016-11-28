package edu.csumb.cst438.router;

import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import java.util.ArrayList;

import static android.R.color.holo_green_dark;
import static android.R.drawable.btn_plus;

public class Friends extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FloatingSearchView mSearchView;
    private TextView noData;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private UserServices userServices;
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
        User testUser5 = new User("Friend5", "test bio5", "test email5", "5");
        User testUser6 = new User("Friend6", "test bio6", "test email6", "6");
        User testUser7 = new User("Friend7", "test bio7", "test email7", "7");
        User testUser8 = new User("Friend8", "test bio8", "test email8", "8");
        User testUser9 = new User("Friend9", "test bio9", "test email9", "9");
        User testUser10 = new User("Friend10", "test bio10", "test email10", "10");
        allFriends = new ArrayList();
        allFriends.add(testUser1);
        allFriends.add(testUser2);
        allFriends.add(testUser3);
        allFriends.add(testUser4);
        allFriends.add(testUser5);
        allFriends.add(testUser6);
        allFriends.add(testUser7);
        allFriends.add(testUser8);
        allFriends.add(testUser9);
        allFriends.add(testUser10);

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
                setDisplayedFriends(SearchEngine.findFriends(query, allFriends));
                if(displayedFriends.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(findViewById(R.id.friends_drawer_layout).getContext());
                    builder.setTitle("Would you like to search for new friends with this name?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //todo removing testng things here

                            User testNewFriend1 = new User("FriendNEW1", "test bio1", "test email1", "1");
                            User testNewFriend2 = new User("FriendNEW2", "test bio2", "test email2", "2");
                            ArrayList<User> newFriends = new ArrayList();
                            newFriends.add(testNewFriend1);
                            newFriends.add(testNewFriend2);


                            //TODO search remote DB for friends and switch to add friends view

                            setDisplayedFriends(newFriends);
                            mRecyclerAdapter.notifyDataSetChanged();

                            return;
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setDisplayedFriends(allFriends);
                            mRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            }
        });

    }

    public void setDisplayedFriends(ArrayList<User> newFriends) {
        this.displayedFriends.clear();
        this.displayedFriends.addAll(newFriends);
    }
}
