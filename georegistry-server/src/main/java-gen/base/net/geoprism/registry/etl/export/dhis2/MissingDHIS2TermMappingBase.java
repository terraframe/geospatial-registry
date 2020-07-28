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
package net.geoprism.registry.etl.export.dhis2;

@com.runwaysdk.business.ClassSignature(hash = 1629932616)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MissingDHIS2TermMapping.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MissingDHIS2TermMappingBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.registry.etl.export.dhis2.MissingDHIS2TermMapping";
  public static java.lang.String OID = "oid";
  public static java.lang.String TERMCODE = "termCode";
  private static final long serialVersionUID = 1629932616;
  
  public MissingDHIS2TermMappingBase()
  {
    super();
  }
  
  public MissingDHIS2TermMappingBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public MissingDHIS2TermMappingBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public MissingDHIS2TermMappingBase(java.lang.Throwable cause)
  {
    super(cause);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.export.dhis2.MissingDHIS2TermMapping.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getTermCode()
  {
    return getValue(TERMCODE);
  }
  
  public void validateTermCode()
  {
    this.validateAttribute(TERMCODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getTermCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.export.dhis2.MissingDHIS2TermMapping.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(TERMCODE);
  }
  
  public void setTermCode(String value)
  {
    if(value == null)
    {
      setValue(TERMCODE, "");
    }
    else
    {
      setValue(TERMCODE, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{termCode}", this.getTermCode());
    return message;
  }
  
}
