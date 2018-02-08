package com.github.bordertech.wcomponents.lib.table.edit;

import com.github.bordertech.wcomponents.Action;
import com.github.bordertech.wcomponents.ActionEvent;
import com.github.bordertech.wcomponents.AjaxTarget;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WAjaxControl;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WTable;
import com.github.bordertech.wcomponents.WebUtilities;
import com.github.bordertech.wcomponents.lib.common.IconConstants;
import com.github.bordertech.wcomponents.lib.common.WDiv;
import com.github.bordertech.wcomponents.lib.common.WLibButton;
import com.github.bordertech.wcomponents.util.TableUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Table column renderer that allows row edit actions.
 *
 * @param <T> the row bean type
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class RowActionPanel<T> extends WDiv implements RowActionable {

	private static final String EDIT_ROWS_ATTR_KEY = "wc-edit-rows";

	private final WLibButton editButton = new WLibButton("Edit") {
		@Override
		public boolean isVisible() {
			Object key = TableUtil.getCurrentRowKey();
			return isAllowEdit() && getRowModeKey(key) == RowMode.READ;
		}
	};

	private final WLibButton cancelButton = new WLibButton("Cancel") {
		@Override
		public boolean isVisible() {
			Object key = TableUtil.getCurrentRowKey();
			return getRowModeKey(key) == RowMode.EDIT;
		}
	};

	private final WLibButton deleteButton = new WLibButton("Delete") {
		@Override
		public boolean isVisible() {
			Object key = TableUtil.getCurrentRowKey();
			return isAllowEdit() && getRowModeKey(key) != RowMode.EDIT;
		}
	};

	private final WAjaxControl editAjax = new WAjaxControl(editButton) {
		@Override
		public boolean isVisible() {
			return editButton.isVisible();
		}
	};

	private final WAjaxControl cancelAjax = new WAjaxControl(cancelButton) {
		@Override
		public boolean isVisible() {
			return cancelButton.isVisible();
		}
	};

	/**
	 * Default constructor.
	 */
	public RowActionPanel() {

		// Buttons Panel
		WDiv buttonPanel = new WDiv();
		add(buttonPanel);

		// Edit button
		editButton.setImageUrl(IconConstants.EDIT_IMAGE, true);
		editButton.setRenderAsLink(true);
		editButton.setToolTip("Edit");
		editButton.setAction(new Action() {
			@Override
			public void execute(final ActionEvent event) {
				handleEditButtonAction();
			}
		});

		// Cancel Button
		cancelButton.setImageUrl(IconConstants.CANCEL_IMAGE, true);
		cancelButton.setRenderAsLink(true);
		cancelButton.setToolTip("Cancel");
		cancelButton.setAction(new Action() {
			@Override
			public void execute(final ActionEvent event) {
				handleCancelButtonAction();
			}
		});

		// Delete Button
		deleteButton.setMessage("Do you want to delete row?");
		deleteButton.setImageUrl(IconConstants.REMOVE_IMAGE, true);
		deleteButton.setRenderAsLink(true);
		deleteButton.setToolTip("Delete");
		deleteButton.setAction(new Action() {
			@Override
			public void execute(final ActionEvent event) {
				handleDeleteButtonAction();
			}
		});

		buttonPanel.add(editButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(deleteButton);

		// Ajax Controls
		buttonPanel.add(editAjax);
		buttonPanel.add(cancelAjax);
	}

	@Override
	public RowMode getCurrentRowMode() {
		Object key = TableUtil.getCurrentRowKey();
		if (key == null) {
			return RowMode.READ;
		}
		return getRowMode(key);
	}

	@Override
	public RowMode getRowMode(final Object rowKey) {
		if (!isAllowEdit()) {
			return RowMode.READ;
		}
		return getRowModeKey(rowKey);
	}

	@Override
	public void addRowModeKey(final Object rowKey, final RowMode mode) {
		if (mode == null) {
			throw new IllegalArgumentException("Row mode cannot be null.");
		}
		WTable table = getTable();
		HashMap<Object, RowMode> editRows = (HashMap<Object, RowMode>) table.getAttribute(EDIT_ROWS_ATTR_KEY);
		if (editRows == null) {
			editRows = new HashMap<>();
			table.setAttribute(EDIT_ROWS_ATTR_KEY, editRows);
		}
		editRows.put(rowKey, mode);
	}

	@Override
	public void removeRowModeKey(final Object rowKey) {
		WTable table = getTable();
		HashMap<Object, RowMode> editRows = (HashMap<Object, RowMode>) table.getAttribute(EDIT_ROWS_ATTR_KEY);
		if (editRows != null) {
			editRows.remove(rowKey);
		}
	}

	@Override
	public RowMode getRowModeKey(final Object key) {
		WTable table = getTable();
		Map<Object, RowMode> editRows = (Map<Object, RowMode>) table.getAttribute(EDIT_ROWS_ATTR_KEY);
		if (editRows == null) {
			return RowMode.READ;
		}
		RowMode mode = editRows.get(key);
		return mode == null ? RowMode.READ : mode;
	}

	@Override
	public void clearRowModeKeys() {
		WTable table = getTable();
		table.setAttribute(EDIT_ROWS_ATTR_KEY, null);
	}

	@Override
	protected void preparePaintComponent(final Request request) {
		super.preparePaintComponent(request);
		if (!isInitialised()) {
			setupColumnAjaxTargets();
			setInitialised(true);
		}
	}

	/**
	 * Setup the row action AJAX targets (ie each column).
	 */
	protected void setupColumnAjaxTargets() {
		WTable table = getTable();
		editAjax.addTargets(getColumnAjaxTargets());
		cancelAjax.addTargets(getColumnAjaxTargets());
		deleteButton.setAjaxTarget(table);
	}

	/**
	 * Handle the edit button action.
	 */
	protected void handleEditButtonAction() {
		Object key = TableUtil.getCurrentRowKey();
		addRowModeKey(key, RowMode.EDIT);
	}

	/**
	 * Handle the delete button action.
	 */
	protected void handleDeleteButtonAction() {
		WTable table = getTable();
		Object key = TableUtil.getCurrentRowKey();
		removeRowModeKey(key);
		T bean = (T) getBean();
		List<T> beans = (List<T>) table.getBean();
		beans.remove(bean);
		table.handleDataChanged();
	}

	/**
	 * Handle the cancel button action.
	 */
	protected void handleCancelButtonAction() {
		Object key = TableUtil.getCurrentRowKey();
		removeRowModeKey(key);
		for (AjaxTarget target : getColumnAjaxTargets()) {
			target.reset();
		}
	}

	/**
	 * @return the column AJAX targets
	 */
	protected List<AjaxTarget> getColumnAjaxTargets() {
		List<AjaxTarget> targets = new ArrayList<>();
		WTable table = getTable();
		for (int i = 0; i < table.getColumnCount(); i++) {
			WComponent renderer = table.getColumn(i).getRenderer();
			if (renderer instanceof AjaxTarget) {
				targets.add((AjaxTarget) renderer);
			}
		}
		return targets;
	}

	/**
	 * @return the parent table
	 */
	protected WTable getTable() {
		return WebUtilities.getAncestorOfClass(WTable.class, this);
	}

	/**
	 * @return true if table is in edit mode
	 */
	protected boolean isAllowEdit() {
		return getTable().isEditable();
	}

}
