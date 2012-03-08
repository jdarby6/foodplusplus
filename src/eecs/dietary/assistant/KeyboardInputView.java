package eecs.dietary.assistant;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


//This is the List View and its activity for the single text ingredient input
//It will utilize our Ingredient class which talks to the database so that it can scrape off ingredients
//It will be passed through the Intent extra parameters including "allergiesSuffered"
public class KeyboardInputView extends ListActivity {
	
	List<String> _allergiesSuffered;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  _allergiesSuffered = DietaryAssistantActivity._Ingredients.allergiesSuffered;
	  setListAdapter(new myAdapter(this, R.layout.list_item, DietaryAssistantActivity._Ingredients.returnAll()));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);
	 //keyboard show 
	  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	  imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	//
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
	  // TODO Auto-generated method stub
		
	  super.onListItemClick(l, v, position, id);
	  if(!_allergiesSuffered.isEmpty()) { 
	  	AlertDialog ad = new AlertDialog.Builder(this).create();
	  	String ingredient = (String) l.getAdapter().getItem(position);
	  	
//dummy message for now....
	  	String message = ingredient + " is in the following allergies I suffer...\n";
	  	for(int i=0; i<_allergiesSuffered.size(); i++) {
	  		message = message + "(" + (i+1) + ")" + " " + _allergiesSuffered.get(i) + "\n";
	  	}
	  	message = message + _allergiesSuffered.get(0);
	  	ad.setMessage(message);
	  	ad.show();
	  }
	 }
	
	
	private class myAdapter extends ArrayAdapter<String> {
		List<String> list;
		Context mContext;
		
		public myAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
			this.mContext = context;
			list = list2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int[] colors = new int[] { 0x30FF0000, 0x300000FF }; //0 is red, 1 is blue
			
			String ingredient = super.getItem(position);
		
			View view = super.getView(position,convertView,parent);
			
			if(DietaryAssistantActivity._Ingredients.check(ingredient)) {

				view.setBackgroundColor(colors[0]);
				//view.setContentDescription("Violates allergy");
				//view.setVerticalFadingEdgeEnabled(true);
			}
			else {
				view.setBackgroundColor(colors[1]);
			}
			
			return view;
				
			
		}
	}
	
}


