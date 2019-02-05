package com.github.bordertech.wcomponents.addons.table.edit;

import com.github.bordertech.wcomponents.AjaxTarget;
import com.github.bordertech.wcomponents.Container;
import com.github.bordertech.wcomponents.Environment;
import com.github.bordertech.wcomponents.Headers;
import com.github.bordertech.wcomponents.RenderContext;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.UIContext;
import com.github.bordertech.wcomponents.UIContextHolder;
import com.github.bordertech.wcomponents.WLabel;
import com.github.bordertech.wcomponents.WTable;
import com.github.bordertech.wcomponents.WebUtilities;
import com.github.bordertech.wcomponents.util.HtmlClassProperties;
import com.github.bordertech.wcomponents.validation.Diagnostic;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Allow a column in a row to be an AJAX target.
 * <p>
 * As the AJAX Target depends on providing the correct ID for the row it is on, each ID getter is wrapped with the correct row uicontext.
 * </p>
 */
public class RowAjaxTarget implements AjaxTarget {

	private static final String NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG = "Not supported for AJAX Row Target.";
	private final AjaxTarget backing;
	private final WTable tbl;
	private final Object rowKey;

	/**
	 *
	 * @param backing the backing AJAX target on the row
	 * @param tbl the parent table
	 * @param rowKey the row key
	 */
	public RowAjaxTarget(final AjaxTarget backing, final WTable tbl, final Object rowKey) {
		this.backing = backing;
		this.tbl = tbl;
		this.rowKey = rowKey;
	}

	@Override
	public String getId() {
		boolean pop = setupRowUic();
		try {
			return backing.getId();
		} finally {
			clearRowUic(pop);
		}
	}

	@Override
	public void paint(final RenderContext renderContext) {
		boolean pop = setupRowUic();
		try {
			backing.paint(renderContext);
		} finally {
			clearRowUic(pop);
		}
	}

	@Override
	public String getInternalId() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getIdName() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setIdName(final String idName) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void serviceRequest(final Request request) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void invokeLater(final Runnable runnable) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void handleRequest(final Request request) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void forward(final String url) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void preparePaint(final Request request) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void validate(final List<Diagnostic> diags) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void showErrorIndicators(final List<Diagnostic> diags) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void showWarningIndicators(final List<Diagnostic> diags) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setLocked(final boolean lock) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isLocked() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isInitialised() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setInitialised(final boolean flag) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isValidate() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setValidate(final boolean flag) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isVisible() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setVisible(final boolean visible) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isHidden() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean hasTabIndex() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public int getTabIndex() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public WLabel getLabel() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setFocussed() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void tidyUpUIContextForTree() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isDefaultState() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public Container getParent() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getTag() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setTag(final String tag) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public Environment getEnvironment() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setEnvironment(final Environment environment) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public Headers getHeaders() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getBaseUrl() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setAttribute(final String key, final Serializable value) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public Serializable getAttribute(final String key) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public Serializable removeAttribute(final String key) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setToolTip(final String text, final Serializable... args) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getToolTip() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setAccessibleText(final String text, final Serializable... args) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getAccessibleText() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setTrackingEnabled(final boolean track) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isTrackingEnabled() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public boolean isTracking() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setHtmlClass(final String className) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void setHtmlClass(final HtmlClassProperties className) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void addHtmlClass(final String className) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void addHtmlClass(final HtmlClassProperties className) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public String getHtmlClass() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public Set getHtmlClasses() {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void removeHtmlClass(final String className) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public void removeHtmlClass(final HtmlClassProperties className) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_AJAX_ROW_TARGET_MSG);
	}

	@Override
	public int hashCode() {
		return backing.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return backing.equals(obj);
	}

	@Override
	public String toString() {
		return backing.toString();
	}

	/**
	 * Setup the user contest for this row.
	 *
	 * @return true if row context setup successfully
	 */
	protected boolean setupRowUic() {
		UIContext uic = UIContextHolder.getCurrent();
		UIContext ruic = getRowUic();
		if (ruic == null || uic == ruic) {
			return false;
		}
		UIContextHolder.pushContext(ruic);
		return true;
	}

	/**
	 * Clear the row context.
	 *
	 * @param pop true if pop context
	 */
	protected void clearRowUic(final boolean pop) {
		if (pop) {
			UIContextHolder.popContext();
		}
	}

	/**
	 * Find the UIContext for the ROW in a table.
	 *
	 * @return the UIContext for the row
	 */
	protected UIContext getRowUic() {
		// TODO This should be available from WComponents
		// The table maybe in a different context
		UIContext tblContext = WebUtilities.getContextForComponent(tbl);
		UIContextHolder.pushContext(tblContext);
		// Find the context for this row
		try {
			List<WTable.RowIdWrapper> rows = tbl.getRepeater().getBeanList();
			for (WTable.RowIdWrapper row : rows) {
				if (Objects.equals(row.getRowKey(), rowKey)) {
					// Get the UIContext for this rowId
					return tbl.getRepeater().getRowContext(row);
				}
			}
		} finally {
			UIContextHolder.popContext();
		}
		return null;
	}

}
