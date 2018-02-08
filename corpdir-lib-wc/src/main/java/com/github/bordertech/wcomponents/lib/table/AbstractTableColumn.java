package com.github.bordertech.wcomponents.lib.table;

import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WTableColumn;
import com.github.bordertech.wcomponents.WText;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Table column details.
 * <p>
 * This class is not thread safe as it has class level members that are not final. If these need to be updated per user,
 * then a new instance needs to be created per user session.
 * <p>
 *
 * @param <V> the columns value type
 * @param <T> the row bean type
 * @author Jonathan Austin
 * @since 1.0.0
 */
public abstract class AbstractTableColumn<V, T> implements TableColumn<V, T> {

	/**
	 * Column comparator.
	 */
	private final Comparator comparator;

	/**
	 * Column id.
	 */
	private final String columnId;

	/**
	 * Column label.
	 */
	private final String columnLabel;

	/**
	 * Renderer instance.
	 */
	private final WComponent renderer;

	/**
	 * Column is editable.
	 */
	private boolean editable;

	/**
	 * Column width.
	 */
	private int width;

	/**
	 * Column alignment.
	 */
	private WTableColumn.Alignment alignment;

	/**
	 * @param label the column label
	 */
	public AbstractTableColumn(final String label) {
		this(null, label, new WText(), null);
	}

	/**
	 * @param label the column label
	 * @param comparator the column comparator
	 */
	public AbstractTableColumn(final String label, final Comparator comparator) {
		this(null, label, new WText(), comparator);
	}

	/**
	 * @param label the column label
	 * @param renderer the column renderer
	 */
	public AbstractTableColumn(final String label, final WComponent renderer) {
		this(null, label, renderer, null);
	}

	/**
	 * @param label the column label
	 * @param renderer the column renderer
	 * @param comparator the column comparator
	 */
	public AbstractTableColumn(final String label, final WComponent renderer, final Comparator comparator) {
		this(null, label, renderer, comparator);
	}

	/**
	 * @param columnId the column id
	 * @param label the column label
	 * @param renderer the column renderer
	 * @param comparator the column comparator
	 */
	public AbstractTableColumn(final String columnId, final String label, final WComponent renderer,
			final Comparator comparator) {
		checkComparator(comparator);
		this.columnId = columnId == null ? UUID.randomUUID().toString() : columnId;
		this.columnLabel = label;
		this.comparator = comparator;
		this.renderer = renderer;
	}

	/**
	 * @param comp the comparator to check is valid
	 */
	protected final void checkComparator(final Comparator comp) {
		if (comp != null && !(comp instanceof Serializable)) {
			throw new IllegalArgumentException("Comparator must implement serializable");
		}
	}

	@Override
	public Comparator getComparator() {
		return comparator;
	}

	@Override
	public String getColumnId() {
		return columnId;
	}

	@Override
	public String getColumnLabel() {
		return columnLabel;
	}

	@Override
	public WComponent getRenderer() {
		return renderer;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable true if column is editable
	 */
	public void setEditable(final boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof AbstractTableColumn)
				&& Objects.equals(columnId, ((AbstractTableColumn) obj).getColumnId());
	}

	@Override
	public int hashCode() {
		return columnId.hashCode();
	}

	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the column width
	 */
	public void setWidth(final int width) {
		this.width = width;
	}

	@Override
	public WTableColumn.Alignment getAlignment() {
		return alignment;
	}

	/**
	 * @param align the column alignment
	 */
	public void setAlignment(final WTableColumn.Alignment align) {
		this.alignment = align;
	}

	@Override
	public void setValue(final T bean, final V value) {
		// NOP
	}

}
