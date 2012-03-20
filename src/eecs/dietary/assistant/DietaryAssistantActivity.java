package eecs.dietary.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;


public class DietaryAssistantActivity extends Activity {
	/** Called when the activity is first created. */

	public static Ingredients _Ingredients;
	
	public static OCR _OCR;
	
	public static OCRReader _OCRReader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_OCR = new OCR(Environment.getExternalStorageDirectory()); //location of tesseract language file (on phone)
		_OCRReader = new OCRReader();
		
		_Ingredients = new Ingredients(getBaseContext());

		setContentView(R.layout.main);

	}

	public void Camera_on_click(final View trash) {
		
		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, CameraView.class);
		
		startActivityForResult(intent, 0);
	}
	
	public void Barcode_on_click(final View trash) {
		
		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, BarcodeView.class);
		
		startActivityForResult(intent, 0);
	}

	public void TextInput_on_click(final View trash) {

		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, KeyboardInputView.class);

		startActivityForResult(intent, 0);

	}

	public void Allergy_on_click(final View trash) {

		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this, AllergyChoiceView.class);
		
		startActivityForResult(intent, 0);

	}

}