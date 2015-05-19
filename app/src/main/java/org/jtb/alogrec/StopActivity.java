package org.jtb.alogrec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jtb.alogrec.donate.R;

public class StopActivity extends Activity {
	private Button stopButton;
	private Button cancelButton;
	private TextView logFileText;
	private EditText check;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.stop);

		check = (EditText)this.findViewById(R.id.stopKeyWord);


        stopButton = (Button) this.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(check.getText().toString().equals("admin")){
//					Log.d("test", check.getText());
					Intent svcIntent = new Intent(StopActivity.this, RecordService.class);
					svcIntent.putExtra(RecordService.EXTRA_STOP_RECORD, true);
					startService(svcIntent);

					LogFilePref lfp = new LogFilePref(StopActivity.this);
					lfp.clearFile();

					finish();
				}

			}});
        cancelButton = (Button) this.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}});
        
        logFileText = (TextView) this.findViewById(R.id.log_file_text);
        logFileText.setText(new LogFilePref(this).toString());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}