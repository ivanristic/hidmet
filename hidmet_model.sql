USE hidmet;

CREATE TABLE `users` (
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into `users`(`username`,`enabled`,`password`) values ('ivan',1,'$2a$10$6TajU85/gVrGUm5fv5Z8beVF37rlENohyLk3BEpZJFi6Av9JNkw9O');

CREATE TABLE `authorities` (
  `authority` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`authority`,`username`),
  KEY `FKhjuy9y4fd8v5m3klig05ktofg` (`username`),
  CONSTRAINT `FKhjuy9y4fd8v5m3klig05ktofg` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into `authorities`(`authority`,`username`) values ('admin','ivan');

CREATE TABLE `persistent_logins` (
  `series` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `last_used` datetime NOT NULL,
  `token` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `forecast_date` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `date` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `description` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `image_location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `short_term_forecast` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bigint(20) NOT NULL,
  `max_temperature` bigint(20) NOT NULL,
  `min_temperature` bigint(20) DEFAULT NULL,
  `table_time` datetime DEFAULT NULL,
  `city_id` bigint(20) NOT NULL,
  `description_id` bigint(20) NOT NULL,
  `date_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`city_id`,`description_id`,`date_id`),
  KEY `FK9sj43st51extc2286o0h45c2m` (`city_id`),
  KEY `FKqacgj34vpc4ds2b3h59p92o9w` (`description_id`),
  KEY `FKhfevxrvmswvccwfyi3klp4mg4` (`date_id`),
  CONSTRAINT `FK9sj43st51extc2286o0h45c2m` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `FKhfevxrvmswvccwfyi3klp4mg4` FOREIGN KEY (`date_id`) REFERENCES `forecast_date` (`id`),
  CONSTRAINT `FKqacgj34vpc4ds2b3h59p92o9w` FOREIGN KEY (`description_id`) REFERENCES `description` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `fiveday_forecast` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bigint(20) NOT NULL,
  `max_temperature` bigint(20) NOT NULL,
  `min_temperature` bigint(20) DEFAULT NULL,
  `table_time` datetime DEFAULT NULL,
  `city_id` bigint(20) NOT NULL,
  `description_id` bigint(20) NOT NULL,
  `date_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`city_id`,`description_id`),
  KEY `FKse3ip3au7n73fo8naeqqgfpu8` (`city_id`),
  KEY `FKhywfp7pcqj3vueaohopvec8ap` (`description_id`),
  KEY `FKly1rp8707m8vd1ve84ff7nfop` (`date_id`),
  CONSTRAINT `FKhywfp7pcqj3vueaohopvec8ap` FOREIGN KEY (`description_id`) REFERENCES `description` (`id`),
  CONSTRAINT `FKly1rp8707m8vd1ve84ff7nfop` FOREIGN KEY (`date_id`) REFERENCES `forecast_date` (`id`),
  CONSTRAINT `FKse3ip3au7n73fo8naeqqgfpu8` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `current_forecast` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bigint(20) NOT NULL,
  `temperature` bigint(20) NOT NULL,
  `feels_like` bigint(20) NOT NULL,
  `humidity` bigint(20) NOT NULL,
  `pressure` decimal(6,2) NOT NULL,
  `table_time` datetime DEFAULT NULL,
  `wind_direction` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `wind_speed` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city_id` bigint(20) NOT NULL,
  `description_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`city_id`,`description_id`),
  KEY `FKqivknah1jseaal8i013b17ecx` (`city_id`),
  KEY `FKnw7hyojn4g234prkqbf2vcxia` (`description_id`),
  CONSTRAINT `FKqivknah1jseaal8i013b17ecx` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `FKnw7hyojn4g234prkqbf2vcxia` FOREIGN KEY (`description_id`) REFERENCES `description` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` int(11) DEFAULT NULL,
  `station_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `network` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `eoi_code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `classification` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqivknah1jsea342al8i013b17ecx` (`city_id`),
  CONSTRAINT `FKnw7hyojdg4n4g234prkqbf2vcxia` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `air_quality` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `station_id` int(11) DEFAULT NULL,
  `so2` decimal(6,2) DEFAULT NULL,
  `no2` decimal(6,2) DEFAULT NULL,
  `nox` decimal(6,2) DEFAULT NULL,
  `no` decimal(6,2) DEFAULT NULL,
  `pm10_24h` decimal(6,2) DEFAULT NULL,
  `pm10_1h` decimal(6,2) DEFAULT NULL,
  `pm25_24h` decimal(6,2) DEFAULT NULL,
  `co` decimal(6,2) DEFAULT NULL,
  `o3` decimal(6,2) DEFAULT NULL,
  `benzen` decimal(6,2) DEFAULT NULL,
  `dd` decimal(6,2) DEFAULT NULL,
  `v` decimal(6,2) DEFAULT NULL,
  `t` decimal(6,2) DEFAULT NULL,
  `table_time` datetime DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKuakzefgaksfd23sadflfkj` (`station_id`),
  CONSTRAINT `FKdt9tynxqhp9bwer8q15n55nx1` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

