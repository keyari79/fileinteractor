package com.company;

import java.io.IOException;

/**
 * Created by keyarash on 08.01.17.
 */
public class MyAutoCloseable implements AutoCloseable {
    public void saySomething() throws IOException {
        throw new IOException("Exceptin from say something");
//        System.out.println("Something");
    }

    @Override
    public void close() throws IOException{
       throw new IOException("Exception from Close");
    }
}
