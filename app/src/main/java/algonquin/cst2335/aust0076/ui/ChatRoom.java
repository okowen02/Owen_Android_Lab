package algonquin.cst2335.aust0076.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    ChatMessageDAO mDAO;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        if(messages == null) {
            chatModel.messages.postValue( messages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.send.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage s = new ChatMessage(
                    binding.newMessage.getText().toString(),
                    currentDateAndTime,
                    true
            );

            Executor t = Executors.newSingleThreadExecutor();
            t.execute(() -> {
                s.id = mDAO.insertMessage(s);
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });

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

            Executor t = Executors.newSingleThreadExecutor();
            t.execute(() -> {
                r.id = mDAO.insertMessage(r);
            });

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
                if(messages.get(position).getIsSentButton()) return 0;
                return 1;
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            tx.add(R.id.fragmentlocation, chatFragment)
                    .replace(R.id.fragmentlocation, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }


    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        TextView messageID = findViewById(R.id.databaseDetails);
//        TextView messageText = findViewById(R.id.);

            if(item.getItemId() == R.id.item_1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete this message?")
                        .setTitle("Question:")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage m = messages.get(pos);
                            Executor t = Executors.newSingleThreadExecutor();
                            t.execute(() -> {
                                mDAO.deleteMessage(m);
                            });
                            messages.remove(pos);
                            myAdapter.notifyItemRemoved(pos);
//                        Snackbar.make(messageText, "This message has been deleted.", Snackbar.LENGTH_LONG)
//                                .setAction("Undo", c -> {
//                                    messages.add(pos, m);
//                                    myAdapter.notifyItemInserted(pos);
//                                })
//                                .show();
                            Toast.makeText(this, "Message deleted.", Toast.LENGTH_LONG).show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .create().show();
            }
            else if(item.getItemId() == R.id.item_2) {
                Toast.makeText(this, "Version 1.0, created by Owen Austin", Toast.LENGTH_LONG).show();
            }

        return true;
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                pos = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(pos);

                chatModel.selectedMessage.postValue(selected);
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }

}