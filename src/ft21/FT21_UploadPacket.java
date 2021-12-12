package ft21;

public class FT21_UploadPacket extends FT21Packet {
	public final String filename;
	public final int optional_data_field;
	
	public FT21_UploadPacket(String filename, int optional_data_field) {
		super(PacketType.UPLOAD);
		super.putByte(1 * Integer.BYTES);
		super.putInt(optional_data_field);
		this.filename = filename;
		super.putString(filename);
		this.optional_data_field = optional_data_field;
	}
	
	public String toString() {
		return String.format("UPLOAD<%s, %d>", filename, optional_data_field);
	}
}
