package com.example.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class reservar extends AppCompatActivity implements View.OnClickListener {

    private String fecha_inicio, hora_inicio, hora_fin, sede, tiempo;
    private int dia,mes,anio,hora,min; //tiempo del sistema
    private String[] seleccionar = new String[]{"Seleccione sede...","Monterrico", "San Miguel"};
    private boolean esHoy=false;
    EditText txtFecha, txtTiempo, txtHora, txtHora2;
    Button btnBuscarReserva, btnLimpiarReserva;
    Spinner sedes;
    ListView lstEstacionamientos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        //Crear adaptador
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,seleccionar);
        sedes = (Spinner) findViewById(R.id.spnSede);
        sedes.setAdapter(adaptador);

        sedes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) mensaje("Debe seleccionar una sede");
                if (position==2) mensaje("La sede seleccionada aun no esta disponible");
            }//fin de onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // findViewById() ->se encarga de acceder a un recurso para cambiar una de sus propiedades

        //vincular EditText a variables
        txtTiempo=(EditText) findViewById(R.id.txtTiempo);
        txtHora=(EditText) findViewById(R.id.txtHora);
        txtHora2=(EditText) findViewById(R.id.txtHora2);
        txtFecha=(EditText) findViewById(R.id.txtFecha);
        btnBuscarReserva = (Button) findViewById(R.id.btnBuscarReservar);
        btnLimpiarReserva = (Button) findViewById(R.id.btnLimpiarReservar);
        lstEstacionamientos = (ListView) findViewById(R.id.lstBuscarReservar);

        //Escuchar el evento click
        txtTiempo.setOnClickListener(this);
        txtHora.setOnClickListener(this);
        txtFecha.setOnClickListener(this);
        btnBuscarReserva.setOnClickListener(this);
        btnLimpiarReserva.setOnClickListener(this);


        //txtFecha.setKeyListener(null); //evita que modifiquen el EditText
        //txtFecha.requestFocus();

    }

    @Override
    public void onClick(View v) {

        tiempo = txtTiempo.getText().toString();
        fecha_inicio = txtFecha.getText().toString();
        hora_inicio  = txtHora.getText().toString();
        //fecha_fin  = String.valueOf(txt.getText());
        hora_fin = String.valueOf(txtHora2.getText());
        sede = seleccionar[sedes.getSelectedItemPosition()];

        switch (v.getId()){
            case R.id.btnBuscarReservar:
                validar();
                break;
            case R.id.txtHora:
                if (fecha_inicio.isEmpty()) txtFecha.setError("Debe seleccionar la fecha de reserva");
                else seleccionarHora();
                    txtFecha.setText("27/4/2020");
                break;
            case R.id.txtFecha:
                if (tiempo.isEmpty()) txtTiempo.setError("Debe indicar el tienmpo a reservar");
                else seleccionarFecha();
                break;
            case R.id.txtTiempo:
                if (!tiempo.isEmpty()) txtTiempo.setEnabled(false);
                if (!txtTiempo.isEnabled()) mensaje ("Para cambiar la hora debe LIMPIAR el formulario");
                break;
            case R.id.btnLimpiarReservar:
                limpiar();
                break;
        }
    }

    void mensaje(String msg){
        Toast toast = Toast.makeText(this,""+msg,Toast.LENGTH_LONG);
        toast.show();
    }

    void validar(){

        if(tiempo.isEmpty() || fecha_inicio.isEmpty() || hora_inicio.isEmpty() ||
                sedes.getSelectedItemPosition()==0){
            if (tiempo.isEmpty()) txtTiempo.setError("Debe ingresar la cantidad de horas a reservar");
            //if (sedes.getSelectedItemPosition()==0) mensaje("Debe seleccionar una sede");
            if (fecha_inicio.isEmpty()) txtFecha.setError("Debe seleccionar la fecha de reserva");
            if (hora_inicio.isEmpty()) txtHora.setError("Debe seleccionar la hora de la reserva");
            mensaje ("Primero debe llenar los campos obligatorios");
        }else{
            mensaje("VALIDACIONES CORRECTAS");
            listarEstacionamientos();
        }
    }

    void seleccionarFecha(){
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        anio = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //formato a fechas para compararlas
                SimpleDateFormat dma = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date1 = dma.parse(dia+"-"+mes+"-"+anio);
                    Date date2 = dma.parse(dayOfMonth+"-"+month+"-"+year);

                    switch (date2.compareTo(date1)){
                        case -1:
                            mensaje("Fecha no válida");
                            txtFecha.setError("Debe escoger una fecha igual o mayor a: "+dia+"-"+(mes+1)+"-"+anio);
                            break;
                        case 0:
                            esHoy=true;
                        case 1:
                            txtFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            txtFecha.setError(null);
                            break;
                    }//Fin de Switch
                } catch (ParseException e) {
                    e.printStackTrace();
                    mensaje("Error al comparar fechas");
                }
            }
        }, dia,mes,anio);
        datePickerDialog.show();
    }

    void seleccionarHora(){

        final Calendar t = Calendar.getInstance(TimeZone.getTimeZone("GMT-5:00"));
        hora = t.get(Calendar.HOUR);
        min = t.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //formato a fechas para compararlas
                SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
                try {
                    Date time1 = hm.parse(hora+":"+min); //Hora del sistema
                    Date time2 = hm.parse(hourOfDay+":"+minute); //Hora seleccionada
                    Date time3 = hm.parse("06:00"); //Apertura de la sede
                    Date time4 = hm.parse("23:00"); //Cierre de la sede
                    if(esHoy=true && time2.compareTo(time1)<0){
                        mensaje("Hora no válida");
                        txtHora.setError("La hora no puede ser pasada");
                    }else if(time2.compareTo(time3)<0 || time2.compareTo(time4)>0){
                        mensaje("El horario de atención es de 6:00 a 23:00");
                        txtHora.setText("");
                        txtHora.setError("Hora no válida");
                    }else if(1440-(((hourOfDay+Integer.parseInt(tiempo))*60)+minute)<0) {
                        mensaje("Las horas reservadas no pueden exceder al dia de hoy");
                        txtHora.setText("");
                        txtHora.setError("Hora no válida");
                    }else {
                        txtHora.setText(hourOfDay+":"+minute);
                        txtHora.setError(null);

                        txtHora2.setText(String.valueOf((((hourOfDay+Integer.parseInt(tiempo))*60)+minute)/60)+
                                ":"+String.valueOf((((hourOfDay+Integer.parseInt(tiempo))*60)+minute)%60));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    mensaje("Error al comparar horas");
                }
            }
        },hora,min,true);
        timePickerDialog.show();
    }

    void limpiar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Se borraran todos los datos ingresados");
        msg.setTitle("¿Desea cancelar el registro?");
        msg.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtTiempo.setText("");
                txtFecha.setText("");
                txtHora.setText("");
                txtHora2.setText("");
                txtTiempo.setError(null);
                txtFecha.setError(null);
                txtHora.setError(null);
                txtHora2.setError(null);
                txtTiempo.setEnabled(true);
                sedes.setSelection(0);
                lstEstacionamientos.setAdapter(null);
                txtTiempo.requestFocus();
            }
        });
        msg.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = msg.create();
        dialog.show();
    }

    void listarEstacionamientos(){
        //Almacenar el sitio que contiene la BD
        final String url="http://parking.scienceontheweb.net/index.php/estacionamientos";

        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("======>",jsonArray.toString());

                    List<String> items = new ArrayList<>();

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        //if ((object.getString("estado")).equals("1"))
                        if ((object.getInt("estado")==1)) //Validar si esta disponible
                            items.add(object.getString("nombre")+"\n"+object.getString("descripcion"));
                    }//fin del for
                    //Crear adaptador
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(reservar.this, android.R.layout.simple_list_item_1, items);

                    //Se asigna el Adapter al ListView
                    lstEstacionamientos.setAdapter(adaptador);
                } catch (JSONException e) {
                    Log.i("======>", e.getMessage());
                    mensaje("e.getMessage()");
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("======>", error.toString());
                mensaje("error.toString()");
            }
        } ); //Se cierra el StringRequest
        //Enviar solicitud
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
