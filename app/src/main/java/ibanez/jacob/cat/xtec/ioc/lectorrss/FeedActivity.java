package ibanez.jacob.cat.xtec.ioc.lectorrss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity to display a single Feed entry
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }
}
