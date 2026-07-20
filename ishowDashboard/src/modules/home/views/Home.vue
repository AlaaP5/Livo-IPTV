<template>
  <v-app>
    <v-main>
      <v-container>
        <v-row justify="center">
          <v-col cols="12" style="max-width: 900px">
            <div class="title">Home Page</div>
          </v-col>
        </v-row>

        <!-- Grid of Cards -->
        <v-row class="cards-grid" justify="center">


          <!-- Account Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="account"
                cover
              >
                <v-card-title class="titleStyle">Account</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Account Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="account/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Tag Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="tag"
                cover
              >
                <v-card-title class="titleStyle">Tag</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Tag Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="tag/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Ad Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="ad"
                cover
              >
                <v-card-title class="titleStyle">Ad</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Ad Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="ad/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Actor Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="actor"
                cover
              >
                <v-card-title class="titleStyle">Actor</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Actor Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="actor/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Section Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="section"
                cover
              >
                <v-card-title class="titleStyle">Section</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Section Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="section/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Channel Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="channel"
                cover
              >
                <v-card-title class="titleStyle">Channel</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Channel Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="channel/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Channel Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="channel"
                cover
              >
                <v-card-title class="titleStyle">Channel Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Channel Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="channel/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Movie Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="movie"
                cover
              >
                <v-card-title class="titleStyle">Movie</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Movie Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="movie/filter"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Movie Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="movie"
                cover
              >
                <v-card-title class="titleStyle">Movie Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Movie Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="movie/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Series Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="series"
                cover
              >
                <v-card-title class="titleStyle">Series</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Series Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="series/filter"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Series Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="series"
                cover
              >
                <v-card-title class="titleStyle">Series Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Series Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="series/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Clip Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="clip"
                cover
              >
                <v-card-title class="titleStyle">Clip</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Clip Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="clip/filter"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Clip Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="clip"
                cover
              >
                <v-card-title class="titleStyle">Clip Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Clip Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="clip/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Tv Program Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="tv"
                cover
              >
                <v-card-title class="titleStyle">Tv Program</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Tv Program Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="tv-program/filter"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Tv Program Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="tv"
                cover
              >
                <v-card-title class="titleStyle">Tv Program Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Tv Program Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="tv-program/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Champion Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="champion"
                cover
              >
                <v-card-title class="titleStyle">Champion</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Champion Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="champion/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Team Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="team"
                cover
              >
                <v-card-title class="titleStyle">Team</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Team Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="team/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Sport Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="team"
                cover
              >
                <v-card-title class="titleStyle">Sport Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Sport Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="sport/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- UpcomingMatch Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="upcoming"
                cover
              >
                <v-card-title class="titleStyle">Upcoming Match</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Upcoming Match Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="upcoming-match/filter"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Complaint Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="complaint"
                cover
              >
                <v-card-title class="titleStyle">Complaint</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Complaint Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="complaint/filter"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- Kids Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="kids"
                cover
              >
                <v-card-title class="titleStyle">Kids Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Kids Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="kids/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>


          <!-- Kids Banners Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="banner"
                cover
              >
                <v-card-title class="titleStyle">Kids Banners</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Kids Banners Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="kids/banner/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>


          <!-- Home Sections Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="section"
                cover
              >
                <v-card-title class="titleStyle">Home Sections</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Home Sections Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="home/sections"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>


          <!-- Home Banners Card -->
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="mx-auto card-item" height="100%">
              <v-img
                class="align-end text-white"
                height="200"
                :src="banner"
                cover
              >
                <v-card-title class="titleStyle">Home Banners</v-card-title>
              </v-img>
              <v-card-subtitle class="pt-4"
                >Home Banners Page</v-card-subtitle
              >
              <v-card-actions>
                <router-link
                  class="text-decoration-none text-dark"
                  to="home/banner/list"
                >
                  <v-btn
                    style="background-color: antiquewhite"
                    color="secondary"
                    block
                    >Move to page</v-btn
                  >
                </router-link>
              </v-card-actions>
            </v-card>
          </v-col>

        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
import account from '@/assets/account.jpg'
import banner from '@/assets/banner.jpg';
import tag from '@/assets/tag.jpg';
import actor from '@/assets/actor.jpg';
import ad from '@/assets/ad.jpg';
import section from '@/assets/section.jpg';
import channel from '@/assets/channel.jpg';
import movie from '@/assets/movie.jpg';
import series from '@/assets/series.jpg';
import clip from '@/assets/clips.jpg';
import tv from '@/assets/tv-program.jpg';
import champion from '@/assets/champion.jpg';
import team from '@/assets/team.jpg';
import upcoming from '@/assets/upcoming-match.jpg';
import kids from '@/assets/kids.jpg';
import complaint from '@/assets/complaint.jpg';
import helpCenter from '@/assets/helpCenter.jpg';
import { useAuthStore } from '../../auth/stores/auth'
import { ref } from 'vue'


const authStore = useAuthStore();

const superAdmin = ref(false);

superAdmin.value = authStore.auth?.role === 'SUPER_ADMIN';

</script>

<style scoped>
.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px 0 0 20px;
}

.card-item {
  width: 300px;
  transition: transform 0.3s ease;
}

.card-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.titleStyle {
  background: rgba(0, 0, 0, 0.5);
  width: 100%;
  padding: 16px;
  color: white;
}

@media (max-width: 600px) {
  .cards-grid {
    grid-template-columns: 1fr;
  }
}
</style>