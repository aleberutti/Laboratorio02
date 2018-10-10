package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {

    private PedidoRepository repo = new PedidoRepository();

    public RestoMessagingService() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        int id = Integer.parseInt(remoteMessage.getData().get("ID_PEDIDO"));
        Pedido ped = repo.buscarPorId(id);
        ped.setEstado(Pedido.Estado.LISTO);
        Intent i2 = new Intent (RestoMessagingService.this, EstadoPedidoReceiver.class);
        i2.putExtra("ID_PEDIDO", id);
        sendBroadcast(i2);
    }


}
