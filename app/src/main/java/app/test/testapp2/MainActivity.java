package app.test.testapp2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    static String str = "";
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.accept);
        textView = (TextView)findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpAsyncTask httpAsyncTask = new HttpAsyncTask(MainActivity.this);
                httpAsyncTask.execute("http://hmkcode.appspot.com/jsonservlet");
            }
        });

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private MainActivity mainActivity;
        String result = "";

        HttpAsyncTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestProperty("content-type", "application/json");

                httpURLConnection.setDoInput(true);

                InputStream is = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            str = result;
            mainActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(str);
                        mainActivity.textView.setText(jsonArray.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
