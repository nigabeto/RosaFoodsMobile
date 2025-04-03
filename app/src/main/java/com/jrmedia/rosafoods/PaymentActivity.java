
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

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.firebase.firestore.QuerySnapshot;
    import com.jrmedia.rosafoods.domain.Items;

    import java.text.NumberFormat;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;

    public class PaymentActivity extends AppCompatActivity {

        TextView mTotal;
        Button payBtn;
        double amount = 0.0;
        String name = "";
        String img_url = "";
        FirebaseFirestore mStore;
        FirebaseAuth mAuth;
        String address;
        List<Items> itemsList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_payment);

            amount = getIntent().getDoubleExtra("amount", 0.0);
            name = getIntent().getStringExtra("name");
            img_url = getIntent().getStringExtra("img_url");
            itemsList = (ArrayList<Items>) getIntent().getSerializableExtra("itemsList");
            mStore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mTotal = findViewById(R.id.sub_total);
            payBtn = findViewById(R.id.pay_btn);
            address = getIntent().getStringExtra("address");

            if (itemsList != null && itemsList.size() > 0) {
                amount = 0.0;
                for (Items item : itemsList) {
                    amount += item.getPrice();
                }
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                mTotal.setText(currencyFormat.format(amount));
            }else{
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                mTotal.setText(currencyFormat.format(amount));
            }



            payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PaymentActivity.this, "Pagamento processado!", Toast.LENGTH_SHORT).show();
                    if (itemsList!=null && itemsList.size()>0){
                        for (Items items : itemsList) {
                            Map<String, Object> mMap = new HashMap<>();
                            mMap.put("name", items.getName());
                            mMap.put("img_url", items.getImg_url());
                            mMap.put("payment_id", address);

                            mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                    .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Intent intent=new Intent(PaymentActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Cart").document().delete();
                    }else {
                        Map<String,Object> mMap = new HashMap<>();
                        mMap.put("name",name);
                        mMap.put("img_url",img_url);
                        mMap.put("payment_id", address);

                        // Limpeza do carrinho após o pagamento
                        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }

                }
            });

            /*payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PaymentActivity.this, "Pagamento processado!", Toast.LENGTH_SHORT).show();

                    for (Items items : itemsList) {
                        Map<String, Object> mMap = new HashMap<>();
                        mMap.put("name", items.getName());
                        mMap.put("img_url", items.getImg_url());
                        mMap.put("payment_id", address);

                        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Orders").add(mMap);
                    }

                    // Limpeza do carrinho após o pagamento
                    *//*mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().delete();
                                        }
                                        Toast.makeText(PaymentActivity.this, "Carrinho esvaziado com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PaymentActivity.this, "Erro ao limpar o carrinho!", Toast.LENGTH_SHORT).show();
                                    }

                                    // Redireciona para a Home após a limpeza do carrinho
                                    Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });*//*

                    *//*mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> deleteTask) {
                                                    if (deleteTask.isSuccessful()) {
                                                        Toast.makeText(PaymentActivity.this, "Carrinho limpo após pagamento!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(PaymentActivity.this, "Erro ao limpar carrinho: " + deleteTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(PaymentActivity.this, "Erro ao obter o carrinho: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*//*


                    *//*mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Remove cada item individualmente, como no botão de remover do XML
                                            document.getReference().delete().addOnSuccessListener(aVoid ->
                                                    Toast.makeText(PaymentActivity.this, "Item removido com sucesso!", Toast.LENGTH_SHORT).show()
                                            ).addOnFailureListener(e ->
                                                    Toast.makeText(PaymentActivity.this, "Erro ao remover item: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                            );
                                        }
                                        Toast.makeText(PaymentActivity.this, "Carrinho limpo após o pagamento!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PaymentActivity.this, "Erro ao obter o carrinho: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*//*


                    *//*mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot result = task.getResult();
                                        if (result != null && !result.isEmpty()) {
                                            for (DocumentSnapshot document : result.getDocuments()) {
                                                document.getReference().delete()
                                                        .addOnSuccessListener(aVoid ->
                                                                Toast.makeText(PaymentActivity.this, "Item removido: " + document.getId(), Toast.LENGTH_SHORT).show()
                                                        )
                                                        .addOnFailureListener(e ->
                                                                Toast.makeText(PaymentActivity.this, "Erro ao remover item: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                                        );
                                            }
                                            Toast.makeText(PaymentActivity.this, "Carrinho limpo após o pagamento!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PaymentActivity.this, "O carrinho já está vazio.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(PaymentActivity.this, "Erro ao obter o carrinho: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(PaymentActivity.this, "Erro ao acessar o Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );*//*

                }
            });*/


            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
