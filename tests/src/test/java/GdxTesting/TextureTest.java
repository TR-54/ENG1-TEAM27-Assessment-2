package test.java.GdxTesting;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import test.java.GdxTesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetTest {
	
	/*Tests are standardised here, the code is to check that all textures are accessible and
	  present categorised by their place in the game.
	 */
	
	@Test
	public void shipTexturesExist() {
		String[] ships = { "ship1", "ship2", "ship3"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s + ".png").exists());
		}
	}
	
	@Test
	public void powerupTexturesExist() {
		String[] ships = { "Shield", "Freeze", "Range", "DamageUp", "SpeedUp"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s + ".png").exists());
		}
	}
	
	@Test
	public void obstacleTexturesExist() {
		String[] ships = {"BarrelFloat"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s + ".png").exists());
		}
	}
	
	@Test
	public void cannonballTexturesExist() {
		String[] ships = {"cannonBall"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s + ".png").exists());
		}
	}
	
	@Test
	public void statusIconsTexturesExist() {
		String[] ships = {"gold_coin", "burst_icon", "attack_icon", "HealthUp", "Repair", "burst_onCoolDown", "bar", "ShieldBar"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s + ".png").exists());
		}
	}
	
	@Test
	public void mapTexturesExist() {
		String[] ships = {"background.png", "castle.png", "water.jpg"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s).exists());
		}
	}
	
	@Test
	public void audioFilesExist() {
		String[] ships = {"music", "seaSounds", "cannonShot", "buttonPress", "explosion"};
		for (String s : ships) {
			assertTrue("the file " + s + ".mp3 does not exist", Gdx.files.internal("../core/assets/models/" + s + ".mp3").exists());
		}
	}
}
