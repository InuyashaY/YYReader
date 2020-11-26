package yzl.swu.yyreader.views;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import yzl.swu.yyreader.models.BookModel;
import yzl.swu.yyreader.models.TxtChapterModel;
import yzl.swu.yyreader.utils.FileManager;

public class LocalPageLoader extends PageLoader {
    public LocalPageLoader(YPageView pageView, BookModel bookModel) {
        super(pageView, bookModel);
    }

    @Override
    public BufferedReader getChapterReader(TxtChapterModel chapterModel) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileManager.getInstance().getFileByFilePath("斗罗大陆.txt")),"utf-8"));
        Log.v("yzll",br.toString());
        return br;
    }
}
