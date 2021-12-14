package ft21;

public class FT21_FinPacket extends FT21Packet {
	public final int seqN;
	public final int optional_data;
	
	public FT21_FinPacket(int seqN, int optional_data) {
		super(PacketType.FIN);
		super.putInt( seqN );
		super.putByte(1 * Integer.BYTES);
		super.putInt(optional_data);
		this.seqN = seqN;
		this.optional_data = optional_data;
	}
	
	
	public String toString() {
		return String.format("FIN<%d %d>", seqN,optional_data);
	}
}