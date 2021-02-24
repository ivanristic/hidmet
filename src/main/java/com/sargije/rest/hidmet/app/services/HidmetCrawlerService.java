package com.sargije.rest.hidmet.app.services;

import com.sargije.rest.hidmet.app.config.MvcConfig;
import com.sargije.rest.hidmet.app.graphql.service.publishers.AirQualityPublisher;
import com.sargije.rest.hidmet.app.graphql.service.publishers.CurrentForecastPublisher;
import com.sargije.rest.hidmet.app.graphql.service.publishers.FivedayForecastPublisher;
import com.sargije.rest.hidmet.app.graphql.service.publishers.ShortTermForecastPublisher;
import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.repository.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@Service
public class HidmetCrawlerService {

	Logger logger = LoggerFactory.getLogger(HidmetCrawlerService.class);

	@Autowired
	MvcConfig mvcConfig;

	@Autowired
	CurrentForecastRepository currentForecastRepository;
	
	@Autowired
	ShortTermForecastRepository shortTermForecastRepository;
	
	@Autowired
	FiveDayForecastRepository fiveDayForecastRepository;
	
	@Autowired
	CityRepository cityRepository;
	
	@Autowired
	ForecastDateRepository forecastDateRepository;
	
	@Autowired
	DescriptionRepository descriptionRepository;

	@Autowired
	CurrentForecastPublisher currentForecastPublisher;

	@Autowired
	FivedayForecastPublisher fivedayForecastPublisher;

	@Autowired
	ShortTermForecastPublisher shortTermForecastPublisher;

	@Autowired
	AirQualityPublisher airQualityPublisher;

	@Autowired
	StationRepository stationRepository;

	@Autowired
	AirQualityRepository airQualityRepository;

	@Value("${hidmet.airquality.url}")
	private String airQualityURL;

	@Value("${hidmet.forecast.url}")
	private String forecastURL;

	//private final String cronFivedayForecast = "0 0/15 10-13 * * *";
	//private final String cronCurrentForecast = "0 7-17,20,30,40,50 * * * *";
	//private final String cronShortermForecast = "0 0/15 4-13 * * *";

	private final String cronFivedayForecast = "0 0/10 * * * *";
    private final String cronCurrentForecast = "0 0/1 * * * *";
	private final String cronShortermForecast = "0 0/10 * * * *";
	private final String cronAirQuality = "0 0/1 * * * *";

	@Scheduled(cron = cronFivedayForecast)
	private void populateFivedayForecast() {
		Document docFivedayForecastHref;
		// setting dateTime pattern
        DateTimeFormatter dateTimeFormatterFiveDay = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

		//logger.info("Fiveday Forecast crawler initialized");
			try {
				docFivedayForecastHref = Jsoup.connect(forecastURL+"prognoza/stanica.php").get();
	//			String[] stringTableTimestamp = doc.select("table tfoot tr td").get(0).text().split("\u00a0");
				LocalDateTime tableTime;
				// checking lenght of date field to verify if forecast is updated or regular
				if(docFivedayForecastHref.select("table tfoot tr td").get(0).text().length() > 40){
					// if forecast is updated date is starting form character 20 to 40
					tableTime = LocalDateTime.parse(docFivedayForecastHref.select("table tfoot tr td").get(0).text().substring(20, 40), dateTimeFormatterFiveDay);
				}else{
					// if forecast isn't updated date is starting from character 0 to 20
					tableTime = LocalDateTime.parse(docFivedayForecastHref.select("table tfoot tr td").get(0).text().substring(0, 20), dateTimeFormatterFiveDay);
				}

				// verify that forecast has changed
				if(!fiveDayForecastRepository.existsByActiveAndTableTime(true, tableTime)){

				logger.info("Fiveday forecast populating data " + LocalDateTime.now());

				docFivedayForecastHref.select("table").remove();
				// getting all the links for forecast
				Elements links = docFivedayForecastHref.select("div#sadrzaj div").get(0).select("a[href]");

					// cache evict and prepare for fresh data
					mvcConfig.fivedayForecastCacheEvict();

                    // get list of forecastsDates after yesterday
					List<ForecastDate> forecastDates = forecastDateRepository.findByForecastDateAfter(LocalDate.now().minusDays(1));

					//changing dateTime pattern to reflect new page
					//dateTimeFormatterFiveDay = DateTimeFormatter.ofPattern("dd.MM.yyyy");

					//updating all forecasts to false
					fiveDayForecastRepository.updateFivedayForecastActiveToFalse();

					List<FivedayForecast> listFiveDayForecast = new ArrayList<>();
					//getting list of Cities and Descriptions
					List<City> cities = (List<City>) cityRepository.findAll();
                    List<Description> currentDescriptions = (List<Description>) descriptionRepository.findAll();
                    // looping trough links list
					for(Element href : links){
						
						//String hcity = href.text();
						City city;

						// verify that there is city from link in list of Cities, if not create one, save to repository and add to list
						// I can change this not to create city in catch block (exists)
						try {
							city = cities.stream().filter(p -> p.getCityName().equals(href.text())).findFirst().get();
						}catch(NoSuchElementException e){
							city = new City();
							city.setCityName(href.text());
							cityRepository.save(city);
							cities.add(city);
						}

						// getting new page from link
						Document docFivedayForecast;
						try {
							Document.OutputSettings settings = new Document.OutputSettings();
							settings.escapeMode(Entities.EscapeMode.xhtml);

							docFivedayForecast = Jsoup.connect(forecastURL+"prognoza/"+href.attr("href")).execute().parse();
							docFivedayForecast.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

							// getting elements from forecast
							Elements tbodyRowsMaxTemp = docFivedayForecast.select("div#sadrzaj div table  tbody tr").get(1).select("td");
	  						Elements tbodyRowsMinTemp = docFivedayForecast.select("div#sadrzaj div table  tbody tr").get(3).select("td");
	  						Elements tbodyRowsImage = docFivedayForecast.select("div#sadrzaj div table  tbody tr").get(5).select("td");
	  						Elements theadRows = docFivedayForecast.select("div#sadrzaj div table  thead").get(0).select("tr th");
	  						Elements theadImages = docFivedayForecast.select("div#sadrzaj div table").get(1).select("tbody tr td");

							//int cnt = descriptionRepository.countByDescriptionNotNull();

							// check that size of description is not same as size in repository, if not populate with fresh data and return list
							/*
  							if((theadImages.size() / 2 ) - 1 != descriptionRepository.countByDescriptionNotNull()) {
								mvcConfig.descriptionCacheEvict();
  								populateDescriptionAndImageForForecast();
  								currentDescriptions = (List<Description>) descriptionRepository.findAll();
  							}
 							*/
							//Calendar calendar = new GregorianCalendar();
	  						for(int i=2; i<theadRows.size()-1; i++){
	  							String[] stringForecastTimestamp = theadRows.get(i).text().split(" ");

								// extracting date from link, verify that Date exist and if not insert date value in table
								/* @TODO
								 * check if it's the same month, if it is create local date withDayOfMonth(int dayOfMonth)
								 * if not use plusMonths(long month)
								 *
								*/


								LocalDate parseForecastDate;// = LocalDate.now().plusDays(Integer.parseInt(stringForecastTimestamp[1].substring(0, 2)) - LocalDate.now().getDayOfMonth());
								//System.out.println(stringForecastTimestamp[1].substring(3,  5));
								if(Integer.parseInt(stringForecastTimestamp[1].substring(3, 5)) == LocalDate.now().getMonth().getValue()){
									parseForecastDate = LocalDate.now().withDayOfMonth(Integer.parseInt(stringForecastTimestamp[1].substring(0, 2)));
								}else{
									parseForecastDate = LocalDate.now().withDayOfMonth(Integer.parseInt(stringForecastTimestamp[1].substring(0, 2))).plusMonths(1L);
								}
                                ForecastDate forecastDate;
                                try {
                                    forecastDate = forecastDates.stream().filter(p -> p.getForecastDate().equals(parseForecastDate)).findFirst().get();

                                }catch(NoSuchElementException e){

	  								forecastDate = new ForecastDate();
                                    forecastDate.setForecastDate(parseForecastDate);
	  								forecastDateRepository.save(forecastDate);
									forecastDates.add(forecastDate);
	  							}

                                // create new forecast and and populate from fields
								FivedayForecast fiveDayForecastModel = new FivedayForecast();
	  							fiveDayForecastModel.setCity(city);
	  							fiveDayForecastModel.setForecastDate(forecastDate);
	  							
	  							if(!tbodyRowsMinTemp.get(i).text().equals("-")){
	  								fiveDayForecastModel.setMinTemperature(Integer.parseInt(tbodyRowsMinTemp.get(i).text()));
	  							}
	  							fiveDayForecastModel.setMaxTemperature(Integer.parseInt(tbodyRowsMaxTemp.get(i).text()));
	  							fiveDayForecastModel.setActive(true);

	  							String img = tbodyRowsImage.get(i).select("img").attr("src");
								Description image;

								try {
									image = currentDescriptions.stream().filter(p -> p.getImageLocation().equals(img)).findFirst().get();
								} catch (NoSuchElementException e) {
									Description descriptionModel = new Description();
									descriptionModel.setImageLocation(img);
									descriptionRepository.save(descriptionModel);
									currentDescriptions.add(descriptionModel);
									image = descriptionModel;
								}

	  							fiveDayForecastModel.setDescription(image);
	  							fiveDayForecastModel.setTableTime(tableTime);
	  							
	  							listFiveDayForecast.add(fiveDayForecastModel);

	  						}
	  						
						}catch (IOException  e) {
						    System.out.println(LocalDateTime.now());
							e.printStackTrace();							
						}	
						
						
					}
					/* all in one update to database*/
					fiveDayForecastRepository.saveAll(listFiveDayForecast);
					/* publish to GraphQL subscribers */
					fivedayForecastPublisher.publish(listFiveDayForecast);
				}
			} catch (IOException e) {
                System.out.println("FivedayForecast "+LocalDateTime.now());
				e.printStackTrace();
			}

    }
	
	@Scheduled(cron = cronCurrentForecast)
	private void populateCurrentForecast() {

		Document docCurrentForecast;
		// setting dateTime pattern
		DateTimeFormatter dtFormatterCurrentForecast = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
			try {
				//connecting to webpage
				docCurrentForecast = Jsoup.connect(forecastURL + "osmotreni/index.php").get();
				docCurrentForecast.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
				//getting rows of interest
				Elements tbodyRows = docCurrentForecast.select("table tbody").get(0).select("tr");
                List<String> theadFields = docCurrentForecast.select("table").get(0).select("thead tr th").eachText();
				//getting date and formating
				LocalDateTime tableTime = LocalDateTime.parse(docCurrentForecast.select("table tfoot tr td").get(0).text().substring(22, 38), dtFormatterCurrentForecast);

				//if there is no forecast with that date continue
				if(!currentForecastRepository.existsByActiveAndTableTime(true, tableTime)){

					logger.info("Current forecast populating data " + LocalDateTime.now());
					//clear cache
					mvcConfig.currentForecastCacheEvict();
					//preparing database for new entries
					currentForecastRepository.updateCurrentForecastActiveToFalse();
					//getting values for cities and descriptions trough one call
					List<City> cities = cityRepository.findDistinctByCurrentForecastsNotNull();//.findAll();
					List<Description> descriptions = (List<Description>) descriptionRepository.findAll();

					List<CurrentForecast> listCurrentForecastsModel = new ArrayList<>();

					//looping trough rows of interest
					for (Element tbodyRow : tbodyRows) {

						Elements tdRows = tbodyRow.select("tr").select("td");
						//getting City and cleaning data
						String hcity = tdRows.get(theadFields.indexOf("Stanica")).text().replaceAll("&nbsp; ", "");

						City city;
						Description description;

						// getting city from list and if doesn't exist create one
						try {
							city = cities.stream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
						} catch (NoSuchElementException e) {
							city = new City();
							city.setCityName(hcity);
							cityRepository.save(city);
							cities.add(city);
							//cities = (List<City>) cityRepository.findAll();
						}
						// if there is all data in row (avoiding jsoup bug)

						if (tdRows.size() == 9) {
							CurrentForecast currentForecastModel = new CurrentForecast();
							currentForecastModel.setCity(city);
							// getting values from html fields
							currentForecastModel.setTemperature(Integer.parseInt(tdRows.get(theadFields.indexOf("Temperatura (°C)")).text().replaceAll("[^\\d]", "")));//.trim().replaceAll("&nbsp; ", "")));
							currentForecastModel.setPressure(Float.parseFloat(tdRows.get(theadFields.indexOf("Pritisak (hPa)")).text().replaceAll("[^\\d.]", "")));//.trim().replaceAll("&nbsp; ", "")));
							currentForecastModel.setWindDirection(tdRows.get(theadFields.indexOf("Pravac vetra")).text().trim().replaceAll("&nbsp; ", ""));
							currentForecastModel.setWindSpeed(tdRows.get(theadFields.indexOf("Brzina vetra (m/s)")).text().trim().replaceAll("&nbsp; ", ""));
							currentForecastModel.setHumidity(Integer.parseInt(tdRows.get(theadFields.indexOf("Vlažnost (%)")).text().replaceAll("[^\\d]", "")));//.trim().replaceAll("&nbsp; ", "")));
							currentForecastModel.setFeelsLike(Integer.parseInt(tdRows.get(theadFields.indexOf("Subjektivni osećaj temperature (°C)")).text().replaceAll("[^\\d]", "")));//.trim().replaceAll("&nbsp; ", "")));
							String img = tdRows.get(theadFields.indexOf("Simbol")).select("img").attr("src");
							// getting description and if doesn't exist create one with value from field
							try {
								description = descriptions.stream().filter(p -> p.getImageLocation().equals(img)).findFirst().get();

							} catch (NoSuchElementException e) {
								Description descriptionModel = new Description();
								descriptionModel.setImageLocation(img);
                                descriptionModel.setDescription(tdRows.get(theadFields.indexOf("Opis vremena")).text().replaceAll("[^\\d]", ""));
								descriptionRepository.save(descriptionModel);
								description = descriptionModel;
								descriptions.add(description);
							}

							currentForecastModel.setDescription(description);

							currentForecastModel.setTableTime(tableTime);
							currentForecastModel.setActive(true);
							listCurrentForecastsModel.add(currentForecastModel);
						}
					}
					/* all in one commit */
					currentForecastRepository.saveAll(listCurrentForecastsModel);
					/* publish to graphql subscribers */
					currentForecastPublisher.publish(listCurrentForecastsModel);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("CurrentForecast" + LocalDateTime.now());
			}

	}
	
	@Scheduled(cron = cronShortermForecast)
	private void populateShortTermForecast() {
		Document docShortTermForecast;
		// setting dateTime pattern
		DateTimeFormatter dateTimeFormatterShortTermForecast = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
			try {

				docShortTermForecast = Jsoup.connect(forecastURL+"prognoza/index.php").get();
				docShortTermForecast.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

				// getting table
				Element table = docShortTermForecast.select("table").get(0); // select the first table.

				// splitting table to header footer and body
				Element thead = table.select("thead tr").get(0);
				Element tfoot = table.select("tfoot tr td").get(0);
				Element tbody = table.select("tbody").get(0);
				LocalDateTime tableTime = LocalDateTime.parse(tfoot.text().substring(20, 40), dateTimeFormatterShortTermForecast);
				Map<Integer, LocalDate> listStringDates = new HashMap<>();
				// verify if short term forecast exists for date and if not continue
				if(!shortTermForecastRepository.existsByActiveAndTableTime(true, tableTime)){

					logger.info("Short term forecast populating data " + LocalDateTime.now());
					//clear cache
					mvcConfig.shortTearmForecastCacheEvict();

					List<Description> currentDescriptions = (List<Description>) descriptionRepository.findAll();


					List<ShortTermForecast> listShortTermForecast = new ArrayList<>();

                   // LocalDate previousDay = LocalDate.now().minusDays(1);
					// getting all dates starting from yesterday
					List<ForecastDate> forecastDates = forecastDateRepository.findByForecastDateAfter(LocalDate.now().minusDays(1));

					// archiving records and preparing database from new data
					shortTermForecastRepository.updateShortTermForecastActiveToFalse();

					Elements theadRows = thead.select("th");
					// looping trough thead skipping first row because it holds column names
					// loop use to collect all dates and inserting them to database
					for (int i = 1; i < theadRows.size(); i++) { // first row is  the col names so skip it.

						Element th = theadRows.get(i);
						//
                        DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern(" dd.MM.yyyy.");
                        // getting date in new format
                        LocalDate parseForecastDate = LocalDate.parse(th.text().substring(th.text().lastIndexOf(' ')), dtformatter);
                        listStringDates.put(i, parseForecastDate);
                        //ForecastDate forecastDate;// = null;
						// if there is no date insert it to database
						// @TODO check if this code is redundant?
                        /*try {
                            forecastDate = forecastDates.stream().filter(p -> p.getForecastDate().equals(parseForecastDate)).findFirst().get();
                        }catch(NoSuchElementException e){
							//ForecastDate
                            forecastDate = new ForecastDate();
							forecastDate.setForecastDate(parseForecastDate);
							forecastDateRepository.save(forecastDate);
							forecastDates.add(forecastDate);
						}*/
					}
					
					Elements tbodyRows = tbody.select("tr");

                    List<City> cities = (List<City>) cityRepository.findAll();
					// looping trough table bodies and collecting data (Cities and forecast values)
					for (Element tr : tbodyRows) {

						Elements tdRows = tr.select("td");

						int tdSize = tdRows.size();

						Integer minTemp = null;
						Integer maxTemp = null;
						City city = null;


						for (int j = 0; j < tdSize; j++) {
							Element td = tdRows.get(j);
							// if it is firsc column it is city
							if (j == 0) {
								String hcity = td.text();

								// verify if there is city in database and if not insert one
								try {
									city = cities.stream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
								} catch (NoSuchElementException e) {
									city = new City();
									city.setCityName(hcity);
									cityRepository.save(city);
									cities.add(city);
								}
							// other than first column
							} else {
								// geting modulo 3 value
								switch (j % 3) {
									// if 0 than its minimum temperature
									case 0:
										minTemp = Integer.parseInt(td.html());
										break;
									// if 1 it is maximum temperature
									case 1:
										maxTemp = Integer.parseInt(td.html());
										break;
									// if 2  it is forecast date
									case 2:
										ForecastDate forecastDate;// = null;
										LocalDate parseForecastDate = listStringDates.get(j / 3 + 1);
										try {
											forecastDate = forecastDates.stream().filter(p -> p.getForecastDate().equals(parseForecastDate)).findFirst().get();
										} catch (NoSuchElementException e) {
											//ForecastDate
											forecastDate = new ForecastDate();
											forecastDate.setForecastDate(parseForecastDate);
											forecastDateRepository.save(forecastDate);
											forecastDates.add(forecastDate);
										}
										// setting values for short term forecast
										ShortTermForecast shortTermForecastModel = new ShortTermForecast();
										shortTermForecastModel.setTableTime(tableTime);
										shortTermForecastModel.setActive(true);
										if (minTemp != null) {
											shortTermForecastModel.setMinTemperature(minTemp);
										}
										shortTermForecastModel.setMaxTemperature(maxTemp);
										shortTermForecastModel.setCity(city);
										shortTermForecastModel.setForecastDate(forecastDate);
										String img = td.select("img").attr("src");
										// if there are no descriptions collect all of them in separate method
										/*if (currentDescriptions.stream().map(Description::getImageLocation).noneMatch(img::equals)) {
											mvcConfig.descriptionCacheEvict();

											populateDescriptionAndImageForForecast();
											currentDescriptions = (List<Description>) descriptionRepository.findAll();
										}

										 */
										Description image;
										try {
											image = currentDescriptions.stream().filter(p -> p.getImageLocation().equals(img)).findFirst().get();
										} catch (NoSuchElementException e) {
											Description descriptionModel = new Description();
											descriptionModel.setImageLocation(img);
											descriptionRepository.save(descriptionModel);
											currentDescriptions.add(descriptionModel);
											image = descriptionModel;
										}
										shortTermForecastModel.setDescription(image);
										listShortTermForecast.add(shortTermForecastModel);
										break;

								}
							}
						}
					}
					/* all in one commit */
					shortTermForecastRepository.saveAll(listShortTermForecast);
					/* publish to graphql subscribers */
					shortTermForecastPublisher.publish(listShortTermForecast);
				}
			}catch (IOException e) {
				System.out.println("ShortTermForecast" + LocalDateTime.now());

				e.printStackTrace();

		}
		
	}
	@Scheduled(cron = cronAirQuality)
	private void populateAirQuality() {
       Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		Document docAirQuality;
		Station station = null;
		DateTimeFormatter dateTimeFormatterAirQuality = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

		try {

            docAirQuality = Jsoup.connect(airQualityURL + "stanicepodaci.php").execute().parse();
            Elements tbodyStations = docAirQuality.select("table").get(0).select("tbody tr");
            List<String> theadFields = docAirQuality.select("table").get(0).select("thead tr th").eachText();

                String time = docAirQuality.select("body > div.admin-wrapper > div.admin-content > div > div.admin-content-header > div > div > div").select("a[href]").get(0).text().substring(21);
                LocalDateTime tableTime = LocalDateTime.parse(time, dateTimeFormatterAirQuality);
                //boolean isTableTime = airQualityRepository.existsByActiveAndTableTime(true, tableTime);
                if (!airQualityRepository.existsByActiveAndTableTime(true, tableTime)) {
                    logger.info("Air quality populating data " + LocalDateTime.now());
                    //  populateStations();

                    List<Station> stations = stationRepository.findAll();
                    airQualityRepository.updateAirQualitySetActiveToFalse();
                    List<AirQuality> listOfAirQuality = new ArrayList<>();
                    for (Element trStation : tbodyStations) {
                        Elements tdStation = trStation.select("td");
                        try {
                            station = stations.parallelStream().filter(x -> {
                                if (x.getStationName().equals("")) {
                                    if (tdStation.get(0).text().contains("-")) {
                                        if (x.getCity().getCityName().equals(tdStation.get(0).text().substring(0, tdStation.get(0).text().indexOf("-")))) {
                                            return true;
                                        }
                                    } else {
                                        if (x.getCity().getCityName().equals(tdStation.get(0).text())) {
                                            return true;
                                        }
                                    }
                                } else {
                                    if (tdStation.get(0).text().contains("-")) {
                                        if ((x.getCity().getCityName() + ' ' + x.getStationName()).equals(tdStation.get(0).text().substring(0, tdStation.get(0).text().indexOf("-")))) {
                                            return true;
                                        }
                                    } else {
                                        if ((x.getCity().getCityName() + ' ' + x.getStationName()).equals(tdStation.get(0).text())) {
                                            return true;
                                        }
                                    }
                                }
                                return false;
                            }).findAny().get();


                        } catch (NoSuchElementException ne) {
                            System.out.println(tdStation.get(0).text());
                            populateStations();
                            ne.printStackTrace();
                        }
                        AirQuality airQuality = new AirQuality();
                        //pattern.matcher(strNum).matches()

                        airQuality.setSulfurDioxide(!pattern.matcher(tdStation.get(theadFields.indexOf("SO2")).text()).matches() ? null : Float.valueOf(tdStation.get(theadFields.indexOf("SO2")).text()));
                        //airQuality.setSulfurDioxide(tdStation.get(2).text().equals("") || tdStation.get(2).text().equals("X") || tdStation.get(2).text().equals("N/A") ? null : Float.valueOf(tdStation.get(2).text()));
                        airQuality.setNitrogenDioxide(!pattern.matcher(tdStation.get(theadFields.indexOf("NO2")).text()).matches() ? null : Float.valueOf(tdStation.get(theadFields.indexOf("NO2")).text()));
                        //airQuality.setMonoNitrogenOxides(tdStation.get(4).text().equals("") || tdStation.get(4).text().equals("X") ? null : Float.valueOf(tdStation.get(4).text()));
                        //airQuality.setNitrogenOxide(tdStation.get(4).text().equals("") || tdStation.get(4).text().equals("X") ? null : Float.valueOf(tdStation.get(4).text()));
                        //after website update TenMicrometerPerDay is depricated
                        //airQuality.setParticleTenMicrometerPerDay(tdStation.get(6).text().equals("") || tdStation.get(6).text().equals("X") ? null : Float.valueOf(tdStation.get(6).text()));
                        airQuality.setParticleTenMicrometer(!pattern.matcher(tdStation.get(theadFields.indexOf("PM10")).text()).matches() ? null : Float.valueOf(tdStation.get(theadFields.indexOf("PM10")).text()));
                        airQuality.setParticleTwoAndAHalfMicrometer(!pattern.matcher(tdStation.get(theadFields.indexOf("PM2.5")).text()).matches() ? null : Float.valueOf(tdStation.get(theadFields.indexOf("PM2.5")).text()));
                        airQuality.setCarbonMonoxide(!pattern.matcher(tdStation.get(theadFields.indexOf("CO")).text()).matches() ? null : Float.valueOf(tdStation.get(theadFields.indexOf("CO")).text()));
                        airQuality.setOzon(!pattern.matcher(tdStation.get(theadFields.indexOf("O3")).text()).matches() ? null : Float.valueOf(tdStation.get(theadFields.indexOf("O3")).text()));
                        //airQuality.setBenzen(tdStation.get(10).text().equals("") || tdStation.get(10).text().equals("X") ? null : Float.valueOf(tdStation.get(10).text()));
                        //airQuality.setDd(tdStation.get(11).text().equals("") || tdStation.get(11).text().equals("X") ? null : Float.valueOf(tdStation.get(11).text()));
                        //airQuality.setSpeed(tdStation.get(12).text().equals("") || tdStation.get(12).text().equals("X") ? null : Float.valueOf(tdStation.get(12).text()));
                        //airQuality.setTemperature(tdStation.get(13).text().equals("") || tdStation.get(13).text().equals("X") ? null : Float.valueOf(tdStation.get(13).text()));
                        boolean airQualityNull = Stream.of(
                                airQuality.getSulfurDioxide(),
                                airQuality.getNitrogenDioxide(),
                                airQuality.getOzon(),
                                airQuality.getCarbonMonoxide(),
                                airQuality.getParticleTenMicrometer(),
                                airQuality.getParticleTwoAndAHalfMicrometer()
                        ).allMatch(Objects::isNull);

                        if (!airQualityNull) {
                            airQuality.setTableTime(tableTime);
                            airQuality.setStation(station);
                            airQuality.setActive(true);
                            listOfAirQuality.add(airQuality);
                        }

                    }
                    airQualityRepository.saveAll(listOfAirQuality);
                    airQualityPublisher.publish(listOfAirQuality);
                }

            } catch(IOException e){
                System.out.println("AirQuality" + LocalDateTime.now());
                e.printStackTrace();
            }

	}

	private void populateStations() {
		Document docStations;
		try {
			docStations = Jsoup.connect(airQualityURL+"pregledstanica.php").execute().parse();
			Elements tbodyStationsUrl = docStations.select("table").get(0).select("tbody tr").select("a[href]");

				List<Station> stations = stationRepository.findAll();
				List<City> cities = (List<City>) cityRepository.findAll();
				for (Element tdStation : tbodyStationsUrl) {
                    Document docii;
                    docii = Jsoup.connect(airQualityURL + tdStation.attr("href")).execute().parse();
					Elements tbodyStations = docii.select("table").get(0).select("tbody tr");

					String hcity = tbodyStations.get(1).select("td").get(1).text();
					String hstationNameTmp = tbodyStations.get(0).select("td").get(1).text().substring(hcity.length()).trim();
					String eoiCode = tbodyStations.get(4).select("td").get(1).text();

					if(hstationNameTmp.contains("-")){
						hstationNameTmp = hstationNameTmp.substring(0, hstationNameTmp.indexOf("-"));
					}
					String hstationName = hstationNameTmp;
					City city;
					Station station;
					try {
						city = cities.parallelStream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
					} catch (NoSuchElementException e) {
						city = new City();
						city.setCityName(hcity);
						cityRepository.save(city);
						cities.add(city);
					}
					try {
						station = stations.parallelStream().filter(e -> e.getEoiCode().equals(eoiCode)).findAny().get();
						//if someone changes name but leaves same eoi code
						if(!(station.getStationName().equals(hstationName) && station.getCity().getCityName().equals(hcity))){
							station.setStationName(hstationName);
							stationRepository.save(station);
						}
					} catch (NoSuchElementException e) {
						station = new Station();
						station.setCity(city);
						station.setStationName(hstationName);
						station.setEoiCode(tbodyStations.get(4).select("td").get(1).text());
						station.setClassification(tbodyStations.get(5).select("td").get(1).text());
						station.setZone(tbodyStations.get(6).select("td").get(1).text());
						station.setNetwork(tbodyStations.get(3).select("td").get(1).text());
						stationRepository.save(station);
						stations.add(station);
					}

				}
		//	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/* unnecesery
	private void populateDescriptionAndImageForForecast() {
		Document docDescriptions;

			try {

				mvcConfig.descriptionCacheEvict();

				docDescriptions = Jsoup.connect(foreacasURL + "prognoza/stanica.php").execute().parse();
				Elements theadImages = docDescriptions.select("div#sadrzaj div table").get(1).select("tbody tr td");
				List<Description> currentDescriptions = (List<Description>) descriptionRepository.findAll();
					for(int j=0; j<theadImages.size(); j=j+2) {
						if (!theadImages.get(j).select("img").attr("src").equals("")) {
							if (currentDescriptions.stream().map(Description::getImageLocation).noneMatch(theadImages.get(j).select("img").attr("src")::equals)) {
								Description imageModel = new Description();
								imageModel.setImageLocation(theadImages.get(j).select("img").attr("src"));
								imageModel.setDescription(theadImages.get(j).select("img").attr("alt"));
								currentDescriptions.add(imageModel);
							}
						}
					}
					descriptionRepository.saveAll(currentDescriptions);
				
			} catch (IOException e) {


				e.printStackTrace();
			}
			
		
	}
 */
}
