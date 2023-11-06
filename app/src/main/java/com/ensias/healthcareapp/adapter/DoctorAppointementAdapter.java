package com.ensias.healthcareapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensias.healthcareapp.ChatActivity;
import com.ensias.healthcareapp.R;
import com.ensias.healthcareapp.model.ApointementInformation;
import com.ensias.healthcareapp.model.Doctor;
import com.ensias.healthcareapp.model.Patient;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorAppointementAdapter extends FirestoreRecyclerAdapter<ApointementInformation, DoctorAppointementAdapter.MyDoctorAppointementHolder> {
    StorageReference pathReference ;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public DoctorAppointementAdapter(@NonNull FirestoreRecyclerOptions<ApointementInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointementHolder myDoctorAppointementHolder, int position, @NonNull final ApointementInformation apointementInformation) {
        myDoctorAppointementHolder.dateAppointement.setText(apointementInformation.getTime());
        myDoctorAppointementHolder.patientName.setText(apointementInformation.getPatientName());
        myDoctorAppointementHolder.appointementType.setText(apointementInformation.getApointementType());
        myDoctorAppointementHolder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apointementInformation.setType("Accepted");
                FirebaseFirestore.getInstance().collection("Patient").document(apointementInformation.getPatientId()).collection("calendar")
                        .document(apointementInformation.getTime().replace("/","_")).set(apointementInformation);
                FirebaseFirestore.getInstance().document(apointementInformation.getChemin()).update("type","Accepted");
                FirebaseFirestore.getInstance().collection("Doctor").document(apointementInformation.getDoctorId()).collection("calendar")
                        .document(apointementInformation.getTime().replace("/","_")).set(apointementInformation);

//////////// here add patient friend to doctor

                FirebaseFirestore.getInstance().document("Patient/"+apointementInformation.getPatientId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseFirestore.getInstance().collection("Doctor").document(apointementInformation.getDoctorId()+"")
                                        .collection("MyPatients").document(apointementInformation.getPatientId()).set(documentSnapshot.toObject(Patient.class));
                            }
                        });
                FirebaseFirestore.getInstance().document("Doctor/"+apointementInformation.getDoctorId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseFirestore.getInstance().collection("Patient").document(apointementInformation.getPatientId()+"")
                                        .collection("MyDoctors").document(apointementInformation.getPatientId()).set(documentSnapshot.toObject(Doctor.class));
                            }
                        });


                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });
        myDoctorAppointementHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apointementInformation.setType("Refused");
                FirebaseFirestore.getInstance().collection("Patient").document(apointementInformation.getPatientId()).collection("calendar")
                        .document(apointementInformation.getTime().replace("/","_")).set(apointementInformation);
                FirebaseFirestore.getInstance().document(apointementInformation.getChemin()).delete();
                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });

        String imageId = apointementInformation.getPatientId()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(myDoctorAppointementHolder.patient_image.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myDoctorAppointementHolder.patient_image);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void openPage(Context wf, Doctor d){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",d.getEmail()+"_"+ FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        i.putExtra("key2",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()+"_"+d.getEmail());
        wf.startActivity(i);
    }

    @NonNull
    @Override
    public MyDoctorAppointementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointement_item, parent, false);
        return new MyDoctorAppointementHolder(v);
    }

    class MyDoctorAppointementHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorAppointementItems
        TextView dateAppointement;
        TextView patientName;
        Button approveBtn;
        Button cancelBtn;
        TextView appointementType;
        ImageView patient_image;
        public MyDoctorAppointementHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointement = itemView.findViewById(R.id.appointement_date);
            patientName = itemView.findViewById(R.id.patient_name);
            approveBtn = itemView.findViewById(R.id.btn_accept);
            cancelBtn = itemView.findViewById(R.id.btn_decline);
            appointementType = itemView.findViewById(R.id.appointement_type);
            patient_image = itemView.findViewById(R.id.patient_image);
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

