package com.example.todo;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Task> mData;
    private View popupInputDialogView = null;
    private EditText taskName = null;
    private EditText taskDescription = null;
    private CheckBox cbstatus = null;
    private ImageView saveUserDataButton = null;
    private ImageView cancelUserDataButton = null;
    final private String UPDATE_TASK_URL = "https://mytodoappserver.000webhostapp.com/updateTask.php";
    final private String DELETE_TASK_URL = "https://mytodoappserver.000webhostapp.com/deleteTask.php";
    final private String greenColor = "#adebad";
    private final String yellowColor ="#fae596";

    public RecyclerViewAdapter(Context mContext, List<Task> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardveiw_item_task,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_id.setText(mData.get(position).getId());
        holder.tv_status.setText(mData.get(position).getStatus());
        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_description.setText(mData.get(position).getDescription());
        if((mData.get(position).getStatus().equals("1")))
            holder.cardView.setBackgroundColor(Color.parseColor(greenColor));
        else
            holder.cardView.setBackgroundColor(Color.parseColor(yellowColor));


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // DELETE button functionality
                final String id = mData.get(position).getId();
                deleteTask(id,position,v);

            }
        });
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = mData.get(position).getId();
                String name = mData.get(position).getName();
                String desc = mData.get(position).getDescription();
                String status = mData.get(position).getStatus();

              //  Toast.makeText(mContext, name+"\n"+desc, Toast.LENGTH_LONG).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                // Set title, icon, can not cancel properties.
                alertDialogBuilder.setTitle("Edit Task");
                alertDialogBuilder.setIcon(R.drawable.doc);
                alertDialogBuilder.setCancelable(true);

                //initPopupViewControls();
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);

                // Inflate the popup dialog from a layout xml file.
                popupInputDialogView = layoutInflater.inflate(R.layout.addtask__popup, null);

                // Get user input edittext and button ui controls in the popup dialog.
                taskName = (EditText) popupInputDialogView.findViewById(R.id.et_tName);
                taskDescription = (EditText) popupInputDialogView.findViewById(R.id.et_tDescription);
                cbstatus = (CheckBox)popupInputDialogView.findViewById(R.id.cb_status) ;
                saveUserDataButton = popupInputDialogView.findViewById(R.id.bt_save);
                cancelUserDataButton = popupInputDialogView.findViewById(R.id.bt_cancel);

                alertDialogBuilder.setView(popupInputDialogView);

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                taskName.setText(name);
                taskDescription.setText(desc);
                if (status.equals("1")) {
                    cbstatus.setChecked(true);
                } else
                    cbstatus.setChecked(false);


                saveUserDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String TaskName = taskName.getText().toString();
                        final String TaskDescription = taskDescription.getText().toString();
                        String TaskStatus = "0";
                        if(cbstatus.isChecked())
                            TaskStatus = "1";

                        if(TaskName.length()>=1)
                        {
                            final String finalTaskStatus = TaskStatus;
                            final String finalTaskStatus1 = TaskStatus;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_TASK_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();

                                    holder.tv_id.setText(id);
                                    holder.tv_status.setText(mData.get(position).getStatus());
                                    holder.tv_name.setText(TaskName);
                                    holder.tv_description.setText(TaskDescription);
                                    if(finalTaskStatus1 == "1")
                                    {
                                        holder.tv_status.setText("1");
                                        holder.cardView.setBackgroundColor(Color.parseColor(greenColor));
                                    }
                                    else
                                    {
                                        holder.tv_status.setText("0");
                                        holder.cardView.setBackgroundColor(Color.parseColor(yellowColor));
                                    }

                                    Task temp = new Task(TaskName,TaskDescription,finalTaskStatus,id);
                                    for (int i = 0; i < mData.size(); i++) {
                                        if(mData.get(i).getId().equals(id))
                                        {
                                            mData.set(i,temp);
                                            break;
                                        }
                                    }

                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("taskId", id);
                                    params.put("taskdescription", TaskDescription);
                                    params.put("taskname", TaskName);
                                    params.put("status", finalTaskStatus);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                            requestQueue.add(stringRequest);
                            alertDialog.cancel();
                        }
                        else
                        {
                            Toast.makeText(mContext,"Please enter Task Name",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public void reload()
    {
        notifyDataSetChanged();
    }
    public void deleteTask(final String Taskid, final int position, final View v)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_TASK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();

                    if(response.length()>3)
                    {
                        mData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mData.size());
                        Snackbar.make(v, "Task Deleted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                    else
                    {
                        Snackbar.make(v, "Unable to Delete Task", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("taskId", Taskid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_description,tv_status,tv_id;
        ImageView img_edit,img_delete;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.cv_taskName) ;
            tv_description = (TextView) itemView.findViewById(R.id.cv_taskDescription) ;
            tv_status = (TextView) itemView.findViewById(R.id.cv_status) ;
            tv_id = (TextView) itemView.findViewById(R.id.cv_taskId) ;

            img_edit = (ImageView) itemView.findViewById(R.id.bt_edit);
            img_delete = (ImageView) itemView.findViewById(R.id.bt_delete);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);


        }
    }
}
