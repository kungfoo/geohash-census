package ch.mollusca.census;

import ch.hsr.geohash.GeoHash;

/**
 * conceptually, anything that has an (arbitrarily small) bounding can have a
 * geohash calculated.
 */
public interface GeoHashable {
	/**
	 * @return the {@link GeoHash} that represents this object.
	 */
	public GeoHash getGeoHash();
}
