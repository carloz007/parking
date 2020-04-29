package com.example.parking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declarar variables
    private String passLogin;
    Button btnLogin, btnRegister;
    EditText txtUsuario,txtClave;

    static String userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Oculta la barra de menu
        getSupportActionBar().hide();

        // findViewById() ->se encarga de acceder a un recurso para cambiar una de sus propiedades
        //Escuchar el evento click de los botones de forma directa
        //((Button) findViewById(R.id.btnLogin)).setOnClickListener(this);

        //vincular Button y EditText a variables
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtUsuario = (EditText) findViewById(R.id.txtUser);
        txtClave = (EditText) findViewById(R.id.txtPass);

        //Escuchar el evento click de las variables
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //if (v==btnLogin)
        userLogin = txtUsuario.getText().toString();
        passLogin = txtClave.getText().toString();
        switch (v.getId()) {
            case R.id.btnLogin:

                if (userLogin.isEmpty() || passLogin.isEmpty()) {
                    if (userLogin.isEmpty()){
                        txtUsuario.setError("El usuario es obligatorio");
                        txtUsuario.requestFocus();  }
                    if(passLogin.isEmpty()){
                        txtClave.setError("La clave es obligatoria");
                        txtClave.requestFocus();}
                }else
                    buscarPass(userLogin);
                break;
            case R.id.btnRegister:
                limpiar();
                Intent intent = new Intent (v.getContext(), registro.class);
                startActivityForResult(intent, 0);
                break;

        }//Fin de Switch

        //Obtener los valores de cada caja de texto
        //user = ((EditText) findViewById(R.id.txtUser)).getText().toString();
        //pass = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        //((TextView) findViewById(R.id.txtWelcome)).setText(String.valueOf(user+pass));

    }

    void buscarPass(String user){
        //Almacenar el json que contiene la clave
        String url="http://parking.scienceontheweb.net/index.php/pass3/"+user;

        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //JSONArray jsonArray = new JSONArray(response);
                    //Log.i("======>",jsonArray.toString());

                    final String passBD;
                    JSONObject object = new JSONObject(response); //jsonArray.getJSONObject(0);
                    //Guardar la clave del registro seleccionado
                    passBD = object.getString("clave");
                    validarClave(passBD);
                } catch (JSONException e) {
                    mensaje("EL USUARIO NO EXISTE");
                    txtUsuario.requestFocus();
                    limpiar();
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

    void validarClave(String clave){
        if (clave.equals(passLogin)){
            //cambiar a activity usuario
            Intent intent = new Intent (this, usuario.class);
            startActivityForResult(intent, 0);
            //Cerrar activity de login
            finish();
        }else{
            txtClave.setError("Contraseña incorrecta");
            txtClave.setText("");
            txtClave.requestFocus();
        }
    }

    void limpiar(){
        txtUsuario.setText("");
        txtClave.setText("");
        txtUsuario.setError(null);
        txtClave.setError(null);
    }

    void mensaje(String msg){
        Toast toast = Toast.makeText(this,""+msg,Toast.LENGTH_LONG);
        toast.show();
    }
}

