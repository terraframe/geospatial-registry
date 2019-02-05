///
/// Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
///
/// This file is part of Runway SDK(tm).
///
/// Runway SDK(tm) is free software: you can redistribute it and/or modify
/// it under the terms of the GNU Lesser General Public License as
/// published by the Free Software Foundation, either version 3 of the
/// License, or (at your option) any later version.
///
/// Runway SDK(tm) is distributed in the hope that it will be useful, but
/// WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU Lesser General Public License for more details.
///
/// You should have received a copy of the GNU Lesser General Public
/// License along with Runway SDK(tm).  If not, see <ehttp://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, Response, URLSearchParams } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/finally';
import { Observable } from 'rxjs/Observable';

import { EventService } from '../event/event.service';

import { AllLocaleInfo } from '../data/localization-manager/localization-manager';

declare var acp: any;

@Injectable()
export class LocalizationManagerService {

    constructor( private http: Http, private eventService: EventService ) { }

    getNewLocaleInfo( ): Promise<AllLocaleInfo> {
        return this.http
          .get( acp + '/localization/getNewLocaleInformation' )
          .toPromise()
          .then( response => {
              return response.json() as AllLocaleInfo;
          } )
    }
    
    installLocale( language: string, country: string, variant: string ): Promise<Response> {
        let params: URLSearchParams = new URLSearchParams();
        params.set( 'language', language );
        params.set( 'country', country );
        params.set( 'variant', variant );
    
        return this.http
          .get( acp + '/localization/installLocale', {params: params} )
          .toPromise()
          .then( response => {
              return response;
          } )
    }
    
}