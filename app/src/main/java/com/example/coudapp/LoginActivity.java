package com.example.coudapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coudapp.helper.DialogHelper;
import com.example.coudapp.models.AppRegistry;
import com.example.coudapp.models.Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bnt_con;
    private EditText emailText;
    private EditText passwordText;
    private TextView codeOblTxt;
    private static final String KEY_MOT_DE_PASSE="codeinitial";
    private static final String KEY_EMAIL="email";

    private ProgressDialog prgDialog;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        //  getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppRegistry registry = AppRegistry.getInstance();
        registry.init(this);
        emailText=(EditText) findViewById(R.id.emailOubTxt);
        passwordText= (EditText) findViewById(R.id.passworOubTxt);
        bnt_con=(Button) findViewById(R.id.loginOublBtn);
        bnt_con.setOnClickListener(this);
        builder =new AlertDialog.Builder(LoginActivity.this);
        prgDialog = new ProgressDialog(this);
        this.prgDialog.setTitle("Connexion");
        this.prgDialog.setMessage("Veuillez patienter !!! ...");
        this.prgDialog.setCancelable(false);
        this.prgDialog.setIndeterminate(true);
    }

    public void getModepasse(){

        AppRegistry appRegistry = AppRegistry.getInstance();
        Setting settings = appRegistry.getSettings();
        final String telephone =settings.telephone;
        final String getEmail = emailText.getText().toString();
        final String getMotedepasse = passwordText.getText().toString();
        //String url= getString(R.string.my_url)+"api/resetaccess/get";
        String url= "http://50.116.97.25:8080/coud-web/api/ucad/restaurants/login";
        // ?username=786666666&password=passer";
        Log.e("logurlllllllllllll",url);
        Log.e("phonerestooooo",telephone);

        final JSONObject jsonobject = new JSONObject();
        try {

            jsonobject.put(KEY_MOT_DE_PASSE,getMotedepasse);
            jsonobject.put(KEY_EMAIL,getEmail);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        prgDialog.show();
        Log.e("sendData",jsonobject.toString() );
        final JsonObjectRequest jsonRequest = new JsonObjectRequest (Request.Method.POST, url+"?username="+getEmail+"&password="+getMotedepasse, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("login",response.toString());
                        prgDialog.dismiss();
                        clearFields();
                        try {
                            String telephone = response.getString("telephone");
                            //String tokenreferesh = response.getString("refresh_token");
                            if (response.getString("nom").equals("null")) {
                                View dlgView = DialogHelper.getInflatedView(LoginActivity.this, R.layout.dialog_message);
                                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("Erreur!");
                                ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Login ou mot de passe incorrect");
                                DialogHelper.createCustomDialog(LoginActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        //$  finish();
                                    }
                                }, null).show();
                            }else if (response.getString("nom").equals("admin")){
                                Intent intent = NFCINSTActivity.creatIntentForReadin(LoginActivity.this, "FROMNFC");
                                LoginActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
                                        }
                                        else {
                                        // Toast.makeText(LoginActivity.this,"KO COUD ", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        }
        // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
        Setting.updateToken(getApplication(), telephone);
        return;

        } catch (Exception e) {
        builder.setTitle("Title");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(e.getMessage());
        displayAlert("input_error");
        e.printStackTrace();
        }
        }
        }, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
        Log.e("sendData erreur",jsonobject.toString() );
        prgDialog.dismiss();
        clearFields();
        String mgerr ="";
        View dlgView = DialogHelper.getInflatedView(LoginActivity.this, R.layout.dialog_message);
        ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
        ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("Erreur!");
        if(error instanceof ServerError){
        mgerr = new String(error.networkResponse.data);
        }else if(error instanceof NetworkError){
        mgerr = "Erreur réseau, veuillez vérifier votre connexion internet";
        }else {
        mgerr = "Erreur réseau, veuillez vérifier votre connexion internet";
        }
        ((TextView) dlgView.findViewById(R.id.messageContent)).setText(mgerr);

        DialogHelper.createCustomDialog(LoginActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        // Intent intent = NFCINSTActivity.creatIntentForReadin(LoginActivity.this, "FROMNFC");
        //  LoginActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
        }
        }, null).show();
        error.printStackTrace();
        //  Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_LONG).show();
        }
        })
        { @Override
public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers=new HashMap<String, String>();
        headers.put("Content-Type","application/json;charset=utf-8");
        return headers;
        }
@Override
protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        int mStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
        }
        };
        // Volley.newRequestQueue(this).add(jsonRequest);
        setRetryPolicy(jsonRequest,this);
        }
public static void setRetryPolicy(JsonObjectRequest postRequest, Object context) {
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000*1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue((Context) context).add(postRequest);

        }

public void displayAlert(String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
        }
        });
        if (code.equals("input_error")) {
        emailText.setText("");
        passwordText.setText("");
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        }
public void clearFields() {
        this.emailText.setText("");
        this.passwordText.setText("");
        }

@Override
protected void onResume() {
        super.onResume();
        AppRegistry.getInstance().CheckInactivity(this);
        }

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NFCTagActivity.READ_REQUEST_CODE && resultCode == NFCTagActivity.READ_REQUEST_CODE) {
        boolean status = data.getBooleanExtra(NFCINSTActivity.ACTION_STATUS, false);
        if (!status)
        return;
        String actionType = data.getStringExtra(NFCTagActivity.ACTION_DATA_CUSTOM);
        String tagId = data.getStringExtra(NFCTagActivity.ACTION_TAG_ID);
        String dataRead = data.getStringExtra(NFCTagActivity.ACTION_DATA_READ);
        Intent intent = null;
        switch (actionType) {
        case "TONFC":
        intent = new Intent(LoginActivity.this, EnrollementActivity.class);
        break;
        case "FROMNFC":
        intent = new Intent(LoginActivity.this, EnrollementActivity.class);
        break;
        }
        if (intent!= null) {
        intent.putExtra(NFCTagActivity.ACTION_TAG_ID, tagId);
        intent.putExtra(NFCTagActivity.ACTION_DATA_READ, dataRead);
        LoginActivity.this.startActivity(intent);
        }

        }
        }

@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
private boolean isNotnull() {
        boolean valid = true;
        String email = this.emailText.getText().toString();
        String nom = this.passwordText.getText().toString();

        if (email.isEmpty()) {
        this.emailText.setError("Ce champ est requis!");
        valid = false;
        } else {
        this.emailText.setError(null);
        }

        if (!email.matches("[0-9]{9,9}$")) {
        this.emailText.setError("Ce champ est de 9 chiffres");
        valid = false;
        } else {
        this.emailText.setError(null);
        }
        if (nom.isEmpty()) {
        this.passwordText.setError("Ce champ est requis!");
        valid = false;
        } else {
        this.passwordText.setError(null);
        }

        return valid;
        }


@Override
public void onClick(View v) {
        if (v==bnt_con){
        if (isNotnull()){
        getModepasse();
        //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        //startActivity(intent);
        }
        }

        }
        }
