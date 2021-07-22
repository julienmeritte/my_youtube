<template>
  <div>
    <div>
      <Header/>
    </div>
    <div class="container-fluid">
      <div>
        <form class="col-xs-4 col-xs-offset-4 p-3">
          <b-form-input size="sm" class="mr-sm-2" placeholder="Search" id="search" @input="startSearching"></b-form-input>
        </form>
      </div>
      <div class="w-100">
        
        <div class="row justify-content-center">
          <div class="card w-25 h-25 p-1 m-1 col-2" v-for="videoList in videoList" :key="videoList.id">
            <img v-on:click="videoRedirect(videoList.videoId)" :src="`${videoList.videoImg}`">
            <p class="card-text" v-on:click="videoRedirect(videoList.videoId)">{{ videoList.videoName }}</p>
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
      elasticUrl: process.env.elasticUrl,
      videoList: [],
      searchValue: "",
    }
  },
  async mounted() {
    let json = [];
    try {
      const response = await this.$axios.$get(this.apiUrl + '/videos?perPage=100');
      response.data.forEach(element => {
        json ["videoName"] = element.name;
        json ["videoImg"] = this.apiUrl + '/' + element.image;
        json ["videoId"] = element.id;
        this.videoList.push(json);
        json = [];
      });
    } catch (error) {

    }
  },
  methods: {
    videoRedirect(id) {
      sessionStorage.setItem("idVideo", id);
      this.$router.push({path: "/video"});
    },
    async startSearching() {
      let json = [];
      this.videoList = [];
      let value = {search};
      this.searchValue = value.search.value;
      try {
        console.log(this.searchValue);
        if (this.searchValue == "") {
           const response = await this.$axios.$get(this.elasticUrl + '/youtube/video/_search');
           response.hits.hits.forEach(element => {
              let tmp = element ["_source"];
              json ["videoName"] = tmp.name;
              json ["videoImg"] = this.apiUrl + '/' + tmp.image;
              json ["videoId"] = tmp.id;
              this.videoList.push(json);
              json = [];
          });
        }
        else {
           const response = await this.$axios.$get(this.elasticUrl + '/youtube/video/_search?q=' + this.searchValue + '*');
            response.hits.hits.forEach(element => {
              let tmp = element ["_source"];
              json ["videoName"] = tmp.name;
              json ["videoImg"] = this.apiUrl + '/' + tmp.image;
              json ["videoId"] = tmp.id;
              this.videoList.push(json);
              json = [];
          });
        }
       
        
        
      } catch (error) {

      }
    }
  }
}
</script>