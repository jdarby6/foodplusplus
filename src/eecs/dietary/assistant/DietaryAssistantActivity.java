package eecs.dietary.assistant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;


public class DietaryAssistantActivity extends Activity {
    /** Called when the activity is first created. */
	
	public static Ingredients _Ingredients; //so every activity can access it..dunno if this is the best style
											//(i was using intent's before to pass data but that got messy)
	private Uri mCapturedImageURI;
	private int TAKEIMG = 600;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        _Ingredients = new Ingredients();
        
        setContentView(R.layout.main);
   
    }
    
    //TODO: Still need to add in the code for when popping up the camera 
    
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
    
    public void Camera_on_click(final View trash) {
       /*String fileName = "temp.jpg";  
       ContentValues values = new ContentValues();  
       values.put(MediaStore.Images.Media.TITLE, fileName);  
       mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  

       Log.d("save", "Line 55: Camera?");
       
       Camera.Parameters parameters = Camera.getParameters();

       parameters.setColorEffect(Camera.Parameters.EFFECT_AQUA);

       camera.setParameters(parameters);
       
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
       intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
       startActivityForResult(intent, TAKEIMG);*/
    	Intent intent = new Intent();
    	intent.setClass(DietaryAssistantActivity.this, CameraView.class);
    	startActivityForResult(intent, 0);
    }
    
}