package net.geoprism.registry.etl;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.system.scheduler.JobHistory;

@Controller(url = "etl")
public class ETLController
{
  protected ETLService service;
  
  public ETLController()
  {
    this.service = new ETLService();
  }
  
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON, url = "import")
  public ResponseIF doImport(ClientRequestIF request, @RequestParamter(name = "json") String json)
  {
    JSONObject config = this.service.doImport(request.getSessionId(), json);
    
    return new RestBodyResponse(config.toString());
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON, url = "get-active")
  public ResponseIF getActiveImports(ClientRequestIF request, @RequestParamter(name = "pageSize") Integer pageSize, @RequestParamter(name = "pageNumber") Integer pageNumber, @RequestParamter(name = "sortAttr") String sortAttr, @RequestParamter(name = "isAscending") Boolean isAscending)
  {
    if (sortAttr == null || sortAttr == "")
    {
      sortAttr = JobHistory.CREATEDATE;
    }
    
    if (isAscending == null)
    {
      isAscending = true;
    }
    
    JSONArray config = this.service.getActiveImports(request.getSessionId(), pageSize, pageNumber, sortAttr, isAscending);
    
    return new RestBodyResponse(config.toString());
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON, url = "get-completed")
  public ResponseIF getCompletedImports(String sessionId, @RequestParamter(name = "pageSize") Integer pageSize, @RequestParamter(name = "pageNumber") Integer pageNumber, @RequestParamter(name = "sortAttr") String sortAttr, @RequestParamter(name = "isAscending") Boolean isAscending)
  {
    if (sortAttr == null || sortAttr == "")
    {
      sortAttr = JobHistory.CREATEDATE;
    }
    
    if (isAscending == null)
    {
      isAscending = true;
    }
    
    JSONArray config = this.service.getCompletedImports(sessionId, pageSize, pageNumber, sortAttr, isAscending);
    
    return new RestBodyResponse(config.toString());
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON, url = "get-errors")
  public ResponseIF getImportErrors(String sessionId, @RequestParamter(name = "historyId") String historyId, @RequestParamter(name = "pageSize") Integer pageSize, @RequestParamter(name = "pageNumber") Integer pageNumber)
  {
    JSONArray config = this.service.getImportErrors(sessionId, historyId, pageSize, pageNumber);
    
    return new RestBodyResponse(config.toString());
  }
  
}