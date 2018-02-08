package com.github.bordertech.wcomponents.lib.table;

import com.github.bordertech.wcomponents.WComponent;
import java.util.Comparator;

/**
 * Default table column that returns the row bean as the column bean.
 *
 * @author jonathan
 * @param <T> the row and column bean type
 */
public class DefaultTableColumn<T> extends AbstractTableColumn<T, T> {

	/**
	 * @param label the column label
	 */
	public DefaultTableColumn(final String label) {
		super(label);
	}

	/**
	 * @param label the column label
	 * @param renderer the column renderer
	 */
	public DefaultTableColumn(final String label, final WComponent renderer) {
		super(label, renderer);
	}

	/**
	 * @param label the column label
	 * @param renderer the column renderer
	 * @param comparator the column comparator
	 */
	public DefaultTableColumn(final String label, final WComponent renderer, final Comparator comparator) {
		super(label, renderer, comparator);
	}

	/**
	 * @param columnId the column id
	 * @param label the column label
	 * @param renderer the column renderer
	 * @param comparator the column comparator
	 */
	public DefaultTableColumn(final String columnId, final String label, final WComponent renderer,
			final Comparator comparator) {
		super(columnId, label, renderer, comparator);
	}

	@Override
	public T getValue(final T bean) {
		return bean;
	}

}
