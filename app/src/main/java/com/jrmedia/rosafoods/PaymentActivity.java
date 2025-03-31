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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    TextView mTotal;
    Button payBtn;
    double amount = 0.0;
    String name="";
    String img_url="";
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        amount = getIntent().getDoubleExtra("amount", 0.0);
        name = getIntent().getStringExtra("name");
        img_url = getIntent().getStringExtra("img_url");
        mStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mTotal = findViewById(R.id.sub_total);
        payBtn = findViewById(R.id.pay_btn);

        // Formatando o valor para o formato de moeda do Brasil (R$)
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        mTotal.setText(currencyFormat.format(amount));

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentActivity.this, "Pagamento processado!", Toast.LENGTH_SHORT).show();
                Map<String,Object> mMap = new HashMap<>();
                mMap.put("name", name);
                mMap.put("img_url", img_url);
                mMap.put("payment_id",address);

                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Intent intent=new Intent(PaymentActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}