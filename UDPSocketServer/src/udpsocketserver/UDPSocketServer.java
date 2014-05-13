package udpsocketserver;

/**
 *
 * @author luishoracio
 */


import com.google.gson.*;
import java.util.Arrays;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPSocketServer {
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        
        DatagramSocket aSocket = null;
        
        try {
            aSocket = new DatagramSocket(4581);
            
            byte [] buffer = new byte[1000];
            
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            
            while (true){
                
                aSocket.receive(request);
                
                Modelo model = new Modelo(buffer);
                
                byte [] a = new byte [] {10,10,10};
        
                switch(buffer[0]){
                    case 20:
                        switch (buffer[1]){
                            case 21:
                                a = model.consultaSaldoCredencial();
                                
                                System.out.println(model.idMaquina);
                                System.out.println(model.idUsuario);
                                System.out.println(model.ixMensaje);
                                
                                break;
                            case 22: 
                                a = model.peticionVentaCashless();
                                
                                System.out.println(model.idMaquina);
                                System.out.println(model.idOperacion);
                                System.out.println(model.itNumber);
                                System.out.println(model.itPrice);
                                System.out.println(model.ixMensaje);
                                
                                break;
                            case 23: 
                                a = model.recargaSaldo();
                                
                                System.out.println(model.idMaquina);
                                System.out.println(model.idUsuario);
                                System.out.println(model.revalue);
                                System.out.println(model.ixMensaje);
                                
                                break;
                            case 24: 
                                a = model.notificacionVentaTruncada();
                                
                                System.out.println(model.cancelacion);
                                System.out.println(model.idMaquina);
                                System.out.println(model.idOperacion);
                                System.out.println(model.itNumber);
                                System.out.println(model.itPrice);
                                System.out.println(model.ixMensaje);
                                
                                break;
                            case 25: 
                                a = model.peticionVentaCash();
                                
                                System.out.println(model.idMaquina);
                                System.out.println(model.idOperacion);
                                System.out.println(model.itNumber);
                                System.out.println(model.itPrice);
                                System.out.println(model.ixMensaje);
                                
                                break;
                                
                            case 31:
                                a = model.consultaSaldoTransfer();
                                
                                System.out.println(model.idMaquina);
                                System.out.println(model.idUsuario);
                                System.out.println(model.ixMensaje);
                                
                                break;
                            case 41:
                                a = model.consultaSaldoAPN();
                                
                                System.out.println(model.idMaquina);
                                System.out.println(model.ixMensaje);
                                
                                break;
                        }
                        
                        break;
                    case 30:
                        a = new byte[]{31,31,30};
                        break;
                        
                    
                }
                
                DatagramPacket reply = new DatagramPacket(a, a.length, 
                    request.getAddress(), request.getPort());
                aSocket.send(reply);
                
            }
            
        } catch (SocketException ex) {
            Logger.getLogger(UDPSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } //Excepciones
    }
    
}
