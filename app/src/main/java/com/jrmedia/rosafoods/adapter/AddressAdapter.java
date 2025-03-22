package com.jrmedia.rosafoods.adapter;

import android.content.Context;
/*import android.location.Address;*/
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jrmedia.rosafoods.domain.Address;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    Context applicationContext;
    List<Address> mAddressList;

    public AddressAdapter(Context applicationContext, List<Address> mAddressList) {
        this.applicationContext=applicationContext;
        this.mAddressList=mAddressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
