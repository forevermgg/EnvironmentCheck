package com.mgg.environmentcheck.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dalvik.system.DexFile;
import timber.log.Timber;

@SuppressLint({"LongLogTag", "TimberTagLength"})
public class ClassUtils {

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(
                "multidex.version", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * 通过指定包名，扫描包下面包含的所有的ClassName
     *
     * @param context     U know
     * @param packageName 包名
     * @return 所有class的集合
     */
    public static Set<String> getFileNameByPackageName(Context context, final String packageName)
            throws PackageManager.NameNotFoundException, IOException, InterruptedException {
        final Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);

        for (final String path : paths) {
            DexFile dexfile = null;

            try {
                if (path.endsWith(".zip")) {
                    // NOT use new DexFile(path), because it will throw "permission error in
                    // /data/dalvik-cache"
                    dexfile = DexFile.loadDex(path, path + ".tmp", 0);
                } else {
                    dexfile = new DexFile(path);
                }
                Enumeration<String> dexEntries = dexfile.entries();
                while (dexEntries.hasMoreElements()) {
                    String className = dexEntries.nextElement();
                    if (className.startsWith(packageName)) {
                        classNames.add(className);
                    }
                }
            } catch (Throwable ignore) {
                Timber.tag("getFileNameByPackageName").e(ignore, "Scan map file in dex files made error.");
            } finally {
                if (null != dexfile) {
                    try {
                        dexfile.close();
                    } catch (Throwable ignore) {
                    }
                }
            }
        }
        Timber.tag("getFileNameByPackageName")
                .d("Filter " + classNames.size() + " classes by packageName <" + packageName + ">");
        return classNames;
    }

    /**
     * get all the dex path
     *
     * @param context the application context
     * @return all the dex path
     * @throws PackageManager.NameNotFoundException
     * @throws IOException
     */
    public static List<String> getSourcePaths(Context context)
            throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo =
                context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        File sourceApk = new File(applicationInfo.sourceDir);

        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir); // add the default apk path

        // the prefix of extracted file, ie: test.classes
        String extractedFilePrefix = sourceApk.getName() + ".classes";

        //        如果VM已经支持了MultiDex，就不要去Secondary Folder加载 Classesx.zip了，那里已经么有了
        //        通过是否存在sp中的multidex.version是不准确的，因为从低版本升级上来的用户，是包含这个sp配置的
        if (!isVMMultidexCapable()) {
            // the total dex numbers
            int totalDexNumber = getMultiDexPreferences(context).getInt("dex.number", 1);
            File dexDir =
                    new File(applicationInfo.dataDir, "code_cache" + File.separator + "secondary-dexes");

            for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
                // for each dex file, ie: test.classes2.zip, test.classes3.zip...
                String fileName = extractedFilePrefix + secondaryNumber + ".zip";
                File extractedFile = new File(dexDir, fileName);
                if (extractedFile.isFile()) {
                    sourcePaths.add(extractedFile.getAbsolutePath());
                    // we ignore the verify zip part
                } else {
                    throw new IOException(
                            "Missing extracted secondary dex file '" + extractedFile.getPath() + "'");
                }
            }
        }
        sourcePaths.addAll(tryLoadInstantRunDexFile(applicationInfo));
        return sourcePaths;
    }

    /**
     * Get instant run dex path, used to catch the branch usingApkSplits=false.
     */
    private static List<String> tryLoadInstantRunDexFile(ApplicationInfo applicationInfo) {
        List<String> instantRunSourcePaths = new ArrayList<>();

        if (null != applicationInfo.splitSourceDirs) {
            // add the split apk, normally for InstantRun, and newest version.
            instantRunSourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            Timber.tag("tryLoadInstantRunDexFile").e("Found InstantRun support");
        } else {
            try {
                // This man is reflection from Google instant run sdk, he will tell me where the dex files
                // go.
                Class pathsByInstantRun = Class.forName("com.android.tools.fd.runtime.Paths");
                Method getDexFileDirectory =
                        pathsByInstantRun.getMethod("getDexFileDirectory", String.class);
                String instantRunDexPath =
                        (String) getDexFileDirectory.invoke(null, applicationInfo.packageName);

                File instantRunFilePath = new File(instantRunDexPath);
                if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
                    File[] dexFile = instantRunFilePath.listFiles();
                    for (File file : dexFile) {
                        if (null != file && file.exists() && file.isFile() && file.getName().endsWith(".dex")) {
                            instantRunSourcePaths.add(file.getAbsolutePath());
                        }
                    }
                    Timber.tag("tryLoadInstantRunDexFile").e("Found InstantRun support");
                }

            } catch (Exception e) {
                Timber.tag("tryLoadInstantRunDexFile").e("InstantRun support error, " + e.getMessage());
            }
        }

        return instantRunSourcePaths;
    }

    /**
     * Identifies if the current VM has a native support for multidex, meaning there is no need for
     * additional installation by this library.
     *
     * @return true if the VM handles multidex
     */
    private static boolean isVMMultidexCapable() {
        boolean isMultidexCapable = false;
        String vmName = null;

        try {
            vmName = "'Android'";
            String versionString = System.getProperty("java.vm.version");
            if (versionString != null) {
                Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
                if (matcher.matches()) {
                    try {
                        int major = Integer.parseInt(matcher.group(1));
                        int minor = Integer.parseInt(matcher.group(2));
                        isMultidexCapable = (major > 2) || ((major == 2) && (minor >= 1));
                    } catch (NumberFormatException ignore) {
                        // let isMultidexCapable be false
                    }
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        Timber.tag("isVMMultidexCapable").e("VM with name "
                + vmName
                + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }
}
