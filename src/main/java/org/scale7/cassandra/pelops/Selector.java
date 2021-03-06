package org.scale7.cassandra.pelops;

import java.nio.ByteBuffer;
import java.util.*;

import org.apache.cassandra.thrift.*;
import org.scale7.cassandra.pelops.exceptions.PelopsException;
import org.scale7.cassandra.pelops.pool.IThriftPool;
import org.scale7.cassandra.pelops.pool.IThriftPool.IPooledConnection;
import org.scale7.cassandra.pelops.exceptions.NotFoundException;

import static org.scale7.cassandra.pelops.Bytes.*;

/**
 * Facilitates the selective retrieval of column data from rows in a Cassandra keyspace.<p/>
 *
 * <p><b>Note</b>:The methods that are marked as throwing {@link org.scale7.cassandra.pelops.exceptions.NotFoundException}
 * are the only methods in Pelops that throw exceptions under non-failure conditions.
 *
 * @author dominicwilliams
 *
 */
public class Selector extends Operand {

    // SlicePredicates constants for common internal uses
    private static final SlicePredicate COLUMNS_PREDICATE_ALL = newColumnsPredicateAll(false);
    private static final SlicePredicate COLUMNS_PREDICATE_ALL_REVERSED = newColumnsPredicateAll(true);

    /**
     * Get the count of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the columns
     * @throws PelopsException if an error occurs
     */
    public int getColumnCount(String columnFamily, Bytes rowKey, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), rowKey, COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param predicate						A predicate selecting the columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the columns
     * @throws PelopsException if an error occurs
     */
    public int getColumnCount(String columnFamily, Bytes rowKey, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), rowKey, predicate, cLevel);
    }

    /**
     * Get the count of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the columns
     * @throws PelopsException if an error occurs
     */
    public int getColumnCount(String columnFamily, String rowKey, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), fromUTF8(rowKey), COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param predicate						A predicate selecting the columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the columns
     * @throws PelopsException if an error occurs
     */
    public int getColumnCount(String columnFamily, String rowKey, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), fromUTF8(rowKey), predicate, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, String rowKey, Bytes superColName, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), fromUTF8(rowKey), COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param predicate						A predicate selecting the sub columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, String rowKey, Bytes superColName, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), fromUTF8(rowKey), predicate, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, Bytes rowKey, Bytes superColName, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), rowKey, COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param predicate						A predicate selecting the sub columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, Bytes rowKey, Bytes superColName, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), rowKey, predicate, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, String rowKey, String superColName, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), fromUTF8(rowKey), COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param predicate						A predicate selecting the sub columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, String rowKey, String superColName, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), fromUTF8(rowKey), predicate, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, Bytes rowKey, String superColName, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), rowKey, COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of sub-columns inside a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column
     * @param predicate						A predicate selecting the sub columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the sub-columns
     * @throws PelopsException if an error occurs
     */
    public int getSubColumnCount(String columnFamily, Bytes rowKey, String superColName, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily, superColName), rowKey, predicate, cLevel);
    }

    /**
     * Get the count of super columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the super columns
     * @throws PelopsException if an error occurs
     */
    public int getSuperColumnCount(String columnFamily, Bytes rowKey, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), rowKey, COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of super columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param predicate						A predicate selecting the super columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the super columns
     * @throws PelopsException if an error occurs
     */
    public int getSuperColumnCount(String columnFamily, Bytes rowKey, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), rowKey, predicate, cLevel);
    }

    /**
     * Get the count of super columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the super columns
     * @throws PelopsException if an error occurs
     */
    public int getSuperColumnCount(String columnFamily, String rowKey, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), fromUTF8(rowKey), COLUMNS_PREDICATE_ALL, cLevel);
    }

    /**
     * Get the count of super columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param predicate						A predicate selecting the super columns to be counted
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The count of the super columns
     * @throws PelopsException if an error occurs
     */
    public int getSuperColumnCount(String columnFamily, String rowKey, SlicePredicate predicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnCount(newColumnParent(columnFamily), fromUTF8(rowKey), predicate, cLevel);
    }

    /**
     * Get the count of columns in a row with a matching predicate
     * @param colParent						The parent of the columns to be counted
     * @param rowKey						The key of the row containing the columns
     * @param predicate						The slice predicate selecting the columns to be counted
     * @param cLevel						The Cassandra consistency level with which to perform the operation
     * @return								The number of matching columns
     * @throws PelopsException					The error
     */
    private int getColumnCount(final ColumnParent colParent, final Bytes rowKey, final SlicePredicate predicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Integer> operation = new IOperation<Integer>() {
            @Override
            public Integer execute(IPooledConnection conn) throws Exception {
                return conn.getAPI().get_count(nullSafeGet(rowKey), colParent, predicate, cLevel);
            }
        };
        return (Integer) tryOperation(operation);
    }

    /**
     * Retrieve a column from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param colName                       The name of the column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getColumnFromRow(final String columnFamily, final String rowKey, final String colName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getColumnFromRow(columnFamily, rowKey, fromUTF8(colName), cLevel);
    }

    /**
     * Retrieve a column from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param colName                       The name of the column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getColumnFromRow(final String columnFamily, final String rowKey, final Bytes colName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getColumnFromRow(columnFamily, fromUTF8(rowKey), colName, cLevel);
    }

    /**
     * Retrieve a column from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param colName                       The name of the column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getColumnFromRow(final String columnFamily, final Bytes rowKey, final Bytes colName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        IOperation<Column> operation = new IOperation<Column>() {
            @Override
            public Column execute(IThriftPool.IPooledConnection conn) throws Exception {
                ColumnPath cp = new ColumnPath(columnFamily);
                cp.setColumn(nullSafeGet(colName));
                ColumnOrSuperColumn cosc = conn.getAPI().get(nullSafeGet(rowKey), cp, cLevel);
                return cosc.column;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve a super column from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>SuperColumn</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public SuperColumn getSuperColumnFromRow(final String columnFamily, final String rowKey, final String superColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getSuperColumnFromRow(columnFamily, rowKey, fromUTF8(superColName), cLevel);
    }

    /**
     * Retrieve a super column from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>SuperColumn</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public SuperColumn getSuperColumnFromRow(final String columnFamily, final String rowKey, final Bytes superColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getSuperColumnFromRow(columnFamily, fromUTF8(rowKey), superColName, cLevel);
    }

    /**
     * Retrieve a super column from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>SuperColumn</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public SuperColumn getSuperColumnFromRow(final String columnFamily, final Bytes rowKey, final Bytes superColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        IOperation<SuperColumn> operation = new IOperation<SuperColumn>() {
            @Override
            public SuperColumn execute(IThriftPool.IPooledConnection conn) throws Exception {
                ColumnPath cp = new ColumnPath(columnFamily);
                cp.setSuper_column(nullSafeGet(superColName));
                ColumnOrSuperColumn cosc = conn.getAPI().get(nullSafeGet(rowKey), cp, cLevel);
                return cosc.super_column;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve a sub column from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column containing the sub column
     * @param subColName                    The name of the sub column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getSubColumnFromRow(final String columnFamily, final String rowKey, final Bytes superColName, final String subColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getSubColumnFromRow(columnFamily, fromUTF8(rowKey), superColName, fromUTF8(subColName), cLevel);
    }

    /**
     * Retrieve a sub column from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column containing the sub column
     * @param subColName                    The name of the sub column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getSubColumnFromRow(final String columnFamily, final String rowKey, final String superColName, final String subColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getSubColumnFromRow(columnFamily, fromUTF8(rowKey), fromUTF8(superColName), fromUTF8(subColName), cLevel);
    }

    /**
     * Retrieve a sub column from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column containing the sub column
     * @param subColName                    The name of the sub column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getSubColumnFromRow(final String columnFamily, final String rowKey, final String superColName, final Bytes subColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getSubColumnFromRow(columnFamily, fromUTF8(rowKey), fromUTF8(superColName), subColName, cLevel);
    }

    /**
     * Retrieve a sub column from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column containing the sub column
     * @param subColName                    The name of the sub column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getSubColumnFromRow(final String columnFamily, final String rowKey, final Bytes superColName, final Bytes subColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        return getSubColumnFromRow(columnFamily, fromUTF8(rowKey), superColName, subColName, cLevel);
    }

    /**
     * Retrieve a sub column from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param superColName                  The name of the super column containing the sub column
     * @param subColName                    The name of the sub column to retrieve
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              The requested <code>Column</code>
     * @throws NotFoundException            If no value is present
     * @throws PelopsException if an error occurs
     */
    public Column getSubColumnFromRow(final String columnFamily, final Bytes rowKey, final Bytes superColName, final Bytes subColName, final ConsistencyLevel cLevel) throws NotFoundException, PelopsException {
        IOperation<Column> operation = new IOperation<Column>() {
            @Override
            public Column execute(IThriftPool.IPooledConnection conn) throws Exception {
                ColumnPath cp = new ColumnPath(columnFamily);
                cp.setSuper_column(nullSafeGet(superColName));
                cp.setColumn(nullSafeGet(subColName));
                ColumnOrSuperColumn cosc = conn.getAPI().get(nullSafeGet(rowKey), cp, cLevel);
                return cosc.column;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve all columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param reversed                      Whether the results should be returned in descending column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getColumnsFromRow(String columnFamily, String rowKey, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily), rowKey, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param colPredicate                  The column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getColumnsFromRow(String columnFamily, String rowKey, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily), rowKey, colPredicate, cLevel);
    }

    /**
     * Retrieve all columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param reversed                      Whether the results should be returned in descending column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getColumnsFromRow(String columnFamily, Bytes rowKey, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily), rowKey, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row
     * @param colPredicate                  The column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getColumnsFromRow(String columnFamily, Bytes rowKey, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily), rowKey, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super column
     * @param superColName                  The name of the super column
     * @param reversed                      Whether the results should be returned in descending sub-column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getSubColumnsFromRow(String columnFamily, Bytes rowKey, Bytes superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily, superColName), rowKey, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super column
     * @param superColName                  The name of the super column
     * @param colPredicate                  The sub-column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getSubColumnsFromRow(String columnFamily, Bytes rowKey, Bytes superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily, superColName), rowKey, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super column
     * @param superColName                  The name of the super column
     * @param reversed                      Whether the results should be returned in descending sub-column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getSubColumnsFromRow(String columnFamily, String rowKey, Bytes superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily, superColName), rowKey, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super column
     * @param superColName                  The name of the super column
     * @param colPredicate                  The sub-column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getSubColumnsFromRow(String columnFamily, String rowKey, Bytes superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily, superColName), rowKey, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super column
     * @param superColName                  The name of the super column
     * @param reversed                      Whether the results should be returned in descending sub-column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getSubColumnsFromRow(String columnFamily, String rowKey, String superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily, superColName), rowKey, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super column
     * @param superColName                  The name of the super column
     * @param colPredicate                  The sub-column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getSubColumnsFromRow(String columnFamily, String rowKey, String superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(newColumnParent(columnFamily, superColName), rowKey, colPredicate, cLevel);
    }

    private List<Column> getColumnsFromRow(final ColumnParent colParent, final String rowKey, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRow(colParent, fromUTF8(rowKey), colPredicate, cLevel);
    }

    private List<Column> getColumnsFromRow(final ColumnParent colParent, final Bytes rowKey, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<List<Column>> operation = new IOperation<List<Column>>() {
            @Override
            public List<Column> execute(IPooledConnection conn) throws Exception {
                List<ColumnOrSuperColumn> apiResult = conn.getAPI().get_slice(nullSafeGet(rowKey), colParent, colPredicate, cLevel);
                return toColumnList(apiResult);
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve all super columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super columns
     * @param reversed                      Whether the results should be returned in descending super column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getSuperColumnsFromRow(final String columnFamily, final String rowKey, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRow(columnFamily, fromUTF8(rowKey), columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve super columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super columns
     * @param colPredicate                  The super column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getSuperColumnsFromRow(final String columnFamily, final String rowKey, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRow(columnFamily, fromUTF8(rowKey), colPredicate, cLevel);
    }

    /**
     * Retrieve all super columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super columns
     * @param reversed                      Whether the results should be returned in descending super column name order
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getSuperColumnsFromRow(final String columnFamily, final Bytes rowKey, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRow(columnFamily, rowKey, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve super columns from a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the super columns
     * @param colPredicate                  The super column selector predicate
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A list of matching columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getSuperColumnsFromRow(final String columnFamily, final Bytes rowKey, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<List<SuperColumn>> operation = new IOperation<List<SuperColumn>>() {
            @Override
            public List<SuperColumn> execute(IPooledConnection conn) throws Exception {
                List<ColumnOrSuperColumn> apiResult = conn.getAPI().get_slice(nullSafeGet(rowKey), newColumnParent(columnFamily), colPredicate, cLevel);
                return toSuperColumnList(apiResult);
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve a page of columns composed from a segment of the sequence of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the columns
     * @param startBeyondName               The sequence of columns must begin with the smallest column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending column name order
     * @param count                         The maximum number of columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getPageOfColumnsFromRow(String columnFamily, String rowKey, Bytes startBeyondName, boolean reversed, int count, ConsistencyLevel cLevel) throws PelopsException {
    	return getPageOfColumnsFromRow(columnFamily, fromUTF8(rowKey), startBeyondName, reversed, count, cLevel);
    }

    /**
     * Retrieve a page of columns composed from a segment of the sequence of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the columns
     * @param startBeyondName               The sequence of columns must begin with the smallest column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending column name order
     * @param count                         The maximum number of columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getPageOfColumnsFromRow(String columnFamily, String rowKey, String startBeyondName, boolean reversed, int count, ConsistencyLevel cLevel) throws PelopsException {
    	return getPageOfColumnsFromRow(columnFamily, fromUTF8(rowKey), fromUTF8(startBeyondName), reversed, count, cLevel);
    }

    /**
     * Retrieve a page of columns composed from a segment of the sequence of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the columns
     * @param startBeyondName               The sequence of columns must begin with the smallest column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending column name order
     * @param count                         The maximum number of columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getPageOfColumnsFromRow(String columnFamily, Bytes rowKey, String startBeyondName, boolean reversed, int count, ConsistencyLevel cLevel) throws PelopsException {
    	return getPageOfColumnsFromRow(columnFamily, rowKey, fromUTF8(startBeyondName), reversed, count, cLevel);
    }

    /**
     * Retrieve a page of columns composed from a segment of the sequence of columns in a row.
     * @param columnFamily                  The column family containing the row
     * @param rowKey                        The key of the row containing the columns
     * @param startBeyondName               The sequence of columns must begin with the smallest column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending column name order
     * @param count                         The maximum number of columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of columns
     * @throws PelopsException if an error occurs
     */
    public List<Column> getPageOfColumnsFromRow(final String columnFamily, final Bytes rowKey, final Bytes startBeyondName, final boolean reversed, final int count, final ConsistencyLevel cLevel) throws PelopsException {
        SlicePredicate predicate;
        if (Bytes.nullSafeGet(startBeyondName) == null) {
            predicate = Selector.newColumnsPredicateAll(reversed, count);
            return getColumnsFromRow(columnFamily, rowKey, predicate, cLevel);
        } else {
            int incrementedCount = count + 1;  // cassandra will return the start row but the user is expecting a page of results beyond that point
            predicate = Selector.newColumnsPredicate(startBeyondName, Bytes.EMPTY, reversed, incrementedCount);
            List<Column> columns = getColumnsFromRow(columnFamily, rowKey, predicate, cLevel);
            if (columns.size() > 0) {
            	Column first = columns.get(0);
            	if (first.name.equals(startBeyondName.getBytes()))
            		return columns.subList(1, columns.size());
            	else if (columns.size() == incrementedCount)
            		return columns.subList(0, columns.size()-1);
            }
            return columns;
        }
    }

    /**
     * Returns an iterator that can be used to iterate over super columns.  The returned iterator delegates to
     * {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)}
     * to fetch batches of super columns (based on the batchSize parameter).
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param batchSize                     The maximum number of super columns that can be retrieved per invocation to {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)} and dictates the number of super columns to be held in memory at any one time
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public Iterator<Column> iterateColumnsFromRow(final String columnFamily, final Bytes rowKey, final Bytes startBeyondName, final boolean reversed, final int batchSize, final ConsistencyLevel cLevel) {
        return new ColumnIterator(this, columnFamily, rowKey, startBeyondName, reversed, batchSize, cLevel);
    }

    /**
     * Returns an iterator that can be used to iterate over super columns.  The returned iterator delegates to
     * {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)}
     * to fetch batches of super columns (based on the batchSize parameter).
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param batchSize                     The maximum number of super columns that can be retrieved per invocation to {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)} and dictates the number of super columns to be held in memory at any one time
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public Iterator<Column> iterateColumnsFromRow(final String columnFamily, final String rowKey, final String startBeyondName, final boolean reversed, final int batchSize, final ConsistencyLevel cLevel) {
        return iterateColumnsFromRow(columnFamily, Bytes.fromUTF8(rowKey), Bytes.fromUTF8(startBeyondName), reversed, batchSize, cLevel);
    }

    /**
     * Retrieve a page of super columns composed from a segment of the sequence of super columns in a row.
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param count                         The maximum number of super columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getPageOfSuperColumnsFromRow(final String columnFamily, String rowKey, Bytes startBeyondName, boolean reversed, int count, ConsistencyLevel cLevel) throws PelopsException {
    	return getPageOfSuperColumnsFromRow(columnFamily, fromUTF8(rowKey), startBeyondName, reversed, count, cLevel);
    }

    /**
     * Retrieve a page of super columns composed from a segment of the sequence of super columns in a row.
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param count                         The maximum number of super columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getPageOfSuperColumnsFromRow(final String columnFamily, String rowKey, String startBeyondName, boolean reversed, int count, ConsistencyLevel cLevel) throws PelopsException {
    	return getPageOfSuperColumnsFromRow(columnFamily, fromUTF8(rowKey), fromUTF8(startBeyondName), reversed, count, cLevel);
    }

    /**
     * Retrieve a page of super columns composed from a segment of the sequence of super columns in a row.
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param count                         The maximum number of super columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getPageOfSuperColumnsFromRow(final String columnFamily, Bytes rowKey, String startBeyondName, boolean reversed, int count, ConsistencyLevel cLevel) throws PelopsException {
    	return getPageOfSuperColumnsFromRow(columnFamily, rowKey, fromUTF8(startBeyondName), reversed, count, cLevel);
    }

    /**
     * Retrieve a page of super columns composed from a segment of the sequence of super columns in a row.
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param count                         The maximum number of super columns that can be retrieved by the scan
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public List<SuperColumn> getPageOfSuperColumnsFromRow(final String columnFamily, final Bytes rowKey, final Bytes startBeyondName, final boolean reversed, final int count, final ConsistencyLevel cLevel) throws PelopsException {
        if (Bytes.nullSafeGet(startBeyondName) == null) {
            SlicePredicate predicate = Selector.newColumnsPredicateAll(reversed, count);
            return getSuperColumnsFromRow(columnFamily, rowKey, predicate, cLevel);
        } else {
            int incrementedCount = count + 1;  // cassandra will return the start row but the user is expecting a page of results beyond that point
            SlicePredicate predicate = Selector.newColumnsPredicate(startBeyondName, Bytes.EMPTY, reversed, incrementedCount);
            List<SuperColumn> columns = getSuperColumnsFromRow(columnFamily, rowKey, predicate, cLevel);
            if (columns.size() > 0) {
            	SuperColumn first = columns.get(0);
            	if (first.name.equals(startBeyondName.getBytes()))
            		return columns.subList(1, columns.size());
            	else if (columns.size() == incrementedCount)
            		return columns.subList(0, columns.size()-1);
            }
            return columns;
        }
    }

    /**
     * Returns an iterator that can be used to iterate over super columns.  The returned iterator delegates to
     * {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)}
     * to fetch batches of super columns (based on the batchSize parameter).
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param batchSize                     The maximum number of super columns that can be retrieved per invocation to {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)} and dictates the number of super columns to be held in memory at any one time
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public Iterator<SuperColumn> iterateSuperColumnsFromRow(final String columnFamily, final Bytes rowKey, final Bytes startBeyondName, final boolean reversed, final int batchSize, final ConsistencyLevel cLevel) {
        return new SuperColumnIterator(this, columnFamily, rowKey, startBeyondName, reversed, batchSize, cLevel);
    }

    /**
     * Returns an iterator that can be used to iterate over super columns.  The returned iterator delegates to
     * {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)}
     * to fetch batches of super columns (based on the batchSize parameter).
     * @param columnFamily                  The name of the column family containing the super columns
     * @param rowKey                        The key of the row
     * @param startBeyondName               The sequence of super columns must begin with the smallest super column name greater than this value. Pass <code>null</code> to start at the beginning of the sequence.
     * @param reversed                      Whether the scan should proceed in descending super column name order
     * @param batchSize                     The maximum number of super columns that can be retrieved per invocation to {@link #getPageOfSuperColumnsFromRow(String, String, Bytes, boolean, int, org.apache.cassandra.thrift.ConsistencyLevel)} and dictates the number of super columns to be held in memory at any one time
     * @param cLevel                        The Cassandra consistency level with which to perform the operation
     * @return                              A page of super columns
     * @throws PelopsException if an error occurs
     */
    public Iterator<SuperColumn> iterateSuperColumnsFromRow(final String columnFamily, String rowKey, String startBeyondName, final boolean reversed, final int batchSize, final ConsistencyLevel cLevel) {
        return iterateSuperColumnsFromRow(columnFamily, Bytes.fromUTF8(rowKey), Bytes.fromUTF8(startBeyondName), reversed, batchSize, cLevel);
    }

    /**
     * Retrieve all columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the columns
     * @param reversed                       Whether the results should be returned in descending column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getColumnsFromRows(String columnFamily, List<Bytes> rowKeys, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily), rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the columns
     * @param colPredicate                   The column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getColumnsFromRows(String columnFamily, List<Bytes> rowKeys, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily), rowKeys, colPredicate, cLevel);
    }

    /**
     * Retrieve all columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the columns
     * @param reversed                       Whether the results should be returned in descending column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getColumnsFromRowsUtf8Keys(String columnFamily, List<String> rowKeys, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily), rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the columns
     * @param colPredicate                   The column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getColumnsFromRowsUtf8Keys(String columnFamily, List<String> rowKeys, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily), rowKeys, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param reversed                       Whether the results should be returned in descending sub-column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, List<Bytes> rowKeys, String superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily, superColName), rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param colPredicate                   The sub-column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, List<Bytes> rowKeys, String superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRows(newColumnParent(columnFamily, superColName), rowKeys, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param reversed                       Whether the results should be returned in descending sub-column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, List<String> rowKeys, String superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
    	return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param colPredicate                   The sub-column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, List<String> rowKeys, String superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

    	return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), rowKeys, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param reversed                       Whether the results should be returned in descending sub-column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, List<Bytes> rowKeys, Bytes superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily, superColName), rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param colPredicate                   The sub-column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, List<Bytes> rowKeys, Bytes superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRows(newColumnParent(columnFamily, superColName), rowKeys, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param reversed                       Whether the results should be returned in descending sub-column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, List<String> rowKeys, Bytes superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a set of rows.
     * @param columnFamily                   The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param superColName                   The name of the super column
     * @param colPredicate                   The sub-column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, List<String> rowKeys, Bytes superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), rowKeys, colPredicate, cLevel);
    }

    /**
     * Retrieve all super columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param reversed                       Whether the results should be returned in descending super column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<SuperColumn>> getSuperColumnsFromRows(final String columnFamily, final List<Bytes> rowKeys, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRows(columnFamily, rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve super columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param colPredicate                   The super column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<SuperColumn>> getSuperColumnsFromRows(final String columnFamily, final List<Bytes> rowKeys, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<Bytes, List<SuperColumn>>> operation = new IOperation<Map<Bytes, List<SuperColumn>>>() {
            @Override
            public Map<Bytes, List<SuperColumn>> execute(IPooledConnection conn) throws Exception {
                Map<ByteBuffer, List<ColumnOrSuperColumn>> apiResult = conn.getAPI().multiget_slice(Bytes.transformBytesToList(rowKeys), newColumnParent(columnFamily), colPredicate, cLevel);
                Map<Bytes, List<SuperColumn>> result = new HashMap<Bytes, List<SuperColumn>>();
                for (ByteBuffer rowKey : apiResult.keySet()) {
                    List<ColumnOrSuperColumn> coscList = apiResult.get(rowKey);
                    List<SuperColumn> columns = new ArrayList<SuperColumn>(coscList.size());
                    for (ColumnOrSuperColumn cosc : coscList) {
                        assert cosc.super_column != null : "The super column should not be null";
                        columns.add(cosc.super_column);
                    }
                    result.put(Bytes.fromByteBuffer(rowKey), columns);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve all super columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param reversed                       Whether the results should be returned in descending super column name order
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<SuperColumn>> getSuperColumnsFromRowsUtf8Keys(final String columnFamily, final List<String> rowKeys, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRowsUtf8Keys(columnFamily, rowKeys, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve super columns from a set of rows.
     * @param columnFamily                  The column family containing the rows
     * @param rowKeys                        The keys of the rows containing the super columns
     * @param colPredicate                   The super column selector predicate
     * @param cLevel                         The Cassandra consistency level with which to perform the operation
     * @return                               A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<SuperColumn>> getSuperColumnsFromRowsUtf8Keys(final String columnFamily, final List<String> rowKeys, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<String, List<SuperColumn>>> operation = new IOperation<Map<String, List<SuperColumn>>>() {
            @Override
            public Map<String, List<SuperColumn>> execute(IPooledConnection conn) throws Exception {
                Map<ByteBuffer, List<ColumnOrSuperColumn>> apiResult = conn.getAPI().multiget_slice(Bytes.transformUTF8ToList(rowKeys), newColumnParent(columnFamily), colPredicate, cLevel);
                Map<String, List<SuperColumn>> result = new HashMap<String, List<SuperColumn>>();
                for (ByteBuffer rowKey : apiResult.keySet()) {
                    List<ColumnOrSuperColumn> coscList = apiResult.get(rowKey);
                    List<SuperColumn> columns = toSuperColumnList(coscList);
                    result.put(toUTF8(rowKey), columns);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    private Map<Bytes, List<Column>> getColumnsFromRows(final ColumnParent colParent, final List<Bytes> rowKeys, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<Bytes, List<Column>>> operation = new IOperation<Map<Bytes, List<Column>>>() {
            @Override
            public Map<Bytes, List<Column>> execute(IThriftPool.IPooledConnection conn) throws Exception {
                Map<ByteBuffer, List<ColumnOrSuperColumn>> apiResult = conn.getAPI().multiget_slice(Bytes.transformBytesToList(rowKeys), colParent, colPredicate, cLevel);
                Map<Bytes, List<Column>> result = new HashMap<Bytes, List<Column>>();
                for (ByteBuffer rowKey : apiResult.keySet()) {
                    List<ColumnOrSuperColumn> coscList = apiResult.get(rowKey);
                    List<Column> columns = toColumnList(coscList);
                    result.put(Bytes.fromByteBuffer(rowKey), columns);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    private Map<String, List<Column>> getColumnsFromRowsUtf8Keys(final ColumnParent colParent, final List<String> rowKeys, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<String, List<Column>>> operation = new IOperation<Map<String, List<Column>>>() {
            @Override
            public Map<String, List<Column>> execute(IPooledConnection conn) throws Exception {
                Map<ByteBuffer, List<ColumnOrSuperColumn>> apiResult = conn.getAPI().multiget_slice(Bytes.transformUTF8ToList(rowKeys), colParent, colPredicate, cLevel);
                Map<String, List<Column>> result = new HashMap<String, List<Column>>();
                for (ByteBuffer rowKey : apiResult.keySet()) {
                    List<ColumnOrSuperColumn> coscList = apiResult.get(rowKey);
                    List<Column> columns = toColumnList(coscList);
                    result.put(toUTF8(rowKey), columns);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve all columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param reversed                        Whether the results should be returned in descending column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getColumnsFromRows(String columnFamily, KeyRange keyRange, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily), keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param colPredicate                    The column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getColumnsFromRows(String columnFamily, KeyRange keyRange, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRows(newColumnParent(columnFamily), keyRange, colPredicate, cLevel);
    }

    /**
     * Retrieve all columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param reversed                        Whether the results should be returned in descending column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getColumnsFromRowsUtf8Keys(String columnFamily, KeyRange keyRange, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily), keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param colPredicate                    The column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getColumnsFromRowsUtf8Keys(String columnFamily, KeyRange keyRange, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily), keyRange, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param reversed                        Whether the results should be returned in descending sub-column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, KeyRange keyRange, Bytes superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily, superColName), keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param colPredicate                    The sub-column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, KeyRange keyRange, Bytes superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRows(newColumnParent(columnFamily, superColName), keyRange, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param reversed                        Whether the results should be returned in descending sub-column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, KeyRange keyRange, Bytes superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param colPredicate                    The sub-column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, KeyRange keyRange, Bytes superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), keyRange, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param reversed                        Whether the results should be returned in descending sub-column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, KeyRange keyRange, String superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRows(newColumnParent(columnFamily, superColName), keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param colPredicate                    The sub-column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getSubColumnsFromRows(String columnFamily, KeyRange keyRange, String superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRows(newColumnParent(columnFamily, superColName), keyRange, colPredicate, cLevel);
    }

    /**
     * Retrieve all sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param reversed                        Whether the results should be returned in descending sub-column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, KeyRange keyRange, String superColName, boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve sub-columns from a super column in a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of sub-columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param superColName                    The name of the super column
     * @param colPredicate                    The sub-column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of sub-columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<Column>> getSubColumnsFromRowsUtf8Keys(String columnFamily, KeyRange keyRange, String superColName, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {

        return getColumnsFromRowsUtf8Keys(newColumnParent(columnFamily, superColName), keyRange, colPredicate, cLevel);
    }

    /**
     * Retrieve all super columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of super columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param reversed                        Whether the results should be returned in descending super column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<SuperColumn>> getSuperColumnsFromRows(final String columnFamily, final KeyRange keyRange, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRows(columnFamily, keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve super columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of super columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param colPredicate                    The super column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<SuperColumn>> getSuperColumnsFromRows(final String columnFamily, final KeyRange keyRange, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<Bytes, List<SuperColumn>>> operation = new IOperation<Map<Bytes, List<SuperColumn>>>() {
            @Override
            public Map<Bytes, List<SuperColumn>> execute(IPooledConnection conn) throws Exception {
                List<KeySlice> apiResult = conn.getAPI().get_range_slices(newColumnParent(columnFamily), colPredicate, keyRange, cLevel);
                Map<Bytes, List<SuperColumn>> result = new LinkedHashMap<Bytes, List<SuperColumn>>();
                for (KeySlice ks : apiResult) {
                    List<ColumnOrSuperColumn> coscList = ks.columns;
                    List<SuperColumn> colList = toSuperColumnList(coscList);
                    result.put(fromByteBuffer(ks.key), colList);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve all super columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of super columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param reversed                        Whether the results should be returned in descending super column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<SuperColumn>> getSuperColumnsFromRowsUtf8Keys(final String columnFamily, final KeyRange keyRange, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getSuperColumnsFromRowsUtf8Keys(columnFamily, keyRange, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve super columns from a range of rows.
     * The method returns a map from the keys of rows in the specified range to lists of super columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param columnFamily                    The column family containing the rows
     * @param keyRange                        A key range selecting the rows
     * @param colPredicate                    The super column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of super columns
     * @throws PelopsException if an error occurs
     */
    public Map<String, List<SuperColumn>> getSuperColumnsFromRowsUtf8Keys(final String columnFamily, final KeyRange keyRange, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<String, List<SuperColumn>>> operation = new IOperation<Map<String, List<SuperColumn>>>() {
            @Override
            public Map<String, List<SuperColumn>> execute(IThriftPool.IPooledConnection conn) throws Exception {
                List<KeySlice> apiResult = conn.getAPI().get_range_slices(newColumnParent(columnFamily), colPredicate, keyRange, cLevel);
                Map<String, List<SuperColumn>> result = new LinkedHashMap<String, List<SuperColumn>>();
                for (KeySlice ks : apiResult) {
                    List<ColumnOrSuperColumn> coscList = ks.columns;
                    List<SuperColumn> colList = toSuperColumnList(coscList);
                    result.put(toUTF8(ks.key), colList);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    private Map<Bytes, List<Column>> getColumnsFromRows(final ColumnParent colParent, final KeyRange keyRange, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<Bytes, List<Column>>> operation = new IOperation<Map<Bytes, List<Column>>>() {
            @Override
            public Map<Bytes, List<Column>> execute(IPooledConnection conn) throws Exception {
                List<KeySlice> apiResult = conn.getAPI().get_range_slices(colParent, colPredicate, keyRange, cLevel);
                Map<Bytes, List<Column>> result = new LinkedHashMap<Bytes, List<Column>>();
                for (KeySlice ks : apiResult) {
                    List<ColumnOrSuperColumn> coscList = ks.columns;
                    List<Column> colList = toColumnList(coscList);
                    result.put(fromByteBuffer(ks.key), colList);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    private Map<String, List<Column>> getColumnsFromRowsUtf8Keys(final ColumnParent colParent, final KeyRange keyRange, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<String, List<Column>>> operation = new IOperation<Map<String, List<Column>>>() {
            @Override
            public Map<String, List<Column>> execute(IPooledConnection conn) throws Exception {
                List<KeySlice> apiResult = conn.getAPI().get_range_slices(colParent, colPredicate, keyRange, cLevel);
                Map<String, List<Column>> result = new LinkedHashMap<String, List<Column>>();
                for (KeySlice ks : apiResult) {
                    List<ColumnOrSuperColumn> coscList = ks.columns;
                    List<Column> colList = toColumnList(coscList);
                    result.put(toUTF8(ks.key), colList);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Retrieve all columns from a range of indexed rows using its secondary index.
     * The method returns a map from the keys of indexed rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param colParent                    The column parent containing the rows
     * @param indexClause                        A index clause
     * @param reversed                        Whether the results should be returned in descending column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getIndexedColumns(String colParent, IndexClause indexClause,boolean reversed, ConsistencyLevel cLevel) throws PelopsException {
    	return getIndexedColumns(newColumnParent(colParent), indexClause, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a range of indexed rows using its secondary index.
     * The method returns a map from the keys of indexed rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param colParent                    The column parent containing the rows
     * @param indexClause                        A index clause
     * @param colPredicate                    The column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getIndexedColumns(String colParent, IndexClause indexClause, SlicePredicate colPredicate, ConsistencyLevel cLevel) throws PelopsException {
    	return getIndexedColumns(newColumnParent(colParent), indexClause, colPredicate, cLevel);
    }

    /**
     * Retrieve all columns from a range of indexed rows using its secondary index.
     * The method returns a map from the keys of indexed rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param colParent                    The column parent
     * @param indexClause                        A index key range selecting the rows
     * @param reversed                        Whether the results should be returned in descending column name order
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getIndexedColumns(final ColumnParent colParent, final IndexClause indexClause, final boolean reversed, final ConsistencyLevel cLevel) throws PelopsException {
        return getIndexedColumns(colParent, indexClause, columnsPredicateAll(reversed), cLevel);
    }

    /**
     * Retrieve columns from a range of indexed rows using its secondary index.
     * The method returns a map from the keys of indexed rows in the specified range to lists of columns from the rows. The map
     * returned is a <code>LinkedHashMap</code> and its key iterator proceeds in the order that the key data was returned by
     * Cassandra. If the cluster uses the RandomPartitioner, this order appears random.
     * @param colParent                    The column parent
     * @param indexClause                        A index key range selecting the rows
     * @param colPredicate                    The column selector predicate
     * @param cLevel                          The Cassandra consistency level with which to perform the operation
     * @return                                A map from row keys to the matching lists of columns
     * @throws PelopsException if an error occurs
     */
    public Map<Bytes, List<Column>> getIndexedColumns(final ColumnParent colParent, final IndexClause indexClause, final SlicePredicate colPredicate, final ConsistencyLevel cLevel) throws PelopsException {
        IOperation<Map<Bytes, List<Column>>> operation = new IOperation<Map<Bytes, List<Column>>>() {
            @Override
            public Map<Bytes, List<Column>> execute(IThriftPool.IPooledConnection conn) throws Exception {
                List<KeySlice> apiResult = conn.getAPI().get_indexed_slices(colParent, indexClause, colPredicate, cLevel);
                Map<Bytes, List<Column>> result = new LinkedHashMap<Bytes, List<Column>>();
                for (KeySlice ks : apiResult) {
                    List<ColumnOrSuperColumn> coscList = ks.columns;
                    List<Column> colList = toColumnList(coscList);
                    result.put(fromByteBuffer(ks.key), colList);
                }
                return result;
            }
        };
        return tryOperation(operation);
    }

    /**
     * Create a new <code>IndexExpression</code> instance.
     * @param colName						The name of the column
     * @param op							The index expression operator (for now only EQ works)
     * @param value		 					Lookup value
     * @return								The new <code>IndexExpression</code>
     */
    public static IndexExpression newIndexExpression(Bytes colName, IndexOperator op, Bytes value) {
    	return new IndexExpression(colName.getBytes(), op, value.getBytes());
    }

    /**
     * Create a new <code>IndexExpression</code> instance.
     * @param colName						The name of the column
     * @param op							The index expression operator (for now only EQ works)
     * @param value		 					Lookup value
     * @return								The new <code>IndexExpression</code>
     */
    public static IndexExpression newIndexExpression(String colName, IndexOperator op, Bytes value) {
    	return newIndexExpression(fromUTF8(colName), op, value);
    }

    /**
     * Create a new <code>IndexClause</code> instance.
     * @param startName						The inclusive column start name of the index range to select in the slice
     * @param count							The maximum number of rows to return
     * @param expressions 					Index value lookup expressions
     * @return								The new <code>IndexClause</code>
     */
    public static IndexClause newIndexClause(String startName, int count, IndexExpression... expressions) {
    	return newIndexClause(fromUTF8(startName), count, expressions);
    }

    /**
     * Create a new <code>IndexClause</code> instance.
     * @param startName						The inclusive column start name of the index range to select in the slice
     * @param count							The maximum number of rows to return
     * @param expressions 					Index value lookup expressions
     * @return								The new <code>IndexClause</code>
     */
    public static IndexClause newIndexClause(Bytes startName, int count, IndexExpression... expressions) {
    	return new IndexClause(Arrays.asList(expressions), startName.getBytes(), count);
    }

    /**
     * Create the internal <code>SlicePredicate</code> instance that selects "all" columns with no imposed limit.
     * Note: these instances should be handled carefully, as they are mutable.
     * @param reversed                        Whether the results should be returned in reverse order
     * @return                                The new <code>SlicePredicate</code>
     */
    private static SlicePredicate columnsPredicateAll(boolean reversed) {
        return reversed ? COLUMNS_PREDICATE_ALL_REVERSED : COLUMNS_PREDICATE_ALL;
    }

    /**
     * Create a new <code>SlicePredicate</code> instance that selects "all" columns with no imposed limit
     * @param reversed                        Whether the results should be returned in reverse order
     * @return                                The new <code>SlicePredicate</code>
     */
    public static SlicePredicate newColumnsPredicateAll(boolean reversed) {
        return newColumnsPredicateAll(reversed, Integer.MAX_VALUE);
    }

    /**
     * Create a new <code>SlicePredicate</code> instance that selects "all" columns
     * @param reversed                        Whether the results should be returned in reverse order
     * @param maxColCount                     The maximum number of columns to return
     * @return                                The new <code>SlicePredicate</code>
     */
    public static SlicePredicate newColumnsPredicateAll(boolean reversed, int maxColCount) {
        SlicePredicate predicate = new SlicePredicate();
        predicate.setSlice_range(new SliceRange(Bytes.EMPTY.getBytes(), Bytes.EMPTY.getBytes(), reversed, maxColCount));
        return predicate;
    }

    /**
     * Create a new <code>SlicePredicate</code> instance.
     * @param startName                       The inclusive column start name of the range to select in the slice
     * @param finishName                      The inclusive column end name of the range to select in the slice
     * @param reversed                        Whether the results should be returned in reverse order
     * @param maxColCount                     The maximum number of columns to return
     * @return                                The new <code>SlicePredicate</code>
     */
    public static SlicePredicate newColumnsPredicate(Bytes startName, Bytes finishName, boolean reversed, int maxColCount) {
        SlicePredicate predicate = new SlicePredicate();
        predicate.setSlice_range(new SliceRange(nullSafeGet(startName), nullSafeGet(finishName), reversed, maxColCount));
        return predicate;
    }

    /**
     * Create a new <code>SlicePredicate</code> instance.
     * @param startName                       The inclusive column start name of the range to select in the slice
     * @param finishName                      The inclusive column end name of the range to select in the slice
     * @param reversed                        Whether the results should be returned in reverse order
     * @param maxColCount                     The maximum number of columns to return
     * @return                                The new <code>SlicePredicate</code>
     */
    public static SlicePredicate newColumnsPredicate(String startName, String finishName, boolean reversed, int maxColCount) {
        return newColumnsPredicate(fromUTF8(startName), fromUTF8(finishName), reversed, maxColCount);
    }

    /**
     * Create a new <code>SlicePredicate</code> instance.
     * @param colNames                        The specific columns names to select in the slice
     * @return                                The new <code>SlicePredicate</code>
     */
    public static SlicePredicate newColumnsPredicate(String... colNames) {
        List<ByteBuffer> asList = new ArrayList<ByteBuffer>(32);
        for (String colName : colNames)
            asList.add(fromUTF8(colName).getBytes());
        SlicePredicate predicate = new SlicePredicate();
        predicate.setColumn_names(asList);
        return predicate;
    }

    /**
     * Create a new <code>SlicePredicate</code> instance.
     * @param colNames                        The specific columns names to select in the slice
     * @return                                The new <code>SlicePredicate</code>
     */
    public static SlicePredicate newColumnsPredicate(Bytes... colNames) {
        List<ByteBuffer> asList = new ArrayList<ByteBuffer>(32);
        for (Bytes colName : colNames)
            asList.add(nullSafeGet(colName));
        SlicePredicate predicate = new SlicePredicate();
        predicate.setColumn_names(asList);
        return predicate;
    }

    /**
     * Create a new <code>KeyRange</code> instance.
     * @param startKey                        The inclusive start key of the range
     * @param finishKey                       The inclusive finish key of the range
     * @param maxKeyCount                     The maximum number of keys to be scanned
     * @return                                The new <code>KeyRange</code> instance
     */
    public static KeyRange newKeyRange(String startKey, String finishKey, int maxKeyCount) {
        return newKeyRange(fromUTF8(startKey), fromUTF8(finishKey), maxKeyCount);
    }

    /**
     * Create a new <code>KeyRange</code> instance.
     * @param startKey                        The inclusive start key of the range
     * @param finishKey                       The inclusive finish key of the range
     * @param maxKeyCount                     The maximum number of keys to be scanned
     * @return                                The new <code>KeyRange</code> instance
     */
    public static KeyRange newKeyRange(Bytes startKey, Bytes finishKey, int maxKeyCount) {
        KeyRange keyRange = new KeyRange(maxKeyCount);
        keyRange.setStart_key(nullSafeGet(startKey));
        keyRange.setEnd_key(nullSafeGet(finishKey));
        return keyRange;
    }

    /**
     * Create a new <code>KeyRange</code> instance.
     * @param startFollowingKey                The exclusive start key of the ring range
     * @param finishKey                        The inclusive finish key of the range (can be less than <code>startFollowing</code>)
     * @param maxKeyCount                      The maximum number of keys to be scanned
     * @return                                 The new <code>KeyRange</code> instance
     */
    public static KeyRange newKeyRingRange(String startFollowingKey, String finishKey, int maxKeyCount) {
        KeyRange keyRange = new KeyRange(maxKeyCount);
        keyRange.setStart_token(startFollowingKey);
        keyRange.setEnd_token(finishKey);
        return keyRange;
    }

    /**
     * Determines if a super column with a particular name exist in the list of super columns.
     * @param superColumns                    The list of super columns
     * @param superColName                    The name of the super column
     * @return                                Whether the super column is present
     */
    public static boolean superColumnExists(List<SuperColumn> superColumns, String superColName) {
        return superColumnExists(superColumns, fromUTF8(superColName));
    }

    /**
     * Determines if a super column with a particular name exist in the list of super columns.
     * @param superColumns                    The list of super columns
     * @param superColName                    The name of the super column
     * @return                                Whether the super column is present
     */
    public static boolean superColumnExists(List<SuperColumn> superColumns, Bytes superColName) {
        for (SuperColumn superColumn : superColumns)
            if (superColumn.name.equals(nullSafeGet(superColName)))
                return true;
        return false;
    }

    /**
     * Get a super column by name from a list of super columns
     * @param superColumns                    The list of super columns
     * @param superColName                    The name of the super column
     * @return                                The super column
     * @throws ArrayIndexOutOfBoundsException    Thrown if the list does not contain a super column with the specified name
     */
    public static SuperColumn getSuperColumn(List<SuperColumn> superColumns, Bytes superColName) throws ArrayIndexOutOfBoundsException {
        for (SuperColumn superColumn : superColumns)
            if (superColumn.name.equals(nullSafeGet(superColName)))
                return superColumn;
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Get a super column by name from a list of super columns
     * @param superColumns                    The list of super columns
     * @param superColName                    The name of the super column
     * @return                                The super column
     * @throws ArrayIndexOutOfBoundsException    Thrown if the list does not contain a super column with the specified name
     */
    public static SuperColumn getSuperColumn(List<SuperColumn> superColumns, String superColName) throws ArrayIndexOutOfBoundsException {
        return getSuperColumn(superColumns, fromUTF8(superColName));
    }

    /**
     * Get the name of a column as a UTF8 string
     * @param column							The column
     * @return									The <code>byte[]</code> name as a UTF8 string
     */
    public static String getColumnStringName(Column column) {
    	return toUTF8(column.getName());
    }

    /**
     * Get the value of a column as a UTF8 string
     * @param column							The column containing the value
     * @return									The <code>byte[]</code> value as a UTF8 string
     */
    public static String getColumnStringValue(Column column) {
    	return toUTF8(column.getValue());
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                         The list of columns
     * @param colName                         The name of the column from which to retrieve the value
     * @param defaultValue                    A default value to return if a column with the specified name is not present in the list
     * @return                                The column value
     */
    public static String getColumnValue(List<Column> columns, String colName, String defaultValue) {
        return getColumnValue(columns, fromUTF8(colName), defaultValue);
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                         The list of columns
     * @param colName                         The name of the column from which to retrieve the value
     * @param defaultValue                    A default value to return if a column with the specified name is not present in the list
     * @return                                The column value
     */
    public static Bytes getColumnValue(List<Column> columns, String colName, Bytes defaultValue) {
        return getColumnValue(columns, fromUTF8(colName), defaultValue);
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the value
     * @param defaultValue                   A default value to return if a column with the specified name is not present in the list
     * @return                                The column value
     */
    public static Bytes getColumnValue(List<Column> columns, Bytes colName, Bytes defaultValue) {
        for (Column column : columns)
            if (column.name.equals(nullSafeGet(colName)))
                return fromByteBuffer(column.value);
        return defaultValue;
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the value
     * @param defaultValue                   A default value to return if a column with the specified name is not present in the list
     * @return                               The column value
     */
    public static String getColumnValue(List<Column> columns, Bytes colName, String defaultValue) {
        for (Column column : columns)
            if (column.name.equals(nullSafeGet(colName)))
                return toUTF8(column.value);
        return defaultValue;
    }

    /**
     * Determines if a column with a particular name exist in the list of columns.
     * @param columns                    The list of columns
     * @param colName                    The name of the column
     * @return                                Whether the column is present
     */
    public static boolean columnExists(List<Column> columns, Bytes colName) {
        for (Column column : columns)
            if (column.name.equals(nullSafeGet(colName)))
                return true;
        return false;
    }

    /**
     * Determines if a column with a particular name exist in the list of columns.
     * @param columns                    The list of columns
     * @param colName                    The name of the column
     * @return                                Whether the column is present
     */
    public static boolean columnExists(List<Column> columns, String colName) {
        return columnExists(columns, fromUTF8(colName));
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the value
     * @return                               The column value
     * @throws ArrayIndexOutOfBoundsException    Thrown if the specified column was not found
     */
    public static Bytes getColumnValue(List<Column> columns, Bytes colName) throws ArrayIndexOutOfBoundsException {
        for (Column column : columns)
            if (column.name.equals(nullSafeGet(colName)))
                return fromByteBuffer(column.value);
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the value
     * @return                               The column value
     * @throws ArrayIndexOutOfBoundsException    Thrown if the specified column was not found
     */
    public static Bytes getColumnValue(List<Column> columns, String colName) throws ArrayIndexOutOfBoundsException {
        return getColumnValue(columns, fromUTF8(colName));
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the value
     * @return                               The column value as a <code>String</code>
     * @throws ArrayIndexOutOfBoundsException    Thrown if the specified column was not found
     */
    public static String getColumnStringValue(List<Column> columns, String colName) throws ArrayIndexOutOfBoundsException {
        return getColumnStringValue(columns, fromUTF8(colName));
    }

    /**
     * Get the value of a column in a list of columns
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the value
     * @return                               The column value as a <code>String</code>
     * @throws ArrayIndexOutOfBoundsException    Thrown if the specified column was not found
     */
    public static String getColumnStringValue(List<Column> columns, Bytes colName) throws ArrayIndexOutOfBoundsException {
        for (Column column : columns)
            if (column.name.equals(nullSafeGet(colName)))
                return toUTF8(column.value);
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Get the time stamp of a column in a list of columns.
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the timestamp
     * @return                               The time stamp (the <code>Mutator</code> object uses time stamps as microseconds)
     * @throws ArrayIndexOutOfBoundsException    Thrown if the list does not contain a column with the specified name
     */
    public static long getColumnTimestamp(List<Column> columns, Bytes colName) throws ArrayIndexOutOfBoundsException {
        for (Column column : columns)
            if (column.name.equals(nullSafeGet(colName)))
                return column.getTimestamp();
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Get the time stamp of a column in a list of columns.
     * @param columns                        The list of columns
     * @param colName                        The name of the column from which to retrieve the timestamp
     * @return                               The time stamp (the <code>Mutator</code> object uses time stamps as microseconds)
     * @throws ArrayIndexOutOfBoundsException    Thrown if the list does not contain a column with the specified name
     */
    public static long getColumnTimestamp(List<Column> columns, String colName) throws ArrayIndexOutOfBoundsException {
        return getColumnTimestamp(columns, fromUTF8(colName));
    }

    /**
     * Create a batch mutation operation.
     */
    public Selector(IThriftPool thrift) {
        super(thrift);
    }

    private static ColumnParent newColumnParent(String columnFamily, String superColName) {
        return newColumnParent(columnFamily, Bytes.fromUTF8(superColName));
    }

    private static ColumnParent newColumnParent(String columnFamily, Bytes superColName) {
        ColumnParent parent = new ColumnParent(columnFamily);
        parent.setSuper_column(nullSafeGet(superColName));
        return parent;
    }

    public static ColumnParent newColumnParent(String columnFamily) {
        return new ColumnParent(columnFamily);
    }

    private static List<Column> toColumnList(List<ColumnOrSuperColumn> coscList) {
        List<Column> columns = new ArrayList<Column>(coscList.size());
        for (ColumnOrSuperColumn cosc : coscList) {
            assert cosc.column != null : "The column should not be null";
            columns.add(cosc.column);
        }
        return columns;
    }

    private static List<SuperColumn> toSuperColumnList(List<ColumnOrSuperColumn> coscList) {
        List<SuperColumn> columns = new ArrayList<SuperColumn>(coscList.size());
        for (ColumnOrSuperColumn cosc : coscList) {
            assert cosc.super_column != null : "The super column should not be null";
            columns.add(cosc.super_column);
        }
        return columns;
    }

}
