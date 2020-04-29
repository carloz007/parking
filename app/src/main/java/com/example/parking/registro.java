package com.example.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registro extends AppCompatActivity implements View.OnClickListener {

    private String getCodigo, getNombres, getApellidos, getMail, getPhone, getClave, getClave2;

    EditText txtCodigo, txtNombres, txtApellidos, txtMail, txtPhone, txtClave, txtClave2;
    Button btnRegistrar, btnLimpiar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // findViewById() ->se encarga de acceder a un recurso para cambiar una de sus propiedades

        //vincular EditText a variables
        txtCodigo=(EditText) findViewById(R.id.txtCodigoRegistro);
        txtNombres=(EditText) findViewById(R.id.txtNombresRegistro);
        txtApellidos=(EditText) findViewById(R.id.txtApellidosRegistro);
        txtMail=(EditText) findViewById(R.id.txtMailRegistro);
        txtPhone=(EditText) findViewById(R.id.txtPhoneRegistro);
        txtClave=(EditText) findViewById(R.id.txtClaveRegistro);
        txtClave2=(EditText) findViewById(R.id.txtClave2Registro);

        //vincular Button a variables
        btnRegistrar=(Button) findViewById(R.id.btnRegistrarRegistro);
        btnLimpiar=(Button) findViewById(R.id.btnLimpiarRegistro);
        //Escuchar el evento click de los botones.
        btnRegistrar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegistrarRegistro:
                getCodigo  = txtCodigo.getText().toString();
                getNombres  = txtNombres.getText().toString();
                getApellidos  = txtApellidos.getText().toString();
                getMail  = txtMail.getText().toString();
                getPhone  = txtPhone.getText().toString();
                getClave  = txtClave.getText().toString();
                getClave2  = txtClave2.getText().toString();
                validar();
                break;
            case R.id.btnLimpiarRegistro:
                limpiar();
                break;
        }//Fin de Switch
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        //alerta.setMessage("Regresara a la ventana de Login");
        alerta.setTitle("¿Desea salir del registro?");
        alerta.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    void limpiar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Se borraran todos los datos ingresados");
        msg.setTitle("¿Desea cancelar el registro?");
        msg.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtCodigo.setText("");
                txtNombres.setText("");
                txtApellidos.setText("");
                txtMail.setText("");
                txtPhone.setText("");
                txtClave.setText("");
                txtClave2.setText("");
                txtCodigo.setError(null);
                txtNombres.setError(null);
                txtApellidos.setError(null);
                txtMail.setError(null);
                txtPhone.setError(null);
                txtClave.setError(null);
                txtClave2.setError(null);
                txtCodigo.requestFocus();
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

    void mensaje(String msg){
        Toast toast = Toast.makeText(this,""+msg,Toast.LENGTH_LONG);
        toast.show();
    }

    void validar(){

        if (getCodigo.isEmpty() || validadCodigo(getCodigo) == false || getNombres.isEmpty()
                || getApellidos.isEmpty() || getClave.isEmpty()|| getClave2.isEmpty()
                || (!getMail.isEmpty() && validarMail(getMail) == false)
                || (!getPhone.isEmpty() && validarPhone(getPhone) == false)
        ){ //Validación de campos obligatorios
            if (getCodigo.isEmpty()) txtCodigo.setError("Este campo es obligatorio");
                else if (validadCodigo(getCodigo) == false) txtCodigo.setError("El código NO es válido");
            if (getNombres.isEmpty()) txtNombres.setError("Este campo es obligatorio");
            if (getApellidos.isEmpty()) txtApellidos.setError("Este campo es obligatorio");
            if (getClave.isEmpty()) txtClave.setError("Este campo es obligatorio");
            if (getClave2.isEmpty()) txtClave2.setError("Este campo es obligatorio");
            //Validación de campos opcionales
            if (!getMail.isEmpty() && validarMail(getMail) == false) txtMail.setError("El email NO es válido");
            if (!getPhone.isEmpty() && validarPhone(getPhone) == false) txtPhone.setError("El teléfono NO es válido");
        }else {
            if (validarPassword(getClave) == false){
                txtClave.setError("La clave NO es válido");
                txtClave.requestFocus();
                txtClave2.setText("");
                mensaje("At least 8 chars\n" +"\n" +"Contains at least one digit\n" +"\n" +
                        "Contains at least one lower alpha char and one upper alpha char\n" +"\n" +
                        "Contains at least one char within a set of special chars (@#%$^& etc.)\n" +
                        "\n" + "Does not contain space, tab, etc.");
            }else{
                if (!getClave.equals(getClave2)){
                    txtClave2.setText("");
                    txtClave2.setError("Las claves deben ser iguales");
                    txtClave2.requestFocus();
                }else {
                    registrarUsuario();
                }//fin de else
            }//fin de else
        }//fin de else
    }

    boolean validarMail(String email) {
        // Patron para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    boolean validarPhone(String phone) {
        Pattern pattern = Pattern.compile("^(([(]?(\\d{2,4})[)]?)|(\\d{2,4})|([+1-9]+\\d{1,2}))?[-\\s]?(\\d{2,3})?[-\\s]?((\\d{7,8})|(\\d{3,4}[-\\s]\\d{3,4}))$");
        Matcher mather = pattern.matcher(phone);
        return mather.find();
    }

    boolean validarPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%&/()=?¡¿'|,;.:@^_+-=])(?=\\S+$).{8,}$");
        Matcher mather = pattern.matcher(password);
        return mather.find();
    }

    boolean validadCodigo(String codigo){
        // Patron para validar el codigo
        Pattern pattern = Pattern.compile("^[A-Za-z]{1}+[A-Za-z0-9]{9,10}$");
        Matcher mather = pattern.matcher(codigo);
        return mather.find();
    }

    void registrarUsuario(){

        final String url="http://parking.scienceontheweb.net/index.php/usuarios";

        StringRequest stringRequest= new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mensaje("Registro Ingresado");
                txtCodigo.setText("");
                txtNombres.setText("");
                txtApellidos.setText("");
                txtMail.setText("");
                txtPhone.setText("");
                txtClave.setText("");
                txtClave2.setText("");
                txtCodigo.setError(null);
                txtNombres.setError(null);
                txtApellidos.setError(null);
                txtMail.setError(null);
                txtPhone.setError(null);
                txtClave.setError(null);
                txtClave2.setError(null);
                txtCodigo.requestFocus();
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("======>", error.toString());
            }
        }
        ) {
            //Parametos a enviar al servidor
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("codigo",getCodigo);
                parametros.put("nombre",getNombres);
                parametros.put("apellido",getApellidos);
                parametros.put("correo",getMail);
                parametros.put("telefono",getPhone);
                parametros.put("foto","");
                parametros.put("clave",getClave);
                return parametros;
            }
        };
        //Enviar solicitud
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

