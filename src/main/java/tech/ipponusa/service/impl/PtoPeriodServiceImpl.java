package tech.ipponusa.service.impl;

import tech.ipponusa.service.PtoPeriodService;
import tech.ipponusa.domain.Employee;
import tech.ipponusa.domain.PtoPeriod;
import tech.ipponusa.domain.PtoRequest;
import tech.ipponusa.domain.User;
import tech.ipponusa.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;
import tech.ipponusa.repository.EmployeeRepository;
import tech.ipponusa.repository.PtoPeriodRepository;
import tech.ipponusa.repository.PtoRequestRepository;
import tech.ipponusa.repository.UserRepository;
import tech.ipponusa.security.SecurityUtils;
import tech.ipponusa.service.dto.PtoPeriodDTO;
import tech.ipponusa.service.mapper.PtoPeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PtoPeriod.
 */
@Service
@Transactional
public class PtoPeriodServiceImpl implements PtoPeriodService{

    private final Logger log = LoggerFactory.getLogger(PtoPeriodServiceImpl.class);
    
    @Inject
	private PtoPeriodRepository ptoPeriodRepository;

	@Inject
	private PtoRequestRepository ptoRequestRepository;

	@Inject
	private PtoPeriodMapper ptoPeriodMapper;

	@Inject
	private EmployeeRepository employeeRepository;

	@Inject
	private UserRepository userRepository;

	/**
	 * Save a ptoPeriod.
	 *
	 * @param ptoPeriodDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public PtoPeriodDTO save(PtoPeriodDTO ptoPeriodDTO) {
		log.debug("Request to save PtoPeriod : {}", ptoPeriodDTO);
		PtoPeriod ptoPeriod = ptoPeriodMapper.ptoPeriodDTOToPtoPeriod(ptoPeriodDTO);

		ptoPeriod = ptoPeriodRepository.save(ptoPeriod);
		PtoPeriodDTO result = ptoPeriodMapper.ptoPeriodToPtoPeriodDTO(ptoPeriod);
		return result;
	}


	private PtoPeriodDTO initialPtoPeriod() {

		PtoPeriodDTO init = new PtoPeriodDTO();

		// Modify the PTO period based on who is logged in.
		String login = SecurityUtils.getCurrentUserLogin();
		log.info("PTO Period Info");
		log.info("User Requesting:" + login);

		// Get the email address from the user table
		// Use email address to lookup the employee ID
		// From there we can determine the PTO period and check balances
		User user = userRepository.findOneByLogin(login).get();
		log.info("User Found: " + user.getFirstName() + " " + user.getLastName());
		log.info("User Email Address: " + user.getEmail());

		init = addEmployeeInfo(init, user);
		return init;
	}

	private PtoPeriodDTO addEmployeeInfo(PtoPeriodDTO init, User user) {

		Employee employee = findEmployeeByEmail(user.getEmail());
		ZonedDateTime endDate = employee.getHireDate().plusYears(1);

		init.setEndDate(endDate);
		init.setDaysInPeriod(employee.getHireDate().until(endDate, ChronoUnit.DAYS));
		init.setEmployeeId(employee.getId());

		init = addAllowedPTO(init, employee);
		return init;
	}

	private PtoPeriodDTO addAllowedPTO(PtoPeriodDTO init, Employee employee) {

		ZonedDateTime hireDate = employee.getHireDate();
		
		// Set hours allowed based on term of service
		if (hireDate.compareTo(hireDate.plusYears(5)) >= 1) {
			init.setHoursAllowed((long) 205);
		} else {
			init.setHoursAllowed((long) 180);
		}

		init = accruePto(init, employee);
		return init;
	}

	private PtoPeriodDTO accruePto(PtoPeriodDTO init, Employee employee) {

		// Divide PTO Hours by the number of days in the period.
		// This will give you PTO accrued per day - may differ per employee
		// due to leap years

		Double hoursAllowed = init.getHoursAllowed().doubleValue();
		Double daysInPeriod = init.getDaysInPeriod().doubleValue();
		Double dailyAccrual = hoursAllowed / daysInPeriod;

		log.info("Daily Accrual Rate for this PTO Period: " + dailyAccrual);

		// Check what the current date is, take the number of days difference
		// Since the beginning of the period
		
		Date beginDate = determineBeginDate(employee);		
		Date endDate = ZonedDateTimeToDateConverter.INSTANCE.convert(init.getEndDate());
		Date today = ZonedDateTimeToDateConverter.INSTANCE.convert(ZonedDateTime.now());
		
		Long diff = today.getTime() - beginDate.getTime();
		Long daysPassed = (long) ((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
		log.info("Days Passed: " + daysPassed);
		Long hoursAccrued = (long) ((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) * dailyAccrual);
		log.info("Hours: " + hoursAccrued);
		init.setHoursAccrued(hoursAccrued);
		init = deductTimeOff(init);
		return init;
		
	}
	
	private Date determineBeginDate(Employee employee){
		
		//Get All PtoPeriods for a given Employee
		List<PtoPeriod> ptoPeriods = ptoPeriodRepository.findPtoPeriodForEmployee(employee.getId());
		for(PtoPeriod p : ptoPeriods){
			log.info("Pto Period Found: End Date: " + p.getEndDate());
		}
		
		//If more than one period found, select the second latest date as the start date
		//Otherwise return the employee hire date
		
		return ZonedDateTimeToDateConverter.INSTANCE.convert(employee.getHireDate());
	}

	private PtoPeriodDTO deductTimeOff(PtoPeriodDTO init) {
		
		//Get a list of PtoRequests for the given employee
		Long totalRequestedHours = (long) 0;
		
		List<PtoRequest> ptoRequests = ptoRequestRepository.findPtoRequestsForEmployee(init.getEmployeeId());
		for(PtoRequest p : ptoRequests){
			totalRequestedHours += p.getHoursRequested();
		}
		
		init.setHoursRemaining(init.getHoursAllowed() - totalRequestedHours);
		
		//Also change the accrued hours to reflect this:
		Long updateAccrued = init.getHoursAccrued() - totalRequestedHours;
		init.setHoursAccrued(updateAccrued);
		
		return init;
	}

	private Employee findEmployeeByEmail(String userEmail) {

		Employee employee = employeeRepository.findEmployeeByEmail(userEmail);
		log.info("Employee Found: " + employee.getFirstName() + " " + employee.getLastName());
		return employee;
	}
    /**
     *  Get all the ptoPeriods.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PtoPeriodDTO> findAll() {
        log.debug("Request to get all PtoPeriods");
        List<PtoPeriodDTO> result = ptoPeriodRepository.findAll().stream()
            .map(ptoPeriodMapper::ptoPeriodToPtoPeriodDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        if(result.isEmpty()){
        	result.add(initialPtoPeriod());
        }
        
        return result;
    }

    /**
     *  Get one ptoPeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PtoPeriodDTO findOne(Long id) {
        log.debug("Request to get PtoPeriod : {}", id);
        PtoPeriod ptoPeriod = ptoPeriodRepository.findOne(id);
        PtoPeriodDTO ptoPeriodDTO = ptoPeriodMapper.ptoPeriodToPtoPeriodDTO(ptoPeriod);
        return ptoPeriodDTO;
    }

    /**
     *  Delete the  ptoPeriod by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PtoPeriod : {}", id);
        ptoPeriodRepository.delete(id);
    }
}
