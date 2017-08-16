package org.tetawex.ecf.core;

import org.tetawex.ecf.model.LevelData;
import org.tetawex.ecf.util.BasicListener;

/**
 * Created by Tetawex on 12.06.2017.
 */
public class ActionResolverAdapter implements ActionResolver {

    @Override
    public void setBackPressedListener(BasicListener listener) {

    }

    @Override
    public boolean externalStorageAccessible() {
        return true;
    }
}
