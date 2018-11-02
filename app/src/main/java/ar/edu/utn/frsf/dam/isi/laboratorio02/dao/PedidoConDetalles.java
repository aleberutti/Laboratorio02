package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class PedidoConDetalles {

    @Embedded
    public Pedido pedido;

    @Relation(parentColumn = "id", entityColumn = "idPedidoAsignado", entity = PedidoDetalle.class)
    public List<PedidoDetalle> detalles;

}
