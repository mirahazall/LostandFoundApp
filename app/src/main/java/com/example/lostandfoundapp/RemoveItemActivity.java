package com.example.lostandfoundapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RemoveItemActivity extends AppCompatActivity {

    private TextView textViewType;
    private TextView textViewName;
    private TextView textViewPhone;
    private TextView textViewDescription;
    private TextView textViewDate;
    private TextView textViewLocation;
    private Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        textViewType = findViewById(R.id.textViewType);
        textViewName = findViewById(R.id.textViewName);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);
        textViewLocation = findViewById(R.id.textViewLocation);

        // Retrieve item ID from intent extras
        long itemId = getIntent().getLongExtra("ITEM_ID", -1);

        // Query the database to fetch item details
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        LostandFoundItem item = dbHelper.getItemById(itemId);

        // Display item details
        if (item != null) {
            textViewType.setText(item.getType());
            textViewName.setText(item.getName());
            textViewPhone.setText(item.getPhone());
            textViewDescription.setText(item.getDescription());
            textViewDate.setText(item.getDate());
            textViewLocation.setText(item.getLocation());
        } else {
            Toast.makeText(this, "Failed to retrieve item details", Toast.LENGTH_SHORT).show();
        }

        removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    long itemId = getIntent().getLongExtra("ITEM_ID", -1);

                // Query the database to fetch item details
                DatabaseHelper dbHelper = new DatabaseHelper(RemoveItemActivity.this);
                LostandFoundItem item = dbHelper.getItemById(itemId);

                // Display item details
                if (item != null) {
                    // Delete the item from the database
                    boolean isDeleted = dbHelper.deleteItem(item.getId());
                    if (isDeleted) {
                        Toast.makeText(RemoveItemActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RemoveItemActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RemoveItemActivity.this, "Failed to retrieve item details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

