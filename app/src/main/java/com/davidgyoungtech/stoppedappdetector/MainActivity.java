package com.davidgyoungtech.stoppedappdetector;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<String> mStoppedPackages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Stopped App Detector");
        updateStoppedAppDisplay(true);
    }


    private List<String> getStoppedPackages() {
        ArrayList<String> stoppedPackages = new ArrayList<String>();
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            boolean appStopped = (packageInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0;
            if (appStopped) {
                String description = packageInfo.packageName;
                stoppedPackages.add(description);
            }
        }
        return stoppedPackages;
    }

    private void updateStoppedAppDisplay(boolean firstRun) {
        TextView stoppedAppEditText = (TextView) findViewById(R.id.stopped_app_textview);
        stoppedAppEditText.setText("Processing...");
        boolean foundOne = false;
        List<String> newStoppedPackages = getStoppedPackages();
        Collections.sort(newStoppedPackages);

        for (String stoppedPackage : newStoppedPackages) {
            if (!foundOne) {
                stoppedAppEditText.setText(stoppedPackage);
            }
            else {
                stoppedAppEditText.setText(stoppedAppEditText.getText()+"\n"+stoppedPackage);
            }
            foundOne = true;
        }
        if (!foundOne) {
            stoppedAppEditText.setText("None found.");
        }

        List<String> oldStoppedPackages = mStoppedPackages;
        mStoppedPackages = newStoppedPackages;
        if (!firstRun) {
            ArrayList<String> newlyStoppedPackages = new ArrayList<String>();
            Collections.copy(newStoppedPackages, newlyStoppedPackages);
            newlyStoppedPackages.removeAll(oldStoppedPackages);
            oldStoppedPackages.removeAll(mStoppedPackages);
            boolean changed = false;
            if (newlyStoppedPackages.size() > 0) {
                changed = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New Stopped Packages")
                        .setMessage(TextUtils.join(", ",newlyStoppedPackages))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();

            }
            if (oldStoppedPackages.size() > 0) {
                changed = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Packages no longer stopped")
                        .setMessage(TextUtils.join(", ",oldStoppedPackages))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();

            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No change in stopped apps")
                        .setMessage("")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        }
    }

    public void refresh(View caller) {
        updateStoppedAppDisplay(false);
    }
}
