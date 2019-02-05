package com.github.bordertech.wcomponents.addons.common;

import org.apache.commons.collections.comparators.ComparableComparator;

/**
 * Ignore case String Comparator.
 */
public class IgnoreCaseComparator extends ComparableComparator {

	@Override
	public int compare(final Object comp1, final Object comp2) {
		if (comp1 == null && comp2 == null) {
			return 0;
		} else if (comp1 == null) {
			return -1;
		} else if (comp2 == null) {
			return 1;
		} else {
			return ((String) comp1).compareToIgnoreCase((String) comp2);
		}
	}

}
