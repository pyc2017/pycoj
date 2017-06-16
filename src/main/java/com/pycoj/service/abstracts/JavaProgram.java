package com.pycoj.service.abstracts;

import com.pycoj.concurrency.ErrStreamReader;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by Heyman on 2017/5/17.
 */
public class JavaProgram extends AbstractProgram {
    private static Logger log=Logger.getLogger(JavaProgram.class);
    private static Runtime runtime=Runtime.getRuntime();
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
            return new State(0,0,0,1,sb.toString());
        }
        return new State(0,0,0,0,"");//submitId t m state info
    }

    /**
     * 运行.class文件
     * @param codeDir 字节码文件路径
     */
    @Override
    public State[] run(String codeDir,String questionDir,int id) throws Exception{
        InputStream[] inputFileStream=getInput(questionDir+id);
        InputStream[] outputFileStream=getOutput(questionDir+id);
        State[] resultStates=new State[inputFileStream.length];

        //开始执行子进程
        int len=-1,len2=-1;
        for (int i=0;i<inputFileStream.length;i++) {
            Process child=runtime.exec("cmd /c java -cp "+codeDir+" Main");
            BufferedOutputStream inputForChildProcess= (BufferedOutputStream) child.getOutputStream();
            BufferedInputStream outputOfChildProcess = (BufferedInputStream) child.getInputStream();
            Thread errThread=new Thread(new ErrStreamReader(child.getErrorStream()));
            errThread.start();
            while ((len = inputFileStream[i].read()) != -1) {//读取至文件末尾
                //向子程序输入
                inputForChildProcess.write((byte)len);
                inputForChildProcess.flush();
            }
            inputFileStream[i].close();
            long start = System.currentTimeMillis();
            boolean exit=child.waitFor(1, TimeUnit.SECONDS);//等待子进程完成
            long tCost=System.currentTimeMillis() - start;
            if (!exit){
                resultStates[i]=new State(0,(int)tCost,0,2,"TLE");
                continue;
            }
            inputForChildProcess.close();
            //获取子进程的输出，并与输出用例做对比
            while ((len = outputFileStream[i].read()) != -1) {
                len2=outputOfChildProcess.read();
            //    System.out.print(len);System.out.print(" ");System.out.println(len2);
                if (len!=len2) {
                    resultStates[i]=new State(0,(int)tCost,0,4,"wrong answer");//wa
                    break;
                }
            }
            //确保标准输出用例与程序输出同时读完才能正确
            if ((len2=outputOfChildProcess.read())!=-1){
                resultStates[i]=new State(0,(int)tCost,0,4,"wrong answer");//wa
            }
            if (resultStates[i]==null){
                resultStates[i]=new State(0,(int)tCost,0,0,"accepted");
            }
            child.destroy();
            outputOfChildProcess.close();
            errThread.interrupt();
        }
        return resultStates;
    }

    @Override
    public final String getFileName() {
        return "Main.java";
    }
}
