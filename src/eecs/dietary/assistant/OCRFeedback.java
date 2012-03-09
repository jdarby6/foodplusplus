package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OCRFeedback extends ListActivity {
	
	List<String> ingredsFound = new ArrayList<String>(); //stores the ingredients in order
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  fillIngredsFound(DietaryAssistantActivity._OCRReader.IngredientsFound);
	  
	//  ingredsFound = DietaryAssistantActivity._OCRReader.IngredientsFound;
	  setListAdapter(new myFeedbackAdapter(this, R.layout.list_item, ingredsFound));

	}
	
	private void fillIngredsFound(List<String> ingreds) {
		List<String> badingreds = new ArrayList<String>();
		List<String> okingreds = new ArrayList<String>();
		List<String> unknowningreds = new ArrayList<String>();
		for(int a=0; a<ingreds.size(); a++) {
			if(DietaryAssistantActivity._Ingredients.check(ingreds.get(a))) {
				badingreds.add(ingreds.get(a));
			}
		//	else if(DietaryAssistantActivity._Ingredients.contains(ingreds.get(a))) {
		//		okingreds.add(ingreds.get(a));
		//	}
			else {
				unknowningreds.add(ingreds.get(a));
			}
		}
		ingredsFound.addAll(badingreds);
		ingredsFound.addAll(okingreds);
		ingredsFound.addAll(unknowningreds);
		
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
			
			String ingredient = super.getItem(position);
		
			View view = super.getView(position,convertView,parent);
			
			if(DietaryAssistantActivity._Ingredients.check(ingredient)) {
				view.setBackgroundColor(colors[0]);
				//view.setContentDescription("Violates allergy");
				//view.setVerticalFadingEdgeEnabled(true);
			}
	//		else if(DietaryAssistantActivity._Ingredients.contains(ingredient)) {
	//			view.setBackgroundColor(colors[1]);
	//		}
			else {
				view.setBackgroundColor(colors[2]);
			}
			
			return view;
				
			
		}
	}

}