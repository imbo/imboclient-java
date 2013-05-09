/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Images;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

/**
 * Images query interface
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface QueryInterface {

    /**
     * Get the page property
     *
     * @return Page number
     */
    public int page();

    /**
     * Set the page property
     *
     * @param pageNum Page number
     * @return Returns this query instance
     */
    public QueryInterface page(int pageNum);

    /**
     * Get the limit property
     *
     * @return Max number of items
     */
    public int limit();

    /**
     * Set the limit property
     *
     * @param limit Max number of items
     * @return Returns this query instance
     */
    public QueryInterface limit(int limit);

    /**
     * Get whether to return the meta data for each image along with the base image data
     *
     * @return Whether to return meta data or not
     */
    public boolean returnMetadata();

    /**
     * Set whether to return the meta data for each image along with the base image data
     *
     * @param returnMetadata True if meta data is wanted, false otherwise
     * @return Returns this query instance
     */
    public QueryInterface returnMetadata(boolean returnMetadata);

    /**
     * Get the meta data query sent along with this request
     *
     * @return The meta data query
     */
    public JSONObject metadataQuery();

    /**
     * Set the meta data query sent along with this request
     *
     * @param metadataQuery The meta data query as a JSON object
     * @return Returns this query instance
     */
    public QueryInterface metadataQuery(JSONObject metadataQuery);

    /**
     * Gets the lower date limit to fetch from
     *
     * @return Date to fetch from
     */
    public Date from();

    /**
     * Sets the date to fetch from when querying
     *
     * @param from The lower date limit to fetch from
     * @return Returns this query instance
     */
    public QueryInterface from(Date from);

    /**
     * Gets the upper date limit to fetch from
     *
     * @return Date to fetch to
     */
    public Date to();

    /**
     * Sets the date to fetch to when querying
     *
     * @param to The upper date limit to fetch to
     * @return Returns this query instance
     */
    public QueryInterface to(Date to);
    
    /**
     * Returns the parameters as a HashMap
     * 
     * @return HashMap of key => values
     */
    public HashMap<String, String> toHashMap();

}