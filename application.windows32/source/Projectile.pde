// This class handles lasers and other energy blasts //<>// //<>// //<>//
class Projectile {

  float x;
  float y;
  float speed;
  float radius;
  float strength;
  int spread;
  boolean alreadyHit = false;
  boolean noCloseShip = true;
  float rotation;
  float startingX;
  float startingY;
  float length;
  boolean passed = false;
  float split;
  int shipID;
  float impactLevel = 40f;

  Projectile() {
  }

  Projectile(float startX, float startY, float startSpeed, float startRotation, float startRadius, float startStrength, int startSpread, float stSplit, int startShipID) {
    startingX = x = startX;
    startingY = y = startY;
    speed = startSpeed;
    rotation = startRotation;
    radius = startRadius;
    strength = startStrength;
    spread = startSpread;
    shipID = startShipID;
    split = stSplit;
    length = 0;
  }

  int update() {

    // check collision with Ship

    Iterator<Ship> iterator = ships.iterator();
    while (iterator.hasNext()) {
      Ship tempShip = iterator.next();
      if (passed == false && collideWithShield(tempShip)) {
        if (tempShip.shield[tempShip.closestShieldPoint(x, y)] >= impactLevel) {
          tempShip.shieldImpact(strength, spread, split, x, y);
          return -1;
        } else {
          passed = true;
        }
      } else if (collideWithShip(tempShip)) {
        tempShip.shipImpact(strength, split, x, y);
        return -1;
      }
    }

    return move(speed);
  }

  // this method determines if a projectile has collided with a ships shield
  boolean collideWithShield(Ship ship) {

    if (ship.id == shipID)
      return false;    

    if (ship.shieldEllipse.inside(x - ship.x, y - ship.y, ship.rotation)) {
      draw();
      return true;
    }

    return false;
  }

  // this method determines if a projectile has collided with the ships hull
  boolean collideWithShip(Ship ship) {

    if (ship.id == shipID)
      return false;    

    Iterator<Ellipse> eiterator = ship.boundingEllipses.iterator();
    while (eiterator.hasNext()) {
      Ellipse ell = eiterator.next();
      if (ell.inside(x - ship.x, y - ship.y, enterprise.rotation)) {
        return true;
      }
    }

    return false;
  }

  // This method moves the ship and returns -1 if its out of range and should be terminated
  int move(float a) {

    x += a * cos((rotation - 90) * PI / 180);
    y += a * sin((rotation - 90) * PI / 180);

    if (x < (anchor.x - width / 1.2) || x > (anchor.x + width / 1.2) )
      return -1;
    else if (y < (anchor.y - height / 1.2) || y > (anchor.y + height / 1.2) )
      return -1;


    return 0;
  }

  float dirX() {
    return cos((rotation - 90) * PI / 180);
  }

  float dirY() {
    return sin((rotation - 90) * PI / 180);
  }

  void draw() {
    pushMatrix();
    stroke(0, 255, 100, 155);
    strokeWeight(2);
    fill(255, 255, 255, 155);
    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    rotate(rotation * PI / 180);

    triangle(-5, 0, 5, 0, 0, 20);

    arc(0, 2, 10, 10, PI, 2 * PI);

    popMatrix();
  }
}

// an inherited class that determines the yellow energy blast of the enterprise
class Phaser extends Projectile {
  float finalLength = 600;
  boolean firstContact = true;
  boolean hullFirstContact = true;
  boolean latchedOn;
  float latchedX;
  float latchedY;
  Ship latchedShip;

  Phaser(float startX, float startY, float startSpeed, float startRotation, float startRadius, float startStrength, int startSpread, float stSplit, int startShipID) {
    startingX = x = startX;
    startingY = y = startY;
    speed = startSpeed;
    rotation = startRotation;
    radius = startRadius;
    strength = startStrength;
    spread = startSpread;
    shipID = startShipID;
    split = stSplit;
    radius = finalLength;
    length = 5;
    radius = 0;
  }

  int update() {

    if (!latchedOn) {
      for (int i = 0; i < speed / 10; i++) {
        move(speed / 10f);
        if (length < finalLength)
          length += speed / 10f;
        else {
          length -= speed / 10f;
          x -= speed * cos((rotation - 90) * PI / 180) / 10f;
          y -= speed * sin((rotation - 90) * PI / 180) / 10f;
        }

        Iterator<Ship> iterator = ships.iterator();
        while (iterator.hasNext()) {
          Ship tempShip = iterator.next();

          if (collideWithShip(tempShip)) {
            latchedOn = true;
            latchedX = x;
            latchedY = y;
            tempShip.shipImpact(strength, split, x, y);
            return -1;
          } else if (passed == false && collideWithShield(tempShip)) {
            if (tempShip.shield[tempShip.closestShieldPoint(x, y)] >= 20f) {
              tempShip.shieldImpact(strength, spread, split, x, y);
              latchedOn = true;
              latchedX = x;
              latchedY = y;
              return -1;
            } else {
              passed = true;
            }
          }
        }
      }
    } else {
      length -= speed;
      x = latchedX -= speed * cos((rotation - 90) * PI / 180);
      y = latchedY -= speed * sin((rotation - 90) * PI / 180);
    }

    return move(0f);
  }

  void draw() {
    pushMatrix();
    strokeWeight(2);


    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    rotate(rotation * PI / 180);

    noFill();
    stroke(255, 198, 10, 30);
    rect(-4.5, 0, 9, length, 8, 8, 8, 8);
    stroke(255, 198, 10, 70);
    rect(-3.5, 0, 7, length, 8, 8, 8, 8);
    stroke(255, 198, 10, 110);
    fill(255, 255, 255, 255);
    rect(-2.5, 0, 5, length, 3, 3, 3, 3);

    popMatrix();
  }
}

// This is the blue torpedo of the enterprise
class QuantumTorpedo extends Projectile {

  QuantumTorpedo(float startX, float startY, float startSpeed, float startRotation, float startRadius, float startStrength, int startSpread, float stSplit, int startShipID) {
    startingX = x = startX;
    startingY = y = startY;
    speed = startSpeed;
    rotation = startRotation;
    radius = startRadius;
    strength = startStrength;
    spread = startSpread;
    shipID = startShipID;
    split = stSplit;
    radius = 0;
    length = 0;
    impactLevel = 30f;
  }

  void draw() {

    pushMatrix();

    translate(x - anchor.x + width / 2, y - anchor.y + height / 2);

    rotate(rotation * PI / 180);

    image(quantumTorpedo, 0, 0);

    popMatrix();
  }
}
