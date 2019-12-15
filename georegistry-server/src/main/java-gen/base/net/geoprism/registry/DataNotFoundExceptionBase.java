package net.geoprism.registry;

@com.runwaysdk.business.ClassSignature(hash = 1836869847)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DataNotFoundException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DataNotFoundExceptionBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.registry.DataNotFoundException";
  public static java.lang.String DATAIDENTIFIER = "dataIdentifier";
  public static java.lang.String OID = "oid";
  private static final long serialVersionUID = 1836869847;
  
  public DataNotFoundExceptionBase()
  {
    super();
  }
  
  public DataNotFoundExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public DataNotFoundExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public DataNotFoundExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getDataIdentifier()
  {
    return getValue(DATAIDENTIFIER);
  }
  
  public void validateDataIdentifier()
  {
    this.validateAttribute(DATAIDENTIFIER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getDataIdentifierMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.DataNotFoundException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(DATAIDENTIFIER);
  }
  
  public void setDataIdentifier(String value)
  {
    if(value == null)
    {
      setValue(DATAIDENTIFIER, "");
    }
    else
    {
      setValue(DATAIDENTIFIER, value);
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.DataNotFoundException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{dataIdentifier}", this.getDataIdentifier());
    message = replace(message, "{oid}", this.getOid());
    return message;
  }
  
}
