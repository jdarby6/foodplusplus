package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;


public class CreateAllergyActivity3 extends ListActivity {
		
	public static int CALL_REVIEW_SCREEN = 784781;
	public static int BACKWARD_BUTTON = 9281;
	
    private List<String> _filteredModelItemsArray;
	
	
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
	private String allergyname; 
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createallergyscreen3);
		
		//setting up the edit box on the top of screen
		_filterbox = (EditText) findViewById(R.id.createallergy3searchbox);
		_filterbox.addTextChangedListener(filterTextWatcher);
		_filterbox.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
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
				startActivityForResult(i,CALL_REVIEW_SCREEN);
			}
		});	
		_keyboard.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);	
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
		
		_clickeditems = new HashSet<String>();
		for(int i=0; i<(_checkedallergies.size()); i++) {
			_clickeditems.addAll(DietaryAssistantActivity._Ingredients.returnByAllergy(_checkedallergies.get(i)));
		}
			
		_filteredModelItemsArray = new ArrayList<String>();
		
		_myAdapter = new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, _bindinglist);
		setListAdapter(_myAdapter);
	
		
		_listview = getListView();
		_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		_listview.setClickable(true);
		_listview.setTextFilterEnabled(true);
		
		_listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 
				if(_clickeditems.contains(((TextView)arg1).getText())) {
					_clickeditems.remove(((TextView)arg1).getText());
				}
				else {
					_clickeditems.add((String)((TextView)arg1).getText());
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
			}
			
	
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				int[] colors = new int[] { 0x30FF0000, 0x300000FF }; //0 is red, 1 is blue
				String ingredient = getItem(position);
				View view = super.getView(position,convertView,parent);
		
				if(_clickeditems.contains(ingredient)) {
					
					view.setBackgroundColor(colors[0]);
					_listview.setItemChecked(position, true);
				
				}
				else {
					view.setBackgroundColor(colors[1]);
					_listview.setItemChecked(position,false);
				}
				
				return view;
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


