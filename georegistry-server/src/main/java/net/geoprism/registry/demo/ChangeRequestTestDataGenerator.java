package net.geoprism.registry.demo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.commongeoregistry.adapter.action.AbstractActionDTO;
import org.commongeoregistry.adapter.action.geoobject.CreateGeoObjectActionDTO;
import org.commongeoregistry.adapter.action.geoobject.UpdateGeoObjectActionDTO;
import org.commongeoregistry.adapter.action.tree.AddChildActionDTO;
import org.commongeoregistry.adapter.action.tree.RemoveChildActionDTO;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.LocatedIn;

import net.geoprism.registry.action.AbstractAction;
import net.geoprism.registry.action.AbstractActionQuery;
import net.geoprism.registry.action.AllGovernanceStatus;
import net.geoprism.registry.action.ChangeRequest;
import net.geoprism.registry.action.ChangeRequestQuery;
import net.geoprism.registry.service.ServiceFactory;

/**
 * This class generates some fake ChangeRequest data for demos and whatnot
 * 
 * @author rrowlands
 */
public class ChangeRequestTestDataGenerator
{
  public static void main(String[] args)
  {
    ChangeRequestTestDataGenerator.build();
  }

  @Request
  private static void build()
  {
    ChangeRequestTestDataGenerator.deleteAllActions();
    ChangeRequestTestDataGenerator.deleteAllChangeRequests();

    buildInTransaction();
  }
  
  private static void buildInTransaction()
  {
    genChangeRequest("CR1", Instant.now().minus(3, ChronoUnit.DAYS), true, false);

    try
    {
      Thread.sleep(1500);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    genChangeRequest("CR2", Instant.now().minus(2, ChronoUnit.DAYS), false, true);

    try
    {
      Thread.sleep(1500);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    genChangeRequest("CR3", Instant.now().minus(1, ChronoUnit.DAYS), false, false);
  }

  @Transaction
  private static void genChangeRequest(String genKey, Instant when, boolean includeRemove, boolean includeAdd)
  {
    GeoObject goNewChild = ServiceFactory.getAdapter().newGeoObjectInstance("Cambodia_District");
    goNewChild.setCode(genKey + "_CODE");
    goNewChild.setDisplayLabel(LocalizedValue.DEFAULT_LOCALE, genKey + "_LABEL");
    goNewChild.setWKTGeometry("MULTIPOLYGON (((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000)))");

    GeoObject testAddChildParent = ServiceFactory.getUtilities().getGeoObjectByCode("855 01", "Cambodia_Province");
    GeoObject testAddChild = ServiceFactory.getUtilities().getGeoObjectByCode("855 0109", "Cambodia_District");

    List<AbstractActionDTO> actions = new ArrayList<AbstractActionDTO>();

    /*
     * Remove Child
     */
    if (includeRemove)
    {
      RemoveChildActionDTO removeChild = new RemoveChildActionDTO();
      removeChild.setChildId(testAddChild.getUid());
      removeChild.setChildTypeCode(testAddChild.getType().getCode());
      removeChild.setParentId(testAddChildParent.getUid());
      removeChild.setParentTypeCode(testAddChildParent.getType().getCode());
      removeChild.setHierarchyCode(LocatedIn.class.getSimpleName());
      removeChild.setCreateActionDate(Date.from(when.minus(9, ChronoUnit.HOURS)));

      actions.add(removeChild);
    }

    /*
     * Add Child
     */
    if (includeAdd)
    {
      AddChildActionDTO addChild = new AddChildActionDTO();
      addChild.setChildId(testAddChild.getUid());
      addChild.setChildTypeCode(testAddChild.getType().getCode());
      addChild.setParentId(testAddChildParent.getUid());
      addChild.setParentTypeCode(testAddChildParent.getType().getCode());
      addChild.setHierarchyCode(LocatedIn.class.getSimpleName());
      addChild.setCreateActionDate(Date.from(when.minus(10, ChronoUnit.HOURS)));

      actions.add(addChild);
    }

    /*
     * Create a new GeoObject
     */
    CreateGeoObjectActionDTO create = new CreateGeoObjectActionDTO();
    create.setGeoObject(goNewChild.toJSON());
    create.setCreateActionDate(Date.from(when.minus(8, ChronoUnit.HOURS)));

    actions.add(create);

    /*
     * Update the previously created GeoObject
     */
    final String NEW_DISPLAY_LABEL = genKey + "_NEW_DISPLAY_LABEL";
    goNewChild.setDisplayLabel(LocalizedValue.DEFAULT_LOCALE, NEW_DISPLAY_LABEL);

    UpdateGeoObjectActionDTO update = new UpdateGeoObjectActionDTO();
    update.setGeoObject(goNewChild.toJSON());
    update.setCreateActionDate(Date.from(when.minus(7, ChronoUnit.HOURS)));

    actions.add(update);

    // Serialize the actions
    String sActions = AbstractActionDTO.serializeActions(actions).toString();

    submitChangeRequest(sActions);
  }

  private static void submitChangeRequest(String sJson)
  {
    ChangeRequest cr = new ChangeRequest();
    cr.addApprovalStatus(AllGovernanceStatus.PENDING);
    cr.apply();

    List<AbstractActionDTO> actionDTOs = AbstractActionDTO.parseActions(sJson);

    for (AbstractActionDTO actionDTO : actionDTOs)
    {
      AbstractAction ra = AbstractAction.dtoToRegistry(actionDTO);
      ra.addApprovalStatus(AllGovernanceStatus.PENDING);
      ra.apply();

      cr.addAction(ra).apply();
    }
  }

  /**
   * These were copied/pasted from TestDataSet because this code is to be run as
   * part of a demo.
   */
  @Request
  public static void deleteAllActions()
  {
    AbstractActionQuery aaq = new AbstractActionQuery(new QueryFactory());

    OIterator<? extends AbstractAction> it = aaq.getIterator();

    while (it.hasNext())
    {
      it.next().delete();
    }
  }

  @Request
  public static void deleteAllChangeRequests()
  {
    ChangeRequestQuery crq = new ChangeRequestQuery(new QueryFactory());

    OIterator<? extends ChangeRequest> it = crq.getIterator();

    while (it.hasNext())
    {
      it.next().delete();
    }
  }
}