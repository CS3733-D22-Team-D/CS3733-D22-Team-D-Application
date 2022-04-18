package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.tables.TableHandler;

public interface Request {

  enum RequestStatus {
    REQUESTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
  }

  enum Priority {
    LOW,
    MEDIUM,
    HIGH,
    OVERDUE
  }

  /** @return name of the Request class */
  @TableHandler(table = 1, col = 0)
  public String requestType();

  /** @return the priority of a given request */
  @TableHandler(table = 1, col = 1)
  public Priority getPriority();

  /** @return whether or not this is a request that requires things to be moved */
  @TableHandler(table = 1, col = 2)
  public boolean requiresTransport();

  /** @return roomID of a given request */
  public String getRoomID();

  public String getNodeID();

  public String getDateNeeded();
}
