package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = 242301075)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MasterListJob.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class MasterListJobQuery extends com.runwaysdk.system.scheduler.ExecutableJobQuery

{

  public MasterListJobQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public MasterListJobQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return net.geoprism.registry.etl.MasterListJob.CLASS;
  }
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList()
  {
    return getMasterList(null);

  }
 
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.registry.etl.MasterListJob.MASTERLIST);

    return (net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.registry.etl.MasterListJob.MASTERLIST, mdAttributeIF, this, alias, null);

  }
 
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.registry.etl.MasterListJob.MASTERLIST);

    return (net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.registry.etl.MasterListJob.MASTERLIST, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.registry.etl.MasterListJob.MASTERLIST)) 
    {
       return new net.geoprism.registry.MasterListQuery.MasterListQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends MasterListJob> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<MasterListJob>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MasterListJobQueryReferenceIF extends com.runwaysdk.system.scheduler.ExecutableJobQuery.ExecutableJobQueryReferenceIF
  {

    public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList();
    public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias);
    public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.etl.MasterListJob masterListJob);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.etl.MasterListJob masterListJob);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MasterListJobQueryReference extends com.runwaysdk.system.scheduler.ExecutableJobQuery.ExecutableJobQueryReference
 implements MasterListJobQueryReferenceIF

  {

  public MasterListJobQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.etl.MasterListJob masterListJob)
    {
      if(masterListJob == null) return this.EQ((java.lang.String)null);
      return this.EQ(masterListJob.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.etl.MasterListJob masterListJob)
    {
      if(masterListJob == null) return this.NE((java.lang.String)null);
      return this.NE(masterListJob.getOid());
    }

  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList()
  {
    return getMasterList(null);

  }
 
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias)
  {
    return (net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF)this.get(net.geoprism.registry.etl.MasterListJob.MASTERLIST, alias, null);

  }
 
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias, String displayLabel)
  {
    return (net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF)this.get(net.geoprism.registry.etl.MasterListJob.MASTERLIST,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.registry.etl.MasterListJob.MASTERLIST)) 
    {
       return new net.geoprism.registry.MasterListQuery.MasterListQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MasterListJobQueryMultiReferenceIF extends com.runwaysdk.system.scheduler.ExecutableJobQuery.ExecutableJobQueryMultiReferenceIF
  {

    public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList();
    public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias);
    public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.etl.MasterListJob ... masterListJob);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.etl.MasterListJob ... masterListJob);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.etl.MasterListJob ... masterListJob);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.etl.MasterListJob ... masterListJob);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.etl.MasterListJob ... masterListJob);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MasterListJobQueryMultiReference extends com.runwaysdk.system.scheduler.ExecutableJobQuery.ExecutableJobQueryMultiReference
 implements MasterListJobQueryMultiReferenceIF

  {

  public MasterListJobQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.etl.MasterListJob ... masterListJob)  {

      String[] itemIdArray = new String[masterListJob.length]; 

      for (int i=0; i<masterListJob.length; i++)
      {
        itemIdArray[i] = masterListJob[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.etl.MasterListJob ... masterListJob)  {

      String[] itemIdArray = new String[masterListJob.length]; 

      for (int i=0; i<masterListJob.length; i++)
      {
        itemIdArray[i] = masterListJob[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.etl.MasterListJob ... masterListJob)  {

      String[] itemIdArray = new String[masterListJob.length]; 

      for (int i=0; i<masterListJob.length; i++)
      {
        itemIdArray[i] = masterListJob[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.etl.MasterListJob ... masterListJob)  {

      String[] itemIdArray = new String[masterListJob.length]; 

      for (int i=0; i<masterListJob.length; i++)
      {
        itemIdArray[i] = masterListJob[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.etl.MasterListJob ... masterListJob)  {

      String[] itemIdArray = new String[masterListJob.length]; 

      for (int i=0; i<masterListJob.length; i++)
      {
        itemIdArray[i] = masterListJob[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList()
  {
    return getMasterList(null);

  }
 
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias)
  {
    return (net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF)this.get(net.geoprism.registry.etl.MasterListJob.MASTERLIST, alias, null);

  }
 
  public net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF getMasterList(String alias, String displayLabel)
  {
    return (net.geoprism.registry.MasterListQuery.MasterListQueryReferenceIF)this.get(net.geoprism.registry.etl.MasterListJob.MASTERLIST,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.registry.etl.MasterListJob.MASTERLIST)) 
    {
       return new net.geoprism.registry.MasterListQuery.MasterListQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}
