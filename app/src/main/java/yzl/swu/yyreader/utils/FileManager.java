package yzl.swu.yyreader.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class FileManager {
    private static String LOG_Info = "FileManager";
    //单例
    private static FileManager instance = new FileManager();

    public static FileManager getInstance() {
        return instance;
    }

    public void test(Context context){
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

    //根目录下的文件
    public List<File> listRootFiles(){
        return listFilesByFilePath(getRootPath());
    }

    //某目录下的文件夹和txt文件
    public List<File> listFilesByFilePath(String filePath){
        File root = new File(filePath);
        File[] dirs = root.listFiles((pathname -> {
            if (pathname.isDirectory()) {
                return true;
            }
            //获取txt文件
            else if(pathname.getName().endsWith(".txt")){
                return true;
            }
            else{
                return false;
            }
        }));

        return new ArrayList<>(Arrays.asList(dirs));
    }

    //获取根目录path
    public String getRootPath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    //获取内存中的所有txt文件
    public List<File> listTxtFiles(){
        List result = listTxtFiles(getRootPath(),0);
        return result;
    }


    private List<File> listTxtFiles(String filePath, int layer){
        File root = new File(filePath);
        List<File> txtFiles = new ArrayList<>();

        //如果层级为 3，则直接返回
        if (layer == 3){
            return txtFiles;
        }

        File[] dirs = root.listFiles(
                pathname -> {
                    if (pathname.isDirectory() && !pathname.getName().startsWith(".")) {
                        return true;
                    }
                    //获取txt文件
                    else if(pathname.getName().endsWith(".txt")){
                        txtFiles.add(pathname);
                        return false;
                    }
                    else{
                        return false;
                    }
                }
        );

        //遍历文件夹
        if(dirs == null || dirs.length == 0) return txtFiles;
        for (File dir : dirs){
            //递归遍历txt文件
            txtFiles.addAll(listTxtFiles(dir.getPath(),layer + 1));
        }
//        for (File f : files){
//            if (!f.isDirectory()){
//                if (f.getName().endsWith(".txt")){
//                    //获取并计算文件大小
//                    long size = f.length();
//                    String t_size = "";
//                    if (size <= 1024){
//                        t_size = size + "B";
//                    }else if (size > 1024 && size <= 1024 * 1024){
//                        size /= 1024;
//                        t_size = size + "KB";
//                    }else {
//                        size = size / (1024 * 1024);
//                        t_size = size + "MB";
//                    }
//                    Log.i(LOG_Info,""+t_size+" "+f.getName()+" "+f.getAbsolutePath());
////                    file_size.add(t_size);//文件大小
////                    file_name.add(f.getName());//文件名称
////                    file_txt_path.add(f.getAbsolutePath());//文件路径
//                }
//            }else if (f.isDirectory()){
//                //如果是目录，迭代进入该目录
//                //listFileTxt(f);
//            }
//        }
        return txtFiles;
    }

    public String getFileSize(File f){
        if (f.getName().endsWith(".txt")) {
            //获取并计算文件大小
            long size = f.length();
            String t_size = "";
            if (size <= 1024) {
                t_size = size + "B";
            } else if (size > 1024 && size <= 1024 * 1024) {
                size /= 1024;
                t_size = size + "KB";
            } else {
                size = size / (1024 * 1024);
                t_size = size + "MB";
            }
            return t_size;
        }
        return null;
    }

    public int getSubFileCount(File file){
        return file.listFiles().length;
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


    public File getFileByFilePath(String filePath){

//        String filePath = Environment.getExternalStorageDirectory().getPath()+"/"+fileName;
        File file = new File(filePath);
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
//        String contenet = br.readLine();
//        while((contenet=br.readLine()) != null){
//            Log.i(LOG_Info,"--------"+contenet);
//        }

        return file;
    }


    private final String SEPARATOR = File.separator;//路径分隔符

    /**
     * 复制res/raw中的文件到指定目录
     * @param context 上下文
     * @param id 资源ID
     * @param fileName 文件名
     * @param storagePath 目标文件夹的路径
     */
    public void copyFilesFromRaw(Context context, int id, String fileName,String storagePath){
        InputStream inputStream=context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
            file.mkdirs();
        }
        readInputStream(storagePath + SEPARATOR + fileName, inputStream);
    }

    /**
     * 读取输入流中的数据写入输出流
     *
     * @param storagePath 目标文件路径
     * @param inputStream 输入流
     */
    public void readInputStream(String storagePath, InputStream inputStream) {
        File file = new File(storagePath);
        try {
            if (!file.exists()) {
                // 1.建立通道对象
                FileOutputStream fos = new FileOutputStream(file);
                // 2.定义存储空间
                byte[] buffer = new byte[inputStream.available()];
                // 3.开始读文件
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                    // 将Buffer中的数据写到outputStream对象中
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// 刷新缓冲区
                // 4.关闭流
                fos.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
