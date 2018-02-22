package se.mdh.driftavbrott.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import se.mdh.driftavbrott.modell.Driftavbrott;

/**
 * En facade för att kunna hämta information om driftavbrott från en central
 * MDH-tjänst.
 *
 * @author Dennis Lundberg
 * @version $Id: DriftavbrottFacade.java 49125 2018-02-20 09:15:04Z dlg01 $
 */
public class DriftavbrottFacade {
  /**
   * Den log som ska användas.
   */
  private static final Log log = LogFactory.getLog(DriftavbrottFacade.class);
  private static final String PROPERTIES_FILE = "se.mdh.driftavbrott.properties";
  private Properties properties;

  /**
   * Skapa en ny instans.
   */
  public DriftavbrottFacade() throws IOException {
    FileInputStream inputStream = null;
    try {
      URL resource = getClass().getClassLoader().getResource(PROPERTIES_FILE);
      if(resource == null) {
        throw new IOException("Hittade inte properties-filen '" + PROPERTIES_FILE + "' på classpath.");
      }
      inputStream = FileUtils.openInputStream(new File(resource.getFile()));
      properties = new Properties();
      properties.load(inputStream);
    }
    finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

  /**
   * Hämta ett pågående driftavbrott för en samling med kanaler.
   *
   * @param kanaler De kanaler som du är intresserad av
   * @param system Det system som frågar om driftavbrott, dvs ditt system
   * @return Ett pågående driftavbrott
   * @throws WebServiceException Om något går fel i kommunikationen med web servicen
   */
  public Driftavbrott getPagaendeDriftavbrott(final List<String> kanaler,
                                              final String system)
      throws WebServiceException {
    try {
      WebClient client = WebClient.create(properties.getProperty("se.mdh.driftavbrott.service.url"))
          .path("/driftavbrott/pagaende")
          .query("kanal", kanaler.toArray())
          .query("system", system);
      client.accept(MediaType.APPLICATION_XML);
      log.debug("Ska hämta driftavbrott från: "
                    + client.getCurrentURI().toString());
      return client.get(Driftavbrott.class);
    }
    catch(WebApplicationException wae) {
      // Hantera standard JAX-RS exception
      if(wae.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
        log.info("Det finns inga driftavbrott för någon av kanalerna " + kanaler + ".");
        return null;
      }
      else {
        String message = "Det gick inte att hämta driftavbrott för kanalerna " + kanaler + " (JAX-RS).";
        log.error(message, wae);
        throw new WebServiceException("", wae);
      }
    }
    catch(Throwable t) {
      // Hantera okänt fel
      String message = "Det gick inte att hämta driftavbrott för kanalerna " + kanaler + " (okänt fel).";
      log.error(message, t);
      throw new WebServiceException(message, t);
    }
  }
}
