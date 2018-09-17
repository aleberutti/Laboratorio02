package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.*;

public class ListaProductos extends AppCompatActivity {

    private Spinner cmbProductosCategoria;
    private ListView lstProductos;
    private ArrayAdapter<Categoria> cmbAdapter;
    private ArrayAdapter<Producto> lstAdapter;
    private ProductoRepository pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        pr= new ProductoRepository();
        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        lstProductos = (ListView) findViewById(R.id.lstProductos);

        cmbAdapter= new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_item, pr.getCategorias());
        cmbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbProductosCategoria.setAdapter(cmbAdapter);


        lstAdapter = new ArrayAdapter<Producto>(this,android.R.layout.simple_spinner_item, pr.buscarPorCategoria((Categoria)cmbProductosCategoria.getItemAtPosition(0)));
        lstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lstProductos.setAdapter(lstAdapter);

        cmbProductosCategoria.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        lstAdapter.clear();
                        lstAdapter.addAll(pr.buscarPorCategoria((Categoria)parent.getItemAtPosition(i)));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );



    }
}
