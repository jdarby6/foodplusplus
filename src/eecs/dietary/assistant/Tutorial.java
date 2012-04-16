package eecs.dietary.assistant;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Tutorial extends Activity {
//	private ImageButton back;
	
//	public static int BACKWARD_BUTTON = 57381;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tutorial);

		Typeface tf = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
		
		TextView tv;
		tv = (TextView) findViewById(R.id.allergiesText);
		tv.setTypeface(tf);
		tv.setText("This button allows you to choose what allergies you suffer from, " + 
				"so that the app knows what ingredients to watch out for. Choose from 8 pre-loaded allergies, " +
				"or create your own custom filter! Long-press on the allergy name to bring up more info or " +
				"delete the allergy from the database.");
		
		tv = (TextView) findViewById(R.id.cameraText);
		tv.setTypeface(tf);
		tv.setText("This button allows you to either take a picture of an ingredients label " +
				"using the camera on your phone, or to choose an existing picture of an ingredients " +
				"label from your phone's picture gallery. Make sure to crop only the ingredients label " +
				"for best results.");
		
		tv = (TextView) findViewById(R.id.barcodeText);
		tv.setTypeface(tf);
		tv.setText("This button allows you to scan a product's barcode, in case the ingredients label " +
				"is too difficult to read. It produces the same results as taking a picture.");
		
		tv = (TextView) findViewById(R.id.keyboardText);
		tv.setTypeface(tf);
		tv.setText("This button allows you to browse through the list of ingredients and filter the list " +
				"by typing in the name of an ingredient. Use this if you have any questions about whether " +
				"a particular ingredient may trigger an allergy.");
		
		tv = (TextView) findViewById(R.id.optionsText);
		tv.setTypeface(tf);
		tv.setText("This button allows you to adjust the image scanning quality for the ingredients label. " +
				"A higher value takes more time and memory, but the results are better. If you're using an " +
				"older phone, you may need to decrease this value.");
		
		
	
//	this.back = (ImageButton) this.findViewById(R.id.buttonBack);
//	this.back.setOnClickListener(new Button.OnClickListener() { 
//		public void onClick(View v) {
//			setResult(BACKWARD_BUTTON);
//			finish(); 
//		}
//	});
	
	}
	
	
}