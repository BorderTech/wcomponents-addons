package com.github.bordertech.wcomponents.lib.grid;

import com.github.bordertech.wcomponents.RenderContext;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WTemplate;
import com.github.bordertech.wcomponents.template.TemplateRendererFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grid panel.
 *
 * @author exitxl
 */
public class Grid extends WTemplate {

	private final WTemplate config = new WTemplate();

	private final WContainer itemsContainer = new WContainer() {
		@Override
		protected void paintComponent(final RenderContext renderContext) {
			List<String> items = getItemOrderIds();
			if (items.isEmpty()) {
				super.paintComponent(renderContext);
			} else {
				// Create map of IDs
				Map<String, WComponent> map = new HashMap<>();
				for (WComponent child : getChildren()) {
					map.put(child.getId(), child);
				}
				// Paint in correct order
				for (String id : items) {
					WComponent child = map.get(id);
					if (child != null) {
						child.paint(renderContext);
					}
				}
			}
		}
	};

	public Grid() {
		super("wclib/hbs/grid/grid-container.hbs", TemplateRendererFactory.TemplateEngine.HANDLEBARS);
		addTaggedComponent("config", config);
		addTaggedComponent("items", itemsContainer);
		config.setVisible(false);
		config.setEngineName(TemplateRendererFactory.TemplateEngine.HANDLEBARS);
	}

	public WTemplate getConfig() {
		return config;
	}

	public void setGridType(final String gridType) {
		getOrCreateComponentModel().gridType = gridType;
	}

	public String getGridType() {
		return getComponentModel().gridType;
	}

	public int getMaxColumns() {
		return getComponentModel().maxColumns;
	}

	public void setMaxColumns(final int maxColumns) {
		getOrCreateComponentModel().maxColumns = maxColumns < 0 ? 0 : maxColumns;
	}

	public List<String> getItemOrderIds() {
		List<String> order = getComponentModel().itemOrderIds;
		return order == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(order);
	}

	public void setItemOrderIds(final List<String> itemOrderIds) {
		getOrCreateComponentModel().itemOrderIds = itemOrderIds;
	}

	public WContainer getItemsContainer() {
		return itemsContainer;
	}

	public void setDraggable(final boolean draggable) {
		if (draggable != isDraggable()) {
			getOrCreateComponentModel().draggable = draggable;
		}
	}

	public boolean isDraggable() {
		return getComponentModel().draggable;
	}

	public void setResizable(final boolean resizable) {
		if (resizable != isResizable()) {
			getOrCreateComponentModel().resizable = resizable;
		}
	}

	public boolean isResizable() {
		return getComponentModel().resizable;
	}

	public void setItemConfigName(final String itemConfigName) {
		getOrCreateComponentModel().itemConfigName = itemConfigName;
	}

	public String getItemConfigName() {
		return getComponentModel().itemConfigName;
	}

	public void setItemTemplateName(final String itemTemplateName) {
		getOrCreateComponentModel().itemTemplateName = itemTemplateName;
	}

	public String getItemTemplateName() {
		return getComponentModel().itemTemplateName;
	}

	public void setItemEngineName(final TemplateRendererFactory.TemplateEngine engine) {
		setItemEngineName(engine == null ? null : engine.getEngineName());
	}

	public void setItemEngineName(final String itemEngineName) {
		getOrCreateComponentModel().itemEngineName = itemEngineName;
	}

	public String getItemEngineName() {
		return getComponentModel().itemEngineName;
	}

	@Override
	public void handleRequest(final Request request) {
		super.handleRequest(request);
		String[] items = request.getParameterValues(getId() + ".items");
		if (items != null && items.length > 0) {
			// Save state
			setItemOrderIds(new ArrayList(Arrays.asList(items)));
		}
	}

	@Override
	protected void preparePaintComponent(final Request request) {
		super.preparePaintComponent(request);
		setupParameters();
	}

	protected void setupParameters() {

		String colsClass = getMaxColumns() > 0 ? "cg-cols-" + getMaxColumns() : "";

		addParameter("gridType", getGridType());
		addParameter("columnsClass", colsClass);
		addParameter("htmlClasses", getHtmlClass());
		addParameter("gridId", getId());
		addParameter("maxColumns", getMaxColumns());
		addParameter("draggable", isDraggable());
		addParameter("resizable", isResizable());

		if (config.isVisible()) {
			for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
				config.addParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	protected GridModel newComponentModel() {
		return new GridModel();
	}

	@Override
	protected GridModel getComponentModel() {
		return (GridModel) super.getComponentModel();
	}

	@Override
	protected GridModel getOrCreateComponentModel() {
		return (GridModel) super.getOrCreateComponentModel();
	}

	/**
	 * Holds the extrinsic state information of the event view.
	 */
	public static class GridModel extends TemplateModel {

		private List<String> itemOrderIds;

		private int maxColumns = 12;

		private boolean draggable;

		private boolean resizable;

		private String itemTemplateName;

		private String itemEngineName;

		private String itemConfigName;

		private String gridType = "cg-grid";
	}

}
