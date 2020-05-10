package com.example.huelight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.Light;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.LightState;
import io.github.zeroone3010.yahueapi.State;
import io.github.zeroone3010.yahueapi.domain.Root;

public class LightList extends AppCompatActivity {

    String bridge_ip,token;
    Hue hue;
    List<HashMap<String,Object>> aList = new ArrayList<HashMap<String,Object>>();
    ListView lightlayout;
    Map<String, LightDto> lights;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lights);

        lightlayout= (ListView) findViewById(R.id.light_layout);

        Intent intent = getIntent();
        bridge_ip = intent.getStringExtra("bridge_ip");
        token = intent.getStringExtra("token");

        hue = new Hue(bridge_ip,token);

        fetchlight();

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lv, View item, int position, long id) {

                ListView lView = (ListView) lv;

                SimpleAdapter adapter = (SimpleAdapter) lView.getAdapter();

                HashMap<String,Object> hm = (HashMap) adapter.getItem(position);

                /** The clicked Item in the ListView */
                CardView rLayout = (CardView) item;

                /** Getting the toggle button corresponding to the clicked item */
                SwitchCompat tgl = (SwitchCompat) rLayout.getChildAt(1);

                String strStatus = "";
                if(tgl.isChecked()){
                    tgl.setChecked(false);
                    LightDto lightDto= (LightDto) hm.get("L_object");
                    LightState s =lightDto.getState();
                    s.setOn(false);
                    lightDto.setState(s);
                    strStatus = "Off";

                }else {
                    tgl.setChecked(true);
                    LightDto lightDto = (LightDto) hm.get("L_object");
                    LightState s = lightDto.getState();
                    s.setOn(true);
                    lightDto.setState(s);
                    strStatus = "On";
                }
                Toast.makeText(getBaseContext(), (String) hm.get("L_name") + " : " + strStatus, Toast.LENGTH_SHORT).show();
            }
        };

        lightlayout.setOnItemClickListener(itemClickListener);
    }

    protected void fetchlight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (hue != null) {
                        Root root = hue.getRaw();
                        Collection<Room> room =hue.getRooms();
                        lights = (Map<String, LightDto>) root.getLights();
                        ldisplay();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void ldisplay(){
        aList=null;
        for (String name : lights.keySet()){
            // search  for value
             LightDto  lightDto= lights.get(name);


             if (lightDto != null) {
                 HashMap<String, Object> hm = new HashMap<String,Object>();
                 hm.put("L_name",lightDto.getName());
                 hm.put("L_stat",lightDto.getState().isOn());
                 hm.put("L_object",lightDto);
                 aList.add(hm);

                 Log.d("TAG", String.valueOf(lightDto.getState().isOn()));
                 Log.d("TAG",lightDto.getName());
             }
             Log.d("TAG","Lights " +name) ;
         }

        // Keys used in Hashmap
        String[] from = {"L_name","L_stat" };

        // Ids of views in listview_layout
        int[] to = { R.id.light_name, R.id.light_switch};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item

        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.light_element, from, to);
        lightlayout.setAdapter(adapter);
    }



}
