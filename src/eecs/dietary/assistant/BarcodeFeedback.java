package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
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
	private static int potentialAllergens;

	private TextView tv;

	
	@Override
	public void onBackPressed() {
	//	setResult(RETURN_FROM_FEEDBACK);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    //	setResult(RETURN_FROM_FEEDBACK);
			finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode_feedback);

		Typeface tf = Typeface.createFromAsset(
		        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
		
		potentialAllergens = 0;
		//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent i = getIntent(); //retrieve previous intent to get extras (for barcode info)

		tv = (TextView) findViewById(R.id.textView1);
		tv.setTypeface(tf);
		
		brand = i.getStringExtra("brand");
		tv = (TextView) findViewById(R.id.brandtext);
		tv.setTypeface(tf);
		tv.setText("Brand: " + brand);

		name = i.getStringExtra("name");
		tv = (TextView) findViewById(R.id.nametext);
		tv.setTypeface(tf);
		tv.setText("Name: " + name);

		description = i.getStringExtra("description");
		tv = (TextView) findViewById(R.id.desctext);
		tv.setTypeface(tf);
		tv.setText("Description: " + description);

		container = i.getStringExtra("container");
		tv = (TextView) findViewById(R.id.containertext);
		tv.setTypeface(tf);
		tv.setText("Container: " + container);

		size = i.getDoubleExtra("size", 0.0);
		uom = i.getStringExtra("uom");
		tv = (TextView) findViewById(R.id.sizetext);
		tv.setTypeface(tf);
		tv.setText("Size: " + String.valueOf(size) + " " + uom);


		//Separates ingredients by commas (also removes the period that sometimes appears at the end of the list
		ingredients = new ArrayList<String>(Arrays.asList(i.getStringExtra("ingredients").toUpperCase().split("[.,] ?")));

		setListAdapter(new myFeedbackAdapter(this, R.layout.list_item, ingredients));

		tv = (TextView) findViewById(R.id.allergencounttext);
		tv.setTypeface(tf);
		tv.setText("Potential allergens in red:");
//		tv.setText("Found " + potentialAllergens + " potential allergens (in red):");

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
			int[] colors = new int[] { 0x30FF0000, 0xFF00FF00, 0xFF888888, 0x07000000 }; //0 is red, 1 is green, 2 is gray, 3 is transparent

			String ingredient = super.getItem(position);
			String allergen = null;
			boolean found = false;

			View view = super.getView(position,convertView,parent);
			Typeface tf = Typeface.createFromAsset(
			        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
			((TextView)view).setTypeface(tf);
			
			ingredient = ingredient.replaceAll("[^a-zA-Z0-9]+", " ");

			Cursor all_bad_ingreds = DietaryAssistantActivity._Ingredients.dbHelper.returnAllAllergens(DietaryAssistantActivity._Ingredients.allergiesSuffered);
			if(all_bad_ingreds != null) {
				for(int i = 0; i < all_bad_ingreds.getCount(); i++) {

					all_bad_ingreds.moveToNext();

					allergen = all_bad_ingreds.getString(0);
					allergen = allergen.replaceAll("[^a-zA-Z0-9]+", " ");

					if(ingredient.contains(allergen) || allergen.contains(ingredient))
                    {
						view.setBackgroundColor(colors[0]);
						view.setContentDescription("Violates allergy");
						view.setVerticalFadingEdgeEnabled(true);

						found = true;
//						potentialAllergens++;
//						TextView tv = (TextView) findViewById(R.id.allergencounttext);
//						tv.setText("Found " + potentialAllergens + " potential allergens (in red):");
						break;
					}
				}
			}
			//					else if(DietaryAssistantActivity._Ingredients.
			//							.contains(ingredient)) {
			//						view.setBackgroundColor(colors[1]);
			//					}
			if(found == false) {
				view.setBackgroundColor(colors[3]);
			}

			return view;


		}
	}

}
