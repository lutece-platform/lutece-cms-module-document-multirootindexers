/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.document.modules.multirootindexers.util;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * This class provides utility methods.
 */
public final class PageTreeUtils
{
    /** Regexp used to skip attributes */
    public static final String NOT_INDEXED_REGEXP = AppPropertiesService.getProperty( 
            "document-multirootindexers.document.attributes.notindexed" );

    /**
     * Private constructor.
     */
    private PageTreeUtils(  )
    {
        // nothing
    }

    /**
     * Gets all pages from root page (instead of all db page)
     * @return all pages from root id
     */
    public static List<Page> getListPagesFromRoot(  )
    {
        int nRootId = PortalService.getRootPageId(  );
        List<Page> listPages = new ArrayList<Page>(  );

        Page rootPage = PageHome.findByPrimaryKey( nRootId );

        listPages.add( rootPage );
        listPages.addAll( getChildPages( nRootId ) );

        return listPages;
    }

    /**
     * Gets all pages id from root page
     * @return all pages id from root id.
     */
    public static Set<Integer> getListPagesIdsFromRoot(  )
    {
        int nRootId = PortalService.getRootPageId(  );
        Set<Integer> setPagesIds = new TreeSet<Integer>(  );

        setPagesIds.add( nRootId );
        setPagesIds.addAll( getListChildPagesids( nRootId ) );

        return setPagesIds;
    }

    /**
     * Gets children pages page (recursively)
     * @param nIdPage root page
     * @return all childrenpages
     */
    private static Collection<Page> getChildPages( int nIdPage )
    {
        Collection<Page> collectionPages = PageHome.getChildPages( nIdPage );
        List<Page> listChildsPage = new ArrayList<Page>(  );

        for ( Page page : collectionPages )
        {
            listChildsPage.addAll( getChildPages( page.getId(  ) ) );
        }

        collectionPages.addAll( listChildsPage );

        return collectionPages;
    }

    /**
     * Gets children pages id page (recursively)
     * @param nIdPage root page
     * @return all childrenpages
     */
    private static Set<Integer> getListChildPagesids( int nIdPage )
    {
        Set<Integer> setPagesIds = new TreeSet<Integer>(  );
        Collection<Page> collectionPages = PageHome.getChildPagesMinimalData( nIdPage );
        Set<Integer> setChildPagesIds = new TreeSet<Integer>(  );

        for ( Page page : collectionPages )
        {
            setChildPagesIds.addAll( getListChildPagesids( page.getId(  ) ) );
            setPagesIds.add( page.getId(  ) );
        }

        setPagesIds.addAll( setChildPagesIds );

        return setPagesIds;
    }
}
