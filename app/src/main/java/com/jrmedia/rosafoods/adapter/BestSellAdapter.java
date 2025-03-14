package com.jrmedia.rosafoods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.BestSell;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BestSellAdapter extends RecyclerView.Adapter<BestSellAdapter.ViewHolder> {
    Context context;
    List<BestSell> mBestSellList;
    public BestSellAdapter(Context context, List<BestSell> mBestSellList) {
        this.context=context;
        this.mBestSellList=mBestSellList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_bestsell_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mName.setText(mBestSellList.get(position).getName());
        /*holder.mPrice.setText(mBestSellList.get(position).getPrice()+" $");*/
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); //Conversão para Real Brasil
        holder.mPrice.setText(format.format(mBestSellList.get(position).getPrice()));
        /*Glide.with(context).load(mBestSellList.get(position).getImg_url()).into(holder.mImage);*/
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
            Glide.with(context).load(mBestSellList.get(currentPosition).getImg_url()).into(holder.mImage);
        }
    }

    @Override
    public int getItemCount() {
        return mBestSellList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mPrice;
        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.bs_img);
            mPrice=itemView.findViewById(R.id.bs_cost);
            mName=itemView.findViewById(R.id.bs_name);
        }
    }
}
