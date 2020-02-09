package com.example.android.smsimplement;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText ipedtv,email;
    String[] arr;
    String msgstr1,phoneno,message;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView tv_sms = findViewById(R.id.sms);
        findViewById(R.id.btn_save_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Changeip.class));
            }
        });


        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });

        progressDialog=new ProgressDialog(this);

        Intent sms_intent=getIntent();
        Bundle b=sms_intent.getExtras();
        TextView tv=(TextView)findViewById(R.id.textview);

        if(b!=null) {
            // Display SMS in the TextView
            msgstr1 = b.getString("sms_str");
            phoneno=msgstr1.substring(14,24);
            Log.d("Sms", msgstr1.substring(35));
            message=msgstr1.substring(35);
            Log.d("length",Integer.toString(message.length()));

            //tv.setText(b.getString("sms_str"));
            tv.setText(msgstr1);

//            String str = "Product Name;Product Description;Price;old price;category;quantity;vendor id";
            int c=countChar(message, ';');
            Log.d("count",Integer.toString(c));

            if(c!=6)
            {
             Doregister d=new Doregister();
             d.sendSMS(phoneno,"Invalid SMS\nCorrect Your SMS");
            }
            else {
                arr = message.split(";", 7);


                if (arr[0].equals("register")) {

                }


                Log.d("main", arr[6]);
                Log.d("main", arr[5]);
                Log.d("main", arr[4]);
                Log.d("main", arr[3]);
                Log.d("main", arr[2]);
                Log.d("main", arr[1]);
                Log.d("main", arr[0]);


                if (isNetworkConnected()) {
                    SharedPreferences prefs = getSharedPreferences("ip", MODE_PRIVATE);
                    String ip = prefs.getString("ip", "No name defined");
                    Log.d("Ip start",ip);
                    Log.d("true", "internet connected");
                    connectionClass = new ConnectionClass(ip);
                    Doregister doregister = new Doregister();
                    doregister.execute("");
                }
            }

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public int countChar(String str, char c)
    {
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }

        return count;
    }



    public class Doregister extends AsyncTask<String, String, String> {


        String namestr = phoneno;
        String emailstr = message;



        String z = "";
        boolean isSuccess = false;


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (namestr.trim().equals("") || emailstr.trim().equals(""))
                z = "Please enter all fields....";
            else {
                try {
                    SharedPreferences prefs = getSharedPreferences("ip", MODE_PRIVATE);
                    String ip = prefs.getString("ip", "No name defined");
                    Log.d("Ip start",ip);

                    Connection con = connectionClass.CONN();

                        //String query = "insert into demoregister values('" + namestr + "','" + emailstr + "')";
//                        String query = "insert into demoregister values('" + arr[0] + "','" + arr[1] +
//                    "','" + arr[2] + "','" + arr[3] + "','" + arr[4] + "','" + arr[5] + "')";

                    //String q="SELECT * FROM vendors WHERE phone="+phoneno;

                    ResultSet rs;
                    try {
                        String q="SELECT * FROM vendors where phone="+phoneno+";";
                        Statement s = con.createStatement();
                         rs = s.executeQuery(q);
                        rs.next();

                        Log.d("result",rs.getString(1));
                    }catch (Exception ex) {
                        Log.d("Empty", "empty");
                        sendSMS(phoneno,"You are not a authenticated user");
                        z="Not authenticated";
                        isSuccess=false;
                        return z;
                    }





                   String query="INSERT INTO products(time,time_update,shop_categorie,quantity,procurement,position,vendor_id) " +
                            "VALUES (0,1,4,'"+ arr[5] +"',0,0," +arr[6]+");";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);



                    String query1="SELECT max(id) FROM products";

                    Statement stmt1 = con.createStatement();
                    ResultSet r = stmt1.executeQuery(query1);
                    r.next();
                    Log.d("result",r.getString(1));

                    String query2="UPDATE products SET url = '"+arr[0]+"_"+r.getString(1)+"' WHERE id="+Integer.parseInt(r.getString(1));

                    Statement stmt2 = con.createStatement();
                    stmt2.executeUpdate(query2);

                    String query3=" INSERT INTO products_translations(title,price,old_price,abbr,for_id) " +
                            "VALUES ('"+arr[0]+"','"+arr[2]+"','"+arr[3]+"','en',"+Integer.parseInt(r.getString(1))+");";
                    Statement stmt3 = con.createStatement();
                    stmt3.executeUpdate(query3);


                    z = "Register successfull";
                    isSuccess = true;



                } catch (Exception ex) {
                    Log.d("Error",ex.toString());
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();


            if (isSuccess) {
                //startActivity(new Intent(MainActivity.this, Main2Activity.class));
                String phoneNo = phoneno;
                sendSMS(phoneNo, "Thank you! \n Your Product has been added successfully");

            }

            progressDialog.hide();
        }

        private void sendSMS(String phoneNumber, String message)
        {
            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0,
                    new Intent(MainActivity.this, Main2Activity.class), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, pi, null);
        }
    }
}