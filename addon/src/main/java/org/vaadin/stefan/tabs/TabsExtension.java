package org.vaadin.stefan.tabs;

import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.Registration;

import java.util.Arrays;

public class TabsExtension {
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
