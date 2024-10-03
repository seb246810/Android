package com.example.myapplication;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.ImageUtils;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Scanning#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Scanning extends Fragment {
    TextView textView;

    String uriString;

    Button gallery;

    Button camera;

    Bitmap bitmap;

    ImageView imageView;



    private static final int REQUEST_CODE = 22;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Scanning() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment scanning.
     */
    // TODO: Rename and change types and number of parameters
    public static Scanning newInstance(String param1, String param2) {
        Scanning fragment = new Scanning();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanning, container, false);
        Toolbar scanningnav = (Toolbar) view.findViewById(R.id.toolbar);

        imageView= (ImageView) view.findViewById(R.id.imageView);



        gallery = (Button) view.findViewById(R.id.Gallery);




        scanningnav.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                OnNavigationItemSelected(menuItem);
                return false;
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                extracttext(bitmap);

            }
        });























        // Inflate the layout for this fragment

        return view;
    }

    public void onActivityResult(int requestcode, int resultcode, @Nullable Intent data){
        if(requestcode == REQUEST_CODE && resultcode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(bitmap);

            //extracttext(bitmap);








    }




        }




    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {




                    try {
                         bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                         imageView.setImageBitmap(bitmap);

                        //extracttext(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    //uriString = uri.toString();
                    //Uri urb = Uri.parse(uriString);
                    //_extractTextFromUri(getContext(), urb);


                }
            });






    public void extracttext(Bitmap bitmap){


        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);



        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully

                                String lastline = "";

                                String previousline = "";

                                String title = "";

                                String date = "";

                                String price="";




                                String parsetext = visionText.getText();

                                Scanner scanner = new Scanner(parsetext);

                                if(scanner.hasNextLine()){
                                    title = scanner.nextLine().toLowerCase();

                                    Log.d(TAG,"Next line is : "+title);
                                }

                                while (scanner.hasNextLine()){
                                    String line = scanner.nextLine();

                                    Log.d(TAG,line);

                                    date=dateformat(line);

                                    if(!date.isEmpty()){
                                        break;
                                    }


                                }

                                Log.d(TAG,"We have the date "+ date);


                                scanner = new Scanner(parsetext);

                                while (scanner.hasNextLine()){






                                    previousline = lastline;

                                    lastline = scanner.nextLine();
                                }

                                 price=totalformat(lastline,previousline);

                                Log.d(TAG,"Price is " + price);


                                scanner = new Scanner(parsetext);
                                while(scanner.hasNextLine()){
                                    String line = scanner.nextLine();
                                    Log.d(TAG,line);
                                }


                                senddata(title,price,date,bitmap);



                                //storereciept edit = new storereciept();

                                //byte[] bytes =sendbitmap(bitmap);

                                //Bundle bundle = new Bundle();

                                //bundle.putByteArray("image",bytes);

                                //edit.setArguments(bundle);

                                //getActivity().getSupportFragmentManager().beginTransaction()
                                        //.replace(R.id.flFragment,edit)
                                        //.addToBackStack(null)
                                       // .commit();





                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                    }
                                });



    }


    public byte[] sendbitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }


    public String dateformat(String line){

        String matchedDate = "";


        String regexpattern = "\\d{1,2}[\\/,-]\\d{1,2}[\\/,]\\d{2,4}";

        Pattern pattern = Pattern.compile(regexpattern);


        Matcher matcher = pattern.matcher(line);



        while (matcher.find()  ) {
            matchedDate = matcher.group();
            Log.d(TAG,"Found date: " + matchedDate);

        }

        return matchedDate;



    }

    public String totalformat(String line,String previousline){

        Log.d(TAG,line+" " + previousline+ " These are the lines sent to function");

        line = line.replace("$","");

        previousline= line.replace("$","");

        line=line.replace("£","");

        previousline=previousline.replace("£","");


        String checkisnum = "\\d+\\.\\d{1,2}";

        String macthednumber = "";




        String zeroformat = "0\\.00";

        Pattern isnum = Pattern.compile(checkisnum);

        Pattern pattern = Pattern.compile(zeroformat);




        if(line.matches(zeroformat) && previousline.matches(checkisnum)){

            Log.d(TAG,"This is previous "+ previousline);

            return previousline;


        } else if (line.matches(checkisnum) && !line.matches(zeroformat)) {

            Log.d(TAG,"This is last line "+ line);
            return line;

        }

        else{




            Matcher matcher = pattern.matcher(line);



            while (matcher.find()  ) {
                 macthednumber = matcher.group();
                Log.d(TAG,"Found date: " + macthednumber);

            }
            return macthednumber;


        }



    }

    public void senddata(String title,String price, String date,Bitmap bitmap){

        storereciept edit = new storereciept();

        byte[] bytes =sendbitmap(bitmap);

        Log.d(TAG,"The date so send is " + date);


        Bundle send = new Bundle();

        send.putString("bundle","camera");

        send.putString("Title",title);

        send.putString("Price",price);

        send.putString("Date",date);

        send.putByteArray("Image",bytes);

        edit.setArguments(send);



        getActivity().getSupportFragmentManager().beginTransaction()
        .replace(R.id.flFragment,edit)
        .addToBackStack(null)
         .commit();
    }


    public boolean
    OnNavigationItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();

        if(itemId==R.id.cam){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,REQUEST_CODE);
            return true;

        }

        else if(itemId==R.id.library){
            mGetContent.launch("image/*");
            return true;


        }

        return false;
    }









}