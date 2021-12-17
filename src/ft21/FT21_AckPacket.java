package ft21;

public class FT21_AckPacket extends FT21Packet {
	public final int cSeqN;
	public final boolean outsideWindow;
	public final int optional_dataSEQ;
	public final int optional_dataTime;
	
	FT21_AckPacket(byte[] bytes) {
		super( bytes );
		int seqN = super.getInt();
		this.cSeqN = Math.abs( seqN );
		this.outsideWindow = seqN < 0;
		this.optional_dataSEQ = super.getInt();
		this.optional_dataTime = super.getInt();

		// decode optional fields here...
	}

	public String toString() {
		return String.format("ACK<%d %d %d>", cSeqN, optional_dataSEQ, optional_dataTime);
	}
	
}