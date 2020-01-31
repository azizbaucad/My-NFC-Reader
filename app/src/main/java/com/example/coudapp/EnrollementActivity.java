package com.example.coudapp;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import com.example.inspiron15.my_nfc_reader.R;

public class EnrollementActivity extends AppCompatActivity implements View.OnClickListener{
    // private static final String url = "http://" + AppRegistry.HOST + "/sib/pmeNFC";
    private String tagId;
    private String tagData;
    //private Toolbar toolbar ;
    private AppBarLayout scrollView;
    private EditText idcardTxt,nonTxt,prenomTxt,emailTxt,numCartetTxt;
    private EditText numeroTxt,etablissementTxt ;
    // private Spinner etablissementTx;
    private String nomEtablissement;

    private Button inscriptioBtn;
    private Button searchBtn;
    private long save_id = 0;
    private ProgressDialog prgDialog;
    String etablissementNom[] = {"ETABLISSEMENT","FASTEF","ENSEPT","ESEA","ESP"};
    ArrayAdapter<String> adapterEtablissementNom;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
//        Log.e("toolbarrrrrrrrrrrrrr",findViewById(R.id.toolbar).toString() );

        prgDialog = new ProgressDialog(this);
        //builder =new AlertDialog.Builder(getContext());
        this.prgDialog.setTitle(" ");
        this.prgDialog.setMessage("Veuillez patienter !!! ...");
        this.prgDialog.setCancelable(false);
        this.prgDialog.setIndeterminate(true);
        scrollView= (AppBarLayout) findViewById(R.id.appBarLayout);
        idcardTxt=(EditText) findViewById(R.id.idTagTxtinscrip);
        nonTxt=(EditText) findViewById(R.id.nonEtudiantTxtinscrip);
        numeroTxt = (EditText) findViewById(R.id.numTelTxtinscrip);
        prenomTxt =(EditText)findViewById(R.id.prenomEtudiantTxtinscrip);
        numCartetTxt= (EditText) findViewById(R.id.numCarteEtuTxtinscrip);
        etablissementTxt= (EditText) findViewById(R.id.etablisementTx);

        // etablissementTx=(Spinner)findViewById(R.id.etablisementTx);

        emailTxt=(EditText) findViewById(R.id.emailTxtinscrip);
        //toolbar= (Toolbar) findViewById(R.id.toolbar);
        this.tagId = this.getIntent().getStringExtra(NFCTagActivity.ACTION_TAG_ID);
        this.tagData = this.getIntent().getStringExtra(NFCTagActivity.ACTION_DATA_READ);
        idcardTxt.setText(tagId);
        inscriptioBtn = (Button) findViewById(R.id.inscriptionBnt);
        searchBtn=(Button) findViewById(R.id.searchBnt);

     /*   adapterEtablissementNom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,etablissementNom);
        adapterEtablissementNom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etablissementTx.setAdapter(adapterEtablissementNom);
        etablissementTx.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                nomEtablissement = adapter.getItemAtPosition(position).toString();
               //  Toast.makeText(getApplicationContext(), nomEtablissement, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        if (this.tagId == null) {
            scrollView.setBackground(scrollView.getContext().getDrawable(R.color.venteReussir));
            View dlgView = DialogHelper.getInflatedView(EnrollementActivity.this, R.layout.dialog_message);
            ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
            ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("INFOS DE LA  CARTE!");
            ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Cette carte est vierge!!! Veuillez l'incrire SVP!");
            DialogHelper.createCustomDialog(EnrollementActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // Intent activityLaucnher = new Intent(EnrollementActivity.this, MainActivity.class);
                    // EnrollementActivity.this.startActivity(activityLaucnher);
                    // inscriptioBtn = (Button) findViewById(R.id.inscriptionBnt);

                }
            }, null).show();

        } else
            Toast.makeText(getApplicationContext(), "Bienvenue", Toast.LENGTH_SHORT).show();
        //  getEtudianByIdCarte();
        // inscriptioBtn = (Button) findViewById(R.id.inscriptionBnt);
        inscriptioBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        etablissementTxt.setText("FASTEF");
      /*  this.inscriptioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // enrollerEtudiant();
               // Intent intent = NFCTagActivity.creatIntentForReading(EnrollementActivity.this, "FROMNFC");
             //  EnrollementActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
            }
        });*/
    }



    public void enrollerEtudiant(){

        final String getIdCard  = idcardTxt.getText().toString();
        final String getNom = nonTxt.getText().toString();
        final String getPrenom = prenomTxt.getText().toString();
        final String getEmail = emailTxt.getText().toString();
        final String getEtablisement = etablissementTxt.getText().toString();
        final String getTelephone = numeroTxt.getText().toString();
        final String getNumCartd = numCartetTxt.getText().toString();

        //final String getNumCartd = "0000000";
        final int getMontant = 0;
        final int getSode = 0;

        AppRegistry appRegistry = AppRegistry.getInstance();
        Setting settings = appRegistry.getSettings();
        final String numResto =settings.telephone;
        //final String numResto = "786666666";
        String url1= "http://50.116.97.25:8080/coud-web/api/ucad/etudiants/payment/";
        // http://localhost:8080/coud-web/api/ucad/etudiants/add
        String url= "http://50.116.97.25:8080/coud-web/api/ucad/etudiants/add";
        final JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("numeroCarte",getIdCard);
            jsonobject.put("nom",getNom);
            jsonobject.put("prenom",getPrenom);
            jsonobject.put("telephone",getTelephone);
            jsonobject.put("etablissement",getEtablisement);
            jsonobject.put("email",getEmail);
            jsonobject.put("numCarteEtudiant",getNumCartd);
            jsonobject.put("montant",getMontant);
            jsonobject.put("solde",getSode);
            Log.e("send etudiant",jsonobject.toString() );

        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("send etudiant",jsonobject.toString() );
        Log.e(" url send etudiant",url );
        prgDialog.show();
        final JsonObjectRequest jsonRequest = new JsonObjectRequest (Request.Method.POST,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("RRRRRRRRRRRRRRRR",response.toString() );
                        prgDialog.dismiss();
                        // clearFields();
                        try {
                            if (response.getString("ResponseCode").equals("0")){
                                // toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.venteReussir)));
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
                                //scrollView.setBackgroundColor(R.color.venteReussir);
                                String msg1 = response.getString("ResponseMessage");
                                View dlgView = DialogHelper.getInflatedView(EnrollementActivity.this, R.layout.dialog_message);
                                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.tick);
                                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("SUCCES!");
                                ((TextView) dlgView.findViewById(R.id.messageContent)).setText(msg1);
                                DialogHelper.createCustomDialog(EnrollementActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = NFCTagActivity.creatIntentForReading(EnrollementActivity.this, "FROMNFC");
                                        EnrollementActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);

                                    }
                                }, null).show();
                                return;
                            } else  {
                                //  toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.venteEchec)));
                                //  orderBtn.setBackground(orderBtn.getContext().getDrawable(venteEchec));
                                Log.d("pppppppppppp", jsonobject.toString());
                                String msg1 = response.getString("ResponseMessage");
                                NotificationManager notificationManager = (NotificationManager) EnrollementActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                //Define sound URI
                                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.error)
                                        .setContentTitle("ERROR!")
                                        .setContentText(msg1)
                                        .setSound(soundUri);
                                //This sets the sound to play
                                //Display notification
                                notificationManager.notify(1, mBuilder.build());
                                //Log.d("iiiiiiiiiiiiem",ieme );
                                View dlgView = DialogHelper.getInflatedView(EnrollementActivity.this, R.layout.dialog_message);
                                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERROR!");
                                ((TextView) dlgView.findViewById(R.id.messageContent)).setText(msg1);
                                DialogHelper.createCustomDialog(EnrollementActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = NFCTagActivity.creatIntentForReading(EnrollementActivity.this, "FROMNFC");
                                        EnrollementActivity.this.startActivityForResult(intent, NFCTagActivity.READ_REQUEST_CODE);
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

                NotificationManager notificationManager = (NotificationManager) EnrollementActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.error)
                        .setContentTitle("ERRO!")
                        .setContentText("Erreur réseau, veuillez vérifier votre connexion internet")
                        .setSound(soundUri); //This sets the sound to play
                //Display notification
                notificationManager.notify(1, mBuilder.build());
                prgDialog.dismiss();
                View dlgView = DialogHelper.getInflatedView(EnrollementActivity.this, R.layout.dialog_message);
                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.tick);
                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERRO!");
                ((TextView) dlgView.findViewById(R.id.messageContent)).setText("Erreur réseau, veuillez vérifier votre connexion internet");
                Log.e("info indi", getNom);
                DialogHelper.createCustomDialog(EnrollementActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
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
    public static void setRetryPolicy(JsonObjectRequest postRequest, Object context) {
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000*1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue((Context) context).add(postRequest);
    }

    public void getEtudiantByNum(){

        final String getIdCardR  = null;
        final String getNomR = null;
        final String getPrenomR = null;
        final String getEmailR = null;
        final String getEtablisementR = null ;
        final String getTelephoneR = null;
        final String getNumCartdR = numCartetTxt.getText().toString();
        //final String getNumCartd = "0000000";
        final int getMontant = 0;
        final int getSode = 0;

        AppRegistry appRegistry = AppRegistry.getInstance();
        Setting settings = appRegistry.getSettings();


        //String url= getString(R.string.my_url)+"api/resetaccess/get";
        String url= "http://50.116.97.25:8080/coud-web/api/ucad/etudiants/findEtudiantByNumCarteEtud";
        // ?username=786666666&password=passer";
        Log.e("logurlllllllllllll",url);

        final JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("numeroCarte",getIdCardR);
            jsonobject.put("nom",getNomR);
            jsonobject.put("prenom",getPrenomR);
            jsonobject.put("telephone",getTelephoneR);
            jsonobject.put("etablissement",getEtablisementR);
            jsonobject.put("email",getEmailR);
            jsonobject.put("numCarteEtudiant",getNumCartdR);
            jsonobject.put("montant",getMontant);
            jsonobject.put("solde",getSode);
            Log.e("Recherche Etudiant",jsonobject.toString() );
        }catch (JSONException e) {
            e.printStackTrace();
        }
        prgDialog.show();
        // Log.e("sendData",jsonobject.toString() );
        Log.e("Recherche Etudiant",jsonobject.toString() );
        final JsonObjectRequest jsonRequest = new JsonObjectRequest (Request.Method.POST, url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("loginnnnnnnnnnnnnnnn",response.toString());

                        prgDialog.dismiss();
                        //  clearFields();
                        try {
                            String telephone = response.getString("telephone");
                            //String tokenreferesh = response.getString("refresh_token");
                            if (response.getString("numeroCarte").equals("null")) {
                                Toast.makeText(getApplicationContext(), "continue la saisie", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    nonTxt.setText(response.getString("nom"));
                                    prenomTxt.setText(response.getString("prenom"));
                                    numeroTxt.setText(response.getString("telephone"));
                                    etablissementTxt.setText(response.getString("etablissement"));
                                    emailTxt.setText(response.getString("email"));
                                    numCartetTxt.setText(response.getString("numCarteEtudiant"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            Setting.updateToken(getApplication(), telephone);
                            return;

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sendData erreur",jsonobject.toString() );
                prgDialog.dismiss();
                // clearFields();
                String mgerr ="";
                View dlgView = DialogHelper.getInflatedView(EnrollementActivity.this, R.layout.dialog_message);
                ((ImageView) dlgView.findViewById(R.id.messageImage)).setImageResource(R.drawable.error);
                ((TextView) dlgView.findViewById(R.id.messageTitle)).setText("ERROR!");
                if(error instanceof ServerError){
                    mgerr = new String(error.networkResponse.data);
                }else if(error instanceof NetworkError){
                    mgerr = "Erreur réseau, veuillez vérifier votre connexion internet";
                }else {
                    mgerr = "Erreur réseau, veuillez vérifier votre connexion internet";
                }
                ((TextView) dlgView.findViewById(R.id.messageContent)).setText(mgerr);

                DialogHelper.createCustomDialog(EnrollementActivity.this, dlgView, "OK", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public boolean isValid() {
        boolean valid = true;
        String email = this.emailTxt.getText().toString();
        String prenom = this.prenomTxt.getText().toString();
        String nom = this.nonTxt.getText().toString();
        String telephone = numeroTxt.getText().toString();

        if (prenom.isEmpty()) {
            this.prenomTxt.setError("Ce champ est requis!");
            valid = false;
        } else {
            this.prenomTxt.setError(null);
        }
        if (nom.isEmpty()) {
            this.nonTxt.setError("Ce champ est requis!");
            valid = false;
        } else {
            this.nonTxt.setError(null);
        }
        if (telephone.isEmpty()) {
            this.numeroTxt.setError("Ce champ est requis!");
            valid = false;
        } else {
            this.numeroTxt.setError(null);
        }
        if (!telephone.matches("[0-9]{9,9}$")) {
            this.numeroTxt.setError("Ce champ est de 9 chiffres");
            valid = false;
        } else {
            this.numeroTxt.setError(null);
        }

        if (!email.isEmpty()&&!email.matches("[a-zA-Z0-9._-]+@[a-z.-]+\\.+[a-z]+")) {
            this.emailTxt.setError("Format non valide!");
            valid = false;
            // return false;
        } else {
            this.emailTxt.setError(null);
        }
        return valid;
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

                    intent = new Intent(EnrollementActivity.this, EnrollementActivity.class);
                    break;
                case "FROMNFC":
                    intent = new Intent(EnrollementActivity.this, EnrollementActivity.class);
                    break;
            }
            if (intent!= null) {
                intent.putExtra(NFCTagActivity.ACTION_TAG_ID, tagId);
                intent.putExtra(NFCTagActivity.ACTION_DATA_READ, dataRead);
                EnrollementActivity.this.startActivity(intent);
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

    @Override
    public void onClick(View v) {
        if (v==inscriptioBtn){
            //enrollerEtudiant();
            if (isValid()){
                enrollerEtudiant();
            }
        }else
        if (v==searchBtn){
            getEtudiantByNum();
        }


    }
}
