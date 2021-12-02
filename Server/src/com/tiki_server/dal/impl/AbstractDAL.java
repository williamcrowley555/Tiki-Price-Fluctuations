/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiki_server.dal.impl;

import com.tiki_server.mapper.RowMapper;
import com.tiki_server.util.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.tiki_server.dal.GenericDAL;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author HP
 */
public class AbstractDAL<T> implements GenericDAL<T>{
    
    private static Connection connection = null;
    private static PreparedStatement statement = null;
    private static CallableStatement callableStatement = null;
    private static ResultSet resultSet = null;
    
    private void setParameters(PreparedStatement statement, Object... parameters) {
        try {
            int index;
            for(int i = 0; i < parameters.length; ++i) {
                index = i + 1;
                Object parameter = parameters[i];
                if(parameter instanceof Long) {
                        statement.setLong(index, (Long) parameter);
                } else if(parameter instanceof String) {
                        statement.setString(index, (String) parameter);
                } else if(parameter instanceof Integer) {
                    statement.setInt(index, (Integer) parameter);
                } else if(parameter instanceof Float) {
                        statement.setFloat(index, (Float) parameter);
                }else if(parameter instanceof Double) {
                    statement.setDouble(index, (Double) parameter);
                }else if(parameter instanceof Boolean) {
                        statement.setBoolean(index, (Boolean) parameter);
                } else if(parameter instanceof Date) {
                        Date date = (Date) parameter;
                        statement.setTimestamp(index, new Timestamp(date.getTime()));
                } else if(parameter instanceof LocalDate) {
                        statement.setObject(index, java.sql.Date.valueOf((LocalDate) parameter));
                } else if(parameter instanceof LocalDateTime) {
                        statement.setObject(index, (LocalDateTime) parameter);
                } else if(parameter instanceof byte[]) {
                        statement.setBytes(index, (byte[]) parameter);
                } else if(parameter == null) {
                        statement.setNull(index, Types.NULL);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<>();
        try {
            connection = DBConnectionUtil.getConnection();
            statement = connection.prepareStatement(sql);
            setParameters(statement, parameters);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {                
                results.add(rowMapper.mapRow(resultSet));
            }
            return results;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                connection.close();
                }
            
                if (statement != null) {
                    statement.close();
                }
            
                if (resultSet != null) {
                    resultSet.close();
                }     
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public Long insert(String sql, Object... parameters) {
        try {
            Long id = null;
            connection = DBConnectionUtil.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            setParameters(statement, parameters);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                id = resultSet.getLong(1);
            }
            
            connection.commit();
            return id;
        } catch (SQLException e) {
                e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                connection.close();
                }
            
                if (statement != null) {
                    statement.close();
                }
            
                if (resultSet != null) {
                    resultSet.close();
                } 
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void update(String sql, Object... parameters) {
        try {
            connection = DBConnectionUtil.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            setParameters(statement, parameters);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                connection.close();
                }
            
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean callProc(String sql, Object... parameters) {
        boolean result = false;
        try {
            connection = DBConnectionUtil.getConnection();
            connection.setAutoCommit(false);
            callableStatement = connection.prepareCall(sql);
            setParameters(callableStatement, parameters);
            result = callableStatement.execute();
            connection.commit();
            
            return result;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                connection.close();
                }
            
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }

    @Override
    public <T> List<T> callQueryProc(String sql, RowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<>();
        try {
            connection = DBConnectionUtil.getConnection();
            callableStatement = connection.prepareCall(sql);
            setParameters(callableStatement, parameters);
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {                
                results.add(rowMapper.mapRow(resultSet));
            }
            return results;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractDAL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                connection.close();
                }
            
                if (callableStatement != null) {
                    callableStatement.close();
                }
            
                if (resultSet != null) {
                    resultSet.close();
                }     
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
