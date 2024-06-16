package com.lsx.finalhomework.controllers;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.adapters.UserMenuViewAdapter;
import com.lsx.finalhomework.entities.Book;
import com.lsx.finalhomework.entities.BookService;
import java.util.ArrayList;
import java.util.List;

public class User_MainActivity extends AppCompatActivity {
    User_BookManageFragment bookManageFragment;
    UserManageFragment userManageFragment;
    infoManagerfragment infoManagerfragment;


    private BottomNavigationView mbottomNavigationView;
    private UserMenuViewAdapter mUserMenuViewAdapter;
    private ViewPager2 viewPager;
    public List<Fragment> fragmentList;
    BookService bookService;

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
//        book = new Book(6,Book.Category.HISTORY,"测试","https://img-blog.csdnimg.cn/2a3afe97e6694f54956d7df82938db83.png","测试","测试","测试",100.0);
//        bookService = new BookService(this);
//        bookService.addBook(book);


        viewPager = findViewById(R.id.menu);
        mbottomNavigationView = findViewById(R.id.menu_main);

        initData();

        //创建Adapter
        mUserMenuViewAdapter = new UserMenuViewAdapter(User_MainActivity.this,fragmentList);

        viewPager.setAdapter(mUserMenuViewAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mbottomNavigationView.setSelectedItemId(R.id.book_item);
                        break;
                    case 1:
                        mbottomNavigationView.setSelectedItemId(R.id.UserM_item);
                        break;
                    case 2:
                        mbottomNavigationView.setSelectedItemId(R.id.InfoM_item);
                        break;
                    default:
                        break;
                }
            }
        });
        mbottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.book_item:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.UserM_item:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.InfoM_item:
                    viewPager.setCurrentItem(2);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void initData() {
        fragmentList = new ArrayList<>();

        bookManageFragment = new User_BookManageFragment();
        userManageFragment = new UserManageFragment();
        infoManagerfragment = new infoManagerfragment();

        fragmentList.add(bookManageFragment);
        fragmentList.add(userManageFragment);
        fragmentList.add(infoManagerfragment);
    }
}