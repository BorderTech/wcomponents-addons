package com.github.bordertech.wcomponents.lib.table;

import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WTableColumn;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Table column renderer and bean value definitions.
 *
 * @param <V> the table column value
 * @param <T> the row bean type
 * @author Jonathan Austin
 */
public interface TableColumn<V, T> extends Serializable {

	/**
	 * @param bean the row bean
	 * @return the value for this column
	 */
	V getValue(final T bean);

	/**
	 * @param bean the row bean
	 * @param value the new value
	 */
	void setValue(final T bean, final V value);

	/**
	 * @return the column comparator
	 */
	Comparator<V> getComparator();

	/**
	 * @return true if column is editable
	 */
	boolean isEditable();

	/**
	 * @return the column id used in config
	 */
	String getColumnId();

	/**
	 * @return the column label
	 */
	String getColumnLabel();

	/**
	 * @return an instance of the renderer
	 */
	WComponent getRenderer();

	/**
	 * @return the column width
	 */
	int getWidth();

	/**
	 * @return the column alignment
	 */
	WTableColumn.Alignment getAlignment();
}
