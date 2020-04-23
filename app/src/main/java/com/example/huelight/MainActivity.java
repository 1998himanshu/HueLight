package com.example.huelight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import io.github.zeroone3010.yahueapi.*;
import io.github.zeroone3010.yahueapi.discovery.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listip = findViewById(R.id.list_ip);
        TextView errorView = findViewById(R.id.nobridge);
        TextView listItem = findViewById(R.id.textView);

        Future<List<HueBridge>> bridgesFuture = new HueBridgeDiscoveryService()
                .discoverBridges(bridge -> Log.e("HII","Bridge found: " + bridge));
        List<String> stringArrayList = new ArrayList<>();

        List<HueBridge> bridges= null;
                try{
                    bridges = bridgesFuture.get();
                    //    HueBridge hueBridge = new HueBridge("192.168.1.14","Name","789");
                    //    bridges.add(0,hueBridge);
                    Log.e("TAGGGG", String.valueOf(bridges.size()));
                    if( !bridges.isEmpty() ) {
                        final String bridgeIp = bridges.get(0).getIp();
                        Log.e("HII","Bridge found at " + bridgeIp);
                        Toast.makeText(this, "IP ="+ bridgeIp, Toast.LENGTH_SHORT).show();
                        for (int i =0; i<bridges.size();i++)
                        {
                            String bridgeIP = bridges.get(i).getIp();
                            String bridgeName = bridges.get(i).getName();
                            String bridgeIpName = bridgeIP + "\n"+ bridgeName;
                            stringArrayList.add(bridgeIpName);
                            Log.e("Item ADded size", String.valueOf(stringArrayList.size()));
                        }
                    }
                    else {
                        listip.setVisibility(View.INVISIBLE);
                        errorView.setVisibility(View.VISIBLE);
                        Toast.makeText(this,"Nothing here",Toast.LENGTH_LONG).show();
                        Log.e("HII","Nothing here");
                    }
                }
        catch(Exception e) {
                    Log.e("TAG","exception");
            e.printStackTrace();
        }
        assert bridges != null;
        ArrayAdapter stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                stringArrayList);
        listip.setAdapter(stringArrayAdapter);
        List<HueBridge> finalBridges = bridges;
        listip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,ipActivity.class);
                i.putExtra("bridge_details",stringArrayList.get(position));
                i.putExtra("bridge_ip", finalBridges.get(position).getIp());
                startActivity(i);
            }
        });
    }
}
