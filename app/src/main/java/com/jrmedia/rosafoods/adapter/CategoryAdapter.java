package com.jrmedia.rosafoods.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jrmedia.rosafoods.ItemsActivity;
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final Context context;
    private final List<Category> mCategoryList;

    public CategoryAdapter(Context context, List<Category> mCategoryList) {
        this.context = context;
        this.mCategoryList = mCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
            Glide.with(context).load(mCategoryList.get(currentPosition).getImg_url()).into(holder.mTypeImg);
        }
        holder.mTypeImg.setOnClickListener(view -> {
            Intent intent = new Intent(context, ItemsActivity.class);
            intent.putExtra("type", mCategoryList.get(holder.getAdapterPosition()).getType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mTypeImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTypeImg = itemView.findViewById(R.id.category_img);
        }
    }
}
