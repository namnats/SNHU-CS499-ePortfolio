package com.example.projthree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TrakTrak.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ITEMS = "inventory";
    private static volatile DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, 2);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (username TEXT PRIMARY KEY, password TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, QUANTITY INTEGER, USERNAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public boolean addUser(String user, String pass) {
        if (user == null || pass == null || user.trim().isEmpty() || pass.trim().isEmpty()) {
            return false;
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", user.trim());

            String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt(12));
            values.put("password", hashedPassword);

            return db.insert(TABLE_USERS, null, values) != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkUser(String user, String pass) {
        if (user == null || pass == null) return false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_USERS + " WHERE username=?", new String[]{user.trim()});
        boolean isValid = false;

        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            if (BCrypt.checkpw(pass, storedHash)) {
                isValid = true;
            }
        }
        cursor.close();
        return isValid;
    }

    public boolean addItem(String name, int qty, String username) {
        if (name == null || name.trim().isEmpty() || username == null) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name.trim());
        values.put("QUANTITY", Math.max(0, qty));
        values.put("USERNAME", username.trim());
        return db.insert(TABLE_ITEMS, null, values) != -1;
    }

    public Cursor getItemsByUser(String username) {
        if (username == null) return null;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE USERNAME = ? ORDER BY NAME ASC", new String[]{username.trim()});
    }

    public void updateItem(int id, String name, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name != null ? name.trim() : "Item");
        values.put("QUANTITY", Math.max(0, qty));
        db.update(TABLE_ITEMS, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, "ID=?", new String[]{String.valueOf(id)});
    }
}