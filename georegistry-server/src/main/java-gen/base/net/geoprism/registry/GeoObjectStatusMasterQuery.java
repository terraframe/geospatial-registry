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
package net.geoprism.registry;

@com.runwaysdk.business.ClassSignature(hash = -343161326)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoObjectStatusMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class GeoObjectStatusMasterQuery extends com.runwaysdk.system.EnumerationMasterQuery

{

  public GeoObjectStatusMasterQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public GeoObjectStatusMasterQuery(com.runwaysdk.query.ValueQuery valueQuery)
  {
    super(valueQuery);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = new com.runwaysdk.business.BusinessQuery(valueQuery, this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public String getClassType()
  {
    return net.geoprism.registry.GeoObjectStatusMaster.CLASS;
  }
  public com.runwaysdk.query.SelectableInteger getStatusOrder()
  {
    return getStatusOrder(null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias)
  {
    return (com.runwaysdk.query.SelectableInteger)this.getComponentQuery().get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableInteger)this.getComponentQuery().get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends GeoObjectStatusMaster> getIterator()
  {
    this.checkNotUsedInValueQuery();
    String sqlStmt;
    if (_limit != null && _skip != null)
    {
      sqlStmt = this.getComponentQuery().getSQL(_limit, _skip);
    }
    else
    {
      sqlStmt = this.getComponentQuery().getSQL();
    }
    java.util.Map<String, com.runwaysdk.query.ColumnInfo> columnInfoMap = this.getComponentQuery().getColumnInfoMap();

    java.sql.ResultSet results = com.runwaysdk.dataaccess.database.Database.query(sqlStmt);
    return new com.runwaysdk.business.BusinessIterator<GeoObjectStatusMaster>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_GeoObjectStatus()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(net.geoprism.registry.GeoObjectStatus.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_GeoObjectStatus()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(net.geoprism.registry.GeoObjectStatus.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeoObjectStatusMasterQueryReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableInteger getStatusOrder();
    public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias);
    public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.GeoObjectStatusMaster geoObjectStatusMaster);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.GeoObjectStatusMaster geoObjectStatusMaster);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeoObjectStatusMasterQueryReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReference
 implements GeoObjectStatusMasterQueryReferenceIF

  {

  public GeoObjectStatusMasterQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.GeoObjectStatusMaster geoObjectStatusMaster)
    {
      if(geoObjectStatusMaster == null) return this.EQ((java.lang.String)null);
      return this.EQ(geoObjectStatusMaster.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.GeoObjectStatusMaster geoObjectStatusMaster)
    {
      if(geoObjectStatusMaster == null) return this.NE((java.lang.String)null);
      return this.NE(geoObjectStatusMaster.getOid());
    }

  public com.runwaysdk.query.SelectableInteger getStatusOrder()
  {
    return getStatusOrder(null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias)
  {
    return (com.runwaysdk.query.SelectableInteger)this.get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableInteger)this.get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, displayLabel);

  }
  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface GeoObjectStatusMasterQueryEnumerationIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumerationIF
  {

    public com.runwaysdk.query.SelectableInteger getStatusOrder();
    public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias);
    public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class GeoObjectStatusMasterQueryEnumeration extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumeration
 implements GeoObjectStatusMasterQueryEnumerationIF
  {

  public GeoObjectStatusMasterQueryEnumeration(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

  public com.runwaysdk.query.SelectableInteger getStatusOrder()
  {
    return getStatusOrder(null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias)
  {
    return (com.runwaysdk.query.SelectableInteger)this.get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableInteger)this.get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, displayLabel);

  }
  }

/**
 * Specifies type safe query methods for the enumeration net.geoprism.registry.GeoObjectStatus.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface GeoObjectStatusQueryIF extends GeoObjectStatusMasterQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus);
  }

/**
 * Implements type safe query methods for the enumeration net.geoprism.registry.GeoObjectStatus.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class GeoObjectStatusQuery extends GeoObjectStatusMasterQueryEnumeration implements  GeoObjectStatusQueryIF
  {
  public GeoObjectStatusQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus)  {

      String[] enumIdArray = new String[geoObjectStatus.length]; 

      for (int i=0; i<geoObjectStatus.length; i++)
      {
        enumIdArray[i] = geoObjectStatus[i].getOid();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus)  {

      String[] enumIdArray = new String[geoObjectStatus.length]; 

      for (int i=0; i<geoObjectStatus.length; i++)
      {
        enumIdArray[i] = geoObjectStatus[i].getOid();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus)  {

      String[] enumIdArray = new String[geoObjectStatus.length]; 

      for (int i=0; i<geoObjectStatus.length; i++)
      {
        enumIdArray[i] = geoObjectStatus[i].getOid();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus)  {

      String[] enumIdArray = new String[geoObjectStatus.length]; 

      for (int i=0; i<geoObjectStatus.length; i++)
      {
        enumIdArray[i] = geoObjectStatus[i].getOid();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.GeoObjectStatus ... geoObjectStatus)  {

      String[] enumIdArray = new String[geoObjectStatus.length]; 

      for (int i=0; i<geoObjectStatus.length; i++)
      {
        enumIdArray[i] = geoObjectStatus[i].getOid();
      }

      return this.containsExactly(enumIdArray);
  }
  }
/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeoObjectStatusMasterQueryMultiReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReferenceIF
  {

    public com.runwaysdk.query.SelectableInteger getStatusOrder();
    public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias);
    public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeoObjectStatusMasterQueryMultiReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReference
 implements GeoObjectStatusMasterQueryMultiReferenceIF

  {

  public GeoObjectStatusMasterQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster)  {

      String[] itemIdArray = new String[geoObjectStatusMaster.length]; 

      for (int i=0; i<geoObjectStatusMaster.length; i++)
      {
        itemIdArray[i] = geoObjectStatusMaster[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster)  {

      String[] itemIdArray = new String[geoObjectStatusMaster.length]; 

      for (int i=0; i<geoObjectStatusMaster.length; i++)
      {
        itemIdArray[i] = geoObjectStatusMaster[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster)  {

      String[] itemIdArray = new String[geoObjectStatusMaster.length]; 

      for (int i=0; i<geoObjectStatusMaster.length; i++)
      {
        itemIdArray[i] = geoObjectStatusMaster[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster)  {

      String[] itemIdArray = new String[geoObjectStatusMaster.length]; 

      for (int i=0; i<geoObjectStatusMaster.length; i++)
      {
        itemIdArray[i] = geoObjectStatusMaster[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.GeoObjectStatusMaster ... geoObjectStatusMaster)  {

      String[] itemIdArray = new String[geoObjectStatusMaster.length]; 

      for (int i=0; i<geoObjectStatusMaster.length; i++)
      {
        itemIdArray[i] = geoObjectStatusMaster[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.query.SelectableInteger getStatusOrder()
  {
    return getStatusOrder(null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias)
  {
    return (com.runwaysdk.query.SelectableInteger)this.get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableInteger getStatusOrder(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableInteger)this.get(net.geoprism.registry.GeoObjectStatusMaster.STATUSORDER, alias, displayLabel);

  }
  }
}
