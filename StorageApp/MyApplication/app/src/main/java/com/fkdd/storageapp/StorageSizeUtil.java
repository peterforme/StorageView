package com.fkdd.storageapp;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class StorageSizeUtil {

    /**
     * 获取外存的总大小
     * @param isRemovable  false:内置存储;true:扩展SD卡存储
     * @return 总大小
     */
    public static long getExternalStorageSizeTotleSize(Context context, boolean isRemovable){
        if(isRemovable){
            return getRemovedSDcardTotleSize(context);
        } else {
            return getSDcardTotleSize();
        }
    }
    /**
     * 获取外存的可用大小
     * @param isRemovable  false:内置存储;true:扩展SD卡存储
     * @return 可用大小
     */
    public static long getExternalStorageSizeAvailableSize(Context context, boolean isRemovable){
        if(isRemovable){
            return getRemovedSDcardAvailableSize(context);
        } else {
            return getSDcardAvailableSize();
        }
    }

    /**
     * 获取内置SD卡的总大小
     * @return
     */
    private static long getSDcardTotleSize(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                int blockSize = sf.getBlockSize();
                int blockCount = sf.getBlockCount();
                return blockSize * blockCount;
            } else {
                long blockSize = sf.getBlockSizeLong();
                long blockCount = sf.getBlockCountLong();
                return blockSize * blockCount;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取内置SD卡剩余空间的大小
     * @return
     */
    private static long getSDcardAvailableSize(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                int blockSize = sf.getBlockSize();
                int availableBlocks = sf.getAvailableBlocks();
                return blockSize * availableBlocks;
            } else {
                long blockSize = sf.getBlockSizeLong();
                long availableBlocks = sf.getAvailableBlocksLong();
                return blockSize * availableBlocks;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取外置SD卡的总大小
     * @return SD卡存在，则返回大小，不存在返回0
     */
    private static long getRemovedSDcardTotleSize(Context context){
        String removedSDcardPath =  getRemovableStoragePath(context);
        if(TextUtils.isEmpty(removedSDcardPath)){
            return 0;
        }
        StatFs sf = new StatFs(removedSDcardPath);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            int blockSize = sf.getBlockSize();
            int blockCount = sf.getBlockCount();
            return blockSize * blockCount;
        } else {
            long blockSize = sf.getBlockSizeLong();
            long blockCount = sf.getBlockCountLong();
            return blockSize * blockCount;
        }
    }

    /**
     * 获取外置SD卡剩余空间大小
     * @return SD卡存在，则返回大小，不存在返回0
     */
    private static long getRemovedSDcardAvailableSize(Context context){
        String removedSDcardPath = getRemovableStoragePath(context);
        if(TextUtils.isEmpty(removedSDcardPath)){
            return 0;
        }
        StatFs sf = new StatFs(removedSDcardPath);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            int blockSize = sf.getBlockSize();
            int availableBlocks = sf.getAvailableBlocks();
            return blockSize * availableBlocks;
        } else {
            long blockSize = sf.getBlockSizeLong();
            long availableBlocks = sf.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        }
    }

    /**
     * 获取外置SD卡的路径
     */
    private static String getRemovableStoragePath(Context context) {
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    File file = new File(path);
                    if(!file.exists() || file.length() == 0){
                        return null;
                    }
                    return path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取系统总内存
     *
     * @param context 可传入应用程序上下文。
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据路径获取内存状态
     * @param path
     * @return
     */
    public static String getMemoryInfo(File path,Context context) {
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();   // 获得一个扇区的大小

        long totalBlocks = stat.getBlockCount();    // 获得扇区的总数

        long availableBlocks = stat.getAvailableBlocks();   // 获得可用的扇区数量

        // 总空间
        String totalMemory =  Formatter.formatFileSize(context, totalBlocks * blockSize);
        // 可用空间
        String availableMemory = Formatter.formatFileSize(context, availableBlocks * blockSize);

        return "总空间: " + totalMemory + "\n可用空间: " + availableMemory;
    }

}
