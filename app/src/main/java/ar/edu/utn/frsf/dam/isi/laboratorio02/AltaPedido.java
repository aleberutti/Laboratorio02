package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AltaPedido extends AppCompatActivity {

    private Pedido unPedido = new Pedido();
    private PedidoRepository repositorioPedido = new PedidoRepository();
    private ProductoRepository repositorioProducto = new ProductoRepository();

    private TextView tvCostoTotal, tvTitulo;
    private RadioButton rbLocal, rbDomicilio;
    private EditText etCorreo, etDomicilio, etHorario;
    private ListView lvProductos;
    private Button btAgregarProducto, btQuitarProducto, btConfirmar, btVolver;

    ArrayAdapter <PedidoDetalle> lstAdapter;
    List<PedidoDetalle> listaProds = new ArrayList<>();

    private boolean retirar = false;
    private double costo = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedido);

        tvCostoTotal = findViewById(R.id.tvTotal);
        tvTitulo = findViewById(R.id.tvTitulo);

        if (unPedido.getId() == null)
            tvTitulo.setText("Pedido 0");
        else
            tvTitulo.setText("Pedido " + unPedido.getId());

        rbDomicilio = findViewById(R.id.rbEnvioADomicilio);
        rbLocal = findViewById(R.id.rbRetiroEnLocal);

        etHorario = findViewById(R.id.etHorarioEntrega);
        etDomicilio = findViewById(R.id.etDireccionEntrega);

        if (rbLocal.isChecked()) {
            etHorario.setEnabled(false);
            etDomicilio.setEnabled(false);
        }

        rbDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHorario.setEnabled(true);
                etDomicilio.setEnabled(true);
                retirar = false;
            }
        });

        rbLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHorario.setEnabled(false);
                etHorario.setText(null);
                etDomicilio.setEnabled(false);
                etDomicilio.setText(null);
                retirar = true;
            }
        });

        btQuitarProducto = findViewById(R.id.btQuitarProducto);
        btQuitarProducto.setEnabled(false);

        lvProductos = (ListView) findViewById(R.id.lvListaProductos);

        lstAdapter = new ArrayAdapter<PedidoDetalle>(this, android.R.layout.simple_list_item_single_choice, listaProds);
        lvProductos.setAdapter(lstAdapter);

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                btQuitarProducto.setEnabled(true);

                btQuitarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listaProds.remove(position);
                        lstAdapter.notifyDataSetChanged();
                        costo = hallarCosto();
                        tvCostoTotal.setText("Total del pedido: $" + Double.parseDouble(String.format("%.2f", costo)));
                        unPedido.setCosto(Double.parseDouble(String.format("%.2f", costo)));
                    }
                });

            }
        });

        btAgregarProducto = findViewById(R.id.btAgregarProducto);
        btAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(AltaPedido.this, ListaProductos.class);
                int1.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(int1, 8);
            }
        });


        btConfirmar = findViewById(R.id.btConfirmar);
        etCorreo = findViewById(R.id.etCorreo);

        btVolver = findViewById(R.id.btVolver);

        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Inicializacion
        Intent in = getIntent();
        int nvo = in.getIntExtra("Code", -2);
        if (nvo != -2) {
            listaProds.addAll(repositorioPedido.buscarPorId(nvo).getDetalle());
            lstAdapter.notifyDataSetChanged();
            costo = hallarCosto();
            tvCostoTotal.setText("Total del pedido: $" + costo);
            unPedido = repositorioPedido.buscarPorId(nvo);
            tvTitulo.setText("Pedido " + nvo);

            etCorreo.setText(unPedido.getMailContacto());
            retirar = unPedido.getRetirar();
            if (retirar) {
                rbLocal.setChecked(true);
                etHorario.setEnabled(false);
                etHorario.setText(null);
                etDomicilio.setEnabled(false);
                etDomicilio.setText(null);
            } else {
                rbDomicilio.setChecked(true);
                etHorario.setEnabled(true);
                etDomicilio.setEnabled(true);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                etHorario.setText(sdf.format(unPedido.getFecha()));
                etDomicilio.setText(unPedido.getDireccionEnvio());
            }


            btConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GregorianCalendar hora = new GregorianCalendar();
                    String[] horaIngresada = null;
                    if (etHorario.getText().toString().isEmpty())
                        horaIngresada = null;
                    else
                        horaIngresada = etHorario.getText().toString().split(":");
                    int valorHora = 0, valorMinuto = 0;
                    if (horaIngresada != null) {
                        valorHora = Integer.valueOf(horaIngresada[0]);
                        valorMinuto = Integer.valueOf(horaIngresada[1]);
                    }
                    if (!validarCampos(horaIngresada))
                        return;

                    hora.set(Calendar.HOUR_OF_DAY, valorHora);
                    hora.set(Calendar.MINUTE, valorMinuto);
                    hora.set(Calendar.SECOND, Integer.valueOf(0));

                    unPedido.setFecha(hora.getTime());
                    unPedido.setDetalle(listaProds);
                    unPedido.setEstado(Pedido.Estado.REALIZADO);


                    Intent historial = new Intent(AltaPedido.this, HistorialPedidos.class);
                    startActivity(historial);

                }
            });
        }else {
            nvo = in.getIntExtra("Vista", -2);
            if (nvo != -2) {
                etDomicilio.setEnabled(false);
                etCorreo.setEnabled(false);
                etHorario.setEnabled(false);
                rbLocal.setEnabled(false);
                rbDomicilio.setEnabled(false);
                btAgregarProducto.setEnabled(false);
                btQuitarProducto.setEnabled(false);
                btConfirmar.setEnabled(false);
                lvProductos.setEnabled(false);

                listaProds.addAll(repositorioPedido.buscarPorId(nvo).getDetalle());
                lstAdapter.notifyDataSetChanged();
                costo = hallarCosto();
                tvCostoTotal.setText("Total del pedido: $" + costo);
                unPedido = repositorioPedido.buscarPorId(nvo);
                tvTitulo.setText("Pedido " + nvo);

                etCorreo.setText(unPedido.getMailContacto());
                retirar = unPedido.getRetirar();
                if (!retirar) {
                    rbDomicilio.setChecked(true);
                    etHorario.setText(unPedido.getFecha().toString());
                    etDomicilio.setText(unPedido.getDireccionEnvio());
                }else{
                    rbLocal.setChecked(true);
                }
            }else {
                btConfirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GregorianCalendar hora = new GregorianCalendar();
                        String[] horaIngresada = null;
                        if (etHorario.getText().toString().isEmpty())
                            horaIngresada = null;
                        else
                            horaIngresada = etHorario.getText().toString().split(":");
                        int valorHora = 0, valorMinuto = 0;
                        try {
                            if (horaIngresada != null) {
                                valorHora = Integer.valueOf(horaIngresada[0]);
                                valorMinuto = Integer.valueOf(horaIngresada[1]);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(AltaPedido.this, "Debe ingresar un horario correcto.\nEjemplo (22:59)", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!validarCampos(horaIngresada))
                            return;
                        hora.set(Calendar.HOUR_OF_DAY, valorHora);
                        hora.set(Calendar.MINUTE, valorMinuto);
                        hora.set(Calendar.SECOND, Integer.valueOf(0));

                        unPedido.setFecha(hora.getTime());
                        unPedido.setDetalle(listaProds);
                        unPedido.setEstado(Pedido.Estado.REALIZADO);
                        unPedido.setDireccionEnvio(etDomicilio.getText().toString());
                        unPedido.setMailContacto(etCorreo.getText().toString());
                        unPedido.setRetirar(retirar);
                        repositorioPedido.guardarPedido(unPedido);

                        Intent historial = new Intent(AltaPedido.this, HistorialPedidos.class);
                        startActivity(historial);

                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode==8){

            PedidoDetalle detalle = new PedidoDetalle(data.getExtras().getInt("cantidad"), repositorioProducto.buscarPorId(data.getExtras().getInt("id")));

            listaProds.add(detalle);

            costo = hallarCosto();
            tvCostoTotal.setText("Total del pedido: $" + Double.parseDouble(String.format("%.2f", costo)));
            unPedido.setCosto(Double.parseDouble(String.format("%.2f", costo)));

            lstAdapter.notifyDataSetChanged();

        }else{
            System.out.println("error");
            System.exit(0);
        }
    }


    private double hallarCosto(){
        costo = 0.0;
        for (int j = 0; j<listaProds.size(); j++){
            costo = (listaProds.get(j).getProducto().getPrecio()*listaProds.get(j).getCantidad()) + costo;
        }
        unPedido.setCosto(Double.parseDouble(String.format("%.2f", costo)));
        return costo;
    }

    private boolean validarCampos (String[] horaIngresada){
        if (etCorreo.getText().toString().isEmpty()) {
            Toast.makeText(AltaPedido.this,"Debe ingresar un mail", Toast.LENGTH_LONG).show();
            return false;
        }else {
            if (etDomicilio.getText().toString().isEmpty() && !retirar){
                Toast.makeText(AltaPedido.this,"Debe ingresar un domicilio", Toast.LENGTH_LONG).show();
                return false;
            }else{
                if (!retirar) {
                    //Validar fecha
                    try {
                        int valorHora = Integer.valueOf(horaIngresada[0]);
                        int valorMinuto = Integer.valueOf(horaIngresada[1]);
                        if (valorHora < 0 || valorHora > 23) {
                            Toast.makeText(AltaPedido.this, "La hora ingresada (" + valorHora + ") es incorrecta", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (valorMinuto < 0 || valorMinuto > 59) {
                            Toast.makeText(AltaPedido.this, "Los minutos (" + valorMinuto + ") son incorrectos", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        return true;
                    }catch (NullPointerException e){
                        Toast.makeText(AltaPedido.this, "Debe ingresar un horario", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else
                    return true;
            }
        }
    }


}
