package org.tetawex.ecf.model;

import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.screen.BaseScreen;
import org.tetawex.ecf.util.BasicListener;

/**
 * Created by tetawex on 08.08.17.
 */
public abstract class BaseECFScreen extends BaseScreen<ECFGame> {
    public BaseECFScreen(ECFGame game) {
        super(game);
        setupListener();
    }
    protected void setupListener(){
        getGame().getActionResolver().setBackPressedListener(new BasicListener() {
            @Override
            public void call() {
                onBackPressed();
            }
        });
    }
    public abstract void onBackPressed();
}
