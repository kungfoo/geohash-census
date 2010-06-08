package ch.mollusca.census;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashBoundingBoxQuery;
import ch.hsr.geohash.queries.GeoHashCircleQuery;
import ch.hsr.geohash.queries.GeoHashQuery;
import ch.hsr.geohash.util.VincentyGeodesy;
import ch.mollusca.util.Lists;

/**
 * this class will use the census places2k.txt file from here:
 * <a>http://www.census.gov/tiger/tms/gazetteer/places2k.txt</a><br>
 * place it in the folder: ${project_root}/input/
 */
public class CensusPlacesTest {
	private static List<Place> places;

	@BeforeClass
	public static void setUpOnce() throws IOException {
		places = Places.load(new FileInputStream(new File("input/places2k.txt")));
		System.out.println(places.size() + " places in file!");
	}

	@Test
	public void testLoadingSmallFile() throws IOException {
		assertEquals(10, Places.load(new FileInputStream(new File("input/places_test.txt"))).size());
	}

	@Test
	public void check1kmSearch() {
		WGS84Point center = new WGS84Point(31.566367, -86.251300);
		GeoHashQuery query = new GeoHashCircleQuery(center, 30000);

		BoundingBox searchBbox = query.getSearchHashes().get(0).getBoundingBox();
		System.out.println(VincentyGeodesy.distanceInMeters(searchBbox.getLowerRight(), searchBbox.getUpperLeft()));
		assertTrue("search should contain the initial point", query.contains(GeoHash.withBitPrecision(center
				.getLatitude(), center.getLongitude(), 64)));
		checkSearchResults(query);
	}

	@Test
	public void checkQueryWithPostgis() {
		/*
		 * according to postgis, the following 8 place are within this searches
		 * hashes expanded bounding box: BOX(-85.4296875 31.2890625,-85.078125
		 * 31.9921875)
		 */
		WGS84Point center = new WGS84Point(31.566367, -85.251300);
		GeoHashQuery query = new GeoHashCircleQuery(center, 30000);
		List<Place> filtered = filter(places, query);

		List<String> placesFromPostgis = Arrays.asList("Abbeville", "Columbia", "Eufaula", "Haleburg", "Headland",
				"Kinsey", "Newville", "Georgetown");
		checkContainsPlacesNamed(filtered, placesFromPostgis);
	}

	private void checkContainsPlacesNamed(List<Place> filtered, List<String> placesFromPostgis) {
		for (Place place : filtered) {
			assertTrue(place.getName() + " should be in the filtered list.", placesFromPostgis
					.contains(place.getName()));
		}
	}

	@Test
	public void checkBoundingBoxQueryAroundRacine() {
		BoundingBox boundingBox = new BoundingBox(42.336, 42.606, -82.981, -82.789);
		GeoHashQuery query = new GeoHashBoundingBoxQuery(boundingBox);
		List<Place> filtered = filter(places, query);
		/*
		 * this is the number of place that actually ARE within that bounding
		 * box. checked via postgis.
		 */
		assertTrue(filtered.size() >= 13);
		/*
		 * again, this number has been determined with postgis and the expanded
		 * bounding box.
		 */
		int numberFromPostgis = 63;
		assertEquals(numberFromPostgis, filtered.size());
		List<String> names = Arrays.asList("Grosse Pointe Park", "Grosse Pointe", "Grosse Pointe Farms",
				"Grosse Pointe Shores", "Grosse Pointe Woods", "Harper Woods", "Eastpointe", "St. Clair Shores",
				"Roseville", "Fraser", "Clinton", "Mount Clemens", "Harrison");

		for (final String name : names) {
			assertTrue("should contain a place named " + name, Lists.contains(filtered, new Lists.Predicate<Place>() {
				public boolean matches(Place place) {
					return place.getName().equals(name);
				}
			}));
		}
	}

	private void checkSearchResults(GeoHashQuery query) {
		System.out.println(query.getWktBox());
		List<Place> filtered = filter(places, query);
		System.out.println(filtered.size() + " places that are within " + query);
	}

	private List<Place> filter(List<Place> places, GeoHashQuery search) {
		List<Place> result = new ArrayList<Place>();
		for (Place place : places) {
			if (search.contains(place.getGeoHash())) {
				result.add(place);
			}
		}
		return result;
	}

	@Test
	public void testSingleLine() {
		String line = "AL0100124Abbeville city                                                       2987     1353      40301945        120383   15.560669    0.046480 31.566367 -85.251300";
		Place place = Places.createFromCensusLine(line);
		assertEquals("Abbeville", place.getName());
	}

	@Test
	public void testMoreComplicatedLine() {
		String line = "AL0104684Bayou La Batre city                                                  2313      845      10438238        377629    4.030226    0.145803 30.403253 -88.248117";
		Place place = Places.createFromCensusLine(line);
		assertEquals("Bayou La Batre", place.getName());
		assertEquals(2313, place.getPopulation());
		assertEquals(-88.248117, place.getLocation().getLongitude(), 1e-20);
		assertEquals(30.403253, place.getLocation().getLatitude(), 1e-20);
	}
}
