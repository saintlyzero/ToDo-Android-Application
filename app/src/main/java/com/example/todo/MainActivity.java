package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button b1;
    TextView t1;
    EditText et_usr, et_pass;
    SharedPreferences sharedPreferences;

    final String SEND_URL = "https://mytodoappserver.000webhostapp.com/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BCD4")));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Todo App</font>"));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#00ACC1"));
        }

        sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String restoredText = sharedPreferences.getString("username", "no");
        Log.d("restored 1", restoredText);
                if (!restoredText.equals("no")) {
            Bundle bundle = new Bundle();
            bundle.putString("username", restoredText);
            Intent intent = new Intent(MainActivity.this, homescreen.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        Log.d("restored 2", restoredText);

        b1 = findViewById(R.id.b_login);
        et_usr =  findViewById(R.id.et_unme);
        et_pass = findViewById(R.id.et_upass);
        t1 =  findViewById(R.id.textView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username, password;
                username = et_usr.getText().toString();
                password = et_pass.getText().toString();

                if (username.length() > 1 && password.length() > 1) {


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("resp",response);
                            if (response.equals(" -1")) // Response codes sent via Server
                            {
                                Toast.makeText(MainActivity.this, "Invalid Details", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Welcome " + response, Toast.LENGTH_LONG).show();

//**********************************Session Management by Shared PReferences****************************************

                                sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.putString("username", username);
                                editor.commit();
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                Intent intent = new Intent(MainActivity.this, homescreen.class);
                                intent.putExtras(bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }


                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap();
                            params.put("username", username);
                            params.put("password", password);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);

                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }
}