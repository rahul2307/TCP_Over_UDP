import java.net.*;

public class PacketHandler extends Thread{
	private DatagramPacket packet;
	public PacketHandler(DatagramPacket packet){
		this.packet = packet;
	}
	
	@Override
	public void run(){
		
		String data = new String(packet.getData());
		System.out.println("Packet Data: "+data);
	}
}