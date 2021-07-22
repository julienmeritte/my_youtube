<template>
  <div>
    <div>
        <Header />
    </div>
    <div>
    <div class="container">
        <form class="col-xs-4 col-xs-offset-4 pt-3" >
            <b-form-input size="sm" class="mr-sm-2" placeholder="Search" id="search"
                          @input="startSearching"></b-form-input>
            
          </form>
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
      searchValue: "",
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
      },
      async startSearching(e) {
        let json = [];
        this.videoList = [];
        let value = {search};
        this.searchValue = value.search.value;
        try {
          const response = await this.$axios.$get('http://elasticsearch:9200/youtube/video/_search');

          response.hits.hits.foreach(element => {
              let tmp = element ["_source"];
              json ["videoName"] = tmp.name;
              json ["videoImg"] = tmp.source;
              json ["videoId"] = tmp.id;
          });
        } catch (error) {
          
        }
      }
  }
}
</script>
