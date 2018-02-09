package com.github.bordertech.wcomponents.lib.demo;

import com.github.bordertech.wcomponents.WApplication;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WPanel;
import com.github.bordertech.wcomponents.WText;
import com.github.bordertech.wcomponents.layout.FlowLayout;
import com.github.bordertech.wcomponents.lib.grid.Grid;
import com.github.bordertech.wcomponents.lib.grid.GridItem;
import com.github.bordertech.wcomponents.lib.grid.MediaSize;
import com.github.bordertech.wcomponents.lib.resource.ApplicationResourceWContent;
import com.github.bordertech.wcomponents.lib.resource.TemplateRegisterWclibJsResource;
import com.github.bordertech.wcomponents.lib.resource.TemplateWContent;

/**
 *
 * @author jonathan
 */
public final class GridDemoUtil {

	private GridDemoUtil() {
	}

	public static void configApplication(final WApplication app) {
		// Custom css
		app.addCssFile("/css/app.css");
		app.addCssUrl("wclib/css/layout-flex.css");
		app.addCssUrl("wclib/css/layout-masonry.css");
		app.addCssUrl("wclib/css/layout-grid.css");

		// Custom JS
		TemplateWContent registerWclib = new TemplateWContent(new TemplateRegisterWclibJsResource(), "reg");
		app.add(registerWclib);
		app.addJsResource(new ApplicationResourceWContent(registerWclib, "regkey"));

		app.addJsUrl("wclib/js/lib/interact-1.2.6.js");
//		app.addJsUrl("wclib/js/wc-grid-drag-drop.js");

	}

	public static Grid buildFlexGrid() {
		Grid grid = new Grid();
		grid.setGridType("wcl-flex");
		buildItems(grid);
		return grid;
	}

	public static Grid buildFlexGrid3() {
		Grid grid = new Grid();
		grid.setGridType("wcl-flex");
		grid.setMaxColumns(3);

		WContainer holder = grid.getItemsContainer();
		for (int i = 1; i < 10; i++) {
			WPanel panel = new WPanel(WPanel.Type.BOX);
			panel.add(new WText("COL " + i));

			GridItem item = new GridItem(grid);
			item.getContentHolder().add(panel);
			holder.add(item);
		}

		return grid;
	}

	public static Grid buildCssGrid() {
		Grid grid = new Grid();
		grid.addHtmlClass("wcl-gap-l");
		buildItems(grid);
		return grid;
	}

	public static Grid buildCssGridCols12() {
		Grid grid = new Grid();
		grid.addHtmlClass("wcl-gap-l");
		buildItems(grid);
		return grid;
	}

	public static Grid buildMasonryGrid() {
		Grid grid = new Grid();
		grid.setGridType("wcl-msry");
		grid.getConfig().setTemplateName("wclib/hbs/grid/grid-msry-config.hbs");
		grid.getConfig().setVisible(true);
		buildItems(grid);
		return grid;
	}

	public static Grid buildBorderGrid() {
		Grid grid = new Grid();
		grid.setHtmlClass("wcl-border");
		grid.setMaxColumns(0);

		WContainer holder = grid.getItemsContainer();

		// North
		WPanel panel = new WPanel(WPanel.Type.BOX);
		panel.add(new WText("NORTH"));
		panel.setHtmlClass("wcl-north");
		holder.add(panel);
		// South
		panel = new WPanel(WPanel.Type.BOX);
		panel.add(new WText("SOUTH"));
		panel.setHtmlClass("wcl-south");
		holder.add(panel);
		// East
		panel = new WPanel(WPanel.Type.BOX);
		panel.add(new WText("EAST"));
		panel.setHtmlClass("wcl-east");
		holder.add(panel);
		// West
		panel = new WPanel(WPanel.Type.BOX);
		panel.add(new WText("WEST"));
		panel.setHtmlClass("wcl-west");
		holder.add(panel);
		// Center
		panel = new WPanel(WPanel.Type.BOX);
		panel.add(new WText("CENTER"));
		panel.setHtmlClass("wcl-center");
		holder.add(panel);

		// Nested
		panel.add(buildCssGrid());

		return grid;
	}

	public static void buildItems(final Grid grid) {

		WContainer holder = grid.getItemsContainer();

		for (int i = 1; i < 25; i++) {

			GridItem item = new GridItem(grid);
			holder.add(item);

			WPanel panel = new WPanel(WPanel.Type.BOX);
			panel.setLayout(new FlowLayout(FlowLayout.Alignment.VERTICAL));
			panel.add(new WText("ITEM " + i));
			item.getContentHolder().add(panel);

			if (i % 2 == 0) {
				item.addMediaSize(MediaSize.XS, 10);
				item.addMediaSize(MediaSize.SM, 8);
				item.addMediaSize(MediaSize.MD, 6);
				item.addMediaSize(MediaSize.LG, 4);
				item.addMediaSize(MediaSize.XL, 2);
			} else if (i % 5 == 0) {
				item.addMediaSize(MediaSize.XS, 9);
				item.addMediaSize(MediaSize.SM, 7);
				item.addMediaSize(MediaSize.MD, 5);
				item.addMediaSize(MediaSize.LG, 3);
				item.addMediaSize(MediaSize.XL, 1);
				panel.add(new WText("ITEM A" + i));
				panel.add(new WText("ITEM B" + i));
				panel.add(new WText("ITEM C" + i));
				panel.add(new WText("ITEM D" + i));
				panel.add(new WText("ITEM E" + i));
			}
		}
	}

}
