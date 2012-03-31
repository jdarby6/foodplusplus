package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AllergyChoiceView extends ListActivity {

	public static int CALL_CREATE_ALLERGY = 73612;
	
	private ArrayList<String> _bindinglist;
	private List<String> _clickeditems;
	private ListView _listview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_clickeditems = new ArrayList<String>();
		_clickeditems.addAll(DietaryAssistantActivity._Ingredients.allergiesSuffered);
		
		_listview = getListView();
		_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		_listview.setClickable(true);
		_listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 
	
						String allergy = (String) _listview.getItemAtPosition(arg2);
						
						if(allergy == "Create new allergy...") { 
							Intent i = new Intent();
							i.setClass(AllergyChoiceView.this, CreateAllergyActivity.class);
							startActivityForResult(i,CALL_CREATE_ALLERGY);
							
						}
						else {
						
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
					
					}
			});
				
		_bindinglist = new ArrayList<String>();
		_bindinglist.add("Create new allergy...");
		_bindinglist.addAll(DietaryAssistantActivity._Ingredients.all_allergies);
		setListAdapter(new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, _bindinglist));
	}

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
				//add code to update the allergy list in this view with the newest created allergy (if not already done)
				//****
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

			LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(allergy == "Create new allergy...") {
				v = li.inflate(R.layout.create_allergy_item, null);
			}
			else {
				v = li.inflate(R.layout.allergy_list_item, null);	
			}

			v.setFocusable(false);
			
			if(allergy != null) {
				
				if(allergy == "Create new allergy...") { 
					TextView t = (TextView) v.findViewById(R.id.text);
					if(t != null) {
						t.setText(allergy);
					}
				}
				else {
					TextView tt = (TextView) v.findViewById(R.id.toptext);
					TextView bt = (TextView) v.findViewById(R.id.bottomtext);
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
			
			}
					
			return v;
		}
	}
}



