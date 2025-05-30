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
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.BestSell;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BestSellAdapter extends RecyclerView.Adapter<BestSellAdapter.ViewHolder> {
    Context context;
    List<BestSell> mBestSellList;

    public BestSellAdapter(Context context, List<BestSell> mBestSellList) {
        this.context = context;
        this.mBestSellList = mBestSellList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_bestsell_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();

        if (currentPosition != RecyclerView.NO_POSITION) {
            BestSell bestSellItem = mBestSellList.get(currentPosition);
            holder.mName.setText(bestSellItem.getName());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            holder.mPrice.setText(format.format(bestSellItem.getPrice()));
            Glide.with(context).load(bestSellItem.getImg_url()).into(holder.mImage);
            holder.mImage.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", bestSellItem);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mBestSellList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImage;
        private final TextView mPrice;
        private final TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.bs_img);
            mPrice = itemView.findViewById(R.id.bs_cost);
            mName = itemView.findViewById(R.id.bs_name);
        }
    }
}
