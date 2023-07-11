package algonquin.cst2335.aust0076.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.aust0076.ui.ChatRoom;

public class ChatRoomViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ChatRoom.ChatMessage>> messages = new MutableLiveData< >();

}
