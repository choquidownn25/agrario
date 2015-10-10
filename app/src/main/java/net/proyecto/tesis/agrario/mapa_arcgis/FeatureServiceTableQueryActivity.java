package net.proyecto.tesis.agrario.mapa_arcgis;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.tasks.query.QueryParameters;

import net.proyecto.tesis.agrario.R;

import org.codehaus.jackson.JsonParser;

/**
 * Created by choqu_000 on 07/10/2015.
 */
public class FeatureServiceTableQueryActivity extends Activity {

    final String FEATURE_SERVICE_URL = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/DamageAssessment/FeatureServer";
    final String DAMAGE_FIELD_NAME = "typdamage";
    final String CAUSE_FIELD_NAME = "primcause";
    public FeatureLayer featureLayer;
    public GeodatabaseFeatureServiceTable featureServiceTable;
    MapView mMapView;
    Spinner mDamageSpinner;
    Spinner mCauseSpinner;
    ArrayAdapter<String> damageAdapter;
    ArrayAdapter<String> causeAdapter;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_query_map);
        //Llama el mapa
        mMapView=(MapView)findViewById(R.id.map);
        // Create a GeodatabaseFeatureServiceTable from the URL of a feature service.
        featureServiceTable = new GeodatabaseFeatureServiceTable(FEATURE_SERVICE_URL, 0);

        // Initialize this GeodatabaseFeatureService to fill it with features from the service.
        featureServiceTable.initialize(new CallbackListener<GeodatabaseFeatureServiceTable.Status>() {

            @Override
            public void onCallback(GeodatabaseFeatureServiceTable.Status status) {

            }

            @Override
            public void onError(Throwable ex) {
                showToast("Error initializing FeatureServiceTable");
            }


            public void onCallback(AsyncTask.Status arg0) {

                // Create a FeatureLayer from the initialized GeodatabaseFeatureServiceTable.
                featureLayer = new FeatureLayer(featureServiceTable);

                // Emphasize the selected features by increasing selection halo size.
                featureLayer.setSelectionColorWidth(20);
                featureLayer.setSelectionColor(-16711936);

                // Add the feature layer to the map.
                mMapView.addLayer(featureLayer);

                // Set up spinners to contain values from the layer to query against.
                setupQuerySpinners();

                // Get the fields that will be used to query the layer.
                Field damageField = featureServiceTable.getField(DAMAGE_FIELD_NAME);
                Field causeField = featureServiceTable.getField(CAUSE_FIELD_NAME);

                // Retrieve the possible domain values for each field and add to the spinner data adapters.
                CodedValueDomain damageDomain = (CodedValueDomain) damageField.getDomain();
                CodedValueDomain causeDomain = (CodedValueDomain) causeField.getDomain();
                damageAdapter.addAll(damageDomain.getCodedValues().values());
                causeAdapter.addAll(causeDomain.getCodedValues().values());

                // On the main thread, connect up the spinners with the filled data adapters.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDamageSpinner.setAdapter(damageAdapter);
                        mCauseSpinner.setAdapter(causeAdapter);
                    }
                });
            }
        });

    }

    public void onClick_okButton(View v) {
        // Build and execute the query.

        // First, check layer exists, and clear any previous selection from the layer.
        if (featureLayer == null) {
            showToast("Feature layer is not set.");
            return;
        }
        featureLayer.clearSelection();

        // Build query predicates to construct a query where clause from selected values.
        String damageType = String.valueOf(mDamageSpinner.getSelectedItem());
        String primCause = String.valueOf(mCauseSpinner.getSelectedItem());
        String whereClause = "typdamage LIKE '" + damageType + "' AND primcause LIKE '" + primCause + "'";

        // Create query parameters, based on the constructed where clause.
        QueryParameters queryParams = new QueryParameters();
        queryParams.setWhere(whereClause);

        // Execute the query and create a callback for dealing with the results of the query.
        featureServiceTable.queryFeatures(queryParams, new CallbackListener<FeatureResult>() {

            @Override
            public void onError(Throwable ex) {
                // Highlight errors to the user.
                showToast("Error querying FeatureServiceTable");
            }

            @Override
            public void onCallback(FeatureResult objs) {

                // If there are no query results, inform user.
                if (objs.featureCount() < 1) {
                    showToast("No results");
                    return;
                }

                // Report number of results to user.
                showToast("Found " + objs.featureCount() + " features.");

                // Iterate the results and select each feature.
                for (Object objFeature : objs) {
                    //featureLayer.selectFeature(feature.getId());
                    JsonParser.Feature feature = (JsonParser.Feature) objFeature;
                    featureLayer.selectFeature(feature.getMask());
                }
            }
        });
    }

    public void setupQuerySpinners() {
        // Get the spinner controls from the layout.
        mDamageSpinner = (Spinner) findViewById(R.id.damageSpinner);
        mCauseSpinner = (Spinner) findViewById(R.id.causeSpinner);

        // Set up array adapters to contain the values in the spinners.
        damageAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
        causeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
    }

    public void showToast(final String message) {
        // Show toast message on the main thread only; this function can be
        // called from query callbacks that run on background threads.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FeatureServiceTableQueryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }

}
