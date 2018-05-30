package com.codezclub.addblog;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AddBlogActivity extends AppCompatActivity {

    String sname,semail,sblog;

    ProgressDialog p;

    EditText et_name,et_email,et_blog;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        et_name = (EditText)findViewById(R.id.et_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_blog = (EditText)findViewById(R.id.et_blog);

        btn_send = (Button)findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sname = et_name.getText().toString();
                semail = et_email.getText().toString();
                sblog = et_blog.getText().toString();

                //Async Task ---- background Thread -------------

                //To achieve multithreading for sending data in background----Create a Class

                Send send = new Send();
                send.execute(sname,semail,sblog);

            }
        });

    }

    private class Send extends AsyncTask<String,String,String> {


      @Override
      protected void onPreExecute()
      {
          super.onPreExecute();
          p = new ProgressDialog(AddBlogActivity.this);
          p.setMessage("Wait");
          p.setCancelable(false);
          p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
          p.show();

      }

        @Override
        protected String doInBackground(String... strings) {

            sname = strings[0];
            semail = strings[1];
            sblog = strings[2];

            try {
                URL url = new URL("http://www.searchkero.com/mj/insert.php");
                HttpURLConnection h = (HttpURLConnection)url.openConnection();
                h.setRequestMethod("POST");

                // -----view info. on server------------
                h.setDoOutput(true);

                OutputStream os = h.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"utf-8")); //send data through outputstrem
                String data = URLEncoder.encode("nameid", "utf-8") + "=" + URLEncoder.encode(sname,"utf-8") + "&&" +
                        URLEncoder.encode("emailid", "utf-8") + "=" + URLEncoder.encode(semail,"utf-8") + "&&" +
                        URLEncoder.encode("ideaid", "utf-8") + "=" + URLEncoder.encode(sblog,"utf-8");

                bw.write(data);
                bw.flush();
                bw.close();
                os.close();

                //---- To Write the data todatabase

                InputStream is = h.getInputStream();
                is.close();
                h.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return "Thanks for your Blog.";
        }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                p.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                et_name.setText("");
                et_email.setText("");
                et_blog.setText("");

            }
        }


}
