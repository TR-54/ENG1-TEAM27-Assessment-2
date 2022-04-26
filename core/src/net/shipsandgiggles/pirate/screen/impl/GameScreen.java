package net.shipsandgiggles.pirate.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.*;
import net.shipsandgiggles.pirate.listener.WorldContactListener;
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;
import net.shipsandgiggles.pirate.entity.impl.college.GoodrickCollege;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;

import java.util.ArrayList;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;





public class GameScreen implements Screen {

	/** main game screen*/

	static final int unpaused = 0;
	static final int paused = 1;
	int pauseState;

	PowerUp shield;
	PowerUp enemyFreeze;
	PowerUp speedBoost;
	PowerUp damageBoost;
	PowerUp reduceShootingCooldown;

	public LangwithCollege langwith;
	public ConstantineCollege constantine;
	public AlcuinCollege alcuin;
	public GoodrickCollege goodrick;
	public Sprite collegeSprite;
	public static float collegesKilled = 0;

	public static HUDmanager hud;
	public DeathScreen deathScreen;
	public PauseScreen pauseScreen;

	public static ArrayList<ExplosionController> Explosions = new ArrayList<ExplosionController>();

	/** implement world*/
	public static World world;
	private final int _height = Gdx.graphics.getHeight();
	private final int _width = Gdx.graphics.getWidth();
	private final Ship playerShips;
	/** camera work*/
	private final OrthographicCamera camera;
	private final float Scale = 2;
	/**graphics */
	private final SpriteBatch batch; /**batch of images "objects" */
	public Sprite playerModel;

	private final Texture[] boats;
	private final Box2DDebugRenderer renderer;
	private final OrthoCachedTiledMapRenderer tmr;
	private final TiledMap map;
	float recordedSpeed = 0;
	int cameraState = 0;
	public Sprite water;
	public boolean intro = false;
	public float zoomedAmount = 0;
	static EntityAi bob;
	EntityAi player;
	public static int collegesCaptured = 0;

	Sprite cannonBall;

	Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));


	public GameScreen() {

		/** initialization of everything*/

		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), false);
		boats = new Texture[3];
		boats[0] = new Texture(Gdx.files.internal("models/ship1.png"));
		boats[1] = new Texture(Gdx.files.internal("models/ship2.png"));
		boats[2] = new Texture(Gdx.files.internal("models/ship3.png"));
		collegeSprite = new Sprite(new Texture("models/castle.png"));

		cannonBall = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
		water = new Sprite(new Texture(Gdx.files.internal("models/water.jpg")));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, _width / Scale, _height / Scale);
		batch = new SpriteBatch();

		world.setContactListener(new WorldContactListener());
		camera.zoom = 2;



		/** objects setup*/



		playerModel = new Sprite(new Texture(Gdx.files.internal("models/ship1.png")));

		playerShips = new Ship(playerModel, 40000f, 100f, 0.3f, 2f, new Location(_width / 2f, _height / 2f), playerModel.getHeight(), playerModel.getWidth(), camera);


		playerShips.setTexture(playerModel);


		/** map initialization */
		map = new TmxMapLoader().load("models/map.tmx");
		tmr = new OrthoCachedTiledMapRenderer(map);

		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collider").getObjects());

		/** creates damping to player */
		playerShips.getEntityBody().setLinearDamping(0.5f);

		/** enemy creation "bob" and Entity ai controller*/
		Body body = createEnemy((int)bobsSprite.getWidth(), (int)bobsSprite.getHeight(), false, new Vector2(_width / 3f, _height / 6f));
		bob = new EntityAi(body, 300f, bobsSprite, world);
		bob.setTarget(playerShips.getEntityBody());

		player = new EntityAi(playerShips.getEntityBody(), 3);
		Steerable<Vector2> pp = player;


		/** status of entity ai */
		Arrive<Vector2> arrives = new Arrive<Vector2>(bob, pp)
				.setTimeToTarget(0.01f)
				.setArrivalTolerance(175f)
				.setDecelerationRadius(50);
		bob.setBehavior(arrives);

		/** set up college*/
		if (GameDifficultyScreen.getDifficulty() == 0){
			langwith = new LangwithCollege(collegeSprite, new Location(150f,151f), 200f, world);
			goodrick = new GoodrickCollege(collegeSprite, new Location(150f,975f), 200f, world);
			alcuin = new AlcuinCollege(collegeSprite, new Location(1750f,151f), 200f, world);
			constantine = new ConstantineCollege(collegeSprite, new Location(1750f,975f), 200f, world);
		}
		else if (GameDifficultyScreen.getDifficulty() == 1){
			langwith = new LangwithCollege(collegeSprite, new Location(150f,151f), 300f, world);
			goodrick = new GoodrickCollege(collegeSprite, new Location(150f,975f), 300f, world);
			alcuin = new AlcuinCollege(collegeSprite, new Location(1750f,151f), 300f, world);
			constantine = new ConstantineCollege(collegeSprite, new Location(1750f,975f), 300f, world);
		}
		else{
			langwith = new LangwithCollege(collegeSprite, new Location(150f,151f), 400f, world);
			goodrick = new GoodrickCollege(collegeSprite, new Location(150f,975f), 400f, world);
			alcuin = new AlcuinCollege(collegeSprite, new Location(1750f,151f), 400f, world);
			constantine = new ConstantineCollege(collegeSprite, new Location(1750f,975f), 400f, world);
		}

		/** set up powerups*/
		shield = new PowerUp(PowerUp.Type.SHIELD, 999, world);
		enemyFreeze = new PowerUp(PowerUp.Type.ENEMYFREEZE, 999, world);
		speedBoost = new PowerUp(PowerUp.Type.SPEEDBOOST, 999, world);
		damageBoost = new PowerUp(PowerUp.Type.DAMAGEBOOST, 999, world);
		reduceShootingCooldown = new PowerUp(PowerUp.Type.REDUCESHOOTCOOLDOWN, 999, world);

		hud = new HUDmanager(batch);
		deathScreen = new DeathScreen(batch);
		pauseState = unpaused;

		pauseScreen = new PauseScreen(batch);
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) {
		/** zoom controller for the intro*/
		if(!intro){
			camera.zoom -= 0.02f;
			zoomedAmount += 0.02;
			if(zoomedAmount >= 1){
				intro = true;
			}
		}


		/** does the update method*/
		update();
		/** colour creation for background*/
		Gdx.gl.glClearColor(.98f, .91f, .761f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		/** draws the water manually to use less resources*/
		water.draw(batch);
		batch.end();

		tmr.render();
		BallsManager.updateBalls(batch);

		/** setting ship position for the sprite of the player ship*/
		playerShips.getSprite().setPosition(playerShips.getEntityBody().getPosition().x * PIXEL_PER_METER - (playerShips.getSkin().getWidth() / 2f), playerShips.getEntityBody().getPosition().y * PIXEL_PER_METER - (playerShips.getSkin().getHeight() / 2f));
		playerShips.getSprite().setRotation((float) Math.toDegrees(playerShips.getEntityBody().getAngle()));

		/** update all the colleges and entities*/
		playerShips.draw(batch);
		langwith.draw(batch);
		constantine.draw(batch);
		goodrick.draw(batch);
		alcuin.draw(batch);

		shield.update(deltaTime, batch, playerShips);
		enemyFreeze.update(deltaTime, batch, playerShips);
		speedBoost.update(deltaTime, batch, playerShips);
		damageBoost.update(deltaTime, batch, playerShips);
		reduceShootingCooldown.update(deltaTime, batch, playerShips);

		if (pauseState == unpaused) { //only shoots player if game is unpaused

			//cannon balls will still move during pause but will not pause damage
			langwith.shootPlayer(playerShips);
			constantine.shootPlayer(playerShips);
			goodrick.shootPlayer(playerShips);
			alcuin.shootPlayer(playerShips);
			if(bob.getFrozen() == false){
				bob.shootPlayer(playerShips);
			}
		}

		//renderer.render(world, camera.combined.scl(PIXEL_PER_METER));                                             Render hitboxes
		bob.update(deltaTime, batch);

		if(bob.isDead()){
			//world.destroyBody(body);
			bob.setPosition(new Vector2(10000,10000));
			Body body = createEnemy((int)bobsSprite.getWidth(), (int)bobsSprite.getHeight(), false, new Vector2(_width / 3f, _height / 6f));
			bob = new EntityAi(body, 300f, bobsSprite, world);
			bob.setTarget(playerShips.getEntityBody());
			Steerable<Vector2> pp = player;
			Arrive<Vector2> arrives = new Arrive<Vector2>(bob, pp)
					.setTimeToTarget(0.01f)
					.setArrivalTolerance(175f)
					.setDecelerationRadius(50);
			bob.setBehavior(arrives);
			bob.setDead(false);
		}

		if(bob.getFrozen() == false){
			Steerable<Vector2> pp = player;
			Arrive<Vector2> arrives = new Arrive<Vector2>(bob, pp)
					.setTimeToTarget(0.01f)
					.setArrivalTolerance(175f)
					.setDecelerationRadius(50);
			bob.setBehavior(arrives);
		}

		/** update for the explosion*/
		updateExplosions();


		/** change of ui incase of victory or death or normal hud*/
		if(playerShips.dead){
			deathScreen.update(hud, 0);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			return;
		}
		if(collegesCaptured == 4){
			deathScreen.update(hud, 1);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			return;
		}
		if(collegesKilled == 4){
			deathScreen.update(hud, 2);
			batch.setProjectionMatrix(deathScreen.stage.getCamera().combined);
			deathScreen.stage.draw();
			return;
		}
		batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		if (pauseState != paused){
			hud.updateLabels(batch);
		}

		if (pauseState == paused){
			//batch.setProjectionMatrix(pauseScreen.stage.getCamera().combined);
			pauseScreen.stage.draw();
		}
	}

	/** updating the explosions*/
	private void updateExplosions() {
		ArrayList<ExplosionController> removeExplosion = new ArrayList<ExplosionController>();
		for(ExplosionController explosion : Explosions){
			explosion.update();

			explosion.draw(batch);
			if(explosion.remove)removeExplosion.add(explosion);
		}
		Explosions.removeAll(removeExplosion);
	}

	public void update() {
		if (pauseState == unpaused) {
			world.step(1 / 60f, 6, 2);
			updateCamera();
			inputUpdate();
			processInput();
			handleDirft();
			tmr.setView(camera);
			batch.setProjectionMatrix(camera.combined);
			playerShips.updateShots(world, cannonBall, camera, Configuration.Cat_Player, (short)(Configuration.Cat_Enemy | Configuration.Cat_College), (short) 0);
		}

		if (pauseState == paused){

			updateCamera();
			tmr.setView(camera);
			batch.setProjectionMatrix(camera.combined);


			/** allow player to unpause**/
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				pauseState = unpaused;
			}
		}

	}

	public void inputUpdate() {
		/** checking for inputs*/
		if (playerShips.dead) return;
		if (playerShips.getEntityBody().getLinearVelocity().len() > 20f) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) | Gdx.input.isKeyPressed(Input.Keys.A)) {
				playerShips.setTurnDirection(2);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) | Gdx.input.isKeyPressed(Input.Keys.D)) {
				playerShips.setTurnDirection(1);
			} else {
				playerShips.setTurnDirection(0);
			}
		}


		if (Gdx.input.isKeyPressed(Input.Keys.UP) | Gdx.input.isKeyPressed(Input.Keys.W)) {
			playerShips.setDriveDirection(1);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) | Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerShips.setDriveDirection(2);
		} else {
			playerShips.setDriveDirection(0);
		}


		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			System.out.println(playerShips.getEntityBody().getPosition());
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			if (cameraState == 0) cameraState = 1;
			else if (cameraState == 1) cameraState = 0;
			else if (cameraState == -1) cameraState = 1;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			if (cameraState == 0) cameraState = -1;
			else if (cameraState == 1) cameraState = -1;
			else if (cameraState == -1) cameraState = 0;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			if(cameraState == 5){
				cameraState = 0;
			}
			else{
				cameraState = 5;
			}

		}
		/** creating zooming */
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			if(camera.zoom < 2)camera.zoom += 0.02f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			if(camera.zoom > 1)camera.zoom -= 0.02f;
		}

		/** Press ESC to activate Pause or Unpause**/
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			pause();

		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && Currency.get().take(Currency.Type.GOLD, 100)){
			Ship.changeMaxHealth(Ship.getMaxHealth() + 50f);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E) && Currency.get().take(Currency.Type.GOLD, 50)){
			Ship.setHealth(Ship.getMaxHealth());
		}

	}

	private void processInput() {
		/** processing the input created*/
		if (playerShips.dead) return;
		Vector2 baseVector = new Vector2(0, 0);

		/** apllying liner velocity to the player based on input*/
		float turnPercentage = 0;
		if (playerShips.getEntityBody().getLinearVelocity().len() < (playerShips.getMaximumSpeed() / 2)) {
			turnPercentage = playerShips.getEntityBody().getLinearVelocity().len() / (playerShips.getMaximumSpeed());
		} else {
			turnPercentage = 1;
		}

		float currentTurnSpeed = playerShips.getTurnSpeed() * turnPercentage;


		/** applying angular velocity to the player based on input*/
		if (playerShips.getTurnDirection() == 1) {
			playerShips.getEntityBody().setAngularVelocity(-currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 2) {
			playerShips.getEntityBody().setAngularVelocity(currentTurnSpeed);
		} else if (playerShips.getTurnDirection() == 0 && playerShips.getEntityBody().getAngularVelocity() != 0) {
			playerShips.getEntityBody().setAngularVelocity(0);
		}

		/** applies speed to the player based on input*/
		if (playerShips.getDriveDirection() == 1) {
			baseVector.set(0, playerShips.getSpeed());
		} else if (playerShips.getDriveDirection() == 2) {
			baseVector.set(0, -playerShips.getSpeed() * 4 / 5);
		}
		if (playerShips.getEntityBody().getLinearVelocity().len() > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			playerShips.getEntityBody().setLinearDamping(1.75f);
		} else {
			playerShips.getEntityBody().setLinearDamping(0.5f);
		}
		//recordedSpeed = playerShips.getEntityBody().getLinearVelocity().len();
		if (playerShips.getEntityBody().getLinearVelocity().len() > playerShips.getMaximumSpeed() / 3f) {
			playerShips.setSpeed(playerShips.getOriginalSpeed() * 2);
		} else {
			playerShips.setSpeed(playerShips.getOriginalSpeed());
		}
		if (!baseVector.isZero() && (playerShips.getEntityBody().getLinearVelocity().len() < playerShips.getMaximumSpeed())) {
			playerShips.getEntityBody().applyForceToCenter(playerShips.getEntityBody().getWorldVector(baseVector), true);
		}
	}

	private void handleDirft() {
		/** handles drifts of the boat */
		Vector2 forwardSpeed = playerShips.getForwardVelocity();
		Vector2 lateralSpeed = playerShips.getLateralVelocity();

		playerShips.getEntityBody().setLinearVelocity(forwardSpeed.x + lateralSpeed.x * playerShips.getDriftFactor(), forwardSpeed.y + lateralSpeed.y * playerShips.getDriftFactor());
	}

	@Override
	public void resize(int width, int height) {
		/** resize function*/
		camera.setToOrtho(false, width / 2, height / 2);
		//viewport.update(width,height, true);
		//batch.setProjectionMatrix(camera.combined);
	}

	public static EntityAi getEnemy(){
		return bob;
	}

	@Override
	public void pause() {
		pauseState = paused;
	}

	@Override
	public void resume() {
		pauseState = unpaused;
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		/** disposing of everything*/
		renderer.dispose();
		world.dispose();
		tmr.dispose();
		batch.dispose();
		map.dispose();
	}

	public void updateCamera() {
		/** updating camera based on states*/
		if(playerShips.dead) {
			if(camera.zoom < 2)camera.zoom += 0.005f;
			CameraManager.lerpOn(camera, playerShips.deathPosition, 0.1f);
			return;
		}
		if (cameraState == 0) {
			CameraManager.lerpOn(camera, playerShips.getEntityBody().getPosition(), 0.1f);
		}
		if (cameraState == -1) {
			CameraManager.lockOn(camera, playerShips.getEntityBody().getPosition());
		}
		if (cameraState == 5) {
			CameraManager.lerpOn(camera, bob.getBody().getPosition(), 0.1f);
		}
	}

	public Body createEnemy(int width, int height, boolean isStatic, Vector2 position) {
		Body body;
		BodyDef def = new BodyDef();

		if (isStatic) def.type = BodyDef.BodyType.StaticBody;
		else def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(position);

		def.fixedRotation = true;
		body = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((width / 2f) / PIXEL_PER_METER, (height / 2f) / PIXEL_PER_METER);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef. density = 1f;
		fixtureDef.filter.categoryBits = Configuration.Cat_Enemy;

		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

		return body;
	}

	public static World getWorld(){
		return world;
	}
	public static void add(Vector2 pp){
		Explosions.add(new ExplosionController(pp));
	}

	public static void collegeKilled(){
		/** adds for each college killed to count for victory*/
		collegesKilled ++;
		collegesCaptured--;
	}
	/** adds college captured to check for victory*/
	public static void collegeCaptured(){collegesCaptured ++;}
}