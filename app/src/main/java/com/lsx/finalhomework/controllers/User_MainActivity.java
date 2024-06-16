package com.lsx.finalhomework.controllers;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.entities.Book;
import com.lsx.finalhomework.entities.BookService;
import java.util.ArrayList;
import java.util.List;

public class User_MainActivity extends AppCompatActivity {

    Fragment infoManagerfragment;
    Fragment userManagersfragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    public List<Fragment> fragmentList = new ArrayList();
    BookService bookService;

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
//        book = new Book(6,Book.Category.HISTORY,"测试","https://img-blog.csdnimg.cn/2a3afe97e6694f54956d7df82938db83.png","测试","测试","测试",100.0);
//        bookService = new BookService(this);
//        bookService.addBook(book);

        Button scanButton = findViewById(R.id.zxing_barcode_scanner);
        scanButton.setOnClickListener(view -> {
            // 启动扫描
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a QR Code");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class);
            scanLauncher.launch(options);
        });
    }

    private final ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            // 扫描结果
            Toast.makeText(User_MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(User_MainActivity.this, "Scan failed", Toast.LENGTH_LONG).show();
        }
    });

}