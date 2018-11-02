package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

public class MyDatabase {

    private static MyDatabase _INSTANCIA_UNICA=null;

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyDatabase(ctx);
        return _INSTANCIA_UNICA;
    }

    private Database db;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private PedidoDao pedidoDao;

    private MyDatabase(Context ctx){
        db = Room.databaseBuilder(ctx,
                Database.class, "database-name")
                .fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
        productoDao = db.productoDao();
        pedidoDao = db.pedidoDao();

    }

    public void borrarTodo(){
        this.db.clearAllTables();
    }


    public CategoriaDao getCategoriaDao() {
        return categoriaDao;
    }

    public void setCategoriaDao(CategoriaDao categoriaDao) {
        this.categoriaDao = categoriaDao;
    }


    public ProductoDao getProductoDao() {
        return productoDao;
    }

    public void setProductoDao(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }


    public PedidoDao getPedidoDao() {
        return pedidoDao;
    }

    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

}
