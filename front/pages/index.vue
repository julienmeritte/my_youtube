<template>
  <div>
    <div>
        <Header />
    </div>
    <div>
    <div class="container">

      <div class="row">
        <div class="card w-25 h-25 m-2" v-for="videoList in videoList" :key="videoList.id">
          <img :src="`${videoList.videoImg}`">
          <p class="card-text" v-on:click="videoRedirect(videoList.videoId)">{{videoList.videoName}}</p>
        </div>
      </div>

    </div>
  </div>
    </div>


</template>

<script>
export default {
  data() {
    return {
      apiUrl: process.env.apiUrl,
      videoList: [],
    }
  },
  async mounted(){
    let json = [];
      try {
        const response = await this.$axios.$get(this.apiUrl + '/videos?perPage=100');
        response.data.forEach(element => {
            json ["videoName"] = element.name;
            json ["videoImg"] = this.apiUrl + '/videos/base' + element.name + '.mp4';
            json ["videoId"] = element.id;
            this.videoList.push(json);
            json = [];
        });
      } catch (error) {
        
      }
  },
  methods: {
      videoRedirect(id) {
        sessionStorage.setItem("idVideo" , id);
        this.$router.push({path: "/video"});
      }
  }
}
</script>
