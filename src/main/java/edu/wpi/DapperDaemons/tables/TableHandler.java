package edu.wpi.DapperDaemons.tables;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(MultiTable.class)
public @interface TableHandler {
  int table();

  int col();
}
