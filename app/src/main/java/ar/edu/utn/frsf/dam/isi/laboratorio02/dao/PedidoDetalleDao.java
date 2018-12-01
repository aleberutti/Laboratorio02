package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PedidoDetalleDao {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalleDao> getAll();

    @Insert
    long insert(PedidoDetalleDao ped_det);

    @Update
    void update(PedidoDetalleDao ped_det);

    @Delete
    void delete(PedidoDetalleDao ped_det);


}
