package com.jrmedia.rosafoods.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jrmedia.rosafoods.DetailActivity;
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.Feature;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {
    Context context;
    List<Feature> mFeatureList;
    public FeatureAdapter(Context context, List<Feature> mFeatureList) {
        this.context=context;
        this.mFeatureList=mFeatureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_feature_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mFeatCost.setText(mFeatureList.get(position).getPrice()+" $");
        holder.mFeatName.setText(mFeatureList.get(position).getName());
        /*Glide.with(context).load(mFeatureList.get(position).getImg_url()).into(holder.mFeatImage);*/
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
            Glide.with(context).load(mFeatureList.get(currentPosition).getImg_url()).into(holder.mFeatImage);
        }
        holder.mFeatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeatureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mFeatImage;
        private TextView mFeatCost;
        private TextView mFeatName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFeatImage=itemView.findViewById(R.id.f_img);
            mFeatCost=itemView.findViewById(R.id.f_cost);
            mFeatName=itemView.findViewById(R.id.f_name);
        }
    }
}
