package com.jrmedia.rosafoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
        mStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        itemsList=new ArrayList<>();
        cartRecyclerView=findViewById(R.id.cart_item_container);
        buyItemButton=findViewById(R.id.cart_item_buy_now);
        totalAmount=findViewById(R.id.total_amount);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setHasFixedSize(true);

        /*buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("itemList", (Serializable) itemsList);
                startActivity(intent);
            }
        });*/

        buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalAmountInDouble = 0.0;
                for (Items item : itemsList) {
                    totalAmountInDouble += item.getPrice();
                }
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("itemList", (Serializable) itemsList);
                intent.putExtra("amount", totalAmountInDouble); // Passando o valor total
                startActivity(intent);
            }
        });


        cartItemAdapter = new CartItemAdapter(itemsList, this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        // Substitua o listener de carregamento do carrinho por:
        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            itemsList.clear(); // Limpa a lista antes de adicionar novos itens
                            if(task.getResult() != null){
                                for(DocumentSnapshot doc : task.getResult().getDocuments()){ // Usa getDocuments() em vez de getDocumentChanges()
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
                    }
                });



        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    private void calculateAmount(List<Items> itemsList) {
        double totalAmountInDouble = 0.0;
        for(Items item : itemsList) {
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