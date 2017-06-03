package org.tetawex.ecf.core.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main_small.ttf",BitmapFont.class);
        textButtonStyle.up = skin.getDrawable("button_pause");
        textButtonStyle.down = skin.getDrawable("button_pause");
        return textButtonStyle;
    }
    public static TextButton.TextButtonStyle generateManaButtonSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getAssetManager().get("fonts/font_main_small.ttf",BitmapFont.class);
        textButtonStyle.up = skin.getDrawable("mana");
        textButtonStyle.down = skin.getDrawable("mana");
        textButtonStyle.fontColor=new Color(0f,0f,0f,0.87f);
        return textButtonStyle;
    }
    public static Label.LabelStyle generateStandardLabelSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.getAssetManager().get("fonts/font_main_medium.ttf",BitmapFont.class);
        style.fontColor=new Color(1f,1f,1f,0.87f);
        return style;
    }
    public static Label.LabelStyle generateDarkLabelSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.getAssetManager().get("fonts/font_main_medium.ttf",BitmapFont.class);
        style.fontColor=new Color(0.8f,0.58f,0.58f,0.87f);
        return style;
    }
    public static Label.LabelStyle generateDarkerLabelSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.getAssetManager().get("fonts/font_main_medium.ttf",BitmapFont.class);
        style.fontColor=new Color(0.3f,0.11f,0.18f,0.87f);
        return style;
    }
    public static Slider.SliderStyle generateStandardSliderSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.knob=skin.getDrawable("slider");
        style.background=skin.getDrawable("dark_line");

        return style;
    }
    public static ScrollPane.ScrollPaneStyle generateStandardScrollPaneSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.vScrollKnob=skin.getDrawable("slider");
        style.vScroll=skin.getDrawable("dark_line");

        return style;
    }
    public static TextButton.TextButtonStyle generateStandardMenuButtonSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        NinePatchDrawable ninePatch = new NinePatchDrawable(skin.getPatch("button_menu"));
        style.font = game.getAssetManager().get("fonts/font_main_medium.ttf",BitmapFont.class);
        style.fontColor=new Color(1f,1f,1f,0.87f);
        style.downFontColor=new Color(0.8f,0.58f,0.58f,0.87f);
        style.up = new Image(ninePatch).getDrawable();
        style.checked = new Image(ninePatch).getDrawable();
        ninePatch = new NinePatchDrawable(skin.getPatch("button_menu_pressed"));
        style.down = new Image(ninePatch).getDrawable();

        return style;
    }
    public static TextButton.TextButtonStyle generateLanguageMenuButtonSkin(ECFGame game){
        Skin skin = new Skin();
        skin.addRegions(game.getAssetManager().get("atlas.atlas",TextureAtlas.class));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        NinePatchDrawable ninePatch = new NinePatchDrawable(skin.getPatch("button_menu_bottom"));
        style.font = game.getAssetManager().get("fonts/font_main_medium.ttf",BitmapFont.class);
        style.fontColor=new Color(1f,1f,1f,0.87f);
        style.downFontColor=new Color(0.8f,0.58f,0.58f,0.87f);
        style.up = new Image(ninePatch).getDrawable();
        style.checked = new Image(ninePatch).getDrawable();
        ninePatch = new NinePatchDrawable(skin.getPatch("button_menu_bottom"));
        style.down = new Image(ninePatch).getDrawable();

        return style;
    }
}
