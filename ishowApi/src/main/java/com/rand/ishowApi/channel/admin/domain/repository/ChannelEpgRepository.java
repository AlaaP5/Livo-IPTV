package com.rand.ishowApi.channel.admin.domain.repository;

import com.rand.ishowApi.channel.admin.domain.entity.ChannelEpg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChannelEpgRepository extends MongoRepository<ChannelEpg, String> {

	// Existing: all active EPG ordered by start date
	List<ChannelEpg> findByChannelIdAndActiveTrueOrderByStartDateAsc(String channelId);

	// Current active EPG item
	@Query("""
        {
            'channelId': ?0,
            'active': true,
            'startDate': { $lte: ?1 },
            'endDate': { $gte: ?1 }
        }
    """)
	Optional<ChannelEpg> findCurrentActiveProgram(
			String channelId,
			LocalDateTime currentDateTime
	);

	// Active EPG list from -12h to +12h around current time
	@Query("""
        {
            'channelId': ?0,
            'active': true,
            'startDate': { $lte: ?2 },
            'endDate': { $gte: ?1 }
        }
    """)
	List<ChannelEpg> findActiveProgramsInRange(
			String channelId,
			LocalDateTime fromDate,
			LocalDateTime toDate
	);
}