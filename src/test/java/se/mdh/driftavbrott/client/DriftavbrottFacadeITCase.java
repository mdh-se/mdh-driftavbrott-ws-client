package se.mdh.driftavbrott.client;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import se.mdh.driftavbrott.modell.Driftavbrott;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tester f�r DriftavbrottFacade.
 *
 * @author Dennis Lundberg
 * @version $Id: DriftavbrottFacadeITCase.java 48632 2017-09-28 13:56:27Z dlg01 $
 */
public class DriftavbrottFacadeITCase {
  /**
   * Den log som ska anv�ndas.
   */
  private static final Log log = LogFactory.getLog(DriftavbrottFacadeITCase.class);
  private static final String KANAL_ALLTID = "alltid";
  private static final String KANAL_ALDRIG = "aldrig";
  private static final String KANAL_SAKNAS = "kanalen.som.inte.finns";

  private static List<String> kanalerFinns = new ArrayList<>();
  private static List<String> kanalerSaknas = new ArrayList<>();
  private static List<String> kanalerFinnsMenHarEjP�g�endeAvbrott = new ArrayList<>();

  @BeforeClass
  public static void beforeClass() {
    kanalerFinns.add(KANAL_ALLTID);
    kanalerSaknas.add(KANAL_SAKNAS);
    kanalerFinnsMenHarEjP�g�endeAvbrott.add(KANAL_ALDRIG);
  }

  @Test
  public void testGetPagaende() throws Exception {
    DriftavbrottFacade facade = new DriftavbrottFacade();
    Driftavbrott driftavbrott = facade.getPagaendeDriftavbrott(kanalerFinns, "Integrationstest");
    log.info(driftavbrott);
    assertNotNull(driftavbrott);
  }

  /**
   * Testa korrekt nullhantering f�r en ickeexisterande kanal.
   */
  @Test
  public void testGetPagaendeKanalSaknas() throws Exception {
    DriftavbrottFacade facade = new DriftavbrottFacade();
    Driftavbrott driftavbrott = facade.getPagaendeDriftavbrott(kanalerSaknas, "Integrationstest");
    log.info(driftavbrott);
    assertNull(driftavbrott);
  }

  /**
   * Testa korrekt nullhantering f�r en kanal som inte har ett p�g�ende avbrott.
   */
  @Test
  public void testGetKanalHarEjPagaendeAvbrott() throws Exception {
    DriftavbrottFacade facade = new DriftavbrottFacade();
    Driftavbrott driftavbrott = facade.getPagaendeDriftavbrott(kanalerFinnsMenHarEjP�g�endeAvbrott, "Integrationstest");
    log.info(driftavbrott);
    assertNull(driftavbrott);
  }

  @Test
  public void testGetPagaendeMedMarginal() throws Exception {
    DriftavbrottFacade facade = new DriftavbrottFacade();
    Driftavbrott driftavbrott = facade.getPagaendeDriftavbrott(kanalerFinns, "Integrationstest", 15);
    log.info(driftavbrott);
    assertNotNull(driftavbrott);
  }

  /**
   * Testa korrekt nullhantering f�r en ickeexisterande kanal.
   */
  @Test
  public void testGetPagaendeKanalSaknasMedMarginal() throws Exception {
    DriftavbrottFacade facade = new DriftavbrottFacade();
    Driftavbrott driftavbrott = facade.getPagaendeDriftavbrott(kanalerSaknas, "Integrationstest", 15);
    log.info(driftavbrott);
    assertNull(driftavbrott);
  }

  /**
   * Testa korrekt nullhantering f�r en kanal som inte har ett p�g�ende avbrott.
   */
  @Test
  public void testGetKanalHarEjPagaendeAvbrottMedMarginal() throws Exception {
    DriftavbrottFacade facade = new DriftavbrottFacade();
    Driftavbrott driftavbrott = facade.getPagaendeDriftavbrott(kanalerFinnsMenHarEjP�g�endeAvbrott, "Integrationstest", 15);
    log.info(driftavbrott);
    assertNull(driftavbrott);
  }
}
