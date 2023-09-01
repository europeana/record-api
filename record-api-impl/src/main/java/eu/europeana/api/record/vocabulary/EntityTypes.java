package eu.europeana.api.record.vocabulary;

public enum EntityTypes implements EntityKeyword {
  Organization("Organization"),
  Concept("Concept"),
  Agent("Agent"),
  Place("Place"),
  TimeSpan("TimeSpan");

  private String entityType;

  public String getEntityType() {
    return entityType;
  }


  EntityTypes(String entityType) {
    this.entityType = entityType;

  }

  @Override
  public String getJsonValue() {
    return getEntityType();
  }

  public static EntityTypes getByEntityType(String entityType) {

    for (EntityTypes entityTypeEnum : EntityTypes.values()) {
      if (entityTypeEnum.getEntityType().equalsIgnoreCase(entityType)) return entityTypeEnum;
    }

    return null;
  }

}
