package ch.mollusca.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JoinerTest {
	private Integer[] integers= new Integer[] { 1, 2, 3, 4, 5 };
	private List<String> strings = Arrays.asList("hello", "wicked", "joiner");

	@Test
	public void testCommaJoiner() {
		Joiner joiner = Joiner.on(", ");
		assertEquals("1, 2, 3, 4, 5", joiner.join(integers));
		assertEquals("", joiner.join(new Integer[] {}));
		assertEquals("1", joiner.join(new Integer[] { 1 }));
		assertEquals("hello, wicked, joiner", joiner.join(strings));
	}
	
	@Test
	public void testSpaceJoiner() {
		Joiner joiner = Joiner.on(" ");
		assertEquals("1 2 3 4 5", joiner.join(integers));
		assertEquals("hello wicked joiner", joiner.join(strings));
	}
}
