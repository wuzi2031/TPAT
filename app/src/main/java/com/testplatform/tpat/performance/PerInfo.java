package com.testplatform.tpat.performance;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;


import com.testplatform.tpat.bean.TrafficInfo;
import com.testplatform.tpat.common.Constants;
import com.testplatform.tpat.utils.ShellUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PerInfo {
    public static String TAG = "per";
    public static long TRFIC_RX = 0;//当前上行总流量
    public static long TRFIC_TX = 0;//当前下行总流量
    private Context mContext;

    public PerInfo(Context context) {
        mContext = context;
    }

    /**
     * 版本信息
     *
     * @return
     */
    public String[] getVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//system version
        return version;
    }

    /**
     * 内存信息
     */
    public void getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i(TAG, "---" + str2);
            }
        } catch (IOException e) {
        }
    }

    /**
     * 获取系统剩余内存大小
     *
     * @return
     */
    public long getAvailMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        Log.e(TAG, "我的机器一共有:" + memoryInfo.totalMem + "内存");
        Log.e(TAG, "其中可用的有:" + memoryInfo.availMem + "内存");
        Log.e(TAG, "其中达到:" + memoryInfo.threshold + "就会有可能触发LMK，系统开始杀进程了");
        Log.e(TAG, "所以现在的状态是:" + memoryInfo.lowMemory);
        return memoryInfo.availMem;
    }

    /**
     * ROM大小
     *
     * @return
     */
    public long[] getRomMemroy() {
        long[] romInfo = new long[2];
        //Total rom memory
        romInfo[0] = getTotalInternalMemorySize();

        //Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
        getVersion();
        return romInfo;
    }

    /**
     * cpu信息
     *
     * @return
     */
    public String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    /**
     * 获取cpu使用率
     *
     * @return
     */
    public Double getCpuUsage() {
        String use = null;
        Double usage = null;
        ShellUtil.CommandResult commandResult = ShellUtil.execCommand("top -n 1", false);
        String[] lines = commandResult.successMsg.split(ShellUtil.COMMAND_LINE_SPLIT);
        for (String line : lines) {
            if (line.contains("%")) {
                if (line.contains("User")) {
                    String[] l = line.split(",");
                    use = l[0].split("\\s+")[1];
                    break;
                } else if (line.contains("user")) {
                    String[] l = line.split("\\s+");
                    use = l[1].split("user")[0];
                    break;
                }
            }

        }
        if (use != null) {
            usage = Double.parseDouble(use.split("%")[0]);
        }
        return usage / 100;
    }

//    /**
//     * 获取cpu使用率
//     *
//     * @return
//     */
//    public String getCpuUsage1() {
//        double usage = 0.0, o_idle = 0.0, o_cpu = 0.0;
//        boolean initCpu = true;
//        if (initCpu) {
//            initCpu = false;
//            RandomAccessFile reader = null;
//            try {
//                reader = new RandomAccessFile("/proc/stat", "r");
//                String load = reader.readLine();
//                String[] toks = load.split(" ");
//                o_idle = Double.parseDouble(toks[5]);
//                o_cpu = Double.parseDouble(toks[2])
//                        + Double.parseDouble(toks[3])
//                        + Double.parseDouble(toks[4])
//                        + Double.parseDouble(toks[6])
//                        + Double.parseDouble(toks[7])
//                        + Double.parseDouble(toks[8])
//                        + Double.parseDouble(toks[9]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            FileUtil.closeRandomAccessFile(reader);
//        } else {
//            RandomAccessFile reader = null;
//            try {
//                reader = new RandomAccessFile("/proc/stat", "r");
//                String load;
//                load = reader.readLine();
//                String[] toks = load.split(" ");
//                double c_idle = Double.parseDouble(toks[5]);
//                double c_cpu = Double.parseDouble(toks[2])
//                        + Double.parseDouble(toks[3])
//                        + Double.parseDouble(toks[4])
//                        + Double.parseDouble(toks[6])
//                        + Double.parseDouble(toks[7])
//                        + Double.parseDouble(toks[8])
//                        + Double.parseDouble(toks[9]);
//                if (0 != ((c_cpu + c_idle) - (o_cpu + o_idle))) {
//                    usage = DoubleUtil.div((100.00 * ((c_cpu - o_cpu))),
//                            ((c_cpu + c_idle) - (o_cpu + o_idle)), 2);
//                }
//                o_cpu = c_cpu;
//                o_idle = c_idle;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            FileUtil.closeRandomAccessFile(reader);
//        }
//        return String.valueOf(usage) + "%";
//    }

    /**
     * 获取磁盘大小
     *
     * @return
     */
    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取sdcard大小
     *
     * @return
     */
    public long[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }

    /**
     * 获取Mac地址
     *
     * @return
     */
    public String getMac() {
        String mac = null;
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            mac = wifiInfo.getMacAddress();
        }
        return mac;
    }

    /**
     * 获取应用的uid
     *
     * @param packageName
     * @return
     */
    public String getUid(String packageName) {
        String uid = null;
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            uid = ai.uid + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uid;
    }

    /**
     * 返回所有的有互联网访问权限的应用程序的流量信息。
     *
     * @return
     */
    public TrafficInfo getTrafficInfo() {
        //获取到配置权限信息的应用程序
        PackageManager pms = mContext.getPackageManager();
        ;
        List<PackageInfo> packinfos = pms
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
        //存放具有Internet权限信息的应用
//        List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();
        TrafficInfo trafficInfo = new TrafficInfo();
        for (PackageInfo packinfo : packinfos) {
            //获取该应用的所有权限信息
            String[] permissions = packinfo.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String permission : permissions) {
                    //筛选出具有Internet权限的应用程序
                    if ("android.permission.INTERNET".equals(permission)) {
                        String packName = packinfo.packageName;
                        if (Constants.DESIGN_PACAGE.contentEquals(packName)) {


                            trafficInfo.setPackname(packName);
                            //获取到应用的uid（user id）
                            int uid = packinfo.applicationInfo.uid;
                            //TrafficStats对象通过应用的uid来获取应用的下载、上传流量信息
                            //发送的 上传的流量byte
                            trafficInfo.setRxFlow(TrafficStats.getUidRxBytes(uid));
                            //下载的流量 byte
                            trafficInfo.setTxFlow(TrafficStats.getUidTxBytes(uid));
                            break;
                        }

                    }
                }
            }
        }
        return trafficInfo;
    }

    /**
     * 上行流量
     *
     * @param uid
     * @return
     */
    public Long getRxTraffic(String uid) {
        String str1 = "/proc/uid_stat/" + uid + "/tcp_rcv";
        String result = "";
        Long rx = new Long(0);
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            result = localBufferedReader.readLine();
            if (!result.contains("No such file or directory")) {
                Long rx_curry = Long.parseLong(result);
                if (TRFIC_RX != 0) {
                    rx = rx_curry - TRFIC_RX;
                }
                TRFIC_RX = rx_curry;

            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return rx;
    }

    /**
     * 下行流量
     *
     * @param uid
     * @return
     */
    public Long getTxTraffic(String uid) {
        String str1 = "/proc/uid_stat/" + uid + "/tcp_snd";
        String result = "";
        Long tx = new Long(0);
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            result = localBufferedReader.readLine();
            if (!result.contains("No such file or directory")) {
                Long tx_curry = Long.parseLong(result);
                if (TRFIC_TX != 0) {
                    tx = tx_curry - TRFIC_TX;
                }
                TRFIC_TX = tx_curry;
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return tx;
    }
}
