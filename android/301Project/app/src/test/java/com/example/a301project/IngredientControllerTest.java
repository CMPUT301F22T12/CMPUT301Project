package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

public class IngredientControllerTest {
    private IngredientController controller;
    private FirebaseFirestore mockFirestore;
    private CollectionReference mockCollectionRef;

    private Ingredient mockIngredient() {
        return new Ingredient("carrot",3,"2022-11-30","pantry","lbs","vegetable");
    }

    @Before
    public void setUp() {
        // Add our mock classes
        mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        this.controller = new IngredientController(mockFirestore);
    }

    @Test
    public void testAddIngredient() {
        // Create a mock ingredient
        Ingredient i = mockIngredient();

        // Call our method
        controller.addIngredient(i);

        // Capture the data value
        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef)
                .add(dataCaptor.capture());
        Map<String, Object> data = dataCaptor.getValue();

        // Make sure the correct data was passed
        assertEquals(data.get("Amount"), i.getAmount());
        assertEquals(data.get("BestBeforeDate"), IngredientController.convertStringToTimestamp(i.getbbd()));
        assertEquals(data.get("Category"), i.getCategory());
        assertEquals(data.get("Location"), i.getLocation());
        assertEquals(data.get("Name"), i.getName());
        assertEquals(data.get("Unit"), i.getUnit());
    }
}
