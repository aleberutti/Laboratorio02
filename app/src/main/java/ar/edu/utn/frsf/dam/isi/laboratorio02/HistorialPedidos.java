package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.lista.DataModel;
import ar.edu.utn.frsf.dam.isi.laboratorio02.lista.PedidoAdapter;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class HistorialPedidos extends AppCompatActivity {

    private ListView lvHistorialPedidos;
    private Button btNuevo, btMenu;

    private PedidoDao pedidoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        lvHistorialPedidos = findViewById(R.id.lvHistorialPedidos);
        btNuevo = findViewById(R.id.btHistorialNuevo);
        btMenu = findViewById(R.id.btHistorialMenu);

        List <DataModel> lstdm = new ArrayList<DataModel>();
        for (int p=0; p<recuperarPedidos().size(); p++){
            Pedido ped = recuperarPedidos().get(p);
            lstdm.add(new DataModel(ped.getId(), ped.getEstado()));
        }

        PedidoAdapter lstAdapter = new PedidoAdapter(this, lstdm);
        lvHistorialPedidos.setAdapter(lstAdapter);


        lvHistorialPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (recuperarPedidos().get(position).getEstado()==Pedido.Estado.REALIZADO || recuperarPedidos().get(position).getEstado()==Pedido.Estado.ACEPTADO) {
                    Intent inte = new Intent(HistorialPedidos.this, AltaPedido.class);
                    inte.putExtra("Code", recuperarPedidos().get(position).getId());
                    startActivity(inte);
                }else{
                    Toast.makeText(HistorialPedidos.this,"El pedido no se puede modificar", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent (HistorialPedidos.this, AltaPedido.class);
                startActivity(int1);
            }
        });

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent (HistorialPedidos.this, MainActivity.class);
                startActivity(int1);
            }
        });

    }

    private List<Pedido> recuperarPedidos(){
        List<Pedido> pedidosLista = pedidoDao.getAll();
        return pedidosLista;
    }


}
