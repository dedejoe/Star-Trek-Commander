// this two methods just bring a rotation back down to 0 - 360
float normalizeAngle(float angle) {

  while (angle >= 360 || angle < 0) {

    if (angle >= 360) {
      angle -= 360;
    } else if (angle < 0) {
      angle += 360;
    }
  }
  return angle;
}

int normalizeAngle(int angle) {

  while (angle >= 360 || angle < 0) {

    if (angle >= 360) {
      angle -= 360;
    } else if (angle < 0) {
      angle += 360;
    }
  }
  return angle;
}

// returns the distance of two points
float distance(float x, float y) {

  return sqrt(sq(x) + sq(y));
}

float distance(float x1, float y1, float x2, float y2) {

  return sqrt(sq(x2 - x1) + sq(y2 - y1));
}

// this class handles the stars
class Star {

  float x;
  float y;
  float radius;

  Star() {
    x = random(width) - width * 3 / 2 + width * (random(3) - 1);
    y = random(height) - height * 5 / 2 + height * (random(4) - 2);
    radius = random(5);
  }

  Star(float newX, float newY, float newRadius) {
    x = newX;
    y = newY;
    radius = newRadius;
  }

  void draw() {
    float transX = x - anchor.x + (width / 2);
    float transY = y - anchor.y + (height / 2);
    if (transX > 0 && transX < width && transY > 0 && transY < height)
      circle(transX, transY, radius);
  }
}
