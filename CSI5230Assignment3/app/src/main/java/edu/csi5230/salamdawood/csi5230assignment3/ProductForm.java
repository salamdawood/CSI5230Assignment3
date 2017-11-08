package edu.csi5230.salamdawood.csi5230assignment3;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ProductForm extends AppCompatActivity {

    EditText productName = null, productPrice = null;
    Button saveButton = null, chooseImageButton = null;
    ImageView productChooseImage = null;
    Intent otherIntent = null;
    int index;
    final int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Bitmap passedImage;
    Random rand = new Random();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        productName = (EditText) findViewById(R.id.productNameEditText);
        productPrice = (EditText) findViewById(R.id.productPriceEditText);
        saveButton = (Button) findViewById(R.id.saveButton);
        chooseImageButton = (Button) findViewById(R.id.chooseImageButton);
        productChooseImage = (ImageView) findViewById(R.id.productFormImage);

        otherIntent = getIntent();

        if (otherIntent != null && otherIntent.hasExtra("index")){
            index = otherIntent.getIntExtra("index", -1);
            productName.setText(otherIntent.getStringExtra("name"));
            productPrice.setText(otherIntent.getStringExtra("price"));
            String filename = otherIntent.getStringExtra("image");
            try {
                FileInputStream is = this.openFileInput(filename);
                passedImage = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            productChooseImage.setImageBitmap(passedImage);
        }


        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productNameValue = productName.getText().toString();
                String productPriceValue = productPrice.getText().toString();

                if (otherIntent != null && otherIntent.hasExtra("index")){



                    //Intent returnIntent = new Intent();
                    Intent returnIntent = new Intent(view.getContext(), MainActivity.class);
                    returnIntent.putExtra("PRODUCT_NAME", productNameValue);
                    returnIntent.putExtra("PRODUCT_PRICE", productPriceValue);
                    returnIntent.putExtra("INDEX", index);
                    if (bitmap != null){
                        try {
                            int randomNumber = rand.nextInt();

                            //Write file
                            String filename = "bitmap.png" + randomNumber;
                            FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            //Cleanup
                            stream.close();
                            bitmap.recycle();

                            returnIntent.putExtra("IMAGE", filename);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        returnIntent.putExtra("IMAGE", otherIntent.getStringExtra("image"));
                    }
                    returnIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    returnIntent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
                    view.getContext().startActivity(returnIntent);
                }
                else {
                    try {
                        int randomNumber = rand.nextInt();

                        //Write file
                        String filename = "bitmap.png" + randomNumber;
                        FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        //Cleanup
                        stream.close();
                        bitmap.recycle();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("PRODUCT_NAME", productNameValue);
                        returnIntent.putExtra("PRODUCT_PRICE", productPriceValue);
                        returnIntent.putExtra("IMAGE", filename);
                        setResult(1, returnIntent);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=
                null) {
            Uri uri = data.getData();
//Copy URI contents into temporary file.
            String path = this.getFilesDir().getAbsolutePath() + "/temp_image";
            //textView.setText(path);
            File tempFile = new File(path);
            try {
                tempFile.createNewFile();
                copyAndClose(this.getContentResolver().openInputStream(data.getData()),
                        new FileOutputStream(tempFile));
            } catch (IOException e) {
//Log Error
            }
//Now fetch the new URI from temp file
            Uri newUri = Uri.fromFile(tempFile);
// Use new URI object just like you used to get bitmap data and display in ImageView
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                productChooseImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyAndClose(InputStream inputStream, FileOutputStream fileOutputStream) {
        try {
            byte[] buffer = new byte[4096]; // To hold file contents
            int bytes_read; // How many bytes in buffer
            // Read a chunk of bytes into the buffer, then write them out,
            // looping until we reach the end of the file (when read() returns
            // -1).
            while ((bytes_read = inputStream.read(buffer)) != -1) // Read until EOF and write
                fileOutputStream.write(buffer, 0, bytes_read);
        } catch (Exception e) {
        }
        // Always close the streams, even if exceptions were thrown
        finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ;
                }
            if (fileOutputStream != null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    ;
                }
        }
    }
}
