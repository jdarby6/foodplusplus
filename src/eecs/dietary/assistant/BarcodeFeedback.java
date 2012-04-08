package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

public class BarcodeFeedback extends ListActivity {

	public String brand;
	public String name;
	public String description;
	public String container;
	public double size;
	public String uom;
	public List<String> ingredients;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent i = getIntent(); //retrieve previous intent to get extras (for barcode info)

		brand = i.getStringExtra("brand");
		name = i.getStringExtra("name");
		description = i.getStringExtra("description");
		container = i.getStringExtra("container");
		size = i.getDoubleExtra("size", 0.0);
		uom = i.getStringExtra("uom");
		ingredients = new ArrayList<String>(Arrays.asList(i.getStringExtra("ingredients").split(", ")));

		setListAdapter(new myFeedbackAdapter(this, R.layout.list_item, ingredients));

	}

	@Override
	public void onPause() {
		super.onPause();
		super.finish();
		//finish();
	}

	private class myFeedbackAdapter extends ArrayAdapter<String> {
		//		List<String> list;
		//		Context mContext;

		public myFeedbackAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
			//			this.mContext = context;
			//			list = list2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int[] colors = new int[] { 0x30FF0000, 0xFF00FF00, 0xFF888888 }; //0 is red, 1 is green, 2 is gray

			//			String ingredient = super.getItem(position);

			View view = super.getView(position,convertView,parent);

			//ingredient = ingredient.replaceAll("[^a-zA-Z0-9]+", " ");

//			if(position < badingreds.size()) {
//				view.setBackgroundColor(colors[0]);
//				//view.setContentDescription("Violates allergy");
//				//view.setVerticalFadingEdgeEnabled(true);
//			}
//			//		else if(DietaryAssistantActivity._Ingredients.contains(ingredient)) {
//			//			view.setBackgroundColor(colors[1]);
//			//		}
//			else {
				view.setBackgroundColor(colors[2]);
//			}

			return view;


		}
	}

}
