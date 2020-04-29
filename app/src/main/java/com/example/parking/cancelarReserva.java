package com.example.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class cancelarReserva extends AppCompatActivity implements View.OnClickListener {

    Button btnCancelarC, btnRefrescarC;
    ListView listaCancelar;
    Reservas reserva;
    ArrayList<Reservas> listaReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_reserva);

        //vincular elementos a variables
        btnCancelarC = (Button) findViewById(R.id.btnEliminarCancelar);
        btnRefrescarC = (Button) findViewById(R.id.btnRegresarCancelar);
        listaCancelar = (ListView) findViewById(R.id.lstReservasCancelar);

        //Escuchar el evento click de las variables
        btnCancelarC.setOnClickListener(this);
        btnRefrescarC.setOnClickListener(this);

        //Toast toast1 = Toast.makeText(this,"REFRESCADO",Toast.LENGTH_SHORT);
        //toast1.show();
        listarReservas();
        eliminarReserva();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEliminarCancelar:
                eliminarTodo();
                listarReservas();
                break;
            case R.id.btnRegresarCancelar:
                onBackPressed();
                //listarReservas();
                break;
        }//Fin de Switch
    }

    //public void listarReservas(){
    void listarReservas(){
        //Almacenar el sitio que contiene la BD
        final String url="http://parking.scienceontheweb.net/index.php/reservas/"+MainActivity.userLogin;

        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    listaReservas = new ArrayList<Reservas>();

                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("======>",jsonArray.toString());

                    List<String> items = new ArrayList<>();

                    String estado="";
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        //Guardar los datos del usuario en un nuevo objeto RESERVAS
                        String cod_upc = object.getString("usuario");
                        Integer idEstacionamiento = object.getInt("estacionamiento");
                        Integer idSede = object.getInt("sede");
                        String fecha = object.getString("Dia-inicio");
                        String horaInicio = object.getString("Hora-inicio");
                        String horaFin = object.getString("Hora-fin");
                        reserva = new Reservas(cod_upc, idEstacionamiento, idSede, fecha, horaInicio, horaFin);
                        listaReservas.add(reserva);
                        items.add("Fecha de reserva: "+object.getString("Dia-inicio")+
                                "\nHora de inicio"+object.getString("Hora-inicio")); //Solo mostrara le fecha
                    }//fin del for
                    //Crear adaptador
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(cancelarReserva.this, android.R.layout.simple_list_item_1, items);
                    //Se asigna el Adapter al ListView
                    listaCancelar.setAdapter(adaptador);

                    //eliminarReserva()

                } catch (JSONException e) {
                    Log.i("======>", e.getMessage());
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("======>", error.toString());
            }
        } ); //Se cierra el StringRequest
        //Enviar solicitud
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void eliminarReserva(){
        //Crea el evento que mostrara el detalle de cada item
        listaCancelar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cod_upc = listaReservas.get(position).getCod_upc();
                Integer idEstacionamiento = listaReservas.get(position).getId_estacionamiento();
                Integer idSede = listaReservas.get(position).getId_sede();
                String fecha = listaReservas.get(position).getDate_start();

                mensaje("Se va a eliminar el registro con los datos:"+cod_upc+
                        " / "+ idEstacionamiento+" / "+ idSede+ " / "+ fecha);
            }
        });
    }

    void mensaje(String msg){
        Toast toast = Toast.makeText(cancelarReserva.this,""+msg,Toast.LENGTH_LONG);
        toast.show();
    }

    void eliminarTodo(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        //alerta.setMessage("Regresara a la ventana de Login");
        alerta.setTitle("¿Desea eliminar todas las reservas?");
        alerta.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    URL url = new URL("http://parking.scienceontheweb.net/index.php/DELETEreservas/"+MainActivity.userLogin);
                    URLConnection con = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    mensaje(String.valueOf(url));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mensaje ("error en la URL");
                } catch (Throwable e) {
                    e.printStackTrace();
                    mensaje ("error al eliminar desde la URL");
                }
            }
        });
        alerta.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alerta.create();
        dialog.show();
    }

    void eliminarTodo2(){
        //Almacenar el sitio que contiene la BD
      /*  final String url="http://parking.scienceontheweb.net/index.php/DELETEreservas/"+MainActivity.userLogin;

        StringRequest stringRequest= new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {



                } catch (JSONException e) {
                    Log.i("======>", e.getMessage());
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("======>", error.toString());
            }
        } ); //Se cierra el StringRequest
        //Enviar solicitud
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

       */
    }
}
