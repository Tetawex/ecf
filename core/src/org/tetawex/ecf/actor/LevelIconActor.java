package org.tetawex.ecf.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.model.LevelCompletionState;

/**
 * Created by Tetawex on 06.06.2017.
 */
public class LevelIconActor extends BaseWidget<ECFGame>{
    private BitmapFont font;
    private LevelCompletionState levelCompletionState;
    private int number;
    private TextureRegion starRegion;
    private TextureRegion starDisabledRegion;
    private TextureRegion backgroundRegion;
    private TextureRegion tintRegion;
    private float starSize=80;

    public LevelIconActor(ECFGame game, LevelCompletionState levelCompletionState, BitmapFont font, int number) {
        super(game);
        this.levelCompletionState=levelCompletionState;
        this.font = font;
        this.number=number;

        starRegion =getGame().getTextureRegionFromAtlas("star");
        starDisabledRegion =getGame().getTextureRegionFromAtlas("star_ungained");
        backgroundRegion =getGame().getTextureRegionFromAtlas("level_active");
        tintRegion =getGame().getTextureRegionFromAtlas("level_inactive");
    }
    @Override
    public void act(float deltaTime) {
    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(backgroundRegion, getX(), getY(), 400, 400);
        font.draw(batch, number + "", getX()+getWidth()/2 - 20, getY() +getHeight()/2+40);
        if(levelCompletionState.isCompleted()) {
            for (int i = 1; i <= 3; i++) {
                if (levelCompletionState.getStars() >= i)
                    batch.draw(starRegion, getX() + starSize * (i - 1), getY(), starSize, starSize);
                else
                    batch.draw(starDisabledRegion, getX() + starSize * (i - 1), getY(), starSize, starSize);
            }
        }
        if(!levelCompletionState.isUnlocked()){
            batch.draw(tintRegion, getX(), getY(), 400, 400);
        }
    }
    @Override
    public float getPrefWidth() {
        return 400;
    }
    @Override
    public float getPrefHeight() {
        return 400;
    }
}
