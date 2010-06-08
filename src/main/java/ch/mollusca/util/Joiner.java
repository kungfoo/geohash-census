package ch.mollusca.util;

import java.util.Iterator;

public class Joiner {
	private String separator;

	public static Joiner on(String separator) {
		return new Joiner(separator);
	}

	private Joiner(String separator) {
		this.separator = separator;
	}

	public <T> String join(T[] ts) {
		if (ts.length == 0) {
			return "";
		}
		if (ts.length == 1) {
			return ts[0].toString();
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ts.length - 1; i++) {
			builder.append(ts[i]).append(separator);
		}
		builder.append(ts[ts.length - 1]);
		return builder.toString();
	}

	public <T> String join(Iterable<T> ts) {
		StringBuilder builder = new StringBuilder();
		Iterator<T> iterator = ts.iterator();
		while (iterator.hasNext()) {
			T t = iterator.next();
			if (iterator.hasNext()) {
				builder.append(t).append(separator);
			} else {
				builder.append(t);
			}
		}
		return builder.toString();
	}
}
