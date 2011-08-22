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
package fr.paris.lutece.plugins.document.modules.multirootindexers.service.indexers;

import fr.paris.lutece.plugins.document.modules.multirootindexers.util.PageTreeUtils;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.PageIndexer;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.lucene.document.Document;

import java.io.IOException;

import java.util.List;


/**
 * Indexes pages from lutece.page.root.
 */
public class MultiRootPageIndexer extends PageIndexer
{
    private static final String INDEXER_NAME = "PageIndexer";
    private static final String INDEXER_VERSION = "1.0.0";

    /**
     * Indexes all pages. <br>
     * {@inheritDoc}
     */
    public void indexDocuments(  ) throws IOException, InterruptedException, SiteMessageException
    {
        String strPageBaseUrl = AppPropertiesService.getProperty( PROPERTY_PAGE_BASE_URL );
        List<Page> listPages = PageTreeUtils.getListPagesFromRoot(  );

        for ( Page page : listPages )
        {
            UrlItem url = new UrlItem( strPageBaseUrl );
            url.addParameter( PARAMETER_PAGE_ID, page.getId(  ) );

            Document doc = null;

            try
            {
                doc = getDocument( page, url.getUrl(  ) );
            }
            catch ( Exception e )
            {
                String strMessage = "Page ID : " + page.getId(  );
                IndexationService.error( this, e, strMessage );
            }

            if ( doc != null )
            {
                IndexationService.write( doc );
            }
        }
    }

    /**
     *
     *{@inheritDoc}
     */
    public String getName(  )
    {
        return INDEXER_NAME;
    }

    /**
     *
     *{@inheritDoc}
     */
    public String getVersion(  )
    {
        return INDEXER_VERSION;
    }
}
