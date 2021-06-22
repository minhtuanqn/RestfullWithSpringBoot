package com.controller;

import com.entity.StaffEntity;
import com.model.DepartmentModel;
import com.model.StaffResourceModel;
import com.resolver.anotation.Pagination;
import com.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import static com.utils.ValidatorUtils.checkExistFieldOfClass;

@RestController
@Validated
@RequestMapping(path = "/departments")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Create new department
     *
     * @param requestModel
     * @return response entity contains created model
     */
    @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody DepartmentModel requestModel) throws SQLIntegrityConstraintViolationException {
        DepartmentModel savedModel = departmentService.createDepartment(requestModel);
        return new ResponseEntity<>(savedModel, HttpStatus.OK);
    }

    /**
     * Delete a department
     *
     * @param id
     * @return response entity contains deleted model
     */
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DepartmentModel> deleteDepartment(@PathVariable @Min(0) Integer id) {
        DepartmentModel deletedModel = departmentService.deleteDepartment(id);
        return new ResponseEntity<>(deletedModel, HttpStatus.OK);
    }

    /**
     * Find department by Id
     *
     * @param id
     * @return response entity contains searched model
     */
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DepartmentModel> findDepartmentById(@PathVariable @Min(0) Integer id) {
        DepartmentModel model = departmentService.findDepartmentById(id);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Update a department
     *
     * @param id
     * @param requestModel
     * @return response entity contains updated model
     */
    @PutMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DepartmentModel> updateDepartment(@PathVariable @Min(0) Integer id,
                                                            @Valid @RequestBody DepartmentModel requestModel) throws SQLIntegrityConstraintViolationException {
        DepartmentModel updatedModel = departmentService.updateDepartment(id, requestModel);
        return new ResponseEntity<>(updatedModel, HttpStatus.OK);
    }

    /**
     * find staff model by department id
     *
     * @param id
     * @return response entity contains staff resource
     */
    @GetMapping(path = "/{id}/staffs", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getStaffsByDepartmentId(
            @PathVariable @Min(0) Integer id,
            @Pagination Pagination pagination
    )  {
        Map<String, String> existError = checkExistFieldOfClass(StaffEntity.class, pagination.sortBy(), "sortBy");
        if (existError != null && existError.size() > 0) {
            return new ResponseEntity<>(existError, HttpStatus.BAD_REQUEST);
        }
        StaffResourceModel resourceModel = departmentService.findAllStaffByDepartmentId(id, pagination);
        return new ResponseEntity<>(resourceModel, HttpStatus.OK);
    }
}