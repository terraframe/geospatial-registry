import { Component, OnInit, Input, EventEmitter, Output, ViewChild } from '@angular/core';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ParentTreeNode, GeoObject, LocalizedValue } from '../../model/registry';
import { RegistryService } from '../../service/registry.service';

import { ErrorModalComponent } from '../../core/modals/error-modal.component';

@Component( {

    selector: 'cascading-geo-selector',
    templateUrl: './cascading-geo-selector.html',
} )
export class CascadingGeoSelector {

    @Input() hierarchies: any;

    @Output() valid = new EventEmitter<boolean>();

    @Input() isValid: boolean = true;

    @ViewChild( "mainForm" ) mainForm;

    parentMap: any = {};

    bsModalRef: BsModalRef;

    constructor( private modalService: BsModalService, private registryService: RegistryService ) {

    }

    ngOnInit(): void {
        for ( var i = 0; i < this.hierarchies.length; ++i ) {
            var hierarchy = this.hierarchies[i];

            for ( var j = 0; j < hierarchy.parents.length; ++j ) {
                if ( hierarchy.parents[j].ptn == null ) {
                    var ptn = new ParentTreeNode();

                    ptn.geoObject = this.newGeoObject();
                    ptn.hierarchyType = hierarchy.code;

                    hierarchy.parents[j].ptn = ptn;
                }
            }
        }

        console.log( "hierarchies after populate", this.hierarchies );
    }

    valueChange( newValue: string, index: number, hierarchy: any ): void {
        let invalid: boolean = false;

        for ( let i = index; i < hierarchy.parents.length; ++i ) {
            let parent = hierarchy.parents[i];

            parent.ptn.geoObject = this.newGeoObject();

            if ( i === index ) {
                parent.ptn.geoObject.properties.displayLabel.localizedValue = newValue;
            }

            invalid = true;
        }

        this.valid.emit();
    }

    private newGeoObject(): GeoObject {
        let go = new GeoObject();
        go.properties = {
            uid: "",
            code: "",
            displayLabel: new LocalizedValue(),
            type: "",
            status: [""],
            sequence: "",
            createDate: "",
            lastUpdateDate: ""
        };

        return go;
    }

    getTypeAheadObservable( text, parent, hierarchy, index ) {
        let geoObjectTypeCode = parent.code;

        let parentCode = null;
        let hierarchyCode = null;
        if ( index > 0 ) {
            let parentParentType = hierarchy.parents[index - 1];

            if ( parentParentType.ptn.geoObject.properties.code != null ) {
                hierarchyCode = hierarchy.code;
                parentCode = parentParentType.ptn.geoObject.properties.code;
            }
        }

        return Observable.create(( observer: any ) => {
            this.registryService.getGeoObjectSuggestions( text, geoObjectTypeCode, parentCode, hierarchyCode ).then( results => {
                observer.next( results );
            } );
        } );
    }

    typeaheadOnSelect( e: TypeaheadMatch, parent: any ): void {
        let ptn: ParentTreeNode = parent.ptn;

        this.registryService.getGeoObjectByCode( e.item.code, parent.code )
            .then( geoObject => {

                ptn.geoObject = geoObject;

                this.valid.emit();

            } ).catch(( err: Response ) => {
                this.error( err.json() );
            } );
    }

    public getIsValid(): boolean {
        return this.getParents() != null;
    }

    public getHierarchies(): any {
        return this.hierarchies;
    }

    public getParents(): any {
        return CascadingGeoSelector.staticGetParents( this.hierarchies );
    }

    public static staticGetParents( hierarchies: any ): ParentTreeNode {
        let ptn = new ParentTreeNode();
        ptn.parents = [];

        for ( var i = 0; i < hierarchies.length; ++i ) {
            let hierarchy: any = hierarchies[i];

            if ( hierarchy.parents.length > 0 ) {
                let parent: any = hierarchy.parents[hierarchy.parents.length - 1];

                if ( parent.ptn != null && parent.ptn.geoObject != null && parent.ptn.geoObject.properties.code.length > 0 ) {
                    ptn.parents.push( parent.ptn );
                }
            }
        }

        if ( ptn.parents.length > 0 ) {
            return ptn;
        }
        else {
            return null;
        }
    }

    public error( err: any ): void {
        // Handle error
        if ( err !== null ) {
            let bsModalRef = this.modalService.show( ErrorModalComponent, { backdrop: true } );
            bsModalRef.content.message = ( err.localizedMessage || err.message );
        }
    }

}