package org.tetawex.ecf.model;

/**
 * Created by Tetawex on 05.06.2017.
 */
public class LevelCompletionState {
    private int stars = 0;
    private boolean completed = false;
    private boolean unlocked = false;

    public LevelCompletionState() {
    }

    public LevelCompletionState(int stars, boolean completed, boolean unlocked) {
        this.stars = stars;
        this.completed = completed;
        this.unlocked = unlocked;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
