package com.example.a301project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Array;
import java.util.Calendar;

/**
 * A class for a fragment that handles adding and editing ingredients
 * Fragment is activated when user clicks certain buttons
 */
public class AddEditIngredientFragment extends DialogFragment {
    // fragment used for adding and editing an ingredient
    private EditText ingredientName;
    private EditText amountName;
    private Spinner locationName;
    private Spinner unitName;
    private EditText bbdName;
    private Spinner categoryName;
    private OnFragmentInteractionListener listener;
    private DatePickerDialog.OnDateSetListener setListener;
    private Button deleteButton;

    /**
     * Method that responds when the fragment has been interacted with
     * OnConfirmPressed either creates a new Ingredient or updates an existing one based on boolean createNewIngredient
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient);
    }

    /**
     * Method for when fragment is attached to the screen
     * @param context {@link Context} the context of the fragment
     * sets the listener object as OnFragmentInteractionListener
     * throws exception if context is not an instance of OnFragmentInteractionListener
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddEditIngredientFragment.OnFragmentInteractionListener) {
            listener = (AddEditIngredientFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    Ingredient currentIngredient;
    boolean createNewIngredient;

    /**
     * Method to set the fragment attributes
     * Sets the information of current ingredient if the tag is EDIT
     * Sets empty EditText views if the tag is ADD, and hides delete button
     * Creates new ingredient or resets information of current ingredient based on the tag
     * @param savedInstanceState {@link Bundle} the last saved instance state of fragment, NULL if
     *                                         fragment is newly created
     * @return dialog fragment with the appropriate fields
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_ingredientlayout, null);

        // get the current date as the default in date picker
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentIngredient = (Ingredient) bundle.get("ingredient");
            createNewIngredient = (boolean) bundle.get("createNew");
        }

        // set variables of EditText fields
        ingredientName = view.findViewById(R.id.edit_name);
        bbdName = view.findViewById(R.id.edit_bbd);
        locationName = view.findViewById(R.id.edit_location);
        amountName = view.findViewById(R.id.edit_amount);
        unitName = view.findViewById(R.id.edit_unit);
        categoryName = view.findViewById(R.id.edit_category);
        deleteButton = view.findViewById(R.id.delete_ingredient_button);

        // sets title of the fragment depending on whether the tag is ADD or EDIT
        String title;
        if (this.getTag().equals("ADD")) {
            title = "Add Entry";
            deleteButton.setVisibility(View.GONE);
        }
        else {
            title = "Edit Entry";
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method for when Delete button is clicked
             * Another fragment pops up to confirm whether user meant to delete
             * @param view {@link View} the view of the fragment that was clicked
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this ingredient?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                IngredientController controller = new IngredientController();
                                controller.removeIngredient(currentIngredient);

                                Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("EDIT");
                                getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
                                Toast.makeText(getContext(), "Ingredient Delete Successful", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /**
                             * Method for when negative button is clicked in delete fragment
                             * @param dialog {@link DialogInterface} the interface of this pop up fragment
                             * @param id {@link Integer} ID of the recipe to be deleted
                             */
                            public void onClick(DialogInterface dialog, int id) {
                                // if No is pressed, return to Edit fragment
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.category_array, R.layout.ingredient_unit_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryName.setAdapter(categoryAdapter);
        categoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Method invoked when a category in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryName.setSelection(i);
                currentIngredient.setCategory(adapterView.getItemAtPosition(i).toString());
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens
            }
        });

        // Location spinner
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.location_array, R.layout.ingredient_unit_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationName.setAdapter(locationAdapter);
        locationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /**
             * Method invoked when a location in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationName.setSelection(i);
                currentIngredient.setLocation(adapterView.getItemAtPosition(i).toString());
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Unit spinner
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.units_array, R.layout.ingredient_unit_item);

        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitName.setAdapter(unitAdapter);
        unitName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /**
             * Method invoked when a unit in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitName.setSelection(i);
                currentIngredient.setUnit(adapterView.getItemAtPosition(i).toString());
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens
            }
        });

        bbdName.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the view is clicked
             * shows date picker
             * @param view {@link View} the view that contains the selected date
             */
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            /**
             * Method invoked a date is selected
             * sets the selected date as the best before date for this ingredient
             * @param datePicker {@link DatePicker} the date picker in view
             * @param year {@link Integer}  the year selected
             * @param month {@link Integer} the month selected
             * @param dayOfMonth {@link Integer} the day selected
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                bbdName.setText(date);
            }
        };

        // set EditText boxes to the specific fields of the current selected Food
        ingredientName.setText(currentIngredient.getName());
        bbdName.setText(currentIngredient.getbbd());
        locationName.setSelection(locationAdapter.getPosition(currentIngredient.getLocation()));
        unitName.setSelection(unitAdapter.getPosition(currentIngredient.getUnit()));
        amountName.setText(currentIngredient.getAmount().toString());
        categoryName.setSelection(categoryAdapter.getPosition(currentIngredient.getCategory()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    /**
                     * Method for getting and setting attributes of current ingredient
                     * @param dialogInterface {@link DialogInterface} the dialog interface of this fragment
                     * @param i {@link Integer} ID of the selected item
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // retrieve text from the text boxes
                        String ingredientName = AddEditIngredientFragment.this.ingredientName.getText().toString();
                        String bestbefore = AddEditIngredientFragment.this.bbdName.getText().toString();
                        String amount = AddEditIngredientFragment.this.amountName.getText().toString();
                        Double doubleAmount = 0.0;

                        // check if any field is empty
                        // if empty, reject add
                        boolean hasEmpty = ingredientName.isEmpty() || bestbefore.isEmpty() || amount.isEmpty();

                        if (hasEmpty) {
                            Toast.makeText(getContext(),  title + " Rejected: Missing Field(s)",Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            doubleAmount = Double.valueOf(amount);
                        }

                        // set the name of the current food as the edited fields
                        currentIngredient.setName(ingredientName);
                        currentIngredient.setBbd(bestbefore);
                        currentIngredient.setAmount(doubleAmount);

                        listener.onConfirmPressed(currentIngredient, createNewIngredient);
                    }
                }).create();
    }
    /**
     * Method to create a new AddEditRecipe fragment
     * @param ingredient {@link Ingredient} the current ingredient
     * @param createNew {@link boolean} variable that indicates whether to create a new ingredient
     * @return An Add/Edit Ingredient fragment
     */
    static AddEditIngredientFragment newInstance(Ingredient ingredient, boolean createNew) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient",ingredient);
        args.putSerializable("createNew", createNew);

        AddEditIngredientFragment fragment = new AddEditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

