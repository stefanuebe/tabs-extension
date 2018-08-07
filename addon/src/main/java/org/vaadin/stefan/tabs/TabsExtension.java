package org.vaadin.stefan.tabs;

import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.Registration;

import java.util.Arrays;

/**
 * Provides different methods to enhance the behavior of the Vaadin 10 tab sheet.
 */
public class TabsExtension {

	/**
	 * This methods modify the given tab sheet so that the 'selected-change' event will not be fired, if the
	 * user clicked on a component, that is specified by the given tags.
	 * <p/>
	 * The call with "vaadin-button" for example would allow you to add buttons into the tab header and click on
	 * them without having the tab selected afterwards. With that you may realize close buttons for tabs.
	 * @param tabs - tab sheet instance
	 * @param tagsToFilter - list of component tab names to filter
	 * @return registration to remove the modification
	 */
	public static Registration createFilterForTabs(Tabs tabs, String... tagsToFilter) {
		tabs.getElement().getNode().runWhenAttached(ui -> {
			String[] convertedTagsToFilter = Arrays.stream(tagsToFilter).map(String::toUpperCase).toArray(String[]::new);
			String joinedFilter = String.join("','", convertedTagsToFilter);


			ui.getPage().executeJavaScript("$0._superOnClick = $0._onClick;", tabs);
			ui.getPage().executeJavaScript("$0._onClick = function(event) {" +
					"   if(!Array.of('" + joinedFilter + "').includes(event.target.tagName)) {" +
					"       $0._superOnClick(event);" +
					"   }" +
					"};", tabs);

		});

		return () -> {
			tabs.getElement().getNode().runWhenAttached(ui -> {
				ui.getPage().executeJavaScript("$0._onClick = $0._superOnClick;", tabs);
				ui.getPage().executeJavaScript("$0._superOnClick = undefined; ", tabs);
			});
		};
	}

	/**
	 * This method disables the functionality with that the tab sheet listen to keyboard input while being focused.
	 * With that modification you may add writable comoponents into the tab sheet. Normally the tab sheet would
	 * consume characters and select tabs with a matching starting char instead.
	 *
	 * @param tabs - tab sheet to modify
	 * @return registration instance to remove the modification
	 */
	public static Registration disableKeySelectionOfTabs(Tabs tabs) {
		tabs.getElement().getNode().runWhenAttached(ui -> {

			ui.getPage().executeJavaScript("$0._superOnKeydown = $0._onKeydown; ", tabs);
			ui.getPage().executeJavaScript("$0._onKeydown = function(event) {" +
					"   const key = event.key.replace(/^Arrow/, '');" +
					"   if(key.length != 1) {" +
					"       $0._superOnKeydown(event);" +
					"   }" +
					"}; ", tabs);
		});

		return () -> {
			tabs.getElement().getNode().runWhenAttached(ui -> {
				ui.getPage().executeJavaScript("$0._onKeydown = $0._superOnKeydown;", tabs);
				ui.getPage().executeJavaScript("$0._superOnKeydown = undefined; ", tabs);
			});
		};
	}
}
