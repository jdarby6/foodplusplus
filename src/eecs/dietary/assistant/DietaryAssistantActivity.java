package eecs.dietary.assistant;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;


public class DietaryAssistantActivity extends Activity {
	/** Called when the activity is first created. */

	public static Ingredients _Ingredients;
	public static Icons _Icons;
	
	public static OCR _OCR;
	
	public static OCRReader _OCRReader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);	
		if(_OCR == null) {
			_OCR = new OCR(Environment.getExternalStorageDirectory()); //location of tesseract language file (on phone)
			_OCRReader = new OCRReader();
			_Ingredients = new Ingredients(getBaseContext());
			_Icons = new Icons(getBaseContext());
		}

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
	
	public void Options_on_click(final View trash) {
		Intent intent = new Intent();
		intent.setClass(DietaryAssistantActivity.this,options.class);
		startActivityForResult(intent,0);
	}
	
	
	
	
	
	

	public static void FormatStringArray(ArrayList<String> _bindinglist) {
		// TODO Auto-generated method stub
		
		for(int j=0; j<_bindinglist.size(); j++) {
			String s = _bindinglist.get(j);
	
			final StringBuilder result = new StringBuilder(s.length());
			String[] words = s.split("\\s");
			for(int i=0,l=words.length;i<l;++i) {
			  if(i>0) result.append(" ");      
			  result.append(Character.toUpperCase(words[i].charAt(0)))
			        .append(words[i].substring(1));
	
			}
			_bindinglist.set(j, result.toString());
		}
	}

}