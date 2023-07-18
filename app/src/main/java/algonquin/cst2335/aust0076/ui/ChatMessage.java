package algonquin.cst2335.aust0076.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name="message")
    String message;
    @ColumnInfo(name = "TimeSent")
    String timeSent;
    @ColumnInfo(name = "SendOrReceive")
    boolean isSentButton;

    public ChatMessage(String m, String t, boolean sent){
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public ChatMessage() {

    }

    String getMessage() { return message; }
    String getTimeSent() { return timeSent; }
    boolean getIsSentButton() { return isSentButton; }


}
