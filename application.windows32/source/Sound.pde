class SoundEffect {

  int counter;
  int rate;
  SoundFile sound;

  SoundEffect(SoundFile newSound, int newRate) {
    sound = newSound;
    rate = newRate;
  }

  void play(float volume) {
    if (counter >= rate) {
      sound.play(1, volume * 0.6);
      counter = 0;
    }
  }
  
  void update(){
  
    counter++;
    
  }
}
