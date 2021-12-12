package ft21;

public class FT21_FinPacket extends FT21Packet {
	public final int seqN;
	public final int optional_data_field;
	
	public FT21_FinPacket(int seqN, int optional_data_field) {
		super(PacketType.FIN);
		super.putInt( seqN );
		super.putByte(1 * Integer.BYTES);
		super.putInt(optional_data_field);
		this.seqN = seqN;
		this.optional_data_field = optional_data_field;
	}
	
	
	public String toString() {
		return String.format("FIN<%d %d>", seqN,optional_data_field );
	}
}