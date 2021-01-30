package yzl.swu.yyreader.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IdRes;

import org.litepal.crud.LitePalSupport;

/**
 * 书籍的模型
 * */
public class BookModel extends LitePalSupport implements Parcelable {
    private String id;
    private String bookTitle;
    private int coverResource;
    private String record;
    private String filePath;

    public BookModel(String bookTitle,int coverResource,String record,String filePath){
        this.bookTitle = bookTitle;
        this.coverResource = coverResource;
        this.record = record;
        this.filePath = filePath;
    }

    private BookModel(){}

    protected BookModel(Parcel in) {
        bookTitle = in.readString();
        coverResource = in.readInt();
        record = in.readString();
        filePath = in.readString();
    }

    public static final Creator<BookModel> CREATOR = new Creator<BookModel>() {
        @Override
        public BookModel createFromParcel(Parcel in) {
            BookModel model = new BookModel();
            model.bookTitle = in.readString();
            model.coverResource = in.readInt();
            model.record = in.readString();
            model.filePath = in.readString();
            return model;
        }

        @Override
        public BookModel[] newArray(int size) {
            return new BookModel[size];
        }
    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getCoverResource() {
        return coverResource;
    }

    public void setCoverResource(int coverResource) {
        this.coverResource = coverResource;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookTitle);
        dest.writeInt(coverResource);
        dest.writeString(record);
        dest.writeString(filePath);
    }
}
