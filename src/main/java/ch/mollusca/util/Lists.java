package ch.mollusca.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
	public interface Predicate<Type> {
		public boolean matches(Type o);
	}

	public interface Mapper<Type, Result> {
		public Result map(Type o);
	}

	public static final <T> boolean contains(List<T> list, Predicate<T> p) {
		for (T t : list) {
			if (p.matches(t))
				return true;
		}
		return false;
	}

	public static final <T> T find(List<T> list, Predicate<T> p) {
		for (T t : list) {
			if (p.matches(t))
				return t;
		}
		return null;
	}

	public static final <T> List<T> findAll(List<T> list, Predicate<T> p) {
		List<T> result = createResultList(list);
		for (T t : list) {
			if (p.matches(t)) {
				result.add(t);
			}
		}
		return result;
	}

	public static final <From, To> List<To> collect(List<From> list, Mapper<From, To> m) {
		List<To> result = createResultList(list);
		for (From f : list) {
			result.add(m.map(f));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static <R, T> List<R> createResultList(List<T> list) {
		try {
			/* try to use the type we already have */
			List<R> result = list.getClass().newInstance();
			return result;
		} catch (Exception e) {
			/* fall back nicely to a useful default type */
			return new ArrayList<R>();
		}
	}
}
