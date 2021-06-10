package com.uah.calificaciones;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.uah.calificaciones.adaptador.AdaptadorCalificaciones;
import com.uah.calificaciones.baseDatos.BaseDatos;
import com.uah.calificaciones.model.Calificacion;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private BaseDatos baseDatos;
    private SQLiteDatabase db;

    private RecyclerView rvCalificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        baseDatos = new BaseDatos(this);
        rvCalificaciones = findViewById(R.id.rvCalificaciones);
        rvCalificaciones.setHasFixedSize(true);

        // Nuestro RecyclerView usará un linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCalificaciones.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NuevaCalificacionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        obtenerCalificaciones();
        super.onStart();
    }

    public void obtenerCalificaciones() {
        db = baseDatos.getReadableDatabase();
        Cursor c = baseDatos.getAllCalificaciones(db);
        ArrayList<Calificacion> listaCalificaciones = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                listaCalificaciones.add(new Calificacion(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)
                ));
            } while(c.moveToNext());
        }
        db.close();

        // Asociamos un adapter
        AdaptadorCalificaciones adaptadorCalificaciones = new AdaptadorCalificaciones(listaCalificaciones);
        adaptadorCalificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // para el evento onClick lanzamos la actividad de modificar calificacion y pasamos el Id por parametro.
                String id = listaCalificaciones.get(rvCalificaciones.getChildAdapterPosition(v)).getId();
                Intent intent = new Intent(getApplicationContext(), EditarCalificacionActivity.class);
                intent.putExtra("ID_KEY", id);
                startActivity(intent);
                Log.e("ID", id);
            }
        });
        rvCalificaciones.setAdapter(adaptadorCalificaciones);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            db = baseDatos.getReadableDatabase();
            baseDatos.deleteAllCalificacion(db);
            db.close();
            Toast.makeText(getApplicationContext(), "Se han eliminado las calificaiones", Toast.LENGTH_SHORT).show();
            obtenerCalificaciones();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}