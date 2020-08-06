/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;

import net.geoprism.registry.ChangeFrequency;
import net.geoprism.registry.MasterList;
import net.geoprism.registry.MasterListVersion;
import net.geoprism.registry.Organization;
import net.geoprism.registry.TileCache;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.test.TestGeoObjectTypeInfo;
import net.geoprism.registry.test.TestHierarchyTypeInfo;
import net.geoprism.registry.test.USATestData;

public class MasterListTest
{
  private static USATestData       testData;

  private static AttributeTermType testTerm;

  @BeforeClass
  public static void setUpClass()
  {
    testData = USATestData.newTestData();
    testData.setUpMetadata();

    setUpInReq();
  }

  @Request
  private static void setUpInReq()
  {
    testTerm = (AttributeTermType) AttributeType.factory("testTerm", new LocalizedValue("testTermLocalName"), new LocalizedValue("testTermLocalDescrip"), AttributeTermType.TYPE, false, false, false);
    // testTerm = (AttributeTermType)
    // ServiceFactory.getRegistryService().createAttributeType(null,
    // USATestData.STATE.getCode(), testTerm.toJSON().toString());

    ServerGeoObjectType got = ServerGeoObjectType.get(USATestData.STATE.getCode());
    testTerm = (AttributeTermType) got.createAttributeType(testTerm.toJSON().toString());
  }

  @AfterClass
  public static void cleanUpClass()
  {
    if (testData != null)
    {
      testData.tearDownMetadata();
    }
  }

  @Before
  public void setUp()
  {
    testData.setUpInstanceData();

    testData.logIn(USATestData.USER_NPS_RA);
  }

  @After
  public void tearDown()
  {
    testData.logOut();

    testData.tearDownInstanceData();
  }

  @Test
  @Request
  public void testSerialization()
  {
    MasterList list = new MasterList();
    list.setUniversal(USATestData.STATE.getUniversal());
    list.getDisplayLabel().setValue("Test List");
    list.setCode("TEST_CODE");
    list.setRepresentativityDate(new Date());
    list.setPublishDate(new Date());
    list.setListAbstract("My Abstract");
    list.setProcess("Process");
    list.setProgress("Progress");
    list.setAccessConstraints("Access Contraints");
    list.setUseConstraints("User Constraints");
    list.setAcknowledgements("Acknowledgements");
    list.setDisclaimer("Disclaimer");
    list.setContactName("Contact Name");
    list.setOrganization(USATestData.ORG_NPS.getServerObject());
    list.setTelephoneNumber("Telephone Number");
    list.setEmail("Email");
    list.setIsMaster(false);
    list.setVisibility(MasterList.PUBLIC);

    JsonObject json = list.toJSON();
    MasterList test = MasterList.fromJSON(json);

    Assert.assertEquals(list.getUniversalOid(), test.getUniversalOid());
    Assert.assertEquals(list.getDisplayLabel().getValue(), test.getDisplayLabel().getValue());
    Assert.assertEquals(list.getRepresentativityDate(), test.getRepresentativityDate());
    Assert.assertEquals(list.getPublishDate(), test.getPublishDate());
    Assert.assertEquals(list.getListAbstract(), test.getListAbstract());
    Assert.assertEquals(list.getProcess(), test.getProcess());
    Assert.assertEquals(list.getProgress(), test.getProgress());
    Assert.assertEquals(list.getAccessConstraints(), test.getAccessConstraints());
    Assert.assertEquals(list.getUseConstraints(), test.getUseConstraints());
    Assert.assertEquals(list.getAcknowledgements(), test.getAcknowledgements());
    Assert.assertEquals(list.getDisclaimer(), test.getDisclaimer());
    Assert.assertEquals(list.getContactName(), test.getContactName());
    Assert.assertEquals(list.getOrganization(), test.getOrganization());
    Assert.assertEquals(list.getTelephoneNumber(), test.getTelephoneNumber());
    Assert.assertEquals(list.getEmail(), test.getEmail());
    Assert.assertEquals(list.getCode(), test.getCode());
    Assert.assertEquals(list.getHierarchiesAsJson().toString(), test.getHierarchiesAsJson().toString());
  }

  // @Test
  // @Request
  // public void testCreateEntity() throws SQLException
  // {
  // JsonObject json = getJson(USATestData.STATE, USATestData.COUNTRY);
  //
  // MasterList test = MasterList.create(json);
  //
  // try
  // {
  // MdBusinessDAOIF mdTable = MdBusinessDAO.get(test.getMdBusinessOid());
  //
  // Assert.assertNotNull(mdTable);
  // }
  // finally
  // {
  // test.delete();
  // }
  // }
  //
  // @Test
  // @Request
  // public void testCreateLeaf() throws SQLException
  // {
  // JsonObject json = getJson(testData.DISTRICT, USATestData.STATE,
  // USATestData.COUNTRY);
  //
  // MasterList test = MasterList.create(json);
  //
  // try
  // {
  // MdBusinessDAOIF mdTable = MdBusinessDAO.get(test.getMdBusinessOid());
  //
  // Assert.assertNotNull(mdTable);
  // }
  // finally
  // {
  // test.delete();
  // }
  // }
  //
  //
  // @Test
  // @Request
  // public void testPublishLeaf()
  // {
  // JsonObject json = getJson(testData.DISTRICT, USATestData.STATE,
  // USATestData.COUNTRY);
  //
  // MasterList test = MasterList.create(json);
  //
  // try
  // {
  // test.publish();
  // }
  // finally
  // {
  // test.delete();
  // }
  // }

  @Test
  @Request
  public void testCreateMultiple()
  {
    JsonObject json = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC);

    MasterList test1 = MasterList.create(json);

    try
    {
      json.addProperty(MasterList.CODE, "CODE_2");

      MasterList test2 = MasterList.create(json);
      test2.delete();

      Assert.fail("Able to create multiple masterlists with the same universal");
    }
    catch (DuplicateDataDatabaseException e)
    {
      test1.delete();
    }
  }

  @Test
  public void testServiceCreateAndRemove()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);

    String oid = result.get(ComponentInfo.OID).getAsString();

    service.remove(testData.clientRequest.getSessionId(), oid);
  }

  @Test
  public void testListByOrg()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);

    try
    {
      JsonArray orgs = service.listByOrg(testData.clientRequest.getSessionId());
      JsonObject org = orgs.get(0).getAsJsonObject();

      Assert.assertNotNull(org.get("oid").getAsString());
      Assert.assertEquals(USATestData.ORG_NPS.getDisplayLabel(), org.get("label").getAsString());
      Assert.assertTrue(org.get("admin").getAsBoolean());
      Assert.assertTrue(org.get("lists").getAsJsonArray().size() > 0);
    }
    finally
    {
      String oid = result.get(ComponentInfo.OID).getAsString();
      service.remove(testData.clientRequest.getSessionId(), oid);
    }
  }

  @Test
  public void testListPublicByOrgFromOtherOrg()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);

    try
    {
      USATestData.runAsUser(USATestData.USER_PPP_RA, (request, adapter) -> {

        JsonArray orgs = service.listByOrg(request.getSessionId());
        JsonObject org = orgs.get(0).getAsJsonObject();

        Assert.assertNotNull(org.get("oid").getAsString());
        Assert.assertEquals(USATestData.ORG_NPS.getDisplayLabel(), org.get("label").getAsString());
        Assert.assertFalse(org.get("admin").getAsBoolean());
        Assert.assertTrue(org.get("lists").getAsJsonArray().size() > 0);
      });
    }
    finally
    {
      String oid = result.get(ComponentInfo.OID).getAsString();
      service.remove(testData.clientRequest.getSessionId(), oid);
    }
  }

  @Test
  public void testPrivateListByOrgFromOtherOrg()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PRIVATE);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);

    try
    {
      USATestData.runAsUser(USATestData.USER_PPP_RA, (request, adapter) -> {
        JsonArray orgs = service.listByOrg(request.getSessionId());
        JsonObject org = orgs.get(0).getAsJsonObject();

        Assert.assertNotNull(org.get("oid").getAsString());
        Assert.assertEquals(USATestData.ORG_NPS.getDisplayLabel(), org.get("label").getAsString());
        Assert.assertFalse(org.get("admin").getAsBoolean());
        Assert.assertTrue(org.get("lists").getAsJsonArray().size() == 0);
      });
    }
    finally
    {
      String oid = result.get(ComponentInfo.OID).getAsString();
      service.remove(testData.clientRequest.getSessionId(), oid);
    }
  }

  @Test
  @Request
  public void testPublishVersion()
  {
    JsonObject json = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC, USATestData.COUNTRY);

    MasterList test = MasterList.create(json);

    try
    {
      MasterListVersion version = test.getOrCreateVersion(new Date(), MasterListVersion.EXPLORATORY);

      try
      {
        MdBusinessDAOIF mdTable = MdBusinessDAO.get(version.getMdBusinessOid());

        Assert.assertNotNull(mdTable);

        version.publish();
      }
      finally
      {
        version.delete();
      }
    }
    finally
    {
      test.delete();
    }
  }

  @Test
  public void testCreatePublishedVersions()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC, USATestData.COUNTRY);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);
    String oid = result.get(ComponentInfo.OID).getAsString();

    try
    {
      service.createPublishedVersions(testData.clientRequest.getSessionId(), oid);

      final JsonObject object = service.getVersions(testData.clientRequest.getSessionId(), oid, MasterListVersion.PUBLISHED);
      final JsonArray json = object.get(MasterList.VERSIONS).getAsJsonArray();

      Assert.assertEquals(1, json.size());
    }
    finally
    {
      service.remove(testData.clientRequest.getSessionId(), oid);
    }
  }

  @Test(expected = SmartExceptionDTO.class)
  public void testCreateFromOtherOrg()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC, USATestData.COUNTRY);

    USATestData.runAsUser(USATestData.USER_PPP_RA, (request, adapter) -> {

      MasterListService service = new MasterListService();
      service.create(request.getSessionId(), listJson);
    });
  }

  @Test(expected = SmartExceptionDTO.class)
  public void testRemoveFromOtherOrg()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC, USATestData.COUNTRY);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);
    String oid = result.get(ComponentInfo.OID).getAsString();

    try
    {
      USATestData.runAsUser(USATestData.USER_PPP_RA, (request, adapter) -> {

        service.remove(request.getSessionId(), oid);
      });
    }
    finally
    {
      service.remove(testData.clientRequest.getSessionId(), oid);
    }
  }

  @Test(expected = SmartExceptionDTO.class)
  public void testCreatePublishedVersionsFromOtherOrg()
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC, USATestData.COUNTRY);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);
    String oid = result.get(ComponentInfo.OID).getAsString();

    try
    {
      USATestData.runAsUser(USATestData.USER_PPP_RA, (request, adapter) -> {

        service.createPublishedVersions(request.getSessionId(), oid);
      });
    }
    finally
    {
      service.remove(testData.clientRequest.getSessionId(), oid);
    }

  }

  @Test
  public void testGetTile() throws IOException
  {
    JsonObject listJson = getJson(USATestData.ORG_NPS.getServerObject(), USATestData.HIER_ADMIN, USATestData.STATE, MasterList.PUBLIC, USATestData.COUNTRY);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);
    String oid = result.get(ComponentInfo.OID).getAsString();

    try
    {
      service.createPublishedVersions(testData.clientRequest.getSessionId(), oid);

      final JsonObject object = service.getVersions(testData.clientRequest.getSessionId(), oid, MasterListVersion.PUBLISHED);
      final JsonArray json = object.get(MasterList.VERSIONS).getAsJsonArray();

      Assert.assertEquals(1, json.size());

      String versionId = json.get(0).getAsJsonObject().get("oid").getAsString();

      JSONObject tileObj = new JSONObject();
      tileObj.put("oid", versionId);
      tileObj.put("x", 1);
      tileObj.put("y", 1);
      tileObj.put("z", 1);

      try (InputStream tile = service.getTile(testData.clientRequest.getSessionId(), tileObj))
      {
        Assert.assertNotNull(tile);

        byte[] ctile = getCachedTile(versionId);

        Assert.assertNotNull(ctile);
        Assert.assertTrue(ctile.length > 0);
      }
    }
    finally
    {
      service.remove(testData.clientRequest.getSessionId(), oid);
    }
  }

  @Request
  private byte[] getCachedTile(String versionId)
  {
    return TileCache.getCachedTile(versionId, 1, 1, 1);
  }

  @Test
  @Request
  public void testGetAnnualFrequencyDates()
  {
    final MasterList list = new MasterList();
    list.addFrequency(ChangeFrequency.ANNUAL);

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    calendar.clear();
    calendar.set(2012, Calendar.MARCH, 3);

    final Date startDate = calendar.getTime();

    calendar.set(2017, Calendar.OCTOBER, 21);

    final Date endDate = calendar.getTime();

    List<Date> dates = list.getFrequencyDates(startDate, endDate);

    Assert.assertEquals(6, dates.size());

    for (int i = 0; i < dates.size(); i++)
    {
      calendar.clear();
      calendar.set( ( 2012 + i ), Calendar.DECEMBER, 31);

      Assert.assertEquals(calendar.getTime(), dates.get(i));
    }
  }

  @Test
  @Request
  public void testGetQuarterFrequencyDates()
  {
    final MasterList list = new MasterList();
    list.addFrequency(ChangeFrequency.QUARTER);

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    calendar.clear();
    calendar.set(2012, Calendar.MARCH, 3);

    final Date startDate = calendar.getTime();

    calendar.set(2013, Calendar.JANUARY, 2);

    final Date endDate = calendar.getTime();

    List<Date> dates = list.getFrequencyDates(startDate, endDate);

    Assert.assertEquals(5, dates.size());
  }

  @Test
  @Request
  public void testGetMonthFrequencyDates()
  {
    final MasterList list = new MasterList();
    list.addFrequency(ChangeFrequency.MONTHLY);

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    calendar.clear();
    calendar.set(2012, Calendar.MARCH, 3);

    final Date startDate = calendar.getTime();

    calendar.set(2013, Calendar.JANUARY, 2);

    final Date endDate = calendar.getTime();

    List<Date> dates = list.getFrequencyDates(startDate, endDate);

    Assert.assertEquals(11, dates.size());
  }

  @Request
  public static JsonObject getJson(Organization org, TestHierarchyTypeInfo ht, TestGeoObjectTypeInfo info, String visibility, TestGeoObjectTypeInfo... parents)
  {
    JsonArray pArray = new JsonArray();
    for (TestGeoObjectTypeInfo parent : parents)
    {
      JsonObject object = new JsonObject();
      object.addProperty("code", parent.getCode());
      object.addProperty("selected", true);

      pArray.add(object);
    }

    JsonObject hierarchy = new JsonObject();
    hierarchy.addProperty("code", ht.getCode());
    hierarchy.add("parents", pArray);

    JsonArray array = new JsonArray();
    array.add(hierarchy);

    MasterList list = new MasterList();
    list.setUniversal(info.getUniversal());
    list.getDisplayLabel().setValue("Test List");
    list.setCode("TEST_CODE");
    list.setRepresentativityDate(new Date());
    list.setPublishDate(new Date());
    list.setListAbstract("My Abstract");
    list.setProcess("Process");
    list.setProgress("Progress");
    list.setAccessConstraints("Access Contraints");
    list.setUseConstraints("User Constraints");
    list.setAcknowledgements("Acknowledgements");
    list.setDisclaimer("Disclaimer");
    list.setContactName("Contact Name");
    list.setOrganization(org);
    list.setTelephoneNumber("Telephone Number");
    list.setEmail("Email");
    list.setHierarchies(array.toString());
    list.addFrequency(ChangeFrequency.ANNUAL);
    list.setIsMaster(false);
    list.setVisibility(visibility);

    return list.toJSON();
  }

}
