package Entity;

import Map.Crd;

public class Entity {
    // Identification data
    int id;
    int owner;

    // State Data
    int health;
    Crd pos;

    // Graphical data
    int entityType; // 0-3 indicates whether the entity is an Agent, Projectile, Resource, or Structure
    int state; // determined by the class of entity, but should reflect the state of the entity for graphical purposes
    int frame; // the animation state of the entity

}
