<template>
  <div>
    <div>
      <Header/>
    </div>
    <div>
      <br>
      <br>
      <br>
      <br>
      <div class="container-fluid container-table">
        <div class="row d-flex flex-row">
          <div class="col-6 bg-info bg-white border-bottom border-dark">
            <div>
              <form onsubmit="return false">
                <div class="form-group">
                  <label for="username">Upload video</label>
                </div>
                <div class="form-group">
                  <label for="username">Name of video</label>
                  <input type="text" class="form-control" id="videoName" v-model="videoName" placeholder="Username">
                </div>
                <div class="form-group">
                  <label for="video">Video file</label>
                  <input type="file" class="form-control-file" multiple id="video" @change="file()">
                </div>
                <button class="btn btn-primary" v-on:click="uploadVideo">Submit</button>
              </form>
            </div>
          </div>
          <div class="col-6 bg-info w-100 bg-white border-left border-bottom border-dark">
            <form onsubmit="return false">
              <div class="form-group">
                <label for="username">Update account</label>
              </div>
              <div class="form-group">
                <label for="username">New Username</label>
                <input type="text" class="form-control" id="user" v-model="userName" placeholder="Username">
              </div>
              <div class="form-group">
                <label for="username">New Pseudo</label>
                <input type="text" class="form-control" id="pseudo" v-model="pseudo" placeholder="Pseudo">
              </div>
              <div class="form-group">
                <label for="email">New Email</label>
                <input type="email" class="form-control" id="email" aria-describedby="emailHelp" v-model="mail"
                       placeholder="Enter email">
              </div>
              <div class="form-group">
                <label for="userPassword">New Password</label>
                <input type="password" class="form-control" id="password" v-model="password" placeholder="Password">
              </div>
              <button class="btn btn-primary" v-on:click="updateAccount">Submit</button>
            </form>
            <br>
          </div>
          <br>
          <div class="row d-flex justify-content-center">
            <div class="col-4 m-5" v-for="items in 15" :key="items.videoName">
              <div class="m-3 d-flex flex-row">
                <img class="w-100 h-100 p-2 img-fluid img-thumbnail border-0" :src="`${currentVideo}`">
                <p class="p-2">test</p>
                <button type="button" class="btn btn-danger" v-on:click="removeVideo">remove</button>
              </div>

            </div>
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
      currentVideo: "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      pseudo: "",
      password: "",
      mail: "",
      userName: "",
      videoFile: '',
      videoName: "",
    }
  },
  async mounted() {
    let idUser = sessionStorage.getItem("id");
    let tokenUser = sessionStorage.getItem("token");
    this.$axios.defaults.headers.common["Authorization"] = 'Bearer ' + tokenUser;
    try {
      const response = await this.$axios.$get('http://localhost:8080/user/' + idUser);
      console.log("test : " + response);
    } catch (error) {
      console.log(error);
    }

  },
  methods: {
    file() {
      console.log(event.target.files);
    },
    updateAccount() {
      console.log("j'update le compte");
    },
    uploadVideo() {
      console.log("c'est parti pour l'upload");
    },
    removeVideo() {
      console.log("je remove la video");
    }

  }
}
</script>
