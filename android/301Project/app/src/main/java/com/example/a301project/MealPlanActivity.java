package com.example.a301project;

import android.os.Bundle;
import android.view.ViewGroup;

public class MealPlanActivity extends NavActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_meal_plan, content, true);

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_meal_plan).setChecked(true);
    }
}