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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
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
        intent.setData(Uri.parse(FDROID_REPO_URL));
        startActivity(intent);
    }

    public void goToApk(View view) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(TBA_DOWNLOAD_URL));
        startActivity(intent);
    }

    public void exportBookmarks (View view) {
        exportBookmarks();
    }

    public void exportBookmarks () {

        ArrayList<String> bookmarks = FirefoxBookmarkExporter.getBookmarks(this);

        if (bookmarks.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (String bmrk : bookmarks)
                sb.append(bmrk).append("\n");

            Intent data = new Intent();
            data.setAction(Intent.ACTION_SEND);
            data.setType("text/plain");
            data.putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(data);

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(sharingIntent, "Share Bookmarks"));
        }
        else
        {
            Toast.makeText(this,"No bookmarks found",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_export) {

            exportBookmarks();
        }


        return super.onOptionsItemSelected(item);
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

    final static String TBA_PACKAGE = "org.torproject.torbrowser";
    final static String TBA_DOWNLOAD_URL = "https://www.torproject.org/download/#android";

    final static String MARKET_URI = "market://details?id=";
    final static String FDROID_APP_URI = "https://guardianproject.info/fdroid";//https://f-droid.org/repository/browse/?fdid=";
    final static String PLAY_APP_URI = "https://play.google.com/store/apps/details?id=";
    final static String FDROID_URI = "https://f-droid.org/repository/browse/?fdfilter=info.guardianproject";
    final static String PLAY_URI = "https://play.google.com/store/apps/developer?id=The+Guardian+Project";

    private final static String FDROID_PACKAGE_NAME = "org.fdroid.fdroid";
    private final static String PLAY_PACKAGE_NAME = "com.android.vending";

    private final static String FDROID_REPO_URL = "https://guardianproject.info/fdroid/";

}
