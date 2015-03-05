package ciid2015.exquisitedatacorpse.compileatruntime;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class PodCompiler extends ClassLoader {

    private final JavaCompiler mCompiler = ToolProvider.getSystemJavaCompiler();
    private final MemoryFileManager mManager = new MemoryFileManager(this.mCompiler);

    public static Pod instance(String pClassName, String pSrc) {
        try {
            Object o = new PodCompiler(pClassName, pSrc).loadClass(pClassName).getConstructor().newInstance();
            return (Pod) o;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public PodCompiler(String classname, String filecontent) {
        this(Collections.singletonMap(classname, filecontent));
    }

    private PodCompiler(Map<String, String> map) {
        List<Source> list = new ArrayList<Source>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new Source(entry.getKey(), Kind.SOURCE, entry.getValue()));
        }
        this.mCompiler.getTask(null, this.mManager, null, null, null, list).call();
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        synchronized (this.mManager) {
            Output mc = this.mManager.map.remove(name);
            if (mc != null) {
                byte[] array = mc.toByteArray();
                return defineClass(name, array, 0, array.length);
            }
        }
        return super.findClass(name);
    }

    class MemoryFileManager extends ForwardingJavaFileManager {

        private final Map<String, Output> map = new HashMap<String, Output>();

        public MemoryFileManager(JavaCompiler compiler) {
            super(compiler.getStandardFileManager(null, null, null));
        }

        public Output getJavaFileForOutput(JavaFileManager.Location location, String name, Kind kind, FileObject source) {
            Output mc = new Output(name, kind);
            this.map.put(name, mc);
            return mc;
        }
    }

    class Output extends SimpleJavaFileObject {

        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        public Output(String name, Kind kind) {
            super(URI.create("memo:///" + name.replace('.', '/') + kind.extension), kind);
        }

        byte[] toByteArray() {
            return this.baos.toByteArray();
        }

        public ByteArrayOutputStream openOutputStream() {
            return this.baos;
        }
    }

    class Source extends SimpleJavaFileObject {

        private final String content;

        public Source(String name, Kind kind, String content) {
            super(URI.create("memo:///" + name.replace('.', '/') + kind.extension), kind);
            this.content = content;
        }

        public CharSequence getCharContent(boolean ignore) {
            return this.content;
        }
    }
}
