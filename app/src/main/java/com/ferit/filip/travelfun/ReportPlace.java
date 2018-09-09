package com.ferit.filip.travelfun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPlace extends AppCompatActivity  implements View.OnClickListener{


    @BindView (R.id.title_report)   EditText name_report;
    @BindView   (R.id.txt_report)EditText txt_report;
   @BindView (R.id.choose) Button chooseBtn;
    @BindView (R.id.upload)  Button uploadBtn;
    @BindView  (R.id.preview)  ImageView img;
    private static  final int IMG_REQUEST=777;
    private Bitmap bitmap;
    double lng,lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_place);
        ButterKnife.bind(this);
        chooseBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);


        Intent intent=getIntent();
        Bundle bundleObject = getIntent().getExtras();

        lat =getIntent().getExtras().getDouble("LATITUDE");
        lng =getIntent().getExtras().getDouble("LONGITUDE");


    }











    @Override
    public void onClick(View view) {


        switch(view.getId()){



            case R.id.choose:  selectImage();
                break;



            case R.id.upload:

                                uploadImage();
                Log.d("DEV","grananje u switchu radi");
                break;


        }

    }

    private  void uploadImage(){
        Log.d("DEV","pokrece se uploadImage");
        String Image= ImageToString();
        String Title = name_report.getText().toString();
        String Txt=txt_report.getText().toString();

        rtfInterface rtfInterface = rtfClient.getrtfClient().create(rtfInterface.class);
        retrofit2.Call<ImageClass> call= rtfInterface.uploadImage(Title,Txt,lat,lng,Image);



        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(retrofit2.Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass =response.body();
                Toast.makeText(ReportPlace.this,"Server response:"+imageClass.getResponse(),Toast.LENGTH_LONG).show();

                uploadBtn.setEnabled(false);
                finish();
            }

            @Override
            public void onFailure(retrofit2.Call<ImageClass> call, Throwable t) {
                Log.d("DEV","nema responsa");
            }
        });

    }


    private void selectImage(){



        Intent intent = new Intent();
        intent.setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);

       startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){



            Uri path = data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                uploadBtn.setEnabled(true);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private String ImageToString(){


        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);

    }
}
