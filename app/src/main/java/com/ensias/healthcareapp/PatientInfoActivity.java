package com.ensias.healthcareapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ensias.healthcareapp.Common.Common;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.ensias.healthcareapp.Common.Common.convertBloodToInt;

public class PatientInfoActivity extends AppCompatActivity {

    EditText heightBtn;
    EditText weightBtn;
    Spinner bloodtypeSpinner;
    Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        updateBtn = findViewById(R.id.updateInfoBtn);
        heightBtn = findViewById(R.id.heightBtn);
        weightBtn = findViewById(R.id.weightBtn);
        final Spinner specialiteList = (Spinner) findViewById(R.id.bloodType);
        ArrayAdapter<CharSequence> adapterSpecialiteList = ArrayAdapter.createFromResource(this,
                R.array.blood_spinner, android.R.layout.simple_spinner_item);
        adapterSpecialiteList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialiteList.setAdapter(adapterSpecialiteList);

        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email");

        FirebaseFirestore.getInstance().collection("Patient").document(patient_email).collection("moreInfo")
                .document(patient_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                weightBtn.setText( ""+documentSnapshot.getString("weight"));
                heightBtn.setText( ""+documentSnapshot.getString("height"));
                if(documentSnapshot.getString("bloodType") != null)
                specialiteList.setSelection(convertBloodToInt(documentSnapshot.getString("bloodType")));
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("height",""+heightBtn.getText());
                map.put("weight",""+weightBtn.getText());
                map.put("bloodType",""+specialiteList.getSelectedItem().toString());
                Log.e("tag", "onClick: "+specialiteList.getTag() );
                FirebaseFirestore.getInstance().collection("Patient").document(patient_email).collection("moreInfo")
                        .document(patient_email).set(map);
                Toast.makeText(PatientInfoActivity.this,"Update Success!",Toast.LENGTH_SHORT).show();

            }
        });
       if(Common.CurrentUserType.equals("patient")){
            updateBtn.setVisibility(View.GONE);
            heightBtn.setEnabled(false);
            weightBtn.setEnabled(false);
            specialiteList.setEnabled(false);
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


