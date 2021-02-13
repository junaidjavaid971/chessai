package app.com.chess.ai.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharePrefData {
    private static final SharePrefData instance = new SharePrefData();
    private String FINGERPRINT = "fingerprint_enable";
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public static synchronized SharePrefData getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharePrefData(Context context) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharePrefData() {
    }



    @SuppressLint("CommitPrefEdits")
    public boolean destroyUserSession() {
        this.spEditor = this.sp.edit();
       /* this.spEditor.remove(USER_MSISDN);
        this.spEditor.remove(NAME);
        this.spEditor.remove(AMMOUNT);
        this.spEditor.remove(EMAIL);*/
        this.spEditor.apply();
        return true;
    }


}
