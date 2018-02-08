package com.github.bordertech.wcomponents.lib.table.edit;

import java.io.Serializable;

/**
 * Marks the column used to hold the row actions.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public interface RowActionable extends Serializable {

	/**
	 * @return the current row mode
	 */
	RowMode getCurrentRowMode();

	/**
	 * @param rowKey the row key to check and considers if the table is editable
	 * @return the row mode
	 */
	RowMode getRowMode(final Object rowKey);

	/**
	 * @param rowKey add the following key to the row modes
	 * @param mode the row mode
	 */
	void addRowModeKey(final Object rowKey, final RowMode mode);

	/**
	 * @param rowKey remove the following key to the row modes
	 */
	void removeRowModeKey(final Object rowKey);

	/**
	 * @param rowKey the row key to check
	 * @return the row mode
	 */
	RowMode getRowModeKey(final Object rowKey);

	/**
	 * Clear all the row mode keys.
	 */
	void clearRowModeKeys();

}
