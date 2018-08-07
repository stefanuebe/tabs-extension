# Tabs Extension

This addon extends the Vaadin 10 Tabs with a few additional features. This features 
may deprecate in future when they move into core.

## Development instructions

The activation of the feature orientates on the Java 8 API by "extending" a given component.

### Filter for selection
The extension allows you to set a list of filterable components for a `Tabs` instance that should not fire a selection change event. 

With this feature you may add components, that the user can interact with and let them handle the selection manually.

**Creating a common filter**
```
String[] arrayOfTagNames = new String[] {"vaadin-button", "vaadin-text-field", ...};
TabsExtension.createFilterForTabs(tabs, arrayOfTagNames);
```

**Disable tab selection via keyboard**
This method is needed when you add writable components (like a text field) as part of you
tabs or as a tab sheet component. Otherwise the tab sheet will select text matching tabs when
you "type" the respective key.
```
TabsExtension.disableKeySelectionOfTabs(tabs);
```


**Removing a modification**
All TabExtension methods will return a Registration with that you may remove the changes from your element.

```
Registration registration = TabsExtension.createFilterForTabs(tabs, ...); // filter activated

registration.remove(); // after that call the filter will be deactivated
```

**Example of a filter for closeable tabs**
```
Tabs tabs = new Tabs();
add(tabs);
IntStream.range(0, 10)
        .mapToObj(value -> new CloseableTab("Tab " + value))
        .forEach(tabs::add);

tabs.addSelectedChangeListener(event -> Notification.show("Selected " + tabs.getSelectedIndex()));

TabsExtension.createFilterForTabs(tabs, CloseableTab.TABS_FILTER);
```

**Example of creating tabs via a inline text field component **
This example shows how to add a text field and a button as part of the tab sheet with 
that you may create new tabs. That allows you to give the user the feeling of that the
text field and button are an integrated part of the tab sheet.

```
Tabs tabs = new Tabs();
add(tabs);
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

TabsExtension.createFilterForTabs(tabs, "vaadin-text-field", "vaadin-button");
TabsExtension.disableKeySelectionOfTabs(tabs);
```


