package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    private static final String CANAL_MENSAJES_ID = "CANAL01";
    private PedidoRepository repositorioPedido = new PedidoRepository();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("idPedido", -1);
        int idAux = intent.getIntExtra("ID_PEDIDO", -1);
        if (idAux!=-1){
            Pedido pedido = repositorioPedido.buscarPorId(idAux);

            Intent destino = new Intent(context, AltaPedido.class);
            destino.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            destino.putExtra("Vista", pedido.getId());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, destino, 0);

            NotificationCompat.Builder mBuilder = new
                    NotificationCompat.Builder(context,
                    EstadoPedidoReceiver.CANAL_MENSAJES_ID)
                    .setSmallIcon(R.drawable.ic_action_name_1)
                    .setContentTitle("El pedido " + idAux + " está listo")
                    .setContentText("Pulsa aquí para visualizarlo")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(99, mBuilder.build());
        }else {
            Pedido pedido = repositorioPedido.buscarPorId(id);

            Intent destino = new Intent(context, AltaPedido.class);
            destino.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            destino.putExtra("Vista", pedido.getId());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, destino, 0);

            NotificationCompat.Builder mBuilder = new
                    NotificationCompat.Builder(context,
                    EstadoPedidoReceiver.CANAL_MENSAJES_ID)
                    .setSmallIcon(R.drawable.ic_action_name_1)
                    .setContentTitle("El pedido fue aceptado")
                    .setContentText("Costo total: " + pedido.getCosto())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(99, mBuilder.build());
        }
    }

//      throw new UnsupportedOperationException("Not yet implemented");
}
