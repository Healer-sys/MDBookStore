package com.lsx.finalhomework.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.adapters.BookListAdapter;
import com.lsx.finalhomework.adapters.MyBookRecyclerViewAdapter;
import com.lsx.finalhomework.entities.Book;
import com.lsx.finalhomework.entities.BookService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class User_BookManageFragment extends Fragment implements MyBookRecyclerViewAdapter.OnItemClickListener, BookListAdapter.OnItemClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //列表
    RecyclerView recyclerView;

    TextView tvBookDate,tvBookIsbn,tvBookName;
    EditText etBookPrice;
    Button btnAddBook, btnBookUpdate;
    String bookIsbn = new String();
    String BookDate = new String();
    BookService bs;
    private List<Book> bookList;
    BookListAdapter adapter;
    ArrayList bookListWithHeader;
    GridLayoutManager gridLayoutManager;


    private String mParam1;
    private String mParam2;

    public User_BookManageFragment() {
        // Required empty public constructor
    }


    public static User_BookManageFragment newInstance(String param1, String param2) {
        User_BookManageFragment fragment = new User_BookManageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__book_manage, container, false);
        Context context = getContext();
        bs = new BookService(context);

        //获取控件
        recyclerView = view.findViewById(R.id.book_manage_list);
        btnAddBook = view.findViewById(R.id.add_book_button);
        btnBookUpdate = view.findViewById(R.id.update_book_button);
        tvBookDate = view.findViewById(R.id.book_datemanage_textview);
        tvBookIsbn = view.findViewById(R.id.book_isbn_textview);
        tvBookName = view.findViewById(R.id.book_name_textview);
        tvBookName.setMovementMethod(new ScrollingMovementMethod());
        etBookPrice = view.findViewById(R.id.book_price_edittext);


        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        bookListWithHeader = new ArrayList<>();
        bookListWithHeader.addAll(bs.getList());
        adapter = new BookListAdapter(bookListWithHeader);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        //recyclerView设置点击事件


        //扫描图书按钮
        btnAddBook.setOnClickListener(v -> {
            // 启动扫描
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a QR Code");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);//允许旋转
            options.setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class);//定义扫描的界面
            scanLauncher.launch(options);
        });

        //添加图书按钮
        btnBookUpdate.setOnClickListener(v -> {
            //判断有没有输入价格
            if(etBookPrice.getText().toString().trim().isEmpty()){
                Toast.makeText(context, "请输入有效的数字", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    JSONObject resultDate =  new JSONObject(BookDate);
                    resultDate = resultDate.getJSONObject("data");
                    //解析图片链接
                    String picturesurl = resultDate.getString("pictures");
                    picturesurl = picturesurl.replace("[", "")
                            .replace("]", "")
                            .replace("\\", "")
                            .replace("\"", "");;
                    Book book = new Book(bs.getBookid()+1, Book.Category.OTHER, resultDate.getString("bookName"),picturesurl , resultDate.getString("author"), resultDate.getString("isbn"), resultDate.getString("bookDesc"), Double.parseDouble(etBookPrice.getText().toString()));
                    bs.addBook(book);
                    UiUpdate();
                    Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return view;
    }
    private final ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {

            bookIsbn = result.getContents();
            new Thread(() -> networkRequest(bookIsbn)).start();
            Toast.makeText(getContext(), "扫描成功" + bookIsbn, Toast.LENGTH_SHORT).show();
        }
    });

    private void networkRequest(String isbn) {
        try {
            URL url = new URL("https://data.isbn.work/openApi/getInfoByIsbn?isbn=" + isbn + "&appKey=ae1718d4587744b0b79f940fbef69e77");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);

            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code" + responseCode);
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                BookDate = reader.readLine();
            }
            getActivity().runOnUiThread(() -> {
                if (BookDate == null || BookDate.trim().isEmpty()) {
                    tvBookName.setText("图书信息获取失败");
                }
                else {
                    try {
                        tvBookIsbn.setText(isbn);
                        JSONObject resultDate =  new JSONObject(BookDate);
                        resultDate = resultDate.getJSONObject("data");
                        tvBookName.setText(resultDate.getString("bookName"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View v) {

        //删除修改书本信息
        int position = recyclerView.getChildAdapterPosition(v);
//        Toast.makeText(getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        Object item = bookListWithHeader.get(position);
        if (item instanceof Book) {
            bundle.putInt("id", ((Book) item).getId());
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("书本信息修改");
            builder.setMessage("你选择以下操作对书本信息进行修改");
            builder.setNeutralButton("修改", (dialog, which) -> {
            //跳转
            Intent intent=new Intent(getContext(),BookFixedActivity.class);
            bundle.putSerializable("Book",((Book) item).getId());
            intent.putExtras(bundle);
            startActivity(intent);
            //关闭Activity
//            getActivity().finish();
            });
            builder.setNegativeButton("删除", (dialog, which) -> {
                bs.deleteBookByid(((Book) item).getId());
                Toast.makeText(getContext(),"书本信息删除成功",Toast.LENGTH_LONG).show();
                //刷新Fragment
                UiUpdate();
                onStart();
            });
            builder.create();
            builder.show();
        }
    }
    public void UiUpdate(){
        bookListWithHeader.clear();
        bookListWithHeader.addAll(bs.getList());
        adapter.notifyDataSetChanged();
    }
}