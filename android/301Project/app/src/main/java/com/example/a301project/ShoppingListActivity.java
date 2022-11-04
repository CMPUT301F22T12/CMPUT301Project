package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Collections;

/**
 * /**
 *  Main Activity class for Shopping List
 *  functionalities for add, edit, delete
 *  initiates an ShoppingListController object that has access to firebase data
 *  handles sorting
 *  @return void
 */
public class ShoppingListActivity extends NavActivity {
    private ListView listView;
    private ArrayAdapter<ShoppingItem> shoppingItemArrayAdapter;
    private ArrayList<ShoppingItem> shoppingItemDataList = new ArrayList<>();
    private ShoppingListController controller = new ShoppingListController();
    private final String[] sortOptions = {"Title", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;

    /**
     * Method for the activity becomes active and can receive input
     * Navigation panel finds the menu and displays it
     */
    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.getMenu().findItem(R.id.action_shopping_list).setChecked(true);
    }

    /**
     * Method for initializing attributes of this activity
     * @param savedInstanceState {@link Bundle} the last saved instance of the fragment, NULL if newly created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_shopping_list, content, true);

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_shopping_list).setChecked(true);

        // Fetch the data
        controller.getShoppingItems(res -> setShoppingItemDataList(res));

        // Attach to listView
        shoppingItemArrayAdapter = new ShoppingListAdapter(this, shoppingItemDataList);
        listView = findViewById(R.id.shoppingItemListView);
        listView.setAdapter(shoppingItemArrayAdapter);

        // Setup sorting
        sortSpinner = findViewById(R.id.shoppingSortSpinner);
        sortSwitch = findViewById(R.id.shoppingSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Method invoked when a sort parameter in this view is selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortDataBySpinner();
            }

            /**
             * Method for when no spinner item is selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @return void
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens

            }
        });
        sortSwitch.setChecked(true);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Method for when sort switch is clicked
             * @param compoundButton {@link CompoundButton} the switch button view that has changed
             * @param b {@link boolean} the checked state of the button
             * @return void
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sortDataBySpinner();
            }
        });
        sortDataBySpinner();
    }

    /**
     * Sets the internal shopping items list the new one.
     * @param a ArrayList of shopping items to set the data list to
     */
    private void setShoppingItemDataList(ArrayList<ShoppingItem> a) {
        shoppingItemDataList.clear();
        shoppingItemDataList.addAll(a);
        shoppingItemArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sorts internal recipe list by selected parameters defined the sortOptions attribute
     * Sort by parameters: Title, Category
     */
    private void sortDataBySpinner() {
        // Make sure views are defined
        sortSpinner = findViewById(R.id.shoppingSortSpinner);
        sortSwitch = findViewById(R.id.shoppingSortSwitch);

        String sortBy = sortSpinner.getSelectedItem().toString();
        Integer asc = sortSwitch.isChecked() ? 1 : -1;

        // determine which sort option was selected, then sort them in ascending or descending order
        // ascending or descending is based on the asc variable
        Collections.sort(shoppingItemDataList, (ShoppingItem s1, ShoppingItem s2) -> {
                    if (sortBy.equals(sortOptions[0])) {
                        return asc * s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
                    } else {
                        return asc * s1.getCategory().toLowerCase().compareTo(s2.getCategory().toLowerCase());
                    }
                }
        );

        shoppingItemArrayAdapter.notifyDataSetChanged();
    }
}