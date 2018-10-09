package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    private PedidoRepository repositorioPedido = new PedidoRepository();

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("idPedido", 1);

        Pedido pedido = repositorioPedido.buscarPorId(id);

        Toast.makeText(context,"Pedido para " + pedido.getMailContacto() + " ha cambiado al estado ACEPTADO", Toast.LENGTH_LONG).show();

//      throw new UnsupportedOperationException("Not yet implemented");
    }
}
