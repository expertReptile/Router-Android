package edu.csumb.cst438.router;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private String rowString;
    private ArrayList<Route> theArrayList;
    private RoutesServices routesServices = new RoutesServices();
    private Connector mConnector;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<Route> objects) {
        this.mContext = context;
        this.theArrayList = objects;
        this.mConnector = new Connector();
        Log.d("SwipeRecyclerAdapter", "SwipeRecyclerViewAdapter constructor completed");
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swiper, parent, false);
        Log.d("SwipeRecyclerAdapter", "onCreateViewHolder completed");
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        rowString = theArrayList.get(position).getRouteName();

        viewHolder.rowText.setText(rowString);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_view));
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
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                Application.currentRoute = theArrayList.get(position);
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                routesServices.deleteRoute(theArrayList.get(position).getRouteName());
                theArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, theArrayList.size());
                mItemManger.closeAllItems();
            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Rename Your Route");

                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String routeName = input.getText().toString();
                        RoutesServices.updateRouteName(routeName, theArrayList.get(position).getRouteName());
                    }
                });

                builder.show();
                notifyItemChanged(position, theArrayList.size());
            }
        });

        viewHolder.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnector.uploadRoute(Integer.parseInt(UserServices.getUserId()), theArrayList.get(position).getStartPointLat(), theArrayList.get(position).getStartPointLon(), theArrayList.get(position).getRouteName(), theArrayList.get(position).getRoute());
                Toast.makeText(view.getContext(), "This route has been uploaded", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Friends.class);
                intent.putExtra("display", "Click a Friend to share your Route with.");
                intent.putExtra("routeId", theArrayList.get(position).getRouteIdRemote());
                ((MyRoutes) mContext).startActivityForResult(intent, 1);
            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
        Log.d("SwipeRecyclerAdapter", "onBindViewHolder completed");
    }

    @Override
    public int getItemCount() {
        Log.d("SwipeRecyclerAdapter", "getItemCount completed");
        return theArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        Log.d("SwipeRecyclerAdapter", "getSwipeLayoutResourceId completed");
        return R.id.swiper;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button deleteButton;
        Button editButton;
        Button uploadButton;
        Button shareButton;
        TextView rowText;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swiper);
            deleteButton = (Button) itemView.findViewById(R.id.Delete);
            editButton = (Button) itemView.findViewById(R.id.Edit);
            uploadButton = (Button) itemView.findViewById(R.id.Upload);
            shareButton = (Button) itemView.findViewById(R.id.Share);
            rowText = (TextView) itemView.findViewById(R.id.swiperRow);
            Log.d("SimpleViewHolder", "SimpleViewHolder constructor completed");
        }
    }
}