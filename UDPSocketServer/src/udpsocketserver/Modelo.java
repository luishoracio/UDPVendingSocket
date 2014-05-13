/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package udpsocketserver;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.text.DecimalFormat;
/**
 *
 * @author luishoracio
 */
public class Modelo {
    String idMaquina = "";
    String idUsuario = "";
    String numTelefono = "";
    String idOperacion = "";
    String ixMensaje = "";
    String itNumber = "";
    String itPrice = "";
    String revalue;
    String cancelacion;
    
    private byte[] arreglo;
    
    Modelo(){}
    
    Modelo(byte[] buffer) {
        arreglo = buffer;
    }
    
    public byte[] consultaSaldoCredencial(){
        
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 2, 17));
        idUsuario = arrayToString(Arrays.copyOfRange(arreglo, 17, 27));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 27, 28));
        
        ModeloJson usuario = buscarOperacion(idMaquina, idUsuario);
        
        byte[] respuesta = null;
        
        if (usuario == null){
            respuesta = new byte[]{21,21,23};
        }
        else{
            respuesta = new byte[]{21,21,22};
            respuesta = concatenarRespuesta(respuesta, usuario.operacion);
            respuesta = concatenarRespuesta(respuesta, formatNumberDecimal( usuario.saldoRecarga, 8 ));
            respuesta = concatenarRespuesta(respuesta, formatNumberDecimal( usuario.saldoSubsidio, 8));
            respuesta = concatenarRespuesta(respuesta, formatNumberDecimal( usuario.saldoNomina, 8));
            respuesta = concatenarRespuesta(respuesta, usuario.modoProductoNoVenta +"");
            respuesta = concatenarRespuesta(respuesta, formatModoVenta(usuario.numeroArticulos, 5));
            respuesta = concatenarRespuesta(respuesta, usuario.mdbIdArticulo);
            respuesta = concatenarRespuesta(respuesta, new byte[]{30,35,38, 0x2F});
        }
        
        return respuesta;
    }
    
    public byte[] consultaSaldoTransfer(){
        
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 2, 17));
        numTelefono = arrayToString(Arrays.copyOfRange(arreglo, 17, 27));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 27, 28));
        
        
        byte[] respuesta = null;
        ModeloJson usuario = buscarOperacion(idMaquina, numTelefono);
        
        if (usuario == null){
            respuesta = new byte[]{21,21,23};
        }
        else{
            respuesta = new byte[]{21,21,21};
            respuesta = concatenarRespuesta(respuesta, usuario.operacion);
            respuesta = concatenarRespuesta(respuesta, formatNumberDecimal( usuario.saldoRecarga, 8 ));
            respuesta = concatenarRespuesta(respuesta, formatNumberDecimal( usuario.saldoSubsidio, 8));
            respuesta = concatenarRespuesta(respuesta, formatNumberDecimal( usuario.saldoNomina, 8));
            respuesta = concatenarRespuesta(respuesta, usuario.modoProductoNoVenta +"");
            respuesta = concatenarRespuesta(respuesta, formatModoVenta(usuario.numeroArticulos, 5));
            respuesta = concatenarRespuesta(respuesta, usuario.mdbIdArticulo);
            respuesta = concatenarRespuesta(respuesta, new byte[]{30,35,38, 0x2F});
        }
        return respuesta;
    }
    
    public byte[] consultaSaldoAPN(){
        
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 2, 17));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 17, 18));
        
        byte[] respuesta = new byte[]{21,21,24};
        
        return respuesta;
    }
    
    public byte[] peticionVentaCashless(){
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 2, 17));
        idOperacion = arrayToString(Arrays.copyOfRange(arreglo, 17, 27));
        itNumber = arrayToString(Arrays.copyOfRange(arreglo, 27, 32));
        itPrice = arrayToString(Arrays.copyOfRange(arreglo, 32, 37));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 37, 38));
        
        byte[] respuesta = new byte[]{21,22,22};
        
        return respuesta;
    }
    
    public byte[] peticionVentaCash(){
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 2, 17));
        idOperacion = arrayToString(Arrays.copyOfRange(arreglo, 17, 27));
        itNumber = arrayToString(Arrays.copyOfRange(arreglo, 27, 32));
        itPrice = arrayToString(Arrays.copyOfRange(arreglo, 32, 37));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 37, 38));
        
        byte[] respuesta = new byte[]{21,25,25};
        
        return respuesta;
    }
    
    public byte[] recargaSaldo(){
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 2, 17));
        idUsuario = arrayToString(Arrays.copyOfRange(arreglo, 17, 27));
        revalue = arrayToString(Arrays.copyOfRange(arreglo,27,35));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 35, 36));
        
        byte[] respuesta = new byte[]{21,23,23};
        
        return respuesta;
    }
    
    public byte[] notificacionVentaTruncada(){
        cancelacion = arrayToString(Arrays.copyOfRange(arreglo,2,3));
        idMaquina = arrayToString(Arrays.copyOfRange(arreglo, 3, 18));
        idOperacion = arrayToString(Arrays.copyOfRange(arreglo, 18, 28));
        itNumber = arrayToString(Arrays.copyOfRange(arreglo, 28, 33));
        itPrice = arrayToString(Arrays.copyOfRange(arreglo, 33, 38));
        ixMensaje = arrayToString(Arrays.copyOfRange(arreglo, 38, 39));
        
        byte[] respuesta = new byte[]{21,24,24};
        
        return respuesta;
    }
    
    private String arrayToString(byte[] subArray){
        String respuesta = "";
        
        for (int i = 0; i < subArray.length; i ++ ){
            int valor = (int)subArray[i];
            int valorHex = Integer.parseInt(valor + "", 16);
            respuesta += (char)valorHex;
        }
        
        return respuesta;
    }
    
    private String formatNumberDecimal (float numero, int longitud){
        
        int numeroInt = Math.round(numero);
        
        int decimal = Math.round((numero * 10) % 10);
        
        String respuesta = String.format("%" +  (longitud - 1) + "d" + decimal,numeroInt).replace(" ", "0");
        
        System.out.println(decimal);
        System.out.println(respuesta);
        
        return respuesta;
    }
    
    private String formatModoVenta (int numero, int longitud){
        
        String respuesta = String.format("%" +  longitud + "d,", numero).replace(" ", "0");
        
        System.out.println(respuesta);
        
        return respuesta;
    }
    
    private ModeloJson buscarOperacion (String maq, String operacion){
        Gson gson = new Gson();
        ModeloJson respuesta = null;
        
        try {
            
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(Modelo.class.getResourceAsStream("jsonTest.json")));
            
            ModeloJson[] obj = gson.fromJson(br, ModeloJson[].class);
            
            System.out.println(Arrays.toString(obj));
            
            for (ModeloJson obj1 : obj) {
                if (obj1.operacion.equals(operacion)) {
                    return obj1;
                }
            }
            
        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }
        
        return respuesta;
    }
    
    private byte[] concatenarRespuesta(byte[] resp,String cadena){
        
        byte[] respAux = new byte[resp.length + cadena.length()];
        
        System.arraycopy(resp, 0, respAux, 0, resp.length);
        System.arraycopy(cadena.getBytes(), 0, respAux, resp.length, cadena.length());
        
        return respAux;
    }
    
    private byte[] concatenarRespuesta(byte[] resp,byte[] cadena){
        
        byte[] respAux = new byte[resp.length + cadena.length];
        
        System.arraycopy(resp, 0, respAux, 0, resp.length);
        System.arraycopy(cadena, 0, respAux, resp.length, cadena.length);
        
        return respAux;
    }
    
    private byte[] tobyteArray(byte[] resp, String cadena){
        byte[] respAux = new byte[resp.length + cadena.length()];
        
        System.arraycopy(resp, 0, respAux, 0, resp.length);
        System.arraycopy(cadena.getBytes(), 0, respAux, resp.length, cadena.length());
        
        return resp;
    }
}
