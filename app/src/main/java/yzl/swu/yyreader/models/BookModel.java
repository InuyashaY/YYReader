package yzl.swu.yyreader.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

/**
 * 书籍的模型
 * */
public class BookModel extends LitePalSupport implements Parcelable {
    //LitePal自动生成id
    private Long id = 0l;
    private String bookId;
    private String bookTitle;
    private String coverResource;
    private String record;
    private String filePath;
    private Boolean isLocal;

    public BookModel(String bookTitle,String coverResource,String record,String filePath,boolean isLocal){
        this.bookTitle = bookTitle;
        this.coverResource = coverResource;
        this.record = record;
        this.filePath = filePath;
        this.isLocal = isLocal;
    }

    public BookModel(){}

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected BookModel(Parcel in) {
        id = in.readLong();
        bookId = in.readString();
        bookTitle = in.readString();
        coverResource = in.readString();
        record = in.readString();
        filePath = in.readString();
        isLocal = in.readBoolean();
    }

    public static final Creator<BookModel> CREATOR = new Creator<BookModel>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public BookModel createFromParcel(Parcel in) {
            BookModel model = new BookModel();
            model.id = in.readLong();
            model.bookId = in.readString();
            model.bookTitle = in.readString();
            model.coverResource = in.readString();
            model.record = in.readString();
            model.filePath = in.readString();
            model.isLocal = in.readBoolean();
            return model;
        }

        @Override
        public BookModel[] newArray(int size) {
            return new BookModel[size];
        }
    };


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getCoverResource() {
        return coverResource;
    }

    public void setCoverResource(String coverResource) {
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

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(bookId);
        dest.writeString(bookTitle);
        dest.writeString(coverResource);
        dest.writeString(record);
        dest.writeString(filePath);
        dest.writeBoolean(isLocal);
    }
}
