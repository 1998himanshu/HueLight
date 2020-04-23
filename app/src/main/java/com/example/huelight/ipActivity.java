package com.example.huelight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import io.github.zeroone3010.yahueapi.Hue;



public class ipActivity extends AppCompatActivity {
    String bridge_ip;
    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        Intent intent = getIntent();
        String bridge_details = intent.getStringExtra("bridge_details");
        bridge_ip= intent.getStringExtra("bridge_ip");

        TextView textView  =findViewById(R.id.ip_textview);
        textView.setText(bridge_details);
    }



    public void Submit(View view) {
        CheckBox id_warning =findViewById(R.id.id_warning);
        EditText id_user=findViewById(R.id.id_user);
        EditText id_token=findViewById(R.id.id_token);



        if (id_warning.isChecked() && !(id_user.getText().toString().isEmpty())){
            final CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(bridge_ip).initializeApiConnection(id_user.getText().toString());
            // Push the button on your Hue Bridge to resolve the apiKey future:
            final String key;
            try {
                key = apiKey.get();
                System.out.println("Store this API key for future use: " + key);
                id_token.setText(key);
                token=key;
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(ipActivity.this,"Token Generation Failed "+e.getMessage(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }

    public void on_connect(View view) {
        if (!token.isEmpty()){
        Intent connect_intent = new Intent(ipActivity.this,huefeatures.class);
        connect_intent.putExtra("bridge_ip",bridge_ip);
        connect_intent.putExtra("token",token);
        startActivity(connect_intent);}
        else {
            Toast.makeText(ipActivity.this,"Generate Token ",Toast.LENGTH_LONG).show();
        }
    }
}
