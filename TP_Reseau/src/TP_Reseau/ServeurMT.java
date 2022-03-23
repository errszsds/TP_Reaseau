package TP_Reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServeurMT  extends Thread{
	private boolean isActive=true;
    private int nombreClient=0; 
	public static void main(String[] args) {
		new ServeurMT().start();
		

	}
	@Override
    public void run() {
    	try {
    		ServerSocket ss=new ServerSocket(1234);
             while(isActive) {
    	Socket socket=ss.accept();
    	++nombreClient;
    	new Conversation(socket,nombreClient).start();
		System.out.println("Suite de l'application...");}
    }catch(IOException e) {
    	e.printStackTrace();
    }
}
	class Conversation extends Thread{
    	private Socket socketClient;
    	private int numero;
    	
    	public Conversation(Socket s,int num) {
    		this.socketClient=s;
    		this.numero=num;
    	}
    	 @Override
 	    public void run() {
 		 try {
 		 InputStream is=socketClient.getInputStream();
 		 InputStreamReader isr =new InputStreamReader(is);
 		 BufferedReader br=new BufferedReader(isr);
 		 
 		 OutputStream os=socketClient.getOutputStream();
 		 PrintWriter pw=new PrintWriter(os,true);
 		 String IP=socketClient.getRemoteSocketAddress().toString();
         pw.println("Bienvenue vous etes le client numero "+numero);
  		System.out.println("Connexion du client numéro "+numero+",IP= "+IP);

 		 while(true) {
 			 String req=br.readLine();
 			 String reponse="length= "+req.length();
 			 pw.println(reponse);
 		 }

 	 }catch (IOException e) {
 	    	e.printStackTrace();

 	 }
}}}
