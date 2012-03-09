package eecs.dietary.assistant;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;


public class DietaryAssistantActivity extends Activity {
	/** Called when the activity is first created. */

	public static Ingredients _Ingredients; //so every activity can access it..dunno if this is the best style
	//(i was using intent's before to pass data but that got messy)
	
	public static OCR _OCR;
	
	public static OCRReader _OCRReader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	//	AlertDialog ad = new AlertDialog.Builder(this).create();
		
		File myDir = Environment.getExternalStorageDirectory();
	//	ad.setMessage(myDir.toString());
	//	ad.show();
		
		_OCR = new OCR(myDir);
		_OCRReader = new OCRReader();
		
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