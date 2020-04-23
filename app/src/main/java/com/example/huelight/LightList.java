package com.example.huelight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.LightState;
import io.github.zeroone3010.yahueapi.State;
import io.github.zeroone3010.yahueapi.domain.Root;

public class LightList extends AppCompatActivity {

    String bridge_ip,token;
    Hue hue;
    List<String> stringArrayList = new ArrayList<>();
    ListView listlight= findViewById(R.id.light_list);

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lights);

        Intent intent = getIntent();
        bridge_ip = intent.getStringExtra("bridge_ip");
        token = intent.getStringExtra("token");
        hue = new Hue(bridge_ip,token);
        fetchlight();
    }

    protected void fetchlight(){
        new  Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if(hue!=null){
                        Root root = hue.getRaw();
                        Map<String, LightDto> lights = (Map<String, LightDto>) root.getLights();
                        for (String name : lights.keySet())
                        {  // search  for value
                            LightDto  lightDto= lights.get(name);

                            if (lightDto != null) {
                                stringArrayList.add(lightDto.getName());
                                Log.d("TAG", String.valueOf(lightDto.getState().isOn()));
                                Log.d("TAG",lightDto.getName());
                            }
                            Log.d("TAG","Lights " +name) ;
                        }
                        Log.d("TAG","LIGHTS CLICKED");
                    }
                }
                catch ( Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        ArrayAdapter stringArrayAdapter = new ArrayAdapter<String>(LightList.this,
                android.R.layout.simple_list_item_1,
                stringArrayList);
        listlight.setAdapter(stringArrayAdapter);
    }



}
