package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardtcDisplayPNGCharacteristics {
	private long width;
	private long height;
	private byte bitDepth;
	private byte colorType;
	private byte compression;
	private byte filter;
	private byte interlace;
	private StandardrgbPalletteEntry[] plte;
	public long getWidth() {
		return width;
	}
	public void setWidth(long width) {
		this.width = width;
	}
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	public byte getBitDepth() {
		return bitDepth;
	}
	public void setBitDepth(byte bitDepth) {
		this.bitDepth = bitDepth;
	}
	public byte getColorType() {
		return colorType;
	}
	public void setColorType(byte colorType) {
		this.colorType = colorType;
	}
	public byte getCompression() {
		return compression;
	}
	public void setCompression(byte compression) {
		this.compression = compression;
	}
	public byte getFilter() {
		return filter;
	}
	public void setFilter(byte filter) {
		this.filter = filter;
	}
	public byte getInterlace() {
		return interlace;
	}
	public void setInterlace(byte interlace) {
		this.interlace = interlace;
	}
	public StandardrgbPalletteEntry[] getPlte() {
		return plte;
	}
	public void setPlte(StandardrgbPalletteEntry[] plte) {
		this.plte = plte;
	}
	
	
}
