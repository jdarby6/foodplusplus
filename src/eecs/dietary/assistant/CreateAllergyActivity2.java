package eecs.dietary.assistant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class CreateAllergyActivity2 extends Activity {
	
	private Button backward;
	private Button forward;
	public static int BACKWARD_BUTTON_CODE = 90819084;
	private List<String> _clickeditems;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createallergyscreen2);
		_clickeditems = new ArrayList<String>();
		//allergies = new ArrayList<String>();
		//allergies = DietaryAssistantActivity._Ingredients.all_allergies;
		
		ListView lv = (ListView) this.findViewById(R.id.listallergycreate2);
		
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,  DietaryAssistantActivity._Ingredients.all_allergies));
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setClickable(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
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
		
		
		
		
		
		
		
		this.backward = (Button) this.findViewById(R.id.buttonBackAllergyCreate2);
		this.forward = (Button) this.findViewById(R.id.buttonForwardAllergyCreate2);
		this.backward.setOnClickListener(new Button.OnClickListener() { //somehow is setting the "create" button
			public void onClick(View v) {
				setResult(BACKWARD_BUTTON_CODE);
				finish(); 
			}
		});
		this.forward.setOnClickListener(new Button.OnClickListener() { //somehow is setting the "create" button
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(CreateAllergyActivity2.this, CreateAllergyActivity3.class);
				i.putStringArrayListExtra("checkedallergies", (ArrayList<String>) _clickeditems);
				startActivityForResult(i,0);
				//finish();
			}
		});
		
		
	}
}




