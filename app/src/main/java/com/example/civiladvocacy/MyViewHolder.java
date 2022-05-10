package com.example.civiladvocacy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;


public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView type;
    TextView name;
    public MyViewHolder(@NonNull View view) {
        super(view);
        type = view.findViewById(R.id.officalType);
        name = view.findViewById(R.id.officialName);

    }
}
