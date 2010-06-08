package ch.mollusca.census;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hsr.geohash.WGS84Point;
import ch.mollusca.util.Joiner;

public class Places {
	public static List<Place> load(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		List<Place> result = new ArrayList<Place>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			result.add(createFromCensusLine(line));
		}
		return result;
	}

	public static Place createFromCensusLine(String line) {
		String name = getTrimmedContents(line, 10, 73);
		name = dropLastWord(name);
		long population = readLong(line, 74, 82);
		long housingUnits = readLong(line, 83, 91);
		long landArea = readLong(line, 92, 105);
		long waterArea = readLong(line, 106, 119);
		double latitude = Double.parseDouble(getTrimmedContents(line, 144, 153));
		double longitude = Double.parseDouble(getTrimmedContents(line, 154, 164));

		return new Place(name, population, housingUnits, landArea, waterArea, new WGS84Point(latitude, longitude));
	}

	private static String dropLastWord(String name) {
		String[] words = name.split("\\s+");
		String[] lastWordDropped = new String[words.length - 1];
		System.arraycopy(words, 0, lastWordDropped, 0, words.length - 1);
		return Joiner.on(" ").join(lastWordDropped);
	}

	private static long readLong(String line, int start, int end) {
		return Long.parseLong(getTrimmedContents(line, start, end));
	}

	private static String getTrimmedContents(String line, int start, int end) {
		return line.substring(start - 1, end).trim();
	}
}
