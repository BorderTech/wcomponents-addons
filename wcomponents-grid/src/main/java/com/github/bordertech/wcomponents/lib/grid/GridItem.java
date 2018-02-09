package com.github.bordertech.wcomponents.lib.grid;

import com.github.bordertech.wcomponents.AjaxHelper;
import com.github.bordertech.wcomponents.RenderContext;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WTemplate;
import com.github.bordertech.wcomponents.lib.common.DragMode;
import com.github.bordertech.wcomponents.lib.common.DropMode;
import com.github.bordertech.wcomponents.lib.common.WDiv;
import com.github.bordertech.wcomponents.template.TemplateRendererFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Grid Item.
 *
 * @author exitxl
 */
public class GridItem extends WTemplate {

	private final WTemplate config = new WTemplate();

	private final WDiv contentHolder = new WDiv();

	private final Grid grid;

	public GridItem(final Grid grid) {
		super("wclib/hbs/grid/grid-item.hbs", TemplateRendererFactory.TemplateEngine.HANDLEBARS);
		this.grid = grid;
		addTaggedComponent("config", config);
		addTaggedComponent("content", contentHolder);
		config.setVisible(false);
		config.setEngineName(TemplateRendererFactory.TemplateEngine.HANDLEBARS);
	}

	public Grid getGrid() {
		return grid;
	}

	public WTemplate getConfig() {
		return config;
	}

	@Override
	public void handleRequest(final Request request) {
		super.handleRequest(request);
		clearResize();
	}

	public WDiv getContentHolder() {
		return contentHolder;
	}

	public void resizeItem() {
		// Only do resize if in AJAX operation
		boolean ajax = AjaxHelper.getCurrentOperation() != null;
		if (!isResize() && ajax) {
			getOrCreateComponentModel().resize = true;
		}
	}

	public void clearResize() {
		if (isResize()) {
			getOrCreateComponentModel().resize = false;
		}

	}

	public boolean isResize() {
		return getComponentModel().resize;
	}

	@Override
	protected void preparePaintComponent(final Request request) {
		super.preparePaintComponent(request);
		setupParameters();
		if (grid.getItemTemplateName() != null) {
			setTemplateName(grid.getItemTemplateName());
		}
		if (grid.getItemEngineName() != null) {
			setEngineName(grid.getItemEngineName());
		}
		if (grid.getItemConfigName() != null) {
			config.setTemplateName(grid.getItemConfigName());
			config.setVisible(true);
		}
	}

	protected void setupParameters() {

		// Build media size classes
		int max = grid.getMaxColumns();
		StringBuilder sizeClasses = new StringBuilder();
		for (Map.Entry<MediaSize, Integer> entry : getMediaSizes().entrySet()) {
			int size = entry.getValue() > max ? max : entry.getValue();
			if (size > 0) {
				String media = entry.getKey() == MediaSize.XS ? "" : "-" + entry.getKey().toString();
				sizeClasses.append(" cg-col").append(media).append("-").append(size);
			}
		}

		addParameter("gridId", getGrid().getId());
		addParameter("sizeClasses", sizeClasses.toString());
		addParameter("itemId", getId());
		addParameter("resize", isResize());
		addParameter("htmlClasses", getHtmlClass());
		addParameter("dropMode", getDropMode());
		addParameter("dragMode", getDragMode());

		if (config.isVisible()) {
			for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
				config.addParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	public void setDragMode(final DragMode mode) {
		getOrCreateComponentModel().dragMode = mode;
	}

	public DragMode getDragMode() {
		return getComponentModel().dragMode;
	}

	public void setDropMode(final DropMode mode) {
		getOrCreateComponentModel().dropMode = mode;
	}

	public DropMode getDropMode() {
		return getComponentModel().dropMode;
	}

	public void addMediaSizes(final int sm, final int lg) {
		if (sm > 0) {
			addMediaSize(MediaSize.SM, sm);
		}
		if (lg > 0) {
			addMediaSize(MediaSize.LG, lg);
		}
	}

	public void addMediaSize(final MediaSize media, final int size) {
		GridItemModel model = getOrCreateComponentModel();
		if (model.mediaSizes == null) {
			model.mediaSizes = new HashMap<>();
		}
		model.mediaSizes.put(media, size);
	}

	public void removeMediaSize(final MediaSize media) {
		GridItemModel model = getOrCreateComponentModel();
		if (model.mediaSizes != null) {
			model.mediaSizes.remove(media);
			if (model.mediaSizes.isEmpty()) {
				model.mediaSizes = null;
			}
		}
	}

	public void clearMediaSizes() {
		getOrCreateComponentModel().mediaSizes = null;
	}

	public Map<MediaSize, Integer> getMediaSizes() {
		Map<MediaSize, Integer> sizes = getComponentModel().mediaSizes;
		return sizes == null ? Collections.EMPTY_MAP : Collections.unmodifiableMap(sizes);
	}

	@Override
	protected void afterPaint(final RenderContext renderContext) {
		super.afterPaint(renderContext);
		clearResize();
	}

	@Override
	protected GridItemModel newComponentModel() {
		return new GridItemModel();
	}

	@Override
	protected GridItemModel getComponentModel() {
		return (GridItemModel) super.getComponentModel();
	}

	@Override
	protected GridItemModel getOrCreateComponentModel() {
		return (GridItemModel) super.getOrCreateComponentModel();
	}

	/**
	 * Holds the extrinsic state information of the event view.
	 */
	public static class GridItemModel extends TemplateModel {

		private boolean resize;

		private DragMode dragMode;

		private DropMode dropMode;

		private Map<MediaSize, Integer> mediaSizes;
	}

}
