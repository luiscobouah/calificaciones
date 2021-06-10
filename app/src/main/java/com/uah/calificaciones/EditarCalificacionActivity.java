package com.uah.calificaciones;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.uah.calificaciones.baseDatos.BaseDatos;
import com.uah.calificaciones.model.Calificacion;
import com.uah.calificaciones.util.DatePickerFragment;

import java.util.ArrayList;
import java.util.Objects;

public class EditarCalificacionActivity extends AppCompatActivity {

    private BaseDatos baseDatos;
    private SQLiteDatabase db;
    private TextInputLayout tvAsignatura;
    private TextInputLayout tvCalificacion ;
    private TextInputLayout tvFechaCalificacion;
    private String asignatura;
    private String calificacion;
    private String fecha;
    private int idCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_calificacion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        baseDatos = new BaseDatos(this);
        tvAsignatura = (TextInputLayout) findViewById(R.id.tvAsignatura);
        tvCalificacion = (TextInputLayout) findViewById(R.id.tvCalificacion);
        tvFechaCalificacion  = (TextInputLayout) findViewById(R.id.tvFechaCalificacion);

        idCalificacion =Integer.parseInt(getIntent().getStringExtra("ID_KEY"));
        Calificacion calificacionModificar = getCalificacionById(idCalificacion);

        tvAsignatura.getEditText().setText(calificacionModificar.getAsignatura());
        tvCalificacion.getEditText().setText(calificacionModificar.getCalificacion());
        tvFechaCalificacion.getEditText().setText(calificacionModificar.getFechaCalificacion());

        Button btGuardarCalificacion;
        btGuardarCalificacion = findViewById(R.id.btGuardarCalificacion);

        tvFechaCalificacion.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Listener para el boton guardar calificacion
        btGuardarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignatura = tvAsignatura.getEditText().getText().toString();
                calificacion = tvCalificacion.getEditText().getText().toString();
                fecha = tvFechaCalificacion.getEditText().getText().toString();

                //Validamos los datos
                if(validate()) {
                    //Obtenemos el modo escritura en la bd
                    db = baseDatos.getWritableDatabase();
                    //comprobamos que se ha abierto correctemente
                    if (db != null) {
                        baseDatos.updateCalificacion(db, idCalificacion, asignatura,calificacion,fecha);
                        //Cerramos la base de datos
                        db.close();
                        Toast.makeText(getApplicationContext(), "Se ha modificado la calificaión", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error al modificar la calificaión", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque enero es 0
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                Objects.requireNonNull(tvFechaCalificacion.getEditText()).setText(selectedDate);
            }
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    // Obtenemos la calificacion a modificar
    private Calificacion getCalificacionById(int idCalificacion) {

        db = baseDatos.getReadableDatabase();
        Cursor c = baseDatos.getCalificacionById(db,idCalificacion);
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

        return listaCalificaciones.get(0);
    }
    /* Función para validar los datos del formulario */
    private boolean validate() {
        boolean valid = true;

        if (asignatura.equals("")) {
            tvAsignatura.getEditText().setError(getString(R.string.asignatura_obligatoria));
            valid = false;
        } else {
            tvAsignatura.setError(null);
        }
        if (calificacion.equals("")) {
            tvCalificacion.getEditText().setError(getString(R.string.calificación_obligatoria));
            valid = false;
        } else {
            if (Integer.parseInt(calificacion)<0 || Integer.parseInt(calificacion)>10) {
                tvCalificacion.getEditText().setError(getString(R.string.rango_nota));
                valid = false;
            } else {
                tvCalificacion.setError(null);
            }
        }
        if (fecha.equals("")) {
            tvFechaCalificacion.getEditText().setError(getString(R.string.fecha_calificacion));
            valid = false;
        } else {
            tvFechaCalificacion.setError(null);
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_calificacion, menu);
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
            baseDatos.deleteCalificacion(db,idCalificacion);
            db.close();
            Toast.makeText(getApplicationContext(), "Se ha eliminado la calificaión", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}