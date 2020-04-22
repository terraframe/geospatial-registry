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
package net.geoprism.registry.etl;

@com.runwaysdk.business.ClassSignature(hash = 1275181660)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MasterListJob.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MasterListJobBase extends com.runwaysdk.system.scheduler.ExecutableJob
{
  public final static String CLASS = "net.geoprism.registry.etl.MasterListJob";
  public static java.lang.String MASTERLIST = "masterList";
  private static final long serialVersionUID = 1275181660;
  
  public MasterListJobBase()
  {
    super();
  }
  
  public net.geoprism.registry.MasterList getMasterList()
  {
    if (getValue(MASTERLIST).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.registry.MasterList.get(getValue(MASTERLIST));
    }
  }
  
  public String getMasterListOid()
  {
    return getValue(MASTERLIST);
  }
  
  public void validateMasterList()
  {
    this.validateAttribute(MASTERLIST);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getMasterListMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.etl.MasterListJob.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(MASTERLIST);
  }
  
  public void setMasterList(net.geoprism.registry.MasterList value)
  {
    if(value == null)
    {
      setValue(MASTERLIST, "");
    }
    else
    {
      setValue(MASTERLIST, value.getOid());
    }
  }
  
  public void setMasterListId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(MASTERLIST, "");
    }
    else
    {
      setValue(MASTERLIST, oid);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MasterListJobQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    MasterListJobQuery query = new MasterListJobQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static MasterListJob get(String oid)
  {
    return (MasterListJob) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MasterListJob getByKey(String key)
  {
    return (MasterListJob) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MasterListJob lock(java.lang.String oid)
  {
    MasterListJob _instance = MasterListJob.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MasterListJob unlock(java.lang.String oid)
  {
    MasterListJob _instance = MasterListJob.get(oid);
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
