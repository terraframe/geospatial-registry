package net.geoprism.georegistry.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.session.Request;

import net.geoprism.georegistry.testframework.USATestData;

public class WMSServiceTest
{
  protected USATestData     testData;

  protected ClientRequestIF adminCR;

  @Before
  public void setUp()
  {
    this.testData = USATestData.newTestData(GeometryType.POLYGON, true);

    this.adminCR = testData.adminClientRequest;
  }

  @After
  public void tearDown() throws IOException
  {
    testData.cleanUp();

    FileUtils.deleteDirectory(new File(VaultProperties.getPath("vault.default"), "files"));
  }

  @Test
  @Request
  public void testCreateDeleteDatabaseViewOnTreeType() throws SQLException
  {
    GeoObjectType type = this.testData.STATE.getGeoObjectType(GeometryType.POLYGON);

    WMSService service = new WMSService();

    String viewName = service.createDatabaseView(type, false);

    try
    {
      ResultSet results = Database.query("SELECT * FROM " + viewName);

      try
      {
        Assert.assertTrue(results.next());
      }
      finally
      {
        results.close();
      }
    }
    finally
    {
      service.deleteDatabaseView(type);
    }
  }

}
