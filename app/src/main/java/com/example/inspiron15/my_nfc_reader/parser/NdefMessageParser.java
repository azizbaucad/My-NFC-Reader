package com.example.inspiron15.my_nfc_reader.parser;

/**
 * Created by Inspiron 15 on 07/10/2019.
 */

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

//import com.example.inspiron15.my_nfc_reader.R;
//import com.example.inspiron15.my_nfc_reader.model.History;
import com.example.inspiron15.my_nfc_reader.record.ParsedNdefRecord;
import com.example.inspiron15.my_nfc_reader.record.SmartPoster;
import com.example.inspiron15.my_nfc_reader.record.TextRecord;
import com.example.inspiron15.my_nfc_reader.record.UriRecord;
//import com.example.inspiron15.my_nfc_reader.utils.NFCReaderApp;

import java.util.ArrayList;
import java.util.List;


public class NdefMessageParser {

    private NdefMessageParser() {
    }

    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();

        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
                elements.add(new ParsedNdefRecord() {
                    public String str() {
                        return new String(record.getPayload());
                    }
                });
            }
        }

        return elements;
    }
}
