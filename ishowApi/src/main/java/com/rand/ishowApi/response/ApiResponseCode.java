package com.rand.ishowApi.response;

public class ApiResponseCode {

    public static final String VERSION_NOT_FOUND = "version.not.found";
    public static final String VERSION_NOT_VALID = "version.not.valid";

    /* =====================
   SUCCESS CODES
   ===================== */
    public static final String SUCCESS_200 = "SUCCESS_200";

    public static final String GENERAL_ERROR = "error.general";
    /* =====================
       ERROR CODES
       ===================== */
    public static final String ERROR_400 = "ERROR_400";
    public static final String ERROR_401 = "ERROR_401";
    public static final String ERROR_403 = "ERROR_403";
    public static final String ERROR_404 = "ERROR_404";
    public static final String ERROR_500 = "ERROR_500";


    /* =====================
   FILE ERROR CODES
   ===================== */
    public static final String ERROR_FILE_EMPTY = "ERROR_FILE_EMPTY";
    public static final String ERROR_FILE_INVALID_TYPE = "ERROR_FILE_INVALID_TYPE";
    public static final String ERROR_FILE_UPLOAD = "ERROR_FILE_UPLOAD";
    public static final String ERROR_FILE_TOO_LARGE = "ERROR_FILE_TOO_LARGE";
    public static final String ERROR_FILE_NOT_FOUND = "ERROR_FILE_NOT_FOUND";
    public static final String ERROR_FILE_READ = "ERROR_FILE_READ";
    public static final String ERROR_IMAGE_INVALID_RATIO = "ERROR_IMAGE_INVALID_RATIO";

    /* =====================
       AUTH ERROR
       ===================== */
    public static final String AUTH_LOGIN_FAILED = "auth.login.failed";
    public static final String AUTH_REFRESH_FAILED = "auth.refresh.failed";
    public static final String AUTH_LOGOUT_FAILED = "auth.logout.failed";


    /* =====================
       ACCOUNT ERROR
       ===================== */
    public static final String ACCOUNT_NOT_FOUND = "account.not.found";
    public static final String ACCOUNT_DELETED = "account.deleted";
    public static final String ACCOUNT_ALREADY_DELETED = "account.already.deleted";
    public static final String ACCOUNT_BANNED = "account.banned";
    public static final String ACCOUNT_VERIFICATION_REQUIRED = "account.verification.required";
    public static final String ACCOUNT_GSM_EXISTS = "account.gsm.exists";
    public static final String ACCOUNT_OTP_INVALID = "account.otp.invalid";



    /* =====================

   ===================== */
    public static final String DURATION_INVALID = "tag.title.exists";

    /* =====================
       TAG ERROR
       ===================== */
    public static final String TAG_TITLE_EXISTS = "tag.title.exists";
    public static final String TAG_NOT_EXISTS = "tag.not.exists";


    /* =====================
     Sections ERROR
     ===================== */
    public static final String SECTION_TITLE_EXISTS = "section.title.exists";
    public static final String SECTION_NOT_EXISTS = "section.not.exists";
    public static final String SECTION_MUST_ACTIVE = "section.must.active";


    /* =====================
       ACTOR ERROR
       ===================== */
    public static final String ACTOR_NOT_EXISTS = "actor.not.exists";
    public static final String ACTOR_NAME_EXISTS = "actor.name.exists";


    /* =====================
       TEAM ERROR
       ===================== */
    public static final String TEAM_NOT_EXISTS = "team.not.exists";
    public static final String TEAM_NAME_EXISTS = "team.name.exists";


    /* =====================
       CHAMPION ERROR
       ===================== */
    public static final String CHAMPION_NOT_EXISTS = "champion.not.exists";
    public static final String CHAMPION_NAME_EXISTS = "champion.name.exists";


    /* =====================
       Ad ERROR
       ===================== */
    public static final String AD_INVALID_DATE_RANGE = "ad.invalid.date.range";
    public static final String AD_NOT_EXISTS = "ad.not.exists";


    /* =====================
       STORAGE ERROR
       ===================== */
    public static final String STORAGE_MOVIE_ID_REQUIRED = "storage.movie.id.required";
    public static final String STORAGE_SERIES_ID_REQUIRED = "storage.series.id.required";
    public static final String STORAGE_SEASON_NUMBER_REQUIRED = "storage.season.number.required";
    public static final String STORAGE_EPISODE_NUMBER_REQUIRED = "storage.episode.number.required";
    public static final String STORAGE_TV_PROGRAM_ID_REQUIRED = "storage.tv.program.id.required";
    public static final String STORAGE_CHANNEL_ID_REQUIRED = "storage.channel.id.required";
    public static final String STORAGE_CLIP_ID_REQUIRED = "storage.clip.id.required";
    public static final String STORAGE_ACTOR_ID_REQUIRED = "storage.actor.id.required";
    public static final String STORAGE_AD_ID_REQUIRED = "storage.ad.id.required";
    public static final String STORAGE_TEAM_ID_REQUIRED = "storage.team.id.required";
    public static final String STORAGE_CHAMPION_ID_REQUIRED = "storage.champion.id.required";
    public static final String STORAGE_CONTENT_TYPE_REQUIRED = "storage.content.type.required";
    public static final String STORAGE_CONTENT_IDS_REQUIRED = "storage.content.ids.required";
    public static final String STORAGE_FILE_CATEGORY_REQUIRED = "storage.file.category.required";
    public static final String INVALID_FILE_EXTENSION = "invalid.file.extension";
    public static final String STORAGE_FILE_REMOVE_FAILED = "storage.file.remove.failed";
    public static final String STORAGE_ARCHIVE_TYPE_INVALID = "storage.archive.type.invalid";
    public static final String STORAGE_ARCHIVE_EMPTY = "storage.archive.empty";
    public static final String STORAGE_ARCHIVE_NO_IMAGES = "storage.archive.no.images";
    public static final String STORAGE_ARCHIVE_READ_FAILED = "storage.archive.read.failed";

    /* =====================
       WATCHLIST ERROR
       ===================== */
    public static final String WATCHLIST_NOT_FOUND = "watchlist.not.found";

    /* =====================
       FAVORITE ERROR
       ===================== */
    public static final String FAVORITE_NOT_FOUND = "favorite.not.found";

    /* =====================
       CHANNEL ERROR
       ===================== */
    public static final String CHANNEL_NOT_FOUND = "channel.not.found";
    public static final String CHANNEL_SECTION_INDEX_UPDATE_FAILED = "channel.section.index.update.failed";
    public static final String CHANNEL_SEARCH_INDEX_UPDATE_FAILED = "channel.search.index.update.failed";
    public static final String CHANNEL_DELETE_FROM_SECTION_FAILED = "channel.delete.from.section.failed";

    /* =====================
       MOVIE ERROR
       ===================== */
    public static final String MOVIE_NOT_FOUND = "movie.not.found";
    public static final String MOVIE_TRANSCODE_NOT_COMPLETE = "movie.transcode.not.complete";
    public static final String MOVIE_SECTION_INDEX_UPDATE_FAILED = "movie.section.index.update.failed";
    public static final String MOVIE_SEARCH_INDEX_UPDATE_FAILED = "movie.search.index.update.failed";
    public static final String MOVIE_DELETE_FROM_SECTION_FAILED = "movie.delete.from.section.failed";
    public static final String MOVIE_NOT_PUBLISH="movie.not.publish";
    /* =====================
       CLIP ERROR
       ===================== */
    public static final String CLIP_NOT_FOUND = "clip.not.found";
    public static final String CLIP_TRANSCODE_NOT_COMPLETE = "clip.transcode.not.complete";
    public static final String CLIP_NOT_PUBLISH = "clip.not.publish";
    public static final String CLIP_SECTION_INDEX_UPDATE_FAILED = "clip.section.index.update.failed";
    public static final String CLIP_SEARCH_INDEX_UPDATE_FAILED = "clip.search.index.update.failed";
    public static final String CLIP_DELETE_FROM_SECTION_FAILED = "clip.delete.from.section.failed";

    /* =====================
       SERIES ERROR
       ===================== */
     public static final String SERIES_NOT_FOUND = "series.not.found";
     public static final String SERIES_SEASON_NOT_FOUND = "series.session.not.found";
     public static final String SERIES_EPISODE_NOT_FOUND = "series.episode.not.found";
      public static final String SERIES_EPISODE_TRANSCODE_NOT_COMPLETE = "series.episode.transcode.not.complete";
      public static final String SERIES_EPISODE_NOT_PUBLISH = "series.episode.not.publish";
     public static final String SERIES_SEARCH_INDEX_NOT_FOUND = "series.search.index.not.found";
      public static final String SERIES_SECTION_INDEX_UPDATE_FAILED = "series.section.index.update.failed";
      public static final String SERIES_DELETE_FROM_SECTION_FAILED = "series.delete.from.section.failed";
      public static final String SERIES_IS_NOT_PUBLISH = "series.is.not.publish";

     /* =====================
        TV PROGRAM ERROR
        ===================== */
    public static final String TV_PROGRAM_NOT_FOUND = "tv.program.not.found";
    public static final String TV_PROGRAM_SEASON_NOT_FOUND = "tv.program.session.not.found";
    public static final String TV_PROGRAM_EPISODE_NOT_FOUND = "tv.program.episode.not.found";
    public static final String TV_PROGRAM_EPISODE_TRANSCODE_NOT_COMPLETE = "tv.program.episode.transcode.not.complete";
    public static final String TV_PROGRAM_SEARCH_INDEX_NOT_FOUND = "tv.program.search.index.not.found";
    public static final String TV_PROGRAM_SECTION_INDEX_UPDATE_FAILED = "tv.program.section.index.update.failed";
    public static final String TV_PROGRAM_DELETE_FROM_SECTION_FAILED = "tv.program.delete.from.section.failed";
    public static final String TV_PROGRAM_IS_NOT_PUBLISH = "tv.program.is.not.publish";

}
