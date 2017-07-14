package css.csslibrary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BugReportFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bug_report,container,false);
    }
    @Override
    public void onStart() {
        super.onStart();
        TextView textView=(TextView)getView().findViewById(R.id.text_email);
        Linkify.addLinks(textView,Linkify.EMAIL_ADDRESSES);
    }
}
