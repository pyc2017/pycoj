package com.pycoj.service.abstracts;

import com.pycoj.entity.State;
import org.apache.log4j.Logger;

import javax.tools.*;
import java.io.*;
import java.util.Arrays;

/**
 * Created by Heyman on 2017/5/17.
 */
public class JavaProgram extends AbstractProgram {
    private static Logger log=Logger.getLogger(JavaProgram.class);
    private static JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();

    /**
     * 编译java文件
     * 已经可以编译源文件，需要优化
     * @param codeDir java文件全名
     */
    @Override
    public State compile(File codeDir) throws IOException {
        long start=System.currentTimeMillis();
        File[] files=new File[]{new File(codeDir,"Main.java")};

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();//收集错误信息
        StandardJavaFileManager manager=compiler.getStandardFileManager(diagnostics,null,null);
        Iterable<? extends JavaFileObject> compilation=manager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        compiler.getTask(null,manager,diagnostics,null,null,compilation).call();
        manager.close();
        log.info("编译时间为:"+(System.currentTimeMillis()-start));
        if (diagnostics.getDiagnostics().size()>0){
            StringBuilder sb=new StringBuilder();
            for (Diagnostic d:diagnostics.getDiagnostics()){
                sb.append("错误发生在第");
                sb.append(d.getLineNumber());
                sb.append("行：");
                sb.append(d.getMessage(null));
                sb.append("\r\n");
            }
            //编译错误
            return new State(0, 0, 0,0,1,sb.toString());
        }
        return new State(0, 0, 0,0,0,"");//submitId t m state info
    }

    @Override
    public final String getFileName() {
        return "Main.java";
    }

    @Override
    public final String getExecutionCommand(String codeDir) {
        return "cmd /c java -cp "+codeDir+" Main";
    }
}
