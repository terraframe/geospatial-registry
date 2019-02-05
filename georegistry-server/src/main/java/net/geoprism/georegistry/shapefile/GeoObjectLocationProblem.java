/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.georegistry.shapefile;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GeoObjectLocationProblem implements Comparable<GeoObjectLocationProblem>
{
  private GeoObjectType type;

  private GeoObject     parent;

  private String        label;

  private JsonArray     context;

  public GeoObjectLocationProblem(GeoObjectType type, String label, GeoObject parent, JsonArray context)
  {
    this.type = type;
    this.label = label;
    this.context = context;
  }

  public String getKey()
  {
    if (this.parent != null)
    {
      return this.parent.getCode() + "-" + this.label;
    }
    else
    {
      return this.label;
    }
  }

  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty("label", label);
    object.addProperty("type", this.type.getCode());
    object.addProperty("typeLabel", this.type.getLocalizedLabel());
    object.add("context", context);

    if (this.parent != null)
    {
      object.addProperty("parent", this.parent.getCode());
    }

    return object;
  }

  @Override
  public int compareTo(GeoObjectLocationProblem o)
  {
    return this.getKey().compareTo(o.getKey());
  }
}