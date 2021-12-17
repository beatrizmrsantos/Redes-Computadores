package ft21;

public class FT21_FinPacket extends FT21Packet {
	public final int seqN;
	public final int optional_dataSEQ;
	public final int optional_dataTime;
	
	public FT21_FinPacket(int seqN, int optional_dataSEQ, int optional_dataTime ) {
		super(PacketType.FIN);
		super.putInt( seqN );
		super.putByte(2 * Integer.BYTES);
		super.putInt(optional_dataSEQ);
		super.putInt(optional_dataTime);
		this.seqN = seqN;
		this.optional_dataSEQ = optional_dataSEQ;
		this.optional_dataTime = optional_dataTime;
	}
	
	
	public String toString() {
		return String.format("FIN<%d %d, %d>", seqN,optional_dataSEQ, optional_dataTime);
	}
}