package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.shipsandgiggles.pirate.HUDmanager;
import net.shipsandgiggles.pirate.listener.WorldContactListener;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

public class PowerUp {
    Sprite shield = new Sprite(new Texture(Gdx.files.internal("models/Shield.png")));
    Sprite enemyFreeze = new Sprite(new Texture(Gdx.files.internal("models/Freeze.png")));
    Sprite rangeBoost = new Sprite(new Texture(Gdx.files.internal("models/Range.png")));
    Sprite damageBoost = new Sprite(new Texture(Gdx.files.internal("models/DamageUp.png")));
    Sprite reduceShootingCooldown = new Sprite(new Texture(Gdx.files.internal("models/SpeedUp.png")));

    float cooldown;
    float timer = 0;
    World world;
    Type type;
    int maxX = 1830;
    int minX = 50;
    int maxY = 1010;
    int minY = 50;
    int randX;
    int randY;
    Rectangle hitbox;

    //range from x: 50-1830 y: 50-1010

    public PowerUp(Type type, float respawnCooldown, World world){
        this.cooldown = respawnCooldown;
        this.world = world;
        this.type = type;

        do{
            randX = (int) Math.floor(Math.random()*(maxX-minX+1)+minX);
            randY = (int) Math.floor(Math.random()*(maxY-minY+1)+minY);
        }
        while(  (randX < 300 && (randY > 800 || randY < 300)) ||
                (randX > 1600 && (randY > 800 || randY < 300)));

         this.hitbox = new Rectangle(randX,randY, 32,32);
    }

    public void update(float delta, Batch batch, Ship player){
        if (timer < cooldown){
            if (this.detectPickup(player)){
                if(this.type == Type.SHIELD){
                    Ship.setShieldStatus(true);
                }
                if(this.type == Type.ENEMYFREEZE){
                    Arrive<Vector2> arrives = new Arrive<Vector2>(GameScreen.getEnemy(), GameScreen.getEnemy())
                            .setTimeToTarget(0.01f)
                            .setArrivalTolerance(175f)
                            .setDecelerationRadius(50);
                    GameScreen.getEnemy().setBehavior(arrives);
                    GameScreen.getEnemy().setFrozen(true);
                }
                if(this.type == Type.RANGEBOOST){
                    CannonBall.setMaxTimer(1.2f);
                }
                if(this.type == Type.DAMAGEBOOST){
                    WorldContactListener.damageMul = 2f;
                }
                if(this.type == Type.REDUCESHOOTCOOLDOWN){
                    Ship.setShootingCoolDown(0.3f);
                }
                timer = cooldown;
            }else{
                this.draw(batch);
            }
        }
        if(timer >= cooldown + 5){
            timer = 0;
            do{
                randX = (int) Math.floor(Math.random()*(maxX-minX+1)+minX);
                randY = (int) Math.floor(Math.random()*(maxY-minY+1)+minY);
            }
            while(  (randX < 300 && (randY > 800 || randY < 300)) ||
                    (randX > 1550 && (randY > 700 || randY < 300)));
            this.hitbox = new Rectangle(randX+5,randY+5, 32,32);

            /** setting back all the powerup stats to normal */
            GameScreen.getEnemy().setFrozen(false);
            CannonBall.setMaxTimer(0.8f);
            Ship.setShootingCoolDown(0.6f);
            WorldContactListener.damageMul = 1f;
        }
        timer += delta;
    }


    public void draw(Batch batch){
        batch.begin();
        if(this.type == Type.SHIELD){
            batch.draw(shield, randX, randY);
        }
        if(this.type == Type.ENEMYFREEZE){
            batch.draw(enemyFreeze, randX, randY);
        }
        if(this.type == Type.RANGEBOOST){
            batch.draw(rangeBoost, randX, randY);
        }
        if(this.type == Type.DAMAGEBOOST){
            batch.draw(damageBoost, randX, randY);
        }
        if(this.type == Type.REDUCESHOOTCOOLDOWN){
            batch.draw(reduceShootingCooldown, randX, randY);
        }
        batch.end();
    }

    public boolean detectPickup(Ship player){
        if (player.hitBox.overlaps(this.hitbox)){
            return true;
        }else{
            return false;
        }
    }

    public enum Type{
        SHIELD,
        ENEMYFREEZE,
        RANGEBOOST,
        DAMAGEBOOST,
        REDUCESHOOTCOOLDOWN;
    }
}
