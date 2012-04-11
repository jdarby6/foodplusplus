package eecs.dietary.assistant;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class CreateAllergyActivity2 extends Activity {
	
	private ImageButton backward;
	private ImageButton forward;
	public static int BACKWARD_BUTTON_CODE = 90819084;
	public static int CALL_ACTIVITY_3 = 837123;
	private ArrayList<String> _clickeditems;
	private String allergyname;
	private ListView lv;
	private int allergyIconIndex;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.createallergyscreen2);
		_clickeditems = new ArrayList<String>();

		Intent sender = getIntent();
		allergyname = sender.getExtras().getString("allergyname");
		allergyIconIndex = sender.getExtras().getInt("iconIndex");
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
		
			
		this.backward = (ImageButton) this.findViewById(R.id.buttonBackAllergyCreate2);
		this.forward = (ImageButton) this.findViewById(R.id.buttonForwardAllergyCreate2);
		this.backward.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				setResult(BACKWARD_BUTTON_CODE);
				finish(); 
			}
		});
		this.forward.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(CreateAllergyActivity2.this, CreateAllergyActivity3.class);
				i.putStringArrayListExtra("checkedallergies", (ArrayList<String>) _clickeditems);
				i.putExtra("allergyname",allergyname);
				i.putExtra("iconIndex",allergyIconIndex);
				startActivityForResult(i,CALL_ACTIVITY_3);
			}
		});
		
		
	}
	
	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(requestcode == CALL_ACTIVITY_3) {
			if(resultcode == CreateAllergyActivity3.BACKWARD_BUTTON) {
				//backward button 
				//do nothing 
			}
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				setResult(CreateAllergyActivityReviewAllergy.SAVED_BUTTON);
				finish();
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
			if(v == null) { 
				LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.allergy_list_item, null);	

			}
			v.setFocusable(false);
			
			if(allergy != null) {
				
				ImageView iv = (ImageView) v.findViewById(R.id.icon);
				int iconIndex = DietaryAssistantActivity._Ingredients.GetIconIndex(allergy);	
				DietaryAssistantActivity._Ingredients.setImageIcon(iv, iconIndex);
				
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if(tt != null) {
					tt.setText(allergy);
				}
				if(bt != null) {
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
