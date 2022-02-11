package yzl.swu.yyreader.common;

import java.io.File;

import yzl.swu.yyreader.utils.FileUtils;

public class Constants {

    //阅读界面 默认字体大小
    public static int DEFAULT_TEXT_SIZE = 18;

    //时间格式
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_DATE = "YYYY-MM-DD";
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";

    //回调代码
    public static final int MAINACTIVITY_REQUEST_CODE = 1;
    public static final int FILESELECTOR_RESULT_CODE = 2;

    public static final String READBOOK_KEY = "readBookKey";

    public static final String FIELSELECTOR_RESULT_KEY = "fileSelectorResultKey";

    //SharedPreference
    public static final String IS_FIRST = "isFirst";
    public static final String FIRST_KEY = "firstKey";

    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    public static String BOOK_CACHE_PATH = FileUtils.getCachePath()+ File.separator
            + "book_cache"+ File.separator;

    //URL_BASE
//    public static final String API_BASE_URL = "http://api.zhuishushenqi.com";
    public static final String API_BASE_URL = "http://192.168.0.107:8080";

    public static final String IMG_BASE_URL = "http://statics.zhuishushenqi.com";
}
