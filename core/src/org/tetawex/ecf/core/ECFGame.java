package org.tetawex.ecf.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.I18NBundle;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.model.Language;
import org.tetawex.ecf.util.FontCharacters;
import org.tetawex.ecf.util.PreferencesProvider;

import java.util.HashMap;
import java.util.Locale;

public class ECFGame extends Game {
	private AssetManager assetManager;
	private TextureAtlas textureAtlas;
	private GameStateManager gameStateManager;
	private I18NBundle i18NBundle;

	private ShaderProgram shaderProgram;
	@Override
	public void create () {
	    assetManager =new AssetManager();
	    assetManager.load("atlas.atlas",TextureAtlas.class);
		assetManager.load("backgrounds/background.png",Texture.class);
        assetManager.load("backgrounds/background_pause.png",Texture.class);

		assetManager.load("sounds/click.ogg", Sound.class);
		assetManager.load("sounds/merge.ogg", Sound.class);
		assetManager.load("sounds/error.ogg", Sound.class);
        assetManager.load("i18n/bundle", I18NBundle.class);

		loadFonts();

	    assetManager.finishLoading();

	    setupLocalisation();

	    textureAtlas= assetManager.get("atlas.atlas", TextureAtlas.class);

	    gameStateManager=new GameStateManager(this, GameStateManager.GameState.MAIN_MENU);

        Music music=Gdx.audio.newMusic(Gdx.files.internal("music/ambient.ogg"));
        music.setLooping(true);
        music.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStateManager.renderCurrentScreen(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
	}

    @Override
    public void resize(int width, int height) {
        gameStateManager.getCurrentScreen().resize(width,height);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
    public TextureRegion getTextureRegionFromAtlas(String name){
		return textureAtlas.findRegion(name);
	}

	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	public String getLocalisedString(String id){
        return i18NBundle.get(id);
    }
    public void setupLocalisation(){
        Language ru_RU=new Language("Русский","ru_RU");
        Language en_US=new Language("English","en_US");
	    FontCharacters.setSupportedLanguages(new Language[]{ru_RU,en_US});

        ECFPreferences preferences=PreferencesProvider.getPreferences();

	    if(preferences.getSelectedLanguageCode().equals("default"))
            i18NBundle = assetManager.get("i18n/bundle", I18NBundle.class);
	    else{
            FileHandle baseFileHandle = Gdx.files.internal("i18n/bundle");
            Locale locale =new Locale(preferences.getSelectedLanguageCode(),preferences.getSelectedCountryCode());
            i18NBundle = I18NBundle.createBundle(baseFileHandle, locale);
        }
        FontCharacters.codeToLanguageMap=new HashMap<String, String>();
        FontCharacters.codeToLanguageMap.put("en_US","English");
        FontCharacters.codeToLanguageMap.put("ru_RU","Русский");
        FontCharacters.codeToLanguageMap.put("default_",getLocalisedString("auto"));
    }
	private void loadFonts(){
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // load to fonts via the generator (implicitly done by the FreetypeFontLoader).
        // Note: you MUST specify a FreetypeFontGenerator defining the ttf font file name and the size
        // of the font to be generated. The names of the fonts are arbitrary and are not pointing
        // to a file on disk!
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontParameters.characters= FontCharacters.en+FontCharacters.ru;
        params.fontFileName = "fonts/font_main.ttf";

        params.fontParameters.size = 48;
        assetManager.load("fonts/font_main_small.ttf", BitmapFont.class, params);

        params.fontParameters.size = 75;
        assetManager.load("fonts/font_main_medium.ttf", BitmapFont.class, params);
    }
}