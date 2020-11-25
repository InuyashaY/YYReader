package yzl.swu.yyreader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import yzl.swu.yyreader.R;
import yzl.swu.yyreader.utils.FileManager;
//import yzl.swu.yyreader.views.YReadView;

public class BookReaderActivity extends AppCompatActivity {
    //YReadView yReadView;

    public static void show(Context context){
        Intent intent = new Intent(context,BookReaderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        //yReadView = findViewById(R.id.mReadView);
        FileManager.getInstance().listTxtFiles();
        try {
            File bookFile = FileManager.getInstance().getFileByFilePath("斗罗大陆.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(bookFile),"utf-8"));
            String contenet = br.readLine();
            String ttt = "";
            int lines = 100;
            while(lines>0){
                if ((contenet=br.readLine()) != null) ttt+=contenet;
                lines--;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}