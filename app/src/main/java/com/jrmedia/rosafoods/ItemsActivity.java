package com.jrmedia.rosafoods;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jrmedia.rosafoods.adapter.ItemsRecyclerAdapter;
import com.jrmedia.rosafoods.domain.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private FirebaseFirestore mStore;
    List<Items> mItemsList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);
        String type = getIntent().getStringExtra("type");
        mStore = FirebaseFirestore.getInstance();
        mItemsList = new ArrayList<>();
        mToolbar = findViewById(R.id.item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Itens");
        itemRecyclerView = findViewById(R.id.items_recycler);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this, mItemsList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);

        if (type == null || type.isEmpty()) {
            mStore.collection("All").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Log.i("TAG", "onComplete: " + doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if (type != null && type.equalsIgnoreCase("Bebidas")) {
            mStore.collection("All").whereEqualTo("type", "Bebidas").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Log.i("TAG", "onComplete: " + doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if (type != null && type.equalsIgnoreCase("Sobremesa")) {
            mStore.collection("All").whereEqualTo("type", "Sobremesa").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Log.i("TAG", "onComplete: " + doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if (type != null && type.equalsIgnoreCase("Pizza")) {
            mStore.collection("All").whereEqualTo("type", "Pizza").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Log.i("TAG", "onComplete: " + doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if (type != null && type.equalsIgnoreCase("Esfihas")) {
            mStore.collection("All").whereEqualTo("type", "Esfihas").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Log.i("TAG", "onComplete: " + doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_it);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchItem(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchItem(String newText) {
        // Limpar a lista antes da nova busca
        mItemsList.clear();
        // Converter o texto da busca para lowercase
        String searchText = newText.toLowerCase();
        mStore.collection("All")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Log.i("TAG", "onComplete: " + doc.toString());
                            Items items = doc.toObject(Items.class);
                            if (items != null && items.getName().toLowerCase().contains(searchText)) {
                                mItemsList.add(items);
                            }
                        }
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

}