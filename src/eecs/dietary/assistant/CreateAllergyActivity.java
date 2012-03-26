package eecs.dietary.assistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//First screen in create allergy sequence
//Displays text box input for allergy name 

public class CreateAllergyActivity extends Activity {
	static final int OK = 0;
	static final int ALLERGY_EXISTS = 1;
	static final int BLANK_FIELD = 2;
	
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
				int error_flag = OK;

				if(et.getText().toString().trim().isEmpty()) error_flag = BLANK_FIELD;
				else {
					String text = et.getText().toString();
					Cursor cursor = DietaryAssistantActivity._Ingredients.dbHelper.checkIfAllergyExists(text);
					int count = cursor.getCount();
					if(cursor.getCount() > 0) error_flag = ALLERGY_EXISTS;
				}
				
				if(error_flag == OK) {
					Intent i = new Intent();
					i.setClass(CreateAllergyActivity.this, CreateAllergyActivity2.class);
					i.putExtra("allergyname", et.getText().toString());
					startActivityForResult(i, CALL_CREATEACTIVITY2);
				}
				else if(error_flag == ALLERGY_EXISTS) {
					CreateAllergyActivity.this.showDialog(ALLERGY_EXISTS);
				}
				else if(error_flag == BLANK_FIELD) {
					CreateAllergyActivity.this.showDialog(BLANK_FIELD);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
		AlertDialog.Builder builder;
		
	    if (id == ALLERGY_EXISTS) {
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
		} else if (id == BLANK_FIELD) {
			builder = new AlertDialog.Builder(this);
			builder.setMessage("You must type in a name for this allergy before continuing.")
			       .setTitle("Error")
			       .setCancelable(false)
			       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			dialog = builder.create();
		} else {
			dialog = null;
		}
	    return dialog;
	}
}