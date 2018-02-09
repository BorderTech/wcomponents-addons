package com.github.bordertech.wcomponents.lib.dashboard;

import com.github.bordertech.wcomponents.AjaxHelper;
import com.github.bordertech.wcomponents.AjaxTarget;
import com.github.bordertech.wcomponents.Margin;
import com.github.bordertech.wcomponents.MessageContainer;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WMessages;
import com.github.bordertech.wcomponents.WPanel;
import com.github.bordertech.wcomponents.WSection;
import com.github.bordertech.wcomponents.WTemplate;
import com.github.bordertech.wcomponents.WText;
import com.github.bordertech.wcomponents.template.TemplateRendererFactory;
import com.github.bordertech.wcomponents.util.Util;

/**
 *
 * @author exitxl
 */
public class DashboardItem extends WTemplate implements MessageContainer {

	private final WSection gridSection;
	private final WText txtHandler = new CtrlButton("handle fa-arrows-alt", "move");
	private final WText txtZoomDown = new CtrlButton("fa-hand-o-down", "zoom in");
	private final WText txtZoomUp = new CtrlButton("fa-hand-o-up", "zoom out");
	private final WText txtRemove = new CtrlButton("fa-trash", "remove");
	private final WMessages messages = new WMessages();
	private final WPanel holder = new WPanel();
	private final WContainer controls = new WContainer();
	private final WPanel resizeGridItemPanel = new WPanel() {
		@Override
		public boolean isHidden() {
			return true;
		}
	};
	private final WContainer menuContainer = new WContainer();

	private final ResizeItemJs jsResizeItem = new ResizeItemJs();

	public DashboardItem(final String contextId, final String title, final int cols) {
		super("wclib/hbs/dashboard/dashboardItem.hbs", TemplateRendererFactory.TemplateEngine.HANDLEBARS);
		setNamingContext(true);
		setIdName(contextId);
		setSearchAncestors(false);

		gridSection = new WSection(title);
		addTaggedComponent("section", gridSection);
		gridSection.getDecoratedLabel().setHead(txtHandler);
		gridSection.getDecoratedLabel().setTail(controls);
		// Zoom down
		controls.add(txtZoomDown);
		// Zoom up
		controls.add(txtZoomUp);
		// Remove
		controls.add(txtRemove);

		gridSection.setHtmlClass("no-border");

		gridSection.getContent().add(menuContainer);
		gridSection.getContent().add(messages);
		gridSection.getContent().add(holder);

		messages.setMargin(new Margin(0, 0, 12, 0));

		// Resize Item
		gridSection.getContent().add(resizeGridItemPanel);
		resizeGridItemPanel.add(jsResizeItem);

		setMinColumns(cols);
	}

	@Override
	public void handleRequest(final Request request) {
		super.handleRequest(request);
		// Save state
		String value = request.getParameter(getId() + ".itemcols");
		if (!Util.empty(value)) {
			setExpandedColumns(Integer.valueOf(value));
		}
	}

	@Override
	public String getHtmlClass() {
		int min = getMinColumns();
		int exp = getExpandedColumns();
		int pos = getPosition();
		String htmlClazz = super.getHtmlClass() == null ? "" : super.getHtmlClass() + " ";
		String htmlClass = htmlClazz + " grid-item-col" + min;
		if (pos > -1) {
			htmlClass += " grid-item-pos" + pos;
		}
		if (exp > min) {
			htmlClass += " is-expanded" + exp;
		}
		return htmlClass;
	}

	public void resizeGridItem() {
		// Only resize in AJAX request
		if (AjaxHelper.getCurrentOperation() != null) {
			jsResizeItem.resizeGridItem();
		}
	}

	public WContainer getMenuContainer() {
		return menuContainer;
	}

	public WPanel getContentHolder() {
		return holder;
	}

	public void setUseResize(final boolean resize) {
		txtZoomUp.setVisible(resize);
	}

	public void setUseRemove(final boolean remove) {
		txtRemove.setVisible(remove);
	}

	@Override
	public WMessages getMessages() {
		return messages;
	}

	public void addControl(final WComponent control) {
		controls.add(control);
	}

	public AjaxTarget getGridAjaxTarget() {
		return gridSection.getContent();
	}

	public AjaxTarget getResizeGridItemTarget() {
		return resizeGridItemPanel;
	}

	public void setMinColumns(final int cols) {
		setAttribute("mincols", cols < 1 ? 1 : cols);
	}

	public int getMinColumns() {
		Integer cols = (Integer) getAttribute("mincols");
		return cols == null ? 1 : cols;
	}

	public void setPosition(final int pos) {
		setAttribute("pos", pos < 0 ? null : pos);
	}

	public int getPosition() {
		Integer pos = (Integer) getAttribute("pos");
		return pos == null ? -1 : pos;
	}

	public void setExpandedColumns(final int cols) {
		setAttribute("itemcols", cols < 1 ? 1 : cols);
	}

	public int getExpandedColumns() {
		Integer cols = (Integer) getAttribute("itemcols");
		return cols == null ? 1 : cols;
	}

}
