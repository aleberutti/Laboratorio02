package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.*;

public class ListaProductos extends AppCompatActivity {

    private Spinner cmbProductosCategoria;
    private ListView lstProductos;
    private ArrayAdapter<Categoria> cmbAdapter;
    private ArrayAdapter<Producto> lstAdapter;
    private ProductoRepository pr;
    private EditText cant;
    private Button pedir;
    private Producto selected;
    private Categoria[] cats = new Categoria[0];

    //daos

    private CategoriaDao categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        cant = findViewById(R.id.edtProdCantidad);
        pedir = findViewById(R.id.btnProdAddPedido);

        Intent i = getIntent();
        int nvo = i.getIntExtra("NUEVO_PEDIDO",0);
        if (!(nvo==1)){
            cant.setEnabled(false);
            pedir.setEnabled(false);
        }

        pr= new ProductoRepository();
        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        lstProductos = (ListView) findViewById(R.id.lstProductos);

        Runnable r = new Runnable() {
            @Override
            public void run() {

//                CategoriaRest catRest = new CategoriaRest();
//
//                try {
//                    cats = catRest.listarTodas()
//                            .toArray(new Categoria[0]);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        cmbAdapter = new ArrayAdapter<Categoria>(ListaProductos.this, android.R.layout.simple_spinner_item, recuperarCategorias());
                        cmbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cmbProductosCategoria.setAdapter(cmbAdapter);

                        lstAdapter = new ArrayAdapter<Producto>(ListaProductos.this, android.R.layout.simple_list_item_single_choice, pr.buscarPorCategoria((Categoria) cmbProductosCategoria.getItemAtPosition(0)));
                        lstProductos.setAdapter(lstAdapter);

                        cmbProductosCategoria.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                                        lstAdapter.clear();
                                        lstAdapter.addAll(pr.buscarPorCategoria((Categoria) parent.getItemAtPosition(i)));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                }
                        );

                    }
                });
            }
        };

        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();

        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lstAdapter, View view, int position, long id) {
                selected = (Producto) lstAdapter.getItemAtPosition(position);
            }
        });

        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cant.getText().toString().isEmpty()){
                    Toast.makeText(ListaProductos.this,"Debe ingresar una cantidad", Toast.LENGTH_LONG).show();
                    return ;
                }else{
                    if (selected==null){
                        Toast.makeText(ListaProductos.this,"Debe ingresar un producto", Toast.LENGTH_LONG).show();
                        return ;
                    }
                }
                Intent i2 = new Intent();
                i2.putExtra("cantidad", Integer.parseInt(cant.getText().toString()));
                i2.putExtra("id", selected.getId());
                setResult(Activity.RESULT_OK, i2);
                finish();
            }
        });
    }

    private List<Categoria> recuperarCategorias(){
        List<Categoria> categoriaLista = categoriaDao.getAll();
        return categoriaLista;
    }

}
