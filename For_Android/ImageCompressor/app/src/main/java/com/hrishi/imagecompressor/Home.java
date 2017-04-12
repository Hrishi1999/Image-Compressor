package com.hrishi.imagecompressor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class Home extends AppCompatActivity {

    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private Uri mImageCaptureUri;
    private Bitmap xyz;
    private String pathx;
    private SeekBar seekBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        imageView = (ImageView)findViewById(R.id.imageView);
        ImageView t = (ImageView)findViewById(R.id.imageView);
        t.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }

        });


        Button pickImage = (Button) findViewById(R.id.button1);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }


        });

        Button compress = (Button) findViewById(R.id.button2);
        compress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try

                {
                    SeekBar s1 = (SeekBar)findViewById(R.id.seekBar1);
                    String path = Environment.getExternalStorageDirectory().toString();
                    OutputStream fOut = null;
                    File file = new File(path, pathx);
                    fOut = new FileOutputStream(file);
                    InputStream imageStream = getContentResolver().openInputStream(mImageCaptureUri);
                    Bitmap ho = BitmapFactory.decodeStream(imageStream);
                    ho.compress(Bitmap.CompressFormat.JPEG, s1.getProgress(), fOut);
                    fOut.flush();
                    fOut.close();

                    MediaStore.Images.Media.insertImage(getContentResolver()
                            ,file.getAbsolutePath(),file.getName(),file.getName());

                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        });

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        final TextView progress = (TextView)findViewById(R.id.textView3);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

                SeekBar so = (SeekBar)findViewById(R.id.seekBar1);
                TextView progressx = (TextView)findViewById(R.id.textView3);
                progressValue = so.getProgress();
                progressx.setText(Integer.toString(progressValue)+"%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


    }


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
                        mImageCaptureUri = selectedImagex;
                        imageReturnedIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                mImageCaptureUri);
                        xyz = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);

                        SeekBar s1 = (SeekBar)findViewById(R.id.seekBar1);
                        TextView t3 = (TextView)findViewById(R.id.textView3);
                        t3.setText(s1.getProgress() + "%");

                        TextView size = (TextView)findViewById(R.id.size);
                        long file_size = f.length();
                        size.setText((file_size/1024)+"KB");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {

            Intent intent = new Intent(this, resize.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
