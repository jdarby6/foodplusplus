package eecs.dietary.assistant;

import java.io.File;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCR {
	
	public TessBaseAPI _tessapi;
	public String readText;
	public int pixeldensity;
	public int subsamplefactor;
	public boolean preferquality;
	public int ocrquality;
	
	private String whitelist = "1234567890&abcdefghijklmnopqrstuvwxyz?ABCDEFGHIJKLMNOPQRSTUVWXYZ/.,() :{}";
	
	OCR(File dir) {
		_tessapi = new TessBaseAPI();
		//_tessapi.init(dir.toString(),"eng",TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
		_tessapi.init(dir.toString(), "eng");
		pixeldensity = 450;
		subsamplefactor = 1;
		preferquality = true;
		ocrquality = 100;
		_tessapi.setVariable("tessedit_char_whitelist",whitelist);
	//	_tessapi.setPageSegMode(TessBaseAPI.PSM_AUTO_OSD);
	//	_tessapi.
	}
	
	void ReadBitmapImage(Bitmap bitmap) {
		_tessapi.setImage(bitmap);
		readText = _tessapi.getUTF8Text();
        Log.d("readText", readText);
	}
	
	public int calculateQualityRating() {
		return ocrquality;
	}
	
	public void setQualityRating(int quality) {
		pixeldensity = (int) (450 * (double)quality/(double)100);
		preferquality = quality > 75;
		subsamplefactor = (int) Math.floor(5 - quality * 4);
		ocrquality = quality;

	}
	
	
	
	
	
	

}
