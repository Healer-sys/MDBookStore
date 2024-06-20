package com.lsx.finalhomework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

import com.lsx.finalhomework.entities.AESUtil;
import com.lsx.finalhomework.entities.Book;
import com.lsx.finalhomework.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class MyAuth extends MyDBHelper {

    private static int userId;
    public static String Key = "htEwE6Sl90KO4RymTo5GdB6Vu7/2b85WjLtdJWQhZ3M=";
    SecretKey HKey;

    public MyAuth(Context context) {
        super(context);
    }

    public void deleteuser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("account", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public enum AuthResult {
        SUCCESS,
        INVALID_USERNAME_OR_PWD,
        USER_EXISTED,
        TOKEN_TOO_LONG,
        UNKNOWN_ERROR
    }

    public static int getUserId() {
        return userId;
    }

    private static void setUserId(int userId) {
        MyAuth.userId = userId;
    }
    /**
     * 获取用户
     * @return 用户列表
     */
    public List<User> getUserList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("account", null, null, null, null, null, null);
        List<User> result = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            User user = new User(cursor.getInt( (int) cursor.getColumnIndex("id")), cursor.getString((int) cursor.getColumnIndex("username")), cursor.getString( (int) cursor.getColumnIndex("password")));
            result.add(user);
        }
        cursor.close();
        db.close();
        return result;
    }
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return 注册结果
     */
    public AuthResult addUser(String username, String password) {
        if (username.length() > 50)
            return AuthResult.TOKEN_TOO_LONG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("account", null, "username=?", new String[]{username}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return AuthResult.USER_EXISTED;
        }
        ContentValues values = new ContentValues();
        values.put("username", username);
//        values.put("password", md5(password));
        try {
            password = AESUtil.encrypt(password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        values.put("password", password);
        int id = (int) db.insert("account", null, values);
        db.close();
        if (id > 0) {
            userId = id;
            return AuthResult.SUCCESS;
        } else
            return AuthResult.UNKNOWN_ERROR;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    public AuthResult authUser(String username, String password) {
        if (username.length() > 50)
            return AuthResult.TOKEN_TOO_LONG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("account", null, "username=?", new String[]{username}, null, null, null);
        String result = null;
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            result = cursor.getString((int) cursor.getColumnIndex("password"));
            userId = cursor.getInt((int) cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();
        try {
            if (result != null && result.equals(AESUtil.encrypt(password)))
                return AuthResult.SUCCESS;
            else {
                userId = 0;
                return AuthResult.INVALID_USERNAME_OR_PWD;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return AuthResult.UNKNOWN_ERROR;
//        if (result != null && result.equals(md5(password)))
//            return AuthResult.SUCCESS;
//        else {
//            userId = 0;
//            return AuthResult.INVALID_USERNAME_OR_PWD;
//        }
    }

    /**
     * 计算字符串的MD5哈希值。
     * MD5（Message-Digest Algorithm）是一种广泛使用的密码散列函数，可产生128位的散列值。
     * 此方法用于将字符串转换为其MD5哈希表示形式。
     *
     * @param string 输入的字符串
     * @return 输入字符串的MD5哈希字符串；如果输入为空，则返回空字符串
     */
    static String md5(String string) {
        // 检查输入字符串是否为空，如果为空则直接返回空字符串
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            // 初始化MD5消息摘要
            md5 = MessageDigest.getInstance("MD5");
            // 计算输入字符串的MD5哈希值
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            // 将哈希字节转换为十六进制字符串
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                // 如果十六进制字符串长度为1，则在其前缀加上0以确保它是两位数
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            // 返回大写的MD5哈希字符串
            return result.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            // 如果找不到MD5算法，则打印堆栈跟踪信息
            e.printStackTrace();
        }
        // 如果发生异常或其他原因，返回空字符串
        return "";
    }
    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        db.update("account", values, "id=?", new String[]{String.valueOf(user.getId())});
        db.close();
        return true;
    }
}