package com.algonquincollege.megh0011.ictlabschedules;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.algonquincollege.mad9132.ictlabschedules.R;

import util.ServiceHandler;
import util.SpecialAdapter;

import domain.Lab;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//import com.algonquincollege.megh0011.ictlabschedules.RandomArrayAdapter;

/**
 * Display the list of ICT labs.
 * 
 * Usage: 1) Click one of the labs from the list to see its schedule.
 * 
 * Notes: 1) class ListLabsActivity extends from Android's class ListActivity.
 * 
 * @author megh0011@algonquinlive.com
 * @Version 1.0
 */
public class ListLabsActivity extends ListActivity implements Constants {
	// JSON Node names
	private static final String TAG_ICT_LABS = "ict-labs";
	private static final String TAG_ROOM = "room";
	private static final String TAG_DESCRIPTION = "description";

	// private ArrayAdapter<Lab> labsAdapter;
	private SpecialAdapter labsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_labs);
		labsAdapter = new SpecialAdapter(this, null, R.layout.lab_view, null,
				null);
		// labsAdapter = new ArrayAdapter<Lab>( this, R.layout.lab_view );
		setListAdapter(labsAdapter);

		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// listView.setAdapter(labsAdapter);

		// String[] from = new String[] {"rowid", "col_1", "col_2", "col_3"};
		// int[] to = new int[] { R.id., R.id.item2, R.id.item3, R.id.item4 };

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedLab = ((TextView) view).getText().toString();
				Intent intent = new Intent(getApplicationContext(),
						ScheduleActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(THE_LAB, selectedLab);
				startActivity(intent);
			}
		});

		// Calling async task to get JSON
		new FetchLabs().execute(URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_author) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class FetchLabs extends AsyncTask<String, Void, List<Lab>> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ListLabsActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

			labsAdapter.clear();
		}

		@Override
		protected List<Lab> doInBackground(String... params) {
			List<Lab> labs = new ArrayList<Lab>();

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(params[0], ServiceHandler.GET);

			Log.d(TAG + " Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					JSONArray jsonLabs = jsonObj.getJSONArray(TAG_ICT_LABS);

					// labs.empty();
					// looping through each Lab, one at a time
					for (int i = 0; i < jsonLabs.length(); i++) {
						JSONObject jsonLab = jsonLabs.getJSONObject(i);

						String room = jsonLab.getString(TAG_ROOM);
						String description = jsonLab.getString(TAG_DESCRIPTION);

						// add this lab to the list of labs
						labs.add(new Lab(room, description));
					}
					return labs;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG + " ServiceHandler",
						"Couldn't get any data from the url");
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<Lab> result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			labsAdapter.addAll(result);

		}
	}
}