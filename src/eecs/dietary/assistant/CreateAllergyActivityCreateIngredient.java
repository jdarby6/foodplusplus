package eecs.dietary.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateAllergyActivityCreateIngredient extends Activity {
	
	private Button discard;
	private Button create; 
	private EditText et;
	public static int BACKWARD_BUTTON = 4203432;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createallergycreateingredient);
		this.et = (EditText) this.findViewById(R.id.editIngredientTextCreate);	
		this.et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		this.discard = (Button) this.findViewById(R.id.buttonDiscardIngredient1);
		this.create = (Button) this.findViewById(R.id.buttonCreateIngredient1);

		this.create.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				//check to see if okay to create and create it
				//send back result that this needs to also be checked and added to the list 
				
			}
		});
		this.discard.setOnClickListener(new Button.OnClickListener() { 
			public void onClick(View v) {
				setResult(BACKWARD_BUTTON);
				finish();
				
			}
		});
	}
	
	@Override
	public void finish() {
		//setActivityResult
		super.finish();
		
	}
	
	
	
	
		
		
}