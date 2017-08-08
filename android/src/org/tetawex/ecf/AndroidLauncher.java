package org.tetawex.ecf;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.tetawex.ecf.core.ActionResolver;
import org.tetawex.ecf.core.ActionResolverAdapter;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.util.BasicListener;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
    private BasicListener backPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        initialize(new ECFGame(new ActionResolverAdapter()), config);
    }

    @Override
    public void onBackPressed() {
        if (backPressedListener != null)
            backPressedListener.call();
    }

    @Override
    public void setBackPressedListener(BasicListener listener) {
        backPressedListener = listener;
    }
}
