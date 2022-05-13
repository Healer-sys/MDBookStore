package com.lsx.finalhomework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lsx.finalhomework.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BookFragment extends Fragment implements MyBookRecyclerViewAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    ViewGroup container;

    BookService bs;
    List<Book> bookList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bs = new BookService(getContext());
        bookList = bs.getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        View view = inflater.inflate(R.layout.book_fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyBookRecyclerViewAdapter adapter = new MyBookRecyclerViewAdapter(bookList);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    public void onItemClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Bundle bundle = new Bundle();
        bundle.putInt("id", bookList.get(position).getId());
        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.action_navigation_booklist_to_bookDetailFragment, bundle);
    }
}