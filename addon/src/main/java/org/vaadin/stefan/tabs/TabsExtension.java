package org.vaadin.stefan.tabs;

import com.vaadin.flow.component.page.Page;
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
	 *
	 * @param tabs         - tab sheet instance
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
	 * This method modifies the keyhandler of the tab sheet. Normally the tab catches the key event, that leads to
	 * unwanted reaction of added components (like buttons or text fields). With this method you may override
	 * the normal keyhandler of the tab sheet and allow inner components to work normally.
	 *
	 * @param tabs                        tab sheet to modify
	 * @param disableCharacterCatch       This flag disables the catching of normal characters when you type in a text field inside the
	 *                                    tab. Normally the tabsheet looks for tabs with the starting letter and auto select that tab while
	 *                                    consuming the character (and thus make it not possible to write a normal text). With this flag
	 *                                    set to <b>true</b> you may use the text fields as intended.
	 * @param enableButtonClickableByKeys Buttons are not clickable via keyboard keys like Enter or Spacebar. With this flag
	 *                                    you may focus the buttons and "hit" them via Enter or Space keys.
	 * @return registration to remove the modification
	 */
	public static Registration modifyKeyHandlerOfTabs(Tabs tabs, boolean disableCharacterCatch, boolean enableButtonClickableByKeys) {
		tabs.getElement().getNode().runWhenAttached(ui -> {
			Page page = ui.getPage();
			restoreKeydownHandler(tabs, page);

			page.executeJavaScript("$0._superOnKeydown = $0._onKeydown; ", tabs);
			String expression = "$0._onKeydown = function(event) {" +
					"   const key = event.key.replace(/^Arrow/, '');";

			if (enableButtonClickableByKeys) {
				expression += "   if((event.target.tagName === 'A' || event.target.tagName === 'VAADIN-BUTTON') && (key == 'Enter' || key == ' ')) {" +
						"       event.target.click();" +
						"   } else ";
			}

			if (!disableCharacterCatch && enableButtonClickableByKeys) {
				expression += "{ $0._superOnKeydown(event); }";
			}


			if (disableCharacterCatch) {
				expression += "if(key.length != 1) {" +
						"       $0._superOnKeydown(event);" +
						"   }";
			}
						expression += "}; ";
			page.executeJavaScript(expression, tabs);

		});

		return () -> {
			tabs.getElement().getNode().runWhenAttached(ui -> {
				restoreKeydownHandler(tabs, ui.getPage());
			});
		};
	}

	/**
	 * Restores the former key down handler.
	 *
	 * @param tabs tabs
	 * @param page page
	 */
	private static void restoreKeydownHandler(Tabs tabs, Page page) {
		page.executeJavaScript("if($0._superOnKeydown) {$0._onKeydown = $0._superOnKeydown;}", tabs);
		page.executeJavaScript("$0._superOnKeydown = undefined; ", tabs);
	}
}
