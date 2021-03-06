package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import eecs.dietary.assistant.KeyboardInputView.ImageAdapterIngredientCard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OCRFeedback extends ListActivity {
	
	public static int RETURN_FROM_FEEDBACK = 938123;
	
	List<String> ingredsFound = new ArrayList<String>(); //stores the ingredients in order
	List<String> badingreds = new ArrayList<String>();
	List<String> okingreds = new ArrayList<String>();
	List<String> unknowningreds = new ArrayList<String>();
	List<Integer> confidences=  new ArrayList<Integer>();
	private Dialog d;
	private Toast toast;
	private GridView gv;
	public static int CALL_BARCODE = 93867123;
	public static int CALL_CAMERA_AGAIN = 38103;
	

	@Override
	public void onBackPressed() {
		setResult(RETURN_FROM_FEEDBACK);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	setResult(RETURN_FROM_FEEDBACK);
			finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String ingredient = (String) l.getAdapter().getItem(position);
		List<String> allergs =  DietaryAssistantActivity._Ingredients.ReturnAllAllergiesUnderIngredient(ingredient);

		d = new Dialog(this,android.R.style.Theme_Dialog);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.ingredient_card);
			
		ImageButton close = (ImageButton) d.findViewById(R.id.closeCard);
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
		//		toast.show();
				
				
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
		bt.setText(DietaryAssistantActivity._Ingredients.getAdditionalIngredientInfo(ingredient));         //WILL BE ADDITIONAL INFO ABOUT EACH INGREDIENT
	//	bt.setTypeface(tf);
		
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
	    	 
	    			View gridItem;
	    			String allergy = (String) getItem(position);
	    			if (convertView == null) {
	    	 
	    				gridItem = new View(_context);
	    				gridItem = inflater.inflate(R.layout.ingredienticon, null);
	    				int iconIndex = DietaryAssistantActivity._Icons.GetIconIndex(allergy);
	    	 
	    				ImageView imageView = (ImageView) gridItem
	    						.findViewById(R.id.grid_item_image);
	    				
	    				DietaryAssistantActivity._Icons.setImageIcon(imageView, iconIndex);
	    				
	    				
	     			} else {
	    				gridItem = (View) convertView;
	    			}
	    			return gridItem;
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
				List<String> list;
				Context mContext;

		public IngredientListAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
						this.mContext = context;
						this.list = list2;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int[] colors = new int[] { 0x30FF0000, 0x300000FF, 0x000000 }; //0 is red, 1 is blue

			String ingredient = list.get(position);

			
			View view = convertView;
			
			if(view == null) {
				LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = li.inflate(R.layout.ocrfeedbackitem, null);			
			}
			
			
			TextView ingred = (TextView) view.findViewById(R.id.ingredientname);
			ingred.setText(ingredient);
			TextView conf = (TextView) view.findViewById(R.id.confidencelevel);
			conf.setText(Integer.toString(confidences.get(position))+"%");
			Typeface tf = Typeface.createFromAsset(
			        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
			conf.setTypeface(tf);
			ingred.setTypeface(tf);
			
			if(DietaryAssistantActivity._Ingredients.check(ingredient)) {

				view.setBackgroundColor(colors[0]);
			}
			else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}

			return view;
		}
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  setContentView(R.layout.ocrfeedback);

	  Typeface tf = Typeface.createFromAsset(
		        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
	  TextView toptv = (TextView) findViewById(R.id.topocrfeedbacktext);
	  toptv.setTypeface(tf);
	  
	 // String ocrtext = DietaryAssistantActivity._OCR.readText;

	  fillIngredsFound(DietaryAssistantActivity._OCRReader.ingreds);
	  if(ingredsFound.size()==0) {
		  Dialog d;
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		  builder.setMessage("The picture was kinda fuzzy and I couldn't find any ingredients :(");
		  builder.setPositiveButton("Redo", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setResult(CALL_CAMERA_AGAIN);
				finish();
			}
		});
		  builder.setNegativeButton("Barcode", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setResult(CALL_BARCODE);
				finish();
			}
		});
		  
		builder.show();
		  
		  
	  }
	  
	  setListAdapter(new IngredientListAdapter(this, R.layout.ocrfeedbackitem, ingredsFound));

	  
	}
	
	@Override
	public void onPause() {
		super.onPause();
		super.finish();
		//finish();
	}
	
	private void fillIngredsFound(List<String> ingreds) {
		badingreds.clear();
		unknowningreds.clear();
		for(int a=0; a<ingreds.size(); a++) {
			if(DietaryAssistantActivity._Ingredients.check(ingreds.get(a))) {
				badingreds.add(ingreds.get(a));
			}
			else {
				unknowningreds.add(ingreds.get(a));
			}
			confidences.add(DietaryAssistantActivity._OCRReader.confidences.get(a));
		}
		ingredsFound.clear();
		ingredsFound.addAll(badingreds);
		ingredsFound.addAll(unknowningreds);
	}
	
	/*
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
			
			if(position < badingreds.size()) {
				view.setBackgroundColor(colors[0]);
			}
			else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}
			
			return view;
				
			
		}
	}*/

}
