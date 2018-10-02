package ar.edu.utn.frsf.dam.isi.laboratorio02.lista;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.AltaPedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.lista.PedidosViewHolder;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PedidoAdapter extends ArrayAdapter<DataModel> {

    private Context ctx;
    private PedidoRepository repositorio = new PedidoRepository();
    private DataModel pedido;
    private List<DataModel> lstdm;
    private PedidoAdapter pa = this;

    public PedidoAdapter(Context act, List<DataModel> lista){
        super(act, R.layout.fila_historial, lista);
        this.ctx = act;
        this.lstdm = lista;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.ctx);
        View fila_historial = convertView;

        PedidosViewHolder holder;
        Pedido ped = repositorio.buscarPorId(pedido.getId());

        if (fila_historial == null){

            fila_historial = inflater.inflate(R.layout.fila_historial, parent, false);

            holder = new PedidosViewHolder(fila_historial);

            holder.btBorrar = convertView.findViewById(R.id.btBorrar);
            holder.btBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer) v.getTag();
                    getItem(pos).setEstado("CANCELADO");
                    repositorio.buscarPorId(getItem(pos).getId()).setEstado(Pedido.Estado.CANCELADO);
                }
            });

            holder.btDetalle = convertView.findViewById(R.id.btDetalle);
            holder.btDetalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer) v.getTag();
                    Intent inte = new Intent(ctx, AltaPedido.class);
                    inte.putExtra("Vista", getItem(pos).getId());
                    ctx.startActivity(inte);
                }
            });

            pedido = super.getItem(position);

            holder.btBorrar.setImageResource(R.drawable.ic_action_delete);
            holder.btBorrar.setEnabled(true);
            holder.iv.setImageResource(R.drawable.ic_action_name);

            fila_historial.setTag(holder);
        }
        else{
            holder = (PedidosViewHolder) convertView.getTag();
        }



//        if (holder==null){
//
//            holder = new PedidosViewHolder(fila_historial);
//
//            holder.btBorrar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Integer pos = (Integer) v.getTag();
//                    getItem(pos).setEstado("CANCELADO");
//                    repositorio.buscarPorId(getItem(pos).getId()).setEstado(Pedido.Estado.CANCELADO);
//                }
//            });
//
//            holder.btDetalle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Integer pos = (Integer) v.getTag();
//                    Intent inte = new Intent(ctx, AltaPedido.class);
//                    inte.putExtra("Vista", getItem(pos).getId());
//                    ctx.startActivity(inte);
//                }
//            });
//
//            fila_historial.setTag(holder);
//        }

//        pedido = super.getItem(position);
//        Pedido ped = repositorio.buscarPorId(pedido.getId());
//        holder.btBorrar.setImageResource(R.drawable.ic_action_delete);
//        holder.btBorrar.setEnabled(true);
//        holder.iv.setImageResource(R.drawable.ic_action_name);


//
//        TextView tvTitulo = fila_historial.findViewById(R.id.tvNro);
//        TextView tvEstado = fila_historial.findViewById(R.id.tvEstado);

        switch (ped.getEstado()){
            case LISTO:
                holder.tvEstado.setTextColor(Color.DKGRAY);
                break;
            case ENTREGADO:
                holder.tvEstado.setTextColor(Color.BLUE);
                break;
            case CANCELADO:
            case RECHAZADO:
                holder.tvEstado.setTextColor(Color.RED);
                break;
            case ACEPTADO:
                holder.tvEstado.setTextColor(Color.GREEN);
                break;
            case EN_PREPARACION:
                holder.tvEstado.setTextColor(Color.MAGENTA);
                break;
            case REALIZADO:
                holder.tvEstado.setTextColor(Color.BLUE);
                break;
        }

//        TextView tvRestantes = fila_historial.findViewById(R.id.tvRestantes);

//        ImageView iv = fila_historial.findViewById(R.id.imgTipo);

//        ImageButton btBorrar = fila_historial.findViewById(R.id.btBorrar);
//        ImageButton btDetalle = fila_historial.findViewById(R.id.btDetalle);

        if ((pedido.getEstado() != "ACEPTADO") && (pedido.getEstado() != "REALIZADO") && (pedido.getEstado() != "EN PREPARACION")) {
            holder.btBorrar.setEnabled(false);
            holder.btBorrar.setImageResource(R.drawable.ic_canceled);
        }

        if (ped.getRetirar()){
            holder.iv.setImageResource(R.drawable.ic_action_name_1);
        }

        holder.tvTitulo.setText(pedido.getNombre());
        holder.tvEstado.setText("Estado: " + pedido.getEstado());

        int cant =0;
        for (int y=0; y<ped.getDetalle().size(); y++){
            cant = ped.getDetalle().get(y).getCantidad() + cant;
        }

        if (!ped.getRetirar()) {
            holder.tvRestantes.setText("Correo: " + ped.getMailContacto() + "\nPrecio: $" + ped.getCosto() + "\n" + "Horario: " + ped.getFecha().toString().substring(0, 19)
                                + "\nCantidad de productos: " + cant);
        }else{
            holder.tvRestantes.setText("Correo: " + ped.getMailContacto() + "\nPrecio: $" + ped.getCosto() + "\n" + "Cantidad de productos: " + cant);
        }

//        holder.btDetalle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent inte = new Intent(ctx, AltaPedido.class);
//                inte.putExtra("Vista", pedido.getId());
//                ctx.startActivity(inte);
//            }
//        });
//
//        holder.btBorrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                repositorio.buscarPorId(pedido.getId()).setEstado(Pedido.Estado.CANCELADO);
//                pedido.setEstado("CANCELADO");
//                pa.notifyDataSetChanged();
//            }
//        });

        holder.tvRestantes.setTag(position);
        holder.tvEstado.setTag(position);
        holder.btBorrar.setTag(position);
        holder.btDetalle.setTag(position);
        holder.iv.setTag(position);
        holder.tvTitulo.setTag(position);

        return fila_historial;
    }


    private static class ViewHolder{
        private TextView
    }

}
