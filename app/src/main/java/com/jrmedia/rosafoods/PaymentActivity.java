package com.jrmedia.rosafoods;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jrmedia.rosafoods.domain.Items;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    TextView mTotal;
    TextView mTotalGeral;
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
        mTotalGeral = findViewById(R.id.total_amt);
        payBtn = findViewById(R.id.pay_btn);
        address = getIntent().getStringExtra("address");

        if (itemsList != null && !itemsList.isEmpty()) {
            amount = 0.0;
            for (Items item : itemsList) {
                amount += item.getPrice();
            }
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String formattedAmount = currencyFormat.format(amount);
        mTotal.setText(formattedAmount);
        mTotalGeral.setText(formattedAmount);

        payBtn.setOnClickListener(view -> {
            Toast.makeText(PaymentActivity.this, "Pagamento processado!", Toast.LENGTH_LONG).show();

            String userId = mAuth.getCurrentUser().getUid();
            List<Map<String, Object>> pedidos = new ArrayList<>();

            if (itemsList != null && !itemsList.isEmpty()) {
                for (Items item : itemsList) {
                    Map<String, Object> pedido = new HashMap<>();
                    pedido.put("name", item.getName());
                    pedido.put("img_url", item.getImg_url());
                    pedido.put("payment_id", address);
                    pedidos.add(pedido);
                }

                // Grava todos os pedidos
                for (Map<String, Object> pedido : pedidos) {
                    mStore.collection("Users").document(userId)
                            .collection("Orders").add(pedido);
                }

                // Apaga carrinho (se quiser, pode ser feito após todos os pedidos serem salvos)
                mStore.collection("Users").document(userId)
                        .collection("Cart").document().delete();
            } else {
                Map<String, Object> pedido = new HashMap<>();
                pedido.put("name", name);
                pedido.put("img_url", img_url);
                pedido.put("payment_id", address);

                mStore.collection("Users").document(userId)
                        .collection("Orders").add(pedido);
            }

            // Após o pagamento, redireciona para Home
            Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
