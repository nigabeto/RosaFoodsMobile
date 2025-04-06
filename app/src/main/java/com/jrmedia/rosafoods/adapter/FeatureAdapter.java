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
import com.jrmedia.rosafoods.domain.Feature;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {
    Context context;
    List<Feature> mFeatureList;

    public FeatureAdapter(Context context, List<Feature> mFeatureList) {
        this.context = context;
        this.mFeatureList = mFeatureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_feature_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); //Conversão para Real Brasil
        holder.mFeatCost.setText(format.format(mFeatureList.get(position).getPrice()));
        holder.mFeatName.setText(mFeatureList.get(position).getName());
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
            Glide.with(context).load(mFeatureList.get(currentPosition).getImg_url()).into(holder.mFeatImage);
        }

        holder.mFeatImage.setOnClickListener(view -> {
            int currentPosition1 = holder.getAdapterPosition(); // Pega a posição correta do item
            if (currentPosition1 != RecyclerView.NO_POSITION) { // Verifica se a posição é válida
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", mFeatureList.get(currentPosition1));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeatureList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mFeatImage;
        private final TextView mFeatCost;
        private final TextView mFeatName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFeatImage = itemView.findViewById(R.id.f_img);
            mFeatCost = itemView.findViewById(R.id.f_cost);
            mFeatName = itemView.findViewById(R.id.f_name);
        }
    }
}
