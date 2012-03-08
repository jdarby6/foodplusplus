package eecs.dietary.assistant;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DietaryAssistantActivity extends Activity {
    /** Called when the activity is first created. */
	
	public static Ingredients _Ingredients; //so every activity can access it..dunno if this is the best style
											//(i was using intent's before to pass data but that got messy)
	public static OCR _OCR;
	
	public static OCRReader _OCRReader;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        File myDir = getExternalFilesDir(Environment.MEDIA_MOUNTED);
        
        _OCR = new OCR(myDir);
        _Ingredients = new Ingredients(myDir.toString()); 
        _OCRReader = new OCRReader();
        
        _OCRReader.IngredientsFound = Arrays.asList("milk", "leche", "coca", "soy", "chocolate");
        
        
        setContentView(R.layout.main);
   
    }
    
    //TODO: Still need to add in the code for when popping up the camera 
    
    public void Camera_on_click(final View trash) {
   	
    	Intent intent = new Intent();
    	intent.setClass(this, OCRFeedback.class);
    	startActivityForResult(intent,0);    	
    	
    }
    
    public void TextInput_on_click(final View trash) {
    	
       	    Intent intent = new Intent();
            intent.setClass(this, KeyboardInputView.class); 
            startActivityForResult(intent,0);
    	
    }
    
    public void Allergy_on_click(final View trash) {
    	//will be used to call up the allergy selector 
    	
    	Intent intent = new Intent();
    	intent.setClass(this, AllergyChoiceView.class);
    	startActivityForResult(intent,0);

    }
    
}