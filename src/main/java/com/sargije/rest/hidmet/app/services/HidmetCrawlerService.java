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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class HidmetCrawlerService {

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
	private String foreacasURL;

	//private final String cronFivedayForecast = "0 0/15 11,12 * * *";
	//private final String cronCurrentForecast = "0 8-10,15,25,35,45 * * * *";
	//private final String cronShortermForecast = "0 0/15 4,5,9,11,12 * * *";

	private final String cronFivedayForecast = "0 0/10 * * * *";
	private final String cronCurrentForecast = "0 0/5 * * * *";
	private final String cronShortermForecast = "0 0/10 * * * *";
	private final String cronAirQuality = "0 0/10 * * * *";

	@Scheduled(cron = cronFivedayForecast)
	public void populateFivedayForecast() {	
		Document doc;

        //SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");

        DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");


			try {
				doc = Jsoup.connect(foreacasURL+"prognoza/stanica.php").get();
	//			String[] stringTableTimestamp = doc.select("table tfoot tr td").get(0).text().split("\u00a0");
				LocalDateTime tableTime = null;
				if(doc.select("table tfoot tr td").get(0).text().length() > 40){
					//tableTime = formatter.parse(doc.select("table tfoot tr td").get(0).text().substring(20, 40));
					tableTime = LocalDateTime.parse(doc.select("table tfoot tr td").get(0).text().substring(20, 40), dtformatter);
				}else{
					//tableTime = formatter.parse(doc.select("table tfoot tr td").get(0).text().substring(0, 20));
					tableTime = LocalDateTime.parse(doc.select("table tfoot tr td").get(0).text().substring(0, 20), dtformatter);
				}
				//OffsetDateTime tableTime = OffsetDateTime.of(tt, ZoneOffset.of("+2"));
				doc.select("table").remove();
				Elements links = doc.select("div#sadrzaj div").get(0).select("a[href]");
							

				boolean isTableTime = fiveDayForecastRepository.existsByActiveAndTableTime(true, tableTime);
				if(!isTableTime){


						mvcConfig.fivedayForecastCacheEvict();


                    Calendar calendar = new GregorianCalendar();
                    //	Date d = calendar.getTime();

                    LocalDate previousDay = LocalDate.now().minusDays(1);
         //           List<ForecastDate> forecastDates = forecastDateRepository.findByForecastDateAfter(LocalDate.from(previousDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
					List<ForecastDate> forecastDates = forecastDateRepository.findByForecastDateAfter(previousDay);
					//formatter = new SimpleDateFormat("dd.MM.yyyy");
					dtformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
/*

					List<FivedayForecast> listFiveDayForecastModel = fiveDayForecastRepository.findByActive(BigInteger.ONE);
					
					for(FivedayForecast fiveDayForecastModel : listFiveDayForecastModel){
						fiveDayForecastModel.setActive(BigInteger.ZERO);
						
					}

					fiveDayForecastRepository.saveAll(listFiveDayForecastModel);
*/

					fiveDayForecastRepository.updateFivedayForecastActiveToFalse();

					List<FivedayForecast> listFiveDayForecast = new ArrayList<FivedayForecast>();
					List<City> cities = (List<City>) cityRepository.findAll();
                    List<Description> currentDescriptions = (List<Description>) descriptionRepository.findAll();
					for(Element href : links){
						
						String hcity = href.text();
						City city = null;

						try {
							city = cities.stream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
						}catch(NoSuchElementException e){
							city = new City();
							city.setCityName(hcity);
							cityRepository.save(city);
							cities.add(city);
						}

						//StringBuilder sb = new StringBuilder();
						//sb.append("http://www.hidmet.gov.rs/latin/prognoza/");
						//sb.append(href.attr("href"));
												
						try {
							Document.OutputSettings settings = new Document.OutputSettings();
							settings.escapeMode(Entities.EscapeMode.xhtml);

							doc = Jsoup.connect(foreacasURL+"prognoza/"+href.attr("href")).execute().parse();
							doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

							Elements tbodyRowsMaxTemp = doc.select("div#sadrzaj div table  tbody tr").get(1).select("td");
	  						Elements tbodyRowsMinTemp = doc.select("div#sadrzaj div table  tbody tr").get(3).select("td");
	  						Elements tbodyRowsImage = doc.select("div#sadrzaj div table  tbody tr").get(5).select("td");
	  						Elements theadRows = doc.select("div#sadrzaj div table  thead").get(0).select("tr th");
	  						Elements theadImages = doc.select("div#sadrzaj div table").get(1).select("tbody tr td");
	  							  						

							int cnt = descriptionRepository.countByDescriptionNotNull();
  							if((theadImages.size() / 2 ) - 1 != cnt) {
								mvcConfig.descriptionCacheEvict();
  								populateDescriptionAndImageForForecast();
  								currentDescriptions = (List<Description>) descriptionRepository.findAll();
  							}

	  						for(int i=2; i<theadRows.size()-1; i++){
	  							String[] stringForecastTimestamp = theadRows.get(i).text().split(" ");
	  							StringBuilder sbu = new StringBuilder();
	  							sbu.append(stringForecastTimestamp[1]);
	  							sbu.append(calendar.get(Calendar.YEAR));
	  							//Date parseForecastDate = formatter.parse(sbu.toString());
                                LocalDate parseForecastDate = LocalDate.parse(sbu.toString(), dtformatter);
                                ForecastDate forecastDate;// = null;
                                try {
                                    forecastDate = forecastDates.stream().filter(p -> p.getForecastDate().equals(parseForecastDate)).findFirst().get();

                                    //ForecastDate forecastDate = forecastDateRepository.findByForecastDate(parseForecastDate);
                                }catch(NoSuchElementException e){

	  								forecastDate = new ForecastDate();
                                    forecastDate.setForecastDate(parseForecastDate);
	  								forecastDateRepository.save(forecastDate);
									forecastDates.add(forecastDate);
	  								//forecastDate = forecastDateRepository.findByForecastDate(parseForecastDate);
	  							}

								FivedayForecast fiveDayForecastModel = new FivedayForecast();
	  							fiveDayForecastModel.setCity(city);
	  							fiveDayForecastModel.setForecastDate(forecastDate);
	  							
	  							if(!tbodyRowsMinTemp.get(i).text().equals("-")){
	  								fiveDayForecastModel.setMinTemperature(new Integer(tbodyRowsMinTemp.get(i).text()));
	  							}
	  							fiveDayForecastModel.setMaxTemperature(new Integer(tbodyRowsMaxTemp.get(i).text()));
	  							fiveDayForecastModel.setActive(true);

	  							String img = tbodyRowsImage.get(i).select("img").attr("src");
	  							Description image = currentDescriptions.stream().filter(p -> p.getImageLocation().equals(img)).findFirst().get();
	  									

	  							fiveDayForecastModel.setDescription(image);
	  							fiveDayForecastModel.setTableTime(tableTime);
	  							
	  							listFiveDayForecast.add(fiveDayForecastModel);

	  						}
	  						
						}catch (IOException  e) {
						    System.out.println(LocalDateTime.now());
							e.printStackTrace();							
						}	
						
						
					}
					/** all in one update */
					fiveDayForecastRepository.saveAll(listFiveDayForecast);

					fivedayForecastPublisher.publish(listFiveDayForecast);
				}else{

				}
			} catch (IOException e) {
                System.out.println(LocalDateTime.now());
				e.printStackTrace();
			}

    }
	
	@Scheduled(cron = cronCurrentForecast)
	public void populateCurrentForecast() {

		Document doc;

		//SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
			try {
				doc = Jsoup.connect(foreacasURL + "osmotreni/index.php").get();
				doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

				Elements tbodyRows = doc.select("table tbody").get(0).select("tr");

				LocalDateTime tableTime = LocalDateTime.parse(doc.select("table tfoot tr td").get(0).text().substring(18, 34), formatter);
				//OffsetDateTime tableTime = OffsetDateTime.of(tt, ZoneOffset.of("+2"));
				boolean isTableTime = currentForecastRepository.existsByActiveAndTableTime(true, tableTime);
				if(!isTableTime){
					//clear cache
					mvcConfig.currentForecastCacheEvict();

					currentForecastRepository.updateCurrentForecastActiveToFalse();
					List<CurrentForecast> listCurrentForecastsModel = new ArrayList<CurrentForecast>();
					List<City> cities = (List<City>) cityRepository.findAll();
					List<Description> descriptions = (List<Description>) descriptionRepository.findAll();
 					for (int i = 0; i < tbodyRows.size(); i++) {
						
						Elements tdRows = tbodyRows.get(i).select("tr").select("td");
						String hcity = tdRows.get(0).text().replaceAll("&nbsp; ", "");
						City city = null;
						Description description = null;

						try {
							city = cities.stream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
						}catch(NoSuchElementException e){
							city = new City();
							city.setCityName(hcity);
							cityRepository.save(city);
							//cities.add(city);
							cities = (List<City>) cityRepository.findAll();
						}
						if(tdRows.size() == 9){
							CurrentForecast currentForecastModel = new CurrentForecast();
							currentForecastModel.setCity(city);
							
							currentForecastModel.setTemperature(new Integer(tdRows.get(1).text().replaceAll("[^\\d]", "" )));//.trim().replaceAll("&nbsp; ", "")));
							currentForecastModel.setPressure(new Float(tdRows.get(2).text().replaceAll("[^\\d.]", "" )));//.trim().replaceAll("&nbsp; ", "")));
							currentForecastModel.setWindDirection(tdRows.get(3).text().trim().replaceAll("&nbsp; ", ""));
							currentForecastModel.setWindSpeed(tdRows.get(4).text().trim().replaceAll("&nbsp; ", ""));
							currentForecastModel.setHumidity(new Integer(tdRows.get(5).text().replaceAll("[^\\d]", "" )));//.trim().replaceAll("&nbsp; ", "")));
							currentForecastModel.setFeelsLike(new Integer(tdRows.get(6).text().replaceAll("[^\\d]", "" )));//.trim().replaceAll("&nbsp; ", "")));
							//currentForecastModel.setImage(tdRows.get(7).select("img").attr("src"));
							String img = tdRows.get(7).select("img").attr("src");
  							//Description image = descriptionRepository.findByImageLocation(img);

							try{
  								description = descriptions.stream().filter(p -> p.getImageLocation().equals(img)).findFirst().get();

							}catch(NoSuchElementException e){
  								Description descriptionModel = new Description();
								descriptionModel.setImageLocation(img);
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
					currentForecastRepository.saveAll(listCurrentForecastsModel);
					currentForecastPublisher.publish(listCurrentForecastsModel);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(LocalDateTime.now());
			}
					
	}
	
	@Scheduled(cron = cronShortermForecast)
	public void populateShortTermForecast() {
		Document doc;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
			try {

				doc = Jsoup.connect(foreacasURL+"prognoza/index.php").get();
				doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);


				Element table = doc.select("table").get(0); // select the first table.
				Element thead = table.select("thead tr").get(0);
				Element tfoot = table.select("tfoot tr td").get(0);
				Element tbody = table.select("tbody").get(0);
				LocalDateTime tableTime = LocalDateTime.parse(tfoot.text().substring(20, 40), formatter);
				Map<Integer, LocalDate> listStringDates = new HashMap<>();
				boolean isTableTyme = shortTermForecastRepository.existsByActiveAndTableTime(true, tableTime);
				if(!isTableTyme){
					//clear cache
					mvcConfig.shortTearmForecastCacheEvict();

					List<Description> currentDescriptions = (List<Description>) descriptionRepository.findAll();


					List<ShortTermForecast> listShortTermForecast = new ArrayList<>();//shortTermForecastRepository.findByActive(BigInteger.ONE);

                    LocalDate previousDay = LocalDate.now().minusDays(1);
                    //List<ForecastDate> forecastDates = forecastDateRepository.findByForecastDateAfter(LocalDate.from(previousDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
					List<ForecastDate> forecastDates = forecastDateRepository.findByForecastDateAfter(previousDay);

					/*for(ShortTermForecast shortTermForecast : listShortTermForecast ){
						shortTermForecast.setActive(false);
					}	*/

					shortTermForecastRepository.updateShortTermForecastActiveToFalse();

					Elements theadRows = thead.select("th");

					for (int i = 1; i < theadRows.size(); i++) { // first row is  the col names so skip it.

						Element th = theadRows.get(i);
				//		String[] stringDate = th.text().split("\u00a0\u00a0");

						//formatter = new SimpleDateFormat("dd.MM.yyyy.");
                        DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern(" dd.MM.yyyy.");
						//Date parseForecastDate = formatter.parse(th.text().substring(th.text().lastIndexOf(' ')));
                        LocalDate parseForecastDate = LocalDate.parse(th.text().substring(th.text().lastIndexOf(' ')), dtformatter);
                        listStringDates.put(i, parseForecastDate);
                        ForecastDate forecastDate;// = null;
                        try {
                            //forecastDate = forecastDateRepository.findByForecastDate(parseForecastDate);
                            forecastDate = forecastDates.stream().filter(p -> p.getForecastDate().equals(parseForecastDate)).findFirst().get();
                        }catch(NoSuchElementException e){
							//ForecastDate
                            forecastDate = new ForecastDate();
							forecastDate.setForecastDate(parseForecastDate);
							forecastDateRepository.save(forecastDate);
							forecastDates.add(forecastDate);
							//forecastDate = forecastDateModel;//forecastDateRepository.findByForecastDate(parseForecastDate);
						}
					}
					
					Elements tbodyRows = tbody.select("tr");

                    List<City> cities = (List<City>) cityRepository.findAll();

					for (int i = 0; i < tbodyRows.size(); i++) {

						Element tr = tbodyRows.get(i);
						Elements tdRows = tr.select("td");

						int tdSize = tdRows.size();

						Integer minTemp = null;
						Integer maxTemp = null;
						City city = null;


						for (int j = 0; j < tdSize; j++) {
							Element td = tdRows.get(j);

							if (j == 0) {
								String hcity = td.text();


								try{
									city = cities.stream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
								}catch(NoSuchElementException e){
									city = new City();
									city.setCityName(hcity);
									cityRepository.save(city);
									cities.add(city);
								}

							} else {
								switch (j % 3) {

								case 0:
									minTemp = new Integer(td.html());
									break;
								case 1:
									maxTemp = new Integer(td.html());
									break;
								case 2:
									//ForecastDate forecastDate = forecastDateRepository.findByForecastDate(listStringDates.get(j / 3 + 1));
									ForecastDate forecastDate;// = null;
									LocalDate parseForecastDate = listStringDates.get(j / 3 + 1);
									try {
										//forecastDate = forecastDateRepository.findByForecastDate(parseForecastDate);
										forecastDate = forecastDates.stream().filter(p -> p.getForecastDate().equals(parseForecastDate)).findFirst().get();
									}catch(NoSuchElementException e){
										//ForecastDate
										forecastDate = new ForecastDate();
										forecastDate.setForecastDate(parseForecastDate);
										forecastDateRepository.save(forecastDate);
										forecastDates.add(forecastDate);
										//forecastDate = forecastDateModel;//forecastDateRepository.findByForecastDate(parseForecastDate);
									}

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
									
									if(!currentDescriptions.stream().map(Description::getImageLocation).filter(img::equals).findFirst().isPresent()) {
										mvcConfig.descriptionCacheEvict();

										populateDescriptionAndImageForForecast();
										currentDescriptions = (List<Description>) descriptionRepository.findAll();
									}
									Description image = currentDescriptions.stream().filter(p -> p.getImageLocation().equals(img)).findFirst().get();

		  							shortTermForecastModel.setDescription(image);
									listShortTermForecast.add(shortTermForecastModel);
									break;

								}
							}
						}
					}
					shortTermForecastRepository.saveAll(listShortTermForecast);

					shortTermForecastPublisher.publish(listShortTermForecast);
				}
			}catch (IOException e) {

				e.printStackTrace();

		}
		
	}
	@Scheduled(cron = cronAirQuality)
	public void populateAirQuality() {

		Document doc;
		Station station = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

		try {

			doc = Jsoup.connect(airQualityURL + "stanicepodaci.php").execute().parse();
			Elements tbodyStations = doc.select("table").get(0).select("tbody tr");
			String time = doc.select("body > div.admin-wrapper > div.admin-content > div > div.admin-content-header > div > div > div").select("a[href]").get(0).text().substring(21);
			LocalDateTime tableTime = LocalDateTime.parse(time, formatter);
			//boolean isTableTyme = shortTermForecastRepository.existsByActiveAndTableTime(BigInteger.ONE, tableTime);
			boolean isTableTime = airQualityRepository.existsByActiveAndTableTime(true, tableTime);
			if (!isTableTime) {

			  //  populateStations();

				List<Station> stations = (List<Station>) stationRepository.findAll();
				airQualityRepository.updateAirQualitySetActiveToFalse();
				List<AirQuality> listOfAirQuality = new ArrayList<AirQuality>();
				for (Element trStation : tbodyStations) {
					Elements tdStation = trStation.select("td");
//				station = stations.stream().filter(e -> (e.getCity().getCityName() + ' ' +e.getStationName()).equals(trStation.get(0).text())).findAny().get();
					try {
						station = stations.parallelStream().filter(x -> {
							if (x.getStationName().equals("")) {
								if(tdStation.get(0).text().indexOf("-") != -1){
									if (x.getCity().getCityName().equals(tdStation.get(0).text().substring(0, tdStation.get(0).text().indexOf("-")))) {
										return true;
									}
								}else{
									if (x.getCity().getCityName().equals(tdStation.get(0).text())) {
										return true;
									}
								}
							} else {
								if(tdStation.get(0).text().indexOf("-") != -1)
								{
									if ((x.getCity().getCityName() + ' ' + x.getStationName()).equals(tdStation.get(0).text().substring(0, tdStation.get(0).text().indexOf("-")))) {
										return true;
									}
								}else{
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
					airQuality.setStation(station);
					airQuality.setSulfurDioxide(tdStation.get(2).text().equals("") || tdStation.get(2).text().equals("X") ? null : Float.valueOf(tdStation.get(2).text()));
					airQuality.setNitrogenDioxide(tdStation.get(3).text().equals("") || tdStation.get(3).text().equals("X") ? null : Float.valueOf(tdStation.get(3).text()));
					airQuality.setMonoNitrogenOxides(tdStation.get(4).text().equals("") || tdStation.get(4).text().equals("X") ? null : Float.valueOf(tdStation.get(4).text()));
					airQuality.setNitrogenOxide(tdStation.get(5).text().equals("") || tdStation.get(5).text().equals("X") ? null : Float.valueOf(tdStation.get(5).text()));
					airQuality.setParticleTenMicrometerPerDay(tdStation.get(6).text().equals("") || tdStation.get(6).text().equals("X") ? null : Float.valueOf(tdStation.get(6).text()));
					airQuality.setParticleTenMicrometerPerHour(tdStation.get(7).text().equals("") || tdStation.get(7).text().equals("X") ? null : Float.valueOf(tdStation.get(7).text()));
					airQuality.setParticleTwoAndAHalfMicrometerPerDay(tdStation.get(8).text().equals("") || tdStation.get(8).text().equals("X") ? null : Float.valueOf(tdStation.get(8).text()));
					airQuality.setCarbonOxide(tdStation.get(9).text().equals("") || tdStation.get(9).text().equals("X") ? null : Float.valueOf(tdStation.get(9).text()));
					airQuality.setOzon(tdStation.get(10).text().equals("") || tdStation.get(10).text().equals("X") ? null : Float.valueOf(tdStation.get(10).text()));
					airQuality.setBenzen(tdStation.get(11).text().equals("") || tdStation.get(11).text().equals("X") ? null : Float.valueOf(tdStation.get(11).text()));
					airQuality.setDd(tdStation.get(12).text().equals("") || tdStation.get(12).text().equals("X") ? null : Float.valueOf(tdStation.get(12).text()));
					airQuality.setSpeed(tdStation.get(13).text().equals("") || tdStation.get(13).text().equals("X") ? null : Float.valueOf(tdStation.get(13).text()));
					airQuality.setTemperature(tdStation.get(14).text().equals("") || tdStation.get(14).text().equals("X") ? null : Float.valueOf(tdStation.get(14).text()));
					airQuality.setTableTime(tableTime);
					airQuality.setActive(true);
					listOfAirQuality.add(airQuality);

				}
				airQualityRepository.saveAll(listOfAirQuality);
				airQualityPublisher.publish(listOfAirQuality);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateStations() {
		Document doc;
		try {
			doc = Jsoup.connect(airQualityURL+"pregledstanica.php").execute().parse();
			Elements tbodyStationsUrl = doc.select("table").get(0).select("tbody tr").select("a[href]");

		//	long val = stationRepository.count();
		//    if(tbodyStationsUrl.size() != val) {
				List<Station> stations = (List<Station>) stationRepository.findAll();
				List<City> cities = (List<City>) cityRepository.findAll();
				for (Element tdStation : tbodyStationsUrl) {

					doc = Jsoup.connect(airQualityURL + tdStation.attr("href")).execute().parse();
					Elements tbodyStations = doc.select("table").get(0).select("tbody tr");

					String hcity = tbodyStations.get(1).select("td").get(1).text();
					String hstationNameTmp = tbodyStations.get(0).select("td").get(1).text().substring(hcity.length()).trim();
					String eoiCode = tbodyStations.get(4).select("td").get(1).text();

					if(hstationNameTmp.indexOf("-")!= -1){
						hstationNameTmp = hstationNameTmp.substring(0, hstationNameTmp.indexOf("-"));
					}
					String hstationName = hstationNameTmp;
					City city = null;
					Station station = null;
					try {
						city = cities.parallelStream().filter(p -> p.getCityName().equals(hcity)).findFirst().get();
					} catch (NoSuchElementException e) {
						city = new City();
						city.setCityName(hcity);
						cityRepository.save(city);
						cities.add(city);
					}
					try {
	//					station = stations.parallelStream().filter(e -> e.getCity().getCityName().equals(hcity) && e.getStationName().equals(hstationName)).findAny().get();
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

	private void populateDescriptionAndImageForForecast() {
		Document doc;

			try {

				mvcConfig.descriptionCacheEvict();

				doc = Jsoup.connect(foreacasURL + "prognoza/stanica.php").execute().parse();
				Elements theadImages = doc.select("div#sadrzaj div table").get(1).select("tbody tr td");
				//List<Description> imageModelList = new ArrayList<Description>();
				List<Description> currentDescriptions = (List<Description>) descriptionRepository.findAll();
					for(int j=0; j<theadImages.size(); j=j+2) {
						if (!theadImages.get(j).select("img").attr("src").equals("")) {
							if (!currentDescriptions.stream().map(Description::getImageLocation).filter(theadImages.get(j).select("img").attr("src")::equals).findFirst().isPresent()) {
								Description imageModel = new Description();
								imageModel.setImageLocation(theadImages.get(j).select("img").attr("src"));
								imageModel.setDescription(theadImages.get(j).select("img").attr("alt"));
								currentDescriptions.add(imageModel);
							}
						}
					}
					descriptionRepository.saveAll(currentDescriptions);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
			
		
	}
}
