package ft21;

import java.nio.ByteBuffer;

public class FT21_UploadPacket extends FT21Packet {
	public final String filename;
	public final int optional_dataSEQ;
	public final int optional_dataTime;
	
	public FT21_UploadPacket(String filename, int optional_dataSEQ, int optional_dataTime) {
		super(PacketType.UPLOAD);
		super.putByte(2 * Integer.BYTES);
		super.putInt(optional_dataSEQ);
		super.putInt(optional_dataTime);
		super.putString(filename);
		this.filename = filename;
		this.optional_dataSEQ = optional_dataSEQ;
		this.optional_dataTime = optional_dataTime;

	}
	
	public String toString() {
		return String.format("UPLOAD<%s, %d, %d>", filename, optional_dataSEQ, optional_dataTime);
	}
}
