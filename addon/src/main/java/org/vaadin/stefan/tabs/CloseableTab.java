package org.vaadin.stefan.tabs;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class CloseableTab extends Tab implements ClickNotifier<CloseableTab> {
	private boolean closed;

	public CloseableTab(String label) {
		super(label);
		Button button = new Button(VaadinIcon.CLOSE.create(), event -> close());
		add(button);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
	}

	private void close() {
		closed = true;
		getUI().ifPresent(ui -> getParent().ifPresent(p -> {
			Tabs tabs = (Tabs) p;
			int selectedIndex = tabs.getSelectedIndex();
			int i = tabs.indexOf(this);

			tabs.remove(this);

			if (selectedIndex > 0 && selectedIndex >= i) {
				ui.getPage().executeJavaScript("$0.selected--;", tabs);
			}
		}));
	}

	public boolean isClosed() {
		return closed;
	}
}