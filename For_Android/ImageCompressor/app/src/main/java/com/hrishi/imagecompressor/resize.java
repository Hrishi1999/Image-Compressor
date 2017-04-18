package com.hrishi.imagecompressor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class resize extends AppCompatActivity {

    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private Uri mImageCaptureUri;
    private Bitmap xyz;
    private String pathx;
    private Bitmap bm_res;
    private int height;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize);
        setTitle("Resize Image");

        imageView = (ImageView)findViewById(R.id.imageView2_res);
        ImageView t = (ImageView)findViewById(R.id.imageView2_res);
        t.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }

        });

        Button resize = (Button) findViewById(R.id.button3_res);
        resize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    try
                    {
                        int inWidth = 0;
                        int inHeight = 0;
                        EditText he = (EditText) findViewById(R.id.editTexth);
                        EditText wi = (EditText) findViewById(R.id.editTextw);
                        String th = he.getText().toString();
                        String tw = he.getText().toString();
                        int newh = Integer.parseInt(th);
                        int neww = Integer.parseInt(tw);
                        InputStream in = getContentResolver().openInputStream(mImageCaptureUri);

                        // decode image size (decode metadata only, not the whole image)
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(in, null, options);
                        in.close();
                        in = null;

                        // save width and height
                        inWidth = options.outWidth;
                        inHeight = options.outHeight;

                        // decode full image pre-resized
                        int dstWidth = neww;
                        int dstHeight = newh;

                        in = getContentResolver().openInputStream(mImageCaptureUri);
                        options = new BitmapFactory.Options();
                        // calc rough re-size (this is no exact resize)
                        options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
                        // decode full image
                        Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

                        // calc exact destination size
                        Matrix m = new Matrix();
                        RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
                        RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
                        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
                        float[] values = new float[9];
                        m.getValues(values);

                        // resize bitmap
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

                        // save image
                        try
                        {
                            String path = Environment.getExternalStorageDirectory().toString();
                            File file = new File(path, pathx);
                            FileOutputStream out = new FileOutputStream(file);
                            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            MediaStore.Images.Media.insertImage(getContentResolver()
                                    , file.getAbsolutePath(), file.getName(), file.getName());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (IOException e)
                    {
                       e.printStackTrace();
                    }
                    /*
                    //
                    EditText he = (EditText) findViewById(R.id.editTexth);
                    EditText wi = (EditText) findViewById(R.id.editTextw);
                    int newh = Integer.getInteger(he.toString());
                    int neww = Integer.getInteger(wi.toString());

                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File file = new File(path, pathx);
                    fOut = new FileOutputStream(file);
                    InputStream imageStream = getContentResolver().openInputStream(mImageCaptureUri);
                    Bitmap to_save = BitmapFactory.decodeStream(imageStream);
                    to_save.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();


                            */
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }


        });

        Button pickImage = (Button) findViewById(R.id.button_res);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

            }


        });

    }
/*
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {


            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        }
        */


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Uri selectedImagex = imageReturnedIntent.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImagex,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        File f = new File(picturePath);
                        String imageName = f.getName();
                        pathx = imageName;

                        final InputStream imageStream = getContentResolver().openInputStream(selectedImagex);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        bm_res = selectedImage;
                        mImageCaptureUri = selectedImagex;
                        imageReturnedIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                mImageCaptureUri);
                        xyz = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);

                        height = selectedImage.getHeight();
                        width = selectedImage.getWidth();

                        TextView dimen = (TextView)findViewById(R.id.textView_dim);
                        dimen.setText(height+"x"+width);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }


    }
    }


