package css.csslibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class BugReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        TextView textView=(TextView)findViewById(R.id.text_email);
        Linkify.addLinks(textView,Linkify.EMAIL_ADDRESSES);
    }
}
