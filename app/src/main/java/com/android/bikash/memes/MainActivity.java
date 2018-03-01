package com.android.bikash.memes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.bikash.memes.services.FloatingViewService;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    ImageView img ;
    private Button btn;
    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();
        img = (ImageView)findViewById(R.id.selected_img);
        img.setImageResource(R.mipmap.ic_launcher);
        PhotoViewAttacher photoAttacher;
        photoAttacher = new PhotoViewAttacher(img);
        photoAttacher.update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(MainActivity.this, FloatingViewService.class));
    }

    void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            startService(new Intent(MainActivity.this, FloatingViewService.class));

        }
    }
    void openDialog() {
        @SuppressLint("RestrictedApi")
        Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        dialog.setContentView(R.layout.option_dialog);
        dialog.show();
    }
}
