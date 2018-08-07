package com.example.paramveerjamhal.food4kids.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.paramveerjamhal.food4kids.EventListActivity;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.UserListActivity;
import com.example.paramveerjamhal.food4kids.entities.Event;
import com.example.paramveerjamhal.food4kids.entities.UserWithEventTAsk;
import com.example.paramveerjamhal.food4kids.entities.WeeklyEvent;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

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
        IOnRecycleViewItemClick mOnRecycleViewItemClick;

        public ViewHolder(View v, IOnRecycleViewItemClick onRecycleViewItemClick) {
            super(v);
            mOnRecycleViewItemClick = onRecycleViewItemClick;
            username=(TextView)v.findViewById(R.id.tv_name);
            status=(TextView)v.findViewById(R.id.tv_status);
            mobile = (TextView) v.findViewById(R.id.tv_mobile);
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

        tokenManager = TokenManager.getInstance(parent.getContext().getSharedPreferences("prefs", MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);
        validator=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        m_Dialog = new ProgressDialog(parent.getContext());    // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v,new ViewHolder.IOnRecycleViewItemClick() {


            @Override
            public void onRecyclerViewItemCLick(View view, int position) {
                Log.e("item click",""+position);
                Intent intent = new Intent(mActivity, EventListActivity.class);
                int event_id=mUserWithEventTAsks.get(position).getEvent_id();

               /* intent.putExtra("event_id", mEventBeanList.get(position).getEventId());
                intent.putExtra("event_Type",mEventBeanList.get(position).getEventType());
                intent.putExtra("event_name", mEventBeanList.get(position).getEventTitle());
                intent.putExtra("event_desc", mEventBeanList.get(position).getEventDescription());
                intent.putExtra("event_date", mEventBeanList.get(position).getEvent_Date());
                intent.putExtra("event_address", mEventBeanList.get(position).getEventAddress());
                intent.putExtra("event_postal", mEventBeanList.get(position).getPostal_code());
                intent.putExtra("event_organizer", mEventBeanList.get(position).getEvent_Organizer());
*/
                mActivity.startActivity(intent);
               /* Event event=mEventBeanList.get(position);
                eventsList= new EventListActivity(mActivity,event);*/
              //  mActivity.finish();

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




        holder.username.setText(userWithEventTAsk.getName());
        holder.status.setText(userWithEventTAsk.getAdmin_approveStatus());
        holder.mobile.setText(userWithEventTAsk.getMobile());
    }

    // Return the size of your dataset (invoked by the layout manager)

    @Override
    public int getItemCount() {
        return mUserWithEventTAsks.size();
    }

} 