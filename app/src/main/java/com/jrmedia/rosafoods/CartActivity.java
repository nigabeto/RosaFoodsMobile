package com.jrmedia.rosafoods;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jrmedia.rosafoods.adapter.CartItemAdapter;
import com.jrmedia.rosafoods.domain.Items;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.ItemRemoved {
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    List<Items> itemsList;
    RecyclerView cartRecyclerView;
    CartItemAdapter cartItemAdapter;
    Button buyItemButton;
    TextView totalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        itemsList = new ArrayList<>();
        cartRecyclerView = findViewById(R.id.cart_item_container);
        buyItemButton = findViewById(R.id.cart_item_buy_now);
        totalAmount = findViewById(R.id.total_amount);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setHasFixedSize(true);

        buyItemButton.setOnClickListener(view -> {
            double totalAmountInDouble = 0.0;
            for (Items item : itemsList) {
                totalAmountInDouble += item.getPrice();
            }
            Intent intent = new Intent(CartActivity.this, AddressActivity.class);
            intent.putExtra("itemList", (Serializable) itemsList);
            intent.putExtra("amount", totalAmountInDouble); // Passando o valor total
            startActivity(intent);
        });

        cartItemAdapter = new CartItemAdapter(itemsList, this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        itemsList.clear(); // Limpa a lista antes de adicionar novos itens
                        if (task.getResult() != null) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                String documentId = doc.getId();
                                Items item = doc.toObject(Items.class);
                                item.setDocId(documentId);
                                itemsList.add(item);
                            }
                            calculateAmount(itemsList);
                            cartItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(CartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void calculateAmount(List<Items> itemsList) {
        double totalAmountInDouble = 0.0;
        for (Items item : itemsList) {
            totalAmountInDouble += item.getPrice();
        }

        // Formatar o valor para o formato de moeda
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        totalAmount.setText("Total: " + currencyFormat.format(totalAmountInDouble));
    }

    @Override
    public void onItemRemoved(List<Items> itemsList) {
        calculateAmount(itemsList);
    }
}