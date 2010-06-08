package ch.mollusca.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ch.mollusca.census.Place;
import ch.mollusca.census.Places;

public class PlacesWithPopulation {
	private static List<Place> places;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		places = Places.load(new FileInputStream(new File("input/places2k.txt")));
		for (Place p : places) {
			if(p.getHousingUnits() > 0){
				System.out.println(p.getName() + ":" + p.getPopulation()/p.getHousingUnits());
			}
		}
	}
}
