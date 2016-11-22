package edu.csumb.cst438.router;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routes);

        routesServices = new RoutesServices();

            // ****** TODO Remove test data
            Route tempRoute1 = new Route(true, 5, "{1}", "12.12", "12.12", 321, "testRoute1");
            Route tempRoute2 = new Route(true, 6, "{2}", "22.22", "22.22", 321, "testRoute2");
            Route tempRoute3 = new Route(true, 7, "{3}", "32.32", "32.32", 321, "testRote3");
            Route tempRoute4 = new Route(true, 8, "{4}", "42.42", "42.42", 321, "testRoute4");
            routesServices.insertRoute(tempRoute1);
            routesServices.insertRoute(tempRoute2);
            routesServices.insertRoute(tempRoute3);
            routesServices.insertRoute(tempRoute4);
            // ******** end test data

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
                Log.d("search test", "searching");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*routesServices.deleteRoute(5);
        routesServices.deleteRoute(6);
        routesServices.deleteRoute(7);
        routesServices.deleteRoute(8);*/
    }
}
