package org.vaadin.stefan.tabs.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.vaadin.stefan.tabs.CloseableTab;
import org.vaadin.stefan.tabs.TabsExtension;

import java.util.stream.IntStream;

/**
 * The main view contains a button and a template element.
 */
@Route("")
public class DemoView extends Div {

	public DemoView() {
		add(new H1("TabExtension demos"));
		add(new H3("Demo 1"));
		add(new Paragraph("This demo shows closeable tabs. You may click the button via mouse or keyboard without" +
				" having the tab sheet firing a selection change event. Also, if available a proper sibling will" +
				" be selected (that fires a normal selection change event in both cases)."));

		add(new H5("Tabsheet WITHOUT select change listener / button click modification"));
		add(new Paragraph("Button will not react on keyboard 'click'. Not selected tab will be selected before closed."));
		add(createDemo1Tabsheet());

		add(new H5("Tabsheet WITH select change listener / button click modification"));
		add(new Paragraph("Button will react on keyboard 'click'. Not selected tab will not be selected when just closed."));

		Tabs tabs = createDemo1Tabsheet();
		add(tabs);


		Registration filterForTabs = TabsExtension.createFilterForTabs(tabs, CloseableTab.TABS_FILTER);
		TabsExtension.modifyKeyHandlerOfTabs(tabs, false, true);


		/* ---------- */
		add(new H3("Demo 2"));
		add(new Paragraph("This demo shows the modification of the keydown handler of the tab sheet to allow you" +
				" using a text field inside the tab sheet. Normally chars that equals a tab name start would be consumed and" +
				" the tab sheet selects that tab for you. With that you cannot really type text into the text field. With" +
				" the usage of that method, you may override this handling to use text fields and similar inside of the tab."));

		add(new H5("Tabsheet WITHOUT keydown modification"));
		add(new Paragraph("Tab sheet will react on keyboard input of normal chars."));
		add(createDemo2Tabsheet());

		add(new H5("Tabsheet WITH keydown modification"));
		add(new Paragraph("Tab sheet will not react on keyboard input of normal chars."));

		tabs = createDemo2Tabsheet();
		add(tabs);

		TabsExtension.createFilterForTabs(tabs, "vaadin-text-field", "vaadin-button");
		TabsExtension.modifyKeyHandlerOfTabs(tabs, true, true);


//		filterForTabs.remove();
	}

	private Tabs createDemo1Tabsheet() {
		Tabs tabs = new Tabs();
		add(tabs);
		IntStream.range(0, 10)
				.mapToObj(value -> new CloseableTab("Tab " + value))
				.forEach(tabs::add);


		tabs.setSelectedIndex(0);
		tabs.addSelectedChangeListener(event -> Notification.show("Selected tab index " + tabs.getSelectedIndex()));
		return tabs;
	}

	private Tabs createDemo2Tabsheet() {
		Tabs tabs = new Tabs();
		tabs.addSelectedChangeListener(event -> Notification.show("Selected " + tabs.getSelectedIndex()));

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		TextField textField = new TextField();
		Button add = new Button("Add");

		add.addClickListener(event -> {
			Tab tab = new Tab(textField.getValue());
			tabs.add(tab);

			tabs.getElement().insertChild(tabs.getComponentCount() - 1, tab.getElement());
			tabs.setSelectedTab(tab); // does not work?

			textField.clear();
		});

		horizontalLayout.add(textField, add);
		tabs.add(horizontalLayout);

		return tabs;
	}

}
