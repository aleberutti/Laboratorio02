package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaRest {

    //Realiza el POST de una categoria el servidor REST
    public void crearCategoria(Categoria c){

        //Variables de conexión y stream lectura-escritura
        HttpURLConnection urlConnection= null;
        DataOutputStream printOut= null;
        InputStream in= null;

        JSONObject categoriaJSON = new JSONObject();

        try {
            categoriaJSON.put("nombre", c.getNombre());


            //Abrimos conexión - cambiar IP para probar
            URL url = new URL("http://192.168.0.12:3000/categorias");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            //Obtener el stream de salida para escribir el JSON
            printOut = new DataOutputStream(urlConnection.getOutputStream());
            String str = categoriaJSON.toString();
            byte[] jsonData = str.getBytes("UTF-8");
            printOut.write(jsonData);
            printOut.flush();

            //Leemos la respuesta y analizamos el código
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();
            int data = isw.read();

            if (urlConnection.getResponseCode() ==200 || urlConnection.getResponseCode()== 201) {
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = isw.read();
                }
                //Analizar los datos recibidos
                Log.d("LAB_04",sb.toString());
            }
            else {
                System.out.println("ERROR: No se pudo ejecutar la operación");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (MalformedURLException e1){
            e1.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }finally {

            if (printOut!=null) {
                try {
                    printOut.close();
                } catch (IOException i1) {
                    i1.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException i2) {
                    i2.printStackTrace();
                }
            }
            if (urlConnection != null)urlConnection.disconnect();
        }




    }
}
