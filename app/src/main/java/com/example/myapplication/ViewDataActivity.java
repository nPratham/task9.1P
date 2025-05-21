package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewDataActivity extends AppCompatActivity {

    LinearLayout itemsContainer = findViewById(R.id.itemsContainer);
    DatabaseHelper dbHelper;

    private static final int REQUEST_CODE_ITEM_DETAIL = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showitems);

        dbHelper = new DatabaseHelper(this);

        loadItems();
    }

    private void loadItems() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("ID");
            int nameIndex = cursor.getColumnIndex("NAME");
            int statusIndex = cursor.getColumnIndex("POST_TYPE");
            int dateIndex = cursor.getColumnIndex("DATE");
            int locationIndex = cursor.getColumnIndex("LOCATION");

            do {
                String itemId = cursor.getString(idIndex);
                String itemName = cursor.getString(nameIndex);
                String itemStatus = cursor.getString(statusIndex);
                String itemDate = cursor.getString(dateIndex);
                String itemLocation = cursor.getString(locationIndex);

                TextView itemTextView = new TextView(this);
                itemTextView.setText(itemName + " - " + itemStatus);
                itemTextView.setTextSize(18);
                itemTextView.setPadding(10, 10, 10, 10);
                itemTextView.setBackgroundResource(android.R.drawable.list_selector_background);

                itemTextView.setTag(itemId);

                itemTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewDataActivity.this, ItemDetailActivity.class);
                        intent.putExtra("ITEM_ID", itemId);
                        intent.putExtra("ITEM_NAME", itemName);
                        intent.putExtra("ITEM_STATUS", itemStatus);
                        intent.putExtra("ITEM_DATE", itemDate);
                        intent.putExtra("ITEM_LOCATION", itemLocation);
                        startActivityForResult(intent, REQUEST_CODE_ITEM_DETAIL);
                    }
                });

                itemsContainer.addView(itemTextView);

            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Toast.makeText(this, "No items found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ITEM_DETAIL && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("ITEM_DELETED", false)) {
                String deletedItemId = data.getStringExtra("DELETED_ITEM_ID");

                for (int i = 0; i < itemsContainer.getChildCount(); i++) {
                    View child = itemsContainer.getChildAt(i);
                    if (child.getTag() != null && child.getTag().equals(deletedItemId)) {
                        itemsContainer.removeView(child);
                        Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }
}