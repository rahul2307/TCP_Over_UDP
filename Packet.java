public class Packet {
	private boolean syncFlag;
	private boolean ackFlag;
	private boolean finFlag;
	private int syncNum;
	private int ackNum;
	private int windowSize;
	private String data;
	
	
	public Packet(){
		this.syncFlag = false;
		this.ackFlag = false;
		this.finFlag = false;
		
		this.syncNum = 0;
		this.ackNum = 0;
		this.windowSize = 0;
		
		this.data = null;
		
	}
	public Packet(boolean syncFlag, 
			boolean ackFlag, 
			boolean finFlag, 
			int synNum, 
			int ackNum, 
			int windowSize, 
			String data){
		
		this.syncFlag = syncFlag;
		this.ackFlag = ackFlag;
		this.finFlag = finFlag;
		
		this.syncNum = synNum;
		this.ackNum = ackNum;
		this.windowSize = windowSize;
		
		this.data = data;
	}
	public boolean isSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(boolean sYN_FLAG) {
		syncFlag = sYN_FLAG;
	}

	public boolean isAckFlag() {
		return ackFlag;
	}

	public void setAckFlag(boolean aCK_FLAG) {
		ackFlag = aCK_FLAG;
	}

	public boolean isFinFlag() {
		return finFlag;
	}

	public void setFinFlag(boolean fIN_FLAG) {
		finFlag = fIN_FLAG;
	}

	public int getSyncNum() {
		return syncNum;
	}

	public void setSyncNum(int sYN_NUM) {
		syncNum = sYN_NUM;
	}

	public int getAckNum() {
		return ackNum;
	}

	public void setAckNum(int aCK_NUM) {
		ackNum = aCK_NUM;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int wINDOW_SIZE) {
		windowSize = wINDOW_SIZE;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("SYNF=").append(syncFlag?1:0).append(";").
		append("SYNN=").append(syncNum).append(";").
		append("ACKF=").append(ackFlag?1:0).append(";").
		append("ACKN=").append(ackNum).append(";").
		append("FINF=").append(finFlag?1:0).append(";").
		append("WINDOW=").append(windowSize).append(";"). 
		append("DATA=").append(data);
		
		return builder.toString();
	}
	public static Packet valueOf(String packetData){
		boolean syncFlag, ackFlag, finFlag;
		int synNum, ackNum, winSize;
		
		syncFlag=ackFlag=finFlag=false;
		synNum=ackNum=winSize=0;
		String data="";
		
		String[] attribs = packetData.split(";");
		Packet packet = new Packet();
		for(String token: attribs){
			String[] pair = token.split("=");
			switch(pair[0]){
				case "SYNF":
					packet.setSyncFlag(pair[1].equals("1"));
					break;
				case "ACKF":
					packet.setAckFlag(pair[1].equals("1"));
					break;
				case "FINF":
					packet.setFinFlag(pair[1].equals("1"));
					break;
				case "SYNN":
					packet.setSyncNum(Integer.parseInt(pair[1]));
					break;
				case "ACKN":
					packet.setAckNum(Integer.parseInt(pair[1]));
					break;
				case "WINDOW":
					packet.setWindowSize(Integer.parseInt(pair[1]));
					break;
				case "DATA":
					packet.setData(pair[1]);
					break;
			}
		}
		return packet;
	}
	
}

