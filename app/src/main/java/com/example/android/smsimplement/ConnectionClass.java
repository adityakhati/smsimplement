package com.example.android.smsimplement;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ABHI on 9/20/2016.
 */
public class ConnectionClass {
    String classs = "com.mysql.jdbc.Driver";
    String url;

    //String url = "jdbc:mysql://192.168.0.101/shop";
    //String url = "jdbc:mysql://192.168.43.18/shop";

    String un = "root";
    String password = "";

    ConnectionClass(String ip){
        url="jdbc:mysql://"+ip+"/shop";

    }

//    String url = "jdbc:mysql://https://electronic-mandi.000webhostapp.com//id12056698_shop";
//    String un = "id12056698_root";
//    String password = "12345";



    @SuppressLint("NewApi")
    public Connection CONN() {
        Log.d("IP address",url);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);

            conn = DriverManager.getConnection(url, un, password);


            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
