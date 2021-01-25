package yzl.swu.yyreader.common;

import java.io.File;
import java.util.List;

public class FileStack {
    Node node = null;
    int count = 0;

    public FileSnapShot pop(){
        Node fileNode = node;
        if (fileNode == null) return null;
        FileSnapShot fileSnapshot = fileNode.fileSnapShot;
        node = fileNode.next;
        --count;
        return fileSnapshot;
    }

    public void push(FileSnapShot fileSnapShot){
        if (fileSnapShot == null) return;
        Node newNode = new Node();
        newNode.fileSnapShot = fileSnapShot;
        newNode.next = node;
        node = newNode;
        count++;
    }


    class Node{
        FileSnapShot fileSnapShot;
        Node next;
    }

    //文件快照
    class FileSnapShot{
        String filePath;
        List<File> fileList;
    }
}
