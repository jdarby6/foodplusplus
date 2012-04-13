package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AllergyChoiceView extends ListActivity {

	public static int CALL_CREATE_ALLERGY = 73612;

	private ArrayList<String> _bindinglist;
	private List<String> _clickeditems;
	private ListView _listview;
	private ImageButton _createbutton;
	private ImageButton _backbutton;
	
	private String[] menuItems;
	private Dialog d;
	
	private CharSequence allergy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		menuItems = new String[] { "Delete" };
		
		setContentView(R.layout.allergyselection);
		_clickeditems = new ArrayList<String>();
		_clickeditems.addAll(DietaryAssistantActivity._Ingredients.allergiesSuffered);

		_listview = getListView();
		_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		_listview.setClickable(true);
		_listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 

				String allergy = (String) _listview.getItemAtPosition(arg2);

				if(_clickeditems.contains(allergy)) {
					_clickeditems.remove(allergy);
					DietaryAssistantActivity._Ingredients.allergiesSuffered.remove(allergy);
					CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkbox);
					cb.setChecked(false);
				}
				else {
					_clickeditems.add(allergy);
					DietaryAssistantActivity._Ingredients.allergiesSuffered.add(allergy);
					CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkbox);
					cb.setChecked(true);
				}				
			}
		});
		_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				TextView tv = (TextView) arg1.findViewById(R.id.toptext);
				allergy = tv.getText();
				
				List<String> ingredients = DietaryAssistantActivity._Ingredients.returnByAllergy(allergy.toString());
				
				
				d = new Dialog(arg0.getContext(),android.R.style.Theme_Dialog);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.allergy_card);
				
				TextView tt = (TextView) d.findViewById(R.id.toptextallergy);
				tt.setText(allergy);
				
				ImageButton close = (ImageButton) d.findViewById(R.id.close);
				close.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						d.dismiss();
					}
				});
				
				ImageButton del = (ImageButton) d.findViewById(R.id.delete);
				del.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						DietaryAssistantActivity._Ingredients.dbHelper.removeAllergy(allergy.toString());
						DietaryAssistantActivity._Ingredients.all_allergies.remove(allergy.toString());
						DietaryAssistantActivity._Ingredients.allergiesSuffered.remove(allergy.toString());
						_clickeditems.clear();
						_clickeditems.addAll(DietaryAssistantActivity._Ingredients.allergiesSuffered);
						//_bindinglist = new ArrayList<String>();
						_bindinglist.clear();
						_bindinglist.addAll(DietaryAssistantActivity._Ingredients.all_allergies);
						d.dismiss();
						_listview.setAdapter(new myAdapter(_listview.getContext(), android.R.layout.simple_list_item_multiple_choice, _bindinglist));
					}
	
				});
				
				
				ListView lv = (ListView) d.findViewById(R.id.listingreds);
				lv.setAdapter(new ArrayAdapter<String>(d.getContext(),R.layout.list_item,ingredients));
				
				//lv.setAdapter(adapter);
				
				
				//call displaying the dialog
				d.show();
				return false;
			}
			
			
			
			
		});


		_bindinglist = new ArrayList<String>();
		_bindinglist.addAll(DietaryAssistantActivity._Ingredients.all_allergies);
		setListAdapter(new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, _bindinglist));
		

//		registerForContextMenu(_listview);


		_backbutton = (ImageButton) findViewById(R.id.allergyback);
		_backbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}	
		});

		_createbutton = (ImageButton) findViewById(R.id.allergycreate);
		_createbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(AllergyChoiceView.this, CreateAllergyActivity.class);
				startActivityForResult(i,CALL_CREATE_ALLERGY);
			}



		});
	}
/*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		if (v.getId()==R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			menu.setHeaderTitle(_bindinglist.get(info.position));
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
//		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
//		String menuItemName = menuItems[menuItemIndex];
		String listItemName = _bindinglist.get(info.position);
		if(menuItemIndex == 0) { // "Delete"
			DietaryAssistantActivity._Ingredients.dbHelper.removeAllergy(listItemName);
			DietaryAssistantActivity._Ingredients.all_allergies.remove(listItemName);
			DietaryAssistantActivity._Ingredients.allergiesSuffered.remove(listItemName);
			
			_clickeditems = new ArrayList<String>();
			_clickeditems.addAll(DietaryAssistantActivity._Ingredients.allergiesSuffered);

			_bindinglist = new ArrayList<String>();
			_bindinglist.addAll(DietaryAssistantActivity._Ingredients.all_allergies);
			setListAdapter(new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, _bindinglist));
		}
		return true;
	}
*/
	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(requestcode == CALL_CREATE_ALLERGY) {
			if(resultcode == CreateAllergyActivity2.BACKWARD_BUTTON_CODE) {
				//backward button from next screen... do nothing
			}
			if(resultcode == CreateAllergyActivity.DISCARD) {
				//do nothing
			}
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				//update the allergy list in this view with the newest created allergy
				_clickeditems = new ArrayList<String>();
				_clickeditems.addAll(DietaryAssistantActivity._Ingredients.allergiesSuffered);

				_bindinglist = new ArrayList<String>();
				_bindinglist.addAll(DietaryAssistantActivity._Ingredients.all_allergies);
				setListAdapter(new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, _bindinglist));
			}
		}	
	}


	private class myAdapter extends ArrayAdapter<String>  {

		private ArrayList<String> items;

		public myAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
			super(context,textViewResourceId,items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			String allergy = getItem(position);
			View v = convertView;

			if(v==null) {

				LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.allergy_list_item, null);	
				v.setFocusable(false);
			}

			if(allergy != null) {

				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);

				ImageView iv = (ImageView) v.findViewById(R.id.icon);
				DietaryAssistantActivity._Icons.setImageIcon(iv, DietaryAssistantActivity._Icons.GetIconIndex(allergy));

				if(tt != null) {
					tt.setText(allergy);
				}
				if(bt != null) {
					//need to make this fill in the "additional info" about allergies (text below allergy name)
					//*****
					int numberOfIngredients = DietaryAssistantActivity._Ingredients.returnByAllergy(allergy).size();
					bt.setText(Integer.toString(numberOfIngredients)+ " ingredients");
				}

				if(_clickeditems.contains(allergy)) {
					CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
					cb.setChecked(true);
				}
				else {
					if(v.findViewById(R.id.checkbox) != null) {
						CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
						cb.setChecked(false);	
					}
				}

			}

			return v;
		}
	}
}



