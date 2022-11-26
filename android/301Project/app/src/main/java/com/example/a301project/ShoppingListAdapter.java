package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;



/**
 * This class creates a custom array list for ShoppingItem objects
 * contains a constructor and a method that returns a view of the custom list
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingItem> {
    private final ArrayList<ShoppingItem> shoppingItems;
    private final Context context;
    private ShoppingListAdapterListener listener;


    /**
     * Makes a Custom list from an array list of ingredients
     * @param context {@link Context} context to the array list
     * @param shoppingItems {@link ArrayList<ShoppingItem>} array list containing shopping items
     * shopping items are similar to ingredients, but they have less attributes
     */
    public ShoppingListAdapter(Context context, ArrayList<ShoppingItem> shoppingItems, ShoppingListAdapterListener listener) {
        super(context, 0, shoppingItems);

        this.shoppingItems = shoppingItems;
        this.context = context;
        this.listener = listener;
    }

    public interface ShoppingListAdapterListener {
        public void  onButtonPressed(int position);
    }

    /**
     * Method for creating a view that will appear in the ingredient adapter
     * @param position {@link Integer} the position of the current view
     * @param convertView {@link View} the reused view to be retrieved
     * @param parent {@link ViewGroup} the collection of views that contains current view
     * @return a view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ShoppingItem s = shoppingItems.get(position);


        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.shopping_row_layout, parent, false);
        }

        // if the purchased button is checked
        Button shoppingItemPurchasedButton = view.findViewById(R.id.shoppingItemPurchasedButton);
        shoppingItemPurchasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonPressed(position);
            }
        });


        // list view to attributes of each shopping item object
        // by finding view text boxes to their ID
        TextView shoppingItemName = view.findViewById(R.id.s_nameText);
        TextView amountName = view.findViewById(R.id.s_amountText);
        TextView unitName = view.findViewById(R.id.s_unitText);
        TextView categoryName = view.findViewById(R.id.s_categoryText);

        // sets the text
        shoppingItemName.setText(s.getName());
        amountName.setText("Need: " + s.getAmount().toString());
        unitName.setText(s.getUnit());
        categoryName.setText(s.getCategory());

        // if the test is null -> don't display the text
        if (s.getUnit() == "null") {
            unitName.setVisibility(View.INVISIBLE);
        }
        if (s.getCategory() == "null") {
            categoryName.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
