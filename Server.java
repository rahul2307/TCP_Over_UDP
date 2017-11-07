import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Server {
	private static final int MAXIMUM_BUFFER_SIZE = 512;
	private static final String INPUT_DATA_FILE_NAME = "data.in";
	private static DatagramSocket serverSocket;
	private static State state = State.NONE;
	private static int ACK_NUM = 0;
	private static int SYNC_NUM = 0;
	private static int WINDOW_SIZE = 0;
	private static String INPUT_DATA = "";
	private static int INITIAL_SEGMENT = 0;
	public static void main(String[] args) {
		if(args.length < 1){
			System.out.println("Run as java Server <server portno>");
			return;
		}
		
		int port = 0;
		try{
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException nfe){
			port = 8080;
		}
		try {
			System.out.println("Binding UDP server to port "+port+"...");
			serverSocket = new DatagramSocket(port);
			System.out.println("Bind successful!");
		} catch (SocketException e) {
			System.out.println("Cannot bind socket to port "+port);
			return;
		}
		
		System.out.println("UDP listener for this server started!");
		listener.start();
	}
	private static Thread listener = new Thread(new Runnable(){
		@Override
		public void run() {
			while(true){
				byte[] buff = new byte[MAXIMUM_BUFFER_SIZE];
				DatagramPacket packet = new DatagramPacket(buff,buff.length);
				
				
				try {
					serverSocket.receive(packet);
					Packet p = Packet.valueOf(new String(buff));
					InetAddress clientAddress = packet.getAddress();
					int clientPort = packet.getPort();

					
					Thread t = timerThread(1);
					t.start();
					try{t.join();}catch(InterruptedException ie){}
					System.out.println("Data Recieved "+p.toString());

					if(state == State.NONE){
						
						if(p.isSyncFlag()){ 
							System.out.println("Threeway handshake 1/3.");
							

							//send ACK+SYN packet
							Packet ackSyncPacket = new Packet();
							ackSyncPacket.setAckFlag(true);
							ACK_NUM = p.getSyncNum()+1;
							ackSyncPacket.setAckNum(ACK_NUM);

							SYNC_NUM = ThreadLocalRandom.current().nextInt(1, 5000);
							ackSyncPacket.setSyncFlag(true);
							ackSyncPacket.setSyncNum(SYNC_NUM);

							send(clientAddress, clientPort, ackSyncPacket.toString());
							state = State.SYN_RECV;
							System.out.println("Threeway handshake 2/3.");
						}
					}else if(state == State.SYN_RECV){
						if(p.isAckFlag() && p.getAckNum() == (++SYNC_NUM)){
							state = State.ESTABLISHED;
							System.out.println("Threeway handshake 3/3.");

							
							Scanner in = new Scanner(new File(INPUT_DATA_FILE_NAME));
							while(in.hasNextLine()){
								INPUT_DATA += in.nextLine();
							}
							in.close();

							INITIAL_SEGMENT = SYNC_NUM;
							//send first data
							Packet datum = new Packet();
							datum.setSyncNum(SYNC_NUM);
							int index = datum.getSyncNum()-INITIAL_SEGMENT;
							datum.setData(""+INPUT_DATA.charAt(index));
							send(clientAddress,clientPort, datum.toString());


						}
					}else if(state == State.ESTABLISHED){
						
						WINDOW_SIZE = p.getWindowSize();
						if(p.isAckFlag()){
							
							if(WINDOW_SIZE == 0){
								
								continue;
							}

							SYNC_NUM = p.getAckNum();
							
							t = timerThread(4);
							t.start();
							try{t.join();}catch(InterruptedException ie){}
							for(int i=0; i<WINDOW_SIZE; i++){
								Packet datum = new Packet();
								datum.setSyncNum(SYNC_NUM+i);
								int index = datum.getSyncNum()-INITIAL_SEGMENT;
								if(index == INPUT_DATA.length()){
									
									System.out.println("Data sent!");
									datum = new Packet();
									datum.setFinFlag(true);
									state = State.FIN_SEND;
									System.out.println("Fourway handshake 1/4");
									
								}else{
									try{datum.setData(""+INPUT_DATA.charAt(index));}catch(StringIndexOutOfBoundsException sioobe){}
								}
								send(clientAddress,clientPort, datum.toString());

							}
						}
						System.out.println("Data Recieved "+p.toString());
					}else if(state == State.FIN_SEND){
						
						
						if(p.isFinFlag() && p.getAckNum() == 0){ 
							System.out.println("Fourway handshake 2/4");
							System.out.println("Fourway handshake 3/4");
							state = State.FIN_ACKD;

							
							Packet finAck = new Packet();
							finAck.setAckFlag(true);
							send(clientAddress,clientPort,finAck.toString());
							System.out.println("Fourway handshake 4/4");
							break;
						}
					}

					

				} catch(FileNotFoundException fnfe){
					System.out.println("File "+INPUT_DATA_FILE_NAME+" not found! Needed for the program!");
				} catch (IOException e) {
					System.out.println("Failed to receive a packet... "+e.getMessage());
				}

				if(state == State.FIN_ACKD){
					Thread t = timerThread(5);
					t.start();
					try{t.join();}catch(InterruptedException ie){}
					//serverSocket.close();
				}
			}
		}
	});
	public static void send(InetAddress address, int port, String message){
		byte[] buff = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buff, buff.length,address,port);
		try {
			serverSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet: "+message);
		}
	}

	private static Thread timerThread(final int seconds){
		return new Thread(new Runnable(){
			@Override
			public void run(){
				try{

					Thread.sleep(seconds*1000);
				}catch(InterruptedException ie){

				}
			}
		});
	}
}
