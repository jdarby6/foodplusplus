package eecs.dietary.assistant;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;



public class options extends Activity implements SeekBar.OnSeekBarChangeListener {
	
	SeekBar _ocrQualityBar;
	TextView _ocrQualityValue;
	TextView _ocrEstimatedtime;
	TextView _ocrQualityValueTop;
	ImageButton _resetbutton;
	Dialog _areyousure;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.options);
		Typeface tf = Typeface.createFromAsset(
		        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
		_ocrQualityBar = (SeekBar) findViewById(R.id.seekbarOCRquality);
		_ocrEstimatedtime = (TextView) findViewById(R.id.estimatedTime);
		_ocrQualityValue = (TextView) findViewById(R.id.OCRqualitytext);
		_ocrQualityValueTop = (TextView) findViewById(R.id.OCRQualityIntro);
		_ocrQualityValueTop.setTypeface(tf);
		_ocrEstimatedtime.setTypeface(tf);
		_ocrQualityValue.setTypeface(tf);
		_ocrQualityBar.setProgress( DietaryAssistantActivity._OCR.calculateQualityRating());
		_ocrQualityBar.setSecondaryProgress(_ocrQualityBar.getProgress());
		_ocrEstimatedtime.setText(calcEstimateTimeMsg(_ocrQualityBar.getProgress()));
		_ocrQualityValue.setText(Integer.toString(_ocrQualityBar.getProgress()));
		_ocrQualityBar.setOnSeekBarChangeListener(this);
		
		
		_resetbutton = (ImageButton) findViewById(R.id.resetbutton);
		_resetbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_areyousure == null) {
					_areyousure = new Dialog(v.getContext());
					_areyousure.requestWindowFeature(Window.FEATURE_NO_TITLE);
					_areyousure.setContentView(R.layout.confirmation);
					
				}
				TextView tv = (TextView) _areyousure.findViewById(R.id.message);
				Typeface tf = Typeface.createFromAsset(
				        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
				tv.setText("Are you sure you want to reset? All user generated allergies will be lost");
				tv.setTypeface(tf);
				ImageButton yes = (ImageButton) _areyousure.findViewById(R.id.yes);
				ImageButton cancel = (ImageButton) _areyousure.findViewById(R.id.cancel);
				yes.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
				//		DietaryAssistantActivity._Ingredients.reset();
						_areyousure.dismiss();
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						_areyousure.dismiss();
					}
				});
				
				_areyousure.show();
			}
			
			
			
			
		});
		
	}
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		_ocrQualityValue.setText(Integer.toString(arg1));
		_ocrQualityBar.setProgress(arg1);
		_ocrEstimatedtime.setText(calcEstimateTimeMsg(arg1));
	}
	public void onStartTrackingTouch(SeekBar arg0) {
		//_ocrQualityBar.setSecondaryProgress(arg0.getProgress());
		
	}
	public void onStopTrackingTouch(SeekBar arg0) {
		_ocrQualityBar.setSecondaryProgress(arg0.getProgress());
		DietaryAssistantActivity._OCR.setQualityRating(arg0.getProgress());
	}
	
	private String calcEstimateTimeMsg(int progress) {

		//need to add code here to determine based on 
		//camera specs of phone, the estimated time
		double estimatedSecs = ((double)5/(double)10000) * Math.pow(progress, 2); //quadratic decay, expected time 5s on 100quality
		String msg = Double.toString(estimatedSecs).substring(0, 3) + " seconds estimated computation time";
		
		return msg;
	}
	
	
	
	
	
}