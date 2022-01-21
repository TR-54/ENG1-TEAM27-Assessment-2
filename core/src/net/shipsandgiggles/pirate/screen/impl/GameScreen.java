package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.shipsandgiggles.pirate.CameraManager;
import net.shipsandgiggles.pirate.TiledObjectUtil;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Ship;
import static net.shipsandgiggles.pirate.conf.Configuration.PixelPerMeter;

public class GameScreen implements Screen {

	//camera work
	private OrthographicCamera camera;
	int cameraState = 0;
	private float Scale = 2;
	//private Viewport viewport;

	//graphics
	private SpriteBatch batch; //batch of images "objects"
	//private Texture background; changed to colour of "deep water"
	//private Body[] islands;
	//private Texture[] islandsTextures;
	private Texture[] boats;

	//implement world
	public static World world;
	private Box2DDebugRenderer renderer;
	private int _height = Gdx.graphics.getHeight();
	private int _width = Gdx.graphics.getWidth();
	private OrthoCachedTiledMapRenderer tmr;
	private TiledMap map;

	//objects
	private Ship playerShips;
	private Ship enemyShips;



	public GameScreen(){
		//for (int x = 0; x < islands.length; x++) islands[x] = createBox(22, 34, true, new Vector2(29,39));
		//islands = new Body[1];
		//islandsTextures = new Texture[1];
		//islandsTextures[0] = new Texture(Gdx.files.internal("models/island1.png"));

		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0,0), false);
		boats = new Texture[3];
		boats[0] = new Texture(Gdx.files.internal("models/ship1.png"));
		boats[1] = new Texture(Gdx.files.internal("models/ship2.png"));
		boats[2] = new Texture(Gdx.files.internal("models/ship3.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, _width/Scale, _height/Scale);
		//viewport = new StretchViewport(_width, _height, camera);
		batch = new SpriteBatch();

		//objects setup
		int random = (int) Math.floor((Math.random() * 2.99f)); //generate random boat
		playerShips = new Ship(boats[0], 40000, 6000, 0f, 2f, _width / 2, _height/ 4,  boats[0].getWidth() ,  boats[0].getHeight());
		//islands[0] = createBox(islandsTextures[0].getWidth(), islandsTextures[0].getHeight(), true , new Vector2(300,300));
		//enemyShips = new Ship(boats[random], 10, _width / 2, _height* 3/ 4, 20, 40);
		map = new TmxMapLoader().load("models/map.tmx");
		tmr = new OrthoCachedTiledMapRenderer(map);

		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collider").getObjects());
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) { //yay c# less goooooo (i changed it to deltaTime cuz im used to it being that from c#)
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(.1f,.36f,.70f,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




		tmr.render();

		batch.begin();
		//player
		batch.draw(playerShips.getBoatTexture(), playerShips.getEntityBody().getPosition().x * PixelPerMeter - (playerShips.getBoatTexture().getWidth()/2), playerShips.getEntityBody().getPosition().y * PixelPerMeter - (playerShips.getBoatTexture().getHeight()/2));
		//batch.draw(islandsTextures[0], islands[0].getPosition().x * PixelPerMeter - (islandsTextures[0].getWidth()/2), islands[0].getPosition().y * PixelPerMeter - (islandsTextures[0].getHeight()/2));
		//enemyShips.draw(batch);

		batch.end();

		renderer.render(world, camera.combined.scl(PixelPerMeter));



	}

	public void update(float deltaTime){
		world.step(1/ 60f, 6,2);
		updateCamera();
		inputUpdate(deltaTime);
		processInput();
		handleDirft();
		tmr.setView(camera);
		batch.setProjectionMatrix(camera.combined);
	}

	public void inputUpdate(float deltaTime){
		//int xForce = 0;
		//int yForce = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) | Gdx.input.isKeyPressed(Input.Keys.A)){
			 playerShips.setTurnDirection(2);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) | Gdx.input.isKeyPressed(Input.Keys.D)){
			playerShips.setTurnDirection(1);
		}
		else{
			playerShips.setTurnDirection(0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.UP) | Gdx.input.isKeyPressed(Input.Keys.W)){
			playerShips.setDriveDirection(1);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) | Gdx.input.isKeyPressed(Input.Keys.S)){
			playerShips.setDriveDirection(2);
		}
		else{
			playerShips.setDriveDirection(0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.P)){
			System.out.println("current ship position is x = " + playerShips.getEntityBody().getPosition().x + " and y = " + playerShips.getEntityBody().getPosition().y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.C)){
			if(cameraState == 0) cameraState = 1;
			else if(cameraState == 1) cameraState = 0;
			else if(cameraState == -1) cameraState = 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.X)){
			if(cameraState == 0) cameraState = -1;
			else if (cameraState == 1) cameraState = -1;
			else if (cameraState == -1) cameraState = 0;
		}

		//playerShips.getEntityBody().setLinearVelocity(new Vector2(xForce * playerShips.getMovementSpeed(),yForce * playerShips.getMovementSpeed()));
	}

	private void processInput() {
		Vector2 baseVector = new Vector2(0,0);

		if(playerShips.getTurnDirection() == 1){
			playerShips.getEntityBody().setAngularVelocity(-playerShips.getTurnSpeed());
		} else if(playerShips.getTurnDirection() == 2){
			playerShips.getEntityBody().setAngularVelocity(playerShips.getTurnSpeed());
		} else if(playerShips.getTurnDirection() == 0 && playerShips.getEntityBody().getAngularVelocity() != 0){
			playerShips.getEntityBody().setAngularVelocity(0);
		}

		if(playerShips.getDriveDirection() == 1){
			baseVector.set(0, playerShips.getMovementSpeed());
		} else if(playerShips.getDriveDirection() == 2){
			baseVector.set(0, -playerShips.getMovementSpeed());
		}

		if(!baseVector.isZero() && (playerShips.getEntityBody().getLinearVelocity().len() < playerShips.getMaxSpeed())){
			playerShips.getEntityBody().applyForceToCenter(playerShips.getEntityBody().getWorldVector(baseVector), true);
		}
	}

	private void handleDirft(){
		Vector2 forwardSpeed = playerShips.getForwardVelocity();
		Vector2 lateralSpeed = playerShips.getLateralVelocity();

		playerShips.getEntityBody().setLinearVelocity(forwardSpeed.x + lateralSpeed.x * playerShips.getDriftFactor(), forwardSpeed.y + lateralSpeed.y * playerShips.getDriftFactor());
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width/2 , height/2);
		//viewport.update(width,height, true);
		//batch.setProjectionMatrix(camera.combined);
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
		renderer.dispose();
		world.dispose();
		tmr.dispose();
		batch.dispose();
		map.dispose();
	}

	public void updateCamera(){
		if(cameraState == 0){
			CameraManager.lerpOn(camera, playerShips.getEntityBody().getPosition(), 0.1f);
		}
		if(cameraState == -1){
			CameraManager.lockOn(camera, playerShips.getEntityBody().getPosition());
		}
	}

	public Body createBox(int width, int height, boolean isStatic, Vector2 position){
		Body body;
		BodyDef def = new BodyDef();

		if(isStatic) def.type = BodyDef.BodyType.StaticBody;
		else def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(position);
		def.fixedRotation = true;
		body = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width/2) / PixelPerMeter, (height/2)/ PixelPerMeter);
		body.createFixture(shape, 1f);
		shape.dispose();

		return  body;
	}
}