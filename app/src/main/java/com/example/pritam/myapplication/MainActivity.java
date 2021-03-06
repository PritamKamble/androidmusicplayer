package com.example.pritam.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView myListViewForSongs;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListViewForSongs=(ListView)findViewById(R.id.mySongListView);

        runtimePermision();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void runtimePermision() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        display();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();
    }

    public ArrayList<File> findsong(File file){

        ArrayList<File> arrayList=new ArrayList<>();

        File[] files=file.listFiles();

        for (File   singlefile: files) {

            if (singlefile.isDirectory() && !singlefile.isHidden()){

                arrayList.addAll(findsong(singlefile));
            }
            else {

                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){

                    arrayList.add(singlefile);
                }

            }
        }

        return arrayList;
    }

    public void display(){

        final ArrayList<File> mySongs = findsong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];

        for (int i =0;i<mySongs.size();i++){

            items[i]=mySongs.get(i).getName().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> myadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_2,android.R.id.text1,items);
        myListViewForSongs.setAdapter(myadapter);
        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songname=myListViewForSongs.getItemAtPosition(position).toString();

                startActivity(new Intent(getApplicationContext(),playerActivity.class).putExtra("songs",mySongs)
                        .putExtra("songname",songname).putExtra("pos",position));

            }
        });
    }
}
