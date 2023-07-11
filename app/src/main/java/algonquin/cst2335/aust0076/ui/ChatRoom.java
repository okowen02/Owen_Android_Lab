package algonquin.cst2335.aust0076.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.aust0076.R;
import algonquin.cst2335.aust0076.data.ChatRoomViewModel;
import algonquin.cst2335.aust0076.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.aust0076.databinding.RecieveMessageBinding;
import algonquin.cst2335.aust0076.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null) {
            chatModel.messages.postValue( messages = new ArrayList<>());
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.send.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage s = new ChatMessage(
                    binding.newMessage.getText().toString(),
                    currentDateAndTime,
                    true
            );

            messages.add(s);
            binding.newMessage.setText("");
            myAdapter.notifyItemInserted(messages.size()-1);

        });

        binding.recieve.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage r = new ChatMessage(
                    binding.newMessage.getText().toString(),
                    currentDateAndTime,
                    false
            );

            messages.add(r);
            binding.newMessage.setText("");
            myAdapter.notifyItemInserted(messages.size()-1);
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                if(viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
                else {
                    RecieveMessageBinding binding = RecieveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                if(messages.get(position).getIsSentButton() == true) return 0;
                return 1;
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }

    public class ChatMessage {
        String message;
        String timeSent;
        boolean isSentButton;

        public ChatMessage(String m, String t, boolean sent){
            message = m;
            timeSent = t;
            isSentButton = sent;
        }

        String getMessage() { return message; }
        String getTimeSent() { return timeSent; }
        boolean getIsSentButton() { return isSentButton; }


    }

}