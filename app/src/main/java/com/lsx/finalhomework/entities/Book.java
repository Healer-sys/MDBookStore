package com.lsx.finalhomework.entities;

public class Book {

    public enum Category {
        COMPUTER,
        NOVEL,
        SCIENCE,
        HISTORY,
        OTHER;

        public String getName() {
            switch (this) {
                case COMPUTER:
                    return "计算机";
                case NOVEL:
                    return "小说";
                case SCIENCE:
                    return "科学";
                case HISTORY:
                    return "历史";
                case OTHER:
                    return "其他";
                default:
                    return "";
            }
        }
    }

        /**
         * 图书唯一标识符。
         */
        int id;

        /**
         * 图书分类。
         */
        Category category;

        /**
         * 图书名称。
         */
        String name;

        /**
         * 图书封面图片的URL。
         */
        String imgUrl;

        /**
         * 图书的作者。
         */
        String author;

        /**
         * 图书的国际标准书号（ISBN）。
         */
        String ISBN;

        /**
         * 图书的简介。
         */
        String description;

        /**
         * 图书的售价。
         */
        double price;

    public Book() { }

    public Book(int id, Category category, String name, String imgUrl, String author, String ISBN, String description, double price) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.imgUrl = imgUrl;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
