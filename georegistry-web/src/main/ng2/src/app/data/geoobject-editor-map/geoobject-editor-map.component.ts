import { Component, OnInit, ViewChild, ElementRef, TemplateRef, ChangeDetectorRef, Input, Output, EventEmitter } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { DatePipe } from '@angular/common';

import { ErrorModalComponent } from '../../core/modals/error-modal.component';
import { AttributeInputComponent } from '../hierarchy/geoobjecttype-management/attribute-input.component';

import { HierarchyService } from '../../service/hierarchy.service';
import { RegistryService } from '../../service/registry.service';
import { ChangeRequestService } from '../../service/change-request.service';


import { IOService } from '../../service/io.service';
import { GeoObjectType, GeoObject, Attribute, AttributeTerm, AttributeDecimal, Term, ParentTreeNode } from '../../model/registry';

import { GeoObjectAttributeExcludesPipe } from '../../data/geoobject-shared-attribute-editor/geoobject-attribute-excludes.pipe';
import { ToEpochDateTimePipe } from '../../data/submit-change-request/to-epoch-date-time.pipe';

import { Observable } from 'rxjs';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead';
import { mergeMap } from 'rxjs/operators';

import { AuthService } from '../../core/auth/auth.service';

import { Map, LngLatBounds, NavigationControl, ImageSource } from 'mapbox-gl';
import * as MapboxDraw from '@mapbox/mapbox-gl-draw';
import * as StaticMode from '@mapbox/mapbox-gl-draw-static-mode';
import * as mapboxgl from 'mapbox-gl';

declare var acp: string;


@Component({
    selector: 'geoobject-editor-map',
    templateUrl: './geoobject-editor-map.component.html',
    styleUrls: ['./geoobject-editor-map.component.css']
})

/**
 * This component is used in the master list when editing a row. In the future it will also be used by the navigator and has
 * potential to also be used in the submit change request and manage change requests.
 */
export class GeoObjectEditorMapComponent implements OnInit {
    
    /* 
     * mapbox-gl map
     */
    map: Map;

    /* 
     * Draw control
     */
    draw: MapboxDraw;
    
    /* 
     * List of base layers
     */
    baseLayers: any[] = [{
        label: 'Outdoors',
        id: 'outdoors-v11',
        selected: true
    }, {
        label: 'Satellite',
        id: 'satellite-v9'
    }, {
        label: 'Streets',
        id: 'streets-v11'
    }];

    layers: any[] = [];
    
    editingControl: any;
    
    geoprismEditingControl: any;
    
    isEditing: boolean;
    
    updatedGeos: any;
    
    deletedGeos: any;
    
    /*
	 * The state of the GeoObject after our edit has been applied 
	 */
    @Input() geoObject: any = {};
    
    constructor(private service: IOService, private modalService: BsModalService, public bsModalRef: BsModalRef, private registryService: RegistryService) {
            
    }
    
    ngOnInit(): void {
      
    }
    
    ngAfterViewInit() {
		setTimeout(() => { 
	        ( mapboxgl as any ).accessToken = 'pk.eyJ1IjoidGVycmFmcmFtZSIsImEiOiJjanZxNTFnaTYyZ2RuNDlxcmNnejNtNjN6In0.-kmlS8Tgb2fNc1NPb5rJEQ';

	        this.map = new Map( {
	            container: 'map',
	            style: 'mapbox://styles/mapbox/outdoors-v11',
	            zoom: 2,
	            center: [110.880453, 10.897852]
	        } );
	        
	        this.map.on( 'load', () => {
	            this.initMap();
	        } );
	    }, 10);
    }
    
    initMap(): void {

        this.map.on( 'style.load', () => {
            //this.addLayers();
            this.refresh( false );
        } );

        //this.addLayers();

        this.refresh( true );

        // Add zoom and rotation controls to the map.
        this.map.addControl( new NavigationControl() );

        //if ( this.admin ) {
            this.editingControl = new MapboxDraw({
			    controls : {
			      point : true,
			      line_string : false,
			      polygon : true,
			      trash : false,
			      combine_features : false,
			      uncombine_features : false
			    }
			});
			this.map.addControl(this.editingControl);
			  
			// Define the GeoprismEditingControl
			//this.geoprismEditingControl = new GeoprismEditingControl();
			//this.map.addControl(this.geoprismEditingControl);
			
			//this.isEditing = false;
			
			console.log("Adding geoObject to editing control");
			this.editingControl.add(this.geoObject);
			  
			this.updatedGeos = {};
			this.map.on( "draw.update", ( $event ) => { this.onDrawUpdate( $event ) } );
			  
			this.deletedGeos = {};
            this.map.on( "draw.delete", ( $event ) => { this.onDrawDelete( $event ) } );
        //}
    }
    
    addLayers(): void {

        //this.map.addSource( 'geoobject', {
        //    type: 'geojson',
        //    data: {
        //        "type": "FeatureCollection",
        //        "features": []
        //    }
        //} );

        // GeoObject Layer
        //this.map.addLayer( {
        //    "id": "geoobject",
        //    "type": "fill",
        //    "source": 'geoobject',
        //    "paint": {
        //       "fill-color": "#848484",
        //       "fill-outline-color": "black",
        //       "fill-opacity": 0.5,
               //"fill-stroke-width": 5,
               //"fill-stroke-color": "#000000"
        //    },
        //} );
    }
    
    refresh( zoom: boolean ): void {
        //( <any>this.map.getSource( 'geoobject' ) ).setData( this.geoObject );

        if ( zoom ) {
          
          this.registryService.getGeoObjectBounds(this.geoObject.properties.code, this.geoObject.properties.type)
            .then(boundArr => {
              let bounds = new LngLatBounds( [boundArr[0], boundArr[1]], [boundArr[2], boundArr[3]] );

              this.map.fitBounds( bounds, { padding: 50 } );
            }).catch((err: Response) => {
                this.error(err.json());
            });
        }
    }
    
    onDrawUpdate( event: any ): void {
        //if ( event.action === 'move' && event.features != null && event.features.length > 0 ) {
        //    this.updateGeometry( event.features[0] )
        //}
    }

    onDrawDelete( event: any ): void {
    }
    
    updateGeometry( feature: any ): void {
    }
    
    saveDraw(): GeoObject {
      let featureCollection: any = this.editingControl.getAll();
      let geoObject = featureCollection.features[0];
    
      console.log("save draw", geoObject);
      
      this.geoObject = geoObject;
      
      return this.geoObject;
    }
    
    cancelDraw(): void {
        //this.draw.deleteAll();
        //this.draw.changeMode( 'static' );

        // Most be after the draw has been added to trigger a repaint of the map
        //this.map.setFilter( "points" );
        //this.map.setFilter( "points-label" );
        //this.active = false;
    }
    
    public error(err: any): void {
        // TODO
        
        // Handle error
        //if (err !== null) {
        //    this.bsModalRef = this.modalService.show(ErrorModalComponent, { backdrop: true });
        //    this.bsModalRef.content.message = (err.localizedMessage || err.message);
        //}
    }
    
    
}