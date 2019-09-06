package info.guardianproject.orfoxmigrate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FirefoxBookmarkExporter {

    private final static String BROWSER_DB = "browser.db";

    public static ArrayList<String> getBookmarks (Context context)
    {

        ArrayList<String> result = new ArrayList();

        File dir = context.getFilesDir();
        getBookmarks(context, dir, result);

        return result;
    }

    private static void getBookmarks (Context context, File dir, ArrayList<String> result)
    {
        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            for (File file : files) {

                Log.d("OrfoxExport", "found file: " + file.getAbsolutePath());

                if (file.isDirectory() && (!file.equals(dir)) && (!file.getName().equals("."))) {
                    getBookmarks(context, file, result);
                } else if (file.getName().equals(BROWSER_DB)) {

                    Log.d("OrfoxExport", "found BROWSER DB!: " + file.getAbsolutePath());

                    SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getPath(), null, 0);

                    Cursor c = db.rawQuery("select * from bookmarks", null);

                    while (c.moveToNext()) {
                        String name = c.getString(1);

                        String url = c.getString(2);

                        if (!TextUtils.isEmpty(url))
                            result.add(name + " " + url);
                    }

                    c.close();
                }
            }
        }
        Log.d("OrfoxExport","found bookmarks " + result.size());
    }
}
