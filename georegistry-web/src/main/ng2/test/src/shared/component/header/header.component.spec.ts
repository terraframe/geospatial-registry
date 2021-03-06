import { TestBed, ComponentFixture, async, tick, fakeAsync } from "@angular/core/testing";
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from "@angular/core";
import { ModalModule, BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { RegistryRoleType } from "@shared/model/core";
import { CgrHeaderComponent } from "@shared/component/header/header.component";
import { ProfileService, AuthService, EventService, LocalizationService } from "@shared/service";

describe("CgrHeaderComponent", () => {
	let component: CgrHeaderComponent;
	let fixture: ComponentFixture<CgrHeaderComponent>;
	let profileService: ProfileService;
	let authService: AuthService;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [CgrHeaderComponent],
			schemas: [NO_ERRORS_SCHEMA],
			imports: [
				HttpClientTestingModule,
				ModalModule.forRoot()
			],
			providers: [
				BsModalService,
				BsModalRef,
				ProfileService,
				AuthService,
				EventService,
				LocalizationService
			]
		}).compileComponents();
	}));

	beforeEach(() => {
		TestBed.inject(EventService);
		TestBed.inject(LocalizationService).decode = jasmine.createSpy().and.returnValue("Test");
		profileService = TestBed.inject(ProfileService);
		authService = TestBed.inject(AuthService);

		authService.isAdmin = jasmine.createSpy().and.returnValue(true);
		profileService.get = jasmine.createSpy().and.returnValue(Promise.resolve({
			oid: "Test",
			username: "Test",
			password: "Test",
			firstName: "Test",
			lastName: "Test",
			email: "Test",
			changePassword: false
		}));

		// initialize component
		fixture = TestBed.createComponent(CgrHeaderComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it(`shouldShowMenuItem HIERARCHIES to be true`, async(() => {
		expect(component.shouldShowMenuItem('HIERARCHIES')).toBeTrue();
	}));

	it(`shouldShowMenuItem LISTS to be true`, async(() => {
		expect(component.shouldShowMenuItem('LISTS')).toBeTrue();
	}));

	it(`shouldShowMenuItem SETTINGS to be true`, async(() => {
		expect(component.shouldShowMenuItem('SETTINGS')).toBeTrue();
	}));

	it(`shouldShowMenuItem UNKNOWN to be true`, async(() => {
		expect(component.shouldShowMenuItem('UNKNOWN')).toBeFalse();
	}));

	it(`shouldShowMenuItem IMPORT`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return !(role === RegistryRoleType.RA || role === RegistryRoleType.RM)
		});

		expect(component.shouldShowMenuItem('IMPORT')).toBeFalse();
	}));

	it(`shouldShowMenuItem IMPORT`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return (role === RegistryRoleType.RA || role === RegistryRoleType.RM)
		});

		expect(component.shouldShowMenuItem('IMPORT')).toBeTrue();
	}));

	it(`shouldShowMenuItem SCHEDULED-JOBS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return !(role === RegistryRoleType.RA || role === RegistryRoleType.RM)
		});

		expect(component.shouldShowMenuItem('SCHEDULED-JOBS')).toBeFalse();
	}));

	it(`shouldShowMenuItem SCHEDULED-JOBS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return (role === RegistryRoleType.RA || role === RegistryRoleType.RM)
		});

		expect(component.shouldShowMenuItem('SCHEDULED-JOBS')).toBeTrue();
	}));

	it(`shouldShowMenuItem NAVIGATOR`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return !(role === RegistryRoleType.RA || role === RegistryRoleType.RM || role === RegistryRoleType.SRA || role === RegistryRoleType.RC)
		});

		expect(component.shouldShowMenuItem('NAVIGATOR')).toBeFalse();
	}));

	it(`shouldShowMenuItem NAVIGATOR`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return (role === RegistryRoleType.RA || role === RegistryRoleType.RM || role === RegistryRoleType.SRA || role === RegistryRoleType.RC)
		});

		expect(component.shouldShowMenuItem('NAVIGATOR')).toBeTrue();
	}));

	it(`shouldShowMenuItem CHANGE-REQUESTS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return !(role === RegistryRoleType.RA || role === RegistryRoleType.RM || role === RegistryRoleType.RC)
		});

		expect(component.shouldShowMenuItem('CHANGE-REQUESTS')).toBeFalse();
	}));

	it(`shouldShowMenuItem CHANGE-REQUESTS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return (role === RegistryRoleType.RA || role === RegistryRoleType.RM || role === RegistryRoleType.RC)
		});

		expect(component.shouldShowMenuItem('CHANGE-REQUESTS')).toBeTrue();
	}));

	it(`shouldShowMenuItem TASKS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return !(role === RegistryRoleType.RA || role === RegistryRoleType.RM || role === RegistryRoleType.SRA)
		});

		expect(component.shouldShowMenuItem('TASKS')).toBeFalse();
	}));

	it(`shouldShowMenuItem TASKS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return (role === RegistryRoleType.RA || role === RegistryRoleType.RM || role === RegistryRoleType.SRA)
		});

		expect(component.shouldShowMenuItem('TASKS')).toBeTrue();
	}));

	it(`shouldShowMenuItem CONFIGS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return !(role === RegistryRoleType.RA)
		});

		expect(component.shouldShowMenuItem('CONFIGS')).toBeFalse();
	}));

	it(`shouldShowMenuItem CONFIGS`, async(() => {
		authService.hasExactRole = jasmine.createSpy().and.callFake((role: RegistryRoleType) => {
			return (role === RegistryRoleType.RA)
		});

		expect(component.shouldShowMenuItem('CONFIGS')).toBeTrue();
	}));

	it(`Test getUsername`, async(() => {
		authService.getUsername = jasmine.createSpy().and.returnValue('tEST');

		expect(component.getUsername()).toEqual('tEST');
	}));

	it('Test account', fakeAsync(() => {
		component.account();
		
		tick(500);
		
		expect(component.bsModalRef).toBeTruthy();
	}));
});


