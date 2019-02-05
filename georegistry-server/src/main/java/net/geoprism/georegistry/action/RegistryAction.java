package net.geoprism.georegistry.action;

import net.geoprism.georegistry.service.RegistryService;

import org.commongeoregistry.adapter.action.AbstractAction;
import org.commongeoregistry.adapter.action.AddChildAction;
import org.commongeoregistry.adapter.action.CreateAction;
import org.commongeoregistry.adapter.action.UpdateAction;

abstract public class RegistryAction
{
    public static RegistryAction convert(AbstractAction action, RegistryService registry, String sessionId)
    {
      if (action instanceof UpdateAction)
      {
        return new RegistryUpdateAction((UpdateAction) action, registry, sessionId);
      }
      else if (action instanceof CreateAction)
      {
        return new RegistryCreateAction((CreateAction) action, registry, sessionId);
      }
      else if (action instanceof AddChildAction)
      {
        return new RegistryAddChildAction((AddChildAction) action, registry, sessionId);
      }
//      else if (action instanceof DeleteAction)
//      {
//        return new RegistryDeleteAction((DeleteAction) action, registry, sessionId);
//      }
      else
      {
        throw new UnsupportedOperationException(action.getClass().getName());
      }
    }

    abstract public void execute();
}