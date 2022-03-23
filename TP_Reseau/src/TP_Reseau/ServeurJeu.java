package TP_Reseau;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
public class ServeurJeu  extends Thread{
	private boolean isActive=true;
    private int nombreClient=0; 
    private int nombreSecret;
    private String gagnant;
    private boolean fin;
	public static void main(String[] args) {
		new ServeurJeu().start();
	}
	@Override 
    public void run() {
    	try {
    		ServerSocket ss=new ServerSocket(1234);
    		nombreSecret=new Random().nextInt(1000);
    		System.out.println("Le serveur a choisi son secret : "+nombreSecret);

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
        pw.println("Devinez nombre secret...?");
 		 while(true) {
 			 String req=br.readLine();
 			 int nombre=Integer.parseInt(req);
 			 System.out.println("Client "+IP+ "tentative avec le nombre: " +nombre);

 			 if(fin == false) {
 				if(nombre> nombreSecret) {
 					pw.println("Votre nombre est superieure au nombre secret");
 				
 			 }else if(nombre < nombreSecret) {
					pw.println("Votre nombre est inferieure au nombre secret");
				}else {
					pw.println("BRAVO,Vous avez gagné");
					gagnant=IP;
					System.out.println("Bravo aus gagnant,IP client : "+gagnant);
					fin=true;
				}
 		 }else {
 			pw.println("Jeu terminé,le gagnant est "+gagnant);
 		 }}
 		
 	 }catch (IOException e) {
 	    	e.printStackTrace();

 	 }
}}}
