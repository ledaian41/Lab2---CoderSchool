package com.codepath.android.booksearch.activity;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.adapter.BookAdapter;
import com.codepath.android.booksearch.api.BookApi;
import com.codepath.android.booksearch.model.Book;
import com.codepath.android.booksearch.model.SearchRequest;
import com.codepath.android.booksearch.model.SearchResult;
import com.codepath.android.booksearch.utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.ListenerClass;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity {
    private SearchRequest mSearchRequest;
    private BookAdapter mBookAdapter;
    private BookApi mBookApi;
    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.tbMenu)
    Toolbar tbMenu;

    @BindView(R.id.lvBooks)
    RecyclerView lvBooks;

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        setUpApi();
        setUpViews();
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mBookApi = RetrofitUtils.get().create(BookApi.class);
    }

    private void setUpViews() {
        mBookAdapter = new BookAdapter(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvBooks.setAdapter(new SlideInBottomAnimationAdapter(mBookAdapter));
        lvBooks.setLayoutManager(mLayoutManager);
        setSupportActionBar(tbMenu);
        mBookAdapter.setmListener(new BookAdapter.Listener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(BookListActivity.this, BookDetailActivity.class);
                intent.putExtra("item", book);
                startActivity(intent);
            }
        });
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks() {
        mBookApi.search(mSearchRequest.toQueryMay()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    handleResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void handleResponse(SearchResult searchResult) {
        mBookAdapter.setBooks(searchResult.getBooks());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        setUpSearchView(searchView, searchItem);
        return true;
    }

    private void setUpSearchView(final SearchView searchView, MenuItem searchItem) {
        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);
        // Set focus on start
        searchItem.expandActionView();
        searchView.requestFocus();
        // Set listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchRequest.setQuery(query);
                searchView.clearFocus();
                fetchBooks();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        fetchBooks();
        pbLoading.setVisibility(View.GONE);
        return super.onPrepareOptionsMenu(menu);
    }
}
