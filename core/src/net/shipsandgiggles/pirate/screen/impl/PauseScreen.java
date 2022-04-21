package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.shipsandgiggles.pirate.HUDmanager;
import net.shipsandgiggles.pirate.conf.Configuration;

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

        Table pauseTable = new Table();

        Label paused = new Label("Game Paused", Configuration.SKIN, "title");
        paused.setAlignment(Align.center);
        Label unpause = new Label("To unpause press SPACE", Configuration.SKIN, "big");
        unpause.setAlignment(Align.center);

        paused.setFontScale(1.5f);
        unpause.setFontScale(0.8f);

        pauseTable.add(paused);
        pauseTable.row();
        pauseTable.add(unpause);
        pauseTable.setPosition(Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight()/2 + 100);

        stage.addActor(pauseTable);


    }


}
