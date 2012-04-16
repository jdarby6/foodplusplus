package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class CreateAllergyActivity3 extends ListActivity {
	static final int ADD_INGREDIENT = 0;
	static final int INGREDIENT_EXISTS = 1;
	static final int BLANK_FIELD = 2;
	static final int OK = 3;

	public static int CALL_REVIEW_SCREEN = 784781;
	public static int BACKWARD_BUTTON = 9281;

	private List<String> _filteredModelItemsArray;


	//private Button _addIngred;
	private ImageButton _backward;
	private ImageButton _forward;
	private ImageButton _keyboard; 
	private EditText _filterbox;

	private static final int FILTER = Menu.FIRST;
	private static final int CHECK_ALL = Menu.FIRST+1;
	private static final int UNCHECK_ALL = Menu.FIRST+2;	

	private HashSet<String> _clickeditems;  //keeps track of what ingredients are clicked/checked
	private ListView _listview;
	private myAdapter _myAdapter;
	private List<String> _bindinglist;
	private List<String> _ingredients;
	private List<String> _checkedallergies;
	//private List<String> _addedIngreds;
	private String allergyname; 
	private int allergyIconIndex;
	private Dialog d;
	private Toast toast;
	private GridView gv;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.createallergyscreen3);
		
		//setting up the edit box on the top of screen
		/*_filterbox = (EditText) findViewById(R.id.createallergy3searchbox);
		_filterbox.addTextChangedListener(filterTextWatcher);
		_filterbox.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);*/

		//setting up button to add new ingredient to list
		//_addIngred = (Button) this.findViewById(R.id.buttonAddIngredient);
		/*_addIngred.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				CreateAllergyActivity3.this.showDialog(ADD_INGREDIENT);
			}
		});*/

		Typeface tf = Typeface.createFromAsset(
		        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
		TextView catv = (TextView) this.findViewById(R.id.ca3tv);
		catv.setTypeface(tf);
		
		//setting up buttons
		_backward = (ImageButton) this.findViewById(R.id.buttonBackAllergyCreate3);
		_forward = (ImageButton) this.findViewById(R.id.buttonForwardAllergyCreate3);
		//_keyboard = (ImageButton) this.findViewById(R.id.buttonKeyAllergyCreate3);
		_backward.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				setResult(BACKWARD_BUTTON);
				finish();
			}
		});		
		_forward.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(CreateAllergyActivity3.this, CreateAllergyActivityReviewAllergy.class);
				i.putExtra("allergyname", allergyname);
				ArrayList<String> ingredients = new ArrayList<String>();
				ingredients.addAll(_clickeditems);
				i.putStringArrayListExtra("ingredients", ingredients);
				i.putExtra("iconIndex",allergyIconIndex);
				startActivityForResult(i,CALL_REVIEW_SCREEN);
			}
		});	
		/*_keyboard.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);	
			}
		});*/	

		_ingredients = new ArrayList<String>();
		_ingredients.addAll(DietaryAssistantActivity._Ingredients.returnAll());

		_bindinglist = new ArrayList<String>();
		_bindinglist.addAll(DietaryAssistantActivity._Ingredients.returnAll());		

		Intent sender = getIntent();
		_checkedallergies = new ArrayList<String>();
		_checkedallergies = sender.getStringArrayListExtra("checkedallergies");
		allergyname = sender.getStringExtra("allergyname");
		allergyIconIndex = sender.getExtras().getInt("iconIndex");
		
		//_addedIngreds = new ArrayList<String>();

		_clickeditems = new HashSet<String>();
		for(int i = 0; i < _checkedallergies.size(); i++) {
			_clickeditems.addAll(DietaryAssistantActivity._Ingredients.returnByAllergy(_checkedallergies.get(i)));
		}

		_filteredModelItemsArray = new ArrayList<String>();

		_myAdapter = new myAdapter(this, R.layout.list_item, _bindinglist);
		setListAdapter(_myAdapter);


		_listview = getListView();
		_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		_listview.setClickable(true);
		_listview.setTextFilterEnabled(true);

		_listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 
				String ingredient = (String) _listview.getItemAtPosition(arg2);
				
				if(_clickeditems.contains(ingredient)) {
					CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkbox);
					cb.setChecked(false);
					_clickeditems.remove(ingredient);
				}
				else {
					CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkbox);
					cb.setChecked(true);
					_clickeditems.add(ingredient);
				}			
			}
		});
		_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String ingredient = (String) _listview.getItemAtPosition(arg2);
				List<String> allergs =  DietaryAssistantActivity._Ingredients.ReturnAllAllergiesUnderIngredient(ingredient);

				d = new Dialog(arg1.getContext(),android.R.style.Theme_Dialog);
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
				
				gv = (GridView) d.findViewById(R.id.myGrid);		
				gv.setAdapter(new ImageAdapterIngredientCard(arg1.getContext(), allergs));
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

				
				TextView bt = (TextView) d.findViewById(R.id.bottomtextingred);		//NEED TO DO --
				bt.setText(DietaryAssistantActivity._Ingredients.getAdditionalIngredientInfo(ingredient));         //WILL BE ADDITIONAL INFO ABOUT EACH INGREDIENT	
				d.show();
				return false;
				
				
			}});
	}

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, CHECK_ALL, 0, "Select All");
        menu.add(0, UNCHECK_ALL, 0, "Deselect All");
        menu.add(0, FILTER, 0, "Filter Ingredients");
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CHECK_ALL:
            	Log.d("create", "Select all clicked");
                for(int i = 0; i < _ingredients.size(); i++)
                {
                	String cur_ingredient = _ingredients.get(i);
                	if(!_clickeditems.contains(cur_ingredient)) {
    					_clickeditems.add(cur_ingredient);
    				}
                }
                _myAdapter.notifyDataSetChanged();
                return true;
            case UNCHECK_ALL:
            	Log.d("create", "Deselect all clicked");
            	for(int i = 0; i < _ingredients.size(); i++)
                {
                	String cur_ingredient = _ingredients.get(i);
                	if(_clickeditems.contains(cur_ingredient)) {
    					_clickeditems.remove(cur_ingredient);
    				}
                }
            	_myAdapter.notifyDataSetChanged();
            	return true;
            case FILTER:
            	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(requestcode == CALL_REVIEW_SCREEN) {
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				setResult(CreateAllergyActivityReviewAllergy.SAVED_BUTTON);
				finish();
			}
			else if(resultcode == CreateAllergyActivityReviewAllergy.BACKWARD_BUTTON) {
				//backward button
				//do nothing
			}
		}	
	}


	//helper for the filter 
	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			_myAdapter.getFilter().filter(s);      
		}

	};

	

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
	    			String ingredient = (String) getItem(position);
	    			String allergy = "milk";
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
	
	
	

	private class myAdapter extends ArrayAdapter<String> implements Filterable {
		private ModelFilter filter;
		private LayoutInflater mInflater;
		private List<String> mlist;

		public myAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
			mInflater = LayoutInflater.from(context);
			mlist = list2;
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String ingredient = getItem(position);
			View v = convertView;

			if(v==null) {
			
				LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.ingredient_item_checkable, null);	
				v.setFocusable(false);
			}
			
			Typeface tf = Typeface.createFromAsset(
			        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
			CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
			TextView tv = (TextView) v.findViewById(R.id.text);
			tv.setTypeface(tf);
			tv.setText(ingredient);
					
			if(_clickeditems.contains(ingredient)) {

				//view.setBackgroundColor(colors[0]);
				//_listview.setItemChecked(position, true);
				cb.setChecked(true);

			}
			else {
				//view.setBackgroundColor(colors[1]);
				//_listview.setItemChecked(position,false);
				cb.setChecked(false);
			}

			return v;
		}

		@Override
		public Filter getFilter() {
			if (filter == null){
				filter  = new ModelFilter();
			}
			return filter;
		}

		//custom filter (used so we never filter our "create ingredient")
		private class ModelFilter extends Filter
		{

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if(constraint != null && constraint.toString().length() > 0)
				{
					ArrayList<String> filteredItems = new ArrayList<String>();

					for(int i = 0, l = _ingredients.size(); i < l; i++)
					{
						String m = _ingredients.get(i);
						if(m.toLowerCase().contains(constraint.toString().toLowerCase()))
							filteredItems.add(m);
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				}
				else
				{
					synchronized(this)
					{
						result.values = _ingredients;
						result.count = _ingredients.size();
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {

				_filteredModelItemsArray = (ArrayList<String>)results.values;
				notifyDataSetChanged();
				clear();
				for(int i = 0, l = _filteredModelItemsArray.size(); i < l; i++)
					add(_filteredModelItemsArray.get(i));
				notifyDataSetInvalidated();
			}
		}

	}
}
/*
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder;

		if (id == ADD_INGREDIENT) {
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Add New Ingredient");
			builder.setMessage("Type the name of the ingredient you would like to add:");

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			builder.setView(input);
			builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					int error_flag = OK;
					
					String name = input.getText().toString();
					if(name.trim().isEmpty()) {
						//call BLANK_FIELD error
						error_flag = BLANK_FIELD;
						CreateAllergyActivity3.this.showDialog(BLANK_FIELD);
					}
					else {
						boolean already_added = false;
						String temp;
						for(int i = 0; i < _addedIngreds.size(); i++) {
							temp = _addedIngreds.get(i);
							if(temp.equals(name.toUpperCase())) already_added = true;
						}
						
						Cursor cursor = DietaryAssistantActivity._Ingredients.dbHelper.checkIfIngredientExists(name);
						if(cursor.getCount() > 0 || already_added == true) {
							//call INGREDIENT_EXISTS error
							error_flag = INGREDIENT_EXISTS;
							CreateAllergyActivity3.this.showDialog(INGREDIENT_EXISTS);
						}
					}
					if(error_flag == OK) {
						_bindinglist.add(name.toUpperCase());
						_clickeditems.add(name.toUpperCase());
						//_addedIngreds.add(name.toUpperCase());
					}
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// do nothing
				}
			});
			dialog = builder.create();
		} 
		else if (id == INGREDIENT_EXISTS) {
			builder = new AlertDialog.Builder(this);
			builder.setMessage("An allergy with this name already exists.")
			       .setTitle("Error")
			       .setCancelable(false)
			       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			dialog = builder.create();
		} 
	    else if (id == BLANK_FIELD) {
			builder = new AlertDialog.Builder(this);
			builder.setMessage("You must type in a name for the ingredient before continuing.")
			       .setTitle("Error")
			       .setCancelable(false)
			       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			dialog = builder.create();
		} 
		else {
			dialog = null;
		}
		return dialog;
	}


}
*/

