/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.io;

@com.runwaysdk.business.ClassSignature(hash = 883147272)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AmbiguousParentException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AmbiguousParentExceptionBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.registry.io.AmbiguousParentException";
  public static java.lang.String CONTEXT = "context";
  public static java.lang.String OID = "oid";
  public static java.lang.String PARENTLABEL = "parentLabel";
  private static final long serialVersionUID = 883147272;
  
  public AmbiguousParentExceptionBase()
  {
    super();
  }
  
  public AmbiguousParentExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public AmbiguousParentExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public AmbiguousParentExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getContext()
  {
    return getValue(CONTEXT);
  }
  
  public void validateContext()
  {
    this.validateAttribute(CONTEXT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getContextMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.io.AmbiguousParentException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(CONTEXT);
  }
  
  public void setContext(String value)
  {
    if(value == null)
    {
      setValue(CONTEXT, "");
    }
    else
    {
      setValue(CONTEXT, value);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.io.AmbiguousParentException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getParentLabel()
  {
    return getValue(PARENTLABEL);
  }
  
  public void validateParentLabel()
  {
    this.validateAttribute(PARENTLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getParentLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.io.AmbiguousParentException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PARENTLABEL);
  }
  
  public void setParentLabel(String value)
  {
    if(value == null)
    {
      setValue(PARENTLABEL, "");
    }
    else
    {
      setValue(PARENTLABEL, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{context}", this.getContext());
    message = replace(message, "{oid}", this.getOid());
    message = replace(message, "{parentLabel}", this.getParentLabel());
    return message;
  }
  
}
