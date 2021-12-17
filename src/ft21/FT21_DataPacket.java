package ft21;

public class FT21_DataPacket extends FT21Packet {
	public final int seqN;
	public final byte[] data;
	public final int optional_dataSEQ;
	public final int optional_dataTime;
	
	public FT21_DataPacket(int seqN, byte[] data , int optional_dataSEQ,int optional_dataTime) {
		this(seqN, data, data.length, optional_dataSEQ,optional_dataTime );
	}

	public FT21_DataPacket(int seqN, byte[] data, int datalen,  int optional_dataSEQ,int optional_dataTime) {
		super(PacketType.DATA);
		super.putInt(seqN);
		super.putByte(2 * Integer.BYTES);
		super.putInt(optional_dataSEQ);
		super.putInt(optional_dataTime);
		super.putBytes(data, datalen);
		this.seqN = seqN;
		this.data = data;
		this.optional_dataSEQ = optional_dataSEQ;
		this.optional_dataTime = optional_dataTime;
	}

	
	public String toString() {
		return String.format("DATA<%d, len: %d , %d, %d>", seqN, data.length,optional_dataSEQ , optional_dataTime);
	}

}