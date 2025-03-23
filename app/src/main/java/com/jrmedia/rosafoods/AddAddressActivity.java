package com.jrmedia.rosafoods;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private EditText mName, mCity, mAddress, mCode, mNumber;
    private Button mAddAddressbtn;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private RequestQueue requestQueue; // Fila de requisição para Volley
    private Handler cepHandler = new Handler();
    private Runnable cepRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);

        mName = findViewById(R.id.ad_name);
        mCity = findViewById(R.id.ad_city);
        mAddress = findViewById(R.id.ad_address);
        mCode = findViewById(R.id.ad_code);
        mNumber = findViewById(R.id.ad_phone);
        mAddAddressbtn = findViewById(R.id.ad_add_address);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(this); // Inicializando Volley
        formatarTelefone();

        // Adiciona listener para buscar CEP
        mCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cep = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

                // Cancela qualquer verificação anterior
                if (cepRunnable != null) {
                    cepHandler.removeCallbacks(cepRunnable);
                }

                cepRunnable = () -> {
                    if (cep.length() == 8) { // Valida se o CEP tem exatamente 8 dígitos
                        buscarEndereco(cep);
                    } else if (cep.length() > 0 && cep.length() != 8) {
                        Toast.makeText(AddAddressActivity.this, "CEP Incorreto", Toast.LENGTH_SHORT).show();
                    }
                };

                // Aguarda 500ms antes de validar
                cepHandler.postDelayed(cepRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mAddAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString().trim();
                String city = mCity.getText().toString().trim();
                String address = mAddress.getText().toString().trim();
                String code = mCode.getText().toString().trim();
                String number = mNumber.getText().toString().trim();

                // Validação dos campos obrigatórios
                if (code.isEmpty()) {
                    mCode.setError("Campo obrigatório");
                    mCode.requestFocus();
                    return;
                }
                if (name.isEmpty()) {
                    mName.setError("Campo obrigatório");
                    mName.requestFocus();
                    return;
                }
                if (number.isEmpty()) {
                    mNumber.setError("Campo obrigatório");
                    mNumber.requestFocus();
                    return;
                }

                // Se passou pelas validações, continua normalmente
                String final_address = name + ", " + city + ", " + address + ", " + code + ", " + number;

                Map<String, String> mMap = new HashMap<>();
                mMap.put("address", final_address);

                mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddAddressActivity.this, "Endereço Adicionado", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
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

    private void buscarEndereco(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("erro")) { // Verifica se o CEP é válido
                                String logradouro = response.getString("logradouro");
                                String localidade = response.getString("localidade");

                                mAddress.setText(logradouro);
                                mCity.setText(localidade);
                            } else {
                                Toast.makeText(AddAddressActivity.this, "CEP inválido!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("ViaCEP", "Erro ao converter JSON", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ViaCEP", "Erro na requisição", error);
                        Toast.makeText(AddAddressActivity.this, "Erro ao buscar endereço!", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request); // Adiciona a requisição na fila
    }

    private void formatarTelefone() {
        mNumber.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;
            private String oldText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String digits = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
                String formatted = "";

                if (digits.length() > 0) {
                    formatted += "(" + digits.substring(0, Math.min(2, digits.length()));
                }
                if (digits.length() > 2) {
                    formatted += ") " + digits.substring(2, Math.min(7, digits.length()));
                }
                if (digits.length() > 7) {
                    formatted += "-" + digits.substring(7, Math.min(11, digits.length()));
                }

                isUpdating = true;
                mNumber.setText(formatted);
                mNumber.setSelection(formatted.length()); // Mantém o cursor no final
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

}
