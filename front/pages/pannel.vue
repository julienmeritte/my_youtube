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
                  <input type="text" class="form-control" id="videoName" v-model="videoName" placeholder="name">
                </div>
                <div class="form-group">
                  <label for="video">Video file</label>
                  <input type="file" id="file" ref="file" v-on:change="file()"/>
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
          <div class="row d-flex">
            <div class="col-9 m-5 container" v-for="items in videoList" :key="items.videoName">
              <div class="d-inline p-2 d-flex justify-content-center">
                <img class="w-100 h-100 p-2 img-fluid img-thumbnail border-0" :src="`${items.videoLink}`">
                <p class="px-5" v-on:click="videoRedirect(items.videoId)">{{items.videoName}}</p>
                
                <button type="button" class="btn btn-danger w-50 h-100" :id="items.videoId" v-on:click="removeVideo(items.videoId)">remove</button>
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
      apiUrl: process.env.apiUrl,
      videoList: [],
      videoName: "",
      pseudo: "",
      password: "",
      mail: "",
      userName: "",
      videoFile: '',
    }
  },
  async mounted() {

    //get user info //
    let idUser = sessionStorage.getItem("id");
    let tokenUser = sessionStorage.getItem("token");
    try {
      const response = await this.$axios.$get(this.apiUrl + '/user/' + idUser, {
        headers: {
          Authorization: "Bearer " + tokenUser
        }
      });
      if (response.data.pseudo != null) {
        this.pseudo = response.data.pseudo;
      }
      this.userName = response.data.username;
      this.mail = response.data.email;
    } catch (error) {
      console.log(error);
    }
    // end get user info //
    // get video list //
    let arrayID = [];
    let json = [];
    try {
      const response = await this.$axios.$get(this.apiUrl + '/user/' + idUser + '/videos?perPage=100', {
        headers: {
          Authorization: "Bearer " + tokenUser
        }
      });
      response.data.forEach(element => {
        json["videoId"] = element.id;
        json["videoName"] = element.name.replace('.mp4' , '');
        json["videoLink"] = this.apiUrl + '/' + element.source;
        this.videoList.push(json);
        json = [];
      });
    } catch (error) {
      console.log(error);
    }
  },
  methods: {
    file() {
      this.videoFile = this.$refs.file.files[0];
    },
    async updateAccount() {
      try {
        const formData = new FormData();
        let id = sessionStorage.getItem("id");
        let tokenUser = sessionStorage.getItem("token");
        if (this.userName != "") {
          formData.append("username", this.userName);
        }
        if (this.password != "") {
          formData.append("password", this.password);
        }
        if (this.pseudo != "") {
          formData.append("pseudo", this.pseudo);
        }
        if (this.mail != "") {
          formData.append("email", this.mail);
        }
        const response = await this.$axios.$put(this.apiUrl + '/user/' + id, formData, {
          headers: {
            Authorization: "Bearer " + tokenUser
          }
        });
      } catch (error) {
        console.log(error);
      }
      location.reload();
    },
    async uploadVideo() {
      try {
        let idUser = sessionStorage.getItem("id");
        let tokenUser = sessionStorage.getItem("token");
        const formData = new FormData();
        this.videoName = this.changeChar(this.videoName);
        formData.append("name", this.videoName);
        formData.append("source", this.videoFile);
        const response = await this.$axios.$post(this.apiUrl + '/user/' + idUser + '/video', formData, {
          headers: {
            Authorization: "Bearer " + tokenUser
          }
        });
      } catch (error) {
        console.log(error);
      }
      alert("votre vidéo est en cours d'upload");
      location.reload();
    },
    async removeVideo(id) {
      console.log(id);
      try {
        let idUser = sessionStorage.getItem("id");
        let tokenUser = sessionStorage.getItem("token");
        const response = await this.$axios.$delete(this.apiUrl + '/video/' + id, {
          headers: {
            Authorization: "Bearer " + tokenUser
          }
        });
      } catch (error) {
        console.log(error);
      }
      location.reload();
    },
    changeChar(string) {
      let array = string.split('');
      let str = '';
      for (let index = 0; index < array.length; index++) {
        if (array[index] == ' ') {
          array[index] = '-';
        }
      }
      for (let index = 0; index < array.length; index++) {
        str += array[index];
      }
      return str;
    },
    videoRedirect(id) {
      sessionStorage.setItem("idVideo" , id);
      this.$router.push({path: "/video"})
    }
  }
}
</script>
