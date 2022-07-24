package com.boromak.interviews.recipebook.testutils;

import org.testcontainers.containers.MariaDBContainer;

public class TestMariaDbContainer extends MariaDBContainer<TestMariaDbContainer> {
  private static final String IMAGE_VERSION = "mariadb:10.6.8";
  private static TestMariaDbContainer container;

  private TestMariaDbContainer() {
    super(IMAGE_VERSION);
  }

  public static TestMariaDbContainer getInstance() {
    if (container == null) {
      container = new TestMariaDbContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("DB_URL", container.getJdbcUrl());
    System.setProperty("DB_USERNAME", container.getUsername());
    System.setProperty("DB_PASSWORD", container.getPassword());
  }

  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
