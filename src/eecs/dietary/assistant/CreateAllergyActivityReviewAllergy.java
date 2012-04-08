package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class CreateAllergyActivityReviewAllergy extends ListActivity {
		
	private TextView tv;
	private ListView lv;
	private ArrayAdapter<String> adapter;
	private List<String> ingredientstoinclude;
	private Button discard;
	private Button create;
	private String allergyname;
	private int allergyIconIndex;
	public static int SAVED_BUTTON = 98765432;
	public static int BACKWARD_BUTTON = 57381;
	private ImageView iv; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reviewallergyscreen);
		ingredientstoinclude = new ArrayList<String>();

		Intent sender = getIntent();
		allergyname = sender.getExtras().getString("allergyname");
		allergyIconIndex = sender.getExtras().getInt("iconIndex");
		tv = (TextView) findViewById(R.id.textviewreviewallergy);
		tv.setText(allergyname );
		iv = (ImageView) findViewById(R.id.iconcreateallergy);
		
		DietaryAssistantActivity._Ingredients.setImageIcon(iv,allergyIconIndex);
		
		ingredientstoinclude = sender.getExtras().getStringArrayList("ingredients");
		
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, ingredientstoinclude);
		lv = getListView();
		lv.setAdapter(adapter);
		
	
	this.discard = (Button) this.findViewById(R.id.buttonDiscardAllergy1);
	this.create = (Button) this.findViewById(R.id.buttonCreateAllergy1);
	this.discard.setOnClickListener(new Button.OnClickListener() { 
		public void onClick(View v) {
			setResult(BACKWARD_BUTTON);
			finish(); 
		}
	});
	this.create.setOnClickListener(new Button.OnClickListener() { 
		public void onClick(View v) {
				
			setResult(SAVED_BUTTON);
			
			finish();
		}
	});
	
	}
	
	
}