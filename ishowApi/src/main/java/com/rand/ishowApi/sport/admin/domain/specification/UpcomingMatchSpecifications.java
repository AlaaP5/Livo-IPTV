package com.rand.ishowApi.sport.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public class UpcomingMatchSpecifications {

    public static UpcomingMatchSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }

    public static UpcomingMatchSpecification hasHomeTeamId(Long id){
        return () -> Criteria.where("homeTeam.id").is(id);
    }

    public static UpcomingMatchSpecification hasAwayTeamId(Long id){
        return () -> Criteria.where("awayTeam.id").is(id);
    }

    public static UpcomingMatchSpecification hasChampionId(Long id){
        return () -> Criteria.where("champion.id").is(id);
    }

    public static UpcomingMatchSpecification nameLike(String search){
        return () -> {
            String regex = ".*" + search + ".*";
            Criteria c1 = Criteria.where("homeTeam.nameEn").regex(regex, "i");
            Criteria c2 = Criteria.where("homeTeam.nameAr").regex(regex, "i");
            Criteria c3 = Criteria.where("awayTeam.nameEn").regex(regex, "i");
            Criteria c4 = Criteria.where("awayTeam.nameAr").regex(regex, "i");
            Criteria c5 = Criteria.where("champion.nameEn").regex(regex, "i");
            Criteria c6 = Criteria.where("champion.nameAr").regex(regex, "i");
            return new Criteria().orOperator(c1,c2,c3,c4,c5,c6);
        };
    }

    // created or updated by (if stored as strings)
    public static UpcomingMatchSpecification createdOrUpdatedBy(String accountId){
        return () -> new Criteria().orOperator(
                Criteria.where("createdBy").is(accountId),
                Criteria.where("updatedBy").is(accountId)
        );
    }
}

