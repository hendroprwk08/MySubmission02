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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchFragment extends Fragment {
    public MovieClass movieClass;
    public ArrayList<MovieClass> searchList;
    public RecyclerView rvSearch;
    public EditText etCari;
    public Button btCari;
    public ProgressDialog pDialog;
    public String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false);

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        etCari = (EditText)rootView.findViewById(R.id.et_cari);
        btCari = (Button)rootView.findViewById(R.id.bt_cari);
        rvSearch = (RecyclerView)rootView.findViewById(R.id.rv_search);

        btCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CARI = etCari.getText().toString().trim();
                URL = "https://api.themoviedb.org/3/search/movie?api_key=" + MainActivity.API + "&language=en-US&query=" + CARI;

                searchList = new ArrayList<>();
                SearchAsync searchAsync = new SearchAsync();
                searchAsync.execute(URL);
            }
        });

        return rootView;
    }

    private class SearchAsync extends AsyncTask<String, Void, ArrayList<MovieClass>> {
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

        @Override
        protected ArrayList<MovieClass> doInBackground(String... strings) {
            SyncHttpClient client = new SyncHttpClient();

            client.get(URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray jsonArray = jsonObj.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            movieClass = new MovieClass();

                            movieClass.setTitle(data.getString("title").toString().trim());
                            movieClass.setOverview(data.getString("overview").toString().trim());
                            movieClass.setReleaseDate(data.getString("release_date").toString().trim());
                            movieClass.setPopularity(data.getString("popularity").toString().trim());
                            movieClass.setVote(data.getString("vote_average").toString().trim());
                            movieClass.setPoster("http://image.tmdb.org/t/p/w185" + data.getString("poster_path").toString().trim());

                            searchList.add(movieClass);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(MainActivity.TAG, statusCode + ": " + error.toString());
                }
            });

            return searchList;
        }
    }

    private void displayList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(linearLayoutManager);

        SearchAdapter nowPlayingAdapter = new SearchAdapter(this.getActivity());//di buat pada DataAdapter.java
        nowPlayingAdapter.setSearchList(searchList);
        nowPlayingAdapter.getListData();
        rvSearch.setAdapter(nowPlayingAdapter);
    }
}
