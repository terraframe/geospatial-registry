import { Component, OnInit, Input, Output, ChangeDetectorRef, EventEmitter } from '@angular/core';
import {
	trigger,
	style,
	animate,
	transition,
} from '@angular/animations';

import { GeoObjectType, Attribute, ValueOverTime, GeoObjectOverTime, AttributeTerm, PRESENT } from '@registry/model/registry';

import { RegistryService, IOService } from '@registry/service';

import { LocalizationService } from '@shared/service';

import Utils from '../../utility/Utils';


@Component({
	selector: 'manage-versions',
	templateUrl: './manage-versions.component.html',
	styleUrls: ['./manage-versions.css'],
	host: { '[@fadeInOut]': 'true' },
	animations: [
		[
			trigger('fadeInOut', [
				transition('void => *', [
					style({
						opacity: 0
					}),
					animate('500ms')
				]),
				transition(':leave',
					animate('500ms', 
						style({
							opacity: 0
						})
					)
				)
			])
		]]
})
export class ManageVersionsComponent implements OnInit {
	message: string = null;
	
	currentDate : Date = new Date();

	@Input() readonly: boolean = false;

    /*
     * Observable subject for MasterList changes.  Called when an update is successful 
     */
	@Output() onChange = new EventEmitter<GeoObjectOverTime>()

	@Input() attribute: Attribute

	@Input() geoObjectType: GeoObjectType;

	originalGeoObjectOverTime: GeoObjectOverTime;
	geoObjectOverTime: GeoObjectOverTime;

	@Input() set geoObjectOverTimeInput(value: GeoObjectOverTime) {
		this.originalGeoObjectOverTime = JSON.parse(JSON.stringify(value));
		this.geoObjectOverTime = value;
	}

	@Input() isNewGeoObject: boolean = false;

	goGeometries: GeoObjectOverTime;

	newVersion: ValueOverTime;

	editingGeometry: number = -1;

	hasDuplicateDate: boolean = false;

	hasConflict: boolean = false;

	originalAttributeState: Attribute;

	constructor(private service: RegistryService, private lService: LocalizationService, public changeDetectorRef: ChangeDetectorRef) { }

	ngOnInit(): void {
	}
	
	ngAfterViewInit() {
		if (this.attribute.code === 'geometry' && this.geoObjectOverTime.attributes[this.attribute.code].values.length === 1) {
			
			setTimeout(() => {
				this.editingGeometry = 0;
			}, 0);
		}
	}

	geometryChange(vAttribute, event): void {
		vAttribute.value = event;
	}

	onDateChange(): any {
		this.hasConflict = false;
	
		let vAttributes = this.geoObjectOverTime.attributes[this.attribute.code].values;


		// check ranges
		for (let j = 0; j < vAttributes.length; j++) {
			const h1 = vAttributes[j];
			h1.conflict = false;
			h1.conflictMessage = [];

			if (!(h1.startDate == null || h1.startDate === '') && !(h1.endDate == null || h1.endDate === '')) {
				let s1: any = new Date(h1.startDate);
				let e1: any = new Date(h1.endDate);
				
				if (Utils.dateEndBeforeStart(s1, e1)) {
					h1.conflict = true;		
					h1.conflictMessage.push(this.lService.decode("manage.versions.startdate.later.enddate.message")); 
					this.hasConflict = true;
				}

				for (let i = 0; i < vAttributes.length; i++) {

					if (j !== i) {
						const h2 = vAttributes[i];
						if (!(h2.startDate == null || h2.startDate === '') && !(h2.endDate == null || h2.endDate === '')) {
							let s2: any = new Date(h2.startDate);
							let e2: any = new Date(h2.endDate);

							// Determine if there is an overlap
							if (Utils.dateRangeOverlaps(s1.getTime(), e1.getTime(), s2.getTime(), e2.getTime())) {
								h1.conflict = true
								h1.conflictMessage.push(this.lService.decode("manage.versions.overlap.message"));

								this.hasConflict = true;
							}
							
						}
					}
				}
			}
		}
		
		this.sort(vAttributes);
	}
	
	onAddNewVersion(): void {
		let votArr: ValueOverTime[] = this.geoObjectOverTime.attributes[this.attribute.code].values;

		let vot: ValueOverTime = new ValueOverTime();
		vot.startDate = null;  // Utils.formatDateString(new Date());
		vot.endDate = null;  // Utils.formatDateString(new Date());

		if (this.attribute.type === "local") {
			//   vot.value = {"localizedValue":null,"localeValues":[{"locale":"defaultLocale","value":null},{"locale":"km_KH","value":null}]};
			vot.value = this.lService.create();
		}
		else if (this.attribute.type === 'geometry') {

			if (votArr.length > 0) {
				if (this.editingGeometry != -1 && this.editingGeometry != null) {
					vot.value = votArr[this.editingGeometry].value;
				}
				else {
					vot.value = votArr[0].value;
				}
			}
			else {
				vot.value = { "type": this.geoObjectType.geometryType, "coordinates": [] };

				if (this.geoObjectType.geometryType === "MULTIPOLYGON") {
					vot.value.type = "MultiPolygon";
				}
				else if (this.geoObjectType.geometryType === "POLYGON") {
					vot.value.type = "Polygon";
				}
				else if (this.geoObjectType.geometryType === "POINT") {
					vot.value.type = "Point";
				}
				else if (this.geoObjectType.geometryType === "MULTIPOINT") {
					vot.value.type = "MultiPoint";
				}
				else if (this.geoObjectType.geometryType === "LINE") {
					vot.value.type = "Line";
				}
				else if (this.geoObjectType.geometryType === "MULTILINE") {
					vot.value.type = "MultiLine";
				}
			}
		}
		else if (this.attribute.type === 'term') {
			var terms = this.getGeoObjectTypeTermAttributeOptions(this.attribute.code);

			if (terms.length > 0) {
				vot.value = terms[0].code;
			}
		}

		votArr.push(vot);

		if (this.attribute.code === 'geometry') {
			this.editingGeometry = votArr.length - 1;
		}

		this.changeDetectorRef.detectChanges();
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
			if (locVal.locale === 'defaultLocale') {
				defVal = locVal.value;
			}

		})

		return defVal;
	}

	setDateAttribute(vot: ValueOverTime, val: string): void {
		vot.value = new Date(val).getTime().toString()
	}

	getGeoObjectTypeTermAttributeOptions(termAttributeCode: string) {
		for (let i = 0; i < this.geoObjectType.attributes.length; i++) {
			let attr: any = this.geoObjectType.attributes[i];

			if (attr.type === "term" && attr.code === termAttributeCode) {

				attr = <AttributeTerm>attr;
				let attrOpts = attr.rootTerm.children;

				// only remove status of the required status type
				if (attrOpts.length > 0) {
					if (attr.code === "status") {
						return Utils.removeStatuses(attrOpts);
					}
					else {
						return attrOpts;
					}
				}
			}
		}

		return null;
	}

	remove(version: any): void {

		let val = this.geoObjectOverTime.attributes[this.attribute.code];

		let position = -1;
		for (let i = 0; i < val.values.length; i++) {
			let vals = val.values[i];


			if (vals.startDate === version.startDate) {
				position = i
			}
		}
		
		if(position > -1){
			val.values.splice(position, 1);
		}
	}

	isChangeOverTime(attr: Attribute): boolean {
		let isChangeOverTime = false;

		this.geoObjectType.attributes.forEach(attribute => {
			if (this.attribute.code === attr.code) {
				isChangeOverTime = attr.isChangeOverTime
			}
		})

		return isChangeOverTime;
	}
	
	setInfinity(vAttribute, attributes): void {
		
		if(vAttribute.endDate === PRESENT){
			vAttribute.endDate = new Date();
		}
		else{
			vAttribute.endDate = PRESENT
		}
		
		this.onDateChange();
		
		this.sort(attributes)
	}
	
	sort(votArr: ValueOverTime[]): void {

		// Sort the data by start date 
		votArr.sort(function(a, b) {

			if (a.startDate == null || a.startDate === '') {
				return 1;
			}
			else if (b.startDate == null || b.startDate === '') {
				return -1;
			}

			let first: any = new Date(a.startDate);
			let next: any = new Date(b.startDate);
			return first - next;
		});
	}
	
	onSubmit(): void {

		this.onChange.emit(this.geoObjectOverTime);
	}

	onCancel(): void {
		this.onChange.emit(this.originalGeoObjectOverTime);
	}
}
