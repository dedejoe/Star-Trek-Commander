import java.util.LinkedList; //<>// //<>//
import java.util.Iterator;

//This is the class for the main ship
class Enterprise extends Ship {

  Enterprise(float startX, float startY, float startRot, PImage startImage, float startScale, float startSpeed, float startTurningSpeed, int newTeam) {

    // initiate all shield values to full 100
    shield = new float[360];
    shieldFlash = new float[360];
    for (int i = 0; i < 360; i++) {
      shield[i] = 255f;
      shieldFlash[i] = 1f;
    }
    x = startX;
    y = startY;
    rotation = startRot;
    image = startImage;
    scale = startScale;
    mass = 1000;
    speed = startSpeed;
    turningSpeed = startTurningSpeed;
    powered = true;
    projectiles = new LinkedList<Projectile>();
    weapons = new LinkedList<Weapon>();
    boundingEllipses = new LinkedList<Ellipse>();
    shieldDisplacementX = 0.95f;
    shieldDisplacementY = 1f;
    team = newTeam;
    boundingEllipses.add(new Ellipse(0, 55, 80, 110));
    boundingEllipses.add(new Ellipse(0, -20, 25, 60));
    boundingEllipses.add(new Ellipse(-30, -50, 15, 105));
    boundingEllipses.add(new Ellipse(28, -50, 15, 105));
    shieldEllipse = new Ellipse(0, 0, (int)(shipWidth() * 1.75 * shieldDisplacementX), (int)(shipHeight() * 1.25 * shieldDisplacementY));
  }
}

class Ship extends Object {

  boolean targetingEnterprise = false;

  int id;
  int shieldBalanceTimer = 60;
  int shieldBalanceQuadrant = -1;
  float[] shield;
  float[] shieldFlash;
  float previousRotation = 0f;
  float myLastRotation;
  boolean changedDirection = false;
  float scale;
  int rotationTimer = 5;
  float shieldDisplacementX;
  float shieldDisplacementY;
  float speed;
  float turningSpeed;
  float radius = 50f;
  PImage image;
  int team;
  LinkedList<Ellipse> boundingEllipses;
  float hullIntegrity = 100;
  Ellipse shieldEllipse = new Ellipse(0, 0, 0, 0);
  LinkedList<Node> nodes;
  LinkedList<Weapon> weapons;
  LinkedList<Projectile> projectiles;

  Ship() {
    projectiles = new LinkedList<Projectile>();
  }

  Ship(float startX, float startY, float startRot, PImage startImage, float startScale, float startSpeed, float startTurningSpeed, int newTeam) {

    // initiate all shield values to full 100 
    shield = new float[360];
    shieldFlash = new float[360];
    for (int i = 0; i < 360; i++) {
      shield[i] = 255f;
      shieldFlash[i] = 1f;
    }
    x = startX;
    y = startY;
    rotation = startRot;
    image = startImage;
    scale = startScale;
    mass = 1000;
    speed = startSpeed;
    turningSpeed = startTurningSpeed;
    powered = true;
    projectiles = new LinkedList<Projectile>();
    weapons = new LinkedList<Weapon>();
    nodes = new LinkedList<Node>();
    boundingEllipses = new LinkedList<Ellipse>();
    shieldDisplacementX = 0.8;
    shieldDisplacementY = 1;
    team = newTeam;

    boundingEllipses.add(new Ellipse(-3, -50, 20, 60));
    boundingEllipses.add(new Ellipse(-55, 10, 15, 60));
    boundingEllipses.add(new Ellipse(0, -10, 110, 80));
    boundingEllipses.add(new Ellipse(55, 10, 15, 60));
    boundingEllipses.add(new Ellipse(2, 50, 23, 60));
    shieldEllipse = new Ellipse(0, 0, (int)(shieldRadiusX() * 2), (int)(shieldRadiusY() * 2));
  }

  void runAI() {


    float desiredAngle = 0f;
    float newSpeed = speed;
    float angle = normalizeAngle(atan2(enterprise.y - y, enterprise.x - x) * 180 / PI - 90) + 360;    

    float rot = rotation + 360;

    // fire weapons and slow down if in range to fire
    Iterator<Ship> iterator = ships.iterator();
    while (iterator.hasNext()) {
      Ship tempShip = iterator.next();
      if (tempShip.team == team || team == 0)
        continue;
      //text((int)abs((rotation + 360) - (angle)) + ", " + (int)rotation + ", " + (int)angle, 50, 50);
      if (abs((rotation + 360) - (angle)) < 30f && sqrt(sq(enterprise.x - x) + sq(enterprise.y - y)) < 900f) {
        if (difficultyHard) {
          newSpeed *= 0.75;
        }
        Iterator<Weapon> weaponIterator = weapons.iterator();
        while (weaponIterator.hasNext()) {
          Weapon tmpWeapon = weaponIterator.next();
          if (tmpWeapon.canFire(true)) {
            tmpWeapon.fire();
          }
        }
      }
    }

    // move unit according to rotation
    x += newSpeed * cos((rotation + 90) * PI / 180);
    y += newSpeed * sin((rotation + 90)* PI / 180);
    //rotation += turningSpeed;

    // determine if we are on attack run or not
    if (!targetingEnterprise) {
      if (sqrt(sq(enterprise.x - x) + sq(enterprise.y - y)) > 800f) {
        targetingEnterprise = true;
      }
    } else {
      if (sqrt(sq(enterprise.x - x) + sq(enterprise.y - y)) < 200f) {
        targetingEnterprise = false;
      }
    }

    // This code block is to deal with the awkward 0 to 360 jump
    if (abs(previousRotation - angle + desiredAngle) > 180f) {
      changedDirection = true;
    }

    //adjust angle towards the enterprise if on an attack run
    if (targetingEnterprise) {
      if (!changedDirection) {
        if (rot > angle + desiredAngle) {
          if (rot - turningSpeed < angle) rot = angle;
          else rotation -= turningSpeed;
          myLastRotation = -turningSpeed;
        } else if (rot < angle + desiredAngle) {

          if (rot + turningSpeed > angle) rot = angle;
          else rotation += turningSpeed;
          myLastRotation = turningSpeed;
        }
      } else {
        rotation += myLastRotation;
        if (rotationTimer == 0) {      
          changedDirection = false;
          rotationTimer = 5;
        } else rotationTimer--;
      }
    } else {
      rotation += previousRotation * 0.001f;
    }

    // auto balance shields on hard

    if (difficultyHard) {
      if (shieldAverage(180, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 3; //<>//
        shieldBalanceTimer = 240;
      } else if (shieldAverage(0, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 2; //<>//
        shieldBalanceTimer = 240;
      } else if (shieldAverage(270, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 1;
        shieldBalanceTimer = 240; //<>//
      } else if (shieldAverage(90, 90) < 80f && shieldBalanceQuadrant == -1) {
        shieldBalanceQuadrant = 0;
        shieldBalanceTimer = 240; //<>//
      }
    }

    previousRotation = angle;
  }

  void update() {

    if (gameOver) return;

    rotation = normalizeAngle(rotation);

    if (team != 0) 
      runAI();

    normalizeShields(shieldBalanceQuadrant, 0.02, 0.02);

    Iterator<Projectile> iterator = projectiles.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().update() == -1) {
        iterator.remove();
      }
    }

    Iterator<Weapon> weaponIterator = weapons.iterator();
    while (weaponIterator.hasNext()) {
      Weapon tmpWeapon = weaponIterator.next();
      tmpWeapon.update();
      if (tmpWeapon.buffered == true && tmpWeapon.canFire(false) ) {
        tmpWeapon.fire();
      }
    }

    if (x < -width) {
      x = width;
    }
    if (x > width) {
      x = -width;
    }
    if (y < -height * 2) {
      y = height * 2;
    }
    if (y > height * 2) {
      y = -height * 2;
    }
  }

  void fire(int whichOne) {

    int counter = 0;
    Iterator<Weapon> iterator = weapons.iterator();
    while (iterator.hasNext()) {
      Weapon tmpWeapon = iterator.next();
      if (counter == whichOne) {
        if (tmpWeapon.canFire(true)) {
          tmpWeapon.fire();
        }
      }
      counter++;
    }
  }

  int closestShieldPoint(float px, float py) {

    int temp = (int)(atan2(px - x, py - y) * 180 / PI + (rotation + 180));

    return normalizeAngle(temp);
  }

  void shieldImpact(float force, int spread, float split, float hitX, float hitY) {
    // force = most damage at center of shot, spread = how much of the shield it affects

    shield_impact.play(1);

    int tmp = closestShieldPoint(hitX, hitY);
    float counter = -spread /2;
    for (int i = tmp - (spread / 2); i < tmp + (spread / 2); i++) {

      int y = i;
      if (i > 359)
        y -= 360;
      else if (i < 0)
        y += 360;

      shieldFlash[y] = 2f;
      shield[y] -= abs(force - (force / 2) * abs(counter)/spread * 0.5) * split;

      if (shield[y] < (255f * (1f - split))) {
        hullIntegrity -= ((force * (1f - split)) / (float)spread) * split * 0.15;
      }

      if (shield[y] < 0) {
        shield[y] = 0;
      }

      counter += 1;
    }

    //int centerShot =
  }

  void shipImpact(float force, float split, float hitX, float hitY) {
    // force = most damage at center of shot, spread = how much of the shield it affects
    hullIntegrity -= (force * (1f - split));

    if (hullIntegrity <= 0f) {
      large_explosion.play(1f);
      Effect effect = new Effect(x + 25, y + 25, explosionImage, 40, 256, 248, 1.5f);
      effects.add(effect);

      hullIntegrity = 0f;

      Iterator<Ship> iterator = ships.iterator();
      while (iterator.hasNext()) {
        Ship tempShip = iterator.next();
        if (tempShip.id == id) {
          iterator.remove();
        }
      }
    } else {
      hull_hit.play(1);

      if (force >= 20) {
        Effect effect = new Effect(hitX, hitY, explosionImage, 40, 256, 248, force / 200f * (1f - split) * 2);
        effects.add(effect);
      }
    }
  }

  //int centerShot =


  float shieldRadiusX() {

    return shipWidth() / 2 * 1.75 * shieldDisplacementX;
  }

  float shieldRadiusY() {

    return shipHeight() / 2 * 1.25 * shieldDisplacementY;
  }

  float getShieldX(int i) {
    float myX = int(shieldRadiusX() * -sin(i * PI/180));
    float myY = int(shieldRadiusY() * cos(i * PI/180));

    return (myX * cos(rotation * PI / 180) + myY * sin(rotation * PI / 180)) + x;
  }

  float getShieldY(int i) {

    float myX = int(shieldRadiusX() * -sin(i * PI/180));
    float myY = int(shieldRadiusY() * cos(i * PI/180));

    return (myX * sin(rotation * PI / 180) - (myY * cos(rotation * PI / 180))) + y;
  }

  float getShieldX(int i, float scalar) {

    float myX = int(scalar * shieldRadiusX() * -sin(i * PI/180));
    float myY = int(scalar * shieldRadiusY() * cos(i * PI/180));

    return (myX * cos(rotation * PI / 180) + myY * sin(rotation * PI / 180)) + x;
  }

  float getShieldY(int i, float scalar) {
    float myX = int(scalar * shieldRadiusX() * -sin(i * PI/180));
    float myY = int(scalar * shieldRadiusY() * cos(i * PI/180));

    return (myX * sin(rotation * PI / 180) - (myY * cos(rotation * PI / 180))) + y;
  }

  void draw() {

    Iterator<Projectile> iterator = projectiles.iterator();
    while (iterator.hasNext()) {
      iterator.next().draw();
    }

    noFill();
    strokeWeight(5);
    smooth();

    pushMatrix();
    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    //ellipse(aimEllipse.x, aimEllipse.y, aimEllipse.w, aimEllipse.h);

    for (int i = 0; i < 36; i++) {
      //Change stroke of curve to shield strength
      if (shieldFlash[i * 10] > 1f) {
        stroke(shieldAverage((i * 10), 10) * 0.2, shieldAverage((i * 10), 10) * 0.2, 255, shieldAverage((i * 10), 10) * shieldFlash[i * 10]);
        shieldFlash[i * 10] -= 0.1f;
      } else
        stroke(0, 0, shieldAverage((i * 10), 10) * 0.5);

      // draw first layer of shield
      curve(getShieldX(i * 10 - 10) - x, getShieldY(i * 10 - 10) - y, getShieldX(i * 10) - x, getShieldY(i * 10) - y, 
        getShieldX(i * 10 + 10) - x, getShieldY(i * 10 + 10) - y, getShieldX(i * 10 + 20) - x, getShieldY(i * 10 + 20) - y);
    }

    // draw ship image
    rotate(rotation * PI / 180);

    image(image, 0, 0, shipWidth(), shipHeight());

    /*  draw shields and hull boundingEllipse
     fill(255, 0, 0, 50);
     ellipse(shieldEllipse.x, shieldEllipse.y, shieldEllipse.w, shieldEllipse.h);
     
     fill(#ffffff);
     Iterator<Ellipse> biterator = boundingEllipses.iterator();
     while (biterator.hasNext()) {
     Ellipse t = biterator.next();
     ellipse(t.x, t.y, t.w, t.h);
     }
     */

    popMatrix();
  }

  int shipWidth() {

    return int(image.width * scale);
  }

  int shipHeight() {

    return int(image.height * scale);
  }

  boolean destroyed() {

    return (hullIntegrity <= 0);
  }

  float shieldAverage(int index, int range) {

    float average = 0f;

    for (int l = index - range / 2; l < index + range /  2; l++) {
      int i = l;
      if (l >= 360) {
        i -= 360;
      } else if (l < 0) {
        i += 360;
      }

      average += shield[i];
    }

    return (average / (range));
  }

  void normalizeShields(int quadrant, float rate, float recharge) {

    if (quadrant == -1) {
      float average = 0f;

      for (int i = 0; i < 360; i++) {
        average += shield[i];
        if (shield[i] < 255) {
          if (shieldFlash[i] >= 1f)
            shield[i] += recharge;
        }
      }

      average /= 360f;

      for (int i = 0; i < 360; i++) {
        if (shieldFlash[i] <= 1f) {
          if (shield[i] < average && shield[i] < 255) {
            if (shield[i] + rate > average) {
              shield[i] = average;
            } else
              shield[i] += rate;
          } else if (shield[i] > average) {
            if (shield[i] - rate < average) {
              shield[i] = average;
            } else if (shield[i] > 255f) {
              shield[i] -= rate * 3;
            } else {
              shield[i] -= rate;
            }
          }
        }
      }
    } else {

      shieldBalanceTimer--;

      float power = 0f;
      float factor = 0.2f;

      for (int i = 0; i < 360; i++) {
        if (shield[i] - factor >= 0) {
          power += factor;
          shield[i] -= factor;
        } else {
          power += shield[i];
          shield[i] = 0;
        }
      }

      int start = 0;
      int range = 0;
      if (quadrant == 0) {
        start = 45;  
        range = start + 90;
      } else if (quadrant == 1) {
        start = 225;  
        range = start + 90;
      } else if (quadrant == 3) {
        start = 135;  
        range = start + 90;
      } else {
        start = 315;
        range = 45;
      }


      for (int i = 0; i < 360; i++) {
        if (quadrant == 2) {
          if (i > start || i < range) {
            shield[i] += power / 90f;
          }
        } else {
          if (i > start && i < range) {
            shield[i] += power / 90f;
          }
        }
      }

      if (shieldBalanceTimer <= 0) {
        shieldBalanceQuadrant = -1;
        shieldBalanceTimer = 120;
      }
    }
  }
}
