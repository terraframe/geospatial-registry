/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.hierarchy;

import java.util.List;

import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyNode;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.session.Request;

import net.geoprism.registry.HierarchyRootException;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.test.FastTestDataset;
import net.geoprism.registry.test.TestGeoObjectTypeInfo;
import net.geoprism.registry.test.TestHierarchyTypeInfo;

public class InheritedHierarchyAnnotationTest
{
  public static final TestGeoObjectTypeInfo TEST_CHILD = new TestGeoObjectTypeInfo("HMST_PROVINCE", FastTestDataset.ORG_CGOV);

  public static final TestHierarchyTypeInfo TEST_HT    = new TestHierarchyTypeInfo("HMST_ReportDiv", FastTestDataset.ORG_CGOV);

  protected static FastTestDataset          testData;

  @BeforeClass
  @Request
  public static void setUpClass()
  {
    testData = FastTestDataset.newTestData();
    testData.setUpMetadata();

    TEST_CHILD.apply();
    TEST_HT.apply();

    TEST_HT.setRoot(FastTestDataset.PROVINCE);
    TEST_HT.getServerObject().addToHierarchy(FastTestDataset.PROVINCE.getServerObject(), TEST_CHILD.getServerObject());
  }

  @AfterClass
  @Request
  public static void cleanUpClass()
  {
    TEST_HT.getServerObject().removeChild(FastTestDataset.PROVINCE.getServerObject(), TEST_CHILD.getServerObject(), true);
    TEST_HT.removeRoot(FastTestDataset.PROVINCE);

    TEST_HT.delete();
    TEST_CHILD.delete();

    testData.tearDownMetadata();
  }

  @Test
  @Request
  public void testCreate()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(TEST_HT.getServerObject(), FastTestDataset.HIER_ADMIN.getServerObject());

    try
    {
      Assert.assertNotNull(annotation);
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testGetByUniversal()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(TEST_HT.getServerObject(), FastTestDataset.HIER_ADMIN.getServerObject());

    try
    {
      List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByUniversal(sGOT.getUniversal());

      Assert.assertEquals(1, annotations.size());

      InheritedHierarchyAnnotation test = annotations.get(0);

      Assert.assertEquals(test.getOid(), annotation.getOid());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testGetByForHierarchy()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);

    try
    {
      List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByRelationship(forHierarchy.getUniversalRelationship());

      Assert.assertEquals(1, annotations.size());

      InheritedHierarchyAnnotation test = annotations.get(0);

      Assert.assertEquals(test.getOid(), annotation.getOid());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testGetByInheritedHierarchy()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);

    try
    {
      List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByRelationship(inheritedHierarchy.getUniversalRelationship());

      Assert.assertEquals(1, annotations.size());

      InheritedHierarchyAnnotation test = annotations.get(0);

      Assert.assertEquals(test.getOid(), annotation.getOid());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testGetByUniversalAndHierarchy()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);

    try
    {
      InheritedHierarchyAnnotation test = InheritedHierarchyAnnotation.get(sGOT.getUniversal(), forHierarchy.getUniversalRelationship());

      Assert.assertNotNull(test);
      Assert.assertEquals(test.getOid(), annotation.getOid());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testRemove()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);
    sGOT.removeInheritedHierarchy(forHierarchy);

    InheritedHierarchyAnnotation test = InheritedHierarchyAnnotation.get(sGOT.getUniversal(), forHierarchy.getUniversalRelationship());

    try
    {
      Assert.assertNull(test);
    }
    finally
    {
      if (test != null)
      {
        test.delete();
      }
    }
  }

  @Request
  @Test(expected = HierarchyRootException.class)
  public void testCreateOnNonRoot()
  {
    ServerGeoObjectType sGOT = TEST_CHILD.getServerObject();
    sGOT.setInheritedHierarchy(TEST_HT.getServerObject(), FastTestDataset.HIER_ADMIN.getServerObject());
  }

  @Test
  @Request
  public void testGetTypeAncestorsInherited()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);

    try
    {
      ServerGeoObjectType childType = TEST_CHILD.getServerObject();

      List<GeoObjectType> results = childType.getTypeAncestors(TEST_HT.getServerObject(), true);

      Assert.assertEquals(2, results.size());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testGetTypeAncestors()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);

    try
    {
      ServerGeoObjectType childType = TEST_CHILD.getServerObject();

      List<GeoObjectType> results = childType.getTypeAncestors(TEST_HT.getServerObject(), false);

      Assert.assertEquals(1, results.size());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  @Request
  public void testFindHierarchy()
  {
    ServerGeoObjectType sGOT = FastTestDataset.PROVINCE.getServerObject();
    ServerHierarchyType forHierarchy = TEST_HT.getServerObject();
    ServerHierarchyType inheritedHierarchy = FastTestDataset.HIER_ADMIN.getServerObject();

    InheritedHierarchyAnnotation annotation = sGOT.setInheritedHierarchy(forHierarchy, inheritedHierarchy);

    try
    {
      ServerGeoObjectType childType = TEST_CHILD.getServerObject();

      ServerHierarchyType hierarchy = childType.findHierarchy(forHierarchy, FastTestDataset.COUNTRY.getServerObject());

      Assert.assertEquals(FastTestDataset.HIER_ADMIN.getCode(), hierarchy.getCode());
    }
    finally
    {
      annotation.delete();
    }
  }

  @Test
  public void testSetInheritedHierarchy()
  {
    FastTestDataset.runAsUser(FastTestDataset.USER_CGOV_RA, (request, adapter) -> {

      HierarchyService service = new HierarchyService();

      try
      {
        HierarchyType ht = service.setInheritedHierarchy(request.getSessionId(), TEST_HT.getCode(), FastTestDataset.HIER_ADMIN.getCode(), FastTestDataset.PROVINCE.getCode());

        List<HierarchyNode> nodes = ht.getRootGeoObjectTypes();
        HierarchyNode node = nodes.get(0);
        GeoObjectType root = node.getGeoObjectType();

        Assert.assertEquals(FastTestDataset.COUNTRY.getCode(), root.getCode());
        Assert.assertEquals(FastTestDataset.HIER_ADMIN.getCode(), node.getInheritedHierarchyCode());
      }
      finally
      {
        HierarchyType ht = service.removeInheritedHierarchy(request.getSessionId(), TEST_HT.getCode(), FastTestDataset.PROVINCE.getCode());

        List<HierarchyNode> nodes = ht.getRootGeoObjectTypes();
        HierarchyNode node = nodes.get(0);
        GeoObjectType root = node.getGeoObjectType();

        Assert.assertEquals(FastTestDataset.PROVINCE.getCode(), root.getCode());
        Assert.assertNull(node.getInheritedHierarchyCode());
      }
    });
  }

}
