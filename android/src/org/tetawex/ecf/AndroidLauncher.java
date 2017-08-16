package org.tetawex.ecf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.tetawex.ecf.core.ActionResolver;
import org.tetawex.ecf.core.ActionResolverAdapter;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.model.LevelData;
import org.tetawex.ecf.util.BasicListener;

import java.io.File;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
    private static final int REQUEST_CHOOSER = 1;
    private BasicListener backPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        initialize(new ECFGame(this), config);
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

    @Override
    public boolean externalStorageAccessible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        return true;
    }

}
