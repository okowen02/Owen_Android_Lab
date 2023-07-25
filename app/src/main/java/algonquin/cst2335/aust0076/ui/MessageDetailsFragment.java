package algonquin.cst2335.aust0076.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.aust0076.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m) {
        selected = m;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageDetails.setText(selected.getMessage());
        binding.timeDetails.setText(selected.getTimeSent());
        if(selected.getIsSentButton()) binding.sendReceiveDetails.setText("Sent");
        else binding.sendReceiveDetails.setText("Received");
        binding.databaseDetails.setText("ID = " + String.valueOf(selected.getID()));

        return binding.getRoot();
    }
}
