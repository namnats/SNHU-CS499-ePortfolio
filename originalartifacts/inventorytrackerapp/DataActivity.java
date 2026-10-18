package com.example.projthree;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView recyclerView;
    InventoryAdapter adapter;
    List<InventoryItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.inventoryRecyclerView);
        Button btnAdd = findViewById(R.id.button11);
        Button btnLogout = findViewById(R.id.button15);
        Button btnToSms = findViewById(R.id.button14);

        itemList = new ArrayList<>();
        loadData();

        adapter = new InventoryAdapter(itemList, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            db.addItem("New Item", 1);
            refreshGrid();
        });

        btnToSms.setOnClickListener(v -> {
            Intent intent = new Intent(DataActivity.this, SmsActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    public void loadData() {
        itemList.clear();
        Cursor cursor = db.getAllItems();
        if (cursor.moveToFirst()) {
            do {
                itemList.add(new InventoryItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void refreshGrid() {
        loadData();
        adapter.notifyDataSetChanged();
    }
}
