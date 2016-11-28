package edu.csumb.cst438.router;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    private RoutesServices routesServices;
    private ArrayList<Route> displayedRoutes;
    private ArrayList<Route> localRoutes;
    private Connector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routes);
        mConnector = new Connector();
        //routesServices = new RoutesServices(******); TODO get the user's saved routes from DB
        routesServices = new RoutesServices();
        localRoutes = new ArrayList(routesServices.getAllLocalRoutes());
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

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {}

            @Override
            public void onSearchAction(String query) {
                displayedRoutes.clear();
                displayedRoutes.addAll(SearchEngine.findRoutes(query, localRoutes));
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRecyclerAdapter.notifyDataSetChanged();
    }
}
