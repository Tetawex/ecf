package org.tetawex.ecf.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ECFGame extends Game {
	private AssetManager assetManager;
	private TextureAtlas textureAtlas;
	private GameStateManager gameStateManager;
	@Override
	public void create () {
	    assetManager=new AssetManager();
	    assetManager.load("atlas.atlas",TextureAtlas.class);
		assetManager.load("backgrounds/background.png",Texture.class);
        assetManager.load("backgrounds/background_pause.png",Texture.class);

		assetManager.load("sounds/click.ogg", Sound.class);
		assetManager.load("sounds/merge.ogg", Sound.class);
		assetManager.load("sounds/error.ogg", Sound.class);

		assetManager.load("fonts/font_main.fnt", BitmapFont.class);

	    assetManager.finishLoading();

	    textureAtlas=assetManager.get("atlas.atlas", TextureAtlas.class);

	    gameStateManager=new GameStateManager(this);

	    gameStateManager.setState(GameStateManager.GameState.GAME);

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


    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}