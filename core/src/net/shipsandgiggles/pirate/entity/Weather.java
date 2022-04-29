package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Weather {

    Sprite cloud = new Sprite(new Texture(Gdx.files.internal("models/Cloud.png")));

    public static final float frame_length = 0.2f;
    public static final int offset = 4;
    public static final int size = 64;

    int maxX = 1830;
    int minX = 50;
    int maxY = 1010;
    int minY = 50;
    private static Animation<?> anim = null;
    float statetime;
    float damageTimer;
    public boolean remove = false;
    public float randX;
    public float randY;
    public Vector2 position;
    Rectangle hitbox;
    public float targetY;
    public float targetX;
    public boolean newTargetSet = false;

    public Weather(Ship player){
        do{
            randX = (int) Math.floor(Math.random()*(maxX-minX+1)+minX);
            randY = (int) Math.floor(Math.random()*(maxY-minY+1)+minY);
            this.hitbox = new Rectangle(randX - offset ,randY - offset , 64,64);
        }
        while(player.hitBox.overlaps(this.hitbox));

        do{
            targetX = (int) Math.floor(Math.random()*(maxX-minX+1)+minX);
            targetY = (int) Math.floor(Math.random()*(maxY-minY+1)+minY);
        }
        while(  (targetX < 300 && (targetY > 700 || targetY < 300)) ||
               (targetX > 1500 && (targetY > 700 || targetY < 300)));

        this.position = new Vector2(randX - offset, randY - offset);
        statetime = 0;

        if(anim == null){
            anim = new Animation(frame_length, TextureRegion.split(new Texture("models/Rain.png"), size, size)[0]);
        }
    }

    public void update(Ship player, float deltatime){
        statetime += deltatime;
        damageTimer += deltatime;
        if(anim.isAnimationFinished(statetime)){
            statetime = 0;
        }
        if(detectHit(player) && damageTimer >= 0.5f){
            player.takeDamage(5f);
            damageTimer = 0;
        }
        if(Math.sqrt((this.position.y - targetY) * (this.position.y - targetY) + (this.position.x - targetX) * (this.position.x - targetX)) <= 100){
            do{
                targetX = (int) Math.floor(Math.random()*(maxX-minX+1)+minX);
                targetY = (int) Math.floor(Math.random()*(maxY-minY+1)+minY);
            }
            while(  (targetX < 300 && (targetY > 800 || targetY < 300)) ||
                    (targetX > 1600 && (targetY > 800 || targetY < 300)));
        }
        this.position.x = this.position.x + (targetX - this.position.x) * 0.001f;
        this.position.y = this.position.y + (targetY - this.position.y) * 0.001f;
        this.hitbox = new Rectangle(this.position.x - offset ,this.position.y - offset , 64,64);
    }

    public void render(Batch batch){
        batch.begin();
        batch.draw((TextureRegion) anim.getKeyFrame(statetime), this.position.x, this.position.y);
        batch.draw(cloud, this.position.x, this.position.y + size);
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
