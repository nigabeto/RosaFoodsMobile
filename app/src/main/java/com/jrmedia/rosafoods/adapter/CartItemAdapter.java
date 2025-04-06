package com.jrmedia.rosafoods.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.Items;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    List<Items> itemsList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ItemRemoved itemRemoved;

    public CartItemAdapter(List<Items> itemsList, ItemRemoved itemRemoved) {
        this.itemsList = itemsList;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.itemRemoved = itemRemoved;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition == RecyclerView.NO_POSITION) {
            return; // Evita continuar se a posição não for válida
        }

        holder.cartName.setText(itemsList.get(currentPosition).getName());
        // Formatar o preço para moeda brasileira
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        double preco = itemsList.get(currentPosition).getPrice(); // Certifique-se de que getPrice() retorna um double
        holder.cartPrice.setText(formatador.format(preco));

        Glide.with(holder.itemView.getContext())
                .load(itemsList.get(currentPosition).getImg_url())
                .into(holder.cartImage);

        holder.removeItem.setOnClickListener(view -> {
            int updatedPosition = holder.getAdapterPosition();
            if (updatedPosition == RecyclerView.NO_POSITION) {
                return;
            }

            firebaseFirestore.collection("Users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .collection("Cart")
                    .document(itemsList.get(updatedPosition).getDocId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            itemsList.remove(updatedPosition);
                            notifyItemRemoved(updatedPosition); // Use notifyItemRemoved em vez de notifyDataSetChanged
                            itemRemoved.onItemRemoved(itemsList);
                            Toast.makeText(holder.itemView.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(holder.itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartName;
        TextView cartPrice;
        TextView removeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cart_image);
            cartName = itemView.findViewById(R.id.cart_name);
            cartPrice = itemView.findViewById(R.id.cart_price);
            removeItem = itemView.findViewById(R.id.remove_item);
        }
    }

    public interface ItemRemoved {
        void onItemRemoved(List<Items> itemsList);
    }
}
