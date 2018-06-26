package com.dicoding.hendropurwoko.mysubmission01new;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpComingFragment extends Fragment {
    public MovieClass movieClass;
    public ArrayList<MovieClass> upComingList;
    public RecyclerView rvUpComing;
    public ProgressDialog pDialog;

    String URL = "https://api.themoviedb.org/3/movie/upcoming?api_key="+ MainActivity.API + "&language=en-US";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_up_coming, container, false);

        View rootView = inflater.inflate(R.layout.fragment_up_coming, container, false);
        rvUpComing = (RecyclerView)rootView.findViewById(R.id.rv_upcoming);

        upComingList = new ArrayList<>();
        UpComingAsync upComingAsync = new UpComingAsync();
        upComingAsync.execute(URL);

        return rootView;
    }

    private class UpComingAsync extends AsyncTask<String,Void, ArrayList<MovieClass>> {
        @Override
        protected ArrayList<MovieClass> doInBackground(String... strings) {
            SyncHttpClient client = new SyncHttpClient();

            client.get(URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray jsonArray = jsonObj.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length() ; i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            movieClass = new MovieClass();

                            movieClass.setTitle(data.getString("title").toString().trim());
                            movieClass.setOverview(data.getString("overview").toString().trim());
                            movieClass.setReleaseDate(data.getString("release_date").toString().trim());
                            movieClass.setPopularity(data.getString("popularity").toString().trim());
                            movieClass.setVote(data.getString("vote_average").toString().trim());
                            movieClass.setPoster("http://image.tmdb.org/t/p/w185"+data.getString("poster_path").toString().trim());

                            upComingList.add(movieClass);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(MainActivity.TAG, statusCode +": "+ error.toString());
                }
            });

            return upComingList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.please_wait));//ambil resource string
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieClass> movieClasses) {
            super.onPostExecute(movieClasses);

            if (pDialog.isShowing())
                pDialog.dismiss();

            displayList();
        }
    }

    private void displayList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUpComing.setLayoutManager(linearLayoutManager);

        NowPlayingAdapter nowPlayingAdapter = new NowPlayingAdapter(this.getActivity());//di buat pada DataAdapter.java
        nowPlayingAdapter.setNowPlayingList(upComingList);
        nowPlayingAdapter.getListData();
        rvUpComing.setAdapter(nowPlayingAdapter);
    }
}
