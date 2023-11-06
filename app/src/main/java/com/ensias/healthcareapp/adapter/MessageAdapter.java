package com.ensias.healthcareapp.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ensias.healthcareapp.R;
import com.ensias.healthcareapp.model.Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message,MessageAdapter.MessageHolder> {

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        if(model.getUserSender().equals(getCurrentUser().getEmail()+"") ){
            //holder.text.setTextSize(20);
            //holder.text.setBackgroundColor(0xC0C0C0);
           // CoordinatorLayout.LayoutParams  lllp= (CoordinatorLayout.LayoutParams) holder.text.getLayoutParams();
           // lllp.gravity= Gravity.LEFT;
            //holder.text.setLayoutParams(lllp);
            //
            // holder.text.setBackground(holder.text.getContext().getResources().getDrawable(R.drawable.rounded_message2));
            holder.text.setText(model.getMessage());
            holder.text.setPadding(35,35,35,35);

        }
        else {


            holder.text2.setText(model.getMessage());
            holder.text2.setPadding(35,35,35,35);
        }

    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_chat_item,parent,false);
        return new MessageHolder(v);
    }

    class MessageHolder extends RecyclerView.ViewHolder{

        TextView text;
        TextView text2;

        public MessageHolder(View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.message_item_text);
            text2 = itemView.findViewById(R.id.message_item_text2);
        }
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
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