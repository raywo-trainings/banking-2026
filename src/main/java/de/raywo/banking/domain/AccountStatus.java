package de.raywo.banking.domain;

/**
 * Aufzählung der möglichen Kontostatus.
 *
 * <p>Ein Enum definiert eine feste Menge benannter Konstanten. Im Gegensatz
 * zu Strings oder int-Konstanten sorgt der Compiler dafür, dass nur gültige
 * Werte verwendet werden können.</p>
 */
public enum AccountStatus {

  /** Das Konto ist aktiv und kann für Transaktionen genutzt werden. */
  ACTIVE,

  /** Das Konto ist inaktiv und gesperrt für Transaktionen. */
  INACTIVE

}
