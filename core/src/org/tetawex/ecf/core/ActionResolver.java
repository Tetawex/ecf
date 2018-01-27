package org.tetawex.ecf.core;

import org.tetawex.ecf.model.LevelData;
import org.tetawex.ecf.util.BasicListener;
import org.tetawex.ecf.util.ConsumerListener;

/**
 * Created by Tetawex on 12.06.2017.
 */
public interface ActionResolver {
    void setBackPressedListener(BasicListener listener);

    boolean externalStorageAccessible();

    void openUrl(String url);
}