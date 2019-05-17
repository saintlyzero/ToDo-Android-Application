package com.example.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerDummyActivity extends AppCompatActivity {
    final List<Task> lstTask = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_dummy);

        final int spanCount = 2; // 2 columns
        final int spacing = 40; // 40px
        final boolean includeEdge = true;



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.31.122:80/todo/getTask.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(homescreen.this, response, Toast.LENGTH_LONG).show();

                try {

                    Toast.makeText(RecyclerDummyActivity.this,"response: "+response,Toast.LENGTH_LONG).show();

                    JSONArray array = new JSONArray(response);
                   for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject serverData = array.getJSONObject(i);
                        String Taskid = serverData.getString("TaskId");
                        String TaskName = serverData.getString("TaskName");
                        String TaskDescription = serverData.getString("TaskDescription");
                        String status = serverData.getString("status");

                       // System.out.println(Taskid+"\n"+TaskName+"\n"+TaskDescription+"\n"+status+"\n");

                        lstTask.add(new Task(TaskName,TaskDescription,status,Taskid));

                    }

                }
                catch (Exception e) {
                    Toast.makeText(RecyclerDummyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id1);
                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(RecyclerDummyActivity.this,lstTask);
                myrv.setLayoutManager(new GridLayoutManager(RecyclerDummyActivity.this,2));
                myrv.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                myrv.setAdapter(myAdapter);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecyclerDummyActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "test1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RecyclerDummyActivity.this);
        requestQueue.add(stringRequest);



    }
}
