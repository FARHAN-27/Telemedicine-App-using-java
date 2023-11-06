package com.ensias.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ensias.healthcareapp.model.Hour;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointementActivity extends AppCompatActivity {

    private LinearLayout lis;
    //final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointement);
        lis = findViewById(R.id.listDate);
        String patient_email = getIntent().getStringExtra("key1");
        String day = getIntent().getStringExtra("key2");
        final CollectionReference addRequest = db.collection("Doctor").document(patient_email).collection("calendar").document(day).collection("hour");


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 398);
        layoutParams.setMargins(200, 0, 300, 0);

        for (int i = 8; i<19;i++){
            final int j = i;
            TextView text = new TextView(this);
            text.setText(i + ":00");
            LinearLayout l = new LinearLayout(this);
            l.setMinimumHeight(250);
            l.addView(text, layoutParams);
            final Button btn = new Button(this);
            addRequest.document(i+"").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Hour note = documentSnapshot.toObject(Hour.class);
                    if(note != null){
                        btn.setText("already choosen");
                    }
                    else{
                        btn.setText("confirme this hour");
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Hour h =new Hour(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
                                addRequest.document(j+"").set(h);
                            }
                        });
                    }

                }
            });

            l.addView(btn);
            lis.addView(l);
        }

    }


}































































//    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nul
//        lam euismod fringilla urna, ut dapibus tortor varius a. Sed in nis
//        i nec nulla auctor feugiat. Phasellus consequat tristique justo, sit amet vul
//        putate sem tincidunt ac. Fusce efficitur justo vitae nunc vehicula, at dictum est finibus. Quisque vel sem non
//        justo lacinia malesuada. Curabitur scelerisque, purus a scelerisque laoreet, ligula ante tincidunt lorem, nec consequat risus
//        lorem nec libero. Sed vitae purus nec elit co
//
//
//        mus euismod nunc id diam pellentesque, id finibus enim bibendum. Integer condimentum, tortor id
//        bibendum viverra, urna nisl tristique tortor, vel egestas justo velit a libero.
//
//        Suspendisse euismod dui au
//        ctor nunc laoreet, sit amet aucto
//        r ipsum eleifend. Cras sit amet erat quam. Fusce at ex sed nisl rhoncus ultric
//        ies. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer a nunc
//        vitae lectus efficitur viverra. Vivamus luctus libero et bibendum interdum. Sed id justo eget eros pellentesque viverra. Quisque auctor ferme
//        ntum odio, ac elementum libero venenatis eget. Morbi at ipsum quis elit bibendum hendrerit. Etiam et nunc quis libero scelerisque ullamcorper. Proin eget luctus purus, eget eleifend elit.
//
//
//
//        Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egesta
//        s. Vestibulum dictum justo euismod, efficitur ex nec, dapibus dolor. Integer eget ligula sit amet lectus gravida finibu
//        s. Suspendisse bibendum tortor sit amet semper euismod. Sed auctor, tortor vel vehicula tincidunt, purus augue posuere eros, non
//        auctor sem odio nec nisl. Curabitur posuere, elit vel fermentum convallis, erat eros ullamcorper sapien, a dictum nunc neque non er
//        os. Vivamus sed dolor at sapien feugiat malesuada. Nullam nec eros non ante bibendum euismod vel eu turpis.