package app.test.testapp2;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    static String str = "";
    Button button;
    TextView textView;
    EditText editName;
    EditText editCountry;
    EditText editTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.accept);
        textView = (TextView)findViewById(R.id.textView);
        editName = (EditText)findViewById(R.id.editName);
        editCountry = (EditText)findViewById(R.id.editCountry);
        editTwitter = (EditText)findViewById(R.id.editTwitter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpAsyncTask httpAsyncTask = new HttpAsyncTask(MainActivity.this);
                httpAsyncTask.execute("http://hmkcode.appspot.com/jsonservlet", editName.getText().toString(), "hi", "bye");
//                httpAsyncTask.execute("http://192.168.1.15:8088/myapp-api/user/list");
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
        protected void onPre

        @Override
        protected String doInBackground(String... strings) {
            Person person = new Person();
            person.setName(strings[1]);
            person.setCountry(strings[2]);
            person.setTwitter(strings[3]);

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

//                JSONObject jsonObject = new JSONObject();
//                jsonObject.accumulate("name", person.getName());
//                jsonObject.accumulate("country", person.getCountry());
//                jsonObject.accumulate("twitter", person.getTwitter());

//                httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setRequestProperty("accept", "application/json");

//                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                InputStream is = httpURLConnection.getInputStream();
//                OutputStream os = httpURLConnection.getOutputStream();

//                String json = jsonObject.toString();
//                os.write(json.getBytes());
//                os.flush();

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
                        JSONObject json = new JSONObject(str);
//                        Iterator<?> key = json.keys();
//                        while (key.hasNext()) {
//                            String k = (String)key.next();
//                            mainActivity.textView.setText(k.toString());
//                        }
                            mainActivity.textView.setText(json.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class Person {
        private String name;
        private String country;
        private String twitter;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", country='" + country + '\'' +
                    ", twitter='" + twitter + '\'' +
                    '}';
        }
    }
}
