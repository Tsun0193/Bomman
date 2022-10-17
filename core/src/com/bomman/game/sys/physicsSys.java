package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.bomman.game.components.Enemy;
import com.bomman.game.components.rigidBody;
import com.bomman.game.components.transform;

public class physicsSys extends IteratingSystem {
    protected ComponentMapper<transform> mTransform;
    protected ComponentMapper<rigidBody> mRigidBody;
    protected ComponentMapper<Enemy> mEnemy;

    public physicsSys() {
        super(Aspect.all(transform.class, rigidBody.class));
    }

    @Override
    protected void process(int entityId) {
        transform Transform = mTransform.get(entityId);
        rigidBody rigidBody = mRigidBody.get(entityId);

        Transform.setPos(rigidBody.body.getPosition());
        Enemy enemy = mEnemy.get(entityId);
        if (enemy != null) {
            if (enemy.type.startsWith("boss")) {
                Transform.temp = -1;
            }
        }
    }
}
