import java.util.LinkedList;
import java.util.Iterator;

class Weapon {

  SoundEffect sound;
  Ship ship;
  float fireRate;
  int counter;
  float damage;
  int spread;
  int type;
  float split;
  float speed;
  boolean buffered;

  Weapon(Ship myShip, float rate, float spe, float dam, int sp, float spl, SoundEffect sou, int myType) {
    ship = myShip;
    fireRate = rate;
    damage = dam;
    spread = sp;
    split = spl;
    speed = spe;
    sound = sou;
    type = myType;
  }

  void update() {

    counter++;
  }

  boolean canFire(boolean buffer) {

    if (buffer == true)
      if (counter >= 0.8f * fireRate * 60f)
        buffered = buffer;

    return ( counter >= fireRate * 60f);
  }

  void fire() {

    // So you can't fire on top of each other
    if (abs(warbird.x - enterprise.x) < enterprise.shieldRadiusX() && abs(warbird.y - enterprise.y) < enterprise.shieldRadiusY()) return;
    
    float vol = 0f;
    float newDamage = damage;

    if (ship.id != 0) {

      if (difficultyHard == true) {
        newDamage *= 1.5;
      }
    } else {

      if (difficultyHard == false) {
        newDamage *= 1.5;
      }
    }

    if (type == 2)
    {
      ship.projectiles.add(new Projectile(ship.getShieldX(160), ship.getShieldY(160), speed, ship.rotation + 180, 20f, newDamage, spread, split, ship.id));
      ship.projectiles.add(new Projectile(ship.getShieldX(200), ship.getShieldY(200), speed, ship.rotation + 180, 20f, newDamage, spread, split, ship.id));
      vol = 0.3f;
    } else if (type == 1) {

      ship.projectiles.add(new QuantumTorpedo(ship.getShieldX(180), ship.getShieldY(180), speed, ship.rotation + 180, 20f, newDamage, spread, split, ship.id));
      vol = 0.9f;
      
    } else if (type == 0) {


      float angle = atan2(mouseY - ((ship.shipWidth() * 0.7) * sin((ship.rotation + 90)* PI / 180)) - height/2 + (anchor.y - enterprise.y), mouseX - ((ship.shipWidth() * 0.7) * cos((ship.rotation + 90) * PI / 180)) - width/2 + (anchor.x - enterprise.x)) * 180 / PI + 90;

      ship.projectiles.add(new Phaser(ship.x + (ship.shipWidth() * 0.7) * cos((ship.rotation + 90) * PI / 180), 
        ship.y + (ship.shipWidth() * 0.7) * sin((ship.rotation + 90)* PI / 180), 
        speed, angle, 25f, newDamage, spread, split, ship.id));
      vol = 0.12f;
    }

    sound.play(vol);
    counter = 0;
    buffered = false;
  }
}
