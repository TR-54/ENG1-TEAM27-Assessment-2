package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.shipsandgiggles.pirate.HUDmanager;
import net.shipsandgiggles.pirate.PirateGame;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.ScreenType;

public class PauseScreen {
    /** adds UI for paused screen **/

    /** This is a very basic implmentation. If you want to add extra functionality do so here **/

    public Stage stage;
    public Viewport viewport;
    public Label paused;
    public Label unpause;

    public PauseScreen (SpriteBatch batch){
        /** construction and setting of the labels*/
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(this.stage);
        Table pauseTable = new Table();

        Label paused = new Label("Game Paused", Configuration.SKIN, "title");
        paused.setAlignment(Align.center);
        Label unpause = new Label("To unpause press SPACE", Configuration.SKIN, "big");
        unpause.setAlignment(Align.center);
        Label spacer = new Label("", Configuration.SKIN, "big");


        TextButton saveAndExit = new TextButton("Save and Exit", Configuration.SKIN);
        saveAndExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LoadingScreen.previousGameExists = true;
                LoadingScreen.soundController.playButtonPress();
                LoadingScreen.soundController.pauseAll();
                PirateGame.get().changeScreen(ScreenType.LOADING);
            }
        });

        TextButton exit = new TextButton("Exit", Configuration.SKIN);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LoadingScreen.previousGameExists = false;
                LoadingScreen.soundController.playButtonPress();
                LoadingScreen.soundController.pauseAll();
                PirateGame.get().changeScreen(ScreenType.LOADING);
            }
        });


        paused.setFontScale(1.5f);
        unpause.setFontScale(0.8f);

        pauseTable.add(paused);
        pauseTable.row();
        pauseTable.add(unpause);
        pauseTable.row();
        pauseTable.add(spacer);
        pauseTable.row();
        pauseTable.add(saveAndExit).fillX().uniformX();
        pauseTable.row();
        pauseTable.add(spacer);
        pauseTable.row();
        pauseTable.add(exit).fillX().uniformX();
        pauseTable.setPosition(Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight()/2 + 100);

        stage.addActor(pauseTable);


    }


}
