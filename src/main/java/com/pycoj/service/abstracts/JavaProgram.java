package com.pycoj.service.abstracts;

import com.pycoj.entity.State;
import org.apache.log4j.Logger;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Created by Heyman on 2017/5/17.
 */
public class JavaProgram extends AbstractProgram {
    private static Logger log=Logger.getLogger(JavaProgram.class);
    private static Runtime runtime=Runtime.getRuntime();
    private static final String _CMD="java Main";
    private static JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();

    /**
     * 编译java文件
     * 已经可以编译源文件，需要优化
     * @param codeDir java文件全名
     */
    @Override
    public State compile(String codeDir) throws IOException {
        long start=System.currentTimeMillis();
        File[] files=new File[]{new File(codeDir+"\\Main.java")};

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
            return new State(1,sb.toString());
        }
        return new State(0,"");
    }

    /**
     * 运行.class文件
     * @param codeDir 字节码文件路径
     */
    @Override
    public State run(String codeDir,String questionDir,int id) throws Exception{
        InputStream inputFileStream=getInput(questionDir+id);
        InputStream outputFileStream=getOutput(questionDir+id);
        byte[] input=new byte[1024];//存储输入用例的缓冲区
        byte[] output=new byte[1024];//存储输出用例的缓冲区
        byte[] outputOfChild=new byte[1024];//存储子进程输出的缓冲区

        //开始执行子进程
        Process child=runtime.exec(_CMD);
        BufferedOutputStream inputForChildProcess= (BufferedOutputStream) child.getOutputStream();
        int len=-1;
        while ((len=inputFileStream.read(input))!=-1){//读取至文件末尾
            //向子程序输入
            inputForChildProcess.write(input);
        }
        child.waitFor();//等待子进程完成

        //获取子进程的输出，并与输出用例做对比
        BufferedInputStream outputOfChildProcess= (BufferedInputStream) child.getInputStream();
        while ((len=outputFileStream.read(output))!=-1){
            String standard,result;
            standard=new String(output);
            outputOfChildProcess.read(outputOfChild);
            result=new String(outputOfChild);
            if (standard.equals(result)){
                return new State(4,"");
            }
        }
        return new State(0,"");
    }

    @Override
    public final String getFileName() {
        return "Main.java";
    }
}
