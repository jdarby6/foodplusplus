package eecs.dietary.assistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//First screen in create allergy sequence
//Displays text box input for allergy name 

public class CreateAllergyActivity extends Activity {
	
	private Button discard;
	private Button create; 
	private EditText et;
	public static int CALL_CREATEACTIVITY2 = 38238414;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createallergyscreen1);
		et = (EditText) this.findViewById(R.id.editTextAllergyCreate);	
		et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
		this.discard = (Button) this.findViewById(R.id.buttonDiscardAllergy1);
		this.create = (Button) this.findViewById(R.id.buttonCreateAllergy1);

		this.create.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				
				//call function to see if the input allergy name in edittext is ok
				if(true) {
					Intent i = new Intent();
					i.setClass(CreateAllergyActivity.this, CreateAllergyActivity2.class);
					i.putExtra("allergyname", et.getText().toString());
					startActivityForResult(i, CALL_CREATEACTIVITY2);
				}	
			}
		});
		this.discard.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				finish();
				
			}
		});
	}
		
		
	@Override 
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(requestcode == CALL_CREATEACTIVITY2) {
			if(resultcode == CreateAllergyActivityReviewAllergy.SAVED_BUTTON) {
				setResult(CreateAllergyActivityReviewAllergy.SAVED_BUTTON);
				finish();
			}
		}	
	}		
}