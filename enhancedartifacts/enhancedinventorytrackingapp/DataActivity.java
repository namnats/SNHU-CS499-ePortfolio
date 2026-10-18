package com.example.projthree;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class DataActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<InventoryItem> itemList;
    private Map<Integer, InventoryItem> idIndexMap;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        db = DatabaseHelper.getInstance(this);
        currentUsername = getIntent().getStringExtra("LOGGED_IN_USER");

        if (currentUsername == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.inventoryRecyclerView);
        Button btnAdd = findViewById(R.id.button11);
        Button btnLogout = findViewById(R.id.button15);
        Button btnToSms = findViewById(R.id.button14);

        itemList = new ArrayList<>();
        idIndexMap = new HashMap<>();

        adapter = new InventoryAdapter(itemList, db, this::refreshGrid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadData();

        btnAdd.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                db.addItem("New Item", 1, currentUsername);
                runOnUiThread(this::refreshGrid);
            });
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
        Executors.newSingleThreadExecutor().execute(() -> {
            List<InventoryItem> tempItems = new ArrayList<>();
            Map<Integer, InventoryItem> tempIdMap = new HashMap<>();

            Cursor cursor = db.getItemsByUser(currentUsername);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int displayCounter = 1;
                    do {
                        int id = cursor.getInt(0);
                        String name = cursor.getString(1);
                        int quantity = cursor.getInt(2);

                        InventoryItem item = new InventoryItem(id, name, quantity);
                        item.setDisplayId(displayCounter++);

                        tempIdMap.put(id, item);
                        tempItems.add(item);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

            runOnUiThread(() -> {
                itemList.clear();
                idIndexMap.clear();
                itemList.addAll(tempItems);
                idIndexMap.putAll(tempIdMap);
                adapter.notifyDataSetChanged();
            });
        });
    }

    public boolean itemExistsInMemory(int id) {
        return idIndexMap.containsKey(id);
    }

    public InventoryItem getItemById(int id) {
        return idIndexMap.get(id);
    }

    public void refreshGrid() {
        loadData();
    }
}