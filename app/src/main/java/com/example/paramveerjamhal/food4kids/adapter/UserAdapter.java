package com.example.paramveerjamhal.food4kids.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.UserListActivity;
import com.example.paramveerjamhal.food4kids.UserParticipationInformation;
import com.example.paramveerjamhal.food4kids.entities.UserWithEventTAsk;
import com.example.paramveerjamhal.food4kids.network.ApiService;

import java.io.Serializable;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final String TAG ="event Adapater" ;
    public  static String DELETE_TAG = "event_delete";
    private List<UserWithEventTAsk> mUserWithEventTAsks;
    private Activity mActivity;
    UserListActivity userListActivity;

    TokenManager tokenManager;
    ApiService service;
    AwesomeValidation validator;
    ProgressDialog m_Dialog;
    String eventType;


    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(Activity activity, List<UserWithEventTAsk> myDataset) {
        mUserWithEventTAsks = myDataset;
        mActivity = activity;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView username;
        public TextView status;
        TextView mobile;
        LinearLayout layout_approve;
        IOnRecycleViewItemClick mOnRecycleViewItemClick;

        ViewHolder(View v, IOnRecycleViewItemClick onRecycleViewItemClick) {
            super(v);
            mOnRecycleViewItemClick = onRecycleViewItemClick;
            username=(TextView)v.findViewById(R.id.tv_username);
            status=(TextView)v.findViewById(R.id.tv_statusA);
            mobile = (TextView) v.findViewById(R.id.tv_setmobile);
            layout_approve=(LinearLayout)v.findViewById(R.id.layout_approve);
            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            mOnRecycleViewItemClick.onRecyclerViewItemCLick(v,getAdapterPosition());
        }



        public static interface IOnRecycleViewItemClick {
            public void onRecyclerViewItemCLick(View view, int position);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.useradapter_layout, parent, false);
  // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v,new ViewHolder.IOnRecycleViewItemClick() {


            @Override
            public void onRecyclerViewItemCLick(View view, int position) {
                Log.e("item click",""+position);
                Intent intent = new Intent(mActivity, UserParticipationInformation.class);
                int event_id=mUserWithEventTAsks.get(position).getEvent_id();
                int user_id=mUserWithEventTAsks.get(position).getId();
                intent.putExtra("user_list", (Serializable) mUserWithEventTAsks);
                intent.putExtra("user_id",user_id);

               /* intent.putExtra("event_id", mEventBeanList.get(position).getEventId());
                intent.putExtra("event_Type",mEventBeanList.get(position).getEventType());
                intent.putExtra("event_name", mEventBeanList.get(position).getEventTitle());
                intent.putExtra("event_desc", mEventBeanList.get(position).getEventDescription());
                intent.putExtra("event_date", mEventBeanList.get(position).getEvent_Date());
                intent.putExtra("event_address", mEventBeanList.get(position).getEventAddress());
                intent.putExtra("event_postal", mEventBeanList.get(position).getPostal_code());
                intent.putExtra("event_organizer", mEventBeanList.get(position).getEvent_Organizer());
*//*
                mActivity.startActivity(intent);
               *//* Event event=mEventBeanList.get(position);
                eventsList= new EventListActivity(mActivity,event);*//*
              //
*/
                mActivity.startActivity(intent);
                mActivity.finish();
            }
        });
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final UserWithEventTAsk userWithEventTAsk = mUserWithEventTAsks.get(position);
       if(userWithEventTAsk!=null) {
           holder.username.setText(String.valueOf(userWithEventTAsk.getName()));
           if(userWithEventTAsk.getAdmin_approveStatus()==1)
           {
               holder.status.setText("Approved");
               holder.layout_approve.setBackgroundResource(R.drawable.button_rounded_corners_gradient);
           }
           else
           {
               holder.status.setText("Approval Pending");
               holder.layout_approve.setBackgroundResource(R.drawable.button_rounded_corners_waiting);
           }

           holder.mobile.setText(String.valueOf(userWithEventTAsk.getMobile()));
       }
    }

    // Return the size of your dataset (invoked by the layout manager)

    @Override
    public int getItemCount() {
        return mUserWithEventTAsks.size();
    }

} 