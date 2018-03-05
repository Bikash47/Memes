package com.android.bikash.memes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bikash.memes.util.ScreenshotUtils;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DragActivity extends AppCompatActivity {
    private ImageView img, customImage;
    private ViewGroup rootLayout;
    private int _xDelta;
    private int _yDelta;
    private RelativeLayout mainView, dialogLayout, mainView2,addView,nextSet;
    private boolean isDaialogShow = false, is2ndTextClick = false, isAdded = false;
    private TextView funnyText, funnyText2, textSizeSeek;
    private SeekBar seekbar;
    private EditText changeText;
    private Switch aSwitch;
    private LinearLayout clrChoos, backClrChoos;
    private ImageButton closeBtn, addBtn, allSet;
    private Bitmap b = null;
    private static final int PERMISSION_REQUEST_CODE = 1;
    final int PIC_CROP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
// Show status bar
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_drag);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        } else {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
        if(Build.VERSION.SDK_INT>=23){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        mainView = (RelativeLayout) findViewById(R.id.main_view);
        mainView2 = (RelativeLayout) findViewById(R.id.main_view_two);

        closeBtn = (ImageButton) findViewById(R.id.close);
        addBtn = (ImageButton) findViewById(R.id.add);

        allSet = (ImageButton) findViewById(R.id.all_set);

        clrChoos = (LinearLayout) findViewById(R.id.color_choos);
        backClrChoos = (LinearLayout) findViewById(R.id.color_back_choos);
        changeText = (EditText) findViewById(R.id.edit_text);
        dialogLayout = (RelativeLayout) findViewById(R.id.dialog_lyout);
        dialogLayout.setVisibility(View.GONE);
        customImage = (ImageView) findViewById(R.id.custom_umage);
        customImage.setImageResource(R.mipmap.ic_launcher);
        aSwitch = (Switch) findViewById(R.id.switchs);
        PhotoViewAttacher photoAttacher;
        photoAttacher = new PhotoViewAttacher(customImage);
        photoAttacher.update();
        // img = (ImageView) findViewById(R.id.paper);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainView.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams secundLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mainView2.setLayoutParams(secundLayoutParams);

        mainView.setOnTouchListener(new ChoiceTouchListener());
        mainView2.setOnTouchListener(new ChoiceTouchListener());


        funnyText = (TextView) findViewById(R.id.funny_text);
        funnyText2 = (TextView) findViewById(R.id.funny_text_2);

        funnyText.setAllCaps(false);
        funnyText2.setAllCaps(false);


        addBtn.setBackgroundResource(R.mipmap.add);
        mainView2.setVisibility(View.GONE);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded) {
                    mainView2.setVisibility(View.GONE);
                    addBtn.setBackgroundResource(R.mipmap.add);
                    isAdded = false;
                } else {
                    mainView2.setVisibility(View.VISIBLE);
                    addBtn.setBackgroundResource(R.mipmap.close);
                    isAdded = true;
                }
            }
        });
        funnyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openDialog();
                is2ndTextClick = false;
                if (!isDaialogShow) {
                    dialogLayout.setVisibility(View.VISIBLE);
                    isDaialogShow = true;
                } else {
                    dialogLayout.setVisibility(View.GONE);
                    isDaialogShow = false;
                }
            }
        });

       /* allSet.setVisibility(View.VISIBLE);
        addBtn.setVisibility(View.VISIBLE);*/

        funnyText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openDialog();
                is2ndTextClick = true;
                if (!isDaialogShow) {
                    dialogLayout.setVisibility(View.VISIBLE);
                    isDaialogShow = true;
                } else {
                    dialogLayout.setVisibility(View.GONE);
                    isDaialogShow = false;
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (is2ndTextClick) {
                    funnyText2.setAllCaps(b);

                } else {
                    funnyText.setAllCaps(b);
                }
            }
        });
        changeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (is2ndTextClick) {
                    funnyText2.setPaintFlags(View.INVISIBLE);
                    funnyText.setPaintFlags(View.INVISIBLE);
                    if (charSequence.length() == 0) {
                        funnyText2.setText("Funny text");
                    } else {
                        funnyText2.setText(charSequence);
                    }
                } else {
                    if (charSequence.length() == 0) {
                        funnyText.setText("Funny text");
                    } else {
                        funnyText.setText(charSequence);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textSizeSeek = (TextView) findViewById(R.id.text_size_seek_show);
        seekbar = (SeekBar) findViewById(R.id.text_size_seek);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (is2ndTextClick) {
                    textSizeSeek.setText("Size : " + String.valueOf(i));
                    funnyText2.setTextSize(10 + i);
                } else {
                    funnyText.setTextSize(10 + i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        clrChoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(DragActivity.this)
                        .setTitle("Choose color")
                        .initialColor(Color.WHITE)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                if (is2ndTextClick) {
                                    funnyText2.setTextColor(selectedColor);
                                } else {
                                    funnyText.setTextColor(selectedColor);
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        backClrChoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(DragActivity.this)
                        .setTitle("Choose color")
                        .initialColor(Color.WHITE)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                rootLayout.setBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLayout.setVisibility(View.GONE);
                isDaialogShow = false;
            }
        });
        addView = (RelativeLayout)findViewById(R.id.add_view);
        nextSet = (RelativeLayout)findViewById(R.id.next_set);

        allSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* isDaialogShow = false; is2ndTextClick = false; isAdded = false;
                allSet.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);*/

                addView.setVisibility(View.GONE);
                nextSet.setVisibility(View.GONE);
                b = ScreenshotUtils.getScreenShot(rootLayout);
                if (b != null) {
                    showScreenShotImage(b);//show bitmap over imageview

                    File saveFile = ScreenshotUtils.getMainDirectoryName(getApplicationContext());//get the path to save screenshot
                    File file = ScreenshotUtils.store(b, "meme" + ".jpg", saveFile);//save the screenshot to selected path
                    // shareScreenshot(file);//finally share screenshot
                    performCrop(Uri.fromFile(file));
                    addView.setVisibility(View.VISIBLE);
                    nextSet.setVisibility(View.VISIBLE);
                } else
                    //If bitmap is null show toast message
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private final class ChoiceTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_BUTTON_PRESS:
                    openDialog();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }

    void openDialog() {
        @SuppressLint("RestrictedApi")
        Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        dialog.setContentView(R.layout.option_dialog);
        dialog.show();
    }

    private void showScreenShotImage(Bitmap b) {
        customImage.setImageBitmap(b);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DragActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DragActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(DragActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(DragActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Bitmap bitmap = null;
        if (requestCode == PIC_CROP) {
            if (data != null) {

                if(data.getData()==null){
                    bitmap = (Bitmap)data.getExtras().get("data");
                }else{
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                customImage.setImageBitmap(bitmap);


               // File sd = Environment.getExternalStorageDirectory();
                File direct = new File(Environment.getExternalStorageDirectory() + "/Memes");

                if (!direct.exists()) {
                    File wallpaperDirectory = new File("/sdcard/Memes/");
                    wallpaperDirectory.mkdirs();
                }
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);                String filename ="Meme"+n+".jpg";
                File dest = new File(direct, filename);

              //  Bitmap bitmapToSave = (Bitmap)data.getExtras().get("data");
                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
               // MediaScannerConnection.scanFile(this, new String[] { direct.getPath() }, new String[] { "image/jpeg" }, null);


            }
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
