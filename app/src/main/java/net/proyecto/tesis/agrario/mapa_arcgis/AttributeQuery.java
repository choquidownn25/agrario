package net.proyecto.tesis.agrario.mapa_arcgis;

/**
 * Created by choqu_000 on 07/10/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import net.proyecto.tesis.agrario.R;

public class AttributeQuery extends Activity {

    MapView mMapView;
    GraphicsLayer graphicsLayer;
    Graphic fillGraphic;
    String queryLayer;
    boolean boolQuery = true;
    ProgressDialog progress;

    final static int HAS_RESULTS = 1;
    final static int NO_RESULT = 2;
    final static int CLEAR_RESULT = 3;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_activity_query);
        mMapView = (MapView) findViewById(R.id.map);

        // get query service
        queryLayer = getResources().getString(R.string.query_service);
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {
                    graphicsLayer = new GraphicsLayer();
                    SimpleRenderer sr = new SimpleRenderer(
                            new SimpleFillSymbol(Color.RED));
                    graphicsLayer.setRenderer(sr);
                    mMapView.addLayer(graphicsLayer);

                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case R.id.avg_household:
            String targetLayer = queryLayer.concat("/3");
            String[] queryArray = { targetLayer, "AVGHHSZ_CY>3.5" };
            AsyncQueryTask ayncQuery = new AsyncQueryTask();
            ayncQuery.execute(queryArray);
            return true;

        case R.id.reset:
            graphicsLayer.removeAll();
            boolQuery = true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 
     * Query Task executes asynchronously.
     * 
     */
    private class AsyncQueryTask extends AsyncTask<String, Void, FeatureResult> {

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(AttributeQuery.this);

            progress = ProgressDialog.show(AttributeQuery.this, "",
                    "Please wait....query task is executing");

        }

        /**
         * First member in string array is the query URL; second member is the
         * where clause.
         */
        @Override
        protected FeatureResult doInBackground(String... queryArray) {

            if (queryArray == null || queryArray.length <= 1)
                return null;

            String url = queryArray[0];
            QueryParameters qParameters = new QueryParameters();
            String whereClause = queryArray[1];
            SpatialReference sr = SpatialReference.create(102100);
            qParameters.setGeometry(mMapView.getExtent());
            qParameters.setOutSpatialReference(sr);
            qParameters.setReturnGeometry(true);
            qParameters.setWhere(whereClause);

            QueryTask qTask = new QueryTask(url);

            try {
                FeatureResult results = qTask.execute(qParameters);
                return results;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(FeatureResult results) {

            String message = "No result comes back";

            if (results != null) {
                int size = (int) results.featureCount();
                for (Object element : results) {
                    progress.incrementProgressBy(size / 100);
                    if (element instanceof Feature) {
                        Feature feature = (Feature) element;
                        // turn feature into graphic
                        Graphic graphic = new Graphic(feature.getGeometry(),
                                feature.getSymbol(), feature.getAttributes());
                        // add graphic to layer
                        graphicsLayer.addGraphic(graphic);
                    }
                }
                // update message with results
                message = String.valueOf(results.featureCount())
                        + " results have returned from query.";

            }
            progress.dismiss();
            Toast toast = Toast.makeText(AttributeQuery.this, message,
                    Toast.LENGTH_LONG);
            toast.show();
            boolQuery = false;

        }

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