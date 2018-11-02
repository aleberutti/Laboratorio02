package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

@Dao
public interface PedidoDao {

    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Insert
    long insert(Pedido ped);

    @Update
    void update(Pedido ped);

    @Delete
    void delete(Pedido ped);

}
