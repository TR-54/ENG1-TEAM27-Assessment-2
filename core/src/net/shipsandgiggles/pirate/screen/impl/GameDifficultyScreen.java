package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.CannonBall;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.college.College;
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;
import net.shipsandgiggles.pirate.entity.impl.college.GoodrickCollege;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;
import net.shipsandgiggles.pirate.screen.ScreenType;

public class GameDifficultyScreen implements Screen {

    public static int difficulty;

    /** the main screen */

    private Stage stage;
    private Table table;

    public Sprite background = new Sprite(new Texture(Gdx.files.internal("models/background.PNG")));;
    private final SpriteBatch batch = new SpriteBatch();;

    @Override
    public void show() {
        this.table = new Table();

        this.table.setFillParent(true);
        this.table.setDebug(true);

        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);

        this.stage.addActor(this.table);

        Label title = new Label("Choose Game Difficulty", Configuration.SKIN, "title");
        title.setAlignment(Align.center);

        /** Initialise Buttons*/

        /** New Game Button*/
        TextButton newGame = new TextButton("Easy", Configuration.SKIN);
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficulty = 0;
                College.setCooldownTimer(2f);
                AlcuinCollege.setHealth(1);
                ConstantineCollege.setHealth(1);
                GoodrickCollege.setHealth(1);
                LangwithCollege.setHealth(1);
                CannonBall.setDamage(50f);
                EntityAi.setCooldownTimer(2f);
                LoadingScreen.soundController.playButtonPress();
                PirateGame.get().changeScreen(ScreenType.INFORMATION);
            }
        });

        /**Preferences Button */
        TextButton preferences = new TextButton("Intermediate", Configuration.SKIN);
        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficulty = 1;
                College.setCooldownTimer(1.5f);
                AlcuinCollege.setHealth(2);
                ConstantineCollege.setHealth(2);
                GoodrickCollege.setHealth(2);
                LangwithCollege.setHealth(2);
                CannonBall.setDamage(70f);
                Ship.changeMaxHealth(150f);
                EntityAi.setCooldownTimer(1.5f);
                LoadingScreen.soundController.playButtonPress();
                PirateGame.get().changeScreen(ScreenType.INFORMATION);
            }
        });

        /** Exit Button*/
        TextButton exit = new TextButton("Hard", Configuration.SKIN);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficulty = 2;
                College.setCooldownTimer(1f);
                AlcuinCollege.setHealth(2);
                ConstantineCollege.setHealth(2);
                GoodrickCollege.setHealth(2);
                LangwithCollege.setHealth(2);
                CannonBall.setDamage(90f);
                Ship.changeMaxHealth(100f);
                EntityAi.setCooldownTimer(1f);
                LoadingScreen.soundController.playButtonPress();
                PirateGame.get().changeScreen(ScreenType.INFORMATION);
            }
        });

        /**  Loading Screen Data*/

        /**Creates a uniform X/Y table. */
        table.add(title).fillX().uniformX();
        /** Sets default gap between.*/
        table.row().pad(10, 0, 10, 0);
        table.add(newGame).fillX().uniformX();
        table.row();
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.98f, .91f, .761f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        batch.end();
        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        this.stage.draw();
    }

    public static int getDifficulty(){
        return difficulty;
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
