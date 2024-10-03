package com.example.myapplication;

import static android.content.ContentValues.TAG;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link storereciept#newInstance} factory method to
 * create an instance of this fragment.
 */
public class storereciept extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String cat;


    ImageView receipt;

    EditText title;

    EditText date;

    EditText Total;

    EditText Category;

    Spinner categgoryselect;

    Button save;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public storereciept() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment storereciept.
     */
    // TODO: Rename and change types and number of parameters
    public static storereciept newInstance(String param1, String param2) {
        storereciept fragment = new storereciept();
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_storereciept, container, false);

        receipt = (ImageView) view.findViewById(R.id.receiptimage);

        Total = (EditText) view.findViewById(R.id.Total);

        title = (EditText) view.findViewById(R.id.Title);

        date = (EditText) view.findViewById(R.id.Date);

        categgoryselect = (Spinner) view.findViewById(R.id.spinner);

        save = (Button) view.findViewById(R.id.Save);



        Bundle bundle = getArguments();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.categories,
                android.R.layout.simple_spinner_item
        );                                              //set up spinner to hold array.

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categgoryselect.setAdapter(adapter);

        categgoryselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cat = categgoryselect.getSelectedItem().toString(); //get selected item in spinner.
                Log.d(TAG,"mood is: "+cat);

                //load corresponding entries

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(!bundle.equals(null)){
            String bundletouse= getArguments().getString("bundle");

            if("camera".equals(bundletouse)){

                byte[] bytearray = getArguments().getByteArray("Image");



                String Price = getArguments().getString("Price");

                String Date = getArguments().getString("Date");

                String Title = getArguments().getString("Title");

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);

                receipt.setImageBitmap(bitmap);

                Total.setText(Price);

                date.setText(Date);

                title.setText(Title);





            } else if ("loading".equals(bundletouse)) {

                String total = getArguments().getString("Total");

                String date2 = getArguments().getString("date");

                String title2 = getArguments().getString("title");

                String category = getArguments().getString("Category");

                String imageref = getArguments().getString("Imageref");



                int position = adapter.getPosition(category);

                Total.setText(total);

                downloadreciept(imageref);

                date.setText(date2);

                categgoryselect.setSelection(position);

                title.setText(title2);




            }
        }















        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String category = Category.getText().toString();

                String name = title.getText().toString();

                String price = Total.getText().toString();

                String date3 = date.getText().toString();

                Drawable drawable =receipt.getDrawable();

                Bitmap img = ((BitmapDrawable) drawable).getBitmap();

                storeData(img,price,cat,date3,name);

            }
        });







        return view;
    }


    public void storeData(Bitmap bitmap,String price,String category,String date,String title){

        String ID = UUID.randomUUID().toString();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference storageReference = storage.getReference();

        StorageReference recieptRef = storageReference.child(ID);

        UploadTask uploadTask = recieptRef.putBytes(byteArray);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Unsuccessful");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata();

            }
        });



        //mAuth = FirebaseAuth.getInstance();

        //String currentUser = mAuth.getCurrentUser().getEmail().toString();  //Saving values in current users collection.

        Map<String, Object> user = new HashMap<>();
        user.put("Image",ID);
        user.put("Title", title);               //putting data in database
        user.put("Date", date);
        user.put("Total",price);
        user.put("Category", category);
// Add a new document with a generated ID
        db.collection("receipts").document(ID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("MainActivity", "DocumentSnapshot added with ID: ");
                        Toast.makeText(getContext(), "receipt added",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                        Log.w("MainActivity", "Error adding document", e);
                    }
                });




    }


    public void downloadreciept(String imgref){



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference();

        StorageReference recieptRef = storageReference.child(imgref);

        final long ONE_MEGABYTE = 1024*1024;

        recieptRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                receipt.setImageBitmap(bitmap);





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}

