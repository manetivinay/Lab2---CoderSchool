package com.codepath.android.booksearch.activity;

import android.content.Intent;
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
import android.view.MenuInflater;
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
import com.codepath.android.booksearch.utils.DividerItemDecoration;
import com.codepath.android.booksearch.utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity implements BookAdapter.ListClickListener {
    private SearchRequest mSearchRequest;
    private BookAdapter mBookAdapter;
    private BookApi mBookApi;
    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.lvBooks)
    RecyclerView lvBooks;

    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpApi();
        setUpViews();
        fetchBooks();
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mBookApi = RetrofitUtils.get().create(BookApi.class);
    }

    private void setUpViews() {
        mBookAdapter = new BookAdapter();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvBooks.setAdapter(new SlideInBottomAnimationAdapter(mBookAdapter));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        lvBooks.addItemDecoration(itemDecoration);
        lvBooks.setLayoutManager(mLayoutManager);
        mBookAdapter.setListClickListener(this);
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks() {
        mProgressBar.setVisibility(View.VISIBLE);
        mBookApi.search(mSearchRequest.toQueryMay()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    mProgressBar.setVisibility(View.GONE);
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_book_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText editText = (EditText) searchView.findViewById(searchEditId);
        editText.setHint("find your favourite book");
        editText.setTextColor(Color.parseColor("#FFFFFF"));
        editText.setHintTextColor(Color.parseColor("#FFFFFF"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                mSearchRequest.setQuery(query);
                fetchBooks();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
    public void onBookItemClick(Book book) {
        Intent toDetailBookIntent = new Intent(this, BookDetailActivity.class);
        toDetailBookIntent.putExtra("book_detail", book);
        startActivity(toDetailBookIntent);
    }
}
