package test.java.GdxTesting;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import test.java.GdxTesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class TextureTest {

	@Test
	public void shipTexturesExist() {
		String[] ships = { "ship1", "ship2", "ship3"};
		for (String s : ships) {
			assertTrue("the file " + s + ".png does not exist", Gdx.files.internal("../core/assets/models/" + s + ".png").exists());
		}
	}
	
	@Test
	public void basicTest() {
		assertTrue(true);
	}

}
