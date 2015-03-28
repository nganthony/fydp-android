package com.uwaterloo.fydp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.uwaterloo.fydp.api.ApplicationException;
import com.uwaterloo.fydp.constants.BinSystemConstants;
import com.uwaterloo.fydp.domain.Bin;
import com.uwaterloo.fydp.domain.BinData;
import com.uwaterloo.fydp.resource.BinDataResource;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a single Bin detail screen.
 * This fragment is either contained in a {@link BinListActivity}
 * in two-pane mode (on tablets) or a {@link BinDetailActivity}
 * on handsets.
 */
public class BinDetailFragment extends Fragment {
    /**
     * The fragment argument representing the bin that this fragment
     * represents.
     */
    public static final String ARG_BIN_ITEM = "bin_item";

    private Bin bin;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Button purchaseButton;
    private TextView inventoryLevelTextView;
    private XYPlot inventoryPlot;
    private Spinner inventoryPlotSpinner;
    private TextView inventoryPlotDates;

    private ImageButton previousDateButton;
    private ImageButton nextDateButton;

    Long startDate;
    Long endDate;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BinDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_BIN_ITEM)) {
            bin = (Bin)getArguments().get(ARG_BIN_ITEM);
        }

        // Get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // Get start of this week in milliseconds
        //cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDate = cal.getTimeInMillis();

        // Start of the end date
        cal.add(Calendar.HOUR_OF_DAY, 1);
        endDate = cal.getTimeInMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bin_detail, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.fragment_bin_detail_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetBinDataTask().execute();
            }
        });

        purchaseButton = (Button)rootView.findViewById(R.id.bin_detail_purchase_button);
        inventoryLevelTextView = (TextView)rootView.findViewById(R.id.bin_detail_inventory_level);
        inventoryPlot = (XYPlot)rootView.findViewById(R.id.bin_detail_inventory_plot);

        if (bin != null) {
            ((TextView) rootView.findViewById(R.id.bin_detail)).setText(bin.getName());
        }

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(bin.getPurchaseUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        inventoryPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        inventoryPlot.getBackgroundPaint().setColor(Color.TRANSPARENT);
        inventoryPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);

        inventoryPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        inventoryPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);

        inventoryPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        inventoryPlot.getBorderPaint().setStrokeWidth(1);
        inventoryPlot.getBorderPaint().setAntiAlias(false);
        inventoryPlot.getBorderPaint().setColor(Color.TRANSPARENT);

        inventoryPlot.getGraphWidget().getDomainLabelPaint().setTextSize(25);
        inventoryPlot.getGraphWidget().getRangeLabelPaint().setTextSize(25);

        inventoryPlot.getGraphWidget().setMarginBottom(60);
        inventoryPlot.getGraphWidget().setMarginTop(30);
        inventoryPlot.getGraphWidget().setMarginRight(30);
        inventoryPlot.getGraphWidget().setMarginLeft(60);

        inventoryPlot.getGraphWidget().setGridPadding(15, 15, 15, 15);

        inventoryPlot.getGraphWidget().setDomainLabelOrientation(-90);

        inventoryPlot.setRangeBoundaries(0, 1000, BoundaryMode.FIXED);
        inventoryPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 100);

        inventoryPlotSpinner = (Spinner)rootView.findViewById(R.id.inventory_plot_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.inventory_plot_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inventoryPlotSpinner.setAdapter(adapter);

        inventoryPlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {     // Hour
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(startDate);
                    cal.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

                    startDate = cal.getTimeInMillis();

                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    endDate = cal.getTimeInMillis();
                }
                else if(position == 1) {       // Day

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(startDate);
                    cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !

                    startDate = cal.getTimeInMillis();

                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    endDate = cal.getTimeInMillis();
                }

                updateInventoryPlotDomainSteps();
                updateInventoryPlotDates();

                inventoryPlot.setDomainBoundaries((Number)startDate, (Number)endDate, BoundaryMode.FIXED);
                inventoryPlot.redraw();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inventoryPlotDates = (TextView)rootView.findViewById(R.id.inventory_plot_dates);
        updateInventoryPlotDates();

        updateInventoryPlotDomainSteps();

        previousDateButton = (ImageButton)rootView.findViewById(R.id.previous_dates);
        previousDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = inventoryPlotSpinner.getSelectedItemPosition();

                if(position == 0) {     // Hour
                    startDate -= 3600000;
                    endDate -= 3600000;
                }
                else if(position == 1) {    // Day
                    startDate -= 86400000;
                    endDate -= 86400000;
                }

                inventoryPlot.setDomainBoundaries((Number)startDate, (Number)endDate, BoundaryMode.FIXED);
                inventoryPlot.redraw();

                updateInventoryPlotDates();
            }
        });

        nextDateButton = (ImageButton)rootView.findViewById(R.id.next_dates);
        nextDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = inventoryPlotSpinner.getSelectedItemPosition();

                if(position == 0) {     // Hour
                    startDate += 3600000;
                    endDate += 3600000;
                }
                else if(position == 1) {    // Day
                    startDate += 86400000;
                    endDate += 86400000;
                }

                inventoryPlot.setDomainBoundaries((Number)startDate, (Number)endDate, BoundaryMode.FIXED);
                inventoryPlot.redraw();

                updateInventoryPlotDates();
            }
        });

        new GetBinDataTask().execute();

        return rootView;
    }

    private void updateInventoryPlotDates() {
        int position = inventoryPlotSpinner.getSelectedItemPosition();

        if(position == 0) {     // Hour
            SimpleDateFormat df1 = new SimpleDateFormat("d MMMM yyyy");
            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");

            String startDateText = df1.format(startDate);
            String startTimeText = df2.format(startDate);
            String endTimeText = df2.format(endDate);

            inventoryPlotDates.setText(startDateText + ", " + startTimeText + " - " + endTimeText);
        }
        else if(position == 1) {    // Day
            SimpleDateFormat df1 = new SimpleDateFormat("d MMMM yyyy");

            String startDateText = df1.format(startDate);

            inventoryPlotDates.setText(startDateText);
        }
    }

    private void updateInventoryPlotDomainSteps() {
        int position = inventoryPlotSpinner.getSelectedItemPosition();

        String dateFormatString = "";

        if(position == 0) {
            // Increment every 5 minutes
            inventoryPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 300000);
            dateFormatString = "HH:mm";
        }
        else if(position == 1) {
            // Increment every one hour
            inventoryPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 3600000);
            dateFormatString = "HH:mm";
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        inventoryPlot.setDomainValueFormat(new Format() {

            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                long timestamp = ((Number) obj).longValue();
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });
    }

    private class GetBinDataTask extends AsyncTask<Void, Void, List<BinData>> {

        boolean getBinDataSuccessful = true;

        @Override
        protected List<BinData> doInBackground(Void... params) {
            List<BinData> binData = null;

            try {
                binData = BinDataResource.getBinData(BinSystemConstants.BIN_SYSTEM_ID, bin.getId());
            } catch (ApplicationException e) {
                getBinDataSuccessful = false;
                e.printStackTrace();
            }

            return binData;
        }

        @Override
        protected void onPostExecute(List<BinData> binData) {
            swipeRefreshLayout.setRefreshing(false);

            // Load bin data in graph
            if(getBinDataSuccessful) {
                if(binData != null) {
                    List<Number> values = new ArrayList<Number>();
                    List<Number> timestamps = new ArrayList<Number>();

                    for(BinData data: binData) {
                        values.add(data.getData());
                        timestamps.add(data.getCreatedTimestamp().getTime());
                    }

                    XYSeries series = new SimpleXYSeries(timestamps, values, "");

                    // Create a formatter to use for drawing a series using LineAndPointRenderer:
                    LineAndPointFormatter seriesFormat = new LineAndPointFormatter(
                            getActivity().getResources().getColor(R.color.secondary_text),                   // line color
                            getActivity().getResources().getColor(R.color.primary_text),                     // point color
                            Color.TRANSPARENT,                                                               // fill color
                            null);

                    seriesFormat.getLinePaint().setStrokeWidth(3);
                    seriesFormat.getVertexPaint().setStrokeWidth(9);

                    inventoryPlot.clear();
                    inventoryPlot.addSeries(series, seriesFormat);
                    inventoryPlot.setDomainBoundaries((Number)startDate, (Number)endDate, BoundaryMode.FIXED);
                    inventoryPlot.redraw();

                    String inventoryLevelString = "Current inventory level: " + "<b>"+ 0 + "</b>" + "%";
                    if(binData.size() != 0) {
                        int inventoryLevel = (int) ((binData.get(binData.size() - 1).getData() / bin.getTotalMass()) * 100);
                        if (inventoryLevel > 100) {
                            inventoryLevel = 100;
                        }

                        inventoryLevelString = "Current inventory level: " + "<b>"+ inventoryLevel + "</b>" + "%";
                    }

                    inventoryLevelTextView.setText(Html.fromHtml(inventoryLevelString));
                }

            }
        }
    }
}
