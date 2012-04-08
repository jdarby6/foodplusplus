package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;


public class CreateAllergyActivity3 extends ListActivity {
	static final int ADD_INGREDIENT = 0;
	static final int INGREDIENT_EXISTS = 1;
	static final int BLANK_FIELD = 2;
	static final int OK = 3;

	public static int CALL_REVIEW_SCREEN = 784781;
	public static int BACKWARD_BUTTON = 9281;

	private List<String> _filteredModelItemsArray;


	//private Button _addIngred;
	private Button _backward;
	private Button _forward;
	private Button _keyboard; 
	private EditText _filterbox;


	private HashSet<String> _clickeditems;  //keeps track of what ingredients are clicked/checked
	private ListView _listview;
	private myAdapter _myAdapter;
	private List<String> _bindinglist;
	private List<String> _ingredients;
	private List<String> _checkedallergies;
	//private List<String> _addedIngreds;
	private String allergyname; 
	private int allergyIconIndex;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.createallergyscreen3);
		
		//setting up the edit box on the top of screen
		_filterbox = (EditText) findViewById(R.id.createallergy3searchbox);
		_filterbox.addTextChangedListener(filterTextWatcher);
		_filterbox.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		//setting up button to add new ingredient to list
		//_addIngred = (Button) this.findViewById(R.id.buttonAddIngredient);
		/*_addIngred.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				CreateAllergyActivity3.this.showDialog(ADD_INGREDIENT);
			}
		});*/

		//setting up buttons
		_backward = (Button) this.findViewById(R.id.buttonBackAllergyCreate3);
		_forward = (Button) this.findViewById(R.id.buttonForwardAllergyCreate3);
		_keyboard = (Button) this.findViewById(R.id.buttonKeyAllergyCreate3);
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
		_keyboard.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);	
			}
		});	

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

	//helper for the filter
	@Override
	protected void onDestroy() {
		super.onDestroy();
		_filterbox.removeTextChangedListener(filterTextWatcher);
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
			
			CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
			TextView tv = (TextView) v.findViewById(R.id.text);
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

