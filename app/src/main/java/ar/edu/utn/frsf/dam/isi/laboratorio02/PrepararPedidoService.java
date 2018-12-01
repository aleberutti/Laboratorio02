package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.Service;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {

    private PedidoRepository repositorioPedido = new PedidoRepository();

    private PedidoDao pedidoDao;

    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//      List<Pedido> lista = repositorioPedido.getLista();

        List<Pedido> lista = recuperarPedidos();

        for (Pedido p : lista) {

            if (p.getEstado().equals(Pedido.Estado.ACEPTADO)) {
                p.setEstado(Pedido.Estado.EN_PREPARACION);

                Intent broadcast = new Intent(PrepararPedidoService.this, EstadoPedidoReceiver.class);
                broadcast.putExtra("idPedido", p.getId());
                broadcast.setAction("ESTADO_EN_PREPARACION");
                sendBroadcast(broadcast);

            }
        }

    }

    private List<Pedido> recuperarPedidos(){
        List<Pedido> pedidosLista = pedidoDao.getAll();
        return pedidosLista;
    }
}