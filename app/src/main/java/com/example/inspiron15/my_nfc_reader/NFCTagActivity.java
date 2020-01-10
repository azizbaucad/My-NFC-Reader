package com.example.inspiron15.my_nfc_reader;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;


        import com.example.inspiron15.my_nfc_reader.nfc.management.NFCManager;

        import com.example.inspiron15.my_nfc_reader.nfc.management.NFCWriteException;

//import com.nfc.management.NFCManager;
//import com.nfc.management.NFCWriteException;

public class NFCTagActivity extends AppCompatActivity implements NFCManager.TagReadListener, NFCManager.TagWriteListener, NFCManager.TagWriteErrorListener {
    public static final String DATA_TO_WRITE_KEY = "DATA_TO_WRITE";
    public static final int CANCEL_REQUEST_CODE =0;
    public static final int READ_REQUEST_CODE = 1;
    public static final int WRITE_REQUEST_CODE = 2;
    public static final String ACTION_STATUS = "ACTION_STATUS";
    public static final String ACTION_TAG_ID = "ACTION_TAG_ID";
    public static final String ACTION_DATA_READ = "ACTION_DATA_READ";
    public static final String ACTION_DATA_CUSTOM = "ACTION_DATA_CUSTOM";


    private String dataToWrite;
    private String dataCustom;
    private TextView promptText;
    private ImageView promptIcon;
    private Handler timer;
    private NFCManager nfcManager;
    private android.widget.LinearLayout LinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_read_write);

        this.dataToWrite = this.getIntent().getStringExtra(NFCTagActivity.DATA_TO_WRITE_KEY);
        this.dataCustom = this.getIntent().getStringExtra(NFCTagActivity.ACTION_DATA_CUSTOM);

        this.promptText = (TextView) this.findViewById(R.id.promptText);
        this.promptIcon = (ImageView) this.findViewById(R.id.promptIcon);

        this.timer =new Handler();
        this.nfcManager = new NFCManager(this);
        this.nfcManager.onActivityCreate();

        this.nfcManager.setOnTagReadListener(this);
        this.nfcManager.setOnTagWriteListener(this);
        this.nfcManager.setOnTagWriteErrorListener(this);

        if(!this.isReadingMode()) {
            this.nfcManager.writeText(this.dataToWrite);
        }
    }

    @Override
    public void onTagRead(String tagId, String tagRead){
        if(!this.isReadingMode())
            return;
        final String ftagId = tagId;
        final String ftagRead = tagRead;
        this.changeStatus(true);
        this.timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.putExtra(NFCTagActivity.ACTION_STATUS, true);
                intent.putExtra(NFCTagActivity.ACTION_TAG_ID, ftagId);
                intent.putExtra(NFCTagActivity.ACTION_DATA_READ, ftagRead);
                intent.putExtra(NFCTagActivity.ACTION_DATA_CUSTOM, NFCTagActivity.this.dataCustom);
                NFCTagActivity.this.setResult(NFCTagActivity.READ_REQUEST_CODE, intent);
                NFCTagActivity.this.finish();
            }
        }, 3000);
    }

    @Override
    public void onTagWritten() {
        this.changeStatus(true);
        this.timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.putExtra(NFCTagActivity.ACTION_STATUS, true);
                intent.putExtra(NFCTagActivity.ACTION_DATA_CUSTOM, NFCTagActivity.this.dataCustom);
                NFCTagActivity.this.setResult(NFCTagActivity.WRITE_REQUEST_CODE, intent);
                NFCTagActivity.this.finish();
            }
        }, 3000);
    }

    @Override
    public void onTagWriteError(NFCWriteException exception) {
        this.changeStatus(false);
        this.timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.putExtra(NFCTagActivity.ACTION_STATUS, false);
                intent.putExtra(NFCTagActivity.ACTION_DATA_CUSTOM, NFCTagActivity.this.dataCustom);
                NFCTagActivity.this.setResult(NFCTagActivity.WRITE_REQUEST_CODE, intent);
                NFCTagActivity.this.finish();
            }
        }, 3000);
    }

    private void changeStatus(boolean isOperationOk){
        this.promptText.setText("");
        if(isOperationOk) {
            this.nfcManager.setOnTagReadListener(null);
            this.promptIcon.setImageResource(R.drawable.ic_check);
        }else {
            this.promptIcon.setImageResource(R.drawable.ic_cross);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.nfcManager.onActivityResume();
    }

    @Override
    protected void onPause() {
        this.nfcManager.onActivityPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra(NFCTagActivity.ACTION_STATUS, false);
        intent.putExtra(NFCTagActivity.ACTION_DATA_CUSTOM, NFCTagActivity.this.dataCustom);
        this.setResult(NFCTagActivity.CANCEL_REQUEST_CODE, intent);
        // startActivity(intent);
        //Intent intent = new Intent(NFCTagActivity.this,NFCTagActivity.class);
       // startActivity(intent);
       // finish();

    }

    @Override
    public void onNewIntent(Intent intent){
        this.nfcManager.onActivityNewIntent(intent);
    }

    private boolean
    isReadingMode(){
        return (this.dataToWrite == null);
    }


    public static Intent createIntentForWriting(Context context, String data, String customData){
        Intent intent =new Intent(context, NFCTagActivity.class);
        intent.putExtra(NFCTagActivity.DATA_TO_WRITE_KEY, data);
        intent.putExtra(NFCTagActivity.ACTION_DATA_CUSTOM, customData);
        return intent;
    }

    public static Intent creatIntentForReading(Context context, String customData){
        Intent intent =new Intent(context, NFCTagActivity.class);
        intent.putExtra(NFCTagActivity.ACTION_DATA_CUSTOM, customData);
        return intent;
    }

}

