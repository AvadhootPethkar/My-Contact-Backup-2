import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by avadh on 2/27/2018.
 */

public class ViewBackupList {

    public void RestoreContactsIntoArrayList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("abc");
    }
}
