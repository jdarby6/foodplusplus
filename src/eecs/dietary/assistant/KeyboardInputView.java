package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


//This is the List View and its activity for the single text ingredient input
//It will utilize our Ingredient class which talks to the database so that it can scrape off ingredients
//It will be passed through the Intent extra parameters including "allergiesSuffered"
public class KeyboardInputView extends ListActivity {

	//List<String> _allergiesSuffered;

	private GridView gv;
	private Button key;
	private Dialog d;
	private Toast toast;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ingredientslist);
		
		//d = new Dialog(this,android.R.style.Theme_Dialog);
		
		//_allergiesSuffered = DietaryAssistantActivity._Ingredients.allergiesSuffered;
		setListAdapter(new IngredientListAdapter(this, R.layout.list_item, DietaryAssistantActivity._Ingredients.returnAll()));

		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		key = (Button) findViewById(R.id.buttonKeyIngredients);
		key.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);	
				
			}
			
			
			
		});
		
		
		
		
	}
	
	@Override 
	public void onPause() {
		super.onPause();
		super.finish();
		
	}
	


	protected void onListItemClick(ListView l, View v, int position, long id) {
		String ingredient = (String) l.getAdapter().getItem(position);
		List<String> allergs =  DietaryAssistantActivity._Ingredients.ReturnAllAllergiesUnderIngredient(ingredient);
//		List<String> allergs2 = new ArrayList<String>();
//		allergs2.add(Integer.toString(allergs.size()));
		//allergs2.add("ewqrqwerqwre");
		//allergs2.add("348284");
		//allergs2.add("18182183");
		d = new Dialog(this,android.R.style.Theme_Dialog);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.ingredient_card);
		
		
		Button close = (Button) d.findViewById(R.id.closeCard);
		close.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(toast != null) {
					toast.cancel();
				}
				d.dismiss();
			}
			
			
		});
		
		TextView tt = (TextView) d.findViewById(R.id.toptextingred);
		tt.setText(ingredient);
		//tt.setTextSize(30);
		
		gv = (GridView) d.findViewById(R.id.myGrid);		
		gv.setAdapter(new ImageAdapterIngredientCard(this, allergs));
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(toast != null) {
					toast.cancel();
				}
				String allergy = (String) gv.getAdapter().getItem(arg2);
				View entry = gv.getChildAt(arg2);
				View entry2 = entry.findViewById(R.id.grid_item_image);
				int[] xy = new int[2];
				CharSequence cs = allergy;
				entry2.getLocationOnScreen(xy);

				toast = Toast.makeText(gv.getContext(),cs,Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP|Gravity.CENTER, xy[0], xy[1]);
				toast.show();
				
				
			}
		});
	/*	gv.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				// TODO Auto-generated method stub
				int[] xy = new int[2]; 
		        view.getLocationOnScreen(xy);
		        toast = Toast.makeText(gv.getContext(),"hehehhe",Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP|Gravity.CENTER, xy[0], xy[1]);
				toast.show();
				
			}
			
			
			
			
		});*/
	
		TextView bt = (TextView) d.findViewById(R.id.bottomtextingred);		//NEED TO DO --
		bt.setText("hehehehqhwerhqwerhqwreqhwerhqhwerhjqwerqrewr");         //WILL BE ADDITIONAL INFO ABOUT EACH INGREDIENT
		
		
		d.show();
		
		/*
		int first_pos = l.getFirstVisiblePosition(); //used to fix toast position 
		if(!DietaryAssistantActivity._Ingredients.allergiesSuffered.isEmpty()) { 
			
			View entry = l.getChildAt(position-first_pos);
			int[] xy = new int[2];
			entry.getLocationOnScreen(xy);
			
			CharSequence toasttext = "Conflicts with...\n";
			
			String ingredient = (String) l.getAdapter().getItem(position);
			

			Cursor cursor = DietaryAssistantActivity._Ingredients.dbHelper.returnAllergyNames(ingredient);
			for(int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				if(DietaryAssistantActivity._Ingredients.allergiesSuffered.contains(cursor.getString(0).toLowerCase())) {
					toasttext = toasttext + cursor.getString(0) + "\n";
					
				}
			}
			if(toasttext.length()>"Conflicts with...\n".length()+1) {
				toasttext = toasttext.subSequence(0, toasttext.length()-1); //chop off last newline
				Toast toast = Toast.makeText(this, toasttext, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, xy[1]);
				toast.show();
			}
			
		}*/
	}
	
	
    public class ImageAdapterIngredientCard extends BaseAdapter {
	      private Context _context;
	      private final List<String> _allergies;
	      
	      
	      
	      public ImageAdapterIngredientCard(Context _MyContext, List<String> allergies)
	      {
	         _context = _MyContext;
	         _allergies = allergies;
	      }
	      
	      public View getView(int position, View convertView, ViewGroup parent) 
	      {
	    	  LayoutInflater inflater = (LayoutInflater) _context
	    				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	 
	    			View gridView;
	    			String allergy = (String) getItem(position);
	    			if (convertView == null) {
	    	 
	    				gridView = new View(_context);
	    				gridView = inflater.inflate(R.layout.ingredienticon, null);
	    	 
	    				// set image based on selected text
	    				ImageView imageView = (ImageView) gridView
	    						.findViewById(R.id.icon); //NEEDS TO BE FIXED BASED ON INGREDIENT
	     			} else {
	    				gridView = (View) convertView;
	    			}
	    			return gridView;
	      }

	      public Object getItem(int arg0) {
	    	  return _allergies.get(arg0);
	      }

	      public long getItemId(int arg0) {
	         // TODO Auto-generated method stub
	         return 0;
	      }

		public int getCount() {
			return _allergies.size();
		}
		
	   }
	

	private class IngredientListAdapter extends ArrayAdapter<String> {
		//		List<String> list;
		//		Context mContext;

		public IngredientListAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
			//			this.mContext = context;
			//			list = list2;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int[] colors = new int[] { 0x30FF0000, 0x300000FF, 0x000000 }; //0 is red, 1 is blue

			String ingredient = super.getItem(position);

			View view = super.getView(position,convertView,parent);
			((TextView)view).setTextColor(Color.GREEN);
			view.setBackgroundColor(Color.WHITE);
			if(DietaryAssistantActivity._Ingredients.check(ingredient)) {

				view.setBackgroundColor(colors[0]);
			}
			else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}

			return view;
		}
	}
}

