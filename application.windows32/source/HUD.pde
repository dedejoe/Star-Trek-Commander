import java.util.LinkedList; //<>// //<>// //<>//

//This class handles drawing the minimap in the bottomleft
void drawMiniMap() {

  pushMatrix();

  stroke(#ffffff);
  strokeWeight(1);
  fill(0, 0, 0, 50);
  ellipseMode(CENTER);
  translate(5, height - height / 2.5 - 5);

  rect(0, 0, width / 5, height / 2.5);

  translate(width / 10, height / 5);

  fill(0, 255, 0);

  circle(enterprise.x / 10, enterprise.y / 10, 8);

  fill(255, 0, 0);

  circle(warbird.x / 10, warbird.y  / 10, 8);


  popMatrix();
}

//this method draws the shield and hull integrity visual
void drawShipStatus() {

  pushMatrix();

  float translationX = width / 5 + height / 5 * 0.38 * 0.8 * 2;
  float translationY = height - height / 7.5;
  textAlign(CENTER);
  stroke(#ffffff);
  strokeWeight(1);
  fill(#000000);
  ellipseMode(CENTER);
  float hullIntegrity = (float)(enterprise.hullIntegrity) / 100;
  tint(255 * (1f - hullIntegrity), 255 * hullIntegrity, 0);
  translate(translationX, translationY);

  // draw ship model
  float imgWidth =  height / 5 * 0.38 * 0.8;
  float imgHeight = height / 5 * 0.8;
  image(hullStatus, 0, 0, imgWidth, imgHeight);

  // draw hull integrity text
  textSize(18);
  fill(#ffffff);
  text((int)enterprise.hullIntegrity + "%", 0, imgHeight / 0.8 * 0.23);


  // draw shield
  noFill();
  strokeWeight(5);

  // draw top
  float average = enterprise.shieldAverage(0, 90);
  float constant = 0f;
  if (shieldBalanceRectangles[2].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 - imgWidth / 3, 0 - imgHeight / 8, 0 - imgWidth / 2, 0 - imgHeight / 1.8, 0 + imgWidth / 2, 0 - imgHeight /  1.8, 0 + imgWidth / 3, 0 - imgHeight / 8);


  // draw bottom
  average = enterprise.shieldAverage(180, 90);
  if (shieldBalanceRectangles[3].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 - imgWidth / 3, 0 + imgHeight / 8, 0 - imgWidth / 2, 0 + imgHeight / 1.8, 0 + imgWidth / 2, 0 + imgHeight /  1.8, 0 + imgWidth / 3, 0 + imgHeight / 8);

  // draw left
  average = enterprise.shieldAverage(90, 90);
  if (shieldBalanceRectangles[0].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 + imgWidth / 2, 0 + imgHeight / 1.8, 0 - imgWidth / 1.4, 0 + imgHeight / 2.2, 0 - imgWidth / 1.4, 0 - imgHeight / 2.2, 0 + imgWidth / 2, 0 - imgHeight / 1.8);

  // draw right
  average = enterprise.shieldAverage(270, 90);
  if (shieldBalanceRectangles[1].intersect(mouseX, mouseY)) constant = 100f;
  stroke(constant, constant, average - 40f);
  constant = 0f;
  curve(0 - imgWidth / 2, 0 + imgHeight / 1.8, 0 + imgWidth / 1.4, 0 + imgHeight / 2.2, 0 + imgWidth / 1.4, 0 - imgHeight / 2.2, 0 - imgWidth / 2, 0 - imgHeight / 1.8);

  fill(#ffffff);
  // draw shield text

  text((int)(enterprise.shieldAverage(0, 90) * 100 / 255) + "%", 0, 0 - imgHeight * 0.7);
  text((int)(enterprise.shieldAverage(180, 90) * 100 / 255) + "%", 0, 0 + imgHeight * 0.75);
  text((int)(enterprise.shieldAverage(90, 90) * 100 / 255) + "%", 0 - imgWidth * 1.25f, 0 );
  text((int)(enterprise.shieldAverage(270, 90) * 100 / 255) + "%", 0 + imgWidth * 1.3f, 0);



  noTint();
  popMatrix();
}

// This class handles all of the menus
class Menu {

  LinkedList<Container> containers;
  int currentContainer;

  Menu() {
    containers = new LinkedList<Container>();
    currentContainer = 0;
  }  

  void update_draw() {

    textFont(myFont);
    pushMatrix();
    translate(width / 2, height / 4 + height / 10);
    textAlign(CENTER);

    image(logo, 0, 0);


    // draw all contained containers
    Iterator<Container> iterator = containers.iterator();

    int counter = 0;
    while (iterator.hasNext()) {
      Container tmpContainer = iterator.next();
      if (counter == currentContainer) {
        tmpContainer.update();
        tmpContainer.draw();
        break;
      }
      counter++;
    }

    textFont(createFont("Arial Bold", 16));

    popMatrix();
  }
}

// This method creates the menu for victory/defeat
void createReplayMenu() {

  menu = new Menu();

  Container container = new Container();

  Button button = new Button("Play Again", 1);
  container.addButton(button);

  button = new Button("Exit", 0);
  container.addButton(button);

  menu.containers.add(container);

  container = new Container();

  button = new Button("Easy", 3);
  container.addButton(button);

  button = new Button("Hard", 3);
  container.addButton(button);

  button = new Button("Back", 2, 32);
  container.addButton(button);

  menu.containers.add(container);
}

// This class handles all of the button clicking in the menus
class Button {

  float timer;
  int timerDirection;
  float x, y;
  int textSize;
  String text;
  int type;
  //type 0 = exit, type 1 = difficulty, type 2 = go Back

  Button(String newText, int newType) {
    x = -width / 150;
    y = 0;
    text = newText;
    type = newType;
    textSize = 64;
  }

  Button(String newText, int newType, int size) {
    x = -width / 150;
    y = 0;
    text = newText;
    type = newType;
    textSize = size;
  }

  void draw() {
    textSize(textSize * timer);
    text(text, x, y);
  }

  boolean mouseOver() {

    int mX = mouseX - width / 2;
    int mY = (mouseY - height / 4 - height / 10);
    float textTop = y - (textSize / 2);
    float textBottom = y + (textSize / 2);
    float textLeft = x - (text.length() * 15);
    float textRight = x + (text.length() * 15);
    return (mY > textTop && mY < textBottom && mX > textLeft && mX < textRight);
  }

  // this method determines what happens when a button is clicked
  void execute() {

    if (type == 0) {
      exit();
    } else if (type == 1) {
      menu.currentContainer = 1;
    } else if (type == 2) {
      menu.currentContainer = 0;
    } else if (type == 5) {
      menu.currentContainer = 2;
    } else if (type == 3) {
      menu.currentContainer = 0;
      if (text == "Easy") {
        difficultyHard = false;
      } else {
        difficultyHard = true;
      }
      menuUp = false;

      // create New Menu

      menu = new Menu();

      Container container = new Container();

      Button button = new Button("Resume", 4);
      container.addButton(button);

      button = new Button("Tutorial", 5);
      container.addButton(button);

      button = new Button("New Game", 1);
      container.addButton(button);

      button = new Button("Exit", 0);
      container.addButton(button);

      menu.containers.add(container);

      container = new Container();

      button = new Button("Easy", 3);
      container.addButton(button);

      button = new Button("Hard", 3);
      container.addButton(button);

      button = new Button("Back", 2, 32);
      container.addButton(button);

      menu.containers.add(container);

      container = new Container(tutorial);

      button = new Button("Back", 2, 32);
      container.addButton(button);

      menu.containers.add(container);

      startGame();
    } else if (type == 4) {
      menuUp = false;
      menuMusic.pause();
      spaceNoise.loop(0.8);
      battleMusic.loop(0.5f);
    }
  }

  // this makes the buttons visually responsive
  void update() {

    if (mouseOver() == true) {

      if (timer <= 0.5f) {

        timerDirection = 1;
      } else if (timer >= 1.0f) {
        timerDirection = -1;
      }

      timer += 0.02f * timerDirection;
    } else {
      timer = 1f;
    }
  }
}

// this class contains all of the buttons on a given menu
class Container {

  private PImage image = null;
  private LinkedList<Button> buttons;

  Container() {
    buttons = new LinkedList<Button>();
  }

  Container(PImage newImage) {
    buttons = new LinkedList<Button>();
    image = newImage;
  }

  void draw() {

    // draw all contained controls
    Iterator<Button> iterator = buttons.iterator();

    // this is for the tutorial
    if (image != null) {
      image(image, 0, height / 2.8);
      while (iterator.hasNext()) {
        Button button = iterator.next();
        button.y = height / 2 + 75;
        button.draw();
      }
      return;
    }

    while (iterator.hasNext()) {
      iterator.next().draw();
    }
  }

  void update() {

    // draw all contained controls
    Iterator<Button> iterator = buttons.iterator();

    while (iterator.hasNext()) {
      iterator.next().update();
    }
  }

  void addButton(Button button) {
    button.y = height / 3 + count() * 64;
    buttons.add(button);
  }

  int count() {
    int i = 0;
    Iterator<Button> iterator = buttons.iterator();

    while (iterator.hasNext()) {
      iterator.next();
      i++;
    }

    return i;
  }
}
