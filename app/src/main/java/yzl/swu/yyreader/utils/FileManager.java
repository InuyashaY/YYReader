package yzl.swu.yyreader.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

public class FileManager {
    private static String LOG_Info = "FileManager";
    //单例
    private static FileManager instance = new FileManager();

    public static FileManager getInstance() {
        return instance;
    }

    public void test(Context context){
        Stack stack = new Stack();
        String a = Environment.getDataDirectory().toString();
        String b = context.getFilesDir().getAbsolutePath();
        String c = context.getCacheDir().getAbsolutePath();
        String d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        String e = Environment.getExternalStorageDirectory().getPath();
        String f = context.getExternalFilesDir("Documents").getPath();
        Log.i(LOG_Info + "a: ", "Environment.getDataDirectory().toString():-----" + a);
        Log.i(LOG_Info + "b: ", "getFilesDir().getAbsolutePath():----- " + b);
        Log.i(LOG_Info + "c: ", "getCacheDir().getAbsolutePath():----- " + c);
        Log.i(LOG_Info + "d: ", "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath():----- " + d);
        Log.i(LOG_Info + "e: ", "Environment.getExternalStorageDirectory().getPath():----- " + e);
        Log.i(LOG_Info + "f: ", "getExternalFilesDir(\"Documents\").getPath():----- " + f);

    }


    public void listTxtFiles(){
        File temp = Environment.getExternalStorageDirectory();
        File[] files = temp.listFiles();
        for (File f : files){
            if (!f.isDirectory()){
                if (f.getName().endsWith(".txt")){
                    //获取并计算文件大小
                    long size = f.length();
                    String t_size = "";
                    if (size <= 1024){
                        t_size = size + "B";
                    }else if (size > 1024 && size <= 1024 * 1024){
                        size /= 1024;
                        t_size = size + "KB";
                    }else {
                        size = size / (1024 * 1024);
                        t_size = size + "MB";
                    }
                    Log.i(LOG_Info,""+t_size+" "+f.getName()+" "+f.getAbsolutePath());
//                    file_size.add(t_size);//文件大小
//                    file_name.add(f.getName());//文件名称
//                    file_txt_path.add(f.getAbsolutePath());//文件路径
                }
            }else if (f.isDirectory()){
                //如果是目录，迭代进入该目录
                //listFileTxt(f);
            }
        }
    }

    public String TestFilePathApkPrivate(Context context, String FileDirName) {
        //不需要挂载测试，因为 app 都可以装 为什么 会没有数据
        String filedirpath = context.getExternalFilesDir(FileDirName).getPath();  //文件夹
        File fileDir = new File(filedirpath);                   //创建文件夹
        if (fileDir.exists()) {    //判断文件是否存在  很重要  别每次都会去覆盖数据
            fileDir.setWritable(true);
            Log.i(LOG_Info, "文件夹已经存在    TestFilePathInternalData（）");
        } else {
            try {
                fileDir.mkdir();
                Log.i(LOG_Info, "文件夹创建成功    TestFilePathExternalData（）");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(LOG_Info, "文件夹创建错误   TestFilePathExternalData()" + e.getMessage());
            }
        }
        return filedirpath;
    }

    public File getFileByFilePath(String fileName){

        String filePath = Environment.getExternalStorageDirectory().getPath()+"/"+fileName;
        File file = new File(filePath);
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
//        String contenet = br.readLine();
//        while((contenet=br.readLine()) != null){
//            Log.i(LOG_Info,"--------"+contenet);
//        }

        return file;
    }
}
