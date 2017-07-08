package com.pycoj.service.abstracts;

import com.pycoj.entity.State;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Created by Heyman on 2017/5/17.
 */
@Component("javaProgram")
public class JavaProgram extends AbstractProgram {
    private static Logger log=Logger.getLogger(JavaProgram.class);
    private static JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();
    @Autowired @Qualifier("javaRunningFileInputStream") private FileInputStream javaRunningFileInputStream;

    /**
     * 编译java文件
     * 已经可以编译源文件，需要优化
     * 由于需要添加计时功能，需要额外有其他类来调用当前类，采用装饰模式，该类为Main1.class
     * @param codeDir java文件全名
     */
    public State compile(File codeDir) throws IOException {
        //将Main1.class拷贝到目标文件夹
        FileChannel inputChannel=javaRunningFileInputStream.getChannel();
        File copiedFile=new File(codeDir,"Main1.class");
        copiedFile.createNewFile();
        FileOutputStream fos=new FileOutputStream(copiedFile);
        fos.getChannel().transferFrom(inputChannel,0,inputChannel.size());
        //将Main1$1.class拷贝到目标文件夹
        copiedFile=new File(codeDir,"Main1$1.class");
        copiedFile.createNewFile();
        fos=new FileOutputStream(copiedFile);
        fos.getChannel().transferFrom(inputChannel,0,inputChannel.size());
        //复制装饰部分完毕

        File[] files=new File[]{new File(codeDir,"Main.java")};

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();//收集错误信息
        StandardJavaFileManager manager=compiler.getStandardFileManager(diagnostics,null,null);
        Iterable<? extends JavaFileObject> compilation=manager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        compiler.getTask(null,manager,diagnostics,null,null,compilation).call();
        manager.close();
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
        return "cmd /c java -cp "+codeDir+" Main1";
    }
}
