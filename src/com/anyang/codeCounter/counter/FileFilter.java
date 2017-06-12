package com.anyang.codeCounter.counter;

import java.io.File;

/**
 * Created by Anyang on 2017/6/12.
 */
public interface FileFilter {
    boolean match(File file);
}
