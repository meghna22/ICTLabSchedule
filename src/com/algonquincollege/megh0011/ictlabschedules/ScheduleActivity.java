package com.algonquincollege.megh0011.ictlabschedules;

import java.util.Calendar;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import util.ServiceHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import domain.Schedule;

//import com.algonquincollege.mad9132.ictlabschedules.R;

/**
 * Display the the schedule for the selected lab.
 * 
 * A schedule is a grid of rows and columns, where the rows are periods of time
 * (based on 24-hour clock): 0800, 0900, etc. and where the columns are the days
 * of the week: monday, tuesday, ... friday
 * 
 * @author megh0011@algonquinlive.com
 * @Version 1.0
 */
public class ScheduleActivity extends Activity implements Constants {
	private String theLab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		// Get the bundle of extras that was sent to this activity
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			theLab = bundle.getString(THE_LAB).toLowerCase(Locale.getDefault());
			// Calling async task to get json
			new FetchSchedule().execute(URL, theLab);
		}
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
	private class FetchSchedule extends AsyncTask<String, Void, Void> {
		private ProgressDialog pDialog;
		private Schedule theSchedule;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ScheduleActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

			theSchedule = new Schedule();
		}

		@Override
		protected Void doInBackground(String... params) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(params[0] + params[1],
					ServiceHandler.GET);

			Log.d(TAG + " Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					JSONObject jsonLab = jsonObj.getJSONObject(params[1]);
					for (Periods period : Periods.values()) {
						JSONObject periodRow = jsonLab.getJSONObject(period
								.name());
						
						for (DOW dow : DOW.values()) {
							String scheduledClass = periodRow.getString(dow
									.name());
							theSchedule.atPut(period, dow, scheduledClass);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG + " ServiceHandler",
						"Couldn't get any data from the url");
			}

			return null;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			String weekday = " ";
			String hours = " ";
			Calendar c = Calendar.getInstance();
			int DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			int HoursOfDay = c.get(Calendar.HOUR_OF_DAY);
			if (Calendar.MONDAY == DayOfWeek) {
				weekday = "Monday";
				TextView Monday = (TextView) findViewById(R.id.monday);
				Monday.setBackgroundColor(Color.YELLOW);
			} else if (Calendar.TUESDAY == DayOfWeek) {
				weekday = "Tuesday";
				TextView tuesday = (TextView) findViewById(R.id.tuesday);
				tuesday.setBackgroundColor(Color.YELLOW);
			} else if (Calendar.WEDNESDAY == DayOfWeek) {
				weekday = "Wednesday";
				TextView Wednesday = (TextView) findViewById(R.id.wednesday);
				Wednesday.setBackgroundColor(Color.YELLOW);
			} else if (Calendar.THURSDAY == DayOfWeek) {
				weekday = "Thursday";
				TextView Thursday = (TextView) findViewById(R.id.thursday);
				Thursday.setBackgroundColor(Color.YELLOW);
			} else if (Calendar.FRIDAY == DayOfWeek) {
				weekday = "Friday";
				TextView Monday = (TextView) findViewById(R.id.friday);
				Monday.setBackgroundColor(Color.YELLOW);
			} 
			else if (Calendar.SATURDAY == DayOfWeek) {
				weekday = "Saturday";
				TextView Monday = (TextView) findViewById(R.id.saturday);
				Monday.setBackgroundColor(Color.YELLOW);
			}	
			else if (Calendar.SUNDAY == DayOfWeek) {
					weekday = "Saturday";
					TextView Monday = (TextView) findViewById(R.id.sunday);
					Monday.setBackgroundColor(Color.YELLOW);
				}
			switch (HoursOfDay) {
			case 8:
				hours = "H0800";
				System.out.println("H0800");
				break;
			case 9:
				hours = "H0900";
				System.out.println("H0900");
				break;
			case 10:
				hours = "H01000";
				System.out.println("H01000");
				break;
			case 11:
				hours = "H1100";
				System.out.println("H1100");
				break;

			case 12:
				hours = "H1200";
				System.out.println("H1200");
				break;
			case 13:
				hours = "H1300";
				System.out.println("H1300");
				break;
			case 14:
				hours = "H1400";
				System.out.println("H1400");
				break;
			case 15:
				hours = "H1500";
				System.out.println("H1500");
				break;
			case 16:
				hours = "H1600";
				System.out.println("H1600");
				break;
			case 17:
				hours = "H1700";
				System.out.println("H1700");
				break;

			default:
				hours = "Invalid hours";
				break;
			}

			int id;
			for (Periods period : Periods.values()) {
				for (DOW dow : DOW.values()) {
					id = getResources().getIdentifier(
							period.name() + dow.name(), "id",
							getBaseContext().getPackageName());

					TextView cell = (TextView) findViewById(id);
					cell.setText(theSchedule.getScheduledClassAt(period, dow));
					
					//String cellText = cell.getText().toString();
					//System.out.println("::::::::::::" + cellText);
					cell.setBackgroundResource(android.R.drawable.editbox_dropdown_light_frame);
					
					
					if (dow.name().equalsIgnoreCase(weekday)
							&& period.name().equalsIgnoreCase(hours)) {
					Toast.makeText(getApplicationContext(), "Today is "+weekday +" Time is "+ hours,Toast.LENGTH_SHORT).show();
						cell.setBackgroundColor(Color.YELLOW);

					}
					
			
				}
				
			}

		}
	}
}
