package ch.mollusca.census;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;

public class GeoHashFactory {
	public static final GeoHash getFullPrecisionHash(WGS84Point location) {
		return GeoHash.withBitPrecision(location.getLatitude(), location.getLongitude(), 64);
	}
}
