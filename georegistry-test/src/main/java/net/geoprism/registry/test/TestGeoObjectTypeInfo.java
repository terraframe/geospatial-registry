package net.geoprism.registry.test;

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.FrequencyType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.junit.Assert;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.registry.AttributeHierarchy;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.ServerGeoObjectTypeConverter;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.ServiceFactory;
import net.geoprism.registry.service.WMSService;

public class TestGeoObjectTypeInfo
  {
    private final TestDataSet testDataSet;

    private Universal                   universal;

    private String                      code;

    private LocalizedValue              displayLabel;

    private LocalizedValue              description;

    private String                      uid;

    private GeometryType                geomType;

    private boolean                     isLeaf;

    private List<TestGeoObjectTypeInfo> children;
    
    private FrequencyType             frequency;

    protected TestGeoObjectTypeInfo(TestDataSet testDataSet, String genKey)
    {
      this(testDataSet, genKey, false);
    }

    protected TestGeoObjectTypeInfo(TestDataSet testDataSet, String genKey, boolean isLeaf)
    {
      this.testDataSet = testDataSet;
      this.code = this.testDataSet.getTestDataKey() + genKey + "Code";
      this.displayLabel = new LocalizedValue(this.testDataSet.getTestDataKey() + " " + genKey + " Display Label");
      this.description = new LocalizedValue(this.testDataSet.getTestDataKey() + " " + genKey + " Description");
      this.children = new LinkedList<TestGeoObjectTypeInfo>();
      this.geomType = GeometryType.POLYGON;
      this.isLeaf = isLeaf;
      this.frequency = FrequencyType.DAILY;
    }

    public String getCode()
    {
      return code;
    }

    public LocalizedValue getDisplayLabel()
    {
      return displayLabel;
    }

    public LocalizedValue getDescription()
    {
      return description;
    }

    public GeometryType getGeometryType()
    {
      return geomType;
    }

    public boolean getIsLeaf()
    {
      return isLeaf;
    }

    public String getUid()
    {
      return uid;
    }

    public void setUid(String uid)
    {
      this.uid = uid;
    }

    public Universal getUniversal()
    {
      return this.universal;
    }
    
    public ServerGeoObjectType getServerObject()
    {
      Optional<GeoObjectType> got = ServiceFactory.getAdapter().getMetadataCache().getGeoObjectType(code);
      
      if (got.isPresent())
      {
        return ServerGeoObjectType.get(getCode());
      }
      
      return null;
    }

    public List<TestGeoObjectTypeInfo> getChildren()
    {
      return this.children;
    }

    public Relationship addChild(TestGeoObjectTypeInfo child, String relationshipType)
    {
      if (!this.children.contains(child))
      {
        this.children.add(child);
      }

      return child.getUniversal().addLink(universal, relationshipType);
    }

    public void assertEquals(GeoObjectType got)
    {
      Assert.assertEquals(code, got.getCode());
      Assert.assertEquals(displayLabel.getValue(), got.getLabel().getValue());
      Assert.assertEquals(description.getValue(), got.getDescription().getValue());
    }

    public void assertEquals(Universal uni)
    {
      Assert.assertEquals(code, uni.getKey());
      Assert.assertEquals(displayLabel.getValue(), uni.getDisplayLabel().getValue());
      Assert.assertEquals(description.getValue(), uni.getDescription().getValue());
    }

    @Request
    public void apply(GeometryType geometryType)
    {
      ServerGeoObjectType type = applyInTrans(geometryType);

      // If this did not error out then add to the cache
      this.testDataSet.adapter.getMetadataCache().addGeoObjectType(type.getType());
    }

    @Transaction
    private ServerGeoObjectType applyInTrans(GeometryType geometryType)
    {
      if (this.testDataSet.debugMode >= 1)
      {
        System.out.println("Applying TestGeoObjectTypeInfo [" + this.getCode() + "].");
      }

      GeoObjectType got = new GeoObjectType(this.getCode(), geometryType, this.getDisplayLabel(), this.getDescription(), this.getIsLeaf(), true, frequency, this.testDataSet.adapter);
//      ServerGeoObjectType type = new ServerGeoObjectTypeBuilder().create(got);
      ServerGeoObjectType type = new ServerGeoObjectTypeConverter().create(got);

      universal = type.getUniversal();

      this.setUid(universal.getOid());

      return type;
    }

    @Request
    public void delete()
    {
      deleteInTrans();
    }

    @Transaction
    private void deleteInTrans()
    {
      if (this.testDataSet.debugMode >= 1)
      {
        System.out.println("Deleting TestGeoObjectTypeInfo [" + this.getCode() + "].");
      }

      Universal uni = this.testDataSet.getUniversalIfExist(this.getCode());
      MdClass mdBiz = this.testDataSet.getMdClassIfExist(RegistryConstants.UNIVERSAL_MDBUSINESS_PACKAGE, this.code);
      if (mdBiz != null)
      {
        AttributeHierarchy.deleteByMdBusiness(MdBusinessDAO.get(mdBiz.getOid()));
      }
      new WMSService().deleteDatabaseViewIfExists(this.getCode());
      if (uni != null)
      {
        this.testDataSet.deleteUniversal(this.getCode());
      }
      if (mdBiz != null)
      {
        this.testDataSet.deleteMdClass(RegistryConstants.UNIVERSAL_MDBUSINESS_PACKAGE, this.code);
      }
      MdClass geoVertexType = this.testDataSet.getMdClassIfExist(RegistryConstants.UNIVERSAL_GRAPH_PACKAGE, this.code);
      if (geoVertexType != null)
      {
        this.testDataSet.deleteMdClass(RegistryConstants.UNIVERSAL_GRAPH_PACKAGE, this.code);
      }

      this.children.clear();
      this.universal = null;
    }

    public ServerGeoObjectType getGeoObjectType(GeometryType geometryType)
    {
      // if (this.getUniversal() != null)
      // {
      // return
      // registryService.getConversionService().universalToGeoObjectType(this.getUniversal());
      // }
      // else
      // {
      return ServerGeoObjectType.get(this.getCode());
      // }
    }
    
    public boolean isPersisted()
    {
      return ServiceFactory.getAdapter().getMetadataCache().getGeoObjectType(code).isPresent();
    }
  }