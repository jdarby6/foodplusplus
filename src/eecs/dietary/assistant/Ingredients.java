package eecs.dietary.assistant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;


class Ingredients {
	List<String> all_ingreds;
	List<String> allergiesSuffered;
	List<String> all_allergies;
	DataBaseHelper dbHelper;

	//Constructor that populates "all_allergies" list variable
	Ingredients(Context context) {
		dbHelper = new DataBaseHelper(context, "all_ingreds_db"); //copy local database "all_ingreds_db" to work with this app
		try {
			dbHelper.createDataBase();

		} catch (IOException ioe) {
			throw new Error("Unable to create database");

		}

		try {
			dbHelper.openDataBase();

		} catch(SQLException sqle){	
			throw sqle;

		}

		all_ingreds = new ArrayList<String>();
		allergiesSuffered = new ArrayList<String>();
		all_allergies = new ArrayList<String>();
		all_allergies.add("eggs");
		all_allergies.add("fish");
		all_allergies.add("milk");
		all_allergies.add("peanuts");
		all_allergies.add("shellfish");
		all_allergies.add("soy");
		all_allergies.add("treenuts");
		all_allergies.add("wheat");	
	}

	public List<String> returnAll() {
		all_ingreds.clear();
		Cursor cursor = dbHelper.returnAll();
		int count = cursor.getCount();
		for(int i = 0; i < count; i++) {
			cursor.moveToNext();
			all_ingreds.add(cursor.getString(0));
		}
		return all_ingreds;
	}
	
	public void InsertAllergyAndIngredient(String allergy, String ingredient) {
		
		dbHelper.Insert(allergy, ingredient);
		
	}
	
	
	

	/*public List<String> getAllergies(String ingredient) {
		// TODO Auto-generated method stub
		return all_ingreds;
	}*/

	public List<String> returnByAllergy(String allergy) {
		Cursor cursor = dbHelper.returnIngredientNames(allergy);
		List<String> results = new ArrayList<String>();
		for(int i=0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			results.add(cursor.getString(0));
		}
		return results;
	}
	
	public boolean check(String ingredient) {
		Cursor cursor = dbHelper.returnAllergyNames(ingredient);
		//List<String> results = new ArrayList<String>();
		String result; 
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			result = cursor.getString(0);
			for(int j=0; j< allergiesSuffered.size(); j++) {
				if(result.toLowerCase().equals(allergiesSuffered.get(j).toLowerCase())) {
					return true;
				}		
			}
		}
		return false;
	}
	
	public boolean checkIfIngredientExists(String ingredient) {
		Cursor cursor = dbHelper.checkIfIngredientExists(ingredient.toUpperCase());
		if(cursor.getCount() > 0) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public boolean checkIfAllergyExists(String allergy) {
		Cursor cursor = dbHelper.checkIfAllergyExists(allergy.toUpperCase());
		if(cursor.getCount() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
}