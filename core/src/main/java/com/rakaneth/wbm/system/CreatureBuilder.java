package com.rakaneth.wbm.system;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class CreatureBuilder {
  private static Map<String, Creature> bluePrints;

  static {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      bluePrints = mapper.readValue(Gdx.files.internal("data/animals.yml").reader(),
                                    new TypeReference<Map<String, Creature>>() {
                                    });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Creature makeAnimal(String animalID) {
    Creature bluePrint = bluePrints.get(animalID);
    return new Creature(bluePrint);
  }
}
