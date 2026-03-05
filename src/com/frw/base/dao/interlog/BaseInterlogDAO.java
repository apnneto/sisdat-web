/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.dao.interlog;

/**
 *
 * @author Maximiliano
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class BaseInterlogDAO {

    @Resource(name="jdbc/winthor-ds")
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void closeResources(Connection conn, Statement stmt, ResultSet rset) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                //do nothing
            }
        }
    }
}

