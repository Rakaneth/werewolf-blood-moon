package com.rakaneth.wbm.system;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import squidpony.squidmath.Coord;
import squidpony.squidmath.OrderedMap;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Creature
    extends GameObject
  implements Actor, Fighter {
  int energy;
  int speed;
  int str;
  static Map<String, Creature> bluePrints;


  static {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      bluePrints = mapper.readValue(Gdx.files.internal("data/animals.yml").reader(),
                                    new TypeReference<Map<String, Creature>>(){});
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Creature makeAnimal(String animalID) {
    Creature bluePrint = bluePrints.get(animalID);
    return new Creature(bluePrint);
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

  public Creature(Creature c) {
    glyph = c.glyph;
    color = c.color;
    name = c.name;
    desc = c.desc;
    pos = c.pos;
    str = c.str;
    speed = c.speed;
  }

  public int getEnergy() { return energy;}
  public void setEnergy(int val) { energy = val; }
  public int getSpeed() { return speed; }
  public int getStr() { return str; }
}
