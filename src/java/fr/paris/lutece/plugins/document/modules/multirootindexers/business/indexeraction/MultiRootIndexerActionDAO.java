/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.document.modules.multirootindexers.business.indexeraction;

import fr.paris.lutece.portal.business.indexeraction.IIndexerActionDAO;
import fr.paris.lutece.portal.business.indexeraction.IndexerAction;
import fr.paris.lutece.portal.business.indexeraction.IndexerActionFilter;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Insert into multiple tables for indexer actions : core_indexer_action and core_indexer_action_en. <br>
 * Reads from only one table : {@link #setLang(String)} with {@link #LANG_ENGLISH} to use english tables.<br>
 * Update is not supported.
 */
public class MultiRootIndexerActionDAO implements IIndexerActionDAO
{
    // Constants
    public static final String CONSTANT_WHERE = " WHERE ";
    public static final String CONSTANT_AND = " AND ";

    // queries for core_indexer_action
    private static final String SQL_QUERY_NEW_PK_FR = "SELECT max( id_action ) FROM core_indexer_action";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY_FR = "SELECT id_action,id_document,id_task,indexer_name, id_portlet" +
        " FROM core_indexer_action WHERE id_action = ?";
    private static final String SQL_QUERY_INSERT_FR = "INSERT INTO core_indexer_action( id_action,id_document,id_task ,indexer_name,id_portlet)" +
        " VALUES(?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE_FR = "DELETE FROM core_indexer_action WHERE id_action = ? ";
    private static final String SQL_QUERY_TRUNCATE_FR = "TRUNCATE core_indexer_action  ";

    //private static final String SQL_QUERY_UPDATE_FR = "UPDATE core_indexer_action SET id_action=?,id_document=?,id_task=?,indexer_name=?,id_portlet=? WHERE id_action = ? ";
    private static final String SQL_QUERY_SELECT_FR = "SELECT id_action,id_document,id_task,indexer_name,id_portlet" +
        " FROM core_indexer_action ";

    // queries for core_indexer_action_en
    private static final String SQL_QUERY_NEW_PK_EN = "SELECT max( id_action ) FROM core_indexer_action_en";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY_EN = "SELECT id_action,id_document,id_task,indexer_name, id_portlet" +
        " FROM core_indexer_action_en WHERE id_action = ?";
    private static final String SQL_QUERY_INSERT_EN = "INSERT INTO core_indexer_action_en( id_action,id_document,id_task ,indexer_name,id_portlet)" +
        " VALUES(?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE_EN = "DELETE FROM core_indexer_action_en WHERE id_action = ? ";
    private static final String SQL_QUERY_TRUNCATE_EN = "TRUNCATE core_indexer_action_en  ";

    //private static final String SQL_QUERY_UPDATE_EN = "UPDATE core_indexer_action_en SET id_action=?,id_document=?,id_task=?,indexer_name=?,id_portlet=? WHERE id_action = ? ";
    private static final String SQL_QUERY_SELECT_EN = "SELECT id_action,id_document,id_task,indexer_name,id_portlet" +
        " FROM core_indexer_action_en ";
    private static final String SQL_FILTER_ID_TASK = " id_task = ? ";
    private static final String LANG_ENGLISH = "en";
    private String _strLang;

    /**
     * Gets the lang to use.
     * @return the lang
     */
    public String getLang(  )
    {
        return _strLang;
    }

    /**
     * Set to {@link #LANG_ENGLISH} to use the english table.
     * @param strLang the language (only {@link #LANG_ENGLISH} supported, any other value will use default table).
     */
    public void setLang( String strLang )
    {
        _strLang = strLang;
    }

    /**
     *
     *{@inheritDoc}
     */
    public int newPrimaryKey( String strQuery )
    {
        DAOUtil daoUtil = new DAOUtil( strQuery );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     *
     * Inserts in two tables.
     * @param indexerAction indexerAction
     */
    public void insert( IndexerAction indexerAction )
    {
        insert( SQL_QUERY_INSERT_EN, indexerAction, newPrimaryKey( SQL_QUERY_NEW_PK_EN ) );
        insert( SQL_QUERY_INSERT_FR, indexerAction, newPrimaryKey( SQL_QUERY_NEW_PK_FR ) );
    }

    /**
     * Inserts the indexeraction using the given query
     * @param strInsertQuery the query
     * @param indexerAction the action
     * @param nPrimaryKey primary key
     */
    private void insert( String strInsertQuery, IndexerAction indexerAction, int nPrimaryKey )
    {
        DAOUtil daoUtil = new DAOUtil( strInsertQuery );
        daoUtil.setString( 2, indexerAction.getIdDocument(  ) );
        daoUtil.setInt( 3, indexerAction.getIdTask(  ) );
        daoUtil.setString( 4, indexerAction.getIndexerName(  ) );
        daoUtil.setInt( 5, indexerAction.getIdPortlet(  ) );
        indexerAction.setIdAction( indexerAction.getIdAction(  ) );
        daoUtil.setInt( 1, nPrimaryKey );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * Load query
     * @return load query
     */
    private String getLoadQuery(  )
    {
        if ( LANG_ENGLISH.equals( _strLang ) )
        {
            return SQL_QUERY_FIND_BY_PRIMARY_KEY_EN;
        }

        return SQL_QUERY_FIND_BY_PRIMARY_KEY_FR;
    }

    /**
     *
     *{@inheritDoc}
     */
    public IndexerAction load( int nId )
    {
        IndexerAction indexerAction = null;

        DAOUtil daoUtil = new DAOUtil( getLoadQuery(  ) );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            indexerAction = new IndexerAction(  );
            indexerAction.setIdAction( daoUtil.getInt( 1 ) );
            indexerAction.setIdDocument( daoUtil.getString( 2 ) );
            indexerAction.setIdTask( daoUtil.getInt( 3 ) );
            indexerAction.setIndexerName( daoUtil.getString( 4 ) );
            indexerAction.setIdPortlet( daoUtil.getInt( 5 ) );
        }

        daoUtil.free(  );

        return indexerAction;
    }

    /**
     * Delete query
     * @return delete query
     */
    private String getDeleteQuery(  )
    {
        if ( LANG_ENGLISH.equals( _strLang ) )
        {
            return SQL_QUERY_DELETE_EN;
        }

        return SQL_QUERY_DELETE_FR;
    }

    /**
     *
     *{@inheritDoc}
     */
    public void delete( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( getDeleteQuery(  ) );
        daoUtil.setInt( 1, nId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Truncate query
     * @return truncate query
     */
    public String getTruncateQuery(  )
    {
        if ( LANG_ENGLISH.equals( _strLang ) )
        {
            return SQL_QUERY_TRUNCATE_EN;
        }

        return SQL_QUERY_TRUNCATE_FR;
    }

    /**
     *
     *{@inheritDoc}
     */
    public void deleteAll(  )
    {
        DAOUtil daoUtil = new DAOUtil( getTruncateQuery(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     *
     * Not supported (never used?)
     * @param indexerAction indexerAction
     */
    public void store( IndexerAction indexerAction )
    {
        throw new UnsupportedOperationException( "Store operation is not supported" );
    }

    /**
     * Select query
     * @return select query
     */
    private String getQuerySelect(  )
    {
        if ( LANG_ENGLISH.equals( _strLang ) )
        {
            return SQL_QUERY_SELECT_EN;
        }

        return SQL_QUERY_SELECT_FR;
    }

    /**
     *
     *{@inheritDoc}
     */
    public List<IndexerAction> selectList( IndexerActionFilter filter )
    {
        List<IndexerAction> indexerActionList = new ArrayList<IndexerAction>(  );
        IndexerAction indexerAction = null;
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( filter.containsIdTask(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_TASK );
        }

        String strSQL = buildRequestWithFilter( getQuerySelect(  ), listStrFilter, null );

        DAOUtil daoUtil = new DAOUtil( strSQL );

        int nIndex = 1;

        if ( filter.containsIdTask(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdTask(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            indexerAction = new IndexerAction(  );
            indexerAction.setIdAction( daoUtil.getInt( 1 ) );
            indexerAction.setIdDocument( daoUtil.getString( 2 ) );
            indexerAction.setIdTask( daoUtil.getInt( 3 ) );
            indexerAction.setIndexerName( daoUtil.getString( 4 ) );
            indexerAction.setIdPortlet( daoUtil.getInt( 5 ) );
            indexerActionList.add( indexerAction );
        }

        daoUtil.free(  );

        return indexerActionList;
    }

    /**
     *
     *{@inheritDoc}
     */
    public List<IndexerAction> selectList(  )
    {
        List<IndexerAction> indexerActionList = new ArrayList<IndexerAction>(  );
        IndexerAction indexerAction = null;

        DAOUtil daoUtil = new DAOUtil( getQuerySelect(  ) );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            indexerAction = new IndexerAction(  );
            indexerAction.setIdAction( daoUtil.getInt( 1 ) );
            indexerAction.setIdDocument( daoUtil.getString( 2 ) );
            indexerAction.setIdTask( daoUtil.getInt( 3 ) );
            indexerAction.setIndexerName( daoUtil.getString( 4 ) );
            indexerAction.setIdPortlet( daoUtil.getInt( 5 ) );
            indexerActionList.add( indexerAction );
        }

        daoUtil.free(  );

        return indexerActionList;
    }

    /**
    * Builds a query with filters placed in parameters
    * @param strSelect the select of the  query
    * @param listStrFilter the list of filter to add in the query
    * @param strOrder the order by of the query
    * @return a query
    */
    public static String buildRequestWithFilter( String strSelect, List<String> listStrFilter, String strOrder )
    {
        StringBuffer strBuffer = new StringBuffer(  );
        strBuffer.append( strSelect );

        int nCount = 0;

        for ( String strFilter : listStrFilter )
        {
            if ( ++nCount == 1 )
            {
                strBuffer.append( CONSTANT_WHERE );
            }

            strBuffer.append( strFilter );

            if ( nCount != listStrFilter.size(  ) )
            {
                strBuffer.append( CONSTANT_AND );
            }
        }

        if ( strOrder != null )
        {
            strBuffer.append( strOrder );
        }

        return strBuffer.toString(  );
    }

    /**
     *
     *{@inheritDoc}
     */
    public int newPrimaryKey(  )
    {
        throw new UnsupportedOperationException(  );
    }
}
