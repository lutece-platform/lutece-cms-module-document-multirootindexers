/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.document.modules.multirootindexers.business.document;

import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortlet;
import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * MultiRootResourceDisplayManager.
 */
public class MultiRootResourceDisplayManager implements IResourceDisplayManager
{
    private static final String PROPERTY_ALLDATA_PAGE_ID = "document.alldata.page_id";
    private static final String TAG_ALLDATA_PAGE_ID = "alldata-page_id";

    /**
     *
     * Adds root page property. <br>
     * {@inheritDoc}
     */
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
        if ( DocumentListPortlet.RESOURCE_ID.equals( strResourceType ) )
        {
            String strIdPageAllData = AppPropertiesService.getProperty( PROPERTY_ALLDATA_PAGE_ID, "1" );
            XmlUtil.addElement( strXml, TAG_ALLDATA_PAGE_ID, strIdPageAllData );
        }
    }

    /**
     *
     *{@inheritDoc}
     */
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
        String strPortletId, HttpServletRequest request )
    {
        // nothing to do
    }
}
