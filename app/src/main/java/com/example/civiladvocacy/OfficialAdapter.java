package com.example.civiladvocacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final ArrayList<Official> officialList;
    private final MainActivity mainActivity;

    public OfficialAdapter(ArrayList<Official> officialList, MainActivity mainActivity) {
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list_entry,
                parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Official official = officialList.get(position);
        holder.name.setText(official.getName() + " (" + official.getParty() + ")");
        holder.type.setText(official.getTitle());


    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
