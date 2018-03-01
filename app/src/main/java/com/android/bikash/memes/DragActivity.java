package com.android.bikash.memes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.w3c.dom.Text;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DragActivity extends AppCompatActivity {
    private ImageView img, customImage;
    private ViewGroup rootLayout;
    private int _xDelta;
    private int _yDelta;
    private RelativeLayout mainView, dialogLayout;
    private boolean isDaialogShow = false;
    private TextView funnyText, textSizeSeek;
    private SeekBar seekbar;
    private EditText changeText;
    private Switch aSwitch;
    private LinearLayout clrChoos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        mainView = (RelativeLayout) findViewById(R.id.main_view);

        clrChoos = (LinearLayout)findViewById(R.id.color_choos);
        changeText = (EditText) findViewById(R.id.edit_text);
        dialogLayout = (RelativeLayout) findViewById(R.id.dialog_lyout);
        dialogLayout.setVisibility(View.GONE);
        customImage = (ImageView) findViewById(R.id.custom_umage);
        customImage.setImageResource(R.mipmap.ic_launcher);
        aSwitch = (Switch)findViewById(R.id.switchs);
        PhotoViewAttacher photoAttacher;
        photoAttacher = new PhotoViewAttacher(customImage);
        photoAttacher.update();
        // img = (ImageView) findViewById(R.id.paper);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400);
        mainView.setLayoutParams(layoutParams);

        mainView.setOnTouchListener(new ChoiceTouchListener());
        funnyText = (TextView) findViewById(R.id.funny_text);
        funnyText.setAllCaps(false);
        funnyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openDialog();
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

                funnyText.setAllCaps(b);
            }
        });
        changeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    funnyText .setText("Funny text here");
                }else {
                    funnyText.setText(charSequence);
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
                textSizeSeek.setText("Size : "+String.valueOf(i));
                funnyText.setTextSize(10 + i);
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
                                funnyText.setTextColor(selectedColor);
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
}
