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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AltaPedido extends AppCompatActivity {

    private Pedido unPedido;
    private PedidoRepository repositorioPedido;
    private ProductoRepository repositorioProducto = new ProductoRepository();

    private TextView tvCostoTotal;
    private RadioButton rbLocal, rbDomicilio;
    private EditText etCorreo, etDomicilio, etHorario;
    private ListView lvProductos;
    private Button btAgregarProducto, btQuitarProducto, btConfirmar, btVolver;

    ArrayAdapter <Producto> lstAdapter;
    List<Producto> listaProds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedido);

        tvCostoTotal = findViewById(R.id.tvTotal);

        rbDomicilio = findViewById(R.id.rbEnvioADomicilio);
        rbLocal = findViewById(R.id.rbRetiroEnLocal);

        etHorario = findViewById(R.id.etHorarioEntrega);
        etDomicilio = findViewById(R.id.etDireccionEntrega);

        etHorario.setEnabled(false);
        etDomicilio.setEnabled(false);

        rbDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHorario.setEnabled(true);
                etDomicilio.setEnabled(true);
            }
        });

        rbLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHorario.setEnabled(false);
                etHorario.setText(null);
                etDomicilio.setEnabled(false);
                etDomicilio.setText(null);
            }
        });

        btQuitarProducto = findViewById(R.id.btQuitarProducto);
        btQuitarProducto.setEnabled(false);

        lvProductos = (ListView) findViewById(R.id.lvListaProductos);

        lstAdapter = new ArrayAdapter<Producto>(this,android.R.layout.simple_list_item_single_choice, listaProds);
        lvProductos.setAdapter(lstAdapter);

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                btQuitarProducto.setEnabled(true);

                btQuitarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listaProds.remove(position);
                        lstAdapter.notifyDataSetChanged();
                        double costo=0.0;
                        for (int j = 0; j<listaProds.size(); j++){
                            costo = listaProds.get(j).getPrecio() + costo;
                        }
                        tvCostoTotal.setText("Total del pedido: $" + costo);
                    }
                });

            }
        });

        btAgregarProducto = findViewById(R.id.btAgregarProducto);
        btAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(AltaPedido.this, ListaProductos.class);
                int1.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(int1, 8);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode==8){
            Producto agregado = repositorioProducto.buscarPorId(data.getExtras().getInt("id"));
            for (int k = 0 ; k<data.getExtras().getInt("cantidad"); k++){
                listaProds.add(agregado);
            }
            double costo = 0.0;
            for (int j = 0; j<listaProds.size(); j++){
                costo = listaProds.get(j).getPrecio() + costo;
            }
            tvCostoTotal.setText("Total del pedido: $" + costo);
            lstAdapter.notifyDataSetChanged();
        }else{
            System.out.println("error");
            System.exit(0);
        }
    }
}
