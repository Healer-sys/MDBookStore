package com.lsx.finalhomework.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lsx.finalhomework.R;
import com.lsx.finalhomework.entities.Book;
import com.lsx.finalhomework.entities.BookService;

public class BookFixedActivity extends AppCompatActivity {

    EditText[] et_selected= new EditText[5];
    int[] et_selected_id = {R.id.et_BookName, R.id.et_BookAuthor, R.id.et_BookDescription, R.id.et_BookIsbn, R.id.et_BookPrice};

    RadioButton[] rb_selected = new RadioButton[5];
    int[] category = {R.id.radioButton1, R.id.radioButton2, R.id.radioButton3, R.id.radioButton4, R.id.radioButton5};
    Button btn;
    BookService bs;
    Book book;
    String temp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fixed);

        btn= findViewById(R.id.button);

        for(int i=0;i<5;i++){
            et_selected[i]= findViewById(et_selected_id[i]);
            rb_selected[i]= findViewById(category[i]);
        }

        bs= new BookService(this);

        Intent intent=getIntent();
        book = bs.getBook(intent.getIntExtra("Book",0));

        //设置显示
        et_selected[0].setText(book.getName());
        et_selected[1].setText(book.getAuthor());
        et_selected[2].setText(book.getDescription());
        et_selected[3].setText(book.getISBN());
        et_selected[4].setText(book.getPrice()+"");

        rb_selected[book.getCategory().ordinal()].setChecked(true);

        //点击按钮
        btn.setOnClickListener( v -> {
            temp = getText(book);
            if( temp != "")
            {
                Toast.makeText(this, temp + "没有填完", Toast.LENGTH_SHORT).show();
            }
            else {
                // 处理书本信息，
                getText(book);
                // 更新书本信息
                bs.updateBook(book.getId(), book);
                // 创建Intent跳转到User_MainActivity。
                Intent UsarMain = new Intent(BookFixedActivity.this, User_MainActivity.class);
                // FLAG_ACTIVITY_CLEAR_TOP表示如果目标Activity已经在栈中，则清除它上面的所有Activity；FLAG_ACTIVITY_SINGLE_TOP表示如果目标Activity已经在栈顶，则不会重新创建，而是回调onNewIntent方法
                UsarMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // 跳转到User_MainActivity
                startActivity(UsarMain);
            }
        });
    }

    private String getText(Book book) {
        String temp = "";

        for(int i=0;i<5;i++){
            if(!et_selected[i].getText().toString().isEmpty()){
                switch (i){
                    case 0:
                        book.setName(et_selected[i].getText().toString());
                        break;
                    case 1:
                        book.setAuthor(et_selected[i].getText().toString());
                        break;
                    case 2:
                        book.setDescription(et_selected[i].getText().toString());
                        break;
                    case 3:
                        book.setISBN(et_selected[i].getText().toString());
                        break;
                    case 4:
                        book.setPrice(Double.parseDouble(et_selected[i].getText().toString()));
                        break;
                }
            }
            else {
                temp = temp + "第" + (i+1) + "个，";
            }
            if(rb_selected[i].isChecked()){
                switch (i){
                    case 0:
                        book.setCategory(Book.Category.COMPUTER);
                        break;
                    case 1:
                        book.setCategory(Book.Category.NOVEL);
                        break;
                    case 2:
                        book.setCategory(Book.Category.SCIENCE);
                        break;
                    case 3:
                        book.setCategory(Book.Category.HISTORY);
                        break;
                   case 4:
                       book.setCategory(Book.Category.OTHER);
                       break;
               }
           }
        }
        return temp;
    }
}