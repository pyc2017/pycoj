package com.pycoj;

import com.pycoj.entity.State;
import com.pycoj.service.abstracts.JavaProgram;
import com.pycoj.service.abstracts.Program;
import org.junit.Test;

/**
 * Created by Heyman on 2017/6/16.
 */
public class JavaProgramTest {
    @Test
    public void runTest() throws Exception {
        Program java=new JavaProgram();
        State[] s= java.run("E:\\ojprogram\\2\\ynF0A1HiE4ZOV8ZqNPLZIfzi4Vq6o3wM","E:\\ojquestion\\",2);
        for (State _s:s){
            System.out.println(_s.getState()+":"+_s.getInfo());
        }
    }
}
