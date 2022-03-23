package TP_Reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import TP_Reseau.ServeurChat.Conversation;


public class ServeurChat  extends Thread{
	private boolean isActive=true;
    private int nombreClient=0; 
    private List<Conversation> clients=new ArrayList<Conversation>();

	public static void main(String[] args) {
		new ServeurChat().start();
		

	}
	@Override
    public void run() {
    	try {
    		ServerSocket ss=new ServerSocket(1234);
             while(isActive) {
    	Socket socket=ss.accept();
    	++nombreClient;
    	Conversation conversation=new Conversation(socket,nombreClient);
    	clients.add(conversation);
    	conversation.start();
		System.out.println("Suite de l'application...");}
    }catch(IOException e) {
    	e.printStackTrace();
    }
}
	class Conversation extends Thread{
    	protected Socket socketClient;
    	protected int numero;
    	
    	public Conversation(Socket s,int num) {
    		this.socketClient=s;
    		this.numero=num;
    	}
    	
    	public void braodcastMessage(String message,Socket socket,int nombreClient) {
    		try {
    		for (Conversation client:clients) {
    			if(client.socketClient != socket) {
    				if(client.numero==nombreClient || nombreClient==-1) {
    					PrintWriter printWriter=new PrintWriter(client.socketClient.getOutputStream(),true);
            			printWriter.println(message);		
    				}
    			
    			}
    			
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    		}
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
 			 if(req.contains("TO")) {
 	 			 String[] requestParams=req.split("TO");
 			 
 			 if(requestParams.length==2);
 			 String message=requestParams[1];
 			 int nombreClient=Integer.parseInt(requestParams[0]);
  			braodcastMessage(message,socketClient,nombreClient);
	 
 			 }else {
 				braodcastMessage(req,socketClient,-1);
 			 }
 			 
 			
 		 }

 	 }catch (IOException e) {
 	    	e.printStackTrace();

 	 }
}}}
