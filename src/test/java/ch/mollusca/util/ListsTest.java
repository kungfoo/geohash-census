package ch.mollusca.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.mollusca.util.Lists.Mapper;
import ch.mollusca.util.Lists.Predicate;

public class ListsTest {

	private List<Integer> integers;
	private Predicate<Integer> equalToTen;
	private Predicate<Integer> smallerThanTen;
	private Predicate<Integer> largerThanTen;

	@Before
	public void setUp() {
		integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		equalToTen = new Predicate<Integer>() {
			public boolean matches(Integer i) {
				return i == 10;
			};
		};

		smallerThanTen = new Predicate<Integer>() {
			public boolean matches(Integer i) {
				return i < 10;
			}
		};

		largerThanTen = new Predicate<Integer>() {
			public boolean matches(Integer i) {
				return i > 10;
			}
		};

	}

	@Test
	public void testContains() {
		assertTrue(Lists.contains(integers, equalToTen));
		assertTrue(Lists.contains(integers, smallerThanTen));
		assertTrue(!Lists.contains(integers, largerThanTen));
	}

	@Test
	public void testFind() {
		assertEquals(new Integer(10), Lists.find(integers, equalToTen));
		assertEquals(null, Lists.find(integers, largerThanTen));
	}

	@Test
	public void testFindAll() {
		assertEquals(Arrays.asList(2, 4, 6, 8, 10), Lists.findAll(integers, new Predicate<Integer>() {
			public boolean matches(Integer o) {
				return o % 2 == 0;
			}
		}));

		List<String> strings = new LinkedList<String>();
		strings.add("hello");
		strings.add("list");
		List<String> startingWithL = Lists.findAll(strings, new Predicate<String>() {
			public boolean matches(String o) {
				return o.startsWith("l");
			}
		});
		assertEquals(Arrays.asList("list"), startingWithL);
		/* also check the new actual type of the list */
		assertEquals(LinkedList.class, startingWithL.getClass());
	}

	@Test
	public void testCollect() {
		List<String> strings = Lists.collect(integers, new Mapper<Integer, String>() {
			public String map(Integer o) {
				return o.toString() + ('a');
			}
		});
		assertEquals(Arrays.asList("1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a"), strings);
	}

}
