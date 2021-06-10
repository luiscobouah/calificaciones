package com.uah.calificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import com.uah.calificaciones.baseDatos.BaseDatos;
import com.uah.calificaciones.util.DatePickerFragment;

import java.util.Objects;

public class NuevaCalificacionActivity extends AppCompatActivity {

    private BaseDatos baseDatos;
    private SQLiteDatabase db;
    private TextInputLayout tvAsignatura;
    private TextInputLayout tvCalificacion ;
    private TextInputLayout tvFechaCalificacion;
    private String asignatura;
    private String calificacion;
    private String fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_calificacion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        baseDatos = new BaseDatos(this);
        tvAsignatura = (TextInputLayout) findViewById(R.id.tvAsignatura);
        tvCalificacion = (TextInputLayout) findViewById(R.id.tvCalificacion);
        tvFechaCalificacion  = (TextInputLayout) findViewById(R.id.tvFechaCalificacion);
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
                    if (db != null)                {
                        baseDatos.insertCalificacion(db, 1, asignatura,calificacion,fecha);
                        //Cerramos la base de datos
                        db.close();
                        Toast.makeText(getApplicationContext(), "Se ha añadido la calificaión", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error al añadir la calificaión", Toast.LENGTH_SHORT).show();
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

}