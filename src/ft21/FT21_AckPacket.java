package ft21;

public class FT21_AckPacket extends FT21Packet {
	public final int cSeqN;
	public final boolean outsideWindow;
	public final int optional_data ;
	
	FT21_AckPacket(byte[] bytes) {
		super( bytes );
		int seqN = super.getInt();
		this.cSeqN =  seqN ;
		this.outsideWindow = seqN < 0;
		this.optional_data = super.getInt();

		// decode optional fields here...
	}

	public String toString() {
		return String.format("ACK<%d %d>", cSeqN, optional_data);
	}
	
}