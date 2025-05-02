package org.example.alphasolutions.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Forkert email eller adgangskode. Hvis du har glemt din adgangskode, kontakt venligst en administrator.");
  }
}
