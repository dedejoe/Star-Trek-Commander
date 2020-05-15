void keyPressed() { //<>// //<>//

  if (gameOver) {
    key = 0;
    return;
  }

  if (key == ESC) {

    if (startedGame == false) {
      menuUp = true;
      return;
    } else {
      menuUp = !menuUp;
      if (menuUp) {
        if (battleMusic != null) {
          spaceNoise.pause();
          battleMusic.pause();
        }
        menuMusic.play();
      } else {
        menuMusic.pause();
        spaceNoise.loop(0.8);
        battleMusic.loop(0.5f);
      }
    }

    key = 0;
  }



  if (enterprise != null && enterprise.destroyed()) return;

  if (menuUp) return;

  if (key == 'w') {
    moving[0] = true;
  } else if (key == 's') {
    moving[1] = true;
  }

  if (key == 'a') {
    moving[2] = true;
  } else if (key == 'd') {
    moving[3] = true;
  }
}

void keyReleased() {
  if (enterprise == null || enterprise.destroyed()) return;
  if (key == 'w') {
    moving[0] = false;
  } else if (key == 's') {
    moving[1] = false;
  }

  if (key == 'a') {
    moving[2] = false;
  } else if (key == 'd') {
    moving[3] = false;
  }

  if (key == ' ') {
    enterprise.fire(1);
  }
}

void runKeyboard() {

  if (gameOver) {
    key = 0;
    return;
  }

  if (enterprise != null && enterprise.destroyed()) return;

  if (menuUp) return;

  if (moving[0]) {
    enterprise.x += enterprise.speed * cos((enterprise.rotation + 90) * PI / 180);
    enterprise.y += enterprise.speed * sin((enterprise.rotation + 90)* PI / 180);
  } else if (moving[1]) {
    enterprise.x -= cos((enterprise.rotation + 90) * PI / 180) * enterprise.speed / 1.7;
    enterprise.y -= sin((enterprise.rotation + 90)* PI / 180) * enterprise.speed / 1.7;
  }

  if (moving[2]) {
    enterprise.setRotation(enterprise.rotation - enterprise.turningSpeed);
  } else if (moving[3]) {
    enterprise.setRotation(enterprise.rotation + enterprise.turningSpeed);
  }
}

void mousePressed () {

  if (!menuUp && gameOver == false) {
    for (int i = 0; i < 4; i++) {
      if ( shieldBalanceRectangles[i].intersect(mouseX, mouseY)) {
        enterprise.shieldBalanceQuadrant = i;
        enterprise.shieldBalanceTimer = 120;
        return;
      }
    }
    enterprise.fire(0);
  } else {

    Iterator<Container> iterator = menu.containers.iterator();

    int counter = 0;
    while (iterator.hasNext()) {
      Container tmpContainer = iterator.next();

      if (menu.currentContainer == counter) {
        // found our container, check if we're over a button and click it.
        Iterator<Button> buttonIterator = tmpContainer.buttons.iterator();
        while (buttonIterator.hasNext()) {
          Button tmpButton = buttonIterator.next();
          if (tmpButton.mouseOver()) {
            tmpButton.execute();
            break;
          }
        }
        break;
      }
      counter++;
    }
  }
}
