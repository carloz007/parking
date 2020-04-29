package com.example.parking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class usuario extends AppCompatActivity implements View.OnClickListener {

    Button btnReservar, btnCancelar,btnCambiarClave;
    ImageView imgCamara;
    TextView lblCodigo, lblNombres, lblCorreo;

    static Usuarios sesionUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        //vincular a variables
        btnReservar=(Button) findViewById(R.id.btnReservarUsuario);
        btnCancelar=(Button) findViewById(R.id.btnCancelarUsuario);
        btnCambiarClave=(Button) findViewById(R.id.btnClaveUsuario);
        imgCamara=(ImageView) findViewById(R.id.imgCamara);
        lblCodigo=(TextView) findViewById(R.id.txtCodigoUsuario);
        lblNombres=(TextView) findViewById(R.id.txtNombreCompleto);
        lblCorreo=(TextView) findViewById(R.id.txtEmailUsuario);


        //Escuchar el evento click
        btnReservar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnCambiarClave.setOnClickListener(this);
        imgCamara.setOnClickListener(this);

        cargarUsuario();

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.menu1:
                Intent intent1 = new Intent (this, editarUsuario.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.menu2:
                Intent intent2 = new Intent (this, cambiarClave.class);
                startActivityForResult(intent2, 0);
                break;
            case R.id.menu3:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgCamara:
                mensaje("clic");
                break;
            case R.id.btnReservarUsuario:
                Intent intent1 = new Intent (v.getContext(), reservar.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.btnCancelarUsuario:
                Intent intent2 = new Intent (v.getContext(), cancelarReserva.class);
                startActivityForResult(intent2, 0);
                break;
            case R.id.btnClaveUsuario:
                Intent intent3 = new Intent (this, cambiarClave.class);
                startActivityForResult(intent3, 0);
                break;
        }//Fin de Switch

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle("Esta por salir de la aplicación");
        alerta.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alerta.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alerta.create();
        dialog.show();
    }

    void mensaje(String msg){
        Toast toast = Toast.makeText(this,""+msg,Toast.LENGTH_LONG);
        toast.show();
    }

    void cargarUsuario(){
        //Almacenar el sitio que contiene la BD
        String url="http://parking.scienceontheweb.net/index.php/usuarios/"+MainActivity.userLogin;

        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("======>",jsonArray.toString());

                    JSONObject object = jsonArray.getJSONObject(0);
                    //Guardar los datos del usuario en un nuevo objeto USUARIOS
                    String cod_upc = object.getString("codigo");
                    String clave = object.getString("clave");
                    String nombre = object.getString("nombre");
                    String apellido = object.getString("apellido");
                    String correo = object.getString("email");
                    String telefono = object.getString("telefono");
                    String foto = object.getString("foto");
                    Integer estado = object.getInt("status");
                    sesionUsuario = new Usuarios(cod_upc, clave, nombre, apellido, correo, foto, telefono,estado);

                    //cargar datos en el Activity
                    lblCodigo.setText(cod_upc);
                    lblNombres.setText(nombre+" "+apellido);
                    lblCorreo.setText(correo);

                } catch (JSONException e) {
                    mensaje("Error al cargar usuario");
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

}
