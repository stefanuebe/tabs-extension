package org.vaadin.stefan.tabs;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

/**
 * A sample for a closeable tab. This tab provides a modifiable button, that will remove the tab from its parent and
 * update the selection properly.
 */
public class CloseableTab extends Tab implements ClickNotifier<CloseableTab> {

	public static final String TABS_FILTER = "VAADIN-BUTTON";
	private final Button button;

	/**
	 * Creates a new instance with the given label.
	 * @param label label
	 */
	public CloseableTab(String label) {
		super(label);
		button = new Button(VaadinIcon.CLOSE.create(), event -> close());
		add(button);
	}

	/**
	 * Called by the close button.
	 */
	private void close() {
		getUI().ifPresent(ui -> getParent().ifPresent(p -> {
			Tabs tabs = (Tabs) p;
			int selectedIndex = tabs.getSelectedIndex();
			int i = tabs.indexOf(this);

			tabs.remove(this);

			if (selectedIndex > 0 && selectedIndex > i) {
				ui.getPage().executeJavaScript("$0.selected--;", tabs);
			} else if(selectedIndex == i && tabs.getComponentCount() > 0){
				ui.getPage().executeJavaScript("$0.dispatchEvent(new CustomEvent('selected-changed', {bubbles: true, composed: true}));", tabs);
			}
		}));
	}

	/**
	 * Returns the close button instance.
	 * @return close button instance
	 */
	public Button getButton() {
		return button;
	}
}