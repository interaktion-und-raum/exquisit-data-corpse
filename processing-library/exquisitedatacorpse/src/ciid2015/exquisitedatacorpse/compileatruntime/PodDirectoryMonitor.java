package ciid2015.exquisitedatacorpse.compileatruntime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;

public class PodDirectoryMonitor extends Thread {

    private final ArrayList<String> mFiles = new ArrayList<String>();

    private final String mPathStr;

    private final PodListener mPodListener;

    private final String mExtension;

    private PodDirectoryMonitor(PodListener pPodListener, String pPath, String pExtension) {
        mPathStr = pPath;
        mExtension = pExtension;
        mPodListener = pPodListener;

        /* collect files from folder */
        File mFolder = new File(pPath);
        File[] mFileList = mFolder.listFiles();
        for (File mFile : mFileList) {
            if (accept(mFile.getName())) {
                mFiles.add(mFile.getName());
            }
        }
        System.out.println("### files found in directory: " + mFiles);
    }

    public void run() {
        try {
            Path mPath = Paths.get(mPathStr);
            WatchService mWatchService = FileSystems.getDefault().newWatchService();
            mPath.register(mWatchService,
                           StandardWatchEventKinds.ENTRY_CREATE,
                           StandardWatchEventKinds.ENTRY_DELETE,
                           StandardWatchEventKinds.ENTRY_MODIFY);

            boolean valid = true;
            do {
                WatchKey watchKey = mWatchService.take();
                for (WatchEvent mEvent : watchKey.pollEvents()) {
                    WatchEvent.Kind mKind = mEvent.kind();
                    String mFileName = mEvent.context().toString();
                    if (accept(mFileName)) {
                        if (StandardWatchEventKinds.ENTRY_CREATE.equals(mKind)) {
//                            System.out.println("### file created:" + mFileName);
                            mFiles.add(mFileName);
                            if (mPodListener != null) {
                                mPodListener.pod_created(mFileName);
                            }
                        } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(mKind)) {
//                            System.out.println("### file deleted:" + mFileName);
                            mFiles.remove(mFileName);
                            if (mPodListener != null) {
                                mPodListener.pod_deleted(mFileName);
                            }
                        } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(mKind)) {
//                            System.out.println("### file modified:" + mFileName);
                            if (mPodListener != null) {
                                mPodListener.pod_modified(mFileName);
                            }
                        } else {
                            System.out.println("### not handling event: " + mKind);
                        }
                    }
                    System.out.println("### files in directory: " + mFiles);
                }
                valid = watchKey.reset();
                Thread.sleep(1000);
            } while (valid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String path() {
        return mPathStr + "/";
    }

    private boolean accept(String pFileName) {
        return !pFileName.startsWith(".") && pFileName.endsWith(mExtension);
    }

    public static PodDirectoryMonitor monitor(PodListener pPodListener, String pDir, String pExtension) {
        PodDirectoryMonitor mMonitor = new PodDirectoryMonitor(pPodListener, pDir, pExtension);
        mMonitor.start();
        return mMonitor;
    }

    public String loadString(String pFile) {
        return loadString(path(), pFile);
    }

    public static String loadString(String pPath, String pFile) {
        final File mFile = new File(pPath + pFile);
        final StringBuilder content = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(mFile));
            String s = null;

            while ((s = reader.readLine()) != null) {
                content.append(s).append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    public static void main(String[] args) {
        String mDir = System.getProperty("user.dir") + "/data";
        System.out.println("monitoring directory: " + mDir);
        PodDirectoryMonitor.monitor(null, mDir, ".java");
    }

    public interface PodListener {

        void pod_created(String mFile);

        void pod_deleted(String mFile);

        void pod_modified(String mFile);
    }
}
