package eecs.dietary.assistant;

import java.io.File;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCR {
	
	public TessBaseAPI _tessapi;
	public String readText;
	
	private String whitelist = "1234567890&abcdefghijklmnopqrstuvwxyz?ABCDEFGHIJKLMNOPQRSTUVWXYZ/.,() :{}";
	
	OCR(File dir) {
		_tessapi = new TessBaseAPI();
		//_tessapi.init(dir.toString(),"eng",TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
		_tessapi.init(dir.toString(), "eng");
		
		_tessapi.setVariable("tessedit_char_whitelist",whitelist);
	//	_tessapi.setPageSegMode(TessBaseAPI.PSM_AUTO_OSD);
	//	_tessapi.
	}
	
	void ReadBitmapImage(Bitmap bitmap) {
		_tessapi.setImage(bitmap);
		readText = _tessapi.getUTF8Text();
        Log.d("readText", readText);
	}
	
	
	

}
