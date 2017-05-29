package org.tetawex.ecf.core.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import org.tetawex.ecf.core.ECFGame;

/**
 * Created by Tetawex on 23.05.2017.
 */
public class StyleFactory {
    public static TextButton.TextButtonStyle generatePauseButtonSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main.fnt",BitmapFont.class);
        textButtonStyle.up = skin.getDrawable("button_pause");
        textButtonStyle.down = skin.getDrawable("button_pause");
        return textButtonStyle;
    }
    public static Label.LabelStyle generateScoreLabelSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        Label.LabelStyle textButtonStyle = new Label.LabelStyle();
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main.fnt",BitmapFont.class);
        textButtonStyle.fontColor=new Color(0.85f,0.85f,0.85f,0.7f);
        return textButtonStyle;
    }
    public static Label.LabelStyle generateManaLabelSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        Label.LabelStyle textButtonStyle = new Label.LabelStyle();
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main.fnt",BitmapFont.class);
        textButtonStyle.fontColor=new Color(0.3f,0.75f,0.85f,0.8f);
        return textButtonStyle;
    }
    public static TextButton.TextButtonStyle generatePauseMenuButtonSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        NinePatchDrawable ninePatch = new NinePatchDrawable(skin.getPatch("button_menu"));
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main.fnt",BitmapFont.class);
        textButtonStyle.up = new Image(ninePatch).getDrawable();
        textButtonStyle.checked = new Image(ninePatch).getDrawable();
        ninePatch = new NinePatchDrawable(skin.getPatch("button_menu_pressed"));
        textButtonStyle.down = new Image(ninePatch).getDrawable();

        return textButtonStyle;
    }
    public static TextButton.TextButtonStyle generateMainMenuButtonSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main.fnt",BitmapFont.class);
        textButtonStyle.up = skin.getDrawable("button_pause");
        textButtonStyle.down = skin.getDrawable("button_pause");
        return textButtonStyle;
    }
}
