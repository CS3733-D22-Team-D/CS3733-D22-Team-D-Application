package edu.wpi.DapperDaemons.APIConverters;

import edu.wpi.DapperDaemons.backend.DAO;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.cs3733.D22.teamZ.api.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for conversion between our Employee Objects and the Employee Objects Present in team Z's API
 */
public class TeamZEmployeeConverter {

    List<Employee> teamDEmployees;
    public TeamZEmployeeConverter() {
        this.teamDEmployees = new ArrayList<>(DAOPouch.getEmployeeDAO().getAll().values());
    }

    /**
     * Allows for our employee database to be converted to team Z's employee database
     */
    public void importTeamDEmployees() {
        // TODO: Implement this so that we can populate the API database with our own employee database
    }
}
