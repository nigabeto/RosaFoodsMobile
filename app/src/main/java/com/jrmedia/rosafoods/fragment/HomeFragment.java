package com.jrmedia.rosafoods.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
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




    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    /*public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            mStore=FirebaseFirestore.getInstance();
            mSeeAll=view.findViewById(R.id.see_all);
            mFeature=view.findViewById(R.id.fea_see_all);
            mBestSell=view.findViewById(R.id.best_sell);
            mCatRecyclerView=view.findViewById(R.id.category_recycler);
            mFeatureRecyclerView=view.findViewById(R.id.feature_recycler);
            mBestSellRecyclerView=view.findViewById(R.id.bestsell_recycler);
            //For Category
            mCategoryList=new ArrayList<>();
            /*mCategoryAdapter=new CategoryAdapter(getContext(),mCategoryList);*/
            mCategoryAdapter = new CategoryAdapter(requireContext(), mCategoryList);
            mCatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
            mCatRecyclerView.setAdapter(mCategoryAdapter);

            //For Feature
            mFeatureList=new ArrayList<>();
            /*mFeatureAdapter=new FeatureAdapter(getContext(),mFeatureList);*/
            mFeatureAdapter = new FeatureAdapter(requireContext(), mFeatureList);
            mFeatureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
            mFeatureRecyclerView.setAdapter(mFeatureAdapter);

            //For BestSell
            mBestSellList=new ArrayList<>();
            /*mBestSellAdapter=new BestSellAdapter(getContext(),mBestSellList);*/
            mBestSellAdapter = new BestSellAdapter(requireContext(), mBestSellList);
            mBestSellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
            mBestSellRecyclerView.setAdapter(mBestSellAdapter);


            mStore.collection("Category")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Category category=document.toObject(Category.class);
                                    mCategoryList.add(category);

                                }
                                mCategoryAdapter.notifyDataSetChanged();
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

            mStore.collection("Feature")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Feature feature=document.toObject(Feature.class);
                                    mFeatureList.add(feature);

                                }
                                mFeatureAdapter.notifyDataSetChanged();
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

            mStore.collection("BestSell")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    BestSell bestSell=document.toObject(BestSell.class);
                                    mBestSellList.add(bestSell);

                                }
                                mBestSellAdapter.notifyDataSetChanged();
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        mSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });
        mBestSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });
        mFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}