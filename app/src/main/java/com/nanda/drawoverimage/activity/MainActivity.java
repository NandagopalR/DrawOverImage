package com.nanda.drawoverimage.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nanda.drawoverimage.R;
import com.nanda.drawoverimage.common.DrawingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btn_snapshot)
    TextView mSnapShot;

    @Bind(R.id.btn_clear)
    TextView mClear;

    @Bind(R.id.layout_imageview)
    RelativeLayout layoutRoot;

    @Bind(R.id.drawing_pad)
    LinearLayout layoutChild;

    @Bind(R.id.edittext)
    EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        layoutChild.addView(new DrawingView(this));
    }

    @OnClick({R.id.btn_snapshot, R.id.btn_clear})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_snapshot:
                if (TextUtils.isEmpty(edittext.getText().toString()))
                    edittext.setVisibility(View.INVISIBLE);
                saveBitMap(layoutRoot);    //which view you want to pass that view as parameter
                break;
            case R.id.btn_clear:
                edittext.setText("");
                layoutChild.removeAllViews();
                layoutChild.addView(new DrawingView(this));
                break;
        }
    }

    private void saveBitMap(View drawView) {
        File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "DrawingImage");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            if (pictureFile.exists())
                showToast(this, "File Created");
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(Context context, String input) {
        Toast.makeText(context, input, Toast.LENGTH_SHORT).show();
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
