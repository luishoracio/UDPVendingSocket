/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package udpclient;

/**
 *
 * @author luishoracio
 */
import java.io.*;
import java.net.*;

public class UDPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        BufferedReader inFromUser =          
                new BufferedReader(new InputStreamReader(System.in));       
        DatagramSocket clientSocket = new DatagramSocket();       
        InetAddress IPAddress = InetAddress.getByName("localhost");       
        byte[] sendData = new byte[1024];       
        byte[] receiveData = new byte[1024];
        
        byte [] a = new byte [] {20, 21, 30, 31, 32, 32, 30, 37, 30, 30, 39, 37, 36, 31, 39, 38, 39, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 32};
        //byte [] a = new byte [] {20, 21, 30, 31, 32, 32, 30, 37, 30, 30, 39, 37, 36, 31, 39, 38, 39, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 32};
        
        sendData = a; //sentence.getBytes();       
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4580);       
        clientSocket.send(sendPacket);       
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);       
        clientSocket.receive(receivePacket);       
        
        String modifiedSentence = new String(receivePacket.getData());     
        
        System.out.println(arrayToString(receivePacket.getData()));
        
        System.out.println("FROM SERVER:" + modifiedSentence);       
        clientSocket.close(); 
        //- See more at: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.2vjcL1C1.dpuf
    }
    
    private static String arrayToString(byte[] subArray){
        String respuesta = "";
        
        for (int i = 0; i < subArray.length; i ++ ){
            int valor = (int)subArray[i];
            int valorHex = Integer.parseInt(valor + "", 16);
            respuesta += (char)valorHex;
        }
        
        return respuesta;
    }
    
}
