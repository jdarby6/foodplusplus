package eecs.dietary.assistant;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllergyChoiceView extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setClickable(true);

		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, DietaryAssistantActivity._Ingredients.all_allergies));
		for(int i = 0; i < DietaryAssistantActivity._Ingredients.all_allergies.size(); i++) {
			if(DietaryAssistantActivity._Ingredients.allergiesSuffered.contains((String) DietaryAssistantActivity._Ingredients.all_allergies.get(i))) {
				lv.setItemChecked(i, true); 
			}
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
}


