package org.tetawex.ecf.util;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.screen.StyleFactory;

public class FileChooser extends Dialog {
    private FileHandle directory;
    private FileHandle file;

    private ECFGame game;

    public FileHandle getFile() {
        return file;
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }

    public void setDirectory(FileHandle directory) {
        if (this.directory != directory) {
            this.directory = directory;
            this.file = null;
            buildList();
        }
    }

    public void setFile(FileHandle file) {
        if (this.file != file) {
            if (this.file != null) {
                Label label = (Label) this.findActor(this.file.name());
                label.setColor(new Color(1,1,1,0.87f));
            }
            Label label = (Label) this.findActor(file.name());
            label.setColor(new Color(0.3f, 0.11f, 0.18f, 0.87f));
            this.file = file;
        }
    }

    public FileChooser(String title, WindowStyle style, ECFGame game) {
        super(title, style);
        this.game=game;
        this.getCell(getButtonTable()).expandX().fill();
        this.getButtonTable().defaults().expandX().pad(16).fill();

        this.button("Load", new FileHandle(""), StyleFactory.generateStandardMenuButtonStyle(game));
        this.button("Cancel",null, StyleFactory.generateStandardMenuButtonStyle(game));

        this.setModal(true);
    }

    public FileChooser(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
        this.setModal(true);
    }

    private void buildList() {
        FileHandle[] files = directory.list();
        Arrays.sort(files, new Comparator<FileHandle>() {
            @Override
            public int compare(FileHandle o1, FileHandle o2) {
                if (o1.isDirectory() && !o2.isDirectory()) {
                    return -1;
                }
                if (o2.isDirectory() && !o1.isDirectory()) {
                    return +1;
                }
                return o1.name().compareToIgnoreCase(o2.name());
            }
        });
        ScrollPane pane = new ScrollPane(null, StyleFactory.generateStandardScrollPaneStyle(game));
        Table table = new Table().top().left();
        table.defaults().left();
        ClickListener fileClickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Label target = (Label) event.getTarget();
                if (target.getName().equals("...")) {
                    setDirectory(directory.parent());
                } else {
                    FileHandle handle = directory.child(target.getName());
                    if (handle.isDirectory()) {
                        setDirectory(handle);
                    } else {
                        setFile(handle);
                    }
                }
            }
        };
        table.row();
        Label label = new Label("...", StyleFactory.generateStandardLabelStyle(game));
        label.setName("...");
        label.addListener(fileClickListener);
        table.add(label).expandX().pad(16).fillX();
        for (FileHandle file : files) {
            table.row();
            label = new Label(file.name(), StyleFactory.generateStandardLabelStyle(game));
            label.setName(file.name());
            label.addListener(fileClickListener);
            table.add(label).expandX().pad(16).fillX();
        }
        pane.setWidget(table);
        this.getContentTable().reset();
        this.getContentTable().add(pane).size(1200,1200);
    }

}
