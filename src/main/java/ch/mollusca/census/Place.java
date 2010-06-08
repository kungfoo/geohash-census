package ch.mollusca.census;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;

public class Place implements GeoHashable {
	private String name;
	private long population;
	private long housingUnits;
	private long landArea;
	private long waterArea;

	private WGS84Point location;
	private GeoHash geohash;

	public Place(String name, long population, long housingUnits, long landArea, long waterArea, WGS84Point location) {
		this.name = name;
		this.population = population;
		this.housingUnits = housingUnits;
		this.landArea = landArea;
		this.waterArea = waterArea;
		this.location = location;
		geohash = GeoHashFactory.getFullPrecisionHash(location);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (housingUnits ^ (housingUnits >>> 32));
		result = prime * result + (int) (landArea ^ (landArea >>> 32));
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (population ^ (population >>> 32));
		result = prime * result + (int) (waterArea ^ (waterArea >>> 32));
		return result;
	}

	public String getName() {
		return name;
	}

	public long getPopulation() {
		return population;
	}

	public long getHousingUnits() {
		return housingUnits;
	}

	public long getLandArea() {
		return landArea;
	}

	public long getWaterArea() {
		return waterArea;
	}

	public WGS84Point getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Place)) {
			return false;
		}
		Place other = (Place) obj;
		if (housingUnits != other.housingUnits) {
			return false;
		}
		if (landArea != other.landArea) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (population != other.population) {
			return false;
		}
		if (waterArea != other.waterArea) {
			return false;
		}
		return true;
	}

	public void setGeohash(GeoHash geohash) {
		if (!geohash.contains(location)) {
			throw new RuntimeException("Illegal Geohash. Does not contain the location");
		}
		this.geohash = geohash;
	}

	public GeoHash getGeoHash() {
		return geohash;
	}
}
