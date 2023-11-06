package com.ensias.healthcareapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ensias.healthcareapp.Common.Common;
import com.ensias.healthcareapp.Interface.IRecyclerItemSelectedListener;
import com.ensias.healthcareapp.R;
import com.ensias.healthcareapp.model.ApointementInformation;
import com.ensias.healthcareapp.model.TimeSlot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {


    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;
    SimpleDateFormat simpleDateFormat;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent,false);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if(timeSlotList.size()==0){
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

        }
        else{
            for (TimeSlot slotValue:timeSlotList){
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == position){

                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

                    holder.txt_time_slot_description.setText("Full");
                    if(slotValue.getType().equals("Checked"))
                        holder.txt_time_slot_description.setText("Choosen");
                    holder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));

                }
            }

        }
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);


            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void onItemSelectedListener(View view, int pos) {
                    for(CardView cardView:cardViewList) {
                        if (cardView.getTag() == null)
                            cardView.setCardBackgroundColor(context.getResources()
                                    .getColor(android.R.color.white));
                    }
                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.holo_orange_dark));

                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                    intent.putExtra(Common.KEY_TIME_SLOT,position);
                    Common.currentTimeSlot = position ;
                    intent.putExtra(Common.KEY_STEP,2);
                    Log.e("pos ", "onItemSelectedListener: "+position );
                    localBroadcastManager.sendBroadcast(intent);
                    if(Common.CurrentUserType == "doctor" && holder.txt_time_slot_description.getText().equals("Available")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(holder.card_time_slot.getContext());
                        alert.setTitle("Block");
                        alert.setMessage("Are you sure you want to block?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ApointementInformation apointementInformation = new ApointementInformation();
                                apointementInformation.setApointementType(Common.Currentaappointementatype);
                                apointementInformation.setDoctorId(Common.CurreentDoctor);
                                apointementInformation.setDoctorName(Common.CurrentDoctorName);
                                apointementInformation.setChemin("Doctor/"+Common.CurreentDoctor+"/"+Common.simpleFormat.format(Common.currentDate.getTime())+"/"+String.valueOf(Common.currentTimeSlot));
                                apointementInformation.setType("full");
                                apointementInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                                        .append("at")
                                        .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
                                apointementInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

                                DocumentReference bookingDate = FirebaseFirestore.getInstance()
                                        .collection("Doctor")
                                        .document(Common.CurreentDoctor)
                                        .collection(Common.simpleFormat.format(Common.currentDate.getTime()))
                                        .document(String.valueOf(Common.currentTimeSlot));

                                bookingDate.set(apointementInformation);
                                dialog.dismiss();
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        alert.show();

                    }
                }
            });
        }



    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot,txt_time_slot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
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



