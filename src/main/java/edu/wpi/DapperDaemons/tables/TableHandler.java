package edu.wpi.DapperDaemons.map.tables;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(MultiTable.class)
public @interface TableHandler {
  int table();

  int col();
}
