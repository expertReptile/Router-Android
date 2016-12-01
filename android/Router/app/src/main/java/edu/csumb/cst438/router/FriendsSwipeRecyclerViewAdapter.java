package edu.csumb.cst438.router;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

/**
 * Created by Pearce on 11/22/16.
 */

public class FriendsSwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<FriendsSwipeRecyclerViewAdapter.FriendsSimpleViewHolder> {
    private Context mContext;
    private String rowString;
    private Boolean isAdd;
    private Connector mConnector;
    private ArrayList<User> theArrayList;

    public FriendsSwipeRecyclerViewAdapter(Context context, ArrayList<User> objects) {
        this.mContext = context;
        this.theArrayList = objects;
        this.isAdd = false;
        this.mConnector = new Connector();
    }

    @Override
    public FriendsSimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_swiper, parent, false);
        return new FriendsSimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendsSimpleViewHolder viewHolder, final int position) {
        rowString = theArrayList.get(position).username;
        viewHolder.rowText.setText(rowString);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        if(isAdd) {
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.addFriendsBottom_view));
        } else {
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.friendsBottom_view));
        }

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
                if(((Friends)v.getContext()).getIntent().hasExtra("display")){
                    ((Friends)v.getContext()).finishActivity(1);
                    Intent returnIntent = ((Friends) v.getContext()).getIntent();
                    returnIntent.putExtra("friendId", theArrayList.get(position).id);
                    returnIntent.putExtra("routeId", returnIntent.getIntExtra("routeId", -1));
                    ((Friends)v.getContext()).setResult(1,returnIntent);
                    ((Friends)v.getContext()).finish();
                }
            }
        });

        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnector.addFriend(Integer.parseInt(theArrayList.get(position).id));
                Snackbar snack = Snackbar.make(v, "Friend Request Sent!", Snackbar.LENGTH_LONG);
                View snackView = snack.getView();
                TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.rgb(0,191,255));
                snack.show();
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                String name = theArrayList.get(position).username;
                mConnector.removeFriend(Integer.parseInt(theArrayList.get(position).id));
                theArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, theArrayList.size());
                mItemManger.closeAllItems();
                Snackbar snack = Snackbar.make(view, name + " was removed from friends!", Snackbar.LENGTH_LONG);
                View snackView = snack.getView();
                TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.rgb(0,191,255));
                snack.show();
            }
        });
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return theArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.friendsSwiper;
    }

    public static class FriendsSimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button deleteButton;
        Button addButton;
        TextView rowText;

        public FriendsSimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.friendsSwiper);
            deleteButton = (Button) itemView.findViewById(R.id.friendsDelete);
            addButton = (Button) itemView.findViewById(R.id.friendsAdd);
            rowText = (TextView) itemView.findViewById(R.id.friendsSwiperRow);
        }
    }

    public void setIsAdd(Boolean bool){
        this.isAdd = bool;
    }
}
