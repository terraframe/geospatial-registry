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
package net.geoprism.registry.etl.export;

@com.runwaysdk.business.ClassSignature(hash = -664204398)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DataExportJob.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DataExportJobBase extends com.runwaysdk.system.scheduler.ExecutableJob
{
  public final static String CLASS = "net.geoprism.registry.etl.export.DataExportJob";
  public static java.lang.String CONFIG = "config";
  private static final long serialVersionUID = -664204398;
  
  public DataExportJobBase()
  {
    super();
  }
  
  public net.geoprism.registry.SynchronizationConfig getConfig()
  {
    if (getValue(CONFIG).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.registry.SynchronizationConfig.get(getValue(CONFIG));
    }
  }
  
  public String getConfigOid()
  {
    return getValue(CONFIG);
  }
  
  public void validateConfig()
  {
    this.validateAttribute(CONFIG);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getConfigMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.export.DataExportJob.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CONFIG);
  }
  
  public void setConfig(net.geoprism.registry.SynchronizationConfig value)
  {
    if(value == null)
    {
      setValue(CONFIG, "");
    }
    else
    {
      setValue(CONFIG, value.getOid());
    }
  }
  
  public void setConfigId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(CONFIG, "");
    }
    else
    {
      setValue(CONFIG, oid);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DataExportJobQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DataExportJobQuery query = new DataExportJobQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static DataExportJob get(String oid)
  {
    return (DataExportJob) com.runwaysdk.business.Business.get(oid);
  }
  
  public static DataExportJob getByKey(String key)
  {
    return (DataExportJob) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static DataExportJob lock(java.lang.String oid)
  {
    DataExportJob _instance = DataExportJob.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static DataExportJob unlock(java.lang.String oid)
  {
    DataExportJob _instance = DataExportJob.get(oid);
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