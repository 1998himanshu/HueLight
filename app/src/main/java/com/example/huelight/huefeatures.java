package com.example.huelight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.Light;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.Root;

public class huefeatures extends AppCompatActivity {

    Hue hue;
    String bridge_ip,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huefeatures);
        Intent intent = getIntent();
        bridge_ip = intent.getStringExtra("bridge_ip");
        token = intent.getStringExtra("token");
        hue = new Hue(bridge_ip,token);
        Log.e("cred",""+bridge_ip+""+token);
        TextView id_bridge_details = findViewById(R.id.id_bdetails);
        id_bridge_details.setText(bridge_ip);
    }

    public void get_lights(View view)
    {
        Intent intent=new Intent(huefeatures.this,LightList.class);
        intent.putExtra("bridge_ip",bridge_ip);
        intent.putExtra("token",token);
        startActivity(intent);
    }

}
