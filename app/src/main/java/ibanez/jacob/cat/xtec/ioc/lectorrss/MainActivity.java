package ibanez.jacob.cat.xtec.ioc.lectorrss;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ibanez.jacob.cat.xtec.ioc.lectorrss.model.RssItem;
import ibanez.jacob.cat.xtec.ioc.lectorrss.utils.ConnectionUtils;

/**
 * Main Activity
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MainActivity extends AppCompatActivity {

    public static final String FEED_CHANNEL = "http://www.eldiario.es/rss/";

    private LinearLayout mSearchBar;
    private EditText mSearchQuery;
    private ProgressBar mProgressBar;
    private ItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get references to member variables
        mSearchBar = (LinearLayout) findViewById(R.id.search_bar);
        mSearchQuery = (EditText) findViewById(R.id.et_search);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mItemAdapter = new ItemAdapter(this);

        //set the layout manager and the adapter of the recycler view
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mItemAdapter);

        //set onClickListener for the search button
        ImageButton searchButton = (ImageButton) findViewById(R.id.ib_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemAdapter.searchItems(mSearchQuery.getText().toString());
            }
        });

        //feed the recycler view, either from the internet or from the database
        feedRecyclerView(true);
    }

    private void feedRecyclerView(boolean loadFromDatabase) {
        //check for internet connection
        if (ConnectionUtils.hasConnection(this)) {
            //if there is connection, start the execution of the async task
            new DownloadRssTask().execute(FEED_CHANNEL);
        } else {
            //otherwise, check if you must load data from database or not
            if (loadFromDatabase) {
                Toast.makeText(this, R.string.toast_offline_load, Toast.LENGTH_SHORT).show();
                //TODO fill adapter list from database
            } else {
                //you pressed refresh button but there is no connection
                Toast.makeText(this, R.string.toast_there_is_no_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method handles behavior when a menu item is selected
     *
     * @param item The selected item
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //we check which button has been pressed
        switch (id) {
            case R.id.action_refresh:   //refresh button has been pressed
                //refresh the recycler view content
                feedRecyclerView(false);
                return true;
            case R.id.action_search:    //search button has been pressed
                //we only have to toggle the search bar
                toggleSearchBar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleSearchBar() {
        //When not visible, the search bar's visibility has to be GONE, so the layout
        //doesn't occupy space in the parent layout
        if (mSearchBar.getVisibility() == View.GONE) {
            mSearchBar.setVisibility(View.VISIBLE);
        } else if (mSearchBar.getVisibility() == View.VISIBLE) {
            mSearchBar.setVisibility(View.GONE);
        }
    }

    /**
     * This class is for downloading a XML file from the internet in a background thread
     */
    private class DownloadRssTask extends AsyncTask<String, Void, List<RssItem>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //set progress bar visible, so we are connecting to the internet
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<RssItem> doInBackground(String... strings) {

//            try {
//                //Carreguem l'XML
//                return mItemService.getAllRssItems(strings[0]);
//            } catch (IOException e) {
//                //Error de connexió
//                return "Error de connexió";
//                //return getResources().getString(R.string.connection_error);
//            } catch (XmlPullParserException e) {
//                //Error de parse
//                return "Error a l'analitzar l'XML";
//                //return getResources().getString(R.string.xml_error);
//            }
//
//            InputStream in;
//            //TODO get the XML from the feed url and process it
//            try {
//                in = ConnectionUtils.openHttpConnection(strings[0]);
//                List<RssItem> result = mItemService.getAllRssItems();
//            } catch (IOException ex) {
//
//            }finally {
//                if (in != null) {
//                    in.close();
//                }
//            }
//            //TODO save to the database all the info of the XML file
//            //TODO download thumbnails to the cache directory
//            return result;

            List<RssItem> items = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                items.add(new RssItem(
                        "Title " + i,
                        "link",
                        "author",
                        "Description",
                        new Date(),
                        "category",
                        "thumbnail"));
            }

            return items;
        }

        @Override
        protected void onPostExecute(List<RssItem> items) {
            //set progress bar invisible, so the result from the internet has arrived
            mProgressBar.setVisibility(View.INVISIBLE);

            //feed the list of items of the recycler view's adapter
            mItemAdapter.setItems(items);
        }
    }
}
