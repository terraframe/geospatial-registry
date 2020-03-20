package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = -1228951041)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to PublishShapefileJob.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class PublishShapefileJobQuery extends net.geoprism.registry.etl.MasterListJobQuery

{

  public PublishShapefileJobQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public PublishShapefileJobQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return net.geoprism.registry.etl.PublishShapefileJob.CLASS;
  }
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion()
  {
    return getVersion(null);

  }
 
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.registry.etl.PublishShapefileJob.VERSION);

    return (net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.registry.etl.PublishShapefileJob.VERSION, mdAttributeIF, this, alias, null);

  }
 
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.registry.etl.PublishShapefileJob.VERSION);

    return (net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.registry.etl.PublishShapefileJob.VERSION, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.registry.etl.PublishShapefileJob.VERSION)) 
    {
       return new net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
  public com.runwaysdk.query.OIterator<? extends PublishShapefileJob> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<PublishShapefileJob>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface PublishShapefileJobQueryReferenceIF extends net.geoprism.registry.etl.MasterListJobQuery.MasterListJobQueryReferenceIF
  {

    public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion();
    public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias);
    public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.etl.PublishShapefileJob publishShapefileJob);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.etl.PublishShapefileJob publishShapefileJob);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class PublishShapefileJobQueryReference extends net.geoprism.registry.etl.MasterListJobQuery.MasterListJobQueryReference
 implements PublishShapefileJobQueryReferenceIF

  {

  public PublishShapefileJobQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.registry.etl.PublishShapefileJob publishShapefileJob)
    {
      if(publishShapefileJob == null) return this.EQ((java.lang.String)null);
      return this.EQ(publishShapefileJob.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.registry.etl.PublishShapefileJob publishShapefileJob)
    {
      if(publishShapefileJob == null) return this.NE((java.lang.String)null);
      return this.NE(publishShapefileJob.getOid());
    }

  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion()
  {
    return getVersion(null);

  }
 
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias)
  {
    return (net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF)this.get(net.geoprism.registry.etl.PublishShapefileJob.VERSION, alias, null);

  }
 
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias, String displayLabel)
  {
    return (net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF)this.get(net.geoprism.registry.etl.PublishShapefileJob.VERSION,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.registry.etl.PublishShapefileJob.VERSION)) 
    {
       return new net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
  public interface PublishShapefileJobQueryMultiReferenceIF extends net.geoprism.registry.etl.MasterListJobQuery.MasterListJobQueryMultiReferenceIF
  {

    public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion();
    public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias);
    public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class PublishShapefileJobQueryMultiReference extends net.geoprism.registry.etl.MasterListJobQuery.MasterListJobQueryMultiReference
 implements PublishShapefileJobQueryMultiReferenceIF

  {

  public PublishShapefileJobQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob)  {

      String[] itemIdArray = new String[publishShapefileJob.length]; 

      for (int i=0; i<publishShapefileJob.length; i++)
      {
        itemIdArray[i] = publishShapefileJob[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob)  {

      String[] itemIdArray = new String[publishShapefileJob.length]; 

      for (int i=0; i<publishShapefileJob.length; i++)
      {
        itemIdArray[i] = publishShapefileJob[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob)  {

      String[] itemIdArray = new String[publishShapefileJob.length]; 

      for (int i=0; i<publishShapefileJob.length; i++)
      {
        itemIdArray[i] = publishShapefileJob[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob)  {

      String[] itemIdArray = new String[publishShapefileJob.length]; 

      for (int i=0; i<publishShapefileJob.length; i++)
      {
        itemIdArray[i] = publishShapefileJob[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.registry.etl.PublishShapefileJob ... publishShapefileJob)  {

      String[] itemIdArray = new String[publishShapefileJob.length]; 

      for (int i=0; i<publishShapefileJob.length; i++)
      {
        itemIdArray[i] = publishShapefileJob[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion()
  {
    return getVersion(null);

  }
 
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias)
  {
    return (net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF)this.get(net.geoprism.registry.etl.PublishShapefileJob.VERSION, alias, null);

  }
 
  public net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF getVersion(String alias, String displayLabel)
  {
    return (net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReferenceIF)this.get(net.geoprism.registry.etl.PublishShapefileJob.VERSION,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.registry.etl.PublishShapefileJob.VERSION)) 
    {
       return new net.geoprism.registry.MasterListVersionQuery.MasterListVersionQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}