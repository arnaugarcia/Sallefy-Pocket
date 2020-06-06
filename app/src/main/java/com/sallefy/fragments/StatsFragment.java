package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.sallefy.R;
import com.sallefy.managers.playlists.MostFollowedPlaylistsCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatsFragment extends Fragment implements MostFollowedPlaylistsCallback {

    private static final String TAG = "StatsFragment";

    private static StatsFragment instance;
    private Context mContext;
    private AnyChartView anyChartView;

    private List<Playlist> mPlaylists;

    public StatsFragment(Context context) {
        this.mContext = context;
        mPlaylists = new ArrayList<>();
    }

    public static StatsFragment getInstance(Context context) {
        if (instance == null) instance = new StatsFragment(context);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMostFollowedPlaylists();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
    }

    void createBarChart(){
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        for (Playlist p : mPlaylists) {
            data.add(new ValueDataEntry(p.getName(), p.getFollowers()));
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Top 10 Playlists by followers");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Playlists");
        cartesian.yAxis(0).title("Followers");

        anyChartView.setChart(cartesian);
    }


    private void getMostFollowedPlaylists() {
        PlaylistManager.getInstance().getMostFollowedPlaylists(this);
    }

    @Override
    public void onMostFollowedPlaylistsSuccess(List<Playlist> playlists) {
        playlists.sort(Comparator.comparing(Playlist::getFollowers).reversed());
        mPlaylists = playlists.stream().limit(10).collect(Collectors.toList());
        createBarChart();
    }

    @Override
    public void onMostFollowedPlaylistsFailure(Throwable throwable) {
        Log.d(TAG, "onMostFollowedPlaylistsFailure: " + throwable.getMessage());
        Toast.makeText(mContext, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }
}
