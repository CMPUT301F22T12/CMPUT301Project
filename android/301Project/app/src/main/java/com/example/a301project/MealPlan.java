package com.example.a301project;

import java.util.ArrayList;

/**
 * Represents an individual meal plan with ingredients and recipes.
 * MealPlanFragment contains multiple instances of these.
 */
public class MealPlan {
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;
    private String startDate;
    private String endDate;
    private String name;

    /**
     * Gets the {@link MealPlan} object's name
     * @return The name of the object
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the {@link MealPlan} object's name
     * @param name The {@link MealPlan} object's name is set to this value
     */
    public void setName(String name) {
        this.name = name;
    }
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    private String getStartDate() {return startDate;}
    private String getEndDate() {return endDate;}


    /**
     * Constructor for an MealPlan's attributes containing ingredients and recipes
     * @param ingredients {@link Ingredient}
     * @param recipes {@link Recipe}
     * @param name {@link String}
     */
    public MealPlan(ArrayList<Ingredient> ingredients, ArrayList<Recipe> recipes, String name, String startDate, String endDate) {
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructor for MealPlan that only takes a name
     * creates ArrayLists as new
     * @param name {@String}
     */
//    public MealPlan(String name) {
//        this(new ArrayList<>(), new ArrayList<>(), name);
//    }

}
