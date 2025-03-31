package com.jrmedia.rosafoods.adapter;

import android.content.Context;
/*import android.location.Address;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jrmedia.rosafoods.R;
import com.jrmedia.rosafoods.domain.Address;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    Context applicationContext;
    List<Address> mAddressList;
    private RadioButton mSelectedRadioButton;
    SelectedAddress selectedAddress;


    public AddressAdapter(Context applicationContext, List<Address> mAddressList, SelectedAddress selectedAddress) {
        this.applicationContext=applicationContext;
        this.mAddressList=mAddressList;
        this.selectedAddress=selectedAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mAddress.setText(mAddressList.get(position).getAddress());

        holder.mRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition(); // Usar getAdapterPosition() em vez da posição direta

                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return; // Item não está mais na lista
                }

                for (Address address : mAddressList) {
                    address.setSelected(false);
                }

                mAddressList.get(adapterPosition).setSelected(true);

                if (mSelectedRadioButton != null) {
                    mSelectedRadioButton.setChecked(false);
                }

                mSelectedRadioButton = (RadioButton) view;
                mSelectedRadioButton.setChecked(true);
                selectedAddress.setAddress(mAddressList.get(adapterPosition).getAddress()); // Usar adapterPosition aqui também
            }
        });

        // Definir o estado do RadioButton baseado no modelo
        holder.mRadio.setChecked(mAddressList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mAddress;
        private RadioButton mRadio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAddress=itemView.findViewById(R.id.address_add);
            mRadio=itemView.findViewById(R.id.select_address);
        }
    }
    public interface SelectedAddress{
        public void setAddress(String s);
    }
}
