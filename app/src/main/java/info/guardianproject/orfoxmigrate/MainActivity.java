package info.guardianproject.orfoxmigrate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.List;

import info.guardianproject.orfox.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void goToGooglePlay(View view) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MARKET_URI + TBA_PACKAGE));
        intent.setPackage(PLAY_PACKAGE_NAME);
        startActivity(intent);
    }

    public void goToFdroid(View view) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(FDROID_APP_URI + TBA_PACKAGE));
        startActivity(intent);
    }

    public void goToApk(View view) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(TBA_DOWNLOAD_URL));
        startActivity(intent);
    }


    public static Intent getInstallIntent(String packageName, Context context) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MARKET_URI + packageName));

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);

        String foundPackageName = null;
        for (ResolveInfo r : resInfos) {
            Log.i("Install", "market: " + r.activityInfo.packageName);
            if (TextUtils.equals(r.activityInfo.packageName, FDROID_PACKAGE_NAME)
                    || TextUtils.equals(r.activityInfo.packageName, PLAY_PACKAGE_NAME)) {
                foundPackageName = r.activityInfo.packageName;
                break;
            }
        }

        if (foundPackageName == null) {
            intent.setData(Uri.parse(FDROID_APP_URI + packageName));
        } else {
            intent.setPackage(foundPackageName);
        }
        return intent;
    }

    final static String TBA_PACKAGE = "org.torproject.android.browser";
    final static String TBA_DOWNLOAD_URL = "https://torproject.org/download/tba.apk";

    final static String MARKET_URI = "market://details?id=";
    final static String FDROID_APP_URI = "https://f-droid.org/repository/browse/?fdid=";
    final static String PLAY_APP_URI = "https://play.google.com/store/apps/details?id=";
    final static String FDROID_URI = "https://f-droid.org/repository/browse/?fdfilter=info.guardianproject";
    final static String PLAY_URI = "https://play.google.com/store/apps/developer?id=The+Guardian+Project";

    private final static String FDROID_PACKAGE_NAME = "org.fdroid.fdroid";
    private final static String PLAY_PACKAGE_NAME = "com.android.vending";

}
