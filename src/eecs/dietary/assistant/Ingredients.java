package eecs.dietary.assistant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.ImageView;


class Ingredients {
	ArrayList<String> all_ingreds;
	ArrayList<String> allergiesSuffered;
	ArrayList<String> all_allergies;
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
		Cursor cursor = dbHelper.returnAllAllergies();
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			all_allergies.add(cursor.getString(0).toLowerCase());
		}
//		all_allergies.add("eggs");
//		all_allergies.add("fish");
//		all_allergies.add("milk");
//		all_allergies.add("peanuts");
//		all_allergies.add("shellfish");
//		all_allergies.add("soy");
//		all_allergies.add("treenuts");
//		all_allergies.add("wheat");	
	}

	public ArrayList<String> returnAll() {
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
	
	public ArrayList<String> ReturnAllAllergiesUnderIngredient(String ingredient) {
		int y = 5;
		ArrayList<String> allergs = new ArrayList<String>();
		Cursor cursor = DietaryAssistantActivity._Ingredients.dbHelper.returnAllergyNames(ingredient);
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			allergs.add(cursor.getString(0));
		}
		return allergs;
	}
	
	
	

	/*public List<String> getAllergies(String ingredient) {
		// TODO Auto-generated method stub
		return all_ingreds;
	}*/

	public ArrayList<String> returnByAllergy(String allergy) {
		Cursor cursor = dbHelper.returnIngredientNames(allergy);
		if(cursor.getCount() == 0) { 
			Log.d("No results", "allergy '" + allergy + "' has no ingredients or does not exist");
		}
		ArrayList<String> results = new ArrayList<String>();
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

	public int GetIconIndex(String allergy) {
		// TODO Auto-generated method stub

		//Cursor c = dbHelper.findAllergyIcon(allergy);
		//return c.get(0) 
		
		
		if(allergy.equalsIgnoreCase("milk")) {
			return 46;
		}
		else if(allergy.equalsIgnoreCase("soy")) {
			return 20;
		}
		else if(allergy.equalsIgnoreCase("wheat")) {
			return 29;
		}
		else if(allergy.equalsIgnoreCase("peanuts")) {
			return 34;
		}
		else if(allergy.equalsIgnoreCase("shellfish")) {
			return 50;
		}
		else {
			int length = allergy.length() + 10;
			return length;
		}

	
	}

	public void InsertAllergyIcon(String allergy, int iconindex) {
		dbHelper.InsertAllergyIcon(allergy, iconindex);
	}
	
	public void RemoveAllergyIcon(String allergy) {
		dbHelper.RemoveAllergyIcon(allergy);
	}
	
	
	public void setImageIcon(ImageView iv, int allergyIconIndex) {
		// TODO Auto-generated method stub
		
		if(iv != null) {
	    	  switch(allergyIconIndex) {
		    	  case 46:		    		 
		    		  iv.setImageResource(R.drawable.untitled46);
		    		  break;
		    	  case 20:
		    		  iv.setImageResource(R.drawable.untitled20);
		    		  break;
		    	  case 29:
		    		  iv.setImageResource(R.drawable.untitled29);
		    		  break;
		    	  case 34:
		    		  iv.setImageResource(R.drawable.untitled34);
		    		  break;
		    	  case 50:
		    		  iv.setImageResource(R.drawable.untitled50);
		    		  break;
		    	  case 4:
		    		  iv.setImageResource(R.drawable.untitled14);
		    		  break;
		    	  case 3:
		    		  iv.setImageResource(R.drawable.untitled15);
		    		  break;
		    	  case 2:
		    		  iv.setImageResource(R.drawable.untitled16);
		    		  break;
		    	  case 7:
		    		  iv.setImageResource(R.drawable.untitled17);
		    		  break;
		    	  case 6:
		    		  iv.setImageResource(R.drawable.untitled18);
		    		  break;
		    	  case 8:
		    		  iv.setImageResource(R.drawable.untitled19);
		    		  break;
		    	  case 5:
		    		  iv.setImageResource(R.drawable.untitled20);
		    		  break;
		    	  case 9:
		    		  iv.setImageResource(R.drawable.untitled21);
		    		  break;
		    	  case 13:
		    		  iv.setImageResource(R.drawable.untitled22);
		    		  break;
		    	  case 12:
		    		  iv.setImageResource(R.drawable.untitled23);
		    		  break;
		    	  case 11:
		    		  iv.setImageResource(R.drawable.untitled24);
		    		  break;
		    	  case 10:
		    		  iv.setImageResource(R.drawable.untitled25);
		    		  break;
		    	  case 14:
		    		  iv.setImageResource(R.drawable.untitled26);
		    		  break;
		    	  case 15:
		    		  iv.setImageResource(R.drawable.untitled27);
		    		  break;
		    	  case 16:
		    		  iv.setImageResource(R.drawable.untitled28);
		    		  break;
		    	  case 17:
		    		  iv.setImageResource(R.drawable.untitled29);
		    		  break;
		    	  case 18:
		    		  iv.setImageResource(R.drawable.untitled30);
		    		  break;
		    	  case 19:
		    		  iv.setImageResource(R.drawable.untitled31);
		    		  break;
		    	  case 21:
		    		  iv.setImageResource(R.drawable.untitled33);
		    		  break;
		    	  case 22:
		    		  iv.setImageResource(R.drawable.untitled34);
		    		  break;
		    	  case 23:
		    		  iv.setImageResource(R.drawable.untitled35);
		    		  break;
		    	  case 24:
		    		  iv.setImageResource(R.drawable.untitled36);
		    		  break;
		    	  case 25:
		    		  iv.setImageResource(R.drawable.untitled37);
		    		  break;
		    	  case 26:
		    		  iv.setImageResource(R.drawable.untitled38);
		    		  break;
		    	  default:
		    		  iv.setImageResource(R.drawable.untitled40);
		    		  break;
	    	  }
		}  
		
	}

	
	
}