package com.example.loginbustrans;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private EditText txtUser;
	private EditText txtPassword;
	private EditText txtStatus;
	private Button btnLogin;
	private String surl = "http://furqon.zz.cv/login.php";
	/**
	 * Method yang dipanggil pada saat aplikaasi dijalankan
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtUser = (EditText) findViewById(R.id.txtUser);
		
		txtUser.setFocusableInTouchMode(true);
		txtUser.requestFocus();
		txtPassword = (EditText) findViewById(R.id.txtPass);
		txtPassword.setFocusableInTouchMode(true);
		txtPassword.requestFocus();
		txtStatus = (EditText) findViewById(R.id.txtStatus);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		
		//daftarkan even onClick pada btnLogin
		 btnLogin.setOnClickListener(new Button.OnClickListener(){
	            public void onClick(View v){
	            	readWebpage(v);
	            }
	        });	
		 if (getIntent().getBooleanExtra("EXIT", false)) {
	            finish();
	            return;
	        }
	}
	
	
	/**
	 * Method untuk Mengirimkan data keserver
	 *
	 */
	public String getRequest(String Url){
       String code;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(Url);
        try{
            HttpResponse response = client.execute(request);
            code= request(response);
        }catch(Exception ex){
           code= "Gagal koneksi ke server!";
        }
        return code;

    }
	/**
	 * Method untuk Menerima data dari server
	 * @param response
	 * @return
	 */
	public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }
/**
	 * Class CallWebPageTask untuk implementasi class AscyncTask
	 */
	private class CallWebPageTask extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog;
		protected Context applicationContext;

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, "Login Process", "Please Wait...", true);
		}

	    @Override
	    protected String doInBackground(String... urls) {
	      String response = "";
	      response = getRequest(urls[0]);
	      return response;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	this.dialog.cancel();
	    	txtStatus.setText(result);
	    }
	  }
	  public void readWebpage(View view) {
	    CallWebPageTask task = new CallWebPageTask();
	    task.applicationContext = MainActivity.this;
	    String url =surl+"?user="+txtUser.getText().toString()+"&&password="+txtPassword.getText().toString();
	    task.execute(new String[] { url });
	  }
	  
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	      if (keyCode == KeyEvent.KEYCODE_BACK ) {
	          Intent intent = new Intent(MainActivity.this, MainActivity.class);
	          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	          intent.putExtra("EXIT", true);
	          startActivity(intent);
	      }
	      return super.onKeyDown(keyCode, event);
	  }
	  /*public void onBackPressed()
	  {
	    int backButtonCount = 0;
			if(backButtonCount >= 1)
	      {
				Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
	          
	          
	          
	      }
	      else
	      {
	          Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
	          backButtonCount++;
	      }
	  }*/
}
