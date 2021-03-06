package edu.csumb.cst438.router;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import java.util.ArrayList;


public class MyRoutes extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FloatingSearchView mSearchView;
    private TextView noDataMyRoutes;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private RoutesServices routesServices = Application.routesService;
    private ArrayList<Route> displayedRoutes;
    private ArrayList<Route> localRoutes;
    private Connector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routes);
        mConnector = new Connector();
        localRoutes = new ArrayList(routesServices.getAllLocalRoutes());
        localRoutes.addAll(mConnector.getRoutesShared());
        displayedRoutes = new ArrayList(localRoutes);
        noDataMyRoutes = (TextView) findViewById(R.id.no_data_myRoutes);
        mRecyclerView = (RecyclerView) findViewById(R.id.myRoutes_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter= new SwipeRecyclerViewAdapter(this, displayedRoutes);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.myRoutes_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.myRoutes_left_drawer);
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menu_string_array)));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        mSearchView.setShowSearchKey(true);

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {}

            @Override
            public void onSearchAction(String query) {
                displayedRoutes.clear();
                displayedRoutes.addAll(SearchEngine.findRoutes(query, localRoutes));
                mRecyclerAdapter.notifyDataSetChanged();
                Log.d("MyRoutes", "onSearchAction completed");
            }
        });
        Log.d("MyRoutes", "onCreate completed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && requestCode == 1) {
            mConnector.shareRoute(Integer.parseInt(data.getStringExtra("friendId")), routesServices.getRouteById(data.getIntExtra("routeId", -1)));
            Toast.makeText(this, "This route has been shared!", Toast.LENGTH_SHORT).show();
        }
        Log.d("MyRoutes", "onActivityResult completed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRecyclerAdapter.notifyDataSetChanged();
        Log.d("MyRoutes", "onStart completed");
    }
}
