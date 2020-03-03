package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = -1240716962)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ImportHistory.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ImportHistoryBase extends com.runwaysdk.system.scheduler.JobHistory
{
  public final static String CLASS = "net.geoprism.registry.etl.ImportHistory";
  public static java.lang.String CONFIGJSON = "configJson";
  public static java.lang.String IMPORTFILE = "importFile";
  public static java.lang.String IMPORTEDRECORDS = "importedRecords";
  public static java.lang.String STAGE = "stage";
  public static java.lang.String VALIDATIONPROBLEMS = "validationProblems";
  private static final long serialVersionUID = -1240716962;
  
  public ImportHistoryBase()
  {
    super();
  }
  
  public String getConfigJson()
  {
    return getValue(CONFIGJSON);
  }
  
  public void validateConfigJson()
  {
    this.validateAttribute(CONFIGJSON);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getConfigJsonMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.ImportHistory.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(CONFIGJSON);
  }
  
  public void setConfigJson(String value)
  {
    if(value == null)
    {
      setValue(CONFIGJSON, "");
    }
    else
    {
      setValue(CONFIGJSON, value);
    }
  }
  
  public com.runwaysdk.system.VaultFile getImportFile()
  {
    if (getValue(IMPORTFILE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.VaultFile.get(getValue(IMPORTFILE));
    }
  }
  
  public String getImportFileOid()
  {
    return getValue(IMPORTFILE);
  }
  
  public void validateImportFile()
  {
    this.validateAttribute(IMPORTFILE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getImportFileMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.ImportHistory.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(IMPORTFILE);
  }
  
  public void setImportFile(com.runwaysdk.system.VaultFile value)
  {
    if(value == null)
    {
      setValue(IMPORTFILE, "");
    }
    else
    {
      setValue(IMPORTFILE, value.getOid());
    }
  }
  
  public void setImportFileId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(IMPORTFILE, "");
    }
    else
    {
      setValue(IMPORTFILE, oid);
    }
  }
  
  public Long getImportedRecords()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(IMPORTEDRECORDS));
  }
  
  public void validateImportedRecords()
  {
    this.validateAttribute(IMPORTEDRECORDS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getImportedRecordsMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.ImportHistory.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(IMPORTEDRECORDS);
  }
  
  public void setImportedRecords(Long value)
  {
    if(value == null)
    {
      setValue(IMPORTEDRECORDS, "");
    }
    else
    {
      setValue(IMPORTEDRECORDS, java.lang.Long.toString(value));
    }
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.etl.ImportStage> getStage()
  {
    return (java.util.List<net.geoprism.registry.etl.ImportStage>) getEnumValues(STAGE);
  }
  
  public void addStage(net.geoprism.registry.etl.ImportStage value)
  {
    if(value != null)
    {
      addEnumItem(STAGE, value.getOid());
    }
  }
  
  public void removeStage(net.geoprism.registry.etl.ImportStage value)
  {
    if(value != null)
    {
      removeEnumItem(STAGE, value.getOid());
    }
  }
  
  public void clearStage()
  {
    clearEnum(STAGE);
  }
  
  public void validateStage()
  {
    this.validateAttribute(STAGE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF getStageMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.ImportHistory.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdClassIF.definesAttribute(STAGE);
  }
  
  public String getValidationProblems()
  {
    return getValue(VALIDATIONPROBLEMS);
  }
  
  public void validateValidationProblems()
  {
    this.validateAttribute(VALIDATIONPROBLEMS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getValidationProblemsMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.ImportHistory.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(VALIDATIONPROBLEMS);
  }
  
  public void setValidationProblems(String value)
  {
    if(value == null)
    {
      setValue(VALIDATIONPROBLEMS, "");
    }
    else
    {
      setValue(VALIDATIONPROBLEMS, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ImportHistoryQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    ImportHistoryQuery query = new ImportHistoryQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static ImportHistory get(String oid)
  {
    return (ImportHistory) com.runwaysdk.business.Business.get(oid);
  }
  
  public static ImportHistory getByKey(String key)
  {
    return (ImportHistory) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static ImportHistory lock(java.lang.String oid)
  {
    ImportHistory _instance = ImportHistory.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static ImportHistory unlock(java.lang.String oid)
  {
    ImportHistory _instance = ImportHistory.get(oid);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}