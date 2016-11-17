package com.mi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mi.activities.Dashboard;
import com.mi.activities.DealActivity;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.test.mypiece.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by MilindVyas on 28-Aug-16.
 */
public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView nameRestaurants;
        TextView campaignOption;
        TextView price;
        TextView dealPrice;
        ImageView imgDeal;


        public MyViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
            nameRestaurants = (TextView) view.findViewById(R.id.nameRestaurants);
            campaignOption = (TextView) view.findViewById(R.id.campaignOption);
            dealPrice = (TextView) view.findViewById(R.id.dealPrice);
            price = (TextView) view.findViewById(R.id.price);
            imgDeal = (ImageView) view.findViewById(R.id.imgDeal);

            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            imgDeal.setScaleType(ImageView.ScaleType.CENTER_CROP);



//            title = (TextView) view.findViewById(R.id.title);
//            count = (TextView) view.findViewById(R.id.count);
//            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public DashboardListAdapter(Context mContext, List<String> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dashboard_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DealActivity.class);
                intent.putExtra("DealPosition", position);
                mContext.startActivity(intent);
            }
        });

        String name = DashBoardAsyncTask.dashboardVector.get(position).getvRestaurantName();
        String price = DashBoardAsyncTask.dashboardVector.get(position).getdOriginPrice();
        String dprice = DashBoardAsyncTask.dashboardVector.get(position).getdPrice();

        if(dprice.contains(".00"))
        {
            dprice = dprice.replace(".00", "");
        }


        holder.nameRestaurants.setText(name);
        holder.campaignOption.setText(DashBoardAsyncTask.dashboardVector.get(position).getvCampaignOption());
        holder.price.setText(price);
        holder.dealPrice.setText(dprice);



        if (holder.imgDeal != null) {
            new ImgDownloaderTask(holder.imgDeal).execute(DashBoardAsyncTask.dashboardVector.get(position).getvImages());
        }

    }

    @Override
    public int getItemCount() {
        return DashBoardAsyncTask.dashboardVector.size();
    }
}
