package net.geoprism.georegistry.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.action.AbstractActionDTO;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.IsARelationship;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdTermRelationship;
import com.runwaysdk.system.metadata.MdTermRelationshipQuery;
import com.runwaysdk.system.ontology.TermUtil;

import net.geoprism.georegistry.action.AbstractAction;
import net.geoprism.georegistry.action.AllGovernanceStatus;
import net.geoprism.georegistry.action.ChangeRequest;
import net.geoprism.georegistry.conversion.TermBuilder;
import net.geoprism.georegistry.query.GeoObjectIterator;
import net.geoprism.georegistry.query.GeoObjectQuery;
import net.geoprism.georegistry.query.LookupRestriction;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.AttributeHierarhcy;
import net.geoprism.registry.GeoRegistryUtil;
import net.geoprism.registry.NoChildForLeafGeoObjectType;

import net.geoprism.georegistry.service.WMSService;

public class RegistryService
{
  private RegistryAdapter adapter;

  protected RegistryService()
  {
  }

  public static RegistryService getInstance()
  {
    return ServiceFactory.getRegistryService();
  }

  @Request
  public synchronized void initialize(RegistryAdapter adapter)
  {
    this.adapter = adapter;
    refreshMetadataCache();
  }

  public void refreshMetadataCache()
  {
    adapter.getMetadataCache().rebuild();

    QueryFactory qf = new QueryFactory();
    UniversalQuery uq = new UniversalQuery(qf);
    OIterator<? extends Universal> it = uq.getIterator();

    try
    {
      while (it.hasNext())
      {
        Universal uni = it.next();

        GeoObjectType got = ServiceFactory.getConversionService().universalToGeoObjectType(uni);

        adapter.getMetadataCache().addGeoObjectType(got);
      }
    }
    finally
    {
      it.close();
    }

    MdBusiness univMdBusiness = MdBusiness.getMdBusiness(Universal.CLASS);

    MdTermRelationshipQuery trq = new MdTermRelationshipQuery(qf);
    trq.WHERE(trq.getParentMdBusiness().EQ(univMdBusiness).AND(trq.getChildMdBusiness().EQ(univMdBusiness)));

    OIterator<? extends MdTermRelationship> it2 = trq.getIterator();

    try
    {
      while (it2.hasNext())
      {
        MdTermRelationship mdTermRel = it2.next();

        // Ignore the IsARelationship class between universals. It should be
        // depricated
        if (mdTermRel.definesType().equals(IsARelationship.CLASS))
        {
          continue;
        }

        HierarchyType ht = ServiceFactory.getConversionService().mdTermRelationshipToHierarchyType(mdTermRel);

        adapter.getMetadataCache().addHierarchyType(ht);
      }
    }
    finally
    {
      it2.close();
    }
  }

  @Request(RequestType.SESSION)
  public GeoObject getGeoObject(String sessionId, String uid, String geoObjectTypeCode)
  {
    return ServiceFactory.getUtilities().getGeoObjectById(uid, geoObjectTypeCode);
  }

  @Request(RequestType.SESSION)
  public GeoObject getGeoObjectByCode(String sessionId, String code, String typeCode)
  {
    return ServiceFactory.getUtilities().getGeoObjectByCode(code, typeCode);
  }

  @Request(RequestType.SESSION)
  public GeoObject createGeoObject(String sessionId, String jGeoObj)
  {
    return createGeoObjectInTransaction(sessionId, jGeoObj);
  }

  @Transaction
  private GeoObject createGeoObjectInTransaction(String sessionId, String jGeoObj)
  {
    GeoObject geoObject = GeoObject.fromJSON(adapter, jGeoObj);

    return ServiceFactory.getUtilities().applyGeoObject(geoObject, true);
  }

  @Request(RequestType.SESSION)
  public GeoObject updateGeoObject(String sessionId, String jGeoObj)
  {
    return updateGeoObjectInTransaction(sessionId, jGeoObj);
  }

  @Transaction
  private GeoObject updateGeoObjectInTransaction(String sessionId, String jGeoObj)
  {
    GeoObject geoObject = GeoObject.fromJSON(adapter, jGeoObj);

    return ServiceFactory.getUtilities().applyGeoObject(geoObject, false);
  }

  @Request(RequestType.SESSION)
  public String[] getUIDS(String sessionId, Integer amount)
  {
    return RegistryIdService.getInstance().getUids(amount);
  }

  @Request(RequestType.SESSION)
  public List<GeoObjectType> getAncestors(String sessionId, String code, String hierarchyCode)
  {
    GeoObjectType child = this.adapter.getMetadataCache().getGeoObjectType(code).get();

    return ServiceFactory.getUtilities().getAncestors(child, hierarchyCode);
  }

  @Request(RequestType.SESSION)
  public ChildTreeNode getChildGeoObjects(String sessionId, String parentUid, String parentGeoObjectTypeCode, String[] childrenTypes, Boolean recursive)
  {
    GeoObject goParent = ServiceFactory.getUtilities().getGeoObjectById(parentUid, parentGeoObjectTypeCode);

    if (goParent.getType().isLeaf())
    {
      throw new UnsupportedOperationException("Leaf nodes cannot have children.");
    }

    String parentRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goParent.getUid(), goParent.getType());

    String[] relationshipTypes = TermUtil.getAllParentRelationships(parentRunwayId);
    Map<String, HierarchyType> htMap = getHierarchyTypeMap(relationshipTypes);
    GeoEntity parent = GeoEntity.get(parentRunwayId);

    GeoObject goRoot = ServiceFactory.getConversionService().geoEntityToGeoObject(parent);
    ChildTreeNode tnRoot = new ChildTreeNode(goRoot, null);

    /*
     * Handle leaf node children
     */
    for (int i = 0; i < childrenTypes.length; ++i)
    {
      GeoObjectType childType = this.adapter.getMetadataCache().getGeoObjectType(childrenTypes[i]).get();

      if (childType.isLeaf())
      {
        Universal universal = ServiceFactory.getConversionService().getUniversalFromGeoObjectType(childType);

        if (ArrayUtils.contains(childrenTypes, universal.getKey()))
        {
          MdBusinessDAOIF mdBusiness = MdBusinessDAO.get(universal.getMdBusinessOid());

          List<MdAttributeDAOIF> mdAttributes = mdBusiness.definesAttributes().stream().filter(mdAttribute -> {
            if (mdAttribute instanceof MdAttributeReferenceDAOIF)
            {
              MdBusinessDAOIF referenceMdBusiness = ( (MdAttributeReferenceDAOIF) mdAttribute ).getReferenceMdBusinessDAO();

              if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
              {
                return true;
              }
            }

            return false;
          }).collect(Collectors.toList());

          for (MdAttributeDAOIF mdAttribute : mdAttributes)
          {
            HierarchyType ht = AttributeHierarhcy.getHierarchyType(mdAttribute.getKey());

            BusinessQuery query = new QueryFactory().businessQuery(mdBusiness.definesType());
            query.WHERE(query.get(mdAttribute.definesAttribute()).EQ(parentRunwayId));

            OIterator<Business> it = query.getIterator();

            try
            {
              List<Business> children = it.getAll();

              for (Business child : children)
              {
                // Do something
                GeoObject goChild = ServiceFactory.getConversionService().leafToGeoObject(childType, child);

                tnRoot.addChild(new ChildTreeNode(goChild, ht));
              }
            }
            finally
            {
              it.close();
            }
          }
        }
      }
    }

    /*
     * Handle tree node children
     */
    TermAndRel[] tnrChildren = TermUtil.getDirectDescendants(parentRunwayId, relationshipTypes);
    for (TermAndRel tnrChild : tnrChildren)
    {
      GeoEntity geChild = (GeoEntity) tnrChild.getTerm();
      Universal uni = geChild.getUniversal();

      if (ArrayUtils.contains(childrenTypes, uni.getKey()))
      {
        GeoObject goChild = ServiceFactory.getConversionService().geoEntityToGeoObject(geChild);
        HierarchyType ht = htMap.get(tnrChild.getRelationshipType());

        ChildTreeNode tnChild;
        if (recursive)
        {
          tnChild = this.getChildGeoObjects(sessionId, goChild.getUid(), goChild.getType().getCode(), childrenTypes, recursive);
        }
        else
        {
          tnChild = new ChildTreeNode(goChild, ht);
        }

        tnRoot.addChild(tnChild);
      }
    }

    return tnRoot;
  }

  private Map<String, HierarchyType> getHierarchyTypeMap(String[] relationshipTypes)
  {
    Map<String, HierarchyType> map = new HashMap<String, HierarchyType>();

    for (String relationshipType : relationshipTypes)
    {
      MdTermRelationship mdRel = (MdTermRelationship) MdTermRelationship.getMdRelationship(relationshipType);

      HierarchyType ht = ServiceFactory.getConversionService().mdTermRelationshipToHierarchyType(mdRel);

      map.put(relationshipType, ht);
    }

    return map;
  }

  @Request(RequestType.SESSION)
  public ParentTreeNode getParentGeoObjects(String sessionId, String childId, String childGeoObjectTypeCode, String[] parentTypes, boolean recursive)
  {
    GeoObject goChild = ServiceFactory.getUtilities().getGeoObjectById(childId, childGeoObjectTypeCode);
    String childRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goChild.getUid(), goChild.getType());

    ParentTreeNode tnRoot = new ParentTreeNode(goChild, null);

    if (goChild.getType().isLeaf())
    {
      Business business = Business.get(childRunwayId);

      List<MdAttributeDAOIF> mdAttributes = business.getMdAttributeDAOs().stream().filter(mdAttribute -> {
        if (mdAttribute instanceof MdAttributeReferenceDAOIF)
        {
          MdBusinessDAOIF referenceMdBusiness = ( (MdAttributeReferenceDAOIF) mdAttribute ).getReferenceMdBusinessDAO();

          if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
          {
            return true;
          }
        }

        return false;
      }).collect(Collectors.toList());

      mdAttributes.forEach(mdAttribute -> {

        String parentRunwayId = business.getValue(mdAttribute.definesAttribute());

        if (parentRunwayId != null && parentRunwayId.length() > 0)
        {
          GeoEntity geParent = GeoEntity.get(parentRunwayId);
          GeoObject goParent = ServiceFactory.getConversionService().geoEntityToGeoObject(geParent);
          Universal uni = geParent.getUniversal();

          if (ArrayUtils.contains(parentTypes, uni.getKey()))
          {
            ParentTreeNode tnParent;

            if (recursive)
            {
              tnParent = this.getParentGeoObjects(sessionId, goParent.getUid(), goParent.getType().getCode(), parentTypes, recursive);
            }
            else
            {
              HierarchyType ht = AttributeHierarhcy.getHierarchyType(mdAttribute.getKey());

              tnParent = new ParentTreeNode(goParent, ht);
            }

            tnRoot.addParent(tnParent);
          }
        }
      });

    }
    else
    {

      String[] relationshipTypes = TermUtil.getAllChildRelationships(childRunwayId);

      Map<String, HierarchyType> htMap = getHierarchyTypeMap(relationshipTypes);

      TermAndRel[] tnrParents = TermUtil.getDirectAncestors(childRunwayId, relationshipTypes);
      for (TermAndRel tnrParent : tnrParents)
      {
        GeoEntity geParent = (GeoEntity) tnrParent.getTerm();
        Universal uni = geParent.getUniversal();

        if (ArrayUtils.contains(parentTypes, uni.getKey()))
        {
          GeoObject goParent = ServiceFactory.getConversionService().geoEntityToGeoObject(geParent);
          HierarchyType ht = htMap.get(tnrParent.getRelationshipType());

          ParentTreeNode tnParent;
          if (recursive)
          {
            tnParent = this.getParentGeoObjects(sessionId, goParent.getUid(), goParent.getType().getCode(), parentTypes, recursive);
          }
          else
          {
            tnParent = new ParentTreeNode(goParent, ht);
          }

          tnRoot.addParent(tnParent);
        }
      }
    }

    return tnRoot;
  }

  @Request(RequestType.SESSION)
  public ParentTreeNode addChild(String sessionId, String parentId, String parentGeoObjectTypeCode, String childId, String childGeoObjectTypeCode, String hierarchyCode)
  {
    return addChildInTransaction(parentId, parentGeoObjectTypeCode, childId, childGeoObjectTypeCode, hierarchyCode);
  }

  @Transaction
  public ParentTreeNode addChildInTransaction(String parentId, String parentGeoObjectTypeCode, String childId, String childGeoObjectTypeCode, String hierarchyCode)
  {
    GeoObject goParent = ServiceFactory.getUtilities().getGeoObjectById(parentId, parentGeoObjectTypeCode);
    GeoObject goChild = ServiceFactory.getUtilities().getGeoObjectById(childId, childGeoObjectTypeCode);
    HierarchyType hierarchy = adapter.getMetadataCache().getHierachyType(hierarchyCode).get();

    if (goParent.getType().isLeaf())
    {
      throw new UnsupportedOperationException("Virtual leaf nodes cannot have children.");
    }
    else if (goChild.getType().isLeaf())
    {
      String parentRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goParent.getUid(), goParent.getType());
      String childRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goChild.getUid(), goChild.getType());

      GeoEntity parent = GeoEntity.get(parentRunwayId);
      Business child = Business.get(childRunwayId);

      Universal parentUniversal = parent.getUniversal();
      String refAttrName = ConversionService.getParentReferenceAttributeName(hierarchyCode, parentUniversal);

      child.appLock();
      child.setValue(refAttrName, parent.getOid());
      child.apply();

      ParentTreeNode node = new ParentTreeNode(goChild, hierarchy);
      node.addParent(new ParentTreeNode(goParent, hierarchy));

      return node;
    }
    else
    {
      String parentRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goParent.getUid(), goParent.getType());
      GeoEntity geParent = GeoEntity.get(parentRunwayId);

      String childRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goChild.getUid(), goChild.getType());
      GeoEntity geChild = GeoEntity.get(childRunwayId);

      String mdTermRelGeoEntity = ConversionService.buildMdTermRelGeoEntityKey(hierarchyCode);

      geChild.addLink(geParent, mdTermRelGeoEntity);

      ParentTreeNode node = new ParentTreeNode(goChild, hierarchy);
      node.addParent(new ParentTreeNode(goParent, hierarchy));

      return node;
    }
  }

  public Boolean exists(String parentId, String parentGeoObjectTypeCode, String childId, String childGeoObjectTypeCode, String hierarchyCode)
  {
    GeoObject goParent = ServiceFactory.getUtilities().getGeoObjectById(parentId, parentGeoObjectTypeCode);
    GeoObject goChild = ServiceFactory.getUtilities().getGeoObjectById(childId, childGeoObjectTypeCode);

    if (goParent.getType().isLeaf())
    {
      throw new UnsupportedOperationException("Virtual leaf nodes cannot have children.");
    }
    else if (goChild.getType().isLeaf())
    {
      return false;
    }
    else
    {
      String parentRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goParent.getUid(), goParent.getType());
      String childRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goChild.getUid(), goChild.getType());

      String mdTermRelGeoEntity = ConversionService.buildMdTermRelGeoEntityKey(hierarchyCode);

      RelationshipQuery query = new QueryFactory().relationshipQuery(mdTermRelGeoEntity);
      query.WHERE(query.parentOid().EQ(parentRunwayId));
      query.AND(query.childOid().EQ(childRunwayId));

      return ( query.getCount() > 0 );
    }
  }

  @Request(RequestType.SESSION)
  public void removeChild(String sessionId, String parentId, String parentGeoObjectTypeCode, String childId, String childGeoObjectTypeCode, String hierarchyCode)
  {
    removeChildInTransaction(parentId, parentGeoObjectTypeCode, childId, childGeoObjectTypeCode, hierarchyCode);
  }

  @Transaction
  public void removeChildInTransaction(String parentId, String parentGeoObjectTypeCode, String childId, String childGeoObjectTypeCode, String hierarchyCode)
  {
    GeoObject goParent = ServiceFactory.getUtilities().getGeoObjectById(parentId, parentGeoObjectTypeCode);
    GeoObject goChild = ServiceFactory.getUtilities().getGeoObjectById(childId, childGeoObjectTypeCode);

    if (goParent.getType().isLeaf())
    {
      throw new UnsupportedOperationException("Virtual leaf nodes cannot have children.");
    }
    else if (goChild.getType().isLeaf())
    {
      String parentRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goParent.getUid(), goParent.getType());
      String childRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goChild.getUid(), goChild.getType());

      GeoEntity parent = GeoEntity.get(parentRunwayId);
      Business child = Business.get(childRunwayId);

      Universal parentUniversal = parent.getUniversal();
      String refAttrName = ConversionService.getParentReferenceAttributeName(hierarchyCode, parentUniversal);

      child.appLock();
      child.setValue(refAttrName, null);
      child.apply();
    }
    else
    {
      String parentRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goParent.getUid(), goParent.getType());
      GeoEntity geParent = GeoEntity.get(parentRunwayId);

      String childRunwayId = RegistryIdService.getInstance().registryIdToRunwayId(goChild.getUid(), goChild.getType());
      GeoEntity geChild = GeoEntity.get(childRunwayId);

      String mdTermRelGeoEntity = ConversionService.buildMdTermRelGeoEntityKey(hierarchyCode);

      geChild.removeLink(geParent, mdTermRelGeoEntity);
    }
  }

  @Request(RequestType.SESSION)
  public void submitChangeRequest(String sessionId, String sJson)
  {
    submitChangeRequestInTransaction(sessionId, sJson);
  }

  @Transaction
  private void submitChangeRequestInTransaction(String sessionId, String sJson)
  {
    ChangeRequest cr = new ChangeRequest();
    cr.addApprovalStatus(AllGovernanceStatus.PENDING);
    cr.apply();

    List<AbstractActionDTO> actionDTOs = AbstractActionDTO.parseActions(sJson);

    for (AbstractActionDTO actionDTO : actionDTOs)
    {
      AbstractAction ra = AbstractAction.dtoToRegistry(actionDTO);
      ra.addApprovalStatus(AllGovernanceStatus.PENDING);
      ra.apply();

      cr.addAction(ra).apply();
    }
  }

  public GeoObjectQuery createQuery(String typeCode)
  {
    GeoObjectType type = ServiceFactory.getAdapter().getMetadataCache().getGeoObjectType(typeCode).get();
    Universal universal = ServiceFactory.getConversionService().getUniversalFromGeoObjectType(type);

    return new GeoObjectQuery(type, universal);
  }

  ///////////////////// Hierarchy Management /////////////////////

  /**
   * Returns the {@link GeoObjectType}s with the given codes or all
   * {@link GeoObjectType}s if no codes are provided.
   * 
   * @param sessionId
   * @param codes
   *          codes of the {@link GeoObjectType}s.
   * @return the {@link GeoObjectType}s with the given codes or all
   *         {@link GeoObjectType}s if no codes are provided.
   */
  @Request(RequestType.SESSION)
  public GeoObjectType[] getGeoObjectTypes(String sessionId, String[] codes)
  {
    if (codes == null || codes.length == 0)
    {
      return adapter.getMetadataCache().getAllGeoObjectTypes();
    }

    GeoObjectType[] gots = new GeoObjectType[codes.length];

    for (int i = 0; i < codes.length; ++i)
    {
      gots[i] = adapter.getMetadataCache().getGeoObjectType(codes[i]).get();
    }

    return gots;
  }

  /**
   * Creates a {@link GeoObjectType} from the given JSON.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link GeoObjectType} to be created.
   * @return newly created {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public GeoObjectType createGeoObjectType(String sessionId, String gtJSON)
  {
    GeoObjectType geoObjectType = GeoObjectType.fromJSON(gtJSON, adapter);

    Universal universal = createGeoObjectType(geoObjectType);
    
    new WMSService().createWMSLayer(geoObjectType, true);

    // If this did not error out then add to the cache
    adapter.getMetadataCache().addGeoObjectType(geoObjectType);

    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return ServiceFactory.getConversionService().universalToGeoObjectType(universal);
  }

  @Transaction
  private Universal createGeoObjectType(GeoObjectType geoObjectType)
  {
    Universal universal = ServiceFactory.getUtilities().createGeoObjectType(geoObjectType);

    return universal;
  }

  /**
   * Updates the given {@link GeoObjectType} represented as JSON.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link GeoObjectType} to be updated.
   * @return updated {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public GeoObjectType updateGeoObjectType(String sessionId, String gtJSON)
  {
    GeoObjectType geoObjectTypeNew = GeoObjectType.fromJSON(gtJSON, adapter);

    GeoObjectType geoObjectTypeOld = adapter.getMetadataCache().getGeoObjectType(geoObjectTypeNew.getCode()).get();

    GeoObjectType geoObjectTypeModified = geoObjectTypeOld.copy(geoObjectTypeNew);

    Universal universal = updateGeoObjectType(geoObjectTypeModified);

    GeoObjectType geoObjectTypeModifiedApplied = ServiceFactory.getConversionService().universalToGeoObjectType(universal);

    // If this did not error out then add to the cache
    adapter.getMetadataCache().addGeoObjectType(geoObjectTypeModifiedApplied);

    return geoObjectTypeModifiedApplied;
  }

  @Transaction
  private Universal updateGeoObjectType(GeoObjectType geoObjectType)
  {
    Universal universal = ServiceFactory.getConversionService().getUniversalFromGeoObjectType(geoObjectType);

    universal.lock();
    universal.getDisplayLabel().setValue(geoObjectType.getLocalizedLabel());
    universal.getDescription().setValue(geoObjectType.getLocalizedDescription());
    universal.apply();

    MdBusiness mdBusiness = universal.getMdBusiness();

    mdBusiness.lock();
    mdBusiness.getDisplayLabel().setValue(universal.getDisplayLabel().getValue());
    mdBusiness.getDescription().setValue(universal.getDescription().getValue());
    mdBusiness.apply();

    mdBusiness.unlock();

    universal.unlock();

    return universal;
  }

  /**
   * Adds an attribute to the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   *
   * @param geoObjectTypeCode
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the GeoObjectType
   * @return updated {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public AttributeType createAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON)
  {
    GeoObjectType geoObjectType = adapter.getMetadataCache().getGeoObjectType(geoObjectTypeCode).get();

    JSONObject attrObj = new JSONObject(attributeTypeJSON);

    AttributeType attrType = AttributeType.factory(attrObj.getString(AttributeType.JSON_CODE), attrObj.getString(AttributeType.JSON_LOCALIZED_LABEL), attrObj.getString(AttributeType.JSON_LOCALIZED_DESCRIPTION), attrObj.getString(AttributeType.JSON_TYPE));

    Universal universal = ServiceFactory.getConversionService().geoObjectTypeToUniversal(geoObjectType);

    MdBusiness mdBusiness = universal.getMdBusiness();

    attrType = ServiceFactory.getUtilities().createMdAttributeFromAttributeType(mdBusiness, attrType);

    geoObjectType.addAttribute(attrType);

    // If this did not error out then add to the cache
    adapter.getMetadataCache().addGeoObjectType(geoObjectType);

    return attrType;
  }

  /**
   * Updates an attribute in the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param geoObjectTypeCode
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the GeoObjectType
   * @return updated {@link AttributeType}
   */
  @Request(RequestType.SESSION)
  public AttributeType updateAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON)
  {
    GeoObjectType geoObjectType = adapter.getMetadataCache().getGeoObjectType(geoObjectTypeCode).get();

    JSONObject attrObj = new JSONObject(attributeTypeJSON);

    String attrTypeCode = attrObj.getString(AttributeType.JSON_CODE);

    AttributeType attrType = geoObjectType.getAttribute(attrTypeCode).get();
    attrType.setLocalizedLabel(attrObj.getString(AttributeType.JSON_LOCALIZED_LABEL));
    attrType.setLocalizedDescription(attrObj.getString(AttributeType.JSON_LOCALIZED_DESCRIPTION));

    Universal universal = ServiceFactory.getConversionService().geoObjectTypeToUniversal(geoObjectType);

    MdBusiness mdBusiness = universal.getMdBusiness();

    attrType = ServiceFactory.getUtilities().updateMdAttributeFromAttributeType(mdBusiness, attrType);

    geoObjectType.addAttribute(attrType);

    // If this did not error out then add to the cache
    adapter.getMetadataCache().addGeoObjectType(geoObjectType);

    return attrType;
  }

  /**
   * Deletes an attribute from the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param gtId
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeName
   *          Name of the attribute to be removed from the GeoObjectType
   * @return updated {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public void deleteAttributeType(String sessionId, String gtId, String attributeName)
  {
    GeoObjectType geoObjectType = adapter.getMetadataCache().getGeoObjectType(gtId).get();

    Universal universal = ServiceFactory.getConversionService().geoObjectTypeToUniversal(geoObjectType);

    MdBusiness mdBusiness = universal.getMdBusiness();

    ServiceFactory.getUtilities().deleteMdAttributeFromAttributeType(mdBusiness, attributeName);

    geoObjectType.removeAttribute(attributeName);

    // If this did not error out then add to the cache
    adapter.getMetadataCache().addGeoObjectType(geoObjectType);
  }

  /**
   * Creates a new {@link Term} object and makes it a child of the term with the
   * given code.
   * 
   * @param sessionId
   * @param parentTemCode
   *          The code of the parent [@link Term}.
   * @param termJSON
   *          JSON of the term object.
   * 
   * @return Newly created {@link Term} object.
   */
  @Request(RequestType.SESSION)
  public Term createTerm(String sessionId, String parentTermCode, String termJSON)
  {
    JSONObject termJSONobj = new JSONObject(termJSON);

    Term term = new Term(termJSONobj.getString(Term.JSON_CODE), termJSONobj.getString(Term.JSON_LOCALIZED_LABEL), "");

    Classifier classifier = TermBuilder.createClassifierFromTerm(parentTermCode, term);

    TermBuilder termBuilder = new TermBuilder(classifier.getKeyName());

    Term returnTerm = termBuilder.build();

    // this.refreshMetadataCache();

    return returnTerm;
  }

  /**
   * Creates a new {@link Term} object and makes it a child of the term with the
   * given code.
   * 
   * @param sessionId
   * @param termJSON
   *          JSON of the term object.
   * 
   * @return Updated {@link Term} object.
   */
  @Request(RequestType.SESSION)
  public Term updateTerm(String sessionId, String termJSON)
  {
    JSONObject termJSONobj = new JSONObject(termJSON);

    String termCode = termJSONobj.getString(Term.JSON_CODE);

    String localizedLabel = termJSONobj.getString(Term.JSON_LOCALIZED_LABEL);

    Classifier classifier = TermBuilder.updateClassifier(termCode, localizedLabel);

    TermBuilder termBuilder = new TermBuilder(classifier.getKeyName());

    Term returnTerm = termBuilder.build();

    // this.refreshMetadataCache();

    return returnTerm;
  }

  /**
   * Deletes the {@link Term} with the given code. All children codoe will be
   * deleted.
   * 
   * @param sessionId
   * @param geoObjectTypeCode
   * @param attributeTypeJSON
   */
  @Request(RequestType.SESSION)
  public void deleteTerm(String sessionId, String termCode)
  {
    String classifierKey = TermBuilder.buildClassifierKeyFromTermCode(termCode);

    Classifier classifier = Classifier.getByKey(classifierKey);
    classifier.delete();

  }

  /**
   * Deletes the {@link GeoObjectType} with the given code.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link GeoObjectType} to delete.
   */
  @Request(RequestType.SESSION)
  public void deleteGeoObjectType(String sessionId, String code)
  {
    deleteGeoObjectTypeInTransaction(sessionId, code);
    
    new WMSService().deleteWMSLayer(code);

    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    // If we get here then it was successfully deleted
    adapter.getMetadataCache().removeGeoObjectType(code);
  }

  @Transaction
  private void deleteGeoObjectTypeInTransaction(String sessionId, String code)
  {
    Universal uni = Universal.getByKey(code);

    MdBusiness mdBusiness = uni.getMdBusiness();

    /*
     * Delete all Attribute references
     */
    AttributeHierarhcy.deleteByUniversal(uni);

    // This deletes the {@link MdBusiness} as well
    uni.delete();

    // Delete the term root
    Classifier classRootTerm = TermBuilder.buildIfNotExistdMdBusinessClassifier(mdBusiness);
    classRootTerm.delete();
  }

  /**
   * Returns the {@link HierarchyType}s with the given codes or all
   * {@link HierarchyType}s if no codes are provided.
   * 
   * @param sessionId
   * @param codes
   *          codes of the {@link HierarchyType}s.
   * @return the {@link HierarchyType}s with the given codes or all
   *         {@link HierarchyType}s if no codes are provided.
   */
  @Request(RequestType.SESSION)
  public HierarchyType[] getHierarchyTypes(String sessionId, String[] codes)
  {
    if (codes == null || codes.length == 0)
    {
      return adapter.getMetadataCache().getAllHierarchyTypes();
    }

    List<HierarchyType> hierarchyTypeList = new LinkedList<HierarchyType>();
    for (String code : codes)
    {
      Optional<HierarchyType> oht = adapter.getMetadataCache().getHierachyType(code);

      if (oht.isPresent())
      {
        hierarchyTypeList.add(oht.get());
      }
    }

    HierarchyType[] hierarchies = hierarchyTypeList.toArray(new HierarchyType[hierarchyTypeList.size()]);

    return hierarchies;
  }

  /**
   * Create the {@link HierarchyType} from the given JSON.
   * 
   * @param sessionId
   * @param htJSON
   *          JSON of the {@link HierarchyType} to be created.
   */
  @Request(RequestType.SESSION)
  public HierarchyType createHierarchyType(String sessionId, String htJSON)
  {
    String code = GeoRegistryUtil.createHierarchyType(htJSON);

    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return adapter.getMetadataCache().getHierachyType(code).get();
  }

  /**
   * Updates the given {@link HierarchyType} represented as JSON.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link HierarchyType} to be updated.
   */
  @Request(RequestType.SESSION)
  public HierarchyType updateHierarchyType(String sessionId, String htJSON)
  {
    HierarchyType hierarchyType = HierarchyType.fromJSON(htJSON, adapter);

    hierarchyType = updateHierarchyTypeTransaction(hierarchyType);

    // The transaction did not error out, so it is safe to put into the cache.
    adapter.getMetadataCache().addHierarchyType(hierarchyType);

    return hierarchyType;
  }

  @Transaction
  private HierarchyType updateHierarchyTypeTransaction(HierarchyType hierarchyType)
  {
    MdTermRelationship mdTermRelationship = ServiceFactory.getConversionService().existingHierarchyToUniversalMdTermRelationiship(hierarchyType);

    mdTermRelationship.lock();

    mdTermRelationship.getDisplayLabel().setValue(hierarchyType.getLocalizedLabel());
    mdTermRelationship.getDescription().setValue(hierarchyType.getLocalizedDescription());
    mdTermRelationship.apply();

    mdTermRelationship.unlock();

    HierarchyType returnHierarchyType = ServiceFactory.getConversionService().mdTermRelationshipToHierarchyType(mdTermRelationship);

    return returnHierarchyType;
  }

  /**
   * Deletes the {@link HierarchyType} with the given code.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link HierarchyType} to delete.
   */
  @Request(RequestType.SESSION)
  public void deleteHierarchyType(String sessionId, String code)
  {
    deleteHierarchyType(code);

    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    // No error at this point so the transaction completed successfully.
    adapter.getMetadataCache().removeHierarchyType(code);
  }

  @Transaction
  private void deleteHierarchyType(String code)
  {
    String mdTermRelUniversalKey = ConversionService.buildMdTermRelUniversalKey(code);

    MdTermRelationship mdTermRelUniversal = MdTermRelationship.getByKey(mdTermRelUniversalKey);

    Universal.getStrategy().shutdown(mdTermRelUniversal.definesType());

    AttributeHierarhcy.deleteByRelationship(mdTermRelUniversal);

    mdTermRelUniversal.delete();

    String mdTermRelGeoEntityKey = ConversionService.buildMdTermRelGeoEntityKey(code);

    MdTermRelationship mdTermRelGeoEntity = MdTermRelationship.getByKey(mdTermRelGeoEntityKey);

    GeoEntity.getStrategy().shutdown(mdTermRelGeoEntity.definesType());

    mdTermRelGeoEntity.delete();

    // /*
    // * Delete the Registry Maintainer role for the hierarchy
    // */
    // RoleDAO.findRole(RegistryConstants.REGISTRY_MAINTAINER_PREFIX +
    // code).getBusinessDAO().delete();
  }

  /**
   * Adds the {@link GeoObjectType} with the given child code to the parent
   * {@link GeoObjectType} with the given code for the given
   * {@link HierarchyType} code.
   * 
   * @param sessionId
   * @param hierarchyTypeCode
   *          code of the {@link HierarchyType} the child is being added to.
   * @param parentGeoObjectTypeCode
   *          parent {@link GeoObjectType}.
   * @param childGeoObjectTypeCode
   *          child {@link GeoObjectType}.
   */
  @Request(RequestType.SESSION)
  public HierarchyType addToHierarchy(String sessionId, String hierarchyTypeCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode)
  {
    String mdTermRelKey = ConversionService.buildMdTermRelUniversalKey(hierarchyTypeCode);
    MdTermRelationship mdTermRelationship = MdTermRelationship.getByKey(mdTermRelKey);

    Universal parentUniversal = Universal.getByKey(parentGeoObjectTypeCode);

    if (parentUniversal.getIsLeafType())
    {
      Universal childUniversal = Universal.getByKey(childGeoObjectTypeCode);

      NoChildForLeafGeoObjectType exception = new NoChildForLeafGeoObjectType();

      exception.setChildGeoObjectTypeLabel(childUniversal.getDisplayLabel().getValue());
      exception.setHierarchyTypeLabel(mdTermRelationship.getDisplayLabel().getValue());
      exception.setParentGeoObjectTypeLabel(parentUniversal.getDisplayLabel().getValue());
      exception.apply();

      throw exception;
    }

    this.addToHierarchy(hierarchyTypeCode, mdTermRelationship, parentGeoObjectTypeCode, childGeoObjectTypeCode);

    // No exceptions thrown. Refresh the HierarchyType object to include the new
    // relationships.
    HierarchyType ht = ServiceFactory.getConversionService().mdTermRelationshipToHierarchyType(mdTermRelationship);

    adapter.getMetadataCache().addHierarchyType(ht);

    return ht;
  }

  @Transaction
  private void addToHierarchy(String hierarchyTypeCode, MdTermRelationship mdTermRelationship, String parentGeoObjectTypeCode, String childGeoObjectTypeCode)
  {
    Universal parent = Universal.getByKey(parentGeoObjectTypeCode);
    Universal child = Universal.getByKey(childGeoObjectTypeCode);

    child.addLink(parent, mdTermRelationship.definesType());

    if (child.getIsLeafType())
    {
      ConversionService.addParentReferenceToLeafType(hierarchyTypeCode, parent, child);
    }
  }

  /**
   * Removes the {@link GeoObjectType} with the given child code from the parent
   * {@link GeoObjectType} with the given code for the given
   * {@link HierarchyType} code.
   * 
   * @param sessionId
   * @param hierarchyCode
   *          code of the {@link HierarchyType} the child is being added to.
   * @param parentGeoObjectTypeCode
   *          parent {@link GeoObjectType}.
   * @param childGeoObjectTypeCode
   *          child {@link GeoObjectType}.
   */
  @Request(RequestType.SESSION)
  public HierarchyType removeFromHierarchy(String sessionId, String hierarchyTypeCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode)
  {
    String mdTermRelKey = ConversionService.buildMdTermRelUniversalKey(hierarchyTypeCode);
    MdTermRelationship mdTermRelationship = MdTermRelationship.getByKey(mdTermRelKey);

    this.removeFromHierarchy(mdTermRelationship, hierarchyTypeCode, parentGeoObjectTypeCode, childGeoObjectTypeCode);

    // No exceptions thrown. Refresh the HierarchyType object to include the new
    // relationships.
    HierarchyType ht = ServiceFactory.getConversionService().mdTermRelationshipToHierarchyType(mdTermRelationship);

    adapter.getMetadataCache().addHierarchyType(ht);

    return ht;
  }

  @Transaction
  private void removeFromHierarchy(MdTermRelationship mdTermRelationship, String hierarchyTypeCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode)
  {
    Universal parent = Universal.getByKey(parentGeoObjectTypeCode);
    Universal child = Universal.getByKey(childGeoObjectTypeCode);

    parent.removeAllChildren(child, mdTermRelationship.definesType());

    if (child.getIsLeafType())
    {
      ConversionService.removeParentReferenceToLeafType(hierarchyTypeCode, parent, child);
    }
  }

  @Request(RequestType.SESSION)
  public JsonArray getGeoObjectSuggestions(String sessionId, String text, String typeCode, String parentCode, String hierarchyCode)
  {
    GeoObjectQuery query = ServiceFactory.getRegistryService().createQuery(typeCode);
    query.setRestriction(new LookupRestriction(text, parentCode, hierarchyCode));
    query.setLimit(10);

    GeoObjectIterator it = query.getIterator();

    try
    {
      JsonArray results = new JsonArray();

      while (it.hasNext())
      {
        GeoObject object = it.next();

        JsonObject result = new JsonObject();
        result.addProperty("id", it.currentOid());
        result.addProperty("name", object.getLocalizedDisplayLabel());

        results.add(result);
      }

      return results;
    }
    finally
    {
      it.close();
    }

  }

  @Request(RequestType.SESSION)
  public GeoObject newGeoObjectInstance(String sessionId, String geoObjectTypeCode)
  {
    return this.adapter.newGeoObjectInstance(geoObjectTypeCode);
  }

}
