package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class OCRFeedback extends ListActivity {
	
	List<String> ingredsFound = new ArrayList<String>(); //stores the ingredients in order
	List<String> badingreds = new ArrayList<String>();
	List<String> okingreds = new ArrayList<String>();
	List<String> unknowningreds = new ArrayList<String>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  AlertDialog ad = new AlertDialog.Builder(this).create();
	  String ocrtext = DietaryAssistantActivity._OCR.readText;
	  int pos = DietaryAssistantActivity._OCRReader.FindFirstPositionOf("Ingredients:", ocrtext);
	  ad.setMessage(ocrtext.subSequence(pos, pos+"Ingredients".length()));
	  ad.show();
	  
	  fillIngredsFound(DietaryAssistantActivity._OCRReader.FindBadIngredients(ocrtext),true);
	  fillIngredsFound(DietaryAssistantActivity._OCRReader.RetrieveIngredients(ocrtext),false);
	  
	//  ingredsFound = DietaryAssistantActivity._OCRReader.IngredientsFound;
	  setListAdapter(new myFeedbackAdapter(this, R.layout.list_item, ingredsFound));

	}
	
	@Override
	public void onPause() {
		super.onPause();
		super.finish();
		//finish();
	}
	
	private void fillIngredsFound(List<String> ingreds, boolean bad) {
	
		for(int a=0; a<ingreds.size(); a++) {
			if(bad) {
				badingreds.add(ingreds.get(a));
			}
		//	else if(DietaryAssistantActivity._Ingredients.contains(ingreds.get(a))) {
		//		okingreds.add(ingreds.get(a));
		//	}
			else {
				unknowningreds.add(ingreds.get(a));
			}
		}
		if(bad) {
			ingredsFound.addAll(badingreds);
		}
		else {
		//ingredsFound.addAll(okingreds);
			ingredsFound.addAll(unknowningreds);
		}
		
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
			
			//ingredient = ingredient.replaceAll("[^a-zA-Z0-9]+", " ");
			
			if(position < badingreds.size()) {
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
