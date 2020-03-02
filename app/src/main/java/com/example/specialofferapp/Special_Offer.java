package com.example.specialofferapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

public class Special_Offer extends AppCompatActivity {

    private static final String TAG = "Special_Offer";
    private ImageView businessLogo;
    private TextView businessname;
    private TextView businessAddress;
    private TextView weburl;
    private TextView offerDetail;
    private FenceData fenceData;
    //font store in typeface
    private Typeface myCustomFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special__offer);
        ConstraintLayout currentlayout = (ConstraintLayout) findViewById(R.id.specialOfferlayout);
        businessLogo = findViewById(R.id.businesslogoimage);
        businessname = findViewById(R.id.nametxt);
        businessAddress = findViewById(R.id.addresstxt);
        weburl = findViewById(R.id.webtxt);
        offerDetail =findViewById(R.id.offerdetailtxt);
        myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/Acme-Regular.ttf");

        Intent intent = getIntent();
        if(intent.hasExtra(FenceData.class.getName()))
        {
            fenceData = (FenceData) intent.getSerializableExtra(FenceData.class.getName());
        }

        loadBusinessLogo(fenceData.getLogo());
        businessname.setText(fenceData.getId());
        businessAddress.setText(fenceData.getAddress());
        Linkify.addLinks(businessAddress,Linkify.ALL);
        weburl.setText(fenceData.getWeburl());
        Linkify.addLinks(weburl,Linkify.ALL);
        offerDetail.setText(fenceData.getMessage());
        loadQRCode(fenceData.getCode());
        loadBackground(fenceData.getFenceColor(),currentlayout);
        customFontApply(myCustomFont);
    }

    private void customFontApply(Typeface myCustomFont) {
        businessname.setTypeface(myCustomFont);
        businessAddress.setTypeface(myCustomFont);
        weburl.setTypeface(myCustomFont);
        offerDetail.setTypeface(myCustomFont,Typeface.NORMAL);
    }

    private void loadBackground(String fenceColor, ConstraintLayout currentlayout) {

        int textColor = Color.parseColor(fenceColor);

       int messageColor = changeDetailTextViewColor(textColor);

        currentlayout.setBackgroundColor(textColor);
        offerDetail.setBackgroundColor(messageColor);

    }

    private int changeDetailTextViewColor(int textColor) {
        int RedColor = Color.red(textColor);
        int GreenColor = Color.green(textColor);
        int BlueColor = Color.blue(textColor);

        if(GreenColor > 230)
        {
            GreenColor = GreenColor - 20;
        }
        else if(GreenColor <= 30)
        {
            GreenColor = GreenColor + 20;
        }
        else
        {
            GreenColor = GreenColor + 25;
        }


        if(BlueColor > 230)
        {
            BlueColor = BlueColor - 20;
        }
        else if(BlueColor <=30)
        {
            BlueColor = BlueColor + 20;
        }
        else
        {
            BlueColor = BlueColor + 25;
        }
        return Color.rgb(RedColor,GreenColor,BlueColor);
    }

    private void loadQRCode(String logo) {
        if(logo.trim().isEmpty())
            return;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            //width and height are not corresponding from imageview size but just gussed number
            BitMatrix bitMatrix = writer.encode(logo, BarcodeFormat.QR_CODE, 512, 512);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) { //one row at a time filling
                for (int y = 0; y < height; y++) {
                    //bitMatrix is working data but not fit into imageview. so have to put into imageview
                    //bitMatrix.get(x, y) --> true then blace else white
                    //contrast color , then it wont have prob. can change from black to red and white
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.qrcodeimage)).setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void loadBusinessLogo(String logo) {
        Picasso picasso = new Picasso.Builder(this).build();
        picasso.isLoggingEnabled();
        picasso.load(logo)
                .into(businessLogo);
    }


}
