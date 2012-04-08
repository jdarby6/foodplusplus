package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BarcodeFeedback extends ListActivity {

	private String brand;
	private String name;
	private String description;
	private String container;
	private double size;
	private String uom;
	private List<String> ingredients;
	private int potentialAllergens;
	
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode_feedback);
		
		potentialAllergens = 0;

		Intent i = getIntent(); //retrieve previous intent to get extras (for barcode info)

		brand = i.getStringExtra("brand");
		tv = (TextView) findViewById(R.id.brandtext);
		tv.setText("Brand: " + brand);
		
		name = i.getStringExtra("name");
		tv = (TextView) findViewById(R.id.nametext);
		tv.setText("Name: " + name);
		
		description = i.getStringExtra("description");
		tv = (TextView) findViewById(R.id.desctext);
		tv.setText("Description: " + description);
		
		container = i.getStringExtra("container");
		tv = (TextView) findViewById(R.id.containertext);
		tv.setText("Container: " + container);
		
		size = i.getDoubleExtra("size", 0.0);
		uom = i.getStringExtra("uom");
		tv = (TextView) findViewById(R.id.sizetext);
		tv.setText("Size: " + String.valueOf(size) + " " + uom);
		
		
		//Separates ingredients by commas (also removes the period that sometimes appears at the end of the list
		ingredients = new ArrayList<String>(Arrays.asList(i.getStringExtra("ingredients").toUpperCase().split("[.,] ?")));

		setListAdapter(new myFeedbackAdapter(this, R.layout.list_item, ingredients));
		
		tv = (TextView) findViewById(R.id.allergencounttext);
		tv.setText("Found " + potentialAllergens + " potential allergens (in red):");

	}

	@Override
	public void onPause() {
		super.onPause();
//		super.finish();
		//finish();
	}

	private class myFeedbackAdapter extends ArrayAdapter<String> {
		List<String> list;
		Context mContext;

		public myFeedbackAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
			this.mContext = context;
			list = list2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int[] colors = new int[] { 0x30FF0000, 0xFF00FF00, 0xFF888888 }; //0 is red, 1 is green, 2 is gray

			String ingredient = super.getItem(position);
			String allergen = null;
			boolean found = false;

			View view = super.getView(position,convertView,parent);

			ingredient = ingredient.replaceAll("[^a-zA-Z0-9]+", " ");
			
			Cursor all_bad_ingreds = DietaryAssistantActivity._Ingredients.dbHelper.returnAllAllergens(DietaryAssistantActivity._Ingredients.allergiesSuffered);
			if(all_bad_ingreds != null) {
				for(int i = 0; i < all_bad_ingreds.getCount(); i++) {
			
					all_bad_ingreds.moveToNext();
					
					allergen = all_bad_ingreds.getString(0);
					allergen = allergen.replaceAll("[^a-zA-Z0-9]+", " ");
					
					if(ingredient.contentEquals(allergen)) {
						view.setBackgroundColor(colors[0]);
						view.setContentDescription("Violates allergy");
						view.setVerticalFadingEdgeEnabled(true);
						
						found = true;
						potentialAllergens++;
						break;
					}
				}
			}
//					else if(DietaryAssistantActivity._Ingredients.
//							.contains(ingredient)) {
//						view.setBackgroundColor(colors[1]);
//					}
			if(found == false) {
				view.setBackgroundColor(colors[2]);
			}

			return view;


		}
	}

}
