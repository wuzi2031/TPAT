package com.testplatform.tpat.utils;

import android.net.LocalSocket;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.channels.FileChannel;

public class FileUtil {
    private static final String TAG = "--FileUtil--";
    public static final String separator = "/";
    public static final String TLOG_POSFIX = ".csv";
    public static final String GW_POSFIX = ".csv";
    public static final String GW_DESC_PREFIX = "gtdesc_";
    public static final String GW_DESC_POSFIX = ".txt";
    public static FilenameFilter CSV_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String filename) {
            if (filename != null && filename.endsWith(GW_POSFIX)) {
                return true;
            }
            return false;
        }
    };

    public static FilenameFilter DESC_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String filename) {
            if (filename != null && filename.startsWith(GW_DESC_PREFIX) && filename.endsWith(GW_DESC_POSFIX)) {
                return true;
            }
            return false;
        }
    };

    public static FilenameFilter CSV_AND_DESC_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String filename) {
            if (filename != null && filename.endsWith(GW_POSFIX)
                    || filename != null && filename.startsWith(GW_DESC_PREFIX) && filename.endsWith(GW_DESC_POSFIX)) {
                return true;
            }
            return false;
        }
    };

    // ==================================================关于文件处理====================================================
    public static boolean isPathStringValid(String path) {
        if (null == path || path.length() == 0) {
            return false;
        }

        if (path.contains(":") || path.contains("*") || path.contains("?")
                || path.contains("\"") || path.contains("<")
                || path.contains(">") || path.contains("|")) {
            Log.w(TAG, "filename can not contains:*:?\"<>|");

            return false;
        }

        return true;
    }

    public static boolean isPath(String path) {
        if (path.contains(separator) || path.contains("\\")) {
            return true;
        }
        return false;
    }

    public static String getPath(String path) {
        int la = path.lastIndexOf(separator);
        String subPath = path.substring(0, la);
        return subPath;
    }

    /**
     * @param path      需要转换的路径或文件名
     * @param defParent 默认父路径名，当path只是文件名时，会自动在前面追加上父路径
     * @param defPosfix 默认后缀名，当path不带后缀名时，则自动将其加上
     * @return
     */
    public static String convertValidFilePath(String path, String defPosfix) {
        String filePath = path;
        if (path.contains(separator) || path.contains("\\")) {
            int la = filePath.lastIndexOf(".");
            if (la < 0) {
                filePath = path + defPosfix;
            } else {
                String temp = filePath.substring(la);
                if (temp.contains(separator) || temp.contains("\\")) {
                    // "."是目录名的一部分而不是后缀名的情况
                    filePath = path + defPosfix;
                }
                // else fileName = fileName
            }
        } else {
            if (!path.contains(".")) // 没有有后缀
            {
                filePath = filePath + defPosfix;
            }
        }

        return filePath;
    }

    public static boolean isFileExists(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isFileValid(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {

                return false;
            }
            f.delete();
        }
        return true;
    }

    public static boolean isFileValid(File parent, String name) {
        File f = new File(parent, name);
        return isFileValid(f);
    }

    /**
     * 删除存在的文件
     *
     * @param filePath
     */
    public static void delExistFile(String filePath) {
        File f = new File(filePath);
        if (f.exists())
            f.delete();
    }

    /**
     * 创建文件夹
     *
     * @param ist
     * @param path
     */
    public static void createDir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean f=directory.mkdirs();
            System.out.print(f);
        }

    }

    public static void createDir(File file) {
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {

            fileParent.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭bufferReader
     *
     * @param br
     */
    public static void closeReader(Reader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭Writer
     *
     * @param br
     */
    public static void closeWriter(Writer wr) {
        if (wr != null) {
            try {
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * flush Writer
     *
     * @param br
     */
    public static void flushWriter(Writer wr) {
        if (wr != null) {
            try {
                wr.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输入流的关闭
     *
     * @param in
     */
    public static void closeInputStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输出流的关闭
     *
     * @param out
     */
    public static void closeOutputStream(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件管道的关闭
     *
     * @param in
     */
    public static void closeFileChannel(FileChannel chl) {
        if (chl != null) {
            try {
                chl.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * RandomAccessFile的关闭
     *
     * @param f RandomAccessFile对象
     */
    public static void closeRandomAccessFile(RandomAccessFile f) {
        if (f != null) {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Socket的关闭
     *
     * @param s Socket对象
     */
    public static void colseSocket(Socket s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * LocalSocket的关闭
     *
     * @param s Socket对象
     */
    public static void colseLocalSocket(LocalSocket s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            System.out.print("文件不存在！" + "n");
        }
    }

    /**
     * 拷贝文件
     *
     * @param s 源文件
     * @param t 目标文件
     */
    public static void copyFile(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            if (!t.exists()) {
                t.createNewFile();
            }

            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            // 连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(fo);
            closeInputStream(fi);
            closeFileChannel(in);
            closeFileChannel(out);
        }
    }

    public static void copyInputToFile(InputStream in, String path) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            byte[] buffer = new byte[10 * 1024];
            bis = new BufferedInputStream(in);
            fos = new FileOutputStream(path);
            int a = bis.read(buffer, 0, buffer.length);
            while (a != -1) {
                fos.write(buffer, 0, a);
                fos.flush();
                a = bis.read(buffer, 0, buffer.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(fos);
            closeInputStream(bis);
            closeInputStream(in);
        }
    }
}
