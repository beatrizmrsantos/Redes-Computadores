package ft21;

public class FT21_AckPacket extends FT21Packet {
	public final int cSeqN;
	public final boolean outsideWindow;
	public final int optional_data_field;
	
	FT21_AckPacket(byte[] bytes, int optional_data_field) {
		super( bytes );
		int seqN = super.getInt();
		this.cSeqN = Math.abs( seqN );
		this.outsideWindow = seqN < 0;
		super.putByte(1 * Integer.BYTES);
		this.optional_data_field = optional_data_field;
		
		// decode optional fields here...
	}

	public String toString() {
		return String.format("ACK<%d %d>", cSeqN, optional_data_field);
	}
	
}