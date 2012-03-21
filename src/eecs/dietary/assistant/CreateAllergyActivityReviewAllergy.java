package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class CreateAllergyActivityReviewAllergy extends ListActivity {
	
	
	private TextView tv;
	private ListView lv;
	private ArrayAdapter<String> adapter;
	private List<String> ingredientstoinclude;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviewallergyscreen);
		ingredientstoinclude = new ArrayList<String>();

		Intent sender = getIntent();
		String allergyname = sender.getExtras().getString("allergyname");
		tv = (TextView) findViewById(R.id.textviewreviewallergy);
		tv.setText("How does this look for" + allergyname + "?");
		
		ingredientstoinclude = sender.getExtras().getStringArrayList("ingredients");
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientstoinclude);
		lv = getListView();
		lv.setAdapter(adapter);
		

	}
	
	
}