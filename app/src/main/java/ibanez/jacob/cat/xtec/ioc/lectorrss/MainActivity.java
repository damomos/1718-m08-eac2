package ibanez.jacob.cat.xtec.ioc.lectorrss;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import ibanez.jacob.cat.xtec.ioc.lectorrss.model.RssItem;
import ibanez.jacob.cat.xtec.ioc.lectorrss.utils.ConnectionUtils;

/**
 * Main Activity
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<RssItem>> {

    //Tag for logging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout mSearchBar;
    private EditText mSearchQuery;
    private ProgressBar mProgressBar;
    private ItemAdapter mItemAdapter;
    private DBInterface mDataBase;

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
        mDataBase = new DBInterface(this);

        //set the layout manager and the adapter of the recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mItemAdapter);
        //add a decorator to separate items
        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);

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
            getSupportLoaderManager().initLoader(1, null, this).forceLoad();
            //new DownloadRssTask().execute(FEED_CHANNEL);
        } else {
            //otherwise, check if you must load data from database or not
            if (loadFromDatabase) {
                //fill adapter list from database
                mDataBase.open();
                mItemAdapter.setItems(mDataBase.getAllItems());
                mDataBase.close();

                Toast.makeText(this, R.string.toast_offline_load, Toast.LENGTH_SHORT).show();
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


    @Override
    public Loader<List<RssItem>> onCreateLoader(int id, Bundle args) {
        /**
         * This class is for downloading a XML file from the internet in a background thread
         */
        return new AsyncTaskLoader<List<RssItem>>(this) {

            List<RssItem> result = null;
            String FEED_CHANNEL = "http://www.eldiario.es/rss/";

            @Override
            protected void onStartLoading() {
                //super.onStartLoading();
                if(result != null) {
                    // use cached data
                    deliverResult(result);
                } else {
                    // we have no data, so kick off loading it
                    forceLoad();
                }
            }

            @Override
            public List<RssItem> loadInBackground() {

                //get the XML from the feed url and process it
                try {
                    result = getRssItems(FEED_CHANNEL);
                    Log.d("RESULT", result.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                //TODO save to the database all the info of the XML file
                storeResult(result);
                //download thumbnails to the cache directory
                cacheImages(result);

                return result;
            }

            @Override
            public void deliverResult(List<RssItem> data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<RssItem>> loader, List<RssItem> data) {
        mItemAdapter.setItems(data);
    }

    @Override
    public void onLoaderReset(Loader<List<RssItem>> loader) {

    }




//    private class DownloadRssTask extends AsyncTask<String, Void, List<RssItem>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //set progress bar visible and hid recycler view, so we are connecting to the internet
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected List<RssItem> doInBackground(String... strings) {
//            List<RssItem> result = null;
//
//            try {
//                //get the XML from the feed url and process it
//                result = getRssItems(strings[0]);
//                //TODO save to the database all the info of the XML file
//                storeResult(result);
//                //download thumbnails to the cache directory
//                cacheImages(result);
//            } catch (IOException ex) {
//
//            } catch (XmlPullParserException ex) {
//
//            }
//
//            return result;
//        }

//        @Override
//        protected void onPostExecute(List<RssItem> items) {
//            //set progress bar invisible and show recycler view, so the result from the internet has arrived
//            mProgressBar.setVisibility(View.INVISIBLE);
//
//            //feed the list of items of the recycler view's adapter
//            mItemAdapter.setItems(items);
//        }
//    }

    private void storeResult(List<RssItem> result) {
        mDataBase.open();
        for (RssItem item : result) {
            mDataBase.insertItem(item);
        }
        mDataBase.close();
    }

    /**
     *
     * @param result
     */
    private void cacheImages(List<RssItem> result) {
        for (RssItem item : result) {
            try {
                URL imageUrl = new URL(item.getThumbnail());
                InputStream inputStream = (InputStream) imageUrl.getContent();
                byte[] bufferImage = new byte[1024];

                OutputStream outputStream = new FileOutputStream(item.getImagePathInCache());

                int count;
                while ((count = inputStream.read(bufferImage)) != -1) {
                    outputStream.write(bufferImage, 0, count);
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException ex) {
                Log.e(TAG, "Error downloading image from " + item.getThumbnail(), ex);
            }
        }
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private List<RssItem> getRssItems(String url) throws IOException, XmlPullParserException {
        InputStream in = null;
        RssItemParser parser = new RssItemParser(this);
        List<RssItem> result = null;

        try {
            in = ConnectionUtils.openHttpConnection(url);
            result = parser.parse(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return result;
    }
}
