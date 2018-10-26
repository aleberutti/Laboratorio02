package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;

import java.io.IOException;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionProductoActivity extends AppCompatActivity {

    private Button btnGuardar, btnBuscar, btnBorrar;
    private ToggleButton crearOBuscar;
    private EditText edtNombre, edtDesc, edtPrecio, edtBuscarPorID;
    private Spinner spCategorias;
    private Boolean flagActualizacion;
    private ArrayAdapter<Categoria> adaptadorspin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);

        btnGuardar = findViewById(R.id.btnGuardar);
        edtNombre = findViewById(R.id.edtNombre);
        edtDesc = findViewById(R.id.edtDesc);
        edtPrecio = findViewById(R.id.edtPrecio);
        spCategorias = findViewById(R.id.spCategorias);
        crearOBuscar = findViewById(R.id.crearOBuscar);
        edtBuscarPorID = findViewById(R.id.edtBuscarPorID);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBorrar = findViewById(R.id.btnBorrar);

        flagActualizacion = false;
        crearOBuscar.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        edtBuscarPorID.setEnabled(false);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                Categoria[] cats = null;
                try {
                    cats = catRest.listarTodas().toArray(new Categoria[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final Categoria[] catsAux = cats;
                try {
                    cats = catRest.listarTodas().toArray(new Categoria[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adaptadorspin = new ArrayAdapter<Categoria>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, catsAux);
                        spCategorias.setAdapter(adaptadorspin);
                        spCategorias.setSelection(0);
                    }
                });
            }
        };
        Thread hiloCargarComo = new Thread(r);
        hiloCargarComo.start();

        crearOBuscar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,
                                        boolean isChecked) {
               flagActualizacion = isChecked;
               btnBuscar.setEnabled(isChecked);
               btnBorrar.setEnabled(isChecked);
               edtBuscarPorID.setEnabled(isChecked);
           }
        });


        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall = clienteRest.borrar(Integer.parseInt(edtBuscarPorID.getText().toString()));
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        edtNombre.setText(null);
                        edtPrecio.setText(null);
                        edtDesc.setText(null);
                        spCategorias.setSelection(-1);
                        edtBuscarPorID.setText(null);
                        Toast.makeText(GestionProductoActivity.this,"El producto ha sido borrado con éxito", Toast.LENGTH_LONG).show();
                        return ;
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        return ;
                    }
                });
            }
        });


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall = clienteRest.buscarProductoPorId(Integer.parseInt(edtBuscarPorID.getText().toString()));
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        edtNombre.setText(response.body().getNombre());
                        edtDesc.setText(response.body().getDescripcion());
                        edtPrecio.setText(response.body().getPrecio().toString());
                        spCategorias.setSelection(response.body().getCategoria().getId()-1);
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        return ;
                    }
                });
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto p = new Producto(edtNombre.getText().toString(), edtDesc.getText().toString(), Double.parseDouble(edtPrecio.getText().toString()), (Categoria) spCategorias.getSelectedItem());

                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);

                Call<Producto> altaCall;

                if (!flagActualizacion) {
                    altaCall = clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this,"El producto ha sido creado con éxito", Toast.LENGTH_LONG).show();
                            return ;
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                            return ;
                        }
                    });
                }else{
                    altaCall = clienteRest.actualizarProducto(Integer.parseInt(edtBuscarPorID.getText().toString()), p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this,"El producto ha sido actualizado", Toast.LENGTH_LONG).show();
                            return ;
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                            return ;
                        }
                    });
                }
            }
        });



    }
}
