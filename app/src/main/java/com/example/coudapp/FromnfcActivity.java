package com.example.coudapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coudapp.helper.DialogHelper;
import com.example.coudapp.models.AppRegistry;
import com.example.coudapp.models.Setting;
import com.google.android.material.appbar.AppBarLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.example.inspiron15.my_nfc_reader.R;

public class FromnfcActivity extends AppCompatActivity {
    // private static final String url = "http://" + AppRegistry.HOST + "/sib/pmeNFC";
    private String tagId;
    private String tagData;
    private EditText idTagTxt;
    private EditText numeroTxt;
    private Toolbar toolbar ;
    private AppBarLayout scrollView;
    private EditText numCartTxt, accountTxt,nonTxt,soldTxt,prenomTex,montantTxt;
    private String pinCode;
    private Button cancelBtn;
    private Button orderBtn;
    private long save_id = 0;
    private ProgressDialog prgDialog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fromnfc);
        Log.e("toolbarrrrrrrrrrrrrr",findViewById(R.id.toolbar).toString() );

        // ().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setBackgroundDrawable();
        //Toolbar actionBar = getActionBar();
        // actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.venteEchec)));

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Commons.getFabSolde(view);
            }
        });*/
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        prgDialog = new ProgressDialog(this);
        //builder =new AlertDialog.Builder(getContext());
        this.prgDialog.setTitle(" ");
        this.prgDialog.setMessage("Veuillez patienter !!! ...");
        this.prgDialog.setCancelable(false);
        this.prgDialog.setIndeterminate(true);
        numeroTxt = (EditText) findViewById(R.id.numeroTxt);
        accountTxt = (EditText) findViewById(R.id.accountTxt);


        soldTxt= (EditText) findViewById(R.id.soldetTxt);
        scrollView= (AppBarLayout) findViewById(R.id.appBarLayout);
        montantTxt=(EditText) findViewById(R.id.montantTxt);
        nonTxt=(EditText) findViewById(R.id.nonEtudiantTxt);
        prenomTex =(EditText)findViewById(R.id.prenomEtudiantTxt);

        toolbar= (Toolbar) findViewById(R.id.toolbar);

        this.tagId = this.getIntent().getStringExtra(NFCTagActivity.ACTION_TAG_ID);
        this.tagData = this.getIntent().getStringExtra(NFCTagActivity.ACTION_DATA_READ);

        idTagTxt = (EditText) findViewById(R.id.idTagTxt);

        if (this.tagId == null) {
            scrollView.setBackground(scrollView.getContext().getDrawable(R.color.venteReussir));
            View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
            ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
            ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("INFOS DE LA  CARTE!");
            ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Cette carte est vierge!!! Veuillez l'incrire SVP!");
            DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                    FromnfcActivity.this.startActivity(activityLaucnher);
                }
            }, null).show();
        } else
            getEtudianByIdCarte();
           /*try {
                View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("l'id de la carte !");
                ((TextView) dlgView.findViewById(R.id.messageContent)).setText(tagId.toString());
                DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                        FromnfcActivity.this.startActivity(activityLaucnher);
                    }
                }, null).show();
            } catch (Exception e) {
            e.printStackTrace();
            View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
            ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
            ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERROR!");
            ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Cette carte est corrompue!!! Ou bien ne répond pas au normes de sécurités de " );
            DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                    FromnfcActivity.this.startActivity(activityLaucnher);
                }
            }, null).show();
        }*/

        numCartTxt = (EditText) findViewById(R.id.amountnfcTxt);
        //cancelBtn = (Button) findViewById(R.id.cancelBtn);
        /*
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                FromnfcActivity.this.startActivity(activityLaucnher);
            }
        });
        */
        orderBtn = (Button) findViewById(R.id.orderBtn);
        this.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                // FromnfcActivity.this.startActivity(activityLaucnher);
                // payerRepas();
                Intent intent = NFCTagActivity.creatIntentForReading(FromnfcActivity.this, "FROMNFC");
                FromnfcActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
                // finish();
            }
        });
    }

    // chercher l'etudiant avec l'id de la carte
    public void getEtudianByIdCarte(){
        final String getCode = this.getIntent().getStringExtra(NFCTagActivity.ACTION_TAG_ID);
        final String getConfCode = "testedone";
        String url= "http://50.116.97.25:8080/coud-web/api/ucad/etudiants/find";
        final JSONObject jsonobject = new JSONObject();
        try {
            JSONObject jsonobject_pawd = new JSONObject();
            //  jsonobject_pawd.put("code",getCode);
            //  jsonobject_pawd.put("second",getConfCode);
            jsonobject.put("code",getCode);

            Log.e("id de la carte",jsonobject.toString() );

        }catch (JSONException e) {
            e.printStackTrace();
        }
        prgDialog.show();
        final JsonObjectRequest jsonRequest = new JsonObjectRequest (Request.Method.POST, url+"?code="+getCode, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("RRRRRRRRRRRRRRRResponse",response.toString() );
                        try {
                            Log.e("testttt","code=="+getCode+"nummCart="+response.getString("numeroCarte").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("RRRRRRRRRRRRRRRResponse",response.toString() );
                        prgDialog.dismiss();

                        try {
                            if (getCode.equals(response.getString("numeroCarte"))) {
                                // clearFields();
                                try {
                                    //String token = response.getString("token");
                                    idTagTxt.setText(getCode.toString());
                                    nonTxt.setText(response.getString("nom"));
                                    prenomTex.setText(response.getString("prenom"));
                                    numeroTxt.setText(response.getString("telephone"));
                                    accountTxt.setText(response.getString("etablissement"));
                                    numCartTxt.setText(response.getString("numCarteEtudiant"));
                                    montantTxt.setText(response.getString("montant").toString());
                                    soldTxt.setText(response.getString("solde").toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                payerRepas();

                            } else  {
                                View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
                                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERROR!");
                                ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Carte numero "+getCode+" introuvable");
                                DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                                        FromnfcActivity.this.startActivity(activityLaucnher);
                                    }
                                }, null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prgDialog.dismiss();
                View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("Erreur!");
                ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Erreur réseau, veuillez vérifier votre connexion internet");
                Log.e("reerrrrrrrrrrrrrrrrrrr", error.toString());
                DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent activityLaucnher = new Intent(FromnfcActivity.this, MainActivity.class);
                        startActivity(activityLaucnher);
                        finish();
                    }
                }, null).show();
                error.printStackTrace();
                //Toast.makeText(CodeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }

        })
        { @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            //Log.d("codeeeeeeeeeeee", token);
            HashMap<String, String> headers=new HashMap<String, String>();
            headers.put("Content-Type","application/json;charset=utf-8");
            // headers.put("Authorization", "Bearer " + token);
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
    //fin chercher l'etudiant avec l'id de la carte
    public void payerRepas(){
        final String getCode  = idTagTxt.getText().toString();
        idTagTxt.setText(getCode.toString());
        final String getNumEtudiant = numCartTxt.getText().toString();
        final String getMontant = montantTxt.getText().toString();
        final String getTelephone = numeroTxt.getText().toString();
        AppRegistry appRegistry = AppRegistry.getInstance();
        Setting settings = appRegistry.getSettings();
        final String numResto =settings.telephone;
        //final String numResto = "786666666";
        String url1= "http://50.116.97.25:8080/coud-web/api/ucad/etudiants/payment/";
        String url= "http://50.116.97.25:8080/coud-web/api/ucad/etudiants/payment/"+"?url=http://50.116.97.25:8080/cash-ws/CashWalletServiceWS";
        final JSONObject jsonobject = new JSONObject();
        try {
            JSONObject jsonobject_resto = new JSONObject();
            jsonobject_resto.put("telephone",numResto);
            jsonobject_resto.put("montan","6000");
            jsonobject.put("codeappareil",jsonobject_resto);
            jsonobject.put("telephone",getTelephone);
            jsonobject.put("numCarteEtudiant",getNumEtudiant);
            jsonobject.put("montant",getMontant);

            Log.e("infoppppppayer",jsonobject.toString() );
        }catch (JSONException e) {
            e.printStackTrace();
        }
        prgDialog.show();
        final JsonObjectRequest jsonRequest = new JsonObjectRequest (Request.Method.POST,url1+"?numEtudiant="+getTelephone+"&numResto=786666666&montant=100", jsonobject,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("RRRRRRRRRRRRRRRR",response.toString() );
                        prgDialog.dismiss();
                        // clearFields();
                        try {
                            if (response.getString("ResponseCode").equals("0")){
                                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.venteReussir)));
                                // orderBtn.setBackground(orderBtn.getContext().getDrawable(R.color.venteReussir));
                                Drawable d = getResources().getDrawable(R.color.venteReussir);
                                // orderBtn.setBackground(orderBtn.getContext().getDrawable(venteEchec));
                                orderBtn.setBackgroundDrawable(d);
                                // scrollView.setBackground(scrollView.getContext().getDrawable(R.color.venteReussir));
                                try {
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //scrollView.setBackgro undColor(R.color.venteReussir);
                                String msg1 = response.getString("ResponseMessage");
                                View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
                                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.tick);
                                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("SUCCES!");
                                ((TextView) dlgView.findViewById(R.id.messageContent)).setText(msg1);
                                Intent intent = NFCTagActivity.creatIntentForReading(FromnfcActivity.this, "FROMNFC");
                                FromnfcActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
                                /*DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = NFCTagActivity.creatIntentForReading(FromnfcActivity.this, "FROMNFC");
                                        FromnfcActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
                                        //  finish();
                                        // Intent intent = new Intent(FromnfcActivity.this,AccuielActivity.class);
                                        // startActivity(intent);

                                    }
                                }, null).show();*/
                                return;
                            } else  {
                                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.venteEchec)));
                                //Drawable d = getResources().getDrawable(venteEchec);
                                // orderBtn.setBackground(orderBtn.getContext().getDrawable(venteEchec));
                               // orderBtn.setBackgroundDrawable(d);
                                Log.d("pppppppppppp", jsonobject.toString());
                                String msg1 = response.getString("ResponseMessage");
                                NotificationManager notificationManager = (NotificationManager)FromnfcActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                //Define sound URI
                                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.error)
                                        .setContentTitle("ERROR!")
                                        .setContentText(msg1)
                                        .setSound(soundUri); //This sets the sound to play
                                //Display notification
                                notificationManager.notify(1, mBuilder.build());
                                //Log.d("iiiiiiiiiiiiem",ieme );
                                View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
                                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERROR!");
                                ((TextView) dlgView.findViewById(R.id.messageContent)).setText(msg1);
                                DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }, null).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("erreeeeeeever", error.toString());
                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.venteEchec)));
               // Drawable d = getResources().getDrawable(venteEchec);
                // orderBtn.setBackground(orderBtn.getContext().getDrawable(venteEchec));
                //orderBtn.setBackgroundDrawable(d);
                //Define Notification Manager
                NotificationManager notificationManager = (NotificationManager)FromnfcActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                //Define sound URI
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.error)
                        .setContentTitle("ERRO!")
                        .setContentText("Erreur réseau, veuillez vérifier votre connexion internet")
                        .setSound(soundUri); //This sets the sound to play
                //Display notification
                notificationManager.notify(1, mBuilder.build());
                prgDialog.dismiss();
                View dlgView = DialogHelper.getInflatedView(FromnfcActivity.this, R.layout.dialog_message);
                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.tick);
                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERRO!");
                ((TextView) dlgView.findViewById(R.id.messageContent)).setText("EErreur réseau, veuillez vérifier votre connexion internet");
                Log.e("info indi", getMontant);
                DialogHelper.createCustomDialog(FromnfcActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, null).show();
                error.printStackTrace();
                //Toast.makeText(CodeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        { @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers=new HashMap<String, String>();
            headers.put("Content-Type","application/json;charset=utf-8");
            // headers.put("Authorization", "Bearer " + token);
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
    //fin de la methode payer le repas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    public boolean isValid() {
        if (numCartTxt.getText().toString().isEmpty()) {
            this.numCartTxt.setError("Ce champ est requis!");
            return false;
        }
        if (accountTxt.getText().toString().isEmpty()) {
            this.accountTxt.setError("Ce champ est requis!");
            return false;
        }
        if (numeroTxt.getText().toString().isEmpty()) {
            this.numeroTxt.setError("Ce champ est requis!");
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NFCTagActivity.READ_REQUEST_CODE && resultCode == NFCTagActivity.READ_REQUEST_CODE) {
            boolean status = data.getBooleanExtra(NFCTagActivity.ACTION_STATUS, false);
            if (!status)
                return;
            String actionType = data.getStringExtra(NFCTagActivity.ACTION_DATA_CUSTOM);
            String tagId = data.getStringExtra(NFCTagActivity.ACTION_TAG_ID);
            String dataRead = data.getStringExtra(NFCTagActivity.ACTION_DATA_READ);

            Intent intent = null;
            switch (actionType) {
                case "TONFC":

                    intent = new Intent(FromnfcActivity.this, FromnfcActivity.class);
                    break;
                case "FROMNFC":
                    intent = new Intent(FromnfcActivity.this, FromnfcActivity.class);
                    break;
            }
            if (intent!= null) {
                intent.putExtra(NFCTagActivity.ACTION_TAG_ID, tagId);
                intent.putExtra(NFCTagActivity.ACTION_DATA_READ, dataRead);
                FromnfcActivity.this.startActivity(intent);
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(FromnfcActivity.this,MainActivity.class);
        // startActivity(intent);
        finishAffinity();
        this.finish();
    }
}
