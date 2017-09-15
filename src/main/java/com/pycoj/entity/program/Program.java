package com.pycoj.entity.program;

import com.pycoj.entity.State;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Heyman on 2017/5/17.
 */
public interface Program {
    /**
     * 编译源文件
     * @param codeDir 路径
     * @return
     * @throws IOException
     */
    public State compile(File codeDir) throws IOException;

    /**
     *
     * @param codeDir 源代码的路径，不包括源代码名字
     * @param questionDir 问题的路径
     * @param id
     * @param sendState 是否需要session发送每一个state
     * @return
     * @throws Exception
     */
    public State[] run(String codeDir, String questionDir, int id, WebSocketSession session,boolean sendState) throws Exception;

    /**
     * 程序源代码的名字，java为Main.java，c为main.c，cpp为main.cpp
     * @return
     */
    public String getFileName();

    public String getExecutionCommand(String codeDir);

    /**
     * 获取题目的对应输入用例的输入通道
     * @param inputFileDir 输入用例所在路径
     * @return
     */
    public FileChannel[] getInput(String inputFileDir) throws FileNotFoundException;

    /**
     * 获取题目对应的输出用例的输入通道
     * @param outputFileDir 输出用例所在路径
     * @return
     */
    public FileChannel[] getOutput(String outputFileDir) throws FileNotFoundException;
}