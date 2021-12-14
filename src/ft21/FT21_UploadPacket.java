package ft21;

import java.nio.ByteBuffer;

public class FT21_UploadPacket extends FT21Packet {
	public final String filename;
	public final int optional_data;
	
	public FT21_UploadPacket(String filename, int optional_data) {
		super(PacketType.UPLOAD);
		super.putByte(1 * Integer.BYTES);
		super.putInt(optional_data);
		super.putString(filename);
		this.filename = filename;
		this.optional_data = optional_data;

	}
	
	public String toString() {
		return String.format("UPLOAD<%s, %d>", filename, optional_data);
	}
}
