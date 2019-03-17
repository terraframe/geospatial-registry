package net.geoprism.registry.excel;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.UnknownTermException;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.session.RequestState;
import com.vividsolutions.jts.geom.Geometry;

import net.geoprism.data.importer.FeatureRow;
import net.geoprism.data.importer.ShapefileFunction;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.io.AmbiguousParentException;
import net.geoprism.registry.io.GeoObjectConfiguration;
import net.geoprism.registry.io.IgnoreRowException;
import net.geoprism.registry.io.Location;
import net.geoprism.registry.io.LocationBuilder;
import net.geoprism.registry.io.PostalCodeFactory;
import net.geoprism.registry.io.PostalCodeLocationException;
import net.geoprism.registry.io.RequiredMappingException;
import net.geoprism.registry.io.SynonymRestriction;
import net.geoprism.registry.io.TermProblem;
import net.geoprism.registry.io.TermValueException;
import net.geoprism.registry.query.CodeRestriction;
import net.geoprism.registry.query.GeoObjectQuery;
import net.geoprism.registry.query.NonUniqueResultException;
import net.geoprism.registry.service.RegistryService;
import net.geoprism.registry.service.ServiceFactory;
import net.geoprism.registry.shapefile.GeoObjectLocationProblem;

public abstract class FeatureRowImporter
{
  protected GeoObjectConfiguration configuration;

  public FeatureRowImporter(GeoObjectConfiguration configuration)
  {
    this.configuration = configuration;
  }

  protected abstract Geometry getGeometry(FeatureRow row);

  protected abstract void setValue(GeoObject entity, AttributeType attributeType, String attributeName, Object value);

  public GeoObjectConfiguration getConfiguration()
  {
    return configuration;
  }

  /**
   * Imports a GeoObject based on the given SimpleFeature. If a matching
   * GeoObject already exists then it is simply updated.
   * 
   * @param feature
   * @throws Exception
   */
  public void create(FeatureRow row)
  {
    try
    {
      GeoObject parent = null;

      /*
       * First, try to get the parent and ensure that this row is not ignored.
       * The getParent method will throw a IgnoreRowException if the parent is
       * configured to be ignored.
       */
      if (this.configuration.isPostalCode() && PostalCodeFactory.isAvailable(this.configuration.getType()))
      {
        parent = this.parsePostalCode(row);
      }
      else if (this.configuration.getHierarchy() != null && this.configuration.getLocations().size() > 0)
      {
        parent = this.getParent(row);
      }

      String geoId = this.getCode(row);

      GeoObject entity;
      boolean isNew = false;

      if (geoId != null && geoId.length() > 0)
      {
        try
        {
          // try an update
          isNew = false;
          entity = ServiceFactory.getUtilities().getGeoObjectByCode(geoId, this.configuration.getType().getCode());
        }
        catch (DataNotFoundException e)
        {
          // create a new entity
          isNew = true;
          entity = ServiceFactory.getAdapter().newGeoObjectInstance(this.configuration.getType().getCode());
        }
      }
      else
      {
        // create a new entity
        isNew = true;
        entity = ServiceFactory.getAdapter().newGeoObjectInstance(this.configuration.getType().getCode());
      }

      Geometry geometry = (Geometry) this.getGeometry(row);
      Object entityName = this.getName(row);

      if (entityName != null)
      {
        if (geometry != null)
        {
          entity.setGeometry(geometry);
        }

        if (isNew)
        {
          entity.setUid(ServiceFactory.getIdService().getUids(1)[0]);
        }

        Map<String, AttributeType> attributes = this.configuration.getType().getAttributeMap();
        Set<Entry<String, AttributeType>> entries = attributes.entrySet();

        for (Entry<String, AttributeType> entry : entries)
        {
          String attributeName = entry.getKey();

          ShapefileFunction function = this.configuration.getFunction(attributeName);

          if (function != null)
          {
            Object value = function.getValue(row);

            if (value != null)
            {
              AttributeType attributeType = entry.getValue();

              this.setValue(entity, attributeType, attributeName, value);
            }
          }
        }

        ServiceFactory.getUtilities().applyGeoObject(entity, isNew);

        if (parent != null)
        {
          String parentTypeCode = parent.getType().getCode();
          String typeCode = entity.getType().getCode();
          String hierarchyCode = this.configuration.getHierarchy().getCode();
          RegistryService service = ServiceFactory.getRegistryService();

          if (isNew || !service.exists(parent.getUid(), parentTypeCode, entity.getUid(), typeCode, hierarchyCode))
          {
            service.addChildInTransaction(parent.getUid(), parentTypeCode, entity.getUid(), typeCode, hierarchyCode);
          }
        }

        // We must ensure that any problems created during the transaction are
        // logged now instead of when the request returns. As such, if any
        // problems exist immediately throw a ProblemException so that normal
        // exception handling can occur.
        List<ProblemIF> problems = RequestState.getProblemsInCurrentRequest();

        if (problems.size() != 0)
        {
          throw new ProblemException(null, problems);
        }
      }
    }
    catch (IgnoreRowException e)
    {
      // Do nothing
    }
  }

  /**
   * @param feature
   *          Shapefile feature
   * 
   * @return The geoId as defined by the 'oid' attribute on the feature. If the
   *         geoId is null then a blank geoId is returned.
   */
  private String getCode(FeatureRow row)
  {
    ShapefileFunction function = this.configuration.getFunction(GeoObject.CODE);

    if (function == null)
    {
      RequiredMappingException ex = new RequiredMappingException();
      ex.setAttributeLabel(this.configuration.getType().getAttribute(GeoObject.CODE).get().getLabel().getValue());
      throw ex;
    }

    Object geoId = function.getValue(row);

    if (geoId != null)
    {
      return geoId.toString();
    }

    return "";
  }

  /**
   * Returns the entity as defined by the 'parent' and 'parentType' attributes
   * of the given feature. If an entity is not found then Earth is returned by
   * default. The 'parent' value of the feature must define an entity name or a
   * geo oid. The 'parentType' value of the feature must define the localized
   * display label of the universal.
   *
   * @param feature
   *          Shapefile feature used to determine the parent
   * @return Parent entity
   */
  private GeoObject getParent(FeatureRow feature)
  {
    List<Location> locations = this.configuration.getLocations();

    GeoObject parent = null;

    JsonArray context = new JsonArray();

    for (Location location : locations)
    {
      ShapefileFunction function = location.getFunction();
      Object label = function.getValue(feature);

      if (label != null)
      {
        String key = parent != null ? parent.getCode() + "-" + label : label.toString();

        if (this.configuration.isExclusion(GeoObjectConfiguration.PARENT_EXCLUSION, key))
        {
          throw new IgnoreRowException();
        }

        // Search
        GeoObjectQuery query = new GeoObjectQuery(location.getType(), location.getUniversal());
        query.setRestriction(new SynonymRestriction(label.toString(), parent, this.configuration.getHierarchyRelationship()));

        try
        {

          GeoObject result = query.getSingleResult();

          if (result != null)
          {
            parent = result;

            JsonObject element = new JsonObject();
            element.addProperty("label", label.toString());
            element.addProperty("type", location.getType().getLabel().getValue());

            context.add(element);
          }
          else
          {
            if (context.size() == 0)
            {
              GeoObject root = this.configuration.getRoot();

              if (root != null)
              {
                JsonObject element = new JsonObject();
                element.addProperty("label", root.getLocalizedDisplayLabel());
                element.addProperty("type", root.getType().getLabel().getValue());

                context.add(element);
              }
            }

            this.configuration.addProblem(new GeoObjectLocationProblem(location.getType(), label.toString(), parent, context));

            return null;
          }
        }
        catch (NonUniqueResultException e)
        {
          AmbiguousParentException ex = new AmbiguousParentException();
          ex.setParentLabel(label.toString());
          ex.setContext(context.toString());

          throw ex;
        }
      }
    }

    return parent;
  }

  private GeoObject parsePostalCode(FeatureRow feature)
  {
    LocationBuilder builder = PostalCodeFactory.get(this.configuration.getType());
    Location location = builder.build(this.configuration.getFunction(GeoObject.CODE));

    ShapefileFunction function = location.getFunction();
    String code = (String) function.getValue(feature);

    if (code != null)
    {
      // Search
      GeoObjectQuery query = new GeoObjectQuery(location.getType(), location.getUniversal());
      query.setRestriction(new CodeRestriction(code));

      GeoObject result = query.getSingleResult();

      if (result != null)
      {
        return result;
      }
      else
      {
        PostalCodeLocationException e = new PostalCodeLocationException();
        e.setCode(code);
        e.setTypeLabel(location.getType().getLabel().getValue());

        throw e;
      }
    }

    return null;
  }

  /**
   * @param feature
   * @return The entityName as defined by the 'name' attribute of the feature
   */
  private LocalizedValue getName(FeatureRow row)
  {
    ShapefileFunction function = this.configuration.getFunction(GeoObject.DISPLAY_LABEL);

    if (function == null)
    {
      RequiredMappingException ex = new RequiredMappingException();
      ex.setAttributeLabel(this.configuration.getType().getAttribute(GeoObject.DISPLAY_LABEL).get().getLabel().getValue());
      throw ex;
    }

    Object attribute = function.getValue(row);

    if (attribute != null)
    {
      return (LocalizedValue) attribute;
    }

    return null;
  }

  protected void setTermValue(GeoObject entity, AttributeType attributeType, String attributeName, Object value)
  {
    if (!this.configuration.isExclusion(attributeName, value.toString()))
    {
      try
      {
        MdBusinessDAOIF mdBusiness = this.configuration.getMdBusiness();
        MdAttributeTermDAOIF mdAttribute = (MdAttributeTermDAOIF) mdBusiness.definesAttribute(attributeName);

        Classifier classifier = Classifier.findMatchingTerm(value.toString().trim(), mdAttribute);

        if (classifier == null)
        {
          Term rootTerm = ( (AttributeTermType) attributeType ).getRootTerm();

          this.configuration.addProblem(new TermProblem(value.toString(), rootTerm.getCode(), mdAttribute.getOid(), attributeName, attributeType.getLabel().getValue()));
        }
        else
        {
          entity.setValue(attributeName, classifier.getClassifierId());
        }
      }
      catch (UnknownTermException e)
      {
        TermValueException ex = new TermValueException();
        ex.setAttributeLabel(e.getAttribute().getLabel().getValue());
        ex.setCode(e.getCode());

        throw e;
      }
    }
  }
}