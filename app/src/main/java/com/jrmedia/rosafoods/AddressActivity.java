package com.jrmedia.rosafoods;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jrmedia.rosafoods.adapter.AddressAdapter;
import com.jrmedia.rosafoods.domain.Address;
import com.jrmedia.rosafoods.domain.BestSell;
import com.jrmedia.rosafoods.domain.Feature;
import com.jrmedia.rosafoods.domain.Items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {
    private RecyclerView mAddressRecyclerView;
    private AddressAdapter mAddressAdapter;
    private Button paymentBtn;
    private Button mAddAddress;
    private List<Address> mAddressList;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
        Object obj = getIntent().getSerializableExtra("item");
        List<Items> itemsList = (ArrayList<Items>) getIntent().getSerializableExtra("itemList");
        mAddressRecyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        mAddAddress = findViewById(R.id.add_address_btn);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.address_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStore = FirebaseFirestore.getInstance();
        mAddressList = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(getApplicationContext(), mAddressList, this);
        mAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAddressRecyclerView.setAdapter(mAddressAdapter);

        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Address address = doc.toObject(Address.class);
                            mAddressList.add(address);
                            mAddressAdapter.notifyDataSetChanged();
                        }
                    }
                });

        mAddAddress.setOnClickListener(view -> {
            Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
            startActivity(intent);
        });
        paymentBtn.setOnClickListener(view -> {
            double amount = 0.0;
            String url = "";
            String name = "";
            if (obj instanceof Feature) {
                Feature f = (Feature) obj;
                amount = f.getPrice();
                url = f.getImg_url();
                name = f.getName();
            }
            if (obj instanceof BestSell) {
                BestSell f = (BestSell) obj;
                amount = f.getPrice();
                url = f.getImg_url();
                name = f.getName();

            }
            if (obj instanceof Items) {
                Items i = (Items) obj;
                amount = i.getPrice();
                url = i.getImg_url();
                name = i.getName();

            }
            if (itemsList != null && itemsList.size() > 0) {
                Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                intent.putExtra("itemList", (Serializable) itemsList);
                intent.putExtra("amount", getIntent().getDoubleExtra("amount", 0.0)); // Repassando o valor total
                startActivity(intent);
            } else {
                Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                intent.putExtra("amount", amount);
                intent.putExtra("img_url", url);
                intent.putExtra("name", name);
                intent.putExtra("address", address);

                startActivity(intent);
            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void setAddress(String s) {
        address = s;
    }
}