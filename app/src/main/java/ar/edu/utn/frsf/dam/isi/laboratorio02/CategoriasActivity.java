package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriasActivity extends AppCompatActivity {

    private EditText textoCat;
    private Button btnCrear, btnMenu;
    private CategoriaRest cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        btnMenu = (Button) findViewById(R.id.btnCategoriaVolver);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textoCat.getText().toString().isEmpty()){
                    cr = new CategoriaRest();

                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            cr.crearCategoria(new Categoria(textoCat.getText().toString()));


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CategoriasActivity.this, "¡Categoría creada con éxito!", Toast.LENGTH_LONG).show();
                                    textoCat.setText("");
                                }
                            });
                        }

                    };
                    Thread thread = new Thread(run);
                    thread.start();
                }
                else{
                    Toast.makeText(CategoriasActivity.this, "¡Debe ingresar un nombre!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(CategoriasActivity.this, MainActivity.class);
                startActivity(menu);
            }
        });

    }
}
