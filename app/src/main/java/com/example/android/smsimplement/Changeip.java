package com.example.android.smsimplement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Changeip extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeip);

        findViewById(R.id.btn_save_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edt=findViewById(R.id.ip_edt);
                String ip = edt.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("ip", MODE_PRIVATE).edit();
                editor.putString("ip", ip);
                editor.apply();

                startActivity(new Intent(Changeip.this,MainActivity.class));
            }
        });
    }
}
