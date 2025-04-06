package com.jrmedia.rosafoods.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jrmedia.rosafoods.ItemsActivity;
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.adapter.BestSellAdapter;
import com.jrmedia.rosafoods.adapter.CategoryAdapter;
import com.jrmedia.rosafoods.adapter.FeatureAdapter;
import com.jrmedia.rosafoods.domain.BestSell;
import com.jrmedia.rosafoods.domain.Category;
import com.jrmedia.rosafoods.domain.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore mStore;
    //Category Tab
    private List<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mCatRecyclerView;
    //Feature Tab
    private List<Feature> mFeatureList;
    private FeatureAdapter mFeatureAdapter;
    private RecyclerView mFeatureRecyclerView;
    //BestSell Tab
    private List<BestSell> mBestSellList;
    private BestSellAdapter mBestSellAdapter;
    private RecyclerView mBestSellRecyclerView;
    private TextView mSeeAll;
    private TextView mFeature;
    private TextView mBestSell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mStore = FirebaseFirestore.getInstance();
        mSeeAll = view.findViewById(R.id.see_all);
        mFeature = view.findViewById(R.id.fea_see_all);
        mBestSell = view.findViewById(R.id.best_sell);
        mCatRecyclerView = view.findViewById(R.id.category_recycler);
        mFeatureRecyclerView = view.findViewById(R.id.feature_recycler);
        mBestSellRecyclerView = view.findViewById(R.id.bestsell_recycler);
        //For Category
        mCategoryList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(requireContext(), mCategoryList);
        mCatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mCatRecyclerView.setAdapter(mCategoryAdapter);

        //For Feature
        mFeatureList = new ArrayList<>();
        mFeatureAdapter = new FeatureAdapter(requireContext(), mFeatureList);
        mFeatureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mFeatureRecyclerView.setAdapter(mFeatureAdapter);

        //For BestSell
        mBestSellList = new ArrayList<>();
        mBestSellAdapter = new BestSellAdapter(requireContext(), mBestSellList);
        mBestSellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mBestSellRecyclerView.setAdapter(mBestSellAdapter);

        mStore.collection("Category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Category category = document.toObject(Category.class);
                            mCategoryList.add(category);
                        }
                        mCategoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

        mStore.collection("Feature")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Feature feature = document.toObject(Feature.class);
                            mFeatureList.add(feature);
                        }
                        mFeatureAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

        mStore.collection("BestSell")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BestSell bestSell = document.toObject(BestSell.class);
                            mBestSellList.add(bestSell);
                        }
                        mBestSellAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        mSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ItemsActivity.class);
            startActivity(intent);
        });
        mBestSell.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ItemsActivity.class);
            startActivity(intent);
        });
        mFeature.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ItemsActivity.class);
            startActivity(intent);
        });
        return view;
    }
}