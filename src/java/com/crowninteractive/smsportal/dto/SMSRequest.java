/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

/**
 *
 * @author Adekanmbi Oluremi <adekanmbi.oluremi@ucitech.com.ng>
 */
public class SMSRequest {
  private String command;
  private String game;
  private String betTypeOrNumbers;
  private String msidn;

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public String getBetTypeOrNumbers() {
    return betTypeOrNumbers;
  }

  public void setBetTypeOrNumbers(String betTypeOrNumbers) {
    this.betTypeOrNumbers = betTypeOrNumbers;
  }

  public String getMsidn() {
    return msidn;
  }

  public void setMsidn(String msidn) {
    this.msidn = msidn;
  }
  
  
}
