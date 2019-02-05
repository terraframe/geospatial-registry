import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

import { GeoObjectType } from '../../hierarchy/hierarchy';
import { ImportConfiguration } from '../io';

import { IOService } from '../../../service/io.service';

@Component( {
    selector: 'shapefile-modal',
    templateUrl: './shapefile-modal.component.html',
    styleUrls: []
} )
export class ShapefileModalComponent implements OnInit {

    configuration: ImportConfiguration;
    message: string = null;
    state: string = 'MAP';

    constructor( private service: IOService, public bsModalRef: BsModalRef ) {
    }

    ngOnInit(): void {
    }

    onStateChange( event: string ): void {
        if ( event === 'BACK' ) {
            this.handleBack();
        }
        else if ( event === 'NEXT' ) {
            this.handleNext();
        }
        else if ( event === 'CANCEL' ) {
            this.handleCancel();
        }
    }

    handleBack(): void {
        if ( this.state === 'LOCATION' ) {
            this.state = 'MAP';
        }
    }

    handleNext(): void {
        if ( this.state === 'MAP' ) {
            this.state = 'LOCATION';
        }
        else if ( this.state === 'LOCATION' ) {
            this.handleSubmit();
        }
        else if ( this.state === 'LOCATION-PROBLEM' ) {

            if ( this.configuration.termProblems != null ) {

            }
            else {
                this.handleSubmit();
            }
        }
    }

    handleSubmit(): void {
        this.service.importShapefile( this.configuration ).then( config => {

            if ( config.locationProblems != null ) {
                this.state = 'LOCATION-PROBLEM';
                this.configuration = config;
            }
            else if ( config.termProblems != null ) {
                this.state = 'TERM-PROBLEM';
                this.configuration = config;
            }
            else {
                this.bsModalRef.hide()
            }
        } ).catch(( err: any ) => {
            this.error( err.json() );
        } );

    }

    handleCancel(): void {
        this.service.cancelShapefileImport( this.configuration ).then( response => {
            this.bsModalRef.hide()
        } ).catch(( err: any ) => {
            this.error( err.json() );
        } );
    }

    error( err: any ): void {
        // Handle error
        if ( err !== null ) {
            this.message = ( err.localizedMessage || err.message );
        }
    }
}