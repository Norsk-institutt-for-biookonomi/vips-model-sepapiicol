# SeptoriaApiicolaModel

<img src="src/main/resources/images/septoriaapiicola.jpg" title="S. apiicola in celery (Photo: E. Fl&oslash;istad, NIBIO)"/>
<p>This model is based on a calculation of how leaf wetness periods influence 
infection of celery by <em>Septoria apiicola</em> in susceptible host plants.<p>
<p>Forecasts of infection risk are given after a minimum of 12 consecutive hours 
of leaf wetness. The wet periods are calculated from sensors placed 30 cm above 
ground. If such a sensor is not available, data from leaf wetness sensors placed 
2 m above ground are used.</p>
<p>The model is developed in Michigan, USA and published by Lacy, 1994.</p> 
<p>It is generally assumed that this model has additional relevance for Septoria petroselini in parsley.</p> 
<h3>Warning interpretation</h3>
<p><em>The following recommendations are provided for the Norwegian celery production.</em></p>
<p>The model is a tool to decide when to start looking for initial disease 
outbreaks. It is not considered accurate enough to provide reliable advice for 
timing of fungicide applications under Norwegian conditions. </p>
<p>If inoculum is present, infections are expected to develop 7-14 days after 
the first infection risk periods (yellow or red warnings). It is recommended to 
intensify field inspections after periods with risk warnings to detect early 
infections of <em>Septoria apiicola</em>. </p>
<ul>
<li>Green: No risk of infection.</li> 
<li>Yellow: Weather conditions favorable for infection risk. No known sources of 
inoculum in the region.</li> 
<li>Red: Risk of infection and infections of Septoria late blight has been 
reported in the nearby region. Disease reports and field inspections are 
performed in collaboration with advisors in the Norwegian Agricultural Advisory service.</li>
</ul>
<p>Graph legend: Blue bars indicate periods of at least 12 consecutive hours of 
leaf wetness. Black bar indicate date of first field observation of Septoria late 
blight.</p>
<h3>Forecast season (Norway only)</h3>
<p>In Norway this model is generally started June 1 and ends September 30. </p>
<p>The model is running on all weather stations located in regions of commercial celery production.</p> 
<h3>Model validation</h3>
<h4>Norway</h4>
<p>Validation of this model under Norwegian conditions is ongoing. The current 
recommendation for this model is as a tool for timing of field inspections. </p>
<p>Studies of weather conditions, spore production and infection events have been 
carried out in field trials at Ås from 2012-2016. Preliminary results indicate a 
good fit, although some adjustments could improve the model, particularly in 
connection with rain events (Nordskog and Le, 2014). </p>
<h4>Internationally</h4>
<p>The model is developed in Michigan, USA and published by Lacy (1994)</p>
<h3>References</h3>
<p>Lacy, M.L. 1994. Influence of wetness periods on infection of celery by 
<em>Septoria apiicola</em> and use in timing sprays for control. Plant Disease 78, 975-979.</p>
<p>Nordskog, B. og Le, V.H. 2015. Varsling av selleribladflekk. Gartneryrket nr 6, s14-15.</p>

### Development

Clone Gitlab repository

```
$ git clone https://gitlab.nibio.no/VIPS/models/java/Model_SEPAPIICOL.git
$ cd Model_SEPAPIICOL
```