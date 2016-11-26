package edu.csumb.cst438.router;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

/**
 * Created by Pearce on 11/9/16.
 */

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {
    private Context mContext;
    private String[] theList;
    private String rowString;
    private String[] theList;
    private ArrayList<Route> theArrayList;
    private RoutesServices routesServices = new RoutesServices();

    public SwipeRecyclerViewAdapter(Context context, String[] objects) {
        this.mContext = context;
        this.theList = objects;
    }

    public SwipeRecyclerViewAdapter(Context context, ArrayList<Route> objects) {
        this.mContext = context;
        this.theArrayList = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swiper, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final String row = theList[position];

        // TODO: update this area to populate the actual row values and not a debug text
        viewHolder.rowText.setText((row) + "  -  Row Position " + position);
        if(theList != null){
            rowString = theList[position];
        } else {
            rowString = theArrayList.get(position).getRouteName();
        }

        viewHolder.rowText.setText(rowString);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_view));


        // TODO implement different actions for the recycler view swiper states and the buttons revealed by the swiper
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                // functionality for when the buttons are hidden
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                // during any movement of the top view
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                // during the movement of the top view from closed state
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                // functionality for when the bottom view is completely visible
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                // during the movement of the top view from opened state
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                // called when mid swipe and user ends touch input
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " onClick : " + row , Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.testButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Clicked on test button 1" + viewHolder.testButton1.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on test button 2" + viewHolder.testButton2.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.testButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on test button 3" + viewHolder.testButton3.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

/*

        Could come in handy as a delete button example

        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                studentList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, studentList.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
*/

        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

/*
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " onClick : " + rowString , Toast.LENGTH_SHORT).show();
                //TODO: implement desired functionality when a row is clicked
            }
        });
*/
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("routeId", theArrayList.get(position).getRouteIdRemote());
                intent.putExtra("route", theArrayList.get(position).getRoute());
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                theArrayList.remove(position);
                routesServices.deleteRoute(theArrayList.get(position).getRouteIdRemote());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, theArrayList.size());
                mItemManger.closeAllItems();
            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //TODO : need dialogue window to edit route info
                Toast.makeText(view.getContext(), "Clicked on " + viewHolder.editButton.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //TODO : implement sharing things
                Toast.makeText(view.getContext(), "Clicked on " + viewHolder.shareButton.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return theList.length;
        if(theList != null) {
            return theList.length;
        } else {
            return theArrayList.size();
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swiper;
    }


    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button testButton1;
        Button testButton2;
        Button testButton3;
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button deleteButton;
        Button editButton;
        Button shareButton;
        TextView rowText;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swiper);
            testButton1 = (Button) itemView.findViewById(R.id.Delete);
            testButton2 = (Button) itemView.findViewById(R.id.Edit);
            testButton3 = (Button) itemView.findViewById(R.id.Share);
            deleteButton = (Button) itemView.findViewById(R.id.Delete);
            editButton = (Button) itemView.findViewById(R.id.Edit);
            shareButton = (Button) itemView.findViewById(R.id.Share);
            rowText = (TextView) itemView.findViewById(R.id.swiperRow);
        }
    }
}
