package com.uah.calificaciones.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.uah.calificaciones.R;
import com.uah.calificaciones.model.Calificacion;

import java.util.ArrayList;

public class AdaptadorCalificaciones   extends RecyclerView.Adapter<AdaptadorCalificaciones.ViewHolder>  implements View.OnClickListener{

    private final ArrayList<Calificacion> calificaciones;
    private View.OnClickListener listener;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView tvAsignatura;
        public TextView tvCalificacion;
        public TextView tvFechaCalificacion;
        public LinearLayout lColorNota;

        public ViewHolder(View v) {
            super(v);
            tvAsignatura = (TextView) v.findViewById(R.id.tvAsignatura);
            tvCalificacion = (TextView) v.findViewById(R.id.tvCalificacion);
            tvFechaCalificacion = (TextView) v.findViewById(R.id.tvFechaCalificacion);
            lColorNota = (LinearLayout) v.findViewById(R.id.lColorNota);
        }
    }

    public AdaptadorCalificaciones(ArrayList<Calificacion> lista) {
        this.calificaciones =lista;
    }

    @Override
    public int getItemCount() {
        return calificaciones.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_calificacion, viewGroup, false);

        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.onClick(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Calificacion item = calificaciones.get(i);
        viewHolder.tvAsignatura.setText(item.getAsignatura());
        viewHolder.tvCalificacion.setText(item.getCalificacion());
        viewHolder.tvFechaCalificacion.setText(item.getFechaCalificacion());
        int colorNota = ContextCompat.getColor(context, R.color.black);;

        // Seleccionamos un color dependiendo de la calificación
        if(Integer.parseInt(item.getCalificacion())>= 0 && Integer.parseInt(item.getCalificacion())<5) {
            colorNota = ContextCompat.getColor(context, R.color.red);
        } else if (Integer.parseInt(item.getCalificacion())>= 5 && Integer.parseInt(item.getCalificacion())<8){
            colorNota = ContextCompat.getColor(context, R.color.yellow);
        } else{
            colorNota = ContextCompat.getColor(context, R.color.green);
        }
        viewHolder.lColorNota.setBackgroundColor(colorNota);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

}