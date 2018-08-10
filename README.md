# Tab sheet Extension

This addon extends the Vaadin 10 Tabs with a few additional features. This features 
may deprecate in future if the core provides similar functions.

## Development instructions

### Creating a common filter

The extension allows you to set a list of filterable components for a `Tabs` instance that should not fire a selection change event. 
With this feature you may add components, that the user can interact with and let them handle the selection manually.

```
String[] arrayOfTagNames = new String[] {"vaadin-button", "vaadin-text-field", ...};
TabsExtension.createFilterForTabs(tabs, arrayOfTagNames);
```

### Disable tab selection via keyboard

This method is needed when you add writable components (like a text field) as part of you
tabs or as a tab sheet component. Otherwise the tab sheet will select text matching tabs when
you "type" the respective key.
```
TabsExtension.disableKeySelectionOfTabs(tabs);
```

### Removing a modification

All TabExtension methods will return a Registration with that you may remove the changes from your element.

```
Registration registration = TabsExtension.createFilterForTabs(tabs, ...); // filter activated

registration.remove(); // after that call the filter will be deactivated
```

### Closable tabs

The extension comes also with a implementation for closeable tabs. You may use them like other tabs, the only
difference is, that the tab shows a close button, that will remove the tab from the tab sheet and handle
the selection of a new tab.

You may style the tab as wanted. To style the button, just use the `getButton()` method of the tab.
```
CloseableTab closeableTab = new CloseableTab("Label");
closeableTab.addClassName("my-closeable-tab");
closeableTab.getButton().addClassName("my-closeable-button");
```

## Examples
### Example of a filter for closeable tabs

```
Tabs tabs = new Tabs();
add(tabs);
IntStream.range(0, 10)
        .mapToObj(value -> new CloseableTab("Tab " + value))
        .forEach(tabs::add);

tabs.addSelectedChangeListener(event -> Notification.show("Selected " + tabs.getSelectedIndex()));

TabsExtension.createFilterForTabs(tabs, CloseableTab.TABS_FILTER);
TabsExtension.modifyKeyHandlerOfTabs(tabs, false, true);
```

### Example of creating tabs via a inline text field component

```
Tabs tabs = new Tabs();
add(tabs);
tabs.addSelectedChangeListener(event -> Notification.show("Selected " + tabs.getSelectedIndex()));

HorizontalLayout layout = new HorizontalLayout();
TextField text = new TextField();
Button add = new Button("Add");
add.addClickListener(event -> {
    Tab tab = new Tab(text.getValue());
    tabs.add(tab);
    tabs.getElement().insertChild(tabs.getComponentCount() - 1, tab.getElement());
    tabs.setSelectedTab(tab); 
    text.clear();
});

layout.add(text, add);
tabs.add(layout);

TabsExtension.createFilterForTabs(tabs, "vaadin-text-field", "vaadin-button");
TabsExtension.modifyKeyHandlerOfTabs(tabs, true, false);
```