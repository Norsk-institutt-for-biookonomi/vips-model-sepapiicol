/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of LygusRugulipennisModel.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 */
package no.nibio.vips.model.septoriaapiicolamodel;

import no.nibio.vips.model.septoriaapiicolamodel.SeptoriaApiicolaModel;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import static junit.framework.Assert.fail;
import no.nibio.vips.entity.ModelConfiguration;
import no.nibio.vips.entity.Result;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.model.ModelId;
import no.nibio.vips.observation.Observation;
import no.nibio.vips.observation.ObservationImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author treinar
 */
public class SeptoriaApiicolaModelTest {
    
    public SeptoriaApiicolaModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getResult method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetResult() throws Exception {
        System.out.println("getResult");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        ModelConfiguration config = this.getConfiguration("/aas_2015.json");
        ObservationImpl obs = new ObservationImpl();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo"));
        cal.set(2015, Calendar.JULY, 15, 12, 2, 0);
        Date obsDate = cal.getTime();
        obs.setTimeOfObservation(obsDate);
        obs.setName("TEST");
        List<Observation> pestObservations = new ArrayList<>();
        pestObservations.add(obs);
        config.setConfigParameter("pestObservations", pestObservations);
        instance.setConfiguration(config);
        //System.out.println(config.toJSON());
        List<Result> result = instance.getResult();
        /*for(Result res:result)
        {
            System.out.println(res.toString());
        }*/
        assertNotNull(result);
    }

    /**
     * Test of getModelId method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetModelId() {
        System.out.println("getModelId");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        ModelId expResult = new ModelId("SEPAPIICOL");
        ModelId result = instance.getModelId();
        assertEquals(expResult.toString(), result.toString());

    }

    /**
     * Test of getModelName method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetModelName_0args() {
        System.out.println("getModelName");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getModelName();
        assertNotNull(result);
        assertTrue(!result.isBlank());
    }

    
    /**
     * Test of getLicense method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetLicense() {
        System.out.println("getLicense");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getLicense();
        assertNotNull(result);
    }

    /**
     * Test of getCopyright method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetCopyright() {
        System.out.println("getCopyright");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getCopyright();
        assertNotNull(result);
    }

    /**
     * Test of getModelDescription method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetModelDescription_0args() {
        System.out.println("getModelDescription");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getModelDescription();
        assertNotNull(result);
    }

    

    /**
     * Test of getWarningStatusInterpretation method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetWarningStatusInterpretation_0args() {
        System.out.println("getWarningStatusInterpretation");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getWarningStatusInterpretation();
        assertNotNull(result);
    }

    

    /**
     * Test of getModelUsage method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetModelUsage_0args() {
        System.out.println("getModelUsage");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getModelUsage();
        assertNotNull(result);
    }

    

    /**
     * Test of getSampleConfig method, of class SeptoriaApiicolaModel.
     */
    @Test
    public void testGetSampleConfig() {
        System.out.println("getSampleConfig");
        SeptoriaApiicolaModel instance = new SeptoriaApiicolaModel();
        String result = instance.getSampleConfig();
        assertNotNull(result);
    }

   
    
    private ModelConfiguration getConfiguration(String fileName)
    {
        try {
            ModelConfiguration config = new ModelConfiguration();
            config.setModelId("NEGPROGMOD");
            
            
            config.setConfigParameter("timeZone", "Europe/Oslo");
            BufferedInputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream(fileName));
            JsonFactory f = new MappingJsonFactory();
            JsonParser jp = f.createParser(inputStream);
            JsonNode all = jp.readValueAsTree();
            List<WeatherObservation> observations = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            Date firstDate = null;
            Date lastDate = null;
            if(all.isArray())
            {
                for(JsonNode node : all){
                    Date timeMeasured = (Date)mapper.convertValue(node.get("timeMeasured").asText(), new TypeReference<Date>(){});
                    if(firstDate == null || firstDate.compareTo(timeMeasured) > 0)
                    {
                        firstDate = timeMeasured;
                    }
                    if(lastDate == null || lastDate.compareTo(timeMeasured) < 0)
                    {
                        lastDate = timeMeasured;
                    }
                    //System.out.println(node.toString());
                    WeatherObservation observation = new WeatherObservation();
                    observation.setTimeMeasured(timeMeasured);
                    observation.setLogIntervalId(node.get("logIntervalId").asInt());
                    observation.setElementMeasurementTypeId(node.get("elementMeasurementTypeId").asText());
                    observation.setValue(node.get("value").asDouble());
                    observations.add(observation);
                }

            }
            else
            {
                fail("Data input from file is not a JSON array");
            }
            config.setConfigParameter("observations", observations);

            
            return config;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } 
    }
    
}
