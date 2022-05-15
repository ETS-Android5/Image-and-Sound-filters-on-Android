package com.example.mobappproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobappproject.RecyclerViewAdapter.OnPickListener;
import com.gu.toolargetool.TooLargeTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class images extends AppCompatActivity implements OnPickListener{

    private static final String TAG = "RecyclerViewer";
    private static final String ASYNC_DEBUG = "Async debug";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    boolean filterFlag;
    boolean imageFlag;

    //
    ImageView mImageView;
    Uri pickedImage;
    int originalWidth;
    int originalHeight;
    int newWidth;
    int newHeight;

    Button mSaver;
    Button mGallery;
    Button mApply;

    Bitmap bmp;
    Bitmap mFiltered;
    ProgressBar mProgress;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filterFlag = false;
        imageFlag = false;

        originalHeight = 0;
        originalWidth = 0;

        newWidth=0;
        newHeight=0;

        setContentView(R.layout.activity_images);
        mImageView = findViewById(R.id.imageView);

        mGallery = findViewById(R.id.gallery);
        mSaver = findViewById(R.id.saver);
        mApply = findViewById(R.id.apply);

        bmp = null;
        mFiltered = null;

        mProgress = findViewById(R.id.progressBar);

        setupListeners();

        getImages();

        checkAndLoad();

    }

    private void setupListeners(){
        mGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkAndLoad();
            }
        });

        mSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterFlag==false) {
                    try {
                        saveMyImage();
                        getMyImage(pickedImage);
                    } catch (IOException e) {
                        Toast.makeText(images.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(images.this, "Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(images.this, "Please wait! Previous filter is still being computed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterFlag==false) {
                    if(imageFlag==false){
                        mImageView.setImageBitmap(bmp);
                        imageFlag = true;
                    }
                    else {
                        mImageView.setImageBitmap(mFiltered);
                        imageFlag = false;
                    }
                }
                else {
                    Toast.makeText(images.this, "Please wait! Previous filter is still being computed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    private void checkAndLoad(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE);
            }
            else{
                loadFromGallery();
            }
        }
        else {
            loadFromGallery();
        }
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        int resourceId = 0;
        String res = "";

        resourceId = R.drawable.lena_lp;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Low-pass");

        resourceId = R.drawable.lena_hp;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("High-pass");

        resourceId = R.drawable.lena_moving;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Moving horizontal");

        resourceId = R.drawable.lena_moving;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Moving vertical");

        resourceId = R.drawable.lena_embossing_east;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Embossing east");

        resourceId = R.drawable.lena_embossing_south;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Embossing south");

        resourceId = R.drawable.lena_sobel;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Sobel");

        resourceId = R.drawable.lena_warming_correct;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Warming");

        resourceId = R.drawable.lena_max;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Maximazing filter");

        resourceId = R.drawable.lena_min;
        res = Integer.toString(resourceId);
        mImageUrls.add(res);
        mNames.add("Minimazing filter");

        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadFromGallery(){
        Intent mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setType("image/*");
        startActivityForResult(mIntent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    loadFromGallery();
                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            pickedImage = data.getData();

            getMyImage(pickedImage);

            bmp = mFiltered.copy(Bitmap.Config.ARGB_8888, true);

            Log.d(TAG, "Bitmap size: " + mFiltered.getByteCount());
            Log.d(TAG, "Width: " + mFiltered.getWidth() + " Height: " + mFiltered.getHeight());
            if(mFiltered.isMutable()==true)Log.d(TAG, "Bitmap IS mutable!");
            mImageView.setImageBitmap(mFiltered);
        }
    }

    private void getMyImage(Uri myUri){
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(myUri, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        mFiltered = BitmapFactory.decodeFile(imagePath, options);
        originalWidth=options.outWidth;
        originalHeight=options.outHeight;
        Log.d(TAG, "Width: " + options.outWidth + " Height: " + options.outHeight);
        if(options.outHeight > 600 && options.outWidth > 600){
            if(options.outHeight < 1000 && options.outWidth < 1000)options.inSampleSize = 2;
            else options.inSampleSize = 3;
        }


        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;
        mFiltered = BitmapFactory.decodeFile(imagePath, options);
        newHeight = mFiltered.getHeight();
        newWidth = mFiltered.getWidth();
        cursor.close();
    }

    private void saveMyImage() throws IOException {


        if(imageFlag==false){
            imageFlag=true;
            mImageView.setImageBitmap(bmp);
        }
        mFiltered=null;

        //Bitmap bitmap = getResizedBitmap(bmp, originalWidth, originalHeight);
        Bitmap bitmap = bmp;
        //Bitmap mbitmap = bmp.copy(Bitmap.Config.ARGB_8888, false);
        //Bitmap bitmap = getResizedBitmap(mbitmap, originalWidth, originalHeight);
        //mbitmap = null;

        FileOutputStream outStream = null;
        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                            + "/SoundAndImageFiltering");
        dir.mkdirs();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String imageName = simpleDateFormat.format(Calendar.getInstance().getTime());

        String fileName = String.format(imageName + ".jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        outStream.flush();
        outStream.close();
    }

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

        Log.d(TAG, "After scalling: (" + resizedBitmap.getWidth() + "," + resizedBitmap.getHeight() + ")");
        return resizedBitmap;
    }

    private int[] chooseFilter(int position){
        int[] mFilter = new int[9];
        switch (position){
            case 0: int[] mFilterLP = {1, 1, 1, 1, 1, 1, 1, 1, 1}; mFilter = mFilterLP; break;
            case 1: int[] mFilterHP = {-1, -1, -1, -1, 9, -1, -1, -1, -1}; mFilter = mFilterHP; break;
            case 2: int[] mFilterMoveHor = {0, -1, 0, 0, 1, 0, 0, 0, 0}; mFilter = mFilterMoveHor; break;
            case 3: int[] mFilterMoveVer = {0, 0, 0, -1, 1, 0, 0, 0, 0}; mFilter = mFilterMoveVer; break;
            case 4: int[] mFilterEmbEast = {-1, 0, 1, -1, 1, 1, -1, 0, 1}; mFilter = mFilterEmbEast; break;
            case 5: int[] mFilterEmbSouth = {-1, -1, -1, 0, 1, 0, 1, 1, 1}; mFilter = mFilterEmbSouth; break;
            case 6: int[] mFilterSobel = {1, 2, 1, 0, 0, 0, -1, -2, -1}; mFilter = mFilterSobel; break;
        }
        return mFilter;
    }


    @Override
    public void onPickClick(int position) {
        if(filterFlag==false) {
            Log.d(TAG, "Called filter no. " + position);
            ExampleAsyncTask task = new ExampleAsyncTask(this);
            task.execute(position);
            Log.d(ASYNC_DEBUG, "Started filter no. " + position);
        }
        else {
            Toast.makeText(images.this, "Please wait! Previous filter is still being computed", Toast.LENGTH_SHORT).show();
        }
    }

    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, ImageView> {

        private WeakReference<images> activityWeakReference;

        ExampleAsyncTask(images activity){
            activityWeakReference = new WeakReference<images>(activity);
        }


        @Override
        protected ImageView doInBackground(Integer... integers) {

            images activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }



            if(integers[0]<7) {

                Log.d(ASYNC_DEBUG, "Loop ends: " + (activity.bmp.getHeight() - 1) + ", " + (activity.bmp.getWidth() - 1));

                int redSum, blueSum, greenSum;
                int pixel = 0;
                int[] Filter = activity.chooseFilter(integers[0]);
                int Size = 3;
                int margin = (Size - 1) / 2;

                int Norm = 0;
                for (int i = 0; i < Size; i++)
                    for (int j = 0; j < Size; j++)
                        Norm += Filter[i + Size * j];
                if (Norm == 0) Norm = 1;


                for (int i = margin; i < activity.bmp.getWidth() - margin; i++) {
                    for (int j = margin; j < activity.bmp.getHeight() - margin; j++) {
                        redSum = 0;
                        greenSum = 0;
                        blueSum = 0;
                        for (int k = 0; k < Size; k++) {
                            for (int l = 0; l < Size; l++) {
                                pixel = activity.mFiltered.getPixel(i + k - margin, j + l - margin);
                                redSum += Filter[k * Size + l] * Color.red(pixel);
                                greenSum += Filter[k * Size + l] * Color.green(pixel);
                                blueSum += Filter[k * Size + l] * Color.blue(pixel);
                            }
                        }
                        redSum /= Norm;
                        greenSum /= Norm;
                        blueSum /= Norm;
                        if (redSum > 255) redSum = 255;
                        else if (redSum < 0) redSum = 0;
                        if (greenSum > 255) greenSum = 255;
                        else if (greenSum < 0) greenSum = 0;
                        if (blueSum > 255) blueSum = 255;
                        else if (blueSum < 0) blueSum = 0;


                        activity.bmp.setPixel(i, j, Color.rgb(redSum, greenSum, blueSum));
                    }
                    publishProgress(i);
                }
            }

            else if(integers[0]==7){
                int redSum, blueSum, greenSum;
                int valr = 0;
                int valg = 0;
                int valb = 0;
                int pixel = 0;
                int[] Filter = {1, 1, 1, 1, -2, 1, -1, -1, -1};
                int Size = 3;
                int margin = (Size - 1) / 2;

                int Norm = 0;
                for (int i = 0; i < Size; i++)
                    for (int j = 0; j < Size; j++)
                        Norm += Filter[i + Size * j];
                if (Norm == 0) Norm = 1;

                Log.d(ASYNC_DEBUG, "Norm = " + Norm);

                for (int i = margin; i < activity.bmp.getWidth() - margin; i++) {
                    for (int j = margin; j < activity.bmp.getHeight() - margin; j++) {
                        redSum = 0;
                        greenSum = 0;
                        blueSum = 0;
                        for (int k = 0; k < Size; k++) {
                            for (int l = 0; l < Size; l++) {
                                pixel = activity.mFiltered.getPixel(i + k - margin, j + l - margin);

                                valr=Filter[k * Size + l] * Color.red(pixel);
                                if(valr<0)valr = 0;
                                if(valr>255)valr=255;
                                redSum += valr;

                                valg=Filter[k * Size + l] * Color.green(pixel);
                                if(valg<0)valg = 0;
                                if(valg>255)valg=255;
                                greenSum += valg;

                                valb=Filter[k * Size + l] * Color.blue(pixel);
                                if(valb<0)valb = 0;
                                if(valb>255)valb=255;
                                blueSum += valb;
                            }
                        }
                        redSum /= Norm;
                        //Log.d(ASYNC_DEBUG, "Red sum = " + redSum);
                        greenSum /= Norm;
                        //Log.d(ASYNC_DEBUG, "Green sum = " + greenSum);
                        blueSum /= Norm;
                        //Log.d(ASYNC_DEBUG, "Blue sum = " + blueSum);
                        if (redSum > 255) redSum = 255;
                        else if (redSum < 0) redSum = 0;
                        if (greenSum > 255) greenSum = 255;
                        else if (greenSum < 0) greenSum = 0;
                        if (blueSum > 255) blueSum = 255;
                        else if (blueSum < 0) blueSum = 0;


                        activity.bmp.setPixel(i, j, Color.rgb(redSum, greenSum, blueSum));
                    }
                    publishProgress(i);
                }

            }

            else if(integers[0]==8){
                int pixel, rmax, gmax, bmax;
                int Size = 3;
                int margin = (Size - 1) / 2;

                for (int i = margin; i < activity.bmp.getWidth() - margin; i++) {
                    for (int j = margin; j < activity.bmp.getHeight() - margin; j++) {
                    rmax = 0;
                    gmax = 0;
                    bmax = 0;
                        for (int k = 0; k < Size; k++) {
                            for (int l = 0; l < Size; l++) {
                                pixel = activity.mFiltered.getPixel(i + k - margin, j + l - margin);
                                if(rmax<Color.red(pixel)) rmax=Color.red(pixel);
                                if(gmax<Color.green(pixel)) gmax=Color.green(pixel);
                                if(bmax<Color.blue(pixel)) bmax=Color.blue(pixel);
                            }
                        }
                        activity.bmp.setPixel(i, j, Color.rgb(rmax, gmax, bmax));
                    }
                    publishProgress(i);
                }
            }

            else if(integers[0]==9){
                int pixel, rmin, gmin, bmin;
                int Size = 3;
                int margin = (Size - 1) / 2;

                for (int i = margin; i < activity.bmp.getWidth() - margin; i++) {
                    for (int j = margin; j < activity.bmp.getHeight() - margin; j++) {
                        rmin = 255;
                        gmin = 255;
                        bmin = 255;
                        for (int k = 0; k < Size; k++) {
                            for (int l = 0; l < Size; l++) {
                                pixel = activity.mFiltered.getPixel(i + k - margin, j + l - margin);
                                if(rmin>Color.red(pixel)) rmin=Color.red(pixel);
                                if(gmin>Color.green(pixel)) gmin=Color.green(pixel);
                                if(bmin>Color.blue(pixel)) bmin=Color.blue(pixel);
                            }
                        }
                        activity.bmp.setPixel(i, j, Color.rgb(rmin, gmin, bmin));
                    }
                    publishProgress(i);
                }
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            images activity = activityWeakReference.get();
            if(activity== null || activity.isFinishing())return;

            activity.filterFlag = true;
            activity.mProgress.setVisibility(View.VISIBLE);
            activity.mProgress.setMax(activity.bmp.getWidth());
            activity.mImageView.setImageBitmap(activity.mFiltered);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            images activity = activityWeakReference.get();
            if(activity== null || activity.isFinishing())return;
            activity.mProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ImageView imageView) {
            super.onPostExecute(imageView);
            images activity = activityWeakReference.get();
            if(activity== null || activity.isFinishing())return;
            Log.d(ASYNC_DEBUG, "DONE!");
            activity.mProgress.setProgress(0);
            activity.mProgress.setVisibility(View.INVISIBLE);
            activity.mImageView.setImageBitmap(activity.bmp);
            activity.filterFlag = false;
            activity.imageFlag = true;
        }
    }

}
