package ft21;

public class FT21_DataPacket extends FT21Packet {
	public final int seqN;
	public final byte[] data;
	public final int optional_data_field;
	
	public FT21_DataPacket(int seqN, byte[] data) {
		this(seqN, data, data.length);
	}

	public FT21_DataPacket(int seqN, byte[] data, int datalen,  int optional_data_field) {
		super(PacketType.DATA);
		super.putInt(seqN);
		super.putByte(1 * Integer.BYTES);
		super.putInt(optional_data_field);
		super.putBytes(data, datalen);
		this.seqN = seqN;
		this.data = data;
		this.optional_data_field = optional_data_field;
	}

	
	public String toString() {
		return String.format("DATA<%d, len: %d , %d>", seqN, data.length,optional_data_field);
	}

}