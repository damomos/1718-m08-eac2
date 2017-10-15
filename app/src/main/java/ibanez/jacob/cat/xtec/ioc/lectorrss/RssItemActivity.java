package ibanez.jacob.cat.xtec.ioc.lectorrss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ibanez.jacob.cat.xtec.ioc.lectorrss.model.RssItem;
import ibanez.jacob.cat.xtec.ioc.lectorrss.utils.ConnectionUtils;
import ibanez.jacob.cat.xtec.ioc.lectorrss.utils.DateUtils;

/**
 * Activity to display a single {@link RssItem}
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class RssItemActivity extends AppCompatActivity {

    /**
     * The constant EXTRA_ITEM for sending a {@link RssItem} between activities inside an {@link Intent}.
     */
    public static final String EXTRA_ITEM = RssItemActivity.class.getCanonicalName() + ".ITEM";
    public static final int MAX_CHARACTERS = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item);

        //Get the reference of the web view in the layout
        WebView webView = (WebView) findViewById(R.id.web_view);

        //we receive the extras from the main activity and retrieve the RssItem from it
        Bundle extras = getIntent().getExtras();
        RssItem item = (RssItem) extras.getSerializable(EXTRA_ITEM);

        if (item != null) {
            if (getSupportActionBar() != null) {
                //Change the toolbar's title
                getSupportActionBar().setTitle(item.getTitle());
            }

            if (ConnectionUtils.hasConnection(this)) {
                //if there's internet connection, show the link of the item in the web view
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(item.getLink());
            } else {
                //otherwise, create an HTML with title, description, author, categories, and publish date
                //and show it in the web view
                String html = buildHtmlFromItem(item);
                webView.loadData(html, "text/html", null);
            }
        }
    }

    private String buildHtmlFromItem(RssItem item) {
        //we check if description is too long
        String description = item.getDescription();
        boolean descriptionIsTooLong = item.getDescription().length() > MAX_CHARACTERS;
        //we create resumed description if it is too long
        if (descriptionIsTooLong) {
            String paddingString = "... ";
            description = item.getDescription().substring(0, MAX_CHARACTERS - paddingString.length()) + paddingString;
        }

        return String.format("<h3>%s</h3>" +
                        "<hr>" +
                        "<p>%s</p>" +
                        "<hr>" +
                        "<p style='text-align: end'><em>%s</em></p>" +
                        "<hr>" +
                        "<p><i>%s:</i> %s</p>" +
                        "</p>" +
                        "<p>%s</p>",
                item.getTitle(),
                description,
                item.getAuthor(),
                getString(R.string.item_categories),
                item.getCategories(),
                DateUtils.dateToString(item.getPubDate(), DateUtils.RSS_DATE_FORMAT)
        );
    }
}
