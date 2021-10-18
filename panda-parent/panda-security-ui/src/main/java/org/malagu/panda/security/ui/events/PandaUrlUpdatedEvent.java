package org.malagu.panda.security.ui.events;

import org.springframework.context.ApplicationEvent;

public class PandaUrlUpdatedEvent extends ApplicationEvent {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PandaUrlUpdatedEvent(Object source) {
    super(source);
  }

}
