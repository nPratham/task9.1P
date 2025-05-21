package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button createAdvertBtn, showItemsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAdvertBtn = findViewById(R.id.button);
        showItemsBtn = findViewById(R.id.button2);

        createAdvertBtn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, FormActivity.class))
        );

        showItemsBtn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewDataActivity.class))
        );
        Button viewMapButton = findViewById(R.id.viewMapButton);
        viewMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

    }
}



