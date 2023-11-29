/*
 * Copyright (c) 2018 NIBIO <http://www.nibio.no/>. 
 *
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import no.nibio.vips.entity.ModelConfiguration;
import no.nibio.vips.entity.Result;
import no.nibio.vips.entity.ResultImpl;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.i18n.I18nImpl;
import no.nibio.vips.model.ConfigValidationException;
import no.nibio.vips.model.Model;
import no.nibio.vips.model.ModelExcecutionException;
import no.nibio.vips.model.ModelId;
import no.nibio.vips.observation.Observation;
import no.nibio.vips.observation.ObservationImpl;
import no.nibio.vips.util.ModelUtil;
import no.nibio.vips.util.WeatherUtil;

/**
 * Model developed in 2011 by Berit Nordskog and Arne Hermansen, NIBIO Division Biotechnology and Plant Health
 * Based on Lacy, M.L. 1994. Influence of wetness periods on infection of celery 
 * by Septoria apiicola and use in timing sprays for control. Plant Disease 78, 975-979.
 * Weather criteria: Minimum 12 consecutive hours with leaf wetness > 48 (minutes/hour) gives infection risk
 * If weather conditions are met, the infection risk is considered high if at lease one disease observation
 * has been made in the field in the same season, moderate otherwise.
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class SeptoriaApiicolaModel extends I18nImpl implements Model{

    private final static ModelId MODEL_ID = new ModelId("SEPAPIICOL");
    
    private final static Integer NIGHT_STARTS_AT_HOUR = 13;
    private final static Integer MINIMUM_LW_VALUE = 48;
    private final static Integer MINIMUM_CONSECUTIVE_WET_HOURS = 12;
    
    private ObjectMapper objectMapper;
    private final ModelUtil modelUtil;
    
    private DataMatrix dataMatrix;
    private List<Observation> pestObservations;
    private TimeZone timeZone;
    
    public SeptoriaApiicolaModel()
    {
        super("no.nibio.vips.model.septoriaapiicolamodel.texts");
        this.modelUtil = new ModelUtil();
    }
    
    @Override
    public List<Result> getResult() throws ModelExcecutionException {
        this.calculateLeafWetnessConditions();
        Observation firstSeasonPestObservation = this.getFirstSeasonPestObservation();
        Date firstSeasonPestObservationDate = firstSeasonPestObservation != null ? 
                new WeatherUtil().normalizeToExactDate(firstSeasonPestObservation.getTimeOfObservation(), this.timeZone) 
                : null;
        
        Date currentDate = this.dataMatrix.getFirstDateWithParameterValue(DataMatrix.WET_DAY);
        Date endDate = this.dataMatrix.getLastDateWithParameterValue(DataMatrix.WET_DAY);
        Calendar cal = Calendar.getInstance(timeZone);
        List<Result> retVal = new ArrayList<>();
        while(currentDate.compareTo(endDate) <= 0)
        {
            Result result = new ResultImpl();
            result.setValidTimeStart(currentDate);
            Integer warningStatus = Result.WARNING_STATUS_NO_RISK;
            if(this.dataMatrix.getParamStringValueForDate(currentDate, DataMatrix.WET_DAY).equals(Boolean.TRUE.toString()))
            {
                // We have wet day. Status MINOR or HIGH RISK is decided based on season pest observation(s)
                if(firstSeasonPestObservationDate == null || currentDate.before(firstSeasonPestObservationDate))
                {
                    warningStatus = Result.WARNING_STATUS_MINOR_RISK;
                }
                else
                {
                    warningStatus = Result.WARNING_STATUS_HIGH_RISK;
                }
            }
            result.setWarningStatus(warningStatus);
            result.setValue(SeptoriaApiicolaModel.MODEL_ID.toString(), DataMatrix.WET_DAY, this.dataMatrix.getParamStringValueForDate(currentDate, DataMatrix.WET_DAY).equals(Boolean.TRUE.toString()) ? "1" : "0");
            result.setValue(SeptoriaApiicolaModel.MODEL_ID.toString(), DataMatrix.PEST_OBSERVED, 
                    this.dataMatrix.getParamIntValueForDate(currentDate, DataMatrix.PEST_OBSERVED) != null ? 
                            String.valueOf(this.dataMatrix.getParamIntValueForDate(currentDate, DataMatrix.PEST_OBSERVED)) 
                            : "0"
            );
            retVal.add(result);
            
            // Moving on
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, 1);
            currentDate = cal.getTime();
        }
        return retVal;
    }

    @Override
    public ModelId getModelId() {
        return SeptoriaApiicolaModel.MODEL_ID;
    }

    @Override
    public String getModelName() {
        return this.getModelName(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getModelName(String language) {
        return this.getText("name", language);
    }

    @Override
    public String getLicense() {
        return "\n" +
            "/*\n" +
            " * Copyright (c) 2018 NIBIO <http://www.nibio.no/>. \n" +
            " *\n" +
            " * This program is free software: you can redistribute it and/or modify\n" +
            " * it under the terms of the GNU Affero General Public License as published by\n" +
            " * the Free Software Foundation, either version 3 of the License, or\n" +
            " * (at your option) any later version.\n" +
            " *\n" +
            " * This program is distributed in the hope that it will be useful,\n" +
            " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
            " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
            " * GNU Affero General Public License for more details.\n" +
            " *\n" +
            " * You should have received a copy of the GNU Affero General Public License\n" +
            " * along with this program.  If not, see <https://www.gnu.org/licenses/>.\n" +
            " *\n" +
            " */";
    }

    @Override
    public String getCopyright() {
        return "(c) 2016 NIBIO (http://www.nibio.no/). Contact: post@nibio.no";
    }

    @Override
    public String getModelDescription() {
        return this.getModelDescription(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getModelDescription(String language) {
        try
        {
            return this.modelUtil.getTextWithBase64EncodedImages(this.getText("description", language), this.getClass());
        }
        catch(IOException ex)
        {
            return this.getText("description", language);
        }
    }

    @Override
    public String getWarningStatusInterpretation() {
        return this.getWarningStatusInterpretation(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getWarningStatusInterpretation(String language) {
        return this.getText("statusInterpretation", language);
    }

    @Override
    public String getModelUsage() {
        return this.getModelUsage(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getModelUsage(String language) {
        return this.getText("usage", language);
    }

    @Override
    public String getSampleConfig() {
        return  "{\n" +
                "\t\"loginInfo\":{\n" +
                "\t\t\"username\":\"example\",\n" +
                "\t\t\"password\":\"example\"\n" +
                "\t},\n" +
                "\t\"modelId\":\"" + MODEL_ID.toString() + "\",\n" +
                "\t\"configParameters\":{\n" +
                "\t\t\"timeZone\":\"Europe/Oslo\",\n" +
                "\t\t\"startDateGrowth\":\"2012-03-25\",\n" +
                "\t\t\"endDateCalculation\":\"2012-03-25\",\n" +
                "\t\t\"pestObservations\":[\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"timeOfObservation\":\"2016-04-20T00:00:00+02:00\",\n" +
                "\t\t\t\t\"observationData\":{}\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"observations\":[\n" +
                "\t\t{\n" +
                "\t\t\t\t\"timeMeasured\": \"2016-06-20T00:00:00+02:00\",\n" +
                "\t\t\t\t\"elementMeasurementTypeId\":\"BT\",\n" +
                "\t\t\t\t\"logIntervalId\":1,\n" +
                "\t\t\t\t\"value\":48\n" +
                "\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}\n";
    }

    @Override
    public void setConfiguration(ModelConfiguration config) throws ConfigValidationException {
        
        this.dataMatrix = new DataMatrix();
        WeatherUtil wUtil = new WeatherUtil();
        this.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Setting time zone
        this.timeZone = TimeZone.getTimeZone((String) config.getConfigParameter("timeZone"));
        // Getting observation data
        this.pestObservations = this.getObjectMapper().convertValue(config.getConfigParameter("pestObservations"), new TypeReference<List<Observation>>(){});
        if(this.pestObservations != null && ! this.pestObservations.isEmpty())
        {
            for(Observation o:this.pestObservations)
            {
                this.dataMatrix.setParamIntValueForDate(wUtil.normalizeToExactDate(o.getTimeOfObservation(), timeZone), DataMatrix.PEST_OBSERVED, 1);
            }
        }
        
        // Getting weather data
        
        List<WeatherObservation> observations = this.getObjectMapper().convertValue(config.getConfigParameter("observations"), new TypeReference<List<WeatherObservation>>(){});
        for(WeatherObservation o:observations)
        {
            if(o.getElementMeasurementTypeId().equals(DataMatrix.BT))
            {
                this.dataMatrix.setParamDoubleValueForDate(o.getTimeMeasured(), o.getElementMeasurementTypeId(), o.getValue());
            }
        }
    }
    
    private ObjectMapper getObjectMapper()
    {
        if(this.objectMapper == null)
        {
            this.objectMapper = new ObjectMapper();
        }
        return this.objectMapper;
    }

    /**
     * 
     * @return date of the first pest observation the current season. Null if none found
     */
    private Observation getFirstSeasonPestObservation() {
        // What's the season?
        // Assuming season starts the same year as first weather data
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this.dataMatrix.getFirstDateWithParameterValue(DataMatrix.BT));
        cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        Date seasonStart = cal.getTime();
        
        if(this.pestObservations != null && ! this.pestObservations.isEmpty())
        {
            Collections.sort(pestObservations);
            for(Observation o:pestObservations)
            {
                if(o.getTimeOfObservation().after(seasonStart))
                {
                    return o;
                }
            }
        }
        return null;
    }

    private void calculateLeafWetnessConditions() throws ModelExcecutionException {
        Calendar cal = Calendar.getInstance(timeZone);
        WeatherUtil wUtil = new WeatherUtil();
        cal.setTime(wUtil.normalizeToExactDate(this.dataMatrix.getFirstDateWithParameterValue(DataMatrix.BT), timeZone));
        // Add two days to be sure to have complete weather data
        cal.add(Calendar.DATE, 2);
        Date currentDate = cal.getTime();
        Date lastLeafWetnessTimestamp = this.dataMatrix.getLastDateWithParameterValue(DataMatrix.BT);
        
        while(currentDate.before(lastLeafWetnessTimestamp))
        {
            cal.setTime(currentDate);
            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);
            Date endOfCurrentDay = cal.getTime();
            cal.set(Calendar.MINUTE, 0);
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.HOUR_OF_DAY, SeptoriaApiicolaModel.NIGHT_STARTS_AT_HOUR);
            Date startOfYesterdayData = cal.getTime();
            
            Boolean wetConditionsMet = this.isWetConditionsMet(startOfYesterdayData, endOfCurrentDay);
            this.dataMatrix.setParamStringValueForDate(currentDate, DataMatrix.WET_DAY, wetConditionsMet.toString());
            // Moving forward
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, 1);
            currentDate = cal.getTime();
        }
    }

    private Boolean isWetConditionsMet(Date startOfYesterdayData, Date endOfCurrentDay) throws ModelExcecutionException {
        // Check if leaf wetness data series ends sometime today
        Date lastLeafWetnessTime = this.dataMatrix.getLastDateWithParameterValue(DataMatrix.BT);
        if(endOfCurrentDay.after(lastLeafWetnessTime))
        {
            endOfCurrentDay = lastLeafWetnessTime;
        }
        
        // Check conditions
        Calendar cal = Calendar.getInstance(timeZone);
        Date currentHour = startOfYesterdayData;
        Integer consecutiveWetHours = 0;
        while(currentHour.compareTo(endOfCurrentDay) <= 0)
        {
            Double hourLeafWetness = this.dataMatrix.getParamDoubleValueForDate(currentHour, DataMatrix.BT);
            if(hourLeafWetness == null)
            {
                throw new ModelExcecutionException("Missing leaf wetness at " + currentHour);
            }
            
            if(hourLeafWetness >= SeptoriaApiicolaModel.MINIMUM_LW_VALUE)
            {
                consecutiveWetHours++;
            }
            else
            {
                consecutiveWetHours = 0;
            }
            
            if(consecutiveWetHours >= SeptoriaApiicolaModel.MINIMUM_CONSECUTIVE_WET_HOURS)
            {
                return true;
            }
            cal.setTime(currentHour);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            currentHour = cal.getTime();
        }
        return false;
    }

}
