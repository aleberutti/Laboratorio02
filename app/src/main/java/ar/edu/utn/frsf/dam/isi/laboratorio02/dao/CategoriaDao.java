package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public interface CategoriaDao {

    @Query("SELECT * FROM Categoria")
    List<Categoria> getAll();

    @Insert
    long insert(Categoria emp);

    @Update
    void update(Categoria emp);

    @Delete
    void delete(Categoria emp);

}
