package com.example.projthree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private final List<InventoryItem> itemList;
    private final DatabaseHelper db;
    private final Runnable onDataChangedCallback;

    public InventoryAdapter(List<InventoryItem> itemList, DatabaseHelper db, Runnable onDataChangedCallback) {
        this.itemList = itemList;
        this.db = db;
        this.onDataChangedCallback = onDataChangedCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryItem item = itemList.get(position);

        holder.idEdit.setText(String.valueOf(item.getDisplayId()));
        holder.nameEdit.setText(item.getName());
        holder.qtyEdit.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            db.updateItem(item.getId(), item.getName(), item.getQuantity() + 1);
            triggerRefresh();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int newQty = Math.max(0, item.getQuantity() - 1);
            db.updateItem(item.getId(), item.getName(), newQty);
            triggerRefresh();
        });

        holder.btnDel.setOnClickListener(v -> {
            db.deleteItem(item.getId());
            triggerRefresh();
        });
    }

    private void triggerRefresh() {
        if (onDataChangedCallback != null) {
            onDataChangedCallback.run();
        }
    }

    @Override
    public int getItemCount() { return itemList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText idEdit, nameEdit, qtyEdit;
        Button btnPlus, btnMinus, btnDel;

        public ViewHolder(View v) {
            super(v);
            idEdit = v.findViewById(R.id.editTextText);
            nameEdit = v.findViewById(R.id.editTextText3);
            qtyEdit = v.findViewById(R.id.editTextNumber2);
            btnPlus = v.findViewById(R.id.button7);
            btnMinus = v.findViewById(R.id.button8);
            btnDel = v.findViewById(R.id.button9);
        }
    }
}