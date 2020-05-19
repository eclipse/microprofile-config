/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck;

import java.util.Objects;

public class Location {
    private String road;
    private String city;
    private String county;
    private String postcode;
    private String country;

    public Location(String loc) {
        String[] parts = loc.split(",");
        this.road = parts[0];
        this.city = parts[1];
        this.county = parts[2];
        this.postcode=parts[3];
        this.country=parts[4];
    }


    /**
     * @return String return the road
     */
    public String getRoad() {
        return road;
    }

    /**
     * @return String return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return String return the county
     */
    public String getCounty() {
        return county;
    }

    /**
     * @return String return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @return String return the country
     */
    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(road, location.road) && Objects.equals(city, location.city) 
            && Objects.equals(county, location.county) && Objects.equals(postcode, location.postcode) && Objects.equals(country, location.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(road, city, county, postcode, country);
    }


}
