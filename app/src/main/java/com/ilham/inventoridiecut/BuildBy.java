package com.ilham.inventoridiecut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class BuildBy extends AppCompatActivity {
    ImageView kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_by);

        kembali = findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuildBy.this,LoginActivity.class));
                finish();
            }
        });
    }
}