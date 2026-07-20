<template>
  <v-navigation-drawer
    v-model="drawer"
    app
    color="primary"
    :width="sidebarWidth"
    permanent
    @mouseenter="expandSidebar"
    @mouseleave="collapseSidebar"
  >
    <v-list>
      <v-list-item class="drawer-title">
        <img style="width: 60px" src="/src/assets/rand.jpeg" />
        <div v-if="isSidebarExpanded">iShow Dash</div>
      </v-list-item>

      <router-link :to="ROUTES_ENUM.HOME" class="nav-link">
        <v-list-item>
          <v-icon class="nav-icon">mdi-home</v-icon>
          <span v-if="isSidebarExpanded">Home</span>
        </v-list-item>
      </router-link>

      <v-divider></v-divider>

      <v-list dense>
        <!-- Account -->
        <v-list-group v-if="superAdmin">
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-account-group</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Account</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.ACCOUNT.BASE + ROUTES_ENUM.ACCOUNT.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Account List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Tag -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-tag</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Tag</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.TAG.BASE + ROUTES_ENUM.TAG.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Tag List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Ad -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-advertisements</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Advertisement</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.AD.BASE + ROUTES_ENUM.AD.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Ad List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Actor -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-account-star</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Actor</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.ACTOR.BASE + ROUTES_ENUM.ACTOR.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Actor List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Section -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Section</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.SECTION.BASE + ROUTES_ENUM.SECTION.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Section List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Channel -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-television</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Channel</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Channel List</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <!-- <router-link
            :to="ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.VIEW_SECTION_CHANNELS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-television</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Section Channels</v-list-item-content
              >
            </v-list-item>
          </router-link> -->

          <router-link
            :to="ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.VIEW_CHANNEL_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Channel Sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.VIEW_CHANNEL_BANNER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Channel Banner</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Movie -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-movie</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Movie</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.MOVIE.BASE + ROUTES_ENUM.MOVIE.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Movie List</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.MOVIE.BASE + ROUTES_ENUM.MOVIE.VIEW_MOVIE_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Movie Sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.MOVIE.BASE + ROUTES_ENUM.MOVIE.VIEW_MOVIE_BANNER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Movie Banner</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Series -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-television-guide</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Series</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.SERIES.BASE + ROUTES_ENUM.SERIES.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Series List</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.SERIES.BASE + ROUTES_ENUM.SERIES.VIEW_SERIES_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Series Sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.SERIES.BASE + ROUTES_ENUM.SERIES.VIEW_SERIES_BANNER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Series Banner</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Clip -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-video</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Clip</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.CLIP.BASE + ROUTES_ENUM.CLIP.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Clip List</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.CLIP.BASE + ROUTES_ENUM.CLIP.VIEW_CLIP_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Clip Sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.CLIP.BASE + ROUTES_ENUM.CLIP.VIEW_CLIP_BANNER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Clip Banner</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- TV Program -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-television</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >TV Program</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.TV_PROGRAM.BASE + ROUTES_ENUM.TV_PROGRAM.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >TV Program List</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.TV_PROGRAM.BASE + ROUTES_ENUM.TV_PROGRAM.VIEW_TV_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Tv Program Sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.TV_PROGRAM.BASE + ROUTES_ENUM.TV_PROGRAM.VIEW_TV_BANNER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-view-dashboard</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Tv Program Banner</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Channel Epg -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-television</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Channel Epg</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.CHANNEL_EPG.BASE + ROUTES_ENUM.CHANNEL_EPG.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Channel Epg List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>

        <!-- Home -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-home</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Home</v-list-item-content
              >
            </v-list-item>
          </template>

          <!-- Champion -->
        <!-- <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-movie</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Champion</v-list-item-content
              >
            </v-list-item>
          </template> -->

          <router-link
            :to="ROUTES_ENUM.MOBILE_HOME.BASE + ROUTES_ENUM.MOBILE_HOME.VIEW_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Home sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.MOBILE_HOME.BASE + ROUTES_ENUM.MOBILE_HOME.BANNER + ROUTES_ENUM.MOBILE_HOME.BANNER_LIST"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-alpha-b-box</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Home banners</v-list-item-content
              >
            </v-list-item>
          </router-link>


        <!-- </v-list-group> -->

        </v-list-group>


        <!-- Sport -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-soccer</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Sport</v-list-item-content
              >
            </v-list-item>
          </template>

          <!-- Champion -->
        <!-- <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-movie</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Champion</v-list-item-content
              >
            </v-list-item>
          </template> -->

          <router-link
            :to="ROUTES_ENUM.SPORT.BASE + ROUTES_ENUM.SPORT.VIEW_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Sport sections</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.CHAMPION.BASE + ROUTES_ENUM.CHAMPION.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Champion List</v-list-item-content
              >
            </v-list-item>
          </router-link>


          <router-link
            :to="ROUTES_ENUM.TEAM.BASE + ROUTES_ENUM.TEAM.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-account-group</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Team List</v-list-item-content
              >
            </v-list-item>
          </router-link>

          <router-link
            :to="ROUTES_ENUM.UPCOMING_MATCH.BASE + ROUTES_ENUM.UPCOMING_MATCH.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-calendar-clock</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Upcoming Match List</v-list-item-content
              >
            </v-list-item>
          </router-link>


        <!-- </v-list-group> -->

        </v-list-group>



        <!-- Kids -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-run-fast</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Kids</v-list-item-content
              >
            </v-list-item>
          </template>

          <!-- Champion -->
        <!-- <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-movie</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Champion</v-list-item-content
              >
            </v-list-item>
          </template> -->

          <router-link
            :to="ROUTES_ENUM.KIDS.BASE + ROUTES_ENUM.KIDS.VIEW_SECTIONS"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Kids sections</v-list-item-content
              >
            </v-list-item>
          </router-link>


          <router-link
            :to="ROUTES_ENUM.KIDS.BASE + ROUTES_ENUM.KIDS.BANNER + ROUTES_ENUM.KIDS.BANNER_LIST"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-alpha-b-box</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Kids banners</v-list-item-content
              >
            </v-list-item>
          </router-link>




        <!-- </v-list-group> -->

        </v-list-group>

        <!-- Complaint -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-message-alert</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Complaint</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.COMPLAINT.BASE + ROUTES_ENUM.COMPLAINT.FILTER"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Complaint List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>


        <!-- Help Center -->
        <v-list-group>
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props">
              <v-icon class="nav-icon">mdi-frequently-asked-questions</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Help Center</v-list-item-content
              >
            </v-list-item>
          </template>

          <router-link
            :to="ROUTES_ENUM.HELP_CENTER.BASE"
            class="nav-link"
          >
            <v-list-item>
              <v-icon class="nav-icon">mdi-format-list-bulleted</v-icon>
              <v-list-item-content v-if="isSidebarExpanded"
                >Help Center List</v-list-item-content
              >
            </v-list-item>
          </router-link>
        </v-list-group>



      </v-list>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ROUTES_ENUM } from "../utils/constants";
import { useAuthStore } from "../modules/auth/stores/auth";

const authStore = useAuthStore();

const superAdmin = ref(false);

superAdmin.value = authStore.auth?.role === "SUPER_ADMIN";

const drawer = defineModel<boolean>({ required: true });

const sidebarWidth = ref(140);
const isSidebarExpanded = ref(false);

const expandSidebar = () => {
  if (!isSidebarExpanded.value) {
    sidebarWidth.value += 130;
    isSidebarExpanded.value = true;
  }
};

const collapseSidebar = () => {
  if (isSidebarExpanded.value) {
    sidebarWidth.value -= 130;
    isSidebarExpanded.value = false;
  }
};
</script>

<style scoped>
.drawer-title {
  text-align: center;
  font-weight: bold;
  margin: 20px 10px 10px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-link {
  text-decoration: none;
  color: #fff;
  display: flex;
  align-items: center;
  position: relative;
  padding: 5px 0;
}

.nav-link.expanded {
  justify-content: flex-start;
}

.nav-link:hover {
  font-weight: bold;
}

.nav-link:hover::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 60px;
  width: 55%;
  height: 2px;
  background-color: rgb(209, 205, 205);
  transform: scaleX(1);
  transition: transform 0.5s ease;
}

.nav-link::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 60px;
  height: 2px;
  background-color: rgb(218, 211, 211);
  transform: scaleX(0);
  transition: transform 1s ease;
  transform-origin: bottom left;
}

.nav-icon {
  margin-right: 10px;
  color: #8da0a1;
}

.v-list-group__items .nav-link {
  padding-left: 20px;
}
</style>