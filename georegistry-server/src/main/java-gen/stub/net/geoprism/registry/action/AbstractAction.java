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
package net.geoprism.registry.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.commongeoregistry.adapter.action.AbstractActionDTO;
import org.json.JSONObject;

import com.runwaysdk.session.Session;
import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.Users;

import net.geoprism.registry.io.GeoObjectImportConfiguration;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.RegistryService;
import net.geoprism.registry.service.ServiceFactory;

public abstract class AbstractAction extends AbstractActionBase
{

  private static final long serialVersionUID = 1324056554;

  protected RegistryService registry;

  public AbstractAction()
  {
    super();

    this.registry = ServiceFactory.getRegistryService();
  }

  abstract public void execute();

  public abstract boolean isVisible();
  
  public abstract boolean referencesType(ServerGeoObjectType type);

  protected abstract String getMessage();

  public AbstractAction(RegistryService registry)
  {
    this.registry = registry;
  }
  
  protected boolean doesGOTExist(String code)
  {
    return ServiceFactory.getAdapter().getMetadataCache().getGeoObjectType(code).isPresent();
  }

  public static AbstractAction dtoToRegistry(AbstractActionDTO actionDTO)
  {
    AbstractAction action = ActionFactory.newAction(actionDTO.getActionType());

    action.buildFromDTO(actionDTO);

    return action;
  }

  protected void buildFromDTO(AbstractActionDTO dto)
  {
    this.setApiVersion(dto.getApiVersion());
    this.setCreateActionDate(dto.getCreateActionDate());
    this.setContributorNotes(dto.getContributorNotes());
    this.setMaintainerNotes(dto.getMaintainerNotes());
  }

  /*
   * TODO : We should be converting to a DTO and then serializing, that way we
   * only have to have the serialization logic in one place.
   */
  public JSONObject serialize()
  {
    AllGovernanceStatus status = this.getApprovalStatus().get(0);
    DateFormat format = new SimpleDateFormat(GeoObjectImportConfiguration.DATE_FORMAT);

    JSONObject jo = new JSONObject();

    jo.put(AbstractAction.OID, this.getOid());
    jo.put("actionType", this.getType());
    jo.put("actionLabel", this.getMdClass().getDisplayLabel(Session.getCurrentLocale()));
    jo.put(AbstractAction.CREATEACTIONDATE, this.getCreateActionDate());
    jo.put(AbstractAction.CONTRIBUTORNOTES, this.getContributorNotes());
    jo.put(AbstractAction.MAINTAINERNOTES, this.getMaintainerNotes());
    jo.put(AbstractAction.ADDITIONALNOTES, this.getAdditionalNotes());
    jo.put(AbstractAction.APPROVALSTATUS, this.getApprovalStatus().get(0).getEnumName());
    jo.put("statusLabel", status.getDisplayLabel());
    jo.put(AbstractAction.CREATEACTIONDATE, format.format(this.getCreateActionDate()));
    jo.put(AbstractAction.CREATEDBY, this.getCreatedBy());

    SingleActor decisionMaker = this.getDecisionMaker();

    if (decisionMaker != null && ( decisionMaker instanceof Users ))
    {
      jo.put(AbstractAction.DECISIONMAKER, ( (Users) decisionMaker ).getUsername());
    }

    return jo;
  }

  /*
   * TODO : We should be converting to a DTO and then using 'buildFromDTO', that
   * way we only have to have the serialization logic in one place.
   */
  public void buildFromJson(JSONObject joAction)
  {
    this.clearApprovalStatus();
    this.addApprovalStatus(AllGovernanceStatus.valueOf(joAction.getString(AbstractAction.APPROVALSTATUS)));

    this.setContributorNotes(joAction.getString(AbstractAction.CONTRIBUTORNOTES));
    this.setMaintainerNotes(joAction.getString(AbstractAction.MAINTAINERNOTES));
    this.setAdditionalNotes(joAction.getString(AbstractAction.ADDITIONALNOTES));
  }

}
