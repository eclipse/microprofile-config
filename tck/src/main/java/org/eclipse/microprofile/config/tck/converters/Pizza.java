/*
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.eclipse.microprofile.config.tck.converters;

/**
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public class Pizza {
    private String flavor;
    private String size;


    public Pizza(String flavour, String size) {
        this.flavor = flavour;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public String getFlavor() {
        return flavor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
       final int prime = 31;
       int result = 1;
       result = prime * result + ((flavor == null) ? 0 : flavor.hashCode());
       result = prime * result + ((size == null) ? 0 : size.hashCode());
       return result;
    }

   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Pizza other = (Pizza) obj;
        if (flavor == null) {
           if (other.flavor != null) {
               return false;
           }
        } 
        else if (!flavor.equals(other.flavor)) {
           return false;
        }
        if (size == null) {
           if (other.size != null) {
               return false;
           }
        } 
        else if (!size.equals(other.size)) {
            return false;
        }
        return true;
    }
    
  
}
