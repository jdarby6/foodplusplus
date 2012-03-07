package eecs.dietary.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class DietaryAssistantActivity extends Activity {
	/** Called when the activity is first created. */

	public static Ingredients _Ingredients; //so every activity can access it..dunno if this is the best style
	//(i was using intent's before to pass data but that got messy)

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_Ingredients = new Ingredients(getBaseContext());

		setContentView(R.layout.main);

	}

	public void Camera_on_click(final View trash) {
		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, CameraView.class);
		startActivityForResult(intent, 0);
	}

	public void TextInput_on_click(final View trash) {

		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, KeyboardInputView.class);

		startActivityForResult(intent,0);

	}

	public void Allergy_on_click(final View trash) {
		//will be used to call up the allergy selector 

		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, AllergyChoiceView.class);
		startActivityForResult(intent,0);

	}

}