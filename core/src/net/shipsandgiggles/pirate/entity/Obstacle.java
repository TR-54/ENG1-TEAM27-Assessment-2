package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;


public class Obstacle {
    public static final float frame_length = 0.2f;
    public static final int size = 32;

    private static Animation<?> anim = null;
    float statetime;
    public boolean remove = false;
    public float randX;
    public float randY;
    public Vector2 position;
    Rectangle hitbox;
    public boolean damagedPlayer = false;

    public Obstacle(Ship player, PowerUp pUp, PowerUp pUp2, PowerUp pUp3, PowerUp pUp4, PowerUp pUp5){
        do{
            randX = (int) Math.floor(Math.random()*(1530-350+1)+350);
            randY = (int) Math.floor(Math.random()*(710-350+1)+350);
            this.hitbox = new Rectangle(randX ,randY , size,size);
        }
        while(this.hitbox.overlaps(pUp.hitbox) || this.hitbox.overlaps(pUp2.hitbox) || this.hitbox.overlaps(pUp3.hitbox) || this.hitbox.overlaps(pUp4.hitbox) || this.hitbox.overlaps(pUp5.hitbox) || player.hitBox.overlaps(this.hitbox));

        this.position = new Vector2(randX, randY);
        statetime = 0;


        if(anim == null){
            anim = new Animation(frame_length, TextureRegion.split(new Texture("models/BarrelFloat.png"), size, size)[0]);
        }
    }

    public void update(Ship player, float deltatime){
        statetime += deltatime;
        if(anim.isAnimationFinished(statetime)){
            statetime = 0;
        }
        if(detectHit(player)){
            if(!damagedPlayer){
                player.takeDamage(20f);
                damagedPlayer = true;
            }
            remove = true;
        }
    }

    public void render(Batch batch){
        batch.begin();
        batch.draw((TextureRegion) anim.getKeyFrame(statetime), position.x, position.y);
        batch.end();
    }

    public boolean detectHit(Ship player){
        if (player.hitBox.overlaps(this.hitbox)){
            return true;
        }else{
            return false;
        }
    }
}
