package org.tetawex.ecf;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import org.tetawex.ecf.core.ActionResolver;
import org.tetawex.ecf.core.ECFGame;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
    private static final int REQUEST_CHOOSER = 1;
    private Function0<Unit> backPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        initialize(new ECFGame(this), config);
    }

    @Override
    public void onBackPressed() {
        //run on render thread cuz libgdx crashes otherwise
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (backPressedListener != null)
                    backPressedListener.invoke();
            }
        });

    }


    @Override
    public boolean externalStorageAccessible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        return true;
    }

    @Override
    public void openUrl(@NotNull String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Nobody will ever see this message", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void setBackPressedListener(@NotNull Function0<Unit> listener) {
        backPressedListener = listener;
    }
}
