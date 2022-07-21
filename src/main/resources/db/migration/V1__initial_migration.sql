CREATE TABLE recipe
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    `name`             VARCHAR(255)          NULL,
    number_of_servings DOUBLE                NOT NULL,
    vegetarian         BOOLEAN               NOT NULL,
    instructions       VARCHAR(6144)         NOT NULL,
    CONSTRAINT pk_recipe PRIMARY KEY (id)
);

CREATE TABLE recipe_ingredients
(
    recipe_id   BIGINT       NOT NULL,
    ingredients VARCHAR(255) NULL
);

ALTER TABLE recipe_ingredients
    ADD CONSTRAINT fk_recipe_ingredients_on_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id);
