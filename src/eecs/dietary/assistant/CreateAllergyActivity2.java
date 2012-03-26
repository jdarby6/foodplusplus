package eecs.dietary.assistant;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


public class CreateAllergyActivity2 extends Activity {
	
	private Button backward;
	private Button forward;
	public static int BACKWARD_BUTTON_CODE = 90819084;
	public static int CALL_ACTIVITY_3 = 837123;
	private ArrayList<String> _clickeditems;
	private String allergyname;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createallergyscreen2);
		_clickeditems = new ArrayList<String>();

		Intent sender = getIntent();
		allergyname = sender.getExtras().getString("allergyname");
		
		lv = (ListView) findViewById(R.id.listallergycreate2);
		
		lv.setAdapter(new myAdapter(this, android.R.layout.simple_list_item_multiple_choice, DietaryAssistantActivity._Ingredients.all_allergies));
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setClickable(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 
				String allergy = (String) lv.getItemAtPosition(arg2);
				
					if(_clickeditems.contains(allergy)) {
						_clickeditems.remove(allergy);
						CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkbox);
						cb.setChecked(false);
					}
					else {
						_clickeditems.add(allergy);
						CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkbox);
						cb.setChecked(true);
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
				i.putExtra("allergyname",allergyname);
				startActivityForResult(i,CALL_ACTIVITY_3);
				//finish();
			}
		});
		
		
	}
	
	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		//if(requestcode == CreateAllergyActivity3.) {
			if(resultcode == CreateAllergyActivity2.BACKWARD_BUTTON_CODE) {
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setMessage("eewrqreeq");
				ad.show();
			}
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				setResult(CreateAllergyActivityReviewAllergy.SAVED_BUTTON);
				finish();
			}
			//decide what to do now about going back straight to main menu
		}	
	//}
	
	
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
			if(v == null) { 
				LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.allergy_list_item, null);	

			}
			v.setFocusable(false);
			
			if(allergy != null) {
				
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if(tt != null) {
					tt.setText(allergy);
				}
				if(bt != null) {
					bt.setText("temp");
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
