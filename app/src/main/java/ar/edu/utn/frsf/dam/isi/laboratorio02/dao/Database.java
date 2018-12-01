package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.RoomDatabase;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

@android.arch.persistence.room.Database(entities = {Categoria.class,},version = 2)
public abstract class Database extends RoomDatabase {
    public abstract CategoriaDao categoriaDao();
    public abstract ProductoDao productoDao();
    public abstract PedidoDao pedidoDao();
    public abstract PedidoDetalleDao pedidoDetalleDao();
}
