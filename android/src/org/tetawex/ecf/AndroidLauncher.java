package org.tetawex.ecf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;
import org.tetawex.ecf.core.ActionResolver;
import org.tetawex.ecf.core.ActionResolverAdapter;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.model.LevelData;
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

    @Override
    public void saveLevelData(LevelData levelData) {
        Intent intent = new Intent(this, FileChooserActivity.class);
        startActivityForResult(intent, FILE_CHOOSER);
    }

    @Override
    public LevelData loadLevelData() {
        return null;
    }

    final int FILE_CHOOSER=1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FILE_CHOOSER) && (resultCode == RESULT_OK)) {
            String fileSelected = data.getStringExtra(Constants.KEY_FILE_SELECTED);
            Toast.makeText(this, "file selected "+fileSelected, Toast.LENGTH_SHORT).show();
        }
    }
}
