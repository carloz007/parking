package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cambiarClave extends AppCompatActivity implements View.OnClickListener {

    EditText txtCodigo, txtPass1, txtPass2;
    Button btnGuardar,btnCancelar;
    String clave1, clave2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_clave);

        txtCodigo = (EditText) findViewById(R.id.txtCodigoCambiarClave);
        txtPass1 = (EditText) findViewById(R.id.txtClaveCambiarClave);
        txtPass2 = (EditText) findViewById(R.id.txtClave2CambiarClave);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCambiarClave);
        btnGuardar = (Button) findViewById(R.id.btnGuardarCambiarClave);

        txtCodigo.setText(MainActivity.userLogin);

        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancelarCambiarClave:
                onBackPressed();
                break;
            case R.id.btnGuardarCambiarClave:
                clave1=txtPass1.getText().toString();
                clave2=txtPass2.getText().toString();
                validar();
                break;
        }
    }


    void mensaje(String msg){
        Toast toast = Toast.makeText(this,""+msg,Toast.LENGTH_LONG);
        toast.show();
    }


    void validar(){

        if (clave1.isEmpty() || clave2.isEmpty()){ //Validación de campos obligatorios
            if (clave1.isEmpty()) txtPass1.setError("Este campo es obligatorio");
            if (clave2.isEmpty()) txtPass2.setError("Este campo es obligatorio");
        }else if (validarPassword(clave1) == false){
            txtPass1.setError("La clave NO es válido");
            txtPass1.requestFocus();
            txtPass2.setText("");
            mensaje("At least 8 chars\n" +"\n" +"Contains at least one digit\n" +"\n" +
                    "Contains at least one lower alpha char and one upper alpha char\n" +"\n" +
                    "Contains at least one char within a set of special chars (@#%$^& etc.)\n" +
                    "\n" + "Does not contain space, tab, etc.");
        }else{
            if (!clave1.equals(clave2)){
                txtPass2.setText("");
                txtPass2.setError("Las claves deben ser iguales");
                txtPass2.requestFocus();
            }else {
                cambiarClave();
            }//fin de else
        }//fin de else
    }

    boolean validarPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%&/()=?¡¿'|,;.:@^_+-=])(?=\\S+$).{8,}$");
        Matcher mather = pattern.matcher(password);
        return mather.find();
    }

    void cambiarClave() { mensaje("cambiar");

        final String url = "http://parking.scienceontheweb.net/index.php/usuarios";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mensaje("Cambio realizado");
                txtPass1.setText("");
                txtPass2.setText("");
                txtPass1.setError(null);
                txtPass2.setError(null);
                txtPass1.requestFocus();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("======>", error.toString());
            }
        }
        ) {
            //Parametos a enviar al servidor
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("codigo", MainActivity.userLogin);
                parametros.put("clave", clave1);
                return parametros;
            }
        };
        //Enviar solicitud
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
