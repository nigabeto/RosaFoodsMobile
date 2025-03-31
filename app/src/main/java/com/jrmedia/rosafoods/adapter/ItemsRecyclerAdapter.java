package com.jrmedia.rosafoods.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jrmedia.rosafoods.DetailActivity;
import com.jrmedia.rosafoods.HomeActivity;
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.Items;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {
    Context applicationContext;
    List<Items> mItemsList;
    public ItemsRecyclerAdapter(Context applicationContext, List<Items> mItemsList) {
        this.applicationContext=applicationContext;
        this.mItemsList=mItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition == RecyclerView.NO_POSITION) {
            return;
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String formattedPrice = currencyFormat.format(mItemsList.get(currentPosition).getPrice());
        holder.mCost.setText(formattedPrice);
        holder.mName.setText(mItemsList.get(currentPosition).getName());

        if (!(applicationContext instanceof HomeActivity)) {
            Glide.with(applicationContext)
                    .load(mItemsList.get(currentPosition).getImg_url())
                    .into(holder.mItemImage);
        } else {
            holder.mItemImage.setVisibility(View.GONE);
        }

        View.OnClickListener detailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("detail", mItemsList.get(holder.getAdapterPosition()));
                applicationContext.startActivity(intent);
            }
        };

        holder.mItemImage.setOnClickListener(detailClickListener);
        holder.mName.setOnClickListener(detailClickListener);
    }


    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImage;
        private TextView mCost;
        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage=itemView.findViewById(R.id.item_image);
            mCost=itemView.findViewById(R.id.item_cost);
            mName=itemView.findViewById(R.id.item_nam);
        }
    }
}
