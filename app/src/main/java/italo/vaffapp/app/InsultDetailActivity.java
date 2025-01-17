package italo.vaffapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import italo.vaffapp.app.common.CommonMethods;
import italo.vaffapp.app.common.CommonSharedPrefsMethods;


/**
 * An activity representing a single Insult detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link InsultListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link InsultDetailFragment}.
 */
public class InsultDetailActivity extends ActionBarActivity {

    private int shared_insults; // # of times a person shares an insult

    final String pref_language_string = "pref_language";
    private int pref_language = 0; // default let's keep the int for Italian

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insult_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent mIntent = getIntent();
        pref_language = mIntent.getIntExtra(pref_language_string, pref_language);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(InsultDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(InsultDetailFragment.ARG_ITEM_ID));
            arguments.putInt(pref_language_string, pref_language);
            InsultDetailFragment fragment = new InsultDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.insult_detail_container, fragment)
                    .commit();
        }

        /* This is taken from InsultActivity's same method */
        CommonSharedPrefsMethods.setupSharedPrefsMethods(this);
        shared_insults = CommonSharedPrefsMethods.getInt("shared_insults", 0);
    }

    // 2. configure a callback handler that's invoked when the share dialog closes and control returns to the calling app
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* This below is taken from InsultActivity's same method */
        // when a user shares and then the program returns to the VaffApp
        if (requestCode == CommonMethods.SHARE_REQUEST) {
            // I have to comment the instruction below, it works only for Twitter
            // all the others app return always RESULT_CANCELLED 0 or RESULT_OK -1 (Facebook)
            //if (resultCode == RESULT_OK) {
            increaseSharedInsult();
        }
    }

    /* This is a copy of InsultActivity's same method */
    public void increaseSharedInsult(){
        shared_insults++;

        CommonMethods.checkSharedInsults(this, getString(R.string.share_reward), shared_insults);

        CommonSharedPrefsMethods.putInt("shared_insults", shared_insults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, InsultListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
