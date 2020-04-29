package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class editarUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        EditText txtCodigo, txtNombres, txtApellidos, txtMail, txtPhone;
        Button btnGuardar,btnCancelar;

        txtCodigo = (EditText) findViewById(R.id.txtCodigoEditarUser);

        txtCodigo.setText(MainActivity.userLogin);
    }
}
