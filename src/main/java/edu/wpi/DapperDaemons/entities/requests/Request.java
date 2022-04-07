package edu.wpi.DapperDaemons.entities.requests;

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
    HIGH
  }

  /** @return name of the Request class */
  public String getRequestType();

  /** @return the priority of a given request */
  public Priority getPriority();

  /** @return whether or not this is a request that requires things to be moved */
  public boolean requiresTransport();
}
