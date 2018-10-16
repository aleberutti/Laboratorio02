package ar.edu.utn.frsf.dam.isi.laboratorio02.lista;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.utn.frsf.dam.isi.laboratorio02.R;

public class PedidosViewHolder {
    ImageButton btDetalle, btBorrar;
    ImageView iv;
    TextView tvTitulo, tvEstado, tvRestantes;

    public PedidosViewHolder(View base){
        this.btDetalle = base.findViewById(R.id.btDetalle);
        this.btBorrar = base.findViewById(R.id.btBorrar);
        this.iv = base.findViewById(R.id.imgTipo);
        this.tvTitulo = base.findViewById(R.id.tvNro);
        this.tvEstado = base.findViewById(R.id.tvEstado);
        this.tvRestantes = base.findViewById(R.id.tvRestantes);
    }
}
