import { Component, OnInit, Input, ChangeDetectorRef, HostBinding } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subject } from 'rxjs/Subject';
import { HttpErrorResponse } from '@angular/common/http';
import {
  trigger,
  style,
  animate,
  transition,
} from '@angular/animations';

import { GeoObjectType, Attribute, ValueOverTime, GeoObjectOverTime, AttributeTerm } from '../../model/registry';

import { RegistryService } from '../../service/registry.service';

import { IOService } from '../../service/io.service';
import { LocalizationService } from '../../../shared/service/localization.service';

import Utils from '../../utility/Utils';


@Component( {
    selector: 'manage-versions-modal',
    templateUrl: './manage-versions-modal.component.html',
    styleUrls: [],
    host: { '[@fadeInOut]': 'true' },
    animations: [
        [
            trigger('fadeInOut', [
                transition('void => *', [
                    style({
                        opacity: 0
                    }),
                    animate('1000ms')
                ]),
                transition('* => void', [
                    style({
                        opacity: 0
                    }),
                    animate('1000ms')
                ])
            ])
        ]]
} )
export class ManageVersionsModalComponent implements OnInit {
    message: string = null;

    readonly: boolean = false;

    /*
     * Observable subject for MasterList changes.  Called when an update is successful 
     */
    onAttributeVersionChange: Subject<GeoObjectOverTime>;

    @Input() attribute: Attribute;

    @Input() geoObjectType: GeoObjectType;

    @Input() geoObjectOverTime: GeoObjectOverTime;

    goGeometries: GeoObjectOverTime;

    isNewGeoObject: boolean = false;

    newVersion: ValueOverTime;

    editingGeometry: number = -1;

    constructor( private service: RegistryService, private iService: IOService, private lService: LocalizationService, 
        public bsModalRef: BsModalRef, public changeDetectorRef: ChangeDetectorRef ) { }

    ngOnInit(): void {

        this.onAttributeVersionChange = new Subject();
    }

    tfInit(): void {
      if (this.attribute.code === 'geometry' && this.geoObjectOverTime.attributes[this.attribute.code].values.length === 1)
	  {
	    this.editingGeometry = 0;
	  }
    }

    onDateChange(event: any, vAttribute: ValueOverTime): any {
        let dt = new Date(event);
        let vAttributes = this.geoObjectOverTime.attributes[this.attribute.code].values;

        vAttribute.startDate = Utils.formatDateString(dt);

        this.snapDates(vAttributes);

        this.changeDetectorRef.detectChanges();
    }

    snapDates(votArr: ValueOverTime[]): void{
        var dateOffset = (24*60*60*1000) * 1; //1 days

        // Sort the data by start date 
        votArr.sort( function( a, b ) {

            if ( a.startDate == null || a.startDate === '') {
                return 1;
            }
            else if ( b.startDate == null  || b.startDate === '' ) {
                return -1;
            }

            let first: any = new Date( a.startDate );
            let next: any = new Date( b.startDate );
            return first - next;
        } );
        
        for ( let i = 1; i < votArr.length; i++ ) {
            let prev = votArr[i - 1];
            let current = votArr[i];

            if(current.startDate){
                prev.endDate = Utils.formatDateString( new Date( new Date( current.startDate ).getTime() - dateOffset ) );
            }
            else{
                prev.endDate = '5000-12-31';
            }
        }
        
        votArr[votArr.length - 1].endDate = '5000-12-31';
        
        console.log(votArr);
    }

    onValidChange(geometryValue): void {
      console.log("Valid Change : " + geometryValue);
    }

    onAddNewVersion(): void {

        let votArr: ValueOverTime[] = this.geoObjectOverTime.attributes[this.attribute.code].values;

        let vot: ValueOverTime = new ValueOverTime();
        vot.startDate = null;  // Utils.formatDateString(new Date());
        vot.endDate = null;  // Utils.formatDateString(new Date());

        if(this.isNewGeoObject){

        	if(this.attribute.type === "local"){
	        //   vot.value = {"localizedValue":null,"localeValues":[{"locale":"defaultLocale","value":null},{"locale":"km_KH","value":null}]};
                vot.value = this.lService.create();
            }
	        else if(this.attribute.type === 'geometry'){
	          vot.value = {"type":"MultiPolygon", "coordinates":[]}; // TODO: This incorrectly assumes MultiPolygon
	        }
        }
        else{
            if(this.attribute.type === "local"){
                vot.value = this.lService.create();
            }
            // else if(attributeType === 'geometry'){
	        //   vot.value = {"type":"MultiPolygon", "coordinates":[]}; // TODO: This incorrectly assumes MultiPolygon
	        // }
        }

        votArr.push(vot);

        if(this.attribute.code === 'geometry'){
		  this.editingGeometry = votArr.length - 1;
		}

        this.snapDates(votArr);

        // this.changeDetectorRef.detectChanges();
    }


    getEmptyValueOverTimeObject(existingObject: ValueOverTime): ValueOverTime {

        let newObj = JSON.parse(JSON.stringify(existingObject));

        if(this.attribute.type === 'local'){
            newObj.localeValues.forEach(val => {
                val.value = null;
            });
        }

        return newObj;
    }


    editGeometry(index: number) {
      this.editingGeometry = index;
    }

    getVersionData(attribute: Attribute) {
        let versions: ValueOverTime[] = [];

        this.geoObjectOverTime.attributes[attribute.code].values.forEach(vAttribute => {
            vAttribute.value.localeValues.forEach(val => {
                versions.push(val);
            })
        })
        return versions;
    }

    getDefaultLocaleVal(locale: any): string {
        let defVal = null;

        locale.localeValues.forEach(locVal => {
            if(locVal.locale === 'defaultLocale'){
               defVal = locVal.value;
            }

        })

        return defVal; 
    }

    getGeoObjectTypeTermAttributeOptions( termAttributeCode: string ) {
        for ( let i = 0; i < this.geoObjectType.attributes.length; i++ ) {
            let attr: any = this.geoObjectType.attributes[i];

            if ( attr.type === "term" && attr.code === termAttributeCode ) {

                attr = <AttributeTerm>attr;
                let attrOpts = attr.rootTerm.children;

                if ( attrOpts.length > 0 ) {
                    return Utils.removeStatuses( JSON.parse( JSON.stringify( attrOpts ) ) );
                }
            }
        }

        return null;
    }

    remove(version: any ): void {

        let val = this.geoObjectOverTime.attributes[this.attribute.code];

        for(let i=0; i<val.values.length; i++){
            let vals = val.values[i];

            if(vals.startDate === version.startDate){
                val.values.splice(i, 1);
            }
        }
    }

    isChangeOverTime(attr: Attribute): boolean{
        let isChangeOverTime = false;

        this.geoObjectType.attributes.forEach(attribute => {
            if(this.attribute.code === attr.code){
                isChangeOverTime = attr.isChangeOverTime
            }
        })

        return isChangeOverTime;
    }

    onSubmit(): void {

        this.onAttributeVersionChange.next( this.geoObjectOverTime );

        this.bsModalRef.hide();
    }

    onCancel(): void {
        this.bsModalRef.hide()
    }

    error( err: HttpErrorResponse ): void {
        // Handle error
        if ( err !== null ) {
            this.message = ( err.error.localizedMessage || err.error.message || err.message );
        }
    }

}
