package com.github.bordertech.wcomponents.lib.dashboard;

import com.github.bordertech.wcomponents.RenderContext;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WTemplate;
import com.github.bordertech.wcomponents.template.TemplateRendererFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds grid panel.
 * <pre>
 *{@code
 *addCssUrl("wclib/css/grid.css");
 *addJsUrl("wclib/js/lib/interact-1.2.6.js");
 *}
 * </pre>
 *
 * @author exitxl
 */
public class Dashboard extends WTemplate {

	private final WContainer gridContainer = new WContainer() {
		@Override
		protected void paintComponent(final RenderContext renderContext) {
			List<String> items = getItemOrder();
			if (items == null) {
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

	/**
	 * Construct panel.
	 */
	public Dashboard() {
		super("wclib/hbs/dashboard/dashboard.hbs", TemplateRendererFactory.TemplateEngine.HANDLEBARS);
		addTaggedComponent("items", gridContainer);
	}

	@Override
	public void handleRequest(final Request request) {
		super.handleRequest(request);
		String[] items = request.getParameterValues(getId() + ".items");
		if (items != null && items.length > 0) {
			// Save state
			setItemOrder(new ArrayList(Arrays.asList(items)));
		}
	}

	public List<String> getItemOrder() {
		return (List<String>) getAttribute("gridItems");
	}

	public void setItemOrder(final ArrayList<String> itemIds) {
		setAttribute("gridItems", itemIds);
	}

	/**
	 * @return the grid content container
	 */
	public WContainer getGridContainer() {
		return gridContainer;
	}

}
