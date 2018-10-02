package ar.edu.utn.frsf.dam.isi.laboratorio02.lista;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class DataModel {

    private String nombre;
    private String estado;
    private Integer id;

    public DataModel(int id, Pedido.Estado estado){
        this.nombre= "Pedido " + id;
        this.id = id;
        if (estado==Pedido.Estado.EN_PREPARACION){
            this.estado = "EN PREPARACION";
        }
        else{
            this.estado = estado.toString();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) { this.estado = estado; }

    public Integer getId() { return id; }
}
