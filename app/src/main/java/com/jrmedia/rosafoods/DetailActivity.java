package com.jrmedia.rosafoods;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jrmedia.rosafoods.domain.BestSell;
import com.jrmedia.rosafoods.domain.Feature;
import com.jrmedia.rosafoods.domain.Items;

public class DetailActivity extends AppCompatActivity {
    private ImageView mImage;
    private TextView mItemName;
    private TextView mPrice;
    private Button mItemRating;
    private TextView mItemRatDesc;
    private TextView mItemDesc;
    private Button mAddToCart;
    private Button mBuyBtn;
    Feature feature = null;
    BestSell bestSell = null;
    Items items=null;
    private Toolbar mToolbar;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        mToolbar=findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImage=findViewById(R.id.item_img);
        mItemName=findViewById(R.id.item_name);
        mPrice=findViewById(R.id.item_price);
        mItemRating=findViewById(R.id.item_rating);
        mItemRatDesc=findViewById(R.id.item_rat_des);
        mItemDesc=findViewById(R.id.item_des);
        mAddToCart=findViewById(R.id.item_add_cart);
        mBuyBtn=findViewById(R.id.item_buy_now);
        mStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        Object obj = getIntent().getSerializableExtra("detail");
        if (obj instanceof Feature){
            feature = (Feature) obj;
        }else if (obj instanceof BestSell){
            bestSell = (BestSell) obj;
        }else if (obj instanceof Items){
            items = (Items) obj;
        }
        if (feature!=null){
            Glide.with(getApplicationContext()).load(feature.getImg_url()).into(mImage);
            mItemName.setText(feature.getName());
            /* mPrice.setText(feature.getPrice()+"$");*/
            // Formatar o preço no formato Real (BRL) com 2 casas decimais
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            mPrice.setText(currencyFormat.format(feature.getPrice()));
            mItemRating.setText(feature.getRating()+"");
            if(feature.getRating()>3){
                mItemRatDesc.setText("Very Good");
            }else{
                mItemRatDesc.setText("Good");
            }
            mItemDesc.setText(feature.getDescription());
        }

        if (bestSell!=null){
            Glide.with(getApplicationContext()).load(bestSell.getImg_url()).into(mImage);
            mItemName.setText(bestSell.getName());
            /* mPrice.setText(bestSell.getPrice()+"$");*/
            // Formatar o preço no formato Real (BRL) com 2 casas decimais
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            mPrice.setText(currencyFormat.format(bestSell.getPrice()));
            mItemRating.setText(bestSell.getRating()+"");
            if(bestSell.getRating()>3){
                mItemRatDesc.setText("Very Good");
            }else{
                mItemRatDesc.setText("Good");
            }
            mItemDesc.setText(bestSell.getDescription());
        }

        if (items!=null){
            Glide.with(getApplicationContext()).load(items.getImg_url()).into(mImage);
            mItemName.setText(items.getName());
            /* mPrice.setText(bestSell.getPrice()+"$");*/
            // Formatar o preço no formato Real (BRL) com 2 casas decimais
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            mPrice.setText(currencyFormat.format(items.getPrice()));
            mItemRating.setText(items.getRating()+"");
            if(items.getRating()>3){
                mItemRatDesc.setText("Very Good");
            }else{
                mItemRatDesc.setText("Good");
            }
            mItemDesc.setText(items.getDescription());
        }

        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(feature!=null){
                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").add(feature).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(bestSell!=null){
                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").add(bestSell).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(items!=null){
                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").add(items).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        mBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailActivity.this,AddressActivity.class);
                if(feature!=null){
                    intent.putExtra("item", feature);
                }
                if(bestSell!=null){
                    intent.putExtra("item", bestSell);
                }
                if(items!=null){
                    intent.putExtra("item", items);
                }
                startActivity(intent);
            }
        });

        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}