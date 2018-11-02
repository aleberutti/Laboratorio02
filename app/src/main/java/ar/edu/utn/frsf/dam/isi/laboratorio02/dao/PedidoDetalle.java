package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

@Dao
public interface PedidoDetalle {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Insert
    long insert(PedidoDetalle ped_det);

    @Update
    void update(PedidoDetalle ped_det);

    @Delete
    void delete(PedidoDetalle ped_det);


}
