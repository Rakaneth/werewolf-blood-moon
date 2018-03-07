package com.rakaneth.wbm.system;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import squidpony.squidmath.Coord;

import java.io.IOException;

public class Creature
    extends GameObject
  implements Actor, Fighter {
  int energy;
  int speed;
  int str;

  public static Creature makeAnimal(String animalID) {
    YAMLFactory factory = new YAMLFactory();
    try {
      YAMLParser parser = factory.createParser(Gdx.files.internal("data/animals.yml").reader());
    } catch (IOException e) {
      e.printStackTrace();
    }
    Creature foetus = new Creature();
    //TODO: finish importing animals from YAML
    return foetus;
  }

  public Creature() {}

  public Creature(char glyph, String color, String name, String desc, Coord pos, int str, int speed) {
    this.glyph = glyph;
    this.color = color;
    this.name = name;
    this.desc = desc;
    this.pos = pos;
    this.str = str;
    this.speed = speed;
  }

  public int getEnergy() { return energy;}
  public void setEnergy(int val) { energy = val; }
  public int getSpeed() { return speed; }
  public int getStr() { return str; }
}
