package eecs.dietary.assistant;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


//This is the List View and its activity for the single text ingredient input
//It will utilize our Ingredient class which talks to the database so that it can scrape off ingredients
//It will be passed through the Intent extra parameters including "allergiesSuffered"
public class KeyboardInputView extends ListActivity {

	//List<String> _allergiesSuffered;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//_allergiesSuffered = DietaryAssistantActivity._Ingredients.allergiesSuffered;
		setListAdapter(new myAdapter(this, R.layout.list_item, DietaryAssistantActivity._Ingredients.returnAll()));

		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		
		
		
		
	}
	


	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		int first_pos = l.getFirstVisiblePosition(); //used to fix toast position 
		
		if(!DietaryAssistantActivity._Ingredients.allergiesSuffered.isEmpty()) { 
			
			View entry = l.getChildAt(position-first_pos);
			int[] xy = new int[2];
			entry.getLocationOnScreen(xy);
			
			CharSequence toasttext = "";
			
			String ingredient = (String) l.getAdapter().getItem(position);

			Cursor cursor = DietaryAssistantActivity._Ingredients.dbHelper.returnAllergyNames(ingredient);
			for(int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				if(DietaryAssistantActivity._Ingredients.allergiesSuffered.contains(cursor.getString(0))) {
					toasttext = toasttext + cursor.getString(0) + "\n";
					
				}
			}
			if(toasttext.length()>2) {
				Toast toast = Toast.makeText(this, toasttext, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, xy[1]);
				toast.show();
			}
			
		}
	}


	private class myAdapter extends ArrayAdapter<String> {
		//		List<String> list;
		//		Context mContext;

		public myAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
			//			this.mContext = context;
			//			list = list2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int[] colors = new int[] { 0x30FF0000, 0x300000FF }; //0 is red, 1 is blue

			String ingredient = super.getItem(position);

			View view = super.getView(position,convertView,parent);

			if(DietaryAssistantActivity._Ingredients.check(ingredient)) {

				view.setBackgroundColor(colors[0]);
			}
			else {
				view.setBackgroundColor(colors[1]);
			}

			return view;
		}
	}
}


