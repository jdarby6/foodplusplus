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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AllergyChoiceView extends ListActivity {

	public static int CALL_CREATE_ALLERGY = 73612;
	
	private List<String> _bindinglist;
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
					
					if( ((TextView)arg1).getText()=="Create new allergy...") {
						Intent i = new Intent();
						i.setClass(AllergyChoiceView.this, CreateAllergyActivity.class);
						startActivityForResult(i,CALL_CREATE_ALLERGY);
			
					}
					else {
						if(_clickeditems.contains(((TextView)arg1).getText())) {
							_clickeditems.remove(((TextView)arg1).getText());
							//lv.setItemChecked(arg2, false);
						}
						else {
							_clickeditems.add((String)((TextView)arg1).getText());
							//lv.setItemChecked(arg2, true);
						}
					}
				}
			});
				

		
		_bindinglist = new ArrayList<String>();
		_bindinglist.addAll(DietaryAssistantActivity._Ingredients.all_allergies);
		_bindinglist.add("Create new allergy...");
		setListAdapter(new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, _bindinglist));
		for(int i = 0; i < DietaryAssistantActivity._Ingredients.all_allergies.size(); i++) {
			if(DietaryAssistantActivity._Ingredients.allergiesSuffered.contains((String) _bindinglist.get(i))) {
				_listview.setItemChecked(i, true); 
			}
		}
	}

	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(requestcode == CALL_CREATE_ALLERGY) {
			if(resultcode == CreateAllergyActivity2.BACKWARD_BUTTON_CODE) {
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setMessage("eewrqreeq");
				ad.show();
			}
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				//ADD CODE HERE TO UPADTE THE LIST AND CHECK OFF THE NEW ALLERGY ?
				setResult(CreateAllergyActivityReviewAllergy.SAVED_BUTTON);
				finish();
			}
			//decide what to do now about going back straight to main menu
		}	
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(DietaryAssistantActivity._Ingredients.allergiesSuffered.contains(l.getItemAtPosition(position))) {
			DietaryAssistantActivity._Ingredients.allergiesSuffered.remove(l.getItemAtPosition(position));
		}
		else { 
			DietaryAssistantActivity._Ingredients.allergiesSuffered.add((String) l.getItemAtPosition(position));
		}
	}
	
	private class myAdapter extends ArrayAdapter<String> implements Filterable {
		public myAdapter(Context context, int textViewResourceId, List<String> list2) {
			super(context,textViewResourceId,list2);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String ingredient = getItem(position);
			View view = super.getView(position,convertView,parent);
	
			if(ingredient=="Create new ingredient...") {
				//make it look normal
			}
			else {
				if(_clickeditems.contains(ingredient)) {
					_listview.setItemChecked(position, true);
				
					//lv.set
					
				}
				else {
					_listview.setItemChecked(position,false);
				}
			}
			
			return view;
		}
	}
}


